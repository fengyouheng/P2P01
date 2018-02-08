package cn.wolfcode.p2p.website.controller;

import cn.wolfcode.p2p.base.domain.RealAuth;
import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.service.IRealAuthService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.util.AjaxResult;
import cn.wolfcode.p2p.website.util.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by seemygo on 2018/1/23.
 */
@Controller
public class RealAuthController {
    @Autowired
    private IRealAuthService realAuthService;
    @Autowired
    private IUserinfoService userinfoService;
    @Value("${file.path}")
    private String filePath;
    @RequestMapping("/realAuth")
    public String realAuthPage(Model model){
        //1.获取userinfo对象,判断当前用户是否拥有实名认证状态码
        Userinfo userinfo = userinfoService.getCurrent();
        if(userinfo.getIsRealAuth()){
            //如果有,根据userinfo中的realAuthId查询对应的实名认证对象,跳转到realAuth_result.ftl页面
            RealAuth realAuth = realAuthService.get(userinfo.getRealAuthId());
            model.addAttribute("realAuth",realAuth);
            model.addAttribute("auditing",false);
            return "realAuth_result";
        }else{
            //如果没有,
            //  判断userinfo对象中的realAuthId是否为null
            if(userinfo.getRealAuthId()==null){
                //  如果为null,此时跳转到申请的页面realAuth.ftl
                return "realAuth";
            }else{
                //  如果不为null，此时跳转realAuth_result.ftl页面（待审核）
                model.addAttribute("auditing",true);
                return "realAuth_result";
            }


        }
    }
    @RequestMapping("/realAuth_save")
    @ResponseBody
    public AjaxResult realAuthSave(RealAuth realAuth){
        AjaxResult result = null;
        try{
            realAuthService.realAuthSave(realAuth);
            result = new AjaxResult("");
        }catch(Exception e){
            e.printStackTrace();
            result = new AjaxResult(false,e.getMessage());
        }
        return result;
    }
    @RequestMapping("/uploadImage")
    @ResponseBody
    public String uploadImage(MultipartFile image){
        return UploadUtil.upload(image,filePath);
    }
}
