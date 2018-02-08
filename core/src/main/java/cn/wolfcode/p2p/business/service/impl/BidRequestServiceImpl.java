package cn.wolfcode.p2p.business.service.impl;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.base.util.BitStatesUtils;
import cn.wolfcode.p2p.base.util.UserContext;
import cn.wolfcode.p2p.business.domain.Bid;
import cn.wolfcode.p2p.business.domain.BidRequest;
import cn.wolfcode.p2p.business.domain.BidRequestAuditHistory;
import cn.wolfcode.p2p.business.domain.PaymentSchedule;
import cn.wolfcode.p2p.business.domain.PaymentScheduleDetail;
import cn.wolfcode.p2p.business.domain.SystemAccount;
import cn.wolfcode.p2p.business.mapper.BidRequestMapper;
import cn.wolfcode.p2p.business.query.BidRequestQueryObject;
import cn.wolfcode.p2p.business.service.IAccountFlowService;
import cn.wolfcode.p2p.business.service.IBidRequestAuditHistoryService;
import cn.wolfcode.p2p.business.service.IBidRequestService;
import cn.wolfcode.p2p.business.service.IBidService;
import cn.wolfcode.p2p.business.service.IPaymentScheduleDetailService;
import cn.wolfcode.p2p.business.service.IPaymentScheduleService;
import cn.wolfcode.p2p.business.service.ISystemAccountFlowService;
import cn.wolfcode.p2p.business.service.ISystemAccountService;
import cn.wolfcode.p2p.business.util.CalculatetUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by seemygo on 2018/1/26.
 */
@Service@Transactional
public class BidRequestServiceImpl implements IBidRequestService {
    @Autowired
    private BidRequestMapper bidRequestMapper;
    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IBidRequestAuditHistoryService bidRequestAuditHistoryService;
    @Autowired
    private IBidService bidService;
    @Autowired
    private IAccountFlowService accountFlowService;
    @Autowired
    private ISystemAccountService systemAccountService;
    @Autowired
    private ISystemAccountFlowService systemAccountFlowService;
    @Autowired
    private IPaymentScheduleService paymentSchduleService;
    @Autowired
    private IPaymentScheduleDetailService paymentScheduleDetailService;
    public int save(BidRequest bidRequest) {
        return bidRequestMapper.insert(bidRequest);
    }

    public int update(BidRequest bidRequest) {
        int count = bidRequestMapper.updateByPrimaryKey(bidRequest);
        if(count<=0){
            throw new RuntimeException("乐观锁异常,bidRequestId:"+bidRequest.getId());
        }
        return count;
    }

    public BidRequest get(Long id) {
        return bidRequestMapper.selectByPrimaryKey(id);
    }

    public boolean canApplyBorrow(Userinfo userinfo) {
        //判断用户是否填写基本资料,是否进行视频认证，实名认证,风控材料分数是否大于30分
        if(userinfo.getIsBasicInfo()&&//用户是否填写基本资料
                userinfo.getIsRealAuth()&&//实名认证
                userinfo.getIsVedioAuth()&&//视频认证
                userinfo.getScore()>= BidConst.CREDIT_BORROW_SCORE//风控材料分数是否大于30分
                ){
            return true;
        }
        return false;
    }

