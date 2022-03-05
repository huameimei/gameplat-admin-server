package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.bean.ActivityMemberInfo;
import com.gameplat.model.entity.member.MemberGrowthStatis;
import com.gameplat.model.entity.member.MemberWealDetail;

import java.util.List;
import java.util.Map;

public interface MemberGrowthStatisMapper extends BaseMapper<MemberGrowthStatis> {

  /** 获取达到有效投注金额的会员账号 */
  List<MemberWealDetail> getMemberSalaryInfo(Integer type);

  int insertOrUpdate(MemberGrowthStatis memberGrowthStatis);

  List<ActivityMemberInfo> findActivityMemberInfo(Map map);
}
