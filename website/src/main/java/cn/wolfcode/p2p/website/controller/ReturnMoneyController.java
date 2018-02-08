package cn.wolfcode.p2p.website.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.util.AjaxResult;
import cn.wolfcode.p2p.base.util.UserContext;
import cn.wolfcode.p2p.business.query.PaymentScheduleQueryObject;
import cn.wolfcode.p2p.business.service.IPaymentScheduleService;

@Controller
public class ReturnMoneyController {
	@Autowired
	private IAccountService accountService;
	@Autowired
	private IPaymentScheduleService paymentScheduleService;
	@RequestMapping("/borrowBidReturn_list")
	public String returnMoneyPage(@ModelAttribute("qo")PaymentScheduleQueryObject qo,Model model){
		model.addAttribute("account", accountService.getCurrent());
		qo.setUserId(UserContext.getCurrent().getId());
		model.addAttribute("pageResult", paymentScheduleService.queryPage(qo));
		return "returnMoney_list";
	}
	
	@RequestMapping("/returnMoney")
	@ResponseBody
	public AjaxResult returnMoney(Long id){
		AjaxResult result;
		try {
			paymentScheduleService.returnMoney(id);
			result = new AjaxResult("");
		} catch (Exception e) {
			e.printStackTrace();
			result = new AjaxResult(false, e.getMessage());
		}
		return result ;
	}
	
}
