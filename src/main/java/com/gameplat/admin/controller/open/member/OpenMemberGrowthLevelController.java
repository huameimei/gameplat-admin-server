package com.gameplat.admin.controller.open.member;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.gameplat.admin.enums.LanguageEnum;
import com.gameplat.admin.model.dto.MemberGrowthConfigEditDto;
import com.gameplat.admin.model.vo.MemberConfigLevelVO;
import com.gameplat.admin.model.vo.MemberGrowthLevelVO;
import com.gameplat.admin.service.MemberGrowthConfigService;
import com.gameplat.admin.service.MemberGrowthLevelService;
import com.gameplat.base.common.context.GlobalContextHolder;
import com.gameplat.base.common.exception.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author lily
 * @description 用户成长等级
 * @date 2021/11/20
 */
@Api(tags = "VIP等级配置")
@Slf4j
@RestController
@RequestMapping("/api/admin/member/growthLevel")
public class OpenMemberGrowthLevelController {

  @Autowired private MemberGrowthLevelService levelService;

  @Autowired private MemberGrowthConfigService configService;

  @ApiOperation(value = "VIP配置和VIP等级列表/查询logo配置列表")
  @GetMapping("/config")
  @PreAuthorize("hasAuthority('member:growthLevel:config')")
  public MemberConfigLevelVO getLevelConfig(
      @ApiParam(name = "language", value = "语言") @RequestParam(required = false) String language) {
    return levelService.getLevelConfig(language);
  }

  @ApiOperation(value = "修改VIP配置")
  @PreAuthorize("hasAuthority('member:growthLevel:edit')")
  @PutMapping("/update")
  public void update(
      @ApiParam(name = "修改VIP配置入参", value = "传入json格式", required = true) @Validated @RequestBody
          MemberGrowthConfigEditDto configEditDto) {

    if (ObjectUtils.isEmpty(configEditDto.getId())) {
      throw new ServiceException(" 编号不能为空");
    }
    configEditDto.setUpdateBy(GlobalContextHolder.getContext().getUsername());
    if (StrUtil.isBlank(configEditDto.getLanguage())) {
      configEditDto.setLanguage(LanguageEnum.app_zh_CN.getCode());
    }
    configService.updateGrowthConfig(configEditDto);
  }

  @ApiOperation(value = "后台批量修改VIP等级")
  @PreAuthorize("hasAuthority('member:growthLevel:updateLevel')")
  @PutMapping("/updateLevel")
  public void batchUpdateLevel(@RequestBody JSONObject obj, HttpServletRequest request) {
    levelService.batchUpdateLevel(obj, request);
  }

  @ApiOperation(value = "VIP等级列表")
  @GetMapping("/vipList")
  @PreAuthorize("hasAuthority('member:growthLevel:config')")
  public List<MemberGrowthLevelVO> vipList(
      @ApiParam(name = "language", value = "语言") @RequestParam(required = false) String language) {
    return levelService.vipList(language);
  }
}
