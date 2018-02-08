package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.MailVerify;
import cn.wolfcode.p2p.base.service.IEmailService;
import cn.wolfcode.p2p.base.service.IMailVerifyService;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

/**
 * Created by seemygo on 2018/1/21.
 */
@Service@Transactional
public class EmailServiceImpl implements IEmailService {
    @Value("${email.applicationUrl}")
    private String applicationUrl;
    @Autowired
    private IMailVerifyService mailVerifyService;
    public void sendEmail(String email) {
        //构建UUID,拼接在地址栏的参数
        String uuid = UUID.randomUUID().toString();
        //构建邮件内容
        StringBuilder msg = new StringBuilder(100);
        msg.append("感谢注册P2P平台,这是您的认证邮件,点击<a href='").append(applicationUrl).append("/bindEmail?key=")
                .append(uuid)
                .append("'>这里</a>完成认证,有效期为").append(BidConst.EAMIL_VAILD_TIME).append("天,请尽快认证");
        System.out.println(msg);
        //把发送的邮件地址,发送人id，发送时间，UUID
        MailVerify mailVerify = new MailVerify();
        mailVerify.setSendTime(new Date());
        mailVerify.setEmail(email);
        mailVerify.setUserId(UserContext.getCurrent().getId());
        mailVerify.setUuid(uuid);
        mailVerifyService.save(mailVerify);

    }
}
