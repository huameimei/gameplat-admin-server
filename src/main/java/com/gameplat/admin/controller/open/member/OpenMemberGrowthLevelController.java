package com.gameplat.admin.controller.open.member;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.gameplat.admin.model.dto.MemberGrowthConfigEditDto;
import com.gameplat.admin.model.vo.MemberConfigLevelVO;
import com.gameplat.admin.model.vo.MemberGrowthLevelVO;
import com.gameplat.admin.service.MemberGrowthConfigService;
import com.gameplat.admin.service.MemberGrowthLevelService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.security.SecurityUserHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户成长等级
 *
 * @author lily
 */
@Slf4j
@Tag(name = "VIP等级配置")
@RestController
@RequestMapping("/api/admin/member/growthLevel")
public class OpenMemberGrowthLevelController {

  @Autowired private MemberGrowthLevelService levelService;

  @Autowired private MemberGrowthConfigService configService;

  @Operation(summary = "VIP配置和VIP等级列表/查询logo配置列表")
  @GetMapping("/config")
  //  @PreAuthorize("hasAuthority('member:growthLevel:config')")
  public MemberConfigLevelVO getLevelConfig() {
    return levelService.getLevelConfig();
  }

  @Operation(summary = "修改VIP配置")
  @PostMapping("/update")
  @PreAuthorize("hasAuthority('member:growthLevel:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'VIP等级配置-->修改VIP配置:' + #dto" )
  // @ApiParam(name = "修改VIP配置入参", value = "传入json格式", required = true)
  public void update(@Validated @RequestBody MemberGrowthConfigEditDto dto) {
    if (ObjectUtils.isEmpty(dto.getId())) {
      throw new ServiceException(" 编号不能为空");
    }

    dto.setUpdateBy(SecurityUserHolder.getUsername());
    dto.setLanguage(LocaleContextHolder.getLocale().toLanguageTag());
    configService.updateGrowthConfig(dto);
  }

  @Operation(summary = "后台批量修改VIP等级")
  @PreAuthorize("hasAuthority('member:growthLevel:edit')")
  @PostMapping("/updateLevel")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'VIP等级配置-->后台批量修改VIP等级:' + #obj" )
  public void batchUpdateLevel(@RequestBody JSONObject obj, HttpServletRequest request) {
    levelService.batchUpdateLevel(obj, request);
  }

  @Operation(summary = "VIP等级列表")
  @GetMapping("/vipList")
  //  @PreAuthorize("hasAuthority('member:growthLevel:config')")
  public List<MemberGrowthLevelVO> vipList() {
    return levelService.vipList();
  }
}
