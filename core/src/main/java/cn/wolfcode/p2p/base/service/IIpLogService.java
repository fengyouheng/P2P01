package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.IpLog;
import cn.wolfcode.p2p.base.query.IpLogQueryObject;
import com.github.pagehelper.PageInfo;

/**
 * Created by seemygo on 2018/1/20.
 */
public interface IIpLogService {
    int save(IpLog ipLog);

    PageInfo queryPage(IpLogQueryObject qo);
}
