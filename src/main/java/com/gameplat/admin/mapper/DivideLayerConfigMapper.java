package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.DivideConfigDTO;
import com.gameplat.admin.model.vo.DivideLayerConfigVo;
import com.gameplat.model.entity.proxy.DivideLayerConfig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/** @Description : 层层代分红模式配置 @Author : cc @Date : 2022/2/22 */
public interface DivideLayerConfigMapper extends BaseMapper<DivideLayerConfig> {
  String countSql =
      "select count(1) from divide_layer_config as dl inner join member as m on dl.user_name = m.account";
  String countSqlWhere = " where m.super_path like concat('%/',#{userName},'/%')";

  /**
   * 分页列表查询
   *
   * @param page
   * @param divideConfigDTO
   * @return
   */
  IPage<DivideLayerConfigVo> pageList(
      PageDTO<DivideLayerConfig> page, @Param("dto") DivideConfigDTO divideConfigDTO);

  /**
   * 根据用户名获取
   *
   * @param userName
   * @return
   */
  @Select("select * from divide_layer_config where user_name = #{userName}")
  DivideLayerConfig getByUserName(@Param("userName") String userName);

  /**
   * 获取团队配置列表
   *
   * @param userName
   * @return
   */
  List<DivideLayerConfigVo> getTeamList(@Param("userName") String userName);

  /**
   * 统计团队配置数量
   *
   * @param userName
   * @return
   */
  @Select(countSql + countSqlWhere)
  Integer countTeam(@Param("userName") String userName);

  /**
   * 获取下级最大返点率
   *
   * @param userName
   * @param code
   * @return
   */
  BigDecimal getChildMaxDivideRatio(@Param("userName") String userName, @Param("code") String code);

  /**
   * 根据游戏编码获取
   *
   * @param userName
   * @param code
   * @return
   */
  String getConfigByGameCode(@Param("userName") String userName, @Param("code") String code);
}
