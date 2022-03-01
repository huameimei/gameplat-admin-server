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

/**
 * @Description : 层层代分红模式配置 @Author : cc @Date : 2022/2/22
 */
public interface DivideLayerConfigMapper extends BaseMapper<DivideLayerConfig> {
  IPage<DivideLayerConfigVo> pageList(
      PageDTO<DivideLayerConfig> page, @Param("dto") DivideConfigDTO divideConfigDTO);

  @Select("select * from divide_layer_config where user_name = #{userName}")
  DivideLayerConfig getByUserName(@Param("userName") String userName);

  List<DivideLayerConfigVo> getTeamList(@Param("userName") String userName);

  String countSql =
      "select count(1) from divide_layer_config as dl inner join member as m on dl.user_name = m.account";
  String countSqlWhere = " where m.super_path like concat('%/',#{userName},'/%')";

  @Select(countSql + countSqlWhere)
  Integer countTeam(@Param("userName") String userName);

  BigDecimal getChildMaxDivideRatio(@Param("userName") String userName, @Param("code") String code);

  String getConfigByGameCode(@Param("userName") String userName, @Param("code") String code);
}
