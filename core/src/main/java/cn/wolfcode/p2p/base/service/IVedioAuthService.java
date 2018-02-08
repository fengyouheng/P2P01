package cn.wolfcode.p2p.base.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;

import cn.wolfcode.p2p.base.domain.VedioAuth;
import cn.wolfcode.p2p.base.query.VedioAuthQueryObject;

public interface IVedioAuthService {
	int save(VedioAuth vedioAuth);
	int update(VedioAuth vedioAuth);
	VedioAuth get(Long id);
	PageInfo queryPage(VedioAuthQueryObject qo);
	void audit(Long loginInfoValue, int state, String remark);
	List<Map<String, Object>> autocomplate(String keyword); //自动补全
}
