package cn.wolfcode.p2p.website.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.business.query.BidRequestQueryObject;
import cn.wolfcode.p2p.business.service.IBidRequestService;

@Controller
public class IndexController {
	@Autowired
	private IBidRequestService bidRequestService ;
	
	@RequestMapping("/index")
	public String indexPage(BidRequestQueryObject qo,Model model){
		model.addAttribute("bidRequests",bidRequestService.queryIndexList(qo));
		qo.setCurrentPage(1);
		qo.setPageSize(5);
		qo.setOrderByCondition("ORDER BY br.bidRequestState ASC");
		qo.setStates(new int[]{BidConst.BIDREQUEST_STATE_BIDDING,BidConst.BIDREQUEST_STATE_PAYING_BACK,BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK});
		return "main";
	}
}
