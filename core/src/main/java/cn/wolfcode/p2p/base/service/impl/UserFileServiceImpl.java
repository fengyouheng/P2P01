package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.SystemDictionaryItem;
import cn.wolfcode.p2p.base.domain.UserFile;
import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.mapper.UserFileMapper;
import cn.wolfcode.p2p.base.query.UserFileQueryObject;
import cn.wolfcode.p2p.base.service.IUserFileService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.util.UserContext;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by seemygo on 2018/1/24.
 */
@Service@Transactional
public class UserFileServiceImpl implements IUserFileService {
    @Autowired
    private UserFileMapper userFileMapper;
    @Autowired
    private IUserinfoService userinfoService;
    public int save(UserFile userFile) {
        return userFileMapper.insert(userFile);
    }

    public int update(UserFile userFile) {
        return userFileMapper.updateByPrimaryKey(userFile);
    }

    public UserFile get(Long id) {
        return userFileMapper.selectByPrimaryKey(id);
    }
    //上传图片完后,保存相关信息
    public void apply(String imgPath) {
        UserFile uf = new UserFile();
        uf.setImage(imgPath);
        uf.setApplier(UserContext.getCurrent());
        uf.setApplyTime(new Date());
        uf.setState(UserFile.STATE_NORMAL);
        this.save(uf);
    }

    public List<UserFile> queryUnSelectFileTypeList() {
        return userFileMapper.queryUnSelectFileTypeList(UserContext.getCurrent().getId());
    }

    public void selectType(Long[] ids, Long[] fileTypes) {
        if(ids!=null && fileTypes!=null&&ids.length==fileTypes.length){
            UserFile uf;
            SystemDictionaryItem fileType;
            for(int i=0;i<ids.length;i++){
                uf = this.get(ids[i]);
                //判断该条风控记录是否数据当前登录用户
                if(UserContext.getCurrent().getId().equals(uf.getApplier().getId())){
                    fileType = new SystemDictionaryItem();
                    fileType.setId(fileTypes[i]);
                    uf.setFileType(fileType);
                    this.update(uf);
                }
            }
        }
    }

    public List<UserFile> selectFileTypeByCondition(boolean isSelectFileType) {
        return userFileMapper.selectFileTypeByCondition(UserContext.getCurrent().getId(),isSelectFileType);
    }

    public PageInfo queryPage(UserFileQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());
        List list = userFileMapper.queryPage(qo);
        return new PageInfo(list);
    }

    public void audit(Long id, int state, int score, String remark) {
        //1根据id获取风控材料对象，处于待审核.
        UserFile userFile = this.get(id);
        if(userFile!=null && userFile.getState()==UserFile.STATE_NORMAL){
            //2.设置上审核人,审核时间,审核备注.
            userFile.setAuditor(UserContext.getCurrent());
            userFile.setAuditTime(new Date());
            userFile.setRemark(remark);
            if(state==UserFile.STATE_PASS){
                //3.审核通过
                //  设置状态
                userFile.setState(UserFile.STATE_PASS);
                userFile.setScore(score);
                //  找到申请人的userinfo对象,score字段得添加对应的分数
                Userinfo applierUserinfo = userinfoService.get(userFile.getApplier().getId());
                applierUserinfo.setScore(applierUserinfo.getScore()+score);
                userinfoService.update(applierUserinfo);
            }else{
                //4.审核失败
                //  设置状态
                userFile.setState(UserFile.STATE_REJECT);
            }
            this.update(userFile);
        }
    }

	public List<UserFile> queryByUserId(Long userId) {
		return userFileMapper.queryByUserId(userId,UserFile.STATE_PASS);
	}
}
