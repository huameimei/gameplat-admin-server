package com.gameplat.admin.controller.open.game;

import com.gameplat.admin.model.dto.GameBalanceQueryDTO;
import com.gameplat.admin.model.dto.GameKickOutDTO;
import com.gameplat.admin.model.dto.OperGameTransferRecordDTO;
import com.gameplat.admin.model.vo.GameBalanceVO;
import com.gameplat.admin.model.vo.GameKickOutVO;
import com.gameplat.admin.model.vo.GameRecycleVO;
import com.gameplat.admin.service.GameAdminService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "游戏控制")
@RestController
@RequestMapping("/api/admin/game")
public class GameAdminController {

  @Autowired private GameAdminService gameAdminService;

  @Operation(summary = "查询单个游戏平台余额")
  @GetMapping(value = "/selectGameBalance")
  @PreAuthorize("hasAuthority('game:gameAdmin:select')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'查询会员游戏金额，账号：'+#dto.account+',游戏平台编码：'+#dto.platformCode")
  public GameBalanceVO selectGameBalance(GameBalanceQueryDTO dto) throws Exception {
    return gameAdminService.selectGameBalance(dto);
  }

  @Operation(summary = "转账到游戏")
  @PostMapping(value = "/transferToGame")
  @PreAuthorize("hasAuthority('game:gameAdmin:transfer')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc =
          "'转换会员余额到游戏，账号：'+#record.account+',游戏平台编码：'+#record.platformCode+',金额：'+#record.amount")
  public void transferToGame(@RequestBody OperGameTransferRecordDTO record) throws Exception {
    gameAdminService.transferToGame(record);
  }

  @Operation(summary = "回收单个游戏金额")
  @PostMapping(value = "/recyclingAmount")
  @PreAuthorize("hasAuthority('game:gameAdmin:recycle')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'回收会员游戏金额，账号：'+#dto.account+',游戏平台编码：'+#dto.platformCode")
  public GameRecycleVO recyclingAmount(@RequestBody GameBalanceQueryDTO dto) {
    return gameAdminService.recyclingAmount(dto);
  }

  @Operation(summary = "没收游戏余额")
  @RequestMapping(value = "/confiscated", method = RequestMethod.POST)
  @PreAuthorize("hasAuthority('game:gameAdmin:confiscated')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'没收会员游戏金额，账号='+#dto.account+',游戏平台编码：'+#dto.platformCode")
  public GameRecycleVO confiscatedAmount(@RequestBody GameBalanceQueryDTO dto) {
    return gameAdminService.confiscatedAmount(dto);
  }

  @Operation(summary = "没收所有游戏平台余额")
  @PostMapping(value = "/confiscatedGameByAccount")
  @PreAuthorize("hasAuthority('game:gameAdmin:confiscatedAll')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'没收会员所有游戏平台金额，账号='+#dto.account")
  public List<GameRecycleVO> confiscatedGameByAccount(@RequestBody GameBalanceQueryDTO dto) {
    return gameAdminService.confiscatedGameByAccount(dto.getAccount());
  }

  @Operation(summary = "查询所有平台余额")
  @PostMapping(value = "/selectGameBalanceByAccount")
  @PreAuthorize("hasAuthority('game:gameAdmin:selectAll')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'查询会员所有游戏平台金额，账号='+#dto.account")
  public List<GameBalanceVO> selectGameBalanceByAccount(@RequestBody GameBalanceQueryDTO dto) {
    return gameAdminService.selectGameBalanceByAccount(dto.getAccount());
  }

  @Operation(summary = "一键回收")
  @PostMapping(value = "/recyclingAmountByAccount")
  @PreAuthorize("hasAuthority('game:gameAdmin:recycleAll')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'回收会员所有游戏平台金额，account='+#dto.account")
  public List<GameRecycleVO> reclaimLiveAmountAsync(@RequestBody GameBalanceQueryDTO dto) {
    return gameAdminService.recyclingAmountByAccount(dto.getAccount());
  }

  @Operation(summary = "一键踢出")
  @RequestMapping(value = "/kickOutAll", method = RequestMethod.POST)
  @PreAuthorize("hasAuthority('game:gameAdmin:kickOutAll')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'踢出会员所有游戏平台，账号='+#dto.account")
  public List<GameKickOutVO> kickOutAll(@RequestBody GameKickOutDTO dto) {
    return gameAdminService.kickOutAll(dto);
  }

  @Operation(summary = "踢出单个游戏")
  @RequestMapping(value = "/kickOut", method = RequestMethod.POST)
  @PreAuthorize("hasAuthority('game:gameAdmin:kickOut')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'踢出会员，账号='+#dto.account+',游戏平台编码：'+#dto.platformCode")
  public void kickOut(@RequestBody GameKickOutDTO dto) {
    gameAdminService.kickOut(dto);
  }

  @Operation(summary = "批量踢出")
  @RequestMapping(value = "/batchKickOut", method = RequestMethod.POST)
  @PreAuthorize("hasAuthority('game:gameAdmin:batchKickOut')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'踢出会员，账号='+#dto.account+',游戏平台编码：'+#dto.platformCode")
  public List<GameKickOutVO> batchKickOut(@RequestBody GameKickOutDTO dto) {
    return gameAdminService.batchKickOut(dto);
  }
}
