package com.gameplat.admin.controller.open.account;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.enums.BlacklistConstant.BizBlacklistType;
import com.gameplat.admin.model.dto.BizBlacklistQueryDTO;
import com.gameplat.admin.model.dto.OperBizBlacklistDTO;
import com.gameplat.admin.model.dto.OptionDTO;
import com.gameplat.admin.service.BizBlacklistService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.blacklist.BizBlacklist;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Tag(name = "业务黑名单")
@RestController
@RequestMapping("/api/admin/account/bizBlack")
public class OpenBizBlacklistController {

  @Autowired private BizBlacklistService bizBlacklistService;

  @Operation(summary = "删除")
  @PostMapping("/del/{id}")
  @PreAuthorize("hasAuthority('account:bizBlack:remove')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type = LogType.ADMIN,
    desc = "'业务黑名单->删除id:' + #dto.id")
  public void delete(@PathVariable Long id) {
    bizBlacklistService.delete(id);
  }

  @Operation(summary = "查询")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('account:bizBlack:view')")
  public IPage<BizBlacklist> findAccountBlacklist(
      PageDTO<BizBlacklist> page, BizBlacklistQueryDTO dto) {
    return bizBlacklistService.queryBizBlacklistList(page, dto);
  }

  @Operation(summary = "根据ID获取")
  @GetMapping(value = "{id}")
  public BizBlacklist get(@PathVariable Long id) {
    return bizBlacklistService.getById(id);
  }

  @Operation(summary = "添加")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('account:bizBlack:add')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type = LogType.ADMIN,
    desc = "'业务黑名单->添加targetType:' + #dto.targetType + 'target:' " +
      "+ #dto.target + 'types' + #dto.types + 'status' + #dto.status")
  public void add(@RequestBody OperBizBlacklistDTO dto) {
    bizBlacklistService.save(dto);
  }

  @Operation(summary = "编辑")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('account:bizBlack:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type = LogType.ADMIN,
    desc = "'业务黑名单->编辑targetType:' + #dto.targetType + 'target:' " +
      "+ #dto.target + 'types' + #dto.types + 'status' + #dto.status" +
      "'id:' + dto.id")
  public void edit(@RequestBody OperBizBlacklistDTO dto) {
    bizBlacklistService.update(dto);
  }

  @Operation(summary = "获取黑名单类型")
  @GetMapping(value = "biz-blacklist-types")
  public List<OptionDTO<String>> bizBlacklistTypes() {
    return Stream.of(BizBlacklistType.values())
        .map(t -> new OptionDTO<>(t.getValue(), t.getText()))
        .collect(Collectors.toList());
  }
}
