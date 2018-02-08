package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.UserFile;
import cn.wolfcode.p2p.base.query.UserFileQueryObject;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by seemygo on 2018/1/24.
 */
public interface IUserFileService {
    int save(UserFile userFile);
    int update(UserFile userFile);
    UserFile get(Long id);

    void apply(String imgPath);

    /**
     * 查询用户没有选择风控类型的集合数据
     * @return
     */
    List<UserFile> queryUnSelectFileTypeList();

    /**
     * 给上传的图片设置风控类型
     * @param ids
     * @param fileTypes
     */
    void selectType(Long[] ids, Long[] fileTypes);

    /**
     * 查询风控材料集合
     * @param isSelectFileType:如果为true，查询当前用户的已经选择风控类型集合.
     *                        如果为false,查询当前没有选择风控类型集合.
     * @return
     */
    List<UserFile> selectFileTypeByCondition(boolean isSelectFileType);


    PageInfo queryPage(UserFileQueryObject qo);

    /**
     * 风控材料审核
     * @param id
     * @param state
     * @param score
     * @param remark
     */
    void audit(Long id, int state, int score, String remark);
	List<UserFile> queryByUserId(Long userId);
}
