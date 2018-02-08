package cn.wolfcode.p2p.base.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by seemygo on 2018/1/21.
 */
@Setter@Getter
public class VerifyCodeVo implements Serializable {
    private String phoneNumber;//手机号码
    private String verifyCode;//验证码
    private Date sendTime;//发送事件s
}
