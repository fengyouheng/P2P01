package cn.wolfcode.p2p.mgrsite.controller;

import cn.wolfcode.p2p.base.domain.Logininfo;
import cn.wolfcode.p2p.base.service.ILogininfoService;
import cn.wolfcode.p2p.base.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by seemygo on 2018/1/20.
 */
@Controller
public class LoginController {
    @Autowired
    private ILogininfoService logininfoService;
    @RequestMapping("/managerLogin")
    @ResponseBody
    public AjaxResult managerLogin(String username,String password){
        AjaxResult result = null;
        Logininfo logininfo = logininfoService.login(username, password, Logininfo.USERTYPE_MANAGER);
        if(logininfo!=null){
            result = new AjaxResult("登录成功");
        }else{
            result = new AjaxResult(false,"登录失败");
        }
        return result;
    }
    @RequestMapping("/index")
    public String indexPage(){
        return "main";
    }
}
