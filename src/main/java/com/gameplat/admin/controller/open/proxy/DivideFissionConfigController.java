package com.gameplat.admin.controller.open.proxy;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.DivideConfigDTO;
import com.gameplat.admin.service.DivideFissionConfigService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.proxy.DivideFissionConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description : 裂变分红模式 @Author : cc @Date : 2022/4/2
 */
@Tag(name = "裂变分红模式")
@RestController
@RequestMapping("/api/admin/divide/fission")
public class DivideFissionConfigController {

  @Autowired private DivideFissionConfigService fissionConfigService;

  /**
   * 裂变配置分页裂变
   *
   * @param page
   * @param dto
   * @return
   */
  @GetMapping("/pageList")
  @PreAuthorize("hasAuthority('agent:bonusFissionconfig:view')")
  public IPage<DivideFissionConfig> list(PageDTO<DivideFissionConfig> page, DivideConfigDTO dto) {
    LambdaQueryWrapper<DivideFissionConfig> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper
        .eq(
            StrUtil.isNotBlank(dto.getUserName()),
            DivideFissionConfig::getUserName,
            dto.getUserName())
        .orderByDesc(DivideFissionConfig::getCreateTime);
    return fissionConfigService.page(page, queryWrapper);
  }

  /**
   * 添加裂变配置
   *
   * @param dto
   */
  @PostMapping("/add")
  @Operation(summary = "新增裂变分红配置")
  @PreAuthorize("hasAuthority('agent:bonusFissionconfig:add')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "'新增裂变分红配置'")
  public void add(@Validated @RequestBody DivideConfigDTO dto) {
    fissionConfigService.add(dto.getUserName(), "zh-CN");
  }

  /**
   * 编辑前获取基础配置
   *
   * @param dto
   * @return
   */
  @GetMapping("/getFissionConfigForEdit")
  public Map<String, Object> getFissionConfigForEdit(DivideConfigDTO dto) {
    return fissionConfigService.getFissionConfigForEdit(dto.getUserName(), "zh-CN");
  }

  /**
   * 编辑裂变分红配置
   *
   * @param dto
   */
  @PostMapping("/edit")
  @Operation(summary = "编辑裂变分红配置")
  @PreAuthorize("hasAuthority('agent:bonusFissionconfig:edit')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "'编辑裂变分红配置'")
  public void edit(@Validated @RequestBody DivideConfigDTO dto) {
    fissionConfigService.edit(dto, "zh-CN");
  }

  /**
   * 删除裂变分红配置
   *
   * @param map
   */
  @Operation(summary = "删除裂变分红配置")
  @PostMapping("/delete")
  @PreAuthorize("hasAuthority('agent:bonusFissionconfig:remove')")
  public void remove(@RequestBody Map<String, String> map) {
    if (StringUtils.isBlank(map.get("ids"))) {
      throw new ServiceException("ids不能为空");
    }
    fissionConfigService.remove(map.get("ids"));
  }
}
