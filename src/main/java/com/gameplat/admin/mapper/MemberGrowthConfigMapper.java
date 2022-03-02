package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.dto.MemberGrowthConfigEditDto;
import com.gameplat.model.entity.member.MemberGrowthConfig;

public interface MemberGrowthConfigMapper extends BaseMapper<MemberGrowthConfig> {

  /** 只查询一条 */
  MemberGrowthConfig findOneConfig(String language);

  /** 修改成长值配置 */
  void updateGrowthConfig(MemberGrowthConfigEditDto configEditDto);
}
