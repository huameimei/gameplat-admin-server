package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.proxy.DivideSummary;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Description : 分红汇总
 * @Author : cc
 * @Date : 2022/2/22
 */
public interface DivideSummaryMapper extends BaseMapper<DivideSummary> {

    Integer getMaxLevel(@Param("periodsId") Long periodsId);
}
