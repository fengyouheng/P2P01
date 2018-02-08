package cn.wolfcode.p2p.mgrsite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wolfcode.p2p.base.util.AjaxResult;
import cn.wolfcode.p2p.business.query.RechargeOfflineQueryObject;
import cn.wolfcode.p2p.business.service.IPlatformBankInfoService;
import cn.wolfcode.p2p.business.service.IRechargeOfflineService;

@Controller
public class RechargeofflineController {

	@Autowired
	private IPlatformBankInfoService platformBankInfoService;
	@Autowired
	private IRechargeOfflineService rechargeOfflineService;
	
	@RequestMapping("/rechargeOffline")
	public String rechargeOfflinePage(@ModelAttribute("qo")RechargeOfflineQueryObject qo ,Model model){
		model.addAttribute("banks",platformBankInfoService.selectAll());
		model.addAttribute("pageResult",rechargeOfflineService.queryPage(qo));
		return "rechargeoffline/list" ;
	}
	@ResponseBody
	@RequestMapping("/rechargeOffline_audit")
	public AjaxResult audit(Long id,int state,String remark){
		AjaxResult result = null ;
		try {
			rechargeOfflineService.audit(id,state,remark);
			result = new AjaxResult("");
		} catch (Exception e) {
			e.printStackTrace();
			result = new AjaxResult(false, e.getMessage());
		}
		return result;
	}
}
