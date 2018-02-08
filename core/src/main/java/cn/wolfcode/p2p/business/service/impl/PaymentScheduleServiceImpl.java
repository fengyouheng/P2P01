package cn.wolfcode.p2p.business.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.base.util.UserContext;
import cn.wolfcode.p2p.business.domain.BidRequest;
import cn.wolfcode.p2p.business.domain.PaymentSchedule;
import cn.wolfcode.p2p.business.domain.PaymentScheduleDetail;
import cn.wolfcode.p2p.business.domain.SystemAccount;
import cn.wolfcode.p2p.business.mapper.PaymentScheduleMapper;
import cn.wolfcode.p2p.business.query.PaymentScheduleQueryObject;
import cn.wolfcode.p2p.business.service.IAccountFlowService;
import cn.wolfcode.p2p.business.service.IBidRequestService;
import cn.wolfcode.p2p.business.service.IBidService;
import cn.wolfcode.p2p.business.service.IPaymentScheduleDetailService;
import cn.wolfcode.p2p.business.service.IPaymentScheduleService;
import cn.wolfcode.p2p.business.service.ISystemAccountFlowService;
import cn.wolfcode.p2p.business.service.ISystemAccountService;
import cn.wolfcode.p2p.business.util.CalculatetUtil;

@Service@Transactional
public class PaymentScheduleServiceImpl implements IPaymentScheduleService{

	@Autowired
	private PaymentScheduleMapper paymentScheduleMapper;
	@Autowired
	private IAccountService accountService;
	@Autowired
	private ISystemAccountService systemAccountService;
	@Autowired
	private ISystemAccountFlowService systemAccountFlowService;
	@Autowired
	private IAccountFlowService accountFlowService;
	@Autowired
	private IPaymentScheduleDetailService paymentScheduleDetailService;
	@Autowired
	private IBidService bidService;
	@Autowired
	private IBidRequestService bidRequestService;
	public int save(PaymentSchedule paymentSchedule) {
		return paymentScheduleMapper.insert(paymentSchedule);
	}

	public int update(PaymentSchedule paymentSchedule) {
		return paymentScheduleMapper.updateByPrimaryKey(paymentSchedule);
	}

	public PaymentSchedule get(Long id) {
		return paymentScheduleMapper.selectByPrimaryKey(id);
	}

	public PageInfo queryPage(PaymentScheduleQueryObject qo) {
		PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());
		List list = paymentScheduleMapper.queryPage(qo);
		return new PageInfo(list);
	}

	public void returnMoney(Long id) {
		//1 对于还款有哪些的限制条件
		PaymentSchedule ps = this.get(id);
		// 获取到还款对象,还款处于正常待还的情况
		if(ps !=null && ps.getState()==BidConst.PAYMENT_STATE_NORMAL){
			// 当前登录的用户是还款人
			if(UserContext.getCurrent().getId().equals(ps.getBorrowUser().getId())){
				// 当前登录用户的可用金额>=该期还款金额
				Account account = accountService.get(id);
				if(account.getUsableAmount().compareTo(ps.getTotalAmount())>=0){
					//2 对于还款对象和还款明细对象有哪些属性需要设置
					// 还款对象设置的状态   已还
					ps.setState(BidConst.PAYMENT_STATE_DONE);
					// 还款对象设置还款日期
					ps.setPayDate(new Date());
					this.update(ps);
					// 还款明细对象设置还款日志
					paymentScheduleDetailService.updatePayDate(ps.getId(),ps.getPayDate());
					//3 对于投资人的账户信息有哪些变化
					// 可用金额减少,待还金额减少,授信额度增加(还款的本金)
					account.setUsableAmount(account.getUsableAmount().subtract(ps.getTotalAmount()));
					account.setUnReturnAmount(account.getUnReturnAmount().subtract(ps.getTotalAmount()));
					account.setRemainBorrowLimit(account.getRemainBorrowLimit().subtract(ps.getPrincipal()));
					// 生成还款成功的流水
					accountFlowService.creatReturnMoneyFlow(account,ps.getTotalAmount());
					//4 对于投资人的账户信息有哪些变化
					Long bidUserId;
					Account bidUserAccount;
					Map<Long,Account>accountMap = new HashMap<Long,Account>();
					BigDecimal InterestManagerCharge ;
					SystemAccount systemAccount = systemAccountService.getCurrent();
					for (PaymentScheduleDetail psd : ps.getDetails()) {
						bidUserId  = psd.getInvestorId();
						bidUserAccount = accountMap.get(bidUserId);
						if(bidUserAccount == null){
							bidUserAccount = accountService.get(bidUserId);
							accountMap.put(bidUserId, bidUserAccount);
						}
					// 可用金额增加,代收本金和代收利息减少
					bidUserAccount.setUsableAmount(bidUserAccount.getUsableAmount().add(psd.getTotalAmount()));
					bidUserAccount.setUnReceivePrincipal(bidUserAccount.getUnReceivePrincipal().subtract(psd.getPrincipal()));
					bidUserAccount.setUnReceiveInterest(bidUserAccount.getUnReceiveInterest().subtract(psd.getInterest()));
					// 生成还款成功的流水
					accountFlowService.createGainPrincipalAndInterestFlow(bidUserAccount,psd.getTotalAmount());
					// 投资人支付利息管理费(利息的10%),投资人可用金额减少
					InterestManagerCharge = CalculatetUtil.calInterestManagerCharge(psd.getInterest());
					bidUserAccount.setUsableAmount(bidUserAccount.getUsableAmount().subtract(InterestManagerCharge));
					// 生成支付利息管理费的流水
					accountFlowService.createPayInterestManagerChargeFlow(bidUserAccount,InterestManagerCharge);
					// 平台账户收取利息管理费
					// 生成平台账户收取利息管理费的流水
					systemAccount.setUsableAmount(systemAccount.getUsableAmount().subtract(InterestManagerCharge));
					systemAccountFlowService.createGainInterestManagerChargeFlow(systemAccount,InterestManagerCharge);
					}
					//统一对账户进行修改
					for(Account accountTmp:accountMap.values()){
						accountService.update(accountTmp);
					}
					systemAccountService.update(systemAccount);
					//5 怎么判断这次的借款所有的还款都已经还清了
					List<PaymentSchedule> psSchedules = paymentScheduleMapper.queryByBidRequestId(ps.getBidRequestId());
					// 通过借款的id获取到该借款的所有的还款,判断所有的还款对象状态是否变成已还状态
					for (PaymentSchedule psSchedule : psSchedules) {
						if(psSchedule.getState() != BidConst.PAYMENT_STATE_DONE){
							return;
						}
					}
					//6 如果借款都已经还清了,对于借款对象和投标对象哪些属性需要设置
					// 借款对象和投标对象的状态修改成  已还清
					BidRequest bidRequest = bidRequestService.get(ps.getBidRequestId());
					bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK);
					bidRequestService.update(bidRequest);
					bidService.updateState(ps.getBidRequestId(),BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK);
				}
			}
		}
	}
}
