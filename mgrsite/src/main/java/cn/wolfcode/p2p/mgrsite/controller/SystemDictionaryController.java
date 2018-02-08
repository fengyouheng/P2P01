package cn.wolfcode.p2p.mgrsite.controller;

import cn.wolfcode.p2p.base.domain.SystemDictionary;
import cn.wolfcode.p2p.base.query.SystemDictionaryQueryObject;
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
public class SystemDictionaryController {
    @Autowired
    private ISystemDictionaryService systemDictionaryService;
    @RequestMapping("/systemDictionary_list")
    public String systemDictionaryPage(@ModelAttribute("qo") SystemDictionaryQueryObject qo,Model model){
        model.addAttribute("pageResult",systemDictionaryService.queryPage(qo));
        return "systemdic/systemDictionary_list";
    }
    @RequestMapping("/systemDictionary_update")
    @ResponseBody
    public AjaxResult saveOrUpdate(SystemDictionary systemDictionary){
        AjaxResult result = null;
        try{
            systemDictionaryService.saveOrUpdate(systemDictionary);
            result = new AjaxResult("");
        }catch(Exception e){
            e.printStackTrace();
            result = new AjaxResult(false,e.getMessage());
        }
        return result;
    }
}
