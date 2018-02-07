package cn.wolfcode.p2p.website;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wolfcode.p2p.base.domain.Logininfo;
import cn.wolfcode.p2p.base.service.ILogininfoService;
import cn.wolfcode.p2p.base.util.AjaxResult;

@Controller
public class RegisterController {

	@Autowired
	private ILogininfoService logininfoService;
	
	@RequestMapping("/userRegister")
    @ResponseBody
    public AjaxResult userRegister(String username, String password){
        AjaxResult result = null;
        try{
            logininfoService.register(username,password);
            result = new AjaxResult("注册成功");
        }catch(Exception e){
            e.printStackTrace();
            result = new AjaxResult(false,e.getMessage());
        }
        return result;
    }
	
	@RequestMapping("/checkUsername")
    @ResponseBody
    public boolean checkUsername(String username){
        return logininfoService.checkUsername(username);
    }
	
	@RequestMapping("/userLogin")
    @ResponseBody
    public AjaxResult userLogin(String username,String password){
        AjaxResult result = null;
        Logininfo logininfo = logininfoService.login(username,password);
        if(logininfo!=null){
            result = new AjaxResult("登录成功");
        }else{
            result = new AjaxResult(false,"账户密码有误.");
        }
        return result;
    }
}
