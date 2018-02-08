package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.Userinfo;

/**
 * Created by seemygo on 2018/1/20.
 */
public interface IUserinfoService {
    int save(Userinfo userinfo);
    int update(Userinfo userinfo);
    Userinfo get(Long id);
    Userinfo getCurrent();

    /**
     * 手机绑定
     * @param phoneNumber
     * @param verifyCode
     */
    void bindPhone(String phoneNumber, String verifyCode);

    /**
     * 邮箱绑定
     * @param key
     */
    void bindEmail(String key);

    /**
     * 填写个人资料
     * @param userinfo
     */
    void basicInfoSave(Userinfo userinfo);
}
