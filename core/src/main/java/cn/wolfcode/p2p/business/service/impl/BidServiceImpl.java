package cn.wolfcode.p2p.business.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.wolfcode.p2p.business.domain.Bid;
import cn.wolfcode.p2p.business.mapper.BidMapper;
import cn.wolfcode.p2p.business.service.IBidService;

@Service@Transactional
public class BidServiceImpl implements IBidService{

	@Autowired
	private BidMapper bidMapper;
	public int save(Bid bid) {
		return bidMapper.insert(bid);
	}

	public int update(Bid bid) {
		int count = bidMapper.updateByPrimaryKey(bid);
		if(count<=0){
			throw new RuntimeException("乐观锁异常,bidId:"+bid.getId());
		}
		return count ;
	}

	public Bid get(Long id) {
		return bidMapper.selectByPrimaryKey(id);
	}

	public void updateState(Long bidRequestId, int bidRequestState) {
		bidMapper.updateState(bidRequestId,bidRequestState);
	}

}
