package com.gameplat.admin.controller.open.account;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.enums.BlacklistConstant.BizBlacklistType;
import com.gameplat.admin.model.domain.BizBlacklist;
import com.gameplat.admin.model.dto.BizBlacklistQueryDTO;
import com.gameplat.admin.model.dto.OperBizBlacklistDTO;
import com.gameplat.admin.model.dto.OptionDTO;
import com.gameplat.admin.service.BizBlacklistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/account/bizBlack")
public class OpenBizBlacklistController {

  @Autowired private BizBlacklistService bizBlacklistService;

  @DeleteMapping("/del/{id}")
  @PreAuthorize("hasAuthority('account:bizBlack:del')")
  public void delete(@PathVariable Long id) {
    bizBlacklistService.delete(id);
  }

  @GetMapping("/list")
  @PreAuthorize("hasAuthority('account:bizBlack:list')")
  public IPage<BizBlacklist> findAccountBlacklist(
      PageDTO<BizBlacklist> page, BizBlacklistQueryDTO bizBlacklistQueryDTO) {
    return bizBlacklistService.queryBizBlacklistList(page, bizBlacklistQueryDTO);
  }

  @GetMapping(value = "{id}")
  public BizBlacklist get(@PathVariable Long id) {
    return bizBlacklistService.getById(id);
  }

  @PostMapping("/add")
  @PreAuthorize("hasAuthority('account:bizBlack:add')")
  public void add(@RequestBody OperBizBlacklistDTO dto) {
    bizBlacklistService.save(dto);
  }

  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('account:bizBlack:edit')")
  public void edit(@RequestBody OperBizBlacklistDTO dto) {
    bizBlacklistService.update(dto);
  }

  @GetMapping(value = "biz-blacklist-types")
  public List<OptionDTO<String>> bizBlacklistTypes() {
    return Stream.of(BizBlacklistType.values())
        .map(t -> new OptionDTO<>(t.getValue(), t.getText()))
        .collect(Collectors.toList());
  }
}
