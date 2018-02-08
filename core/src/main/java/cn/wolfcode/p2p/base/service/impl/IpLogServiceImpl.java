package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.IpLog;
import cn.wolfcode.p2p.base.mapper.IpLogMapper;
import cn.wolfcode.p2p.base.query.IpLogQueryObject;
import cn.wolfcode.p2p.base.service.IIpLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

/**
 * Created by seemygo on 2018/1/20.
 */
@Service@Transactional
public class IpLogServiceImpl implements IIpLogService {
    @Autowired
    private IpLogMapper ipLogMapper;
    public int save(IpLog ipLog) {
        return ipLogMapper.insert(ipLog);
    }

    public PageInfo queryPage(IpLogQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());
        List<IpLog> list = ipLogMapper.queryPage(qo);
        return new PageInfo(list);
    }
}
