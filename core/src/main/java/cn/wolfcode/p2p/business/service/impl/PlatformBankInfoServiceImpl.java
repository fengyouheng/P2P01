package cn.wolfcode.p2p.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.wolfcode.p2p.business.domain.PlatformBankInfo;
import cn.wolfcode.p2p.business.mapper.PlatformBankInfoMapper;
import cn.wolfcode.p2p.business.query.PlatformBankInfoQueryObject;
import cn.wolfcode.p2p.business.service.IPlatformBankInfoService;

@Service
@Transactional
public class PlatformBankInfoServiceImpl implements IPlatformBankInfoService{

	@Autowired
	private PlatformBankInfoMapper platformBankInfoMapper;
	
	public int save(PlatformBankInfo platformBankInfo) {
		return platformBankInfoMapper.insert(platformBankInfo);
	}

	public int update(PlatformBankInfo platformBankInfo) {
		return platformBankInfoMapper.updateByPrimaryKey(platformBankInfo);
	}

	public PlatformBankInfo get(Long id) {
		return platformBankInfoMapper.selectByPrimaryKey(id);
	}

	public PageInfo queryPage(PlatformBankInfoQueryObject qo) {
		PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());
		List list = platformBankInfoMapper.queryPage(qo);
		return new PageInfo(list);
	}

	public void saveOrUpdate(PlatformBankInfo platformBankInfo) {
		if(platformBankInfo.getId()==null){
			platformBankInfoMapper.insert(platformBankInfo);
		}else{
			platformBankInfoMapper.updateByPrimaryKey(platformBankInfo);
		}
	}

	public List<PlatformBankInfo> selectAll() {
		return platformBankInfoMapper.selectAll();
	}

}
