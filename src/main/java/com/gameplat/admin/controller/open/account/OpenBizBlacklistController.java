package com.gameplat.admin.controller.open.account;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.enums.BlacklistConstant.BizBlacklistType;
import com.gameplat.admin.model.dto.BizBlacklistQueryDTO;
import com.gameplat.admin.model.dto.OperBizBlacklistDTO;
import com.gameplat.admin.model.dto.OptionDTO;
import com.gameplat.admin.service.BizBlacklistService;
import com.gameplat.model.entity.blacklist.BizBlacklist;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Api(tags = "业务黑名单")
@Slf4j
@RestController
@RequestMapping("/api/admin/account/bizBlack")
public class OpenBizBlacklistController {

  @Autowired private BizBlacklistService bizBlacklistService;

  @ApiOperation("删除")
  @PostMapping("/del/{id}")
  @PreAuthorize("hasAuthority('account:bizBlack:remove')")
  public void delete(@PathVariable Long id) {
    bizBlacklistService.delete(id);
  }

  @ApiOperation("查询")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('account:bizBlack:view')")
  public IPage<BizBlacklist> findAccountBlacklist(
      PageDTO<BizBlacklist> page, BizBlacklistQueryDTO dto) {
    return bizBlacklistService.queryBizBlacklistList(page, dto);
  }

  @ApiOperation("根据ID获取")
  @GetMapping(value = "{id}")
  public BizBlacklist get(@PathVariable Long id) {
    return bizBlacklistService.getById(id);
  }

  @ApiOperation("添加")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('account:bizBlack:add')")
  public void add(@RequestBody OperBizBlacklistDTO dto) {
    bizBlacklistService.save(dto);
  }

  @ApiOperation("编辑")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('account:bizBlack:edit')")
  public void edit(@RequestBody OperBizBlacklistDTO dto) {
    bizBlacklistService.update(dto);
  }

  @ApiOperation("获取黑名单类型")
  @GetMapping(value = "biz-blacklist-types")
  public List<OptionDTO<String>> bizBlacklistTypes() {
    return Stream.of(BizBlacklistType.values())
        .map(t -> new OptionDTO<>(t.getValue(), t.getText()))
        .collect(Collectors.toList());
  }
}
