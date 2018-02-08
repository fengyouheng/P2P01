package cn.wolfcode.p2p.base.util;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by seemygo on 2018/1/18.
 */
@Setter@Getter
public class AjaxResult {
    private boolean success = true;
    private String msg;
    //new AjaxResult("保存成功");----->{success:true,msg:'保存成功'}
    public AjaxResult(String msg) {
        this.msg = msg;
    }
    //new AjaxResult(false,"保存失败");----->{success:false,msg:'保存失败'}
    public AjaxResult(boolean success,String msg) {
        this.success = success;
        this.msg = msg;
    }

}
