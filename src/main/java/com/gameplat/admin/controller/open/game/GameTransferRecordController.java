package com.gameplat.admin.controller.open.game;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.GameTransferRecordQueryDTO;
import com.gameplat.admin.model.dto.OperGameTransferRecordDTO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GameAdminService;
import com.gameplat.admin.service.GamePlatformService;
import com.gameplat.admin.service.GameTransferRecordService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.game.GamePlatform;
import com.gameplat.model.entity.game.GameTransferRecord;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "游戏额度转换记录")
@Slf4j
@RestController
@RequestMapping("/api/admin/game/gameTransferRecord")
public class GameTransferRecordController {

  @Autowired private GameTransferRecordService gameTransferRecordService;

  @Autowired private GameAdminService gameAdminService;

  @Autowired private GamePlatformService gamePlatformService;

  @Operation(summary = "查询")
  @GetMapping(value = "/queryPage")
  @PreAuthorize("hasAuthority('game:gameTransferRecord:view')")
  public PageDtoVO<GameTransferRecord> queryGameTransferRecord(
      Page<GameTransferRecord> page, GameTransferRecordQueryDTO dto) {
    return gameTransferRecordService.queryGameTransferRecord(page, dto);
  }

  @Operation(summary = "后台补单")
  @PostMapping(value = "/fillOrders")
  @PreAuthorize("hasAuthority('game:gameTransferRecord:fillOrders')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'游戏额度转换记录-->后台补单:' + #dto" )
  public void fillOrders(@RequestBody OperGameTransferRecordDTO dto) throws Exception {
    gameAdminService.fillOrders(dto);
  }

  @Operation(summary = "根据会员ID查询")
  @GetMapping(value = "/queryPlatformByMemberId/{memberId}")
  public List<GamePlatform> queryPlatformByMemberId(@PathVariable("memberId") Long memberId) {
    List<String> platformCodeList =
        gameTransferRecordService.findPlatformCodeList(memberId).stream()
            .map(GameTransferRecord::getPlatformCode)
            .collect(Collectors.toList());
    return gamePlatformService.queryByTransfer().stream()
        .filter(item -> platformCodeList.contains(item.getCode()))
        .collect(Collectors.toList());
  }
}
