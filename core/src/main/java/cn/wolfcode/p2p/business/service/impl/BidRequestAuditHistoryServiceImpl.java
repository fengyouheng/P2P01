package cn.wolfcode.p2p.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.wolfcode.p2p.business.domain.BidRequestAuditHistory;
import cn.wolfcode.p2p.business.mapper.BidRequestAuditHistoryMapper;
import cn.wolfcode.p2p.business.service.IBidRequestAuditHistoryService;

@Service@Transactional
public class BidRequestAuditHistoryServiceImpl implements IBidRequestAuditHistoryService{

	@Autowired
	private BidRequestAuditHistoryMapper bidRequestAuditHistoryMapper;
	
	public int save(BidRequestAuditHistory bidRequestAuditHistory) {
		
		return bidRequestAuditHistoryMapper.insert(bidRequestAuditHistory);
	}

	public List<BidRequestAuditHistory> queryByBidRequestId(Long bidRequestId) {
		return bidRequestAuditHistoryMapper.queryByBidRequestId(bidRequestId);
	}
}
