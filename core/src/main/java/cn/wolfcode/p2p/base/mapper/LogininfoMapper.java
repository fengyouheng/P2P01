package cn.wolfcode.p2p.base.mapper;

import java.util.List;
import java.util.Map;

import cn.wolfcode.p2p.base.domain.Logininfo;
import org.apache.ibatis.annotations.Param;

public interface LogininfoMapper {

    int insert(Logininfo record);

    int selectCountByUsername(String username);

    Logininfo login(@Param("username") String username, @Param("password") String password, @Param("userType") int userType);

    int queryCountByUserType(int userType);

	List<Map<String, Object>> autocomplate(@Param("keyword")String keyword, @Param("userType")int userType);
}