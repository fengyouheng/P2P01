package cn.wolfcode.p2p.base.mapper;

import cn.wolfcode.p2p.base.domain.Logininfo;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface LogininfoMapper {
    int insert(Logininfo record);

	int selectCountByUsername(String username);

	Logininfo login(@Param("username")String username, @Param("password")String password);


}