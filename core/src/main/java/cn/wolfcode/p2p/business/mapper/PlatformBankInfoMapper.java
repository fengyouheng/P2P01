package cn.wolfcode.p2p.business.mapper;

import cn.wolfcode.p2p.business.domain.PlatformBankInfo;
import cn.wolfcode.p2p.business.query.PlatformBankInfoQueryObject;

import java.util.List;

public interface PlatformBankInfoMapper {

    int insert(PlatformBankInfo record);

    PlatformBankInfo selectByPrimaryKey(Long id);

    List<PlatformBankInfo> selectAll();

    int updateByPrimaryKey(PlatformBankInfo record);

	List queryPage(PlatformBankInfoQueryObject qo);
}