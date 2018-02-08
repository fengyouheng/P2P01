package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.business.domain.Bid;

public interface IBidService {
	int save(Bid bid);
	int update(Bid bid);
	Bid get(Long id);
	//批量修改投标状态
	void updateState(Long bidRequestId, int bidRequestState);
}
