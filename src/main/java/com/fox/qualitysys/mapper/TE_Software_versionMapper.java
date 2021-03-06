package com.fox.qualitysys.mapper;

import com.fox.qualitysys.entity.TE_Software_version;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TE_Software_versionMapper {

    List<TE_Software_version> selectSoftwareVersion(@Param("product") String  product);

}