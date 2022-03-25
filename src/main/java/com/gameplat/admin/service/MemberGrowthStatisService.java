package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.bean.ActivityMemberInfo;
import com.gameplat.admin.model.dto.MemberGrowthChangeDto;
import com.gameplat.admin.model.dto.MemberGrowthStatisDTO;
import com.gameplat.admin.model.vo.MemberGrowthConfigVO;
import com.gameplat.admin.model.vo.MemberGrowthStatisVO;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.member.MemberGrowthConfig;
import com.gameplat.model.entity.member.MemberGrowthStatis;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface MemberGrowthStatisService extends IService<MemberGrowthStatis> {

  IPage<MemberGrowthStatisVO> findStatisList(
      PageDTO<MemberGrowthStatis> page, MemberGrowthStatisDTO dto);

  List<MemberGrowthStatis> findList(MemberGrowthStatisDTO dto);

  Integer dealUpLevel(Long afterGrowth, MemberGrowthConfig memberGrowthConfig);

  void insertOrUpdate(MemberGrowthStatis userGrowthStatis);

  List<ActivityMemberInfo> findActivityMemberInfo(Map map);

  MemberGrowthStatis findOrInsert(Long memberId,String account);

  /**
   * 成长值变动
   */
  void changeGrowth(MemberGrowthChangeDto dto);

  /**
   * 根据变动前后的等级  计算奖励金额
   */
  BigDecimal calcRewordAmount(Integer beforeLevel, Integer afterLevel, MemberGrowthConfig growthConfig);

  /**
   * 处理升级
   */
  void dealPayUpReword(Integer beforeLevel, Integer afterLevel, MemberGrowthConfig growthConfig, Member member);

  /**
   * 异步 变动成长值
   */
  void asyncChangeGrowth(MemberGrowthChangeDto dto);

  /**
   * 修改成长值汇总数据
   * */
  Boolean updateMemberGrowthStatis(MemberGrowthStatis entity);
}
