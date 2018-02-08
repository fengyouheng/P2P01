package cn.wolfcode.p2p.website.controller;

import java.math.BigDecimal;

import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.service.IRealAuthService;
import cn.wolfcode.p2p.base.service.IUserFileService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.util.AjaxResult;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.base.util.UserContext;
import cn.wolfcode.p2p.business.domain.BidRequest;
import cn.wolfcode.p2p.business.service.IBidRequestService;
import cn.wolfcode.p2p.website.util.RequireLogin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by seemygo on 2018/1/23.
 */
@Controller
public class BorrowController {
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
	private IRealAuthService realAuthService;
    @Autowired
	private IUserFileService userFileService;
    @Autowired
    private IBidRequestService bidRequestService;
    @RequestMapping("/borrow")
    public String borrrowPage(Model model){
        //判断是否有登录,如果登录了跳转到ftl,否则跳转到html页面中
        if(UserContext.getCurrent()==null){
            return "redirect:borrowStatic.html";
        }
        //account
        model.addAttribute("account",accountService.getCurrent());
        //userinfo
        model.addAttribute("userinfo",userinfoService.getCurrent());

        model.addAttribute("creditBorrowScore", BidConst.CREDIT_BORROW_SCORE);
        return "borrow";
    }
    @RequestMapping("/borrowInfo")
    public String borrowInfo(Model model){
    	if(UserContext.getCurrent()!=null){
    		//是否已经登录,判断是否满足借款条件
    		Userinfo userinfo = userinfoService.getCurrent();
    		if(bidRequestService.canApplyBorrow(userinfo)){
    			//判断用户是否由借款的流程正在审核
    			if(!userinfo.gethasBidRequestProcess()){
    				//才进入申请页面
    				//minBidRequestAmount 最小借款金额
    				model.addAttribute("minBidRequestAmount", BidConst.SMALLEST_BIDREQUEST_AMOUNT);
    				//account 账户信息
    				model.addAttribute("account", accountService.getCurrent());
    				//minBidAmount 系统最小投标金额
    				model.addAttribute("minBidAmount",BidConst.SMALLEST_BID_AMOUNT);
    				return "borrow_apply";
    			}else{
    				//进入结果页面
    				return "borrow_apply_result";
    			}
    		}
    	}
    	return "redirect:/borrow";
    }
    @RequestMapping("/borrow_apply")
    @RequireLogin
    public String apply(BidRequest bidRequest){
    	bidRequestService.apply(bidRequest);
    	return "redirect:/borrowInfo";
    }
    
    @RequestMapping("/borrow_info")
    public String frontBorrorInfoPage(Long id,Model model){
    	BidRequest bidRequest = bidRequestService.get(id);
		if(bidRequest!=null){
			model.addAttribute("bidRequest", bidRequest);
			Userinfo userinfo = userinfoService.get(bidRequest.getCreateUser().getId());
			model.addAttribute("userInfo", userinfo);
			model.addAttribute("realAuth", realAuthService.get(userinfo.getRealAuthId()));
			model.addAttribute("userFiles",userFileService.queryByUserId(bidRequest.getCreateUser().getId()));
			//判断是否登录
			if(UserContext.getCurrent()==null){
				model.addAttribute("self", false);
			}else{
				//判断当前用户是否是借款人
				if(UserContext.getCurrent().getId().equals(bidRequest.getCreateUser().getId())){
					//是本人
					model.addAttribute("self", true);
				}else{
					//投资人
					model.addAttribute("self", false);
					model.addAttribute("account", accountService.getCurrent());
				}
			}
		}
    	return "borrow_info";
    } 
    @RequestMapping("/borrow_bid")
    @ResponseBody
    public AjaxResult bid(Long bidRequestId,BigDecimal amount){
    	AjaxResult result = null ;
    	try {
    		bidRequestService.bid(bidRequestId,amount);
    		result = new AjaxResult("");
		} catch (Exception e) {
			e.printStackTrace();
			result = new AjaxResult(false, e.getMessage());
		}
    	return result ;
    }
}
