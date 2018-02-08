package cn.wolfcode.p2p.business.service;

import java.util.Date;

import cn.wolfcode.p2p.business.domain.PaymentScheduleDetail;

public interface IPaymentScheduleDetailService {
	int save(PaymentScheduleDetail paymentScheduleDetail);
	PaymentScheduleDetail get(Long id);
	void updatePayDate(Long id, Date payDate);
}
