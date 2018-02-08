package cn.wolfcode.p2p.business.mapper;

import java.util.List;

import cn.wolfcode.p2p.business.domain.BidRequestAuditHistory;

public interface BidRequestAuditHistoryMapper {
    int insert(BidRequestAuditHistory record);

	List<BidRequestAuditHistory> queryByBidRequestId(Long bidRequestId);
}