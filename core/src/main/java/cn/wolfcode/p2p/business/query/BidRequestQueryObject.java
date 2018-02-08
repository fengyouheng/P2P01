package cn.wolfcode.p2p.business.query;

import lombok.Getter;
import lombok.Setter;
import cn.wolfcode.p2p.base.query.QueryObject;

@Getter@Setter
public class BidRequestQueryObject extends QueryObject{
	private int bidRequestState = -1 ;
	private int[] states;
	private String orderByCondition ;
}
