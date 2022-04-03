package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.model.entity.proxy.SalaryConfig;

/** @Description : 工资配置 @Author : cc @Date : 2022/4/2 */
public interface SalaryConfigMapper extends BaseMapper<SalaryConfig> {
  /**
   * 获取符合代理层级和游戏大类的工资配置
   *
   * @param agentLevel
   * @param kindCode
   * @return
   */
  SalaryConfig getConfig(Integer agentLevel, String kindCode);
}
