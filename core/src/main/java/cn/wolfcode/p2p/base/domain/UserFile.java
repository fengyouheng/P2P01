package cn.wolfcode.p2p.base.domain;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by seemygo on 2018/1/24.
 */
@Setter@Getter
public class UserFile extends BaseAuthDomain {
    private String image;//风控材料地址
    private int score;//风控材料分数
    private SystemDictionaryItem fileType;//风控类型
    public String getJsonString(){
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("id",id);
        param.put("applier",applier.getUsername());
        param.put("fileType",fileType.getTitle());
        param.put("image",image);
        return JSON.toJSONString(param);
    }
}
