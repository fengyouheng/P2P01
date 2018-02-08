package cn.wolfcode.p2p.business.mapper;

import cn.wolfcode.p2p.business.domain.RechargeOffline;
import cn.wolfcode.p2p.business.query.RechargeOfflineQueryObject;

import java.util.List;

public interface RechargeOfflineMapper {

    int insert(RechargeOffline record);

    RechargeOffline selectByPrimaryKey(Long id);

    int updateByPrimaryKey(RechargeOffline record);

	List queryPage(RechargeOfflineQueryObject qo);
}