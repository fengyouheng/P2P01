package cn.wolfcode.p2p.business.service;

import java.math.BigDecimal;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.business.domain.AccountFlow;

public interface IAccountFlowService {
	int save(AccountFlow accountFlow);
	//充值成功的流水
	void creatRechargeOfflineFlow(Account applierAccount, BigDecimal amount);
	//投标的流水
	void creatBidFlow(Account account, BigDecimal amount);
	//投标失败的流水
	void creatBidFailedFlow(Account bidUserAccount, BigDecimal availableAmount);
	void createBorrowSuccessFlow(Account account,BigDecimal amount); //借款成功的流水
	void createPayAccountManagerChargeFlow(Account account,BigDecimal amount);//支付平台借款手续费
	void createBidSuccessFlow(Account account, BigDecimal amount);//投标成功的流水
	void creatReturnMoneyFlow(Account account, BigDecimal amount);//还款流水
	void createGainPrincipalAndInterestFlow(Account account,BigDecimal amount);//回款流水
	void createPayInterestManagerChargeFlow(Account account,BigDecimal interestManagerCharge);
}
