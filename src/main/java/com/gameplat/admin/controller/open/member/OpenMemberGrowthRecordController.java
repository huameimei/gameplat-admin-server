package com.gameplat.admin.controller.open.member;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.MemberGrowthChangeDto;
import com.gameplat.admin.model.dto.MemberGrowthRecordDTO;
import com.gameplat.admin.model.vo.GrowthScaleVO;
import com.gameplat.admin.model.vo.MemberGrowthRecordVO;
import com.gameplat.admin.service.MemberGrowthRecordService;
import com.gameplat.admin.service.MemberGrowthStatisService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.lang.Assert;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.member.MemberGrowthRecord;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户成长值记录
 *
 * @author lily
 */
@Slf4j
@Tag(name = "VIP成长记录")
@RestController
@RequestMapping("/api/admin/member/growthRecord")
public class OpenMemberGrowthRecordController {

  @Autowired private MemberGrowthRecordService memberGrowthRecordService;

  @Autowired private MemberGrowthStatisService memberGrowthStatisService;

  @GetMapping("/list")
  @Operation(summary = "查询成长值记录列表")
  @PreAuthorize("hasAuthority('member:growthRecord:view')")
  public IPage<MemberGrowthRecordVO> listWealGrowthRecord(
      PageDTO<MemberGrowthRecord> page, MemberGrowthRecordDTO dto) {
    dto.setLanguage(LocaleContextHolder.getLocale().toLanguageTag());
    return memberGrowthRecordService.findRecordList(page, dto);
  }

  @PostMapping("/editGrowth")
  @Operation(summary = "修改单个会员成长值")
  @PreAuthorize("hasAuthority('member:growthRecord:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'VIP成长记录-->修改单个会员成长值:' + #dto" )
  public void editMemberGrowth(@RequestBody MemberGrowthChangeDto dto, HttpServletRequest request) {
    log.info("单个会员成长值变动：MemberGrowthRecord={}", dto);
    if (dto == null || dto.getChangeGrowth() == null) {
      throw new ServiceException("参数不全！");
    }
    if (dto.getChangeGrowth() == 0) {
      throw new ServiceException("扣除/添加成长值不能为0！");
    }
    dto.setType(3);
    memberGrowthStatisService.changeGrowth(dto);
  }

  @GetMapping("/getBar")
  @Operation(summary = "进度条")
  @PreAuthorize("hasAuthority('member:growthRecord:view')")
  public GrowthScaleVO progressBar(Integer level, Long memberId) {
    Assert.isTrue(ObjectUtils.isNotNull(level) && ObjectUtils.isNotNull(memberId), "参数不全!");
    return memberGrowthRecordService.progressBar(level, memberId);
  }
}
