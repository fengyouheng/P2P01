package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.SystemDictionaryItem;
import cn.wolfcode.p2p.base.mapper.SystemDictionaryItemMapper;
import cn.wolfcode.p2p.base.query.SystemDictionaryItemQueryObject;
import cn.wolfcode.p2p.base.service.ISystemDictionaryItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by seemygo on 2018/1/23.
 */
@Service@Transactional
public class SystemDictionaryItemServiceImpl implements ISystemDictionaryItemService {
    @Autowired
    private SystemDictionaryItemMapper systemDictionaryItemMapper;
    public int save(SystemDictionaryItem systemDictionaryItem) {
        return systemDictionaryItemMapper.insert(systemDictionaryItem);
    }

    public int update(SystemDictionaryItem systemDictionaryItem) {
        return systemDictionaryItemMapper.updateByPrimaryKey(systemDictionaryItem);
    }

    public SystemDictionaryItem get(Long id) {
        return systemDictionaryItemMapper.selectByPrimaryKey(id);
    }

    public PageInfo queryPage(SystemDictionaryItemQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());
        List list = systemDictionaryItemMapper.queryPage(qo);
        return new PageInfo(list);
    }

    public void saveOrUpdate(SystemDictionaryItem systemDictionaryItem) {
        if(systemDictionaryItem.getId()==null){
            this.save(systemDictionaryItem);
        }else{
            this.update(systemDictionaryItem);
        }
    }

    public List<SystemDictionaryItem> queryListByParentSn(String sn) {
        return systemDictionaryItemMapper.queryListByParentSn(sn);
    }
}
