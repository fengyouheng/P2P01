package cn.wolfcode.p2p.mgrsite.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wolfcode.p2p.base.query.VedioAuthQueryObject;
import cn.wolfcode.p2p.base.service.IVedioAuthService;
import cn.wolfcode.p2p.base.util.AjaxResult;

@Controller
public class VedioAuthController {

	@Autowired
	private IVedioAuthService vedioAuthService;
	
	@RequestMapping("/vedioAuth")
	public String vedioAuthPage(@ModelAttribute("qo")VedioAuthQueryObject qo, Model model){
		model.addAttribute("pageResult", vedioAuthService.queryPage(qo));
		return "vedioAuth/list";
	}
	
	@RequestMapping("/vedioAuth_audit")
	@ResponseBody
	public AjaxResult audit(Long loginInfoValue,int state,String remark){
		AjaxResult result = null ;
			try {
				vedioAuthService.audit(loginInfoValue,state,remark);
				result = new AjaxResult("审核成功");
			} catch (Exception e) {
				e.printStackTrace();
				result = new AjaxResult(false,e.getMessage());
			}
		return result;
	}
	@RequestMapping("/vedioAuth_autocomplate")
	@ResponseBody
	public List<Map<String,Object>> autocomplate(String keyword){
		
		return vedioAuthService.autocomplate(keyword);
	}
}
