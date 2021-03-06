package com.fox.avisys.mapper;


import com.fox.avisys.entity.AutoFloor_AVI_Maintain_Record;

import java.math.BigDecimal;
import java.util.Date;

public interface AutoFloor_AVI_Maintain_RecordMapper {

    Date selectAVIDBDate();

    int deleteByPrimaryKey(BigDecimal id);

    int insert(AutoFloor_AVI_Maintain_Record record);

    int insertSelective(AutoFloor_AVI_Maintain_Record record);

    AutoFloor_AVI_Maintain_Record selectByPrimaryKey(BigDecimal id);

    int updateByPrimaryKeySelective(AutoFloor_AVI_Maintain_Record record);

    int updateByPrimaryKey(AutoFloor_AVI_Maintain_Record record);
}