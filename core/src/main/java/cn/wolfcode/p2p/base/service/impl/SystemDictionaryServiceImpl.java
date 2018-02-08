package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.SystemDictionary;
import cn.wolfcode.p2p.base.mapper.SystemDictionaryMapper;
import cn.wolfcode.p2p.base.query.SystemDictionaryQueryObject;
import cn.wolfcode.p2p.base.service.ISystemDictionaryService;
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
public class SystemDictionaryServiceImpl implements ISystemDictionaryService {
    @Autowired
    private SystemDictionaryMapper systemDictionaryMapper;
    public int save(SystemDictionary systemDictionary) {
        return systemDictionaryMapper.insert(systemDictionary);
    }

    public int update(SystemDictionary systemDictionary) {
        return systemDictionaryMapper.updateByPrimaryKey(systemDictionary);
    }

    public SystemDictionary get(Long id) {
        return systemDictionaryMapper.selectByPrimaryKey(id);
    }

    public PageInfo queryPage(SystemDictionaryQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());
        List list = systemDictionaryMapper.queryPage(qo);
        return new PageInfo(list);
    }

    public void saveOrUpdate(SystemDictionary systemDictionary) {
        if(systemDictionary.getId()==null){
            this.save(systemDictionary);
        }else{
            this.update(systemDictionary);
        }
    }

    public List<SystemDictionary> selectAll() {
        return systemDictionaryMapper.selectAll();
    }
}
