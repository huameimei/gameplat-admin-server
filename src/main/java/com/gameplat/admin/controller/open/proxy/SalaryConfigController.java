package com.gameplat.admin.controller.open.proxy;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.SalaryConfigDTO;
import com.gameplat.admin.model.vo.SalaryConfigVO;
import com.gameplat.admin.service.SalaryConfigService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.proxy.SalaryConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description : 工资配置 @Author : cc @Date : 2022/4/2
 */
@Tag(name = "工资配置")
@RestController
@RequestMapping("/api/admin/salary/config")
public class SalaryConfigController {

  @Autowired private SalaryConfigService salaryConfigService;

  /**
   * 分页列表
   *
   * @param page
   * @param dto
   * @return
   */
  @Operation(summary = "工资配置列表")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('salary:config:view')")
  public IPage<SalaryConfigVO> list(PageDTO<SalaryConfig> page, SalaryConfigDTO dto) {
    return salaryConfigService.queryPage(page, dto);
  }

  /**
   * 最大代理层级
   *
   * @return
   */
  @GetMapping("/maxLevel")
  public Integer getMaxLevel() {
    return salaryConfigService.getMaxLevel();
  }

  /**
   * 添加
   *
   * @param dto
   */
  @PostMapping("/add")
  @Operation(summary = "新增工资配置")
  @PreAuthorize("hasAuthority('salary:config:add')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "'新增工资配置'")
  public void add(@Validated @RequestBody SalaryConfigDTO dto) {
    salaryConfigService.add(dto);
  }

  /**
   * 编辑
   *
   * @param dto
   */
  @PostMapping("/edit")
  @Operation(summary = "编辑工资配置")
  @PreAuthorize("hasAuthority('salary:config:edit')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "'编辑工资配置'")
  public void edit(@Validated @RequestBody SalaryConfigDTO dto) {
    salaryConfigService.edit(dto);
  }

  /**
   * 删除
   *
   * @param map
   */
  @Operation(summary = "删除期数")
  @PostMapping("/delete")
  @PreAuthorize("hasAuthority('salary:config:remove')")
  public void remove(@RequestBody Map<String, String> map) {
    if (StringUtils.isBlank(map.get("ids"))) {
      throw new ServiceException("ids不能为空");
    }
    salaryConfigService.delete(map.get("ids"));
  }
}
