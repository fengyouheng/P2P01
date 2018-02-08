package cn.wolfcode.p2p.business.domain;

import java.math.BigDecimal;
import java.util.Date;

import cn.wolfcode.p2p.base.domain.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountFlow extends BaseDomain {

	private Long accountId;// 流水是关于哪个账户的
	private BigDecimal amount;// 这次账户发生变化的金额
	private Date tradeTime;// 这次账户发生变化的时间
	private int actionType;// 资金变化类型
	private BigDecimal usableAmount;// 发生变化之后的可用余额;
	private BigDecimal freezedAmount;// 发生变化之后的冻结金额;
	private String remark;//账户流水说明

}
