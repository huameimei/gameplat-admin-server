package com.gameplat.admin.controller.open.operator;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.RedEnvelopeConfigDTO;
import com.gameplat.admin.model.dto.UserRedEnvelopeDTO;
import com.gameplat.admin.model.vo.UserRedEnvelopeVO;
import com.gameplat.admin.service.RedEnvelopeConfigService;
import com.gameplat.admin.service.UserRedEnvelopeService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.recharge.RedEnvelopeConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "红包配置")
@RestController
@RequestMapping("/api/admin/operator/red")
public class RedEnvelopeInfoController {

  @Autowired private UserRedEnvelopeService userRedEnvelopeService;

  @Autowired private RedEnvelopeConfigService redEnvelopeService;

  @Operation(summary = "新增红包配置")
  @PostMapping("redAdd")
  @PreAuthorize("hasAuthority('operator:red:add')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'红包配置-->新增红包配置:' + #dto")
  public Object redAdd(@RequestBody RedEnvelopeConfigDTO dto) {
    return redEnvelopeService.redAdd(dto);
  }

  @Operation(summary = "红包配置列表")
  @GetMapping("/redList")
  @PreAuthorize("hasAuthority('operator:red:view')")
  public Object redList(PageDTO<RedEnvelopeConfig> page, RedEnvelopeConfigDTO dto) {
    return redEnvelopeService.redList(page, dto);
  }

  @Operation(summary = "红包设置")
  @PostMapping("/redEdit")
  @PreAuthorize("hasAuthority('operator:red:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'红包配置-->红包设置:' + #dto")
  public Object redEdit(@RequestBody RedEnvelopeConfigDTO dto) {
    return redEnvelopeService.redEdit(dto);
  }

  @Operation(summary = "红包删除")
  @PostMapping("/redDelete")
  @PreAuthorize("hasAuthority('operator:red:remove')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'红包配置-->红包删除:' + #ids")
  public Object redDelete(@RequestBody List<Integer> ids) {
    return redEnvelopeService.redDelete(ids);
  }

  @Operation(summary = "红包记录")
  @GetMapping("/recordList")
  @PreAuthorize("hasAuthority('operator:red:view')")
  public IPage<UserRedEnvelopeVO> recordList(UserRedEnvelopeDTO dto) {
    return userRedEnvelopeService.recordList(dto);
  }

  @Operation(summary = "红包回收")
  @PostMapping("/redRecycle")
  @PreAuthorize("hasAuthority('operator:red:reply')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'红包配置-->红包回收:'")
  public Object redRecycle() {
    return null;
  }
}
