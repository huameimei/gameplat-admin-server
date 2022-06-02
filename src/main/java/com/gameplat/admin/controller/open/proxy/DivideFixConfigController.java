package com.gameplat.admin.controller.open.proxy;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.DivideConfigDTO;
import com.gameplat.admin.service.DivideFixConfigService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.proxy.DivideFixConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 固定比例分红模式
 *
 * @author cc
 */
@Tag(name = "固定比例分红模式")
@RestController
@RequestMapping("/api/admin/divide/fix")
public class DivideFixConfigController {

  @Autowired private DivideFixConfigService fixConfigService;

  @Operation(summary = "固定分红比例分页列表")
  @GetMapping("/pageList")
  @PreAuthorize("hasAuthority('agent:bonusFixconfig:view')")
  public IPage<DivideFixConfig> list(PageDTO<DivideFixConfig> page, DivideConfigDTO queryObj) {
    LambdaQueryWrapper<DivideFixConfig> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper
        .eq(
            StrUtil.isNotBlank(queryObj.getUserName()),
            DivideFixConfig::getUserName,
            queryObj.getUserName())
        .orderByDesc(DivideFixConfig::getCreateTime);
    return fixConfigService.page(page, queryWrapper);
  }

  @PostMapping("/add")
  @Operation(summary = "新增固定比例分红配置")
  @PreAuthorize("hasAuthority('agent:bonusFixconfig:add')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "'新增固定比例分红配置'")
  public void add(@Validated @RequestBody DivideConfigDTO divideConfigDTO) {
    fixConfigService.add(divideConfigDTO.getUserName(), "zh-CN");
  }

  @Operation(summary = "编辑前获取固定比例配置")
  @GetMapping("/getFixConfigForEdit")
  public Map<String, Object> getFixConfigForEdit(DivideConfigDTO dto) {
    return fixConfigService.getFixConfigForEdit(dto.getUserName(), "zh-CN");
  }

  @PostMapping("/edit")
  @Operation(summary = "编辑固定比例分红配置")
  @PreAuthorize("hasAuthority('agent:bonusFixconfig:edit')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "'新增固定比例分红配置'")
  public void edit(@Validated @RequestBody DivideConfigDTO dto) {
    fixConfigService.edit(dto, "zh-CN");
  }

  @Operation(summary = "删除固定分红配置")
  @PostMapping("/delete")
  @PreAuthorize("hasAuthority('agent:bonusFixconfig:remove')")
  public void remove(@RequestBody Map<String, String> map) {
    if (StringUtils.isBlank(map.get("ids"))) {
      throw new ServiceException("ids不能为空");
    }
    fixConfigService.remove(map.get("ids"));
  }
}
