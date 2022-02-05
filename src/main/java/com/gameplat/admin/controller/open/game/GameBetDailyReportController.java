package com.gameplat.admin.controller.open.game;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.domain.GamePlatform;
import com.gameplat.admin.model.domain.GameBetDailyReport;
import com.gameplat.admin.model.domain.SysDictData;
import com.gameplat.admin.model.dto.GameBetDailyReportQueryDTO;
import com.gameplat.admin.model.dto.OperGameMemberDayReportDTO;
import com.gameplat.admin.model.vo.GameBetReportVO;
import com.gameplat.admin.model.vo.GameReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GamePlatformService;
import com.gameplat.admin.service.GameBetDailyReportService;
import com.gameplat.base.common.exception.ServiceException;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import com.gameplat.base.common.util.DateUtil;
import com.gameplat.base.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/game/gameBetDailyReport")
public class GameBetDailyReportController {

  @Resource
  private GameBetDailyReportService gameBetDailyReportService;

  @Resource
  private GamePlatformService gamePlatformService;


  @GetMapping(value = "/queryPage")
  public PageDtoVO<GameBetDailyReport> queryPage(Page<GameBetDailyReport> page,
      GameBetDailyReportQueryDTO dto) {
    return gameBetDailyReportService.queryPage(page, dto);
  }


  // TODO 导出真人投注日报表


  /**
   * 真人重新生成日报表
   */
  @PostMapping(value = "/resetDayReport")
  public void resetDayReport(@RequestBody OperGameMemberDayReportDTO dto) throws ServiceException {
    List<GamePlatform> list = gamePlatformService.list();
    GamePlatform gamePlatform = list.stream().filter(item ->  item.getCode().equals(dto.getPlatformCode())).findAny().orElse(null);
    if(gamePlatform == null){
      throw new ServiceException("游戏类型不存在！");
    }
    gameBetDailyReportService.saveGameBetDailyReport(dto.getStatTime(), gamePlatform);
  }


  /**
   * 查询真人数据统计
   */
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

  /**
   * 查询投注日报表记录
   */
  @GetMapping(value = "/queryBetReport")
  public PageDtoVO<GameBetReportVO> queryBetReport(Page<GameBetDailyReportQueryDTO> page, GameBetDailyReportQueryDTO dto) {
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
              sb.append("'"+split[i]+"'").append(",");
          }
          sb.deleteCharAt(sb.toString().length()-1);
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
              sb.append("'"+split[i]+"'").append(",");
          }
          sb.deleteCharAt(sb.toString().length()-1);
          sb.append(")");
          String str = sb.toString();
          dto.setPlatformCode(str);
      }



    return gameBetDailyReportService.querybetReportList(page,dto);
  }



}