    public void apply(BidRequest bidRequest) {
        Userinfo userinfo = userinfoService.getCurrent();
        Account account = accountService.getCurrent();
        if(this.canApplyBorrow(userinfo)&& //1.判断当前用户是否具备借款的资格
                !userinfo.gethasBidRequestProcess()&& //2.该用户是没有借款的流程
                bidRequest.getBidRequestAmount().compareTo(BidConst.SMALLEST_BIDREQUEST_AMOUNT)>=0&&//用户借款金额>=系统的最小借款金额
                bidRequest.getBidRequestAmount().compareTo(account.getRemainBorrowLimit())<=0&&//用户借款金额<=用户的剩余授信额度
                bidRequest.getCurrentRate().compareTo(BidConst.SMALLEST_CURRENT_RATE)>=0&&//借款利率>=系统最小借款年化利率
                bidRequest.getCurrentRate().compareTo(BidConst.MAX_CURRENT_RATE)<=0&&//借款利率<=系统最大借款年化利率
                bidRequest.getMinBidAmount().compareTo(BidConst.SMALLEST_BID_AMOUNT)>=0// //用户设定的最小投标>=系统最小投标
                ){
            //创建BidRequest对象,设置相关属性
            BidRequest br = new BidRequest();
            br.setApplyTime(new Date());//借款申请时间
            br.setBidRequestAmount(bidRequest.getBidRequestAmount());//用户的借款金额
            br.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_PENDING);//借款对象的状态(待发布)
            br.setBidRequestType(BidConst.BIDREQUEST_TYPE_NORMAL);//借款的类型(信用标)
            br.setCreateUser(UserContext.getCurrent());//借款人
            br.setCurrentRate(bidRequest.getCurrentRate());//借款利率
            br.setDescription(bidRequest.getDescription());//借款的描述
            br.setDisableDays(bidRequest.getDisableDays());//招标天数
            br.setMinBidAmount(bidRequest.getMinBidAmount());//投标的最小金额
            br.setMonthes2Return(bidRequest.getMonthes2Return());//还款期数
            br.setReturnType(BidConst.RETURN_TYPE_MONTH_INTEREST_PRINCIPAL);//还款的方式(按月分期(等额本息))
            br.setTitle(bidRequest.getTitle());//借款的标题
            br.setTotalRewardAmount(CalculatetUtil.calTotalInterest(br.getReturnType(),br.getBidRequestAmount(),br.getCurrentRate(),br.getMonthes2Return()));
            this.save(br);
            //找到当前登录用户的userinfo，给用户添加正在借款的状态码
            userinfo.addState(BitStatesUtils.HAS_BIDREQUEST_PROCESS);
            userinfoService.update(userinfo);
        }
    }


    public PageInfo queryPage(BidRequestQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());
        List list = bidRequestMapper.queryPage(qo);
        return new PageInfo(list);
    }


    public void publishAudit(Long id, int state, String remark) {
        //1.条件判断
        //根据id查询BidRequest对象,判断借款对象是否处于待审核的状态
        BidRequest bidRequest = this.get(id);
        if(bidRequest!=null && bidRequest.getBidRequestState() == BidConst.BIDREQUEST_STATE_PUBLISH_PENDING){
            //创建审核历史对象.(设置审核人，审核时间,审核备注)
            BidRequestAuditHistory brah = new BidRequestAuditHistory();
            brah.setApplier(bidRequest.getCreateUser());
            brah.setApplyTime(bidRequest.getApplyTime());
            brah.setAuditor(UserContext.getCurrent());
            brah.setAuditTime(new Date());
            brah.setRemark(remark);
            brah.setBidRequestId(bidRequest.getId());
            brah.setAuditType(BidRequestAuditHistory.PUBLISH_AUDIT);
            if(state==BidRequestAuditHistory.STATE_PASS){
                //审核通过?
                //  招标截止时间,标的发布时间,标的状态(招标),标的风控意见
                brah.setState(BidRequestAuditHistory.STATE_PASS);
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_BIDDING);
                bidRequest.setPublishTime(new Date());
                bidRequest.setNote(remark);
                bidRequest.setDisableDate(DateUtils.addDays(bidRequest.getPublishTime(),bidRequest.getDisableDays()));

            }else{
                //审核拒绝?

                brah.setState(BidRequestAuditHistory.STATE_REJECT);
                //  标的状态(发标前审核拒绝
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_REFUSE);
                //  找到申请人,移除借款状态码
                Userinfo createUserUserinfo = userinfoService.get(bidRequest.getCreateUser().getId());
                createUserUserinfo.removeState(BitStatesUtils.HAS_BIDREQUEST_PROCESS);
                userinfoService.update(createUserUserinfo);
            }
            bidRequestAuditHistoryService.save(brah);
            this.update(bidRequest);


        }

    }


    public List<BidRequest> queryIndexList(BidRequestQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());
        List list = bidRequestMapper.queryPage(qo);
        return list;
    }


    public void bid(Long bidRequestId, BigDecimal amount) {
        //1.哪些限制条件?
        BidRequest bidRequest = this.get(bidRequestId);
        if(bidRequest!=null &&bidRequest.getBidRequestState()==BidConst.BIDREQUEST_STATE_BIDDING){
            if(!UserContext.getCurrent().getId().equals(bidRequest.getCreateUser().getId())){
                //判断当前登录的用户是否借款人
                Account account  = accountService.getCurrent();
                //根据id获取bidRequest对象,判断是否处于招标中.
                if(amount.compareTo(bidRequest.getMinBidAmount())>=0&&//投标金额>=借款设置最小投标金额
                        amount.compareTo(account.getUsableAmount().min(bidRequest.getRemainAmount()))<=0//投标金额<=MIN(账户可用金额,该标剩余金额)
                        ){

                    //投标钱？用户？
                    //标,投标次数,目前所投的钱增加
                    bidRequest.setBidCount(bidRequest.getBidCount()+1);
                    bidRequest.setCurrentSum(bidRequest.getCurrentSum().add(amount));
                    //--------------------------------
                    //标属性？
                    Bid bid = new Bid();
                    bid.setActualRate(bidRequest.getCurrentRate());//借款的年华利率
                    bid.setAvailableAmount(amount);//投标基恩
                    bid.setBidRequestId(bidRequest.getId());//关联哪个借款
                    bid.setBidRequestTitle(bidRequest.getTitle());//借款标题
                    bid.setBidTime(new Date());
                    bid.setBidUser(UserContext.getCurrent());
                    bid.setBidRequestState(bidRequest.getBidRequestState());
                    bidService.save(bid);
                    //投标的对象
                    //投资人账户？
                    account.setUsableAmount(account.getUsableAmount().subtract(amount));
                    account.setFreezedAmount(account.getFreezedAmount().add(amount));
                    accountService.update(account);
                    //可用金额减少,冻结金额增加
                    accountFlowService.creatBidFlow(account,amount);
                    //生成投标的流水
                    //怎么判断标已经投满呢?
                    //当前已投的金额==借款的金额
                    if(bidRequest.getCurrentSum().compareTo(bidRequest.getBidRequestAmount())==0){
                        //如果投满,借款对象和投标对象哪些属性需要变化?
                        bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1);
                        //借款对象和投标对象的bidRequestState---->满标一审的状态
                        bidService.updateState(bidRequest.getId(),BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1);
                    }
                    this.update(bidRequest);

                }
            }

        }


    }


	public void audit1(Long id, int state, String remark) {
		//1.根据id获取bidRequest对象,处于满标一审状态.
		BidRequest bidRequest = this.get(id);
		if(bidRequest!=null && bidRequest.getBidRequestState() == BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1){
			//2.创建审核历史对象并设置相关的参数
			createBidRequestAuditHistory(BidRequestAuditHistory.AUDIT1,state,remark,bidRequest);
			if(state==BidRequestAuditHistory.STATE_PASS){
				//3.如果审核通过
				//	借款对象的状态和投标对象的状态----->满标二审
				bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2);
				bidService.updateState(bidRequest.getId(), BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2);
			}else{
				auditReject(bidRequest);
			}
			this.update(bidRequest);
			
		}
		
	}

	private void auditReject(BidRequest bidRequest) {
		//4.如果审核拒绝
		//	借款对象的状态和投标对象的状态----->满审拒绝\
		bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_REJECTED);
		bidService.updateState(bidRequest.getId(), BidConst.BIDREQUEST_STATE_REJECTED);
		//	遍历投标集合,获取到投资人的账户
		Long bidUserId;
		Account bidUserAccount;
		Map<Long, Account> accountMap = new HashMap<Long, Account>();
		for(Bid bid:bidRequest.getBids()){
			bidUserId = bid.getBidUser().getId();
			bidUserAccount = accountMap.get(bidUserId);
			if(bidUserAccount==null){
				bidUserAccount = accountService.get(bidUserId);
				accountMap.put(bidUserId, bidUserAccount);
			}
//					冻结金额减少，可用金额增加.
			bidUserAccount.setFreezedAmount(bidUserAccount.getFreezedAmount().subtract(bid.getAvailableAmount()));
			bidUserAccount.setUsableAmount(bidUserAccount.getUsableAmount().add(bid.getAvailableAmount()));
//					生成投标失败的流水
			accountFlowService.creatBidFailedFlow(bidUserAccount,bid.getAvailableAmount());
		}
		//对账户进行统一的修改
		for(Account account:accountMap.values()){
			accountService.update(account);
		}
		//	找到借款人的userinfo，移除借款的状态码.
		Userinfo createUserUserinfo = userinfoService.get(bidRequest.getCreateUser().getId());
		createUserUserinfo.removeState(BitStatesUtils.HAS_BIDREQUEST_PROCESS);
		userinfoService.update(createUserUserinfo);
	}

	private void createBidRequestAuditHistory(int auditType,int state,String remark,BidRequest bidRequest) {
		BidRequestAuditHistory brah = new BidRequestAuditHistory();
		brah.setApplier(bidRequest.getCreateUser());
		brah.setApplyTime(new Date());
		brah.setAuditor(UserContext.getCurrent());
		brah.setAuditTime(new Date());
		brah.setBidRequestId(bidRequest.getId());
		brah.setRemark(remark);
		brah.setAuditType(auditType);
		if(state==BidRequestAuditHistory.STATE_PASS){
			brah.setState(BidRequestAuditHistory.STATE_PASS);
		}else{
			brah.setState(BidRequestAuditHistory.STATE_REJECT);
		}
		bidRequestAuditHistoryService.save(brah);
	}


	public void audit2(Long id, int state, String remark) {
		//1.有哪些限制条件
		//根据id获取bidRequest对象,处于满标二审状态
		BidRequest bidRequest =  this.get(id);
		if(bidRequest!=null && bidRequest.getBidRequestState() == BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2){
			//2.需要创建审核对象?
			createBidRequestAuditHistory(BidRequestAuditHistory.AUDIT2,state,remark,bidRequest);
			if(state==BidRequestAuditHistory.STATE_PASS){
				//3.如果审核通过
				//	对于借款对象和投标对象有哪些变化?----》还款中
				bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_PAYING_BACK);
				bidService.updateState(bidRequest.getId(), BidConst.BIDREQUEST_STATE_PAYING_BACK);
				//	对借款人有哪些变化?
				Account createUserAccount = accountService.get(bidRequest.getCreateUser().getId());
				//		可用金额增加,待还本息增加,剩余授信额度减少
				//借款人的可用金额=借款人的原可用金额+借款金额
				createUserAccount.setUsableAmount(createUserAccount.getUsableAmount().add(bidRequest.getBidRequestAmount()));
				//借款人的待还金额=借款人的原待还金额+借款金额+这次借款利息
				createUserAccount.setUnReturnAmount(createUserAccount.getUnReturnAmount().add(bidRequest.getBidRequestAmount().add(bidRequest.getTotalRewardAmount())));
				//借款人的剩余授信额度=借款人的原剩余授信额度-借款金额
				createUserAccount.setRemainBorrowLimit(createUserAccount.getRemainBorrowLimit().subtract(bidRequest.getBidRequestAmount()));
				//		生成借款成功的流水
				accountFlowService.createBorrowSuccessFlow(createUserAccount,bidRequest.getBidRequestAmount());
				//		移除申请人的借款状态码
				Userinfo createUserUserinfo = userinfoService.get(bidRequest.getCreateUser().getId());
				createUserUserinfo.removeState(BitStatesUtils.HAS_BIDREQUEST_PROCESS);
				userinfoService.update(createUserUserinfo);
				//		支付平台借款手续费.(5%借款手续费)
				BigDecimal accountManagementCharge = CalculatetUtil.calAccountManagementCharge(bidRequest.getBidRequestAmount());
				//		借款人的可用金额减少，生成支付平台借款手续费的流水
				createUserAccount.setUsableAmount(createUserAccount.getUsableAmount().subtract(accountManagementCharge));
				accountFlowService.createPayAccountManagerChargeFlow(createUserAccount,accountManagementCharge);
				accountService.update(createUserAccount);
				//		系统账户手续借款手续费(TODO)
				SystemAccount systemAccount = systemAccountService.getCurrent();
				systemAccount.setUsableAmount(systemAccount.getUsableAmount().add(accountManagementCharge));
				systemAccountService.update(systemAccount);
				systemAccountFlowService.createGainAccountManagerChargeFlow(systemAccount,accountManagementCharge);
				//		生成系统账户收取借款手续费的流水
				//	对于投资人有哪些变化?
				//		遍历投标的集合,获取到投资人的账户
				Long bidUserId;
				Account bidUserAccount;
				Map<Long, Account> accountMap = new HashMap<Long, Account>();
				for(Bid bid:bidRequest.getBids()){
					bidUserId = bid.getBidUser().getId();
					bidUserAccount = accountMap.get(bidUserId);
					if(bidUserAccount==null){
						bidUserAccount = accountService.get(bidUserId);
						accountMap.put(bidUserId, bidUserAccount);
					}
					//冻结金额减少,待收本金和待收利息增加(TODO)
					bidUserAccount.setFreezedAmount(bidUserAccount.getFreezedAmount().subtract(bid.getAvailableAmount()));
					//生成投标成功的流水
					accountFlowService.createBidSuccessFlow(bidUserAccount,bid.getAvailableAmount());
				}
				//	还款对象和还款明细对象？
				List<PaymentSchedule> pSchedules = createPaymentScheduleList(bidRequest);
				//计算投资人的待收利息和待收本金
				for(PaymentSchedule ps:pSchedules){
					for(PaymentScheduleDetail psd:ps.getDetails()){
						bidUserAccount = accountMap.get(psd.getInvestorId());
						bidUserAccount.setUnReceivePrincipal(bidUserAccount.getUnReceivePrincipal().add(psd.getPrincipal()));
						bidUserAccount.setUnReceiveInterest(bidUserAccount.getUnReceiveInterest().add(psd.getInterest()));
					}
				}
				//对账户进行统一的修改
				for(Account account:accountMap.values()){
					accountService.update(account);
				}
			}else{
				//4.如果审核拒绝
				//	和满标一审一样
				auditReject(bidRequest);
			}
			this.update(bidRequest);
		}
		
	}

	private List<PaymentSchedule> createPaymentScheduleList(BidRequest bidRequest) {
		List<PaymentSchedule> pSchedules  = new ArrayList<PaymentSchedule>();
		PaymentSchedule ps;
		BigDecimal principalTemp = BigDecimal.ZERO;
		BigDecimal interestTemp = BigDecimal.ZERO;
		for(int i=0;i<bidRequest.getMonthes2Return();i++){
			ps = new PaymentSchedule();
			ps.setBidRequestId(bidRequest.getId());//关联借款的id
			ps.setBidRequestTitle(bidRequest.getTitle());//关联借款的标题
			ps.setBidRequestType(bidRequest.getBidRequestType());//借款的类型(信用标)
			ps.setBorrowUser(bidRequest.getCreateUser());//关联借款人
			ps.setMonthIndex(i+1);//是第几个月的还款对象
			ps.setDeadLine(DateUtils.addMonths(bidRequest.getPublishTime(), i+1));//还款的截止时间(标的发布时间+期数*月)
			ps.setReturnType(bidRequest.getReturnType());
			//ps.setInterest(interest)
			//ps.setPrincipal()
			//ps.setTotalAmount()
			//判断是否最后一期还款
			if(i<bidRequest.getMonthes2Return()-1){
				//不是最后一期
				ps.setTotalAmount(CalculatetUtil.calMonthToReturnMoney(bidRequest.getReturnType(), //还款方式(按月分期)
						bidRequest.getBidRequestAmount(),//还款金额
						bidRequest.getCurrentRate(), //还款的年华利率
						i+1, //第几期的还款
						bidRequest.getMonthes2Return()));//还款期数
				ps.setInterest(CalculatetUtil.calMonthlyInterest(bidRequest.getReturnType(), 
						bidRequest.getBidRequestAmount(), 
						bidRequest.getCurrentRate(), 
						i+1, 
						bidRequest.getMonthes2Return()));
				//该期本金=该期还款金额-该期利息
				ps.setPrincipal(ps.getTotalAmount().subtract(ps.getInterest()));
				interestTemp = interestTemp.add(ps.getInterest());
				principalTemp = principalTemp.add(ps.getPrincipal());
			}else{
				//是最后一期
				//该期本金=借款的本金-前N-1期的本金之和
				ps.setPrincipal(bidRequest.getBidRequestAmount().subtract(principalTemp));
				//该期利息=借款的利息-前N-1期的利息之和
				ps.setInterest(bidRequest.getTotalRewardAmount().subtract(interestTemp));
				//该期还款金额=该期本金+该期利息
				ps.setTotalAmount(ps.getInterest().add(ps.getPrincipal()));
			}
			paymentSchduleService.save(ps);
			createPaymentSchduleDetail(ps,bidRequest);
			pSchedules.add(ps);
		}
		return pSchedules;
	}

	private void createPaymentSchduleDetail(PaymentSchedule ps,BidRequest bidRequest) {
		PaymentScheduleDetail psd;
		Bid bid;
		BigDecimal principalTemp = BigDecimal.ZERO;
		BigDecimal interestTemp = BigDecimal.ZERO;
		for(int i=0;i<bidRequest.getBids().size();i++){
			bid = bidRequest.getBids().get(i);
			psd = new PaymentScheduleDetail();
			psd.setBidAmount(bid.getAvailableAmount());//还款明细对象设置投标金额
			psd.setBidId(bid.getId());//关联投标对象
			psd.setBidRequestId(bidRequest.getId());//关联借款对象
			psd.setBorrowUser(bidRequest.getCreateUser());//管理借款人
			psd.setDeadLine(ps.getDeadLine());//设置还款截止时间
			psd.setInvestorId(bid.getBidUser().getId());//关联投资人id
			psd.setMonthIndex(ps.getMonthIndex());//第几期的还款明细对象
			psd.setPaymentScheduleId(ps.getId());//关联还款对象
			psd.setReturnType(ps.getReturnType());//还款方式
			
			//psd.setTotalAmount(totalAmount)
			//psd.setInterest(interest)
			//psd.setPrincipal(principal)
			//判断是否最后一个投标人
			if(i<bidRequest.getBids().size()-1){
				//不是最后一个投标人
				BigDecimal bidRate = bid.getAvailableAmount().divide(bidRequest.getBidRequestAmount(),BidConst.CAL_SCALE,RoundingMode.HALF_UP);
				//该还款明细的本金=(该次投标/借款金额)*该期还款的本金
				psd.setPrincipal(bidRate.multiply(ps.getPrincipal()).setScale(BidConst.STORE_SCALE,RoundingMode.HALF_UP));
				//该还款明细的利息=(该次投标/借款金额)*该期还款的利息
				psd.setInterest(bidRate.multiply(ps.getInterest()).setScale(BidConst.STORE_SCALE,RoundingMode.HALF_UP));
				//该还款明细的总金额=该还款明细的本金+该还款明细的利息
				psd.setTotalAmount(psd.getInterest().add(psd.getPrincipal()));
				principalTemp = principalTemp.add(psd.getPrincipal());
				interestTemp = interestTemp.add(psd.getInterest());
			}else{
				//是最后一个投标人
				//该还款明细的本金=该期还款的本金-(前N-1个投资人的本金之和)
				psd.setPrincipal(ps.getPrincipal().subtract(principalTemp));
				//该还款明细的利息=该期还款的利息-(前N-1个投资人的利息之和)
				psd.setInterest(ps.getInterest().subtract(interestTemp));
				//该还款明细的总金额=该还款明细的本金+该还款明细的利息
				psd.setTotalAmount(psd.getInterest().add(psd.getPrincipal()));
			}
			
			paymentScheduleDetailService.save(psd);
			ps.getDetails().add(psd);
		}
	}
}
