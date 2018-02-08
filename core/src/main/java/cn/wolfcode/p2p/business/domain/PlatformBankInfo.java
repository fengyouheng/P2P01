package cn.wolfcode.p2p.business.domain;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import lombok.Getter;
import lombok.Setter;
import cn.wolfcode.p2p.base.domain.BaseAuthDomain;
import cn.wolfcode.p2p.base.domain.BaseDomain;

@Getter@Setter
public class PlatformBankInfo extends BaseAuthDomain{

	private String bankName; //银行名称
	private String accountNumber;//银行账号
	private String bankForkName; //支行名称
	private String accountName; //开户人姓名
	
	public String getJsonString(){
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("id",id);
		param.put("bankName",bankName);
		param.put("accountNumber",accountNumber);
		param.put("bankForkName",bankForkName);
		param.put("accountName",accountName);
		return JSON.toJSONString(param);
	}
}
