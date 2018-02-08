package cn.wolfcode.p2p.business.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.business.domain.AccountFlow;
import cn.wolfcode.p2p.business.mapper.AccountFlowMapper;
import cn.wolfcode.p2p.business.service.IAccountFlowService;

@Service@Transactional
public class AccountFlowServiceImpl implements IAccountFlowService{

	@Autowired
	private AccountFlowMapper accountFlowMapper;
	
	public int save(AccountFlow accountFlow) {
		return accountFlowMapper.insert(accountFlow);
	}

	public void creatRechargeOfflineFlow(Account account,BigDecimal amount) {
		creatFlow(account,amount,BidConst.ACCOUNT_ACTIONTYPE_RECHARGE_OFFLINE,"线下充值成功"+amount+"元");
	}

	public void creatBidFlow(Account account, BigDecimal amount) {
		creatFlow(account,amount,BidConst.ACCOUNT_ACTIONTYPE_BID_FREEZED,"投标冻结"+amount+"元");
	}
	public void creatFlow(Account account,BigDecimal amount,int actionType,String remark){
		AccountFlow flow = new AccountFlow();
		flow.setAccountId(account.getId());
		flow.setAmount(amount);
		flow.setTradeTime(new Date());
		flow.setUsableAmount(account.getUsableAmount());
		flow.setFreezedAmount(account.getFreezedAmount());
		flow.setActionType(BidConst.ACCOUNT_ACTIONTYPE_RECHARGE_OFFLINE);
		flow.setRemark("线下充值成功:"+amount+"元");
		this.save(flow);
	}

	public void creatBidFailedFlow(Account account,BigDecimal amount) {
		creatFlow(account, amount, BidConst.ACCOUNT_ACTIONTYPE_BID_UNFREEZED, "投标失败,取消冻结:"+amount+"元");
	}

	public void createBorrowSuccessFlow(Account account, BigDecimal amount) {
		creatFlow(account, amount, BidConst.ACCOUNT_ACTIONTYPE_BIDREQUEST_SUCCESSFUL, "借款成功:"+amount+"元");
	}

	public void createPayAccountManagerChargeFlow(Account account,BigDecimal amount) {
		creatFlow(account, amount, BidConst.ACCOUNT_ACTIONTYPE_CHARGE, "支付平台借款手续费:"+amount+"元");
	}

	public void createBidSuccessFlow(Account account, BigDecimal amount) {
		creatFlow(account, amount, BidConst.ACCOUNT_ACTIONTYPE_BID_SUCCESSFUL, "投标成功:"+amount+"元");
	}

	public void creatReturnMoneyFlow(Account account, BigDecimal amount) {
		creatFlow(account, amount, BidConst.ACCOUNT_ACTIONTYPE_RETURN_MONEY, "还款成功:"+amount+"元");
	}

	public void createGainPrincipalAndInterestFlow(Account account,BigDecimal amount) {
		creatFlow(account, amount, BidConst.ACCOUNT_ACTIONTYPE_CALLBACK_MONEY, "回款成功:"+amount+"元");
	}

	public void createPayInterestManagerChargeFlow(Account account,BigDecimal amount) {
		creatFlow(account, amount, BidConst.ACCOUNT_ACTIONTYPE_INTEREST_SHARE, "支付利息管理费成功:"+amount+"元");
	}
}
