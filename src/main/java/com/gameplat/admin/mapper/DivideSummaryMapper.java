package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.model.entity.proxy.DivideSummary;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/** @Description : 分红汇总 @Author : cc @Date : 2022/2/22 */
public interface DivideSummaryMapper extends BaseMapper<DivideSummary> {

  /**
   * 获取期数最大的代理等级
   *
   * @param periodsId
   * @return
   */
  Integer getMaxLevel(@Param("periodsId") Long periodsId);

  /**
   * 根据用户名获取
   *
   * @param userName
   * @return
   */
  @Select(
      "select * from divide_summary where account = #{userName} order by create_time desc limit 1")
  DivideSummary getByUserName(@Param("userName") String userName);
}
