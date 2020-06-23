package com.example.demologin.controller;

import com.alibaba.fastjson.JSON;
import com.example.demologin.dao.StudentDao;
import com.example.demologin.entity.Result;
import com.example.demologin.entity.Student;
import com.example.demologin.utils.MD5;
import com.example.demologin.utils.RedisUtils;
import com.example.demologin.utils.UserAgentUtils;
import cz.mallat.uasparser.UserAgentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Controller
public class StudentController {
    @Resource
    private StudentDao studentDao;
    @Autowired
    private RedisUtils redisUtils;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public Result loginByPwd(Student student, HttpServletRequest request)throws Exception{

        String ua=request.getHeader("User-Agent");
        System.out.println("ua:"+ua);
        UserAgentInfo userAgentInfo= UserAgentUtils.uaSparser.parse(ua);
        String type=userAgentInfo.getDeviceType();

        Object[] loginResult=this.login(student,type);
        Result result=new Result();
        if (loginResult==null){
            result.setCode(2001);
            result.setMessage("登录失败");
        }else{
            result.setCode(1001);
            result.setMessage("登录成功");
            result.setData(JSON.toJSONString(loginResult));
        }
        return result;
    }

    public Object[] login(Student student,String type)throws Exception{
        Student stuDb=studentDao.getStu(student.getSid(),student.getPwd());
        if (stuDb==null){
            return null;
        }
        //到这里，说明登录信息没有问题，生成token
        String token=this.createToken(stuDb, type);
        //保存
        this.saveToken(token,stuDb);
        return new Object[]{stuDb,token};
    }

    private String createToken(Student student,String type){
        StringBuilder builder=new StringBuilder();
        builder.append("token-");
        builder.append(type+"-");
        String info= MD5.getMD5(student.getSid().toString(),32);
        builder.append(info+"-");
        builder.append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        builder.append(UUID.randomUUID().toString().substring(0,6));
        return builder.toString();
    }

    private void saveToken(String token,Student student){

        String tokenKey="stu"+student.getSid();
        String tokenValue=null;
        if ((tokenValue=(String)redisUtils.get(tokenKey))!=null){
            redisUtils.delete(tokenKey);
            redisUtils.delete(tokenValue);
        }
        redisUtils.set(tokenKey,token,30000);
        redisUtils.set(token, JSON.toJSONString(student),30000);
    }


}
