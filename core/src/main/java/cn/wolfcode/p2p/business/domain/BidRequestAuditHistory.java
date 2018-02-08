package cn.wolfcode.p2p.business.domain;

import lombok.Getter;
import lombok.Setter;
import cn.wolfcode.p2p.base.domain.BaseAuthDomain;

@Getter@Setter
public class BidRequestAuditHistory extends BaseAuthDomain{
	public static final int PUBLISH_AUDIT = 0; //发标前审核
	public static final int AUDIT1 = 1; //满标一审
	public static final int AUDIT2 = 2; //满标二审
	private Long bidRequestId;
	private int auditType;
	
	public String getAuditTypeDisplay(){
		switch(this.auditType){
		case PUBLISH_AUDIT:return"发标前审核";
		case AUDIT1:return"满标一审";
		case AUDIT2:return"满标二审";
		default:return "";
		}
	}
}
