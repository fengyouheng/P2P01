package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.SystemDictionary;
import cn.wolfcode.p2p.base.query.SystemDictionaryQueryObject;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by seemygo on 2018/1/23.
 */
public interface ISystemDictionaryService {
    int save(SystemDictionary systemDictionary);
    int update(SystemDictionary systemDictionary);
    SystemDictionary get(Long id);

    PageInfo queryPage(SystemDictionaryQueryObject qo);

    void saveOrUpdate(SystemDictionary systemDictionary);

    List<SystemDictionary> selectAll();
}
