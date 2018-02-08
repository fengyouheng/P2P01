package cn.wolfcode.p2p.website.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by seemygo on 2018/1/21.
 */
@Controller
public class MeiLianController {
    //localhost/sendSms
    @RequestMapping("/sendSms")
    @ResponseBody
    public String sendSms(String username,String password,String apiKey,String phoneNumber,String content){
        System.out.println("username:"+username);
        System.out.println("password:"+password);
        System.out.println("apiKey:"+apiKey);
        System.out.println("phoneNumber:"+phoneNumber);
        System.out.println("content:"+content);
        return "success";
    }
}
