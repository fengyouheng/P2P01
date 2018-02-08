package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.RealAuth;
import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.mapper.RealAuthMapper;
import cn.wolfcode.p2p.base.query.RealAuthQueryObject;
import cn.wolfcode.p2p.base.service.IRealAuthService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.util.BitStatesUtils;
import cn.wolfcode.p2p.base.util.UserContext;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by seemygo on 2018/1/23.
 */
@Service@Transactional
public class RealAuthServiceImpl implements IRealAuthService {
    @Autowired
    private RealAuthMapper realAuthMapper;
    @Autowired
    private IUserinfoService userinfoService;
    public int save(RealAuth realAuth) {
        return realAuthMapper.insert(realAuth);
    }

    public int update(RealAuth realAuth) {
        return realAuthMapper.updateByPrimaryKey(realAuth);
    }

    public RealAuth get(Long id) {
        return realAuthMapper.selectByPrimaryKey(id);
    }

    public void realAuthSave(RealAuth realAuth) {
        //把实名认证信息存入到数据库中.
        RealAuth ra = new RealAuth();
        ra.setAddress(realAuth.getAddress());//身份证地址
        ra.setApplier(UserContext.getCurrent());//申请人
        ra.setApplyTime(new Date());//申请时间
        ra.setBornDate(realAuth.getBornDate());//出生年月
        ra.setIdNumber(realAuth.getIdNumber());//身份证号码
        ra.setImage1(realAuth.getImage1());//身份证正面图片地址
        ra.setImage2(realAuth.getImage2());//身份证反面图片地址
        ra.setRealName(realAuth.getRealName());//真实姓名
        ra.setSex(realAuth.getSex());//性别
        ra.setState(RealAuth.STATE_NORMAL);//状态-待审核
        this.save(ra);
        //把实名认证对象设置到userinfo中的realAuthId字段
        Userinfo userinfo = userinfoService.getCurrent();
        userinfo.setRealAuthId(ra.getId());
        userinfoService.update(userinfo);
    }

    public PageInfo queryPage(RealAuthQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());
        List result = realAuthMapper.queryPage(qo);
        return new PageInfo(result);
    }

    public void audit(Long id, int state, String remark) {
        //1.根据id查询实名认证审核对象,判断是否为null,判断是否已经审核了.
        RealAuth realAuth = this.get(id);
        if(realAuth!=null && realAuth.getState() == RealAuth.STATE_NORMAL){
            //2.设置审核人，审核时间,审核备注.
            realAuth.setAuditor(UserContext.getCurrent());
            realAuth.setAuditTime(new Date());
            realAuth.setRemark(remark);
            //获取到申请人的userinfo
            Userinfo applierUserinfo = userinfoService.get(realAuth.getApplier().getId());
            //3.审核通过
            if(state==RealAuth.STATE_PASS){
                //      状态修改审核通过,
                realAuth.setState(RealAuth.STATE_PASS);
                //      给申请人的userinfo添加实名认证的状态码
                applierUserinfo.addState(BitStatesUtils.OP_REAL_AUTH);
                //      设置申请人的userinfo中的realName和idNumber
                applierUserinfo.setRealName(realAuth.getRealName());
                applierUserinfo.setIdNumber(realAuth.getIdNumber());
            }else{
                //4.审核拒绝
                realAuth.setState(RealAuth.STATE_REJECT);
                //      状态修改成审核拒绝
                //      把userinfo中的realAuthId设置为null
                applierUserinfo.setRealAuthId(null);
            }
            this.update(realAuth);
            userinfoService.update(applierUserinfo);
        }

    }
}
