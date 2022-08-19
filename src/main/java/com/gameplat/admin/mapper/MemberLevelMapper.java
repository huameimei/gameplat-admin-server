package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.model.entity.member.MemberInfo;
import com.gameplat.model.entity.member.MemberLevel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MemberLevelMapper extends BaseMapper<MemberLevel> {

  /**
   * 根据会员层级获取待分层会员
   *
   * @param level Integer
   * @return List
   */
  List<MemberInfo> getMemberInfoByLevel(Integer level);

  /**
   * 批量修改层级会员数
   *
   * @param map Map
   */
  void batchUpdateMemberNum(@Param("map") Map<Integer, Integer> map);

  MemberLevel getLevelLike(@Param("levelName") String levelName);

  Integer getMaxLevelValue();
}
