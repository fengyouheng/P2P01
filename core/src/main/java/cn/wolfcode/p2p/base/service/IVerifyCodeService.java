package cn.wolfcode.p2p.base.service;

/**
 * Created by seemygo on 2018/1/21.
 */
public interface IVerifyCodeService {
    /**
     * 发送的短信
     * @param phoneNumber：手机号码
     */
    void sendVerifyCode(String phoneNumber);

    /**
     * 校验验证码
     * @param phoneNumber
     * @param verifyCode
     * @return
     */
    boolean validate(String phoneNumber, String verifyCode);
}
