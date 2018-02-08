package cn.wolfcode.p2p.business.mapper;

import org.apache.ibatis.annotations.Param;

import cn.wolfcode.p2p.business.domain.Bid;

public interface BidMapper {

    int insert(Bid record);

    Bid selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Bid record);

	void updateState(@Param("bidRequestId")Long bidRequestId, @Param("bidRequestState")int bidRequestState);
}