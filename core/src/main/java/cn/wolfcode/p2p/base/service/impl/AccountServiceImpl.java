package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.mapper.AccountMapper;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by seemygo on 2018/1/20.
 */
@Service@Transactional
public class AccountServiceImpl implements IAccountService {
    @Autowired
    private AccountMapper accountMapper;
    public int save(Account account) {
        return accountMapper.insert(account);
    }

    public int update(Account account) {
        int count = accountMapper.updateByPrimaryKey(account);
        if(count<=0){
            throw new RuntimeException("乐观锁异常");
        }
        return count;
    }

    public Account get(Long id) {
        return accountMapper.selectByPrimaryKey(id);
    }

    public Account getCurrent() {
        return this.get(UserContext.getCurrent().getId());
    }
}
