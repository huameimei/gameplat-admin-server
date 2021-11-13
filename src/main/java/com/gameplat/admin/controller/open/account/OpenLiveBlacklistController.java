package com.gameplat.admin.controller.open.account;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.LiveBlacklist;
import com.gameplat.admin.model.dto.LiveBlacklistQueryDTO;
import com.gameplat.admin.model.dto.OperLiveBlacklistDTO;
import com.gameplat.admin.service.LiveBlacklistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/account/liveBlack")
public class OpenLiveBlacklistController {

  @Autowired private LiveBlacklistService liveBlacklistService;

  @DeleteMapping("/del/{id}")
  @PreAuthorize("hasAuthority('account:liveBlack:del')")
  public void delete(@PathVariable Long id) {
    liveBlacklistService.delete(id);
  }

  @GetMapping("/list")
  @PreAuthorize("hasAuthority('account:liveBlack:list')")
  public IPage<LiveBlacklist> findAccountBlacklist(
      PageDTO<LiveBlacklist> page, LiveBlacklistQueryDTO liveBlacklistQueryDTO) {
    return liveBlacklistService.queryLiveBlacklistList(page, liveBlacklistQueryDTO);
  }

  @PostMapping("/add")
  @PreAuthorize("hasAuthority('account:liveBlack:add')")
  public void add(@RequestBody OperLiveBlacklistDTO dto) {
    liveBlacklistService.save(dto);
  }

  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('account:liveBlack:edit')")
  public void edit(@RequestBody OperLiveBlacklistDTO dto) {
    liveBlacklistService.update(dto);
  }
}
