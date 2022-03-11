package com.gameplat.admin.controller.open.game;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.GameBetDailyReportQueryDTO;
import com.gameplat.admin.model.dto.GameBetRecordQueryDTO;
import com.gameplat.admin.model.dto.OperGameMemberDayReportDTO;
import com.gameplat.admin.model.dto.UserGameBetRecordDto;
import com.gameplat.admin.model.vo.GameBetRecordVO;
import com.gameplat.admin.model.vo.GameBetReportVO;
import com.gameplat.admin.model.vo.GameReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GameBetDailyReportService;
import com.gameplat.admin.service.GameBetRecordInfoService;
import com.gameplat.admin.service.GamePlatformService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.DateUtil;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.model.entity.game.GameBetDailyReport;
import com.gameplat.model.entity.game.GamePlatform;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin/game/gameBetDailyReport")
public class GameBetDailyReportController {

  @Autowired private GameBetDailyReportService gameBetDailyReportService;

  @Autowired private GamePlatformService gamePlatformService;

  @GetMapping(value = "/queryPage")
  public PageDtoVO<GameBetDailyReport> queryPage(
      Page<GameBetDailyReport> page, GameBetDailyReportQueryDTO dto) {
    return gameBetDailyReportService.queryPage(page, dto);
  }

  /**
   * 游戏平台维度数据统计
   * @param dto
   * @return
   */
  @GetMapping(value = "/queryGamePlatformReport")
  public List<GameReportVO> queryGamePlatformReport(GameBetDailyReportQueryDTO dto) {
    return gameBetDailyReportService.queryGamePlatformReport(dto);
  }


  // TODO 导出真人投注日报表


  /**
   * 游戏重新生成日报表
   */
  @PostMapping(value = "/resetDayReport")
  public void resetDayReport(@RequestBody OperGameMemberDayReportDTO dto) {
    List<GamePlatform> list = gamePlatformService.list();
    GamePlatform gamePlatform =
        list.stream()
            .filter(item -> item.getCode().equals(dto.getPlatformCode()))
            .findAny()
            .orElseThrow(() -> new ServiceException("游戏类型不存在！"));
    gameBetDailyReportService.saveGameBetDailyReport(dto.getStatTime(), gamePlatform);
  }

  /** 查询真人数据统计 */
  @GetMapping(value = "/queryReport")
  public List<GameReportVO> queryReport(GameBetDailyReportQueryDTO dto) {
    if (StringUtils.isBlank(dto.getBeginTime())) {
      String beginTime = DateUtil.getDateToString(new Date());
      dto.setBeginTime(beginTime);
    }
    if (StringUtils.isBlank(dto.getEndTime())) {
      String endTime = DateUtil.getDateToString(new Date());
      dto.setEndTime(endTime);
    }
    return gameBetDailyReportService.queryReportList(dto);
  }

  /** 查询投注日报表记录 */
  @GetMapping(value = "/queryBetReport")
  public PageDtoVO<GameBetReportVO> queryBetReport(
      Page<GameBetDailyReportQueryDTO> page, GameBetDailyReportQueryDTO dto) {
    if (StringUtils.isEmpty(dto.getBeginTime())) {
      String beginTime = DateUtil.getDateToString(new Date());
      dto.setBeginTime(beginTime);
    }
    if (StringUtils.isEmpty(dto.getEndTime())) {
      String endTime = DateUtil.getDateToString(new Date());
      dto.setEndTime(endTime);
    }
    if (StringUtils.isNotEmpty(dto.getLiveGameSuperType())) {
      StringBuilder sb = new StringBuilder();
      sb.append("(");
      String[] split = dto.getLiveGameSuperType().split(",");
      for (int i = 0; i < split.length; i++) {
        sb.append("'" + split[i] + "'").append(",");
      }
      sb.deleteCharAt(sb.toString().length() - 1);
      sb.append(")");
      String str = sb.toString();
      dto.setLiveGameSuperType(str);
    }

    if (StringUtils.isNotEmpty(dto.getPlatformCode())) {
      dto.setLiveGameSuperType("");
      StringBuilder sb = new StringBuilder();
      sb.append("(");
      String[] split = dto.getPlatformCode().split(",");
      for (int i = 0; i < split.length; i++) {
        sb.append("'" + split[i] + "'").append(",");
      }
      sb.deleteCharAt(sb.toString().length() - 1);
      sb.append(")");
      String str = sb.toString();
      dto.setPlatformCode(str);
    }

    return gameBetDailyReportService.querybetReportList(page, dto);
  }


  @Autowired
  private GameBetRecordInfoService gameBetRecordInfoService;

  @ApiOperation(value = "获取会员投注记录")
  @GetMapping("findUserGameBetRecord")
  public PageDtoVO<GameBetRecordVO> findUserGameBetRecord(Page<GameBetRecordVO> page, @Valid UserGameBetRecordDto dto) {
    GameBetRecordQueryDTO gameBetRecordQueryDTO = new GameBetRecordQueryDTO();
    BeanUtil.copyProperties(dto,gameBetRecordQueryDTO);
    gameBetRecordQueryDTO.setTimeType(1);
    return gameBetRecordInfoService.queryPageBetRecord(page,gameBetRecordQueryDTO);
  }
}
