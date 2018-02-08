package cn.wolfcode.p2p.base.service;

import java.util.List;
import java.util.Map;

import cn.wolfcode.p2p.base.domain.Logininfo;

/**
 * Created by seemygo on 2018/1/17.
 */
public interface ILogininfoService {
    /**
     * 注册公共
     * @param username
     * @param password
     * @return
     */
    Logininfo register(String username,String password);

    /**
     * 校验用户名是否存在
     * @param username
     * @return
     */
    boolean checkUsername(String username);

    /**
     * 登录方法
     * @param username
     * @param password
     * @return
     */
    Logininfo login(String username, String password,int userType);

    /**
     * 初始化后台管理员
     */
    void initAdmin();
    
    //自动补全功能
	List<Map<String, Object>> autocomplate(String keyword);
}
