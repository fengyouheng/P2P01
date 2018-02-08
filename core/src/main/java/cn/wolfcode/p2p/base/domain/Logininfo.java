package cn.wolfcode.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by seemygo on 2018/1/17.
 */
@Setter@Getter
public class Logininfo extends BaseDomain {
    public static final int STATE_NORMAL = 0;//正常状态
    public static final int STATE_LOCK = 1;//锁定状态
    public static final int USERTYPE_USER = 0;//普通用户
    public static final int USERTYPE_MANAGER = 1;//管理员用户
    private String username;
    private String password;
    private int state;
    private int userType;
}
