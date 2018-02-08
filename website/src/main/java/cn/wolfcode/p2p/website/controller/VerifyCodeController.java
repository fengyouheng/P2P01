package cn.wolfcode.p2p.website.controller;

import cn.wolfcode.p2p.base.service.IVerifyCodeService;
import cn.wolfcode.p2p.base.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by seemygo on 2018/1/21.
 */
@Controller
public class VerifyCodeController {
    @Autowired
    private IVerifyCodeService verifyCodeService;
    @RequestMapping("/sendVerifyCode")
    @ResponseBody
    public AjaxResult sendVerifyCode(String phoneNumber){
        AjaxResult result = null;
        try{
            //后台发送验证码
            verifyCodeService.sendVerifyCode(phoneNumber);
            result = new AjaxResult("发送验证码成功");
        }catch(Exception e){
            e.printStackTrace();
            result = new AjaxResult(false,e.getMessage());
        }
        return result;
    }
}
