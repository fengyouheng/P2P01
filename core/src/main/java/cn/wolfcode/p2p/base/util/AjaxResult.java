package cn.wolfcode.p2p.base.util;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class AjaxResult {
	
	private boolean success = true ;
	private String msg ;
	
	//new AjaxResult(s保存成功)
	public AjaxResult(String msg){
		this.msg = msg ;
	}
	
	//new AjaxResult(false,'保存失败')
	public AjaxResult(boolean success,String msg){
		this.msg = msg ;
		this.success = success;
	}
}
