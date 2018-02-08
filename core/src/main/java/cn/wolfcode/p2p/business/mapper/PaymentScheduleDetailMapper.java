package cn.wolfcode.p2p.business.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import cn.wolfcode.p2p.business.domain.PaymentScheduleDetail;

public interface PaymentScheduleDetailMapper {

    int insert(PaymentScheduleDetail paymentScheduleDetail);

    PaymentScheduleDetail selectByPrimaryKey(Long id);

	void updatePayDate(@Param("id")Long id,@Param("payDate")Date payDate);

}