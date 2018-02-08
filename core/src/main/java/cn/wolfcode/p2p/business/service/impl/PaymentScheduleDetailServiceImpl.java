package cn.wolfcode.p2p.business.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.wolfcode.p2p.business.domain.PaymentScheduleDetail;
import cn.wolfcode.p2p.business.mapper.PaymentScheduleDetailMapper;
import cn.wolfcode.p2p.business.service.IPaymentScheduleDetailService;

@Service@Transactional
public class PaymentScheduleDetailServiceImpl implements IPaymentScheduleDetailService{

	@Autowired
	private PaymentScheduleDetailMapper paymentScheduleDetailMapper;
	
	public int save(PaymentScheduleDetail paymentScheduleDetail) {
		return paymentScheduleDetailMapper.insert(paymentScheduleDetail) ;
	}

	public PaymentScheduleDetail get(Long id) {
		return paymentScheduleDetailMapper.selectByPrimaryKey(id);
	}

	public void updatePayDate(Long id, Date payDate) {
		paymentScheduleDetailMapper.updatePayDate(id,payDate);
	}
}
