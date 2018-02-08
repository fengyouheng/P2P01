package cn.wolfcode.p2p.base.domain;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by seemygo on 2018/1/20.
 */
@Setter@Getter
public class SystemDictionary extends BaseDomain {
    private String title;//分类名称,如:教育背景
    private String sn;//分类编码 ,如:educationBackground
    public String getJsonString(){
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("id",id);
        param.put("title",title);
        param.put("sn",sn);
        return JSON.toJSONString(param);
    }
}
