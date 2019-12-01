package com.zhiku.controller;


import com.zhiku.entity.User;
import com.zhiku.service.CourseSaveService;
import com.zhiku.util.ResponseData;
import com.zhiku.view.CourseView;
import com.zhiku.view.SectionContentView;
import com.zhiku.view.SectionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@CrossOrigin(value = "*")
@Controller
@RequestMapping(value = "/backstage/course")
public class CourseSaveController {
    //todo：前端页面目前使用webapp下的jsp，待修改
    //管理员uid，只有登录这些账号只能访问页面和使用功能
    private int[] admUids={101};
    @Autowired
    CourseSaveService courseSaveService;

    /**
     * 创建课程
     * @param user 自动获取
     * @param title 课程名称
     * @param vid 版本号
     * @param describe 课程描述
     * @param iconPath 课程图片地址
     */
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData create(User user,String title,String describe,String vid,String iconPath){
        if(!isAdm(user)) {
            return ResponseData.powerError();
        }
        if(title==null||title.equals( "" )){
            return new ResponseData( 400, "课程名称不能为空");
        }
        courseSaveService.createCourse( title,vid,describe,iconPath );
        return ResponseData.ok();
    }

    /**
     * 修改课程，可修改课程版本号、标题、图片、描述
     * @param user 自动获取
     * @param title 课程名称
     * @param vid 版本号
     * @param describe 课程描述
     * @param iconPath 课程图片地址
     */
    @RequestMapping(value = "/update_info",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData update(User user,Integer cid,
                               @RequestParam(required = false)String title,
                               @RequestParam(required = false)String describe,
                               @RequestParam(required = false)String vid,
                               @RequestParam(required = false)String iconPath){
        if(!isAdm(user)) {
            return ResponseData.powerError();
        }
        String error=courseSaveService.update( cid,title,vid,describe,iconPath );
        if(error!=null){
            return new ResponseData( 400,error );
        }
        return ResponseData.ok();
    }


    //TODO:覆盖课程内容、删除课程内容，都没有处理段落（目前段落都不删），应当判断段落是否收藏，然后删除或者禁止删
    /**
     * 添加课程内容
     * @param user 自动获取
     * @param file md文件
     * @param cid 课程id,vid 版本号
     */
    //TODO:测试，postman无法传二维数组，临时拼接为一维
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData save(User user, MultipartFile file,Integer cid,
                             @RequestParam(required = false) String vid, @RequestParam(required = false) Integer[][] seqs)
            throws IOException {
        if(!isAdm(user)) {
            return ResponseData.powerError();
        }

        Integer[][] seqs2=seqs;

        //储存文件
        if(Objects.equals( file.getOriginalFilename(), "" )){
            return new ResponseData(400,"课程文件不能为空");
        }
        if (!courseSaveService.checkFile( file )) {
            return new ResponseData(400,"文件类型不符合，请上传.md或.txt类型！");
        }
        String filePath="";
        try{
            filePath= courseSaveService.saveFile( file );
        }catch (Exception e){
            return new ResponseData(400,"文件上传失败");
        }
        //储存课程内容
        String re= courseSaveService.saveContent( filePath,cid,vid, seqs2);
        if(re!=null){
            return new ResponseData(400,re+"!");
        }
        return ResponseData.ok();
    }

    /**
     * 删除课程
     * @param user 自动获取
     * @param cid 课程id
     * @param vid 课程版本号
     * @param seqs 要删除的章节,不传表示课程全部删除
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData delete(User user,Integer cid,String vid,@RequestParam(required = false) Integer[][] seqs) {
        if(!isAdm(user)) {
            return ResponseData.powerError();
        }
        courseSaveService.delete( cid,vid,seqs );
        return  ResponseData.ok();
    }

    /**
     * 预览课程内容
     * @param user 自动获取
     * @param file md|txt文件
     */
    @RequestMapping(value = "/preview",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData preview(User user, MultipartFile file) throws IOException {
        if(!isAdm(user)) {
            return ResponseData.powerError();
        }
        //储存文件
        if(Objects.equals( file.getOriginalFilename(), "" )){
            return new ResponseData(400,"课程文件不能为空");
        }
        if (!courseSaveService.checkFile( file )) {
            return new ResponseData(400,"文件类型不符合，请上传.md或.txt类型！");
        }
        String filePath="";
        try{
            filePath= courseSaveService.saveFile( file );
        }catch (Exception e){
            return new ResponseData(400,"文件上传失败！");
        }
        Map<String, SectionContentView> sectionViewMap=new HashMap<>(  );
        CourseView courseView= courseSaveService.preview( filePath,sectionViewMap );

        Set<String> keys=sectionViewMap.keySet();
        for (String key:keys){
            System.out.println(key+":"+sectionViewMap.get( key ).getKnowledgeViews().size());
        }
        ResponseData responseData=new ResponseData( );
        responseData.putDataValue( "courseView",courseView );
        responseData.putDataValue( "sectionViewMap",sectionViewMap );//？key为Integer的map无法转化为json ！！

        return responseData;
    }

    //判断是否是管理员
    private boolean isAdm(User user){
        if(user==null)
            return false;
        int uid=user.getUid();
        for (int admUid : admUids) {
            if (uid == admUid) {
                return true;
            }
        }
        return false;
    }

}
