package cn.wolfcode.p2p.mgrsite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.service.IRealAuthService;
import cn.wolfcode.p2p.base.service.IUserFileService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.util.AjaxResult;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.business.domain.BidRequest;
import cn.wolfcode.p2p.business.query.BidRequestQueryObject;
import cn.wolfcode.p2p.business.service.IBidRequestAuditHistoryService;
import cn.wolfcode.p2p.business.service.IBidRequestService;

@Controller
public class BorrowController {
	@Autowired
	private IBidRequestService bidRequestService;
	@Autowired
	private IUserinfoService userinfoService;
	@Autowired
	private IRealAuthService realAuthService;
	@Autowired
	private IBidRequestAuditHistoryService bidRequestAuditHistoryService;
	@Autowired
	private IUserFileService userFileService;
	
	@RequestMapping("/bidrequest_publishaudit_list")
	public String pulishAuditPage(@ModelAttribute("qo")BidRequestQueryObject qo,Model model){
		qo.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_PENDING);
		model.addAttribute("pageResult",bidRequestService.queryPage(qo));
		return "bidrequest/publish_audit";
	}
	
	@RequestMapping("/bidrequest_publishaudit")
	@ResponseBody
	public AjaxResult publishAudit(Long id,int state,String remark){
		AjaxResult result = null ;
		try {
			bidRequestService.publishAudit(id,state,remark);
			result = new AjaxResult("");
		} catch (Exception e) {
			e.printStackTrace();
			result = new AjaxResult(false, e.getMessage());
		}
		return result ;
	}
	@RequestMapping("/borrow_info")
	public String borrowInfoPage(Long id,Model model){
		//bidRequest
		BidRequest bidRequest = bidRequestService.get(id);
		if(bidRequest!=null){
			model.addAttribute("bidRequest", bidRequest);
			Userinfo userinfo = userinfoService.get(bidRequest.getCreateUser().getId());
			model.addAttribute("userinfo", userinfo);
			model.addAttribute("realAuth", realAuthService.get(userinfo.getRealAuthId()));
			model.addAttribute("audits", bidRequestAuditHistoryService.queryByBidRequestId(bidRequest.getId()));
			model.addAttribute("userFiles",userFileService.queryByUserId(bidRequest.getCreateUser().getId()));
		}
		return "bidrequest/borrow_info";
	}
	
	@RequestMapping("/bidrequest_audit1_list")
	public String fullAudit1Page(@ModelAttribute("qo")BidRequestQueryObject qo,Model model){
		qo.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1);
		model.addAttribute("pageResult",bidRequestService.queryPage(qo));
		return "bidrequest/audit1";
	}
	
	@RequestMapping("/bidrequest_audit1")
	@ResponseBody
	public AjaxResult fullAudit1(Long id,int state,String remark){
		AjaxResult result = null ;
		try {
			bidRequestService.audit1(id,state,remark);
			result = new AjaxResult("");
		} catch (Exception e) {
			e.printStackTrace();
			result = new AjaxResult(false, e.getMessage());
		}
		return result ;
	}
	
	@RequestMapping("/bidrequest_audit2_list")
	public String fullAudit2Page(@ModelAttribute("qo")BidRequestQueryObject qo,Model model){
		qo.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2);
		model.addAttribute("pageResult",bidRequestService.queryPage(qo));
		return "bidrequest/audit2";
	}
	
	@RequestMapping("/bidrequest_audit2")
	@ResponseBody
	public AjaxResult fullAudit2(Long id,int state,String remark){
		AjaxResult result = null ;
		try {
			bidRequestService.audit2(id,state,remark);
			result = new AjaxResult("");
		} catch (Exception e) {
			e.printStackTrace();
			result = new AjaxResult(false, e.getMessage());
		}
		return result ;
	}
}
