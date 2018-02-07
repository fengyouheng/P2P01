package cn.wolfcode.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class Logininfo extends BaseDomain{
	public static final int STATE_NORMAL = 0;//Õý³£×´Ì¬
	public static final int STATE_LOCK = 1;//Ëø¶¨×´Ì¬
	private String username;
	private String password;
	private int state;
}