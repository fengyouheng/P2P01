package cn.wolfcode.p2p.business.service;

import java.util.List;

import com.github.pagehelper.PageInfo;

import cn.wolfcode.p2p.business.domain.PlatformBankInfo;
import cn.wolfcode.p2p.business.query.PlatformBankInfoQueryObject;

public interface IPlatformBankInfoService {
	int save(PlatformBankInfo platformBankInfo);
	int update(PlatformBankInfo platformBankInfo);
	PlatformBankInfo get(Long id);
	PageInfo queryPage(PlatformBankInfoQueryObject qo);
	void saveOrUpdate(PlatformBankInfo platformBankInfo);
	List<PlatformBankInfo> selectAll();
}
