package com.fox.avisys.mapper;


import com.fox.avisys.entity.AutoFloor_AVI_Employee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AutoFloor_AVI_EmployeeMapper {

    List<AutoFloor_AVI_Employee> selectALLEmployee();

    String selectBYEmployeeNum(@Param("employeeNum") String employeeNum);

}