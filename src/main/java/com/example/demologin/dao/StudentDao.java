package com.example.demologin.dao;

import com.example.demologin.entity.Student;
import org.apache.ibatis.annotations.Param;

public interface StudentDao {

    Student getStu(@Param("sid")int sid,@Param("pwd")String pwd);
}
