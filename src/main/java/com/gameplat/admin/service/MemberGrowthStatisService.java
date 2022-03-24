package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.bean.ActivityMemberInfo;
import com.gameplat.admin.model.dto.MemberGrowthStatisDTO;
import com.gameplat.admin.model.vo.MemberGrowthConfigVO;
import com.gameplat.admin.model.vo.MemberGrowthStatisVO;
import com.gameplat.model.entity.member.MemberGrowthStatis;

import java.util.List;
import java.util.Map;

public interface MemberGrowthStatisService extends IService<MemberGrowthStatis> {

  IPage<MemberGrowthStatisVO> findStatisList(
      PageDTO<MemberGrowthStatis> page, MemberGrowthStatisDTO dto);

  List<MemberGrowthStatis> findList(MemberGrowthStatisDTO dto);

  Integer dealUpLevel(Integer afterGrowth, MemberGrowthConfigVO memberGrowthConfig);

  void insertOrUpdate(MemberGrowthStatis userGrowthStatis);

  List<ActivityMemberInfo> findActivityMemberInfo(Map map);

  MemberGrowthStatis findOrInsert(Long memberId,String account);
}
