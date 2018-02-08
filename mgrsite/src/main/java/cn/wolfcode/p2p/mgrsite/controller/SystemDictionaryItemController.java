package cn.wolfcode.p2p.mgrsite.controller;

import cn.wolfcode.p2p.base.domain.SystemDictionaryItem;
import cn.wolfcode.p2p.base.query.SystemDictionaryItemQueryObject;
import cn.wolfcode.p2p.base.service.ISystemDictionaryItemService;
import cn.wolfcode.p2p.base.service.ISystemDictionaryService;
import cn.wolfcode.p2p.base.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by seemygo on 2018/1/23.
 */
@Controller
public class SystemDictionaryItemController {
    @Autowired
    private ISystemDictionaryService systemDictionaryService;
    @Autowired
    private ISystemDictionaryItemService systemDictionaryItemService;
    @RequestMapping("/systemDictionaryItem_list")
    public String systemDictionaryItemPage(@ModelAttribute("qo") SystemDictionaryItemQueryObject qo, Model model){
        model.addAttribute("systemDictionaryGroups",systemDictionaryService.selectAll());
        model.addAttribute("pageResult",systemDictionaryItemService.queryPage(qo));
        return "systemdic/systemDictionaryItem_list";
    }
    @RequestMapping("/systemDictionaryItem_update")
    @ResponseBody
    public AjaxResult saveOrUpdate(SystemDictionaryItem systemDictionaryItem){
        AjaxResult result = null;
        try{
            if(systemDictionaryItem.getParentId()==null){
                result = new AjaxResult(false,"请先选择数据字典分类.");
                return result;
            }
            systemDictionaryItemService.saveOrUpdate(systemDictionaryItem);
            result = new AjaxResult("保存成功");
        }catch(Exception e){
            e.printStackTrace();
            result = new AjaxResult(false,e.getMessage());
        }
        return result;
    }
}
