package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.RealAuth;
import cn.wolfcode.p2p.base.query.RealAuthQueryObject;
import com.github.pagehelper.PageInfo;

/**
 * Created by seemygo on 2018/1/23.
 */
public interface IRealAuthService {
    int save(RealAuth realAuth);
    int update(RealAuth realAuth);
    RealAuth get(Long id);

    /**
     * 实名认证的申请
     * @param realAuth
     */
    void realAuthSave(RealAuth realAuth);

    PageInfo queryPage(RealAuthQueryObject qo);

    /**
     * 实名认证审核
     * @param id
     * @param state
     * @param remark
     */
    void audit(Long id, int state, String remark);
}
