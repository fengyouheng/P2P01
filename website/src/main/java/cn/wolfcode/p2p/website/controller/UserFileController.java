package cn.wolfcode.p2p.website.controller;

import cn.wolfcode.p2p.base.domain.UserFile;
import cn.wolfcode.p2p.base.service.ISystemDictionaryItemService;
import cn.wolfcode.p2p.base.service.IUserFileService;
import cn.wolfcode.p2p.base.util.AjaxResult;
import cn.wolfcode.p2p.website.util.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by seemygo on 2018/1/24.
 */
@Controller
public class UserFileController {
    @Autowired
    private IUserFileService userFileService;
    @Autowired
    private ISystemDictionaryItemService systemDictionaryItemService;
    @Value("${file.path}")
    private String filePath;
    @RequestMapping("/userFile")
    public String userFilePage(Model model){
        List<UserFile> unselectFileTypeList = userFileService.selectFileTypeByCondition(false);
        if(unselectFileTypeList.size()>0){
            model.addAttribute("userFiles",unselectFileTypeList);
            //图片下拉框数据查询,通过数据字典查询
            model.addAttribute("fileTypes",systemDictionaryItemService.queryListByParentSn("userFileType"));
            return "userFiles_commit";
        }else{
            List<UserFile> selectFileTypeList = userFileService.selectFileTypeByCondition(true);
            model.addAttribute("userFiles",selectFileTypeList);
            return "userFiles";
        }

       /* 1.查询当前用户没有选择风控类型集合unSelectFileTypeList
        如果unSelectFileTypeList.size()>0:当前用户存在没有选择风控类型记录，去到userFile_commit页面，放入的数据unSelectFileTypeList
        如果unSelectFileTypeList.size()==0:当前用户所有的风控材料都已经选择风控类型.去到userFile.ftl页面。selectFileTypeList
        */


        //model.addAttribute("userFiles",new ArrayList());
        //return "userFiles";

        //userFiles:当前用户上传了图片，但是没有选择风控类型记录集合.
        /*model.addAttribute("userFiles",userFileService.queryUnSelectFileTypeList());
        model.addAttribute("fileTypes",systemDictionaryItemService.queryListByParentSn("userFileType"));
        return "userFiles_commit";*/
    }
    @RequestMapping("/userFileUpload")
    @ResponseBody
    public String userFileUpload(MultipartFile file){  //后前台页面格式一致
        String imgPath = UploadUtil.upload(file, filePath);
        userFileService.apply(imgPath);  //上传成功后,往数据表插入记录
        return imgPath;
    }
    @RequestMapping("/userFile_selectType")
    @ResponseBody
    public AjaxResult selectType(Long[] id,Long[] fileType){
        AjaxResult result = null;
        try{
        	//将认证材料id和风控类型id更新进数据库
            userFileService.selectType(id,fileType); 
            result = new AjaxResult("");
        }catch(Exception e){
            e.printStackTrace();
            result = new AjaxResult(false,e.getMessage());
        }
        return result;
    }
}
