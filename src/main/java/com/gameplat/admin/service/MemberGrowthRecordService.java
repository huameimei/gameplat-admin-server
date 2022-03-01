package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.MemberGrowthChangeDto;
import com.gameplat.admin.model.dto.MemberGrowthRecordDTO;
import com.gameplat.admin.model.vo.GrowthScaleVO;
import com.gameplat.admin.model.vo.MemberGrowthRecordVO;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.member.MemberGrowthConfig;
import com.gameplat.model.entity.member.MemberGrowthRecord;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface MemberGrowthRecordService extends IService<MemberGrowthRecord> {

  IPage<MemberGrowthRecordVO> findRecordList(
      PageDTO<MemberGrowthRecord> page, MemberGrowthRecordDTO dto);

  Integer dealUpLevel(Long afterGrowth, MemberGrowthConfig memberGrowthConfig);

  List<MemberGrowthRecord> findRecordGroupBy(MemberGrowthRecord entity);

  /** 修改单个会员成长值 */
  void editMemberGrowth(MemberGrowthChangeDto dto, HttpServletRequest request);

  Boolean insertMemberGrowthRecord(MemberGrowthRecord userGrowthRecord);

  GrowthScaleVO progressBar(Integer level, Long memberId);

  /** 处理升级 */
  void dealPayUpReword(
      Integer beforeLevel,
      Integer afterLevel,
      MemberGrowthConfig growthConfig,
      Member member,
      HttpServletRequest request);
}
