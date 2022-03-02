package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.dto.MemberGrowthLevelEditDto;
import com.gameplat.model.entity.member.MemberGrowthLevel;

import java.util.List;

public interface MemberGrowthLevelMapper extends BaseMapper<MemberGrowthLevel> {

  List<MemberGrowthLevel> findList(Integer limitLevel, String language);

  /** 批量修改VIP等级 */
  int batchUpdateLevel(List<MemberGrowthLevelEditDto> list);

  /** 根据等级查询 */
  String findLevelName(Integer level);
}
