package com.kgc.wxdemo.controller;

import com.kgc.wxdemo.config.ExcelUploadUtils;
import com.kgc.wxdemo.config.JxlExcel;
import com.kgc.wxdemo.entity.User;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @version v1.0
 * @ProjectName: wxdemo
 * @ClassName: JxlExcelController
 * @Description: TODO(一句话描述该类的功能)
 * @Author: 李茜骏
 * @Date: 2020/6/16 21:49
 */
@Controller
public class ExcelController {



    @RequestMapping(value = "/")
    public String into(){
        return "aaa";
    }


    @RequestMapping(value = "/uplode")
    public void uplode(MultipartFile file)throws Exception{
        //ExcelUploadUtils excelUploadUtils = new ExcelUploadUtils();
//        List<String[]> excelData = ExcelUploadUtils.getExcelData(file);
//        List<User> list = new ArrayList<>();
//        for(int i=0;i<excelData.size();i++){
//            String[] strings = excelData.get(i);
//            User user = new User();
//            user.setName(strings[0]);
//            user.setPassword(strings[1]);
//            user.setGender(strings[2]);
//            list.add(user);
//        }
        List<User> list = JxlExcel.readExcel(file);
        for (User user : list) {
            System.out.println(user);
        }
    }

    @RequestMapping(value = "get")
    public void get(HttpServletRequest request, HttpServletResponse response){
        String fileName = "测试一号.xls";
        List<User> list = new ArrayList<>();
        User user = new User();
        user.setName("xian");
        user.setPassword("123456");
        user.setGender("男");
        User user1 = new User();
        user1.setName("xian1");
        user1.setPassword("123456");
        user1.setGender("男");
        User user2 = new User();
        user2.setName("xian2");
        user2.setPassword("123456");
        user2.setGender("男");
        list.add(user);
        list.add(user1);
        list.add(user2);
        HSSFWorkbook wb = JxlExcel.getHSSFWorkbook(list);

        try {
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
