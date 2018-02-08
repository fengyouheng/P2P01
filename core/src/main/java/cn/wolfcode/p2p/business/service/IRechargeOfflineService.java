package cn.wolfcode.p2p.business.service;

import com.github.pagehelper.PageInfo;

import cn.wolfcode.p2p.business.domain.RechargeOffline;
import cn.wolfcode.p2p.business.query.RechargeOfflineQueryObject;

public interface IRechargeOfflineService {
	int save(RechargeOffline rechargeOffline);
	int update(RechargeOffline rechargeOffline);
	RechargeOffline get(Long id);
	void apply(RechargeOffline rechargeOffline); //线下充值申请
	PageInfo queryPage(RechargeOfflineQueryObject qo);
	void audit(Long id, int state, String remark);
}
