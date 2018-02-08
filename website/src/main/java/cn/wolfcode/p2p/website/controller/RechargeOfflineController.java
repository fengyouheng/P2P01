package cn.wolfcode.p2p.website.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wolfcode.p2p.base.util.AjaxResult;
import cn.wolfcode.p2p.business.domain.RechargeOffline;
import cn.wolfcode.p2p.business.service.IPlatformBankInfoService;
import cn.wolfcode.p2p.business.service.IRechargeOfflineService;
import cn.wolfcode.p2p.website.util.RequireLogin;

@Controller
public class RechargeOfflineController {
	
	@Autowired
	private IPlatformBankInfoService platformBankInfoService;
	@Autowired
	private IRechargeOfflineService rechargeOfflineService;
	
	@RequestMapping("/recharge")
	public String rechargeOfflinePage(Model model){
		model.addAttribute("banks",platformBankInfoService.selectAll());
		return "recharge";
	}
	
	@RequestMapping("/recharge_save")
	@ResponseBody
	@RequireLogin //只有登录才能访问
	public AjaxResult rechargeSave(RechargeOffline rechargeOffline){
		AjaxResult result = null ;
		try {
			rechargeOfflineService.apply(rechargeOffline);
			result = new AjaxResult("");
		} catch (Exception e) {
			e.printStackTrace();
			result = new AjaxResult(false, e.getMessage());
		}
		return result ;
	}
}
