package cn.wolfcode.p2p.business.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.util.UserContext;
import cn.wolfcode.p2p.business.domain.RechargeOffline;
import cn.wolfcode.p2p.business.mapper.RechargeOfflineMapper;
import cn.wolfcode.p2p.business.query.RechargeOfflineQueryObject;
import cn.wolfcode.p2p.business.service.IAccountFlowService;
import cn.wolfcode.p2p.business.service.IRechargeOfflineService;

@Service@Transactional
public class RechargeOfflineServiceImpl implements IRechargeOfflineService{

	@Autowired
	private RechargeOfflineMapper rechargeOfflineMapper;
	@Autowired
	private IAccountFlowService accountFlowService;
	@Autowired
	private IAccountService accountService;
	
	public int save(RechargeOffline rechargeOffline) {
		return rechargeOfflineMapper.insert(rechargeOffline);
	}

	public int update(RechargeOffline rechargeOffline) {
		return rechargeOfflineMapper.updateByPrimaryKey(rechargeOffline);
	}

	public RechargeOffline get(Long id) {
		return rechargeOfflineMapper.selectByPrimaryKey(id);
	}

	public void apply(RechargeOffline rechargeOffline) {
		RechargeOffline offline = new RechargeOffline();
		offline.setAmount(rechargeOffline.getAmount());
		offline.setBankInfo(rechargeOffline.getBankInfo());
		offline.setApplier(UserContext.getCurrent());
		offline.setApplyTime(new Date());
		offline.setTradeCode(rechargeOffline.getStateDisplay());
		offline.setTradeTime(rechargeOffline.getTradeTime());
		offline.setState(rechargeOffline.STATE_NORMAL);
		offline.setNote(rechargeOffline.getNote());
		this.save(offline);
	}

	public PageInfo queryPage(RechargeOfflineQueryObject qo) {
		PageHelper.startPage(qo.getCurrentPage(), qo.getPageSize());
		List list = rechargeOfflineMapper.queryPage(qo);
		return new PageInfo(list);
	}
	//线下充值审核
	public void audit(Long id, int state, String remark) {
		//条件判断
		RechargeOffline ro = this.get(id);
		if(ro!=null && ro.getState()==RechargeOffline.STATE_NORMAL){
			//根据id获取审核对象,判断处于待审核状态
			ro.setAuditor(UserContext.getCurrent());
			ro.setAuditTime(new Date());
			ro.setRemark(remark);
			if(state==RechargeOffline.STATE_PASS){
				//设置相关的属性
				ro.setState(RechargeOffline.STATE_PASS);
				//审核成功
				Account applierAccount = accountService.get(ro.getApplier().getId());
				//获取到申请人的账户,可用金额增加
				applierAccount.setUsableAmount(applierAccount.getUsableAmount()
						.add(ro.getAmount()));
				accountService.update(applierAccount);
				//生成充值成功的流水
				accountFlowService.creatRechargeOfflineFlow(applierAccount,ro.getAmount());
			}else{
				//审核失败
				//设置状态
				ro.setState(RechargeOffline.STATE_REJECT);
			}
			this.update(ro);
		}
	}

}
