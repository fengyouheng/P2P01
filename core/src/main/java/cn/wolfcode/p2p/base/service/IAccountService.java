package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.Account;

/**
 * Created by seemygo on 2018/1/20.
 */
public interface IAccountService {
    int save(Account account);
    int update(Account account);
    Account get(Long id);
    Account getCurrent();
}
