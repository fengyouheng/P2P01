package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.MailVerify;

/**
 * Created by seemygo on 2018/1/21.
 */
public interface IMailVerifyService {
    int save(MailVerify mailVerify);
    MailVerify get(String uuid);
}
