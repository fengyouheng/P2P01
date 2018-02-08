package cn.wolfcode.p2p.business.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.sun.tools.corba.se.idl.constExpr.Divide;

import cn.wolfcode.p2p.base.domain.BaseDomain;
import cn.wolfcode.p2p.base.domain.Logininfo;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.business.util.CalculatetUtil;
import lombok.Getter;
import lombok.Setter;

////借款对象
@Getter@Setter
public class BidRequest extends BaseDomain {
    private int version;// 版本号
    private int returnType;// 还款类型(等额本息)
    private int bidRequestType;// 借款类型(信用标)
    private int bidRequestState;// 借款状态
    private BigDecimal bidRequestAmount;// 借款总金额
    private BigDecimal currentRate;// 年化利率
    private BigDecimal minBidAmount;// 最小投標金额
    private int monthes2Return;// 还款月数
    private int bidCount = 0;// 已投标次数(冗余)
    private BigDecimal totalRewardAmount;// 总回报金额(总利息)
    private BigDecimal currentSum = BidConst.ZERO;// 当前已投标总金额
    private String title;// 借款标题
    private String description;// 借款描述
    private String note;// 风控意见
    private Date disableDate;// 招标截止日期
    private int disableDays;// 招标天数
    private Logininfo createUser;// 借款人
    private Date applyTime;// 申请时间
    private Date publishTime;// 发标时间
    private List<Bid> bids = new ArrayList<Bid>();// 针对该借款的投标
    
    public String getJsonString(){
    	Map<String,Object> param = new HashMap<String,Object>();
    	param.put("id",id);
    	param.put("username",createUser.getUsername());
    	param.put("title",title);
    	param.put("bidRequestAmount",bidRequestAmount);
    	param.put("currentRate",currentRate);
    	param.put("monthes2Return",monthes2Return);
    	param.put("returnType",getReturnTypeDisplay());
    	param.put("totalRewardAmount",totalRewardAmount);
    	return JSON.toJSONString(param);
    }
    public String getReturnTypeDisplay(){
    	return this.returnType == BidConst.RETURN_TYPE_MONTH_INTEREST?"按月到期":"按月分期";
    }
    public BigDecimal getRemainAmount(){
    	return this.bidRequestAmount.subtract(this.currentSum);
    }
    //注意 公式写错,当 (this.scale() - divisor.scale())；如果无法表示准确的商值,则抛出 ArithmeticException。
    public BigDecimal getPersent(){
    	return this.currentSum.multiply(CalculatetUtil.ONE_HUNDRED).divide(this.bidRequestAmount,2,RoundingMode.HALF_UP);
    }
    public String getBidRequestStateDisplay() {
		switch (this.bidRequestState) {
		case BidConst.BIDREQUEST_STATE_PUBLISH_PENDING:
			return "待发布";
		case BidConst.BIDREQUEST_STATE_BIDDING:
			return "招标中";
		case BidConst.BIDREQUEST_STATE_UNDO:
			return "已撤销";
		case BidConst.BIDREQUEST_STATE_BIDDING_OVERDUE:
			return "流标";
		case BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1:
			return "满标一审";
		case BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2:
			return "满标二审";
		case BidConst.BIDREQUEST_STATE_REJECTED:
			return "满标审核被拒";
		case BidConst.BIDREQUEST_STATE_PAYING_BACK:
			return "还款中";
		case BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK:
			return "完成";
		case BidConst.BIDREQUEST_STATE_PAY_BACK_OVERDUE:
			return "逾期";
		case BidConst.BIDREQUEST_STATE_PUBLISH_REFUSE:
			return "发标拒绝";
		default:
			return "";
		}
	}
}
