package cn.wolfcode.p2p.mgrsite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.wolfcode.p2p.business.domain.PlatformBankInfo;
import cn.wolfcode.p2p.business.query.PlatformBankInfoQueryObject;
import cn.wolfcode.p2p.business.service.IPlatformBankInfoService;

@Controller
public class PlatformBankInfoController {

	@Autowired
	private IPlatformBankInfoService platformBankInfoService;
	
	@RequestMapping("/companyBank_list")
	public String platformBankInfoPage(@ModelAttribute("qo")PlatformBankInfoQueryObject qo,Model molde){
		molde.addAttribute("pageResult", platformBankInfoService.queryPage(qo));
		return "platformbankinfo/list";
	}
	
	@RequestMapping("/companyBank_update")
	public String saveOrUpdate(PlatformBankInfo platformBankInfo){
		platformBankInfoService.saveOrUpdate(platformBankInfo);
		return "redirect:/companyBank_list";
	}
}
