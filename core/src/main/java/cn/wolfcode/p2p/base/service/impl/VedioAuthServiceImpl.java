package cn.wolfcode.p2p.base.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.wolfcode.p2p.base.domain.Logininfo;
import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.domain.VedioAuth;
import cn.wolfcode.p2p.base.mapper.VedioAuthMapper;
import cn.wolfcode.p2p.base.query.VedioAuthQueryObject;
import cn.wolfcode.p2p.base.service.ILogininfoService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.service.IVedioAuthService;
import cn.wolfcode.p2p.base.util.BitStatesUtils;
import cn.wolfcode.p2p.base.util.UserContext;

@Service@Transactional
public class VedioAuthServiceImpl implements IVedioAuthService{

	@Autowired
	private VedioAuthMapper vedioAuthMapper;
	@Autowired
	private IUserinfoService userinfoService;
	@Autowired
	private ILogininfoService logininfoService;
	public int save(VedioAuth vedioAuth) {
		return vedioAuthMapper.insert(vedioAuth);
	}

	public int update(VedioAuth vedioAuth) {
		return vedioAuthMapper.updateByPrimaryKey(vedioAuth);
	}

	public VedioAuth get(Long id) {
		return vedioAuthMapper.selectByPrimaryKey(id);
	}

	public PageInfo queryPage(VedioAuthQueryObject qo) {
		PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());
		List list = vedioAuthMapper.queryPage(qo);
		return new PageInfo(list);
	}

	public void audit(Long loginInfoValue, int state, String remark) {
		Userinfo userinfo = userinfoService.get(loginInfoValue);
		if(userinfo != null && !userinfo.getIsVedioAuth()){ 			//不为空且之前没有验证过
			VedioAuth va = new VedioAuth();
			Logininfo applier = new Logininfo();				//申请人
			applier.setId(loginInfoValue);
			va.setApplier(applier);
			va.setApplyTime(new Date());
			va.setAuditor(UserContext.getCurrent());		//审核人
			va.setAuditTime(new Date());
			va.setRemark(remark);
			if(state==VedioAuth.STATE_PASS){
				va.setState(VedioAuth.STATE_PASS);
				userinfo.addState(BitStatesUtils.OP_VEDIO_AUTH);
				userinfoService.update(userinfo);
			}else{
				va.setState(VedioAuth.STATE_REJECT);
			}
			this.save(va);
		}
	}

	public List<Map<String, Object>> autocomplate(String keyword) {
		return logininfoService.autocomplate(keyword);
	}
}
