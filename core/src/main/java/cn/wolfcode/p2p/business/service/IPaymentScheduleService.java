package cn.wolfcode.p2p.business.service;

import com.github.pagehelper.PageInfo;

import cn.wolfcode.p2p.business.domain.PaymentSchedule;
import cn.wolfcode.p2p.business.query.PaymentScheduleQueryObject;

public interface IPaymentScheduleService {
	int save(PaymentSchedule paymentSchedule);
	int update(PaymentSchedule paymentSchedule);
	PaymentSchedule get(Long id);
	PageInfo queryPage(PaymentScheduleQueryObject qo);
	void returnMoney(Long id);
}
