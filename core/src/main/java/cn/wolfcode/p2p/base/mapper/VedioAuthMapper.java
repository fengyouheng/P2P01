package cn.wolfcode.p2p.base.mapper;

import java.util.List;

import cn.wolfcode.p2p.base.domain.VedioAuth;
import cn.wolfcode.p2p.base.query.VedioAuthQueryObject;

public interface VedioAuthMapper {

    int insert(VedioAuth record);

    VedioAuth selectByPrimaryKey(Long id);

    int updateByPrimaryKey(VedioAuth record);

	List queryPage(VedioAuthQueryObject qo);
}