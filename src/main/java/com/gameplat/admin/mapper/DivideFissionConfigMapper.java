package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.model.entity.proxy.DivideFissionConfig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Description : 裂变分红模式配置 @Author : cc @Date : 2022/2/22
 */
public interface DivideFissionConfigMapper extends BaseMapper<DivideFissionConfig> {
  /**
   * 根据用户名获取
   *
   * @param userName
   * @return
   */
  @Select("select * from divide_fission_config where user_name = #{userName}")
  DivideFissionConfig getByUserName(@Param("userName") String userName);

  /**
   * 根据游戏编码获取配置
   *
   * @param superName
   * @param code
   * @return
   */
  String getConfigByGameCode(@Param("superName") String superName, @Param("code") String code);
}
