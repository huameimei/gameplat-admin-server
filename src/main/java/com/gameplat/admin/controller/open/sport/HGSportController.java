package com.gameplat.admin.controller.open.sport;

import com.gameplat.admin.model.dto.HGSportDTO;
import com.gameplat.admin.service.HGSportService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author aBen
 * @date 2022/1/6 0:02
 * @desc 皇冠体育
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/sport/hGSport")
public class HGSportController {

  @Autowired private HGSportService hgSportService;

  @ApiOperation(value = "HG注单列表")
  @GetMapping("/queryHGBetOrder")
  public Object queryHGBetOrder(HGSportDTO dto) {
    return hgSportService.queryHGBetOrder(dto);
  }

  @ApiOperation(value = "HG用户投注限制列表")
  @GetMapping("/queryHGBetLimitList")
  public Object queryHGBetLimitList(HGSportDTO dto) {
    return hgSportService.queryHGBetLimitList(dto);
  }

  @ApiOperation(value = "HG联赛列表")
  @GetMapping("/queryHGSportLeague")
  public Object queryHGSportLeague(HGSportDTO dto) {
    return hgSportService.queryHGSportLeague(dto);
  }

  @ApiOperation(value = "HG赛果列表")
  @GetMapping("/queryHGSportResult")
  public Object queryHGSportResult(HGSportDTO dto) {
    return hgSportService.queryHGSportResult(dto);
  }

  @ApiOperation(value = "HG即时注单详情")
  @GetMapping("/queryHGCurBetOrder")
  public Object queryHGCurBetOrder(HGSportDTO dto) {
    return hgSportService.queryHGCurBetOrder(dto);
  }

  @ApiOperation(value = "HG注单子列表")
  @GetMapping("/queryHGBetsDetail")
  public Object queryHGBetsDetail(HGSportDTO dto) {
    return hgSportService.queryHGBetsDetail(dto);
  }

  @ApiOperation(value = "HG投注设置列表")
  @GetMapping("/queryHGBetConfigs")
  public Object queryHGBetConfigs(HGSportDTO dto) {
    return hgSportService.queryHGBetConfigs(dto);
  }

  @ApiOperation(value = "HG没有赛果的比赛列表")
  @GetMapping("/queryHGEntryResult")
  public Object queryHGEntryResult(HGSportDTO dto) {
    return hgSportService.queryHGEntryResult(dto);
  }

  @ApiOperation(value = "HG单个用户限额列表")
  @GetMapping("/queryHGUserLimit")
  public Object queryHGUserLimit(HGSportDTO dto) {
    return hgSportService.queryHGUserLimit(dto);
  }

  @ApiOperation(value = "HG公告列表")
  @GetMapping("/queryHGSportMessage")
  public Object queryHGSportMessage(HGSportDTO dto) {
    return hgSportService.queryHGSportMessage(dto);
  }

  @ApiOperation(value = "HG赛事变更列表")
  @GetMapping("/queryHGChangeNotice")
  public Object queryHGChangeNotice(HGSportDTO dto) {
    return hgSportService.queryHGChangeNotice(dto);
  }

  @ApiOperation(value = "手动结算-注单详情")
  @GetMapping("/queryHGHandOrdersDetail")
  public Object queryHGHandOrdersDetail(HGSportDTO dto) {
    return hgSportService.queryHGHandOrdersDetail(dto);
  }

  @ApiOperation(value = "参数设置/修改")
  @PostMapping("/updateHgConfigModify")
  public Object updateHgConfigModify(@RequestBody HGSportDTO dto) {
    return hgSportService.updateHgConfigModify(dto);
  }

  @ApiOperation(value = "参数设置列表")
  @GetMapping("/queryHgSportConfig")
  public Object queryHgSportConfig(HGSportDTO dto) {
    return hgSportService.queryHgSportConfig(dto);
  }

  @ApiOperation(value = "输入足球赛果")
  @PostMapping("/inputResultForFoot")
  public Object inputResultForFoot(@RequestBody HGSportDTO dto) {
    return hgSportService.inputResultForFoot(dto);
  }

  @ApiOperation(value = "输入篮球赛果")
  @PostMapping("/inputResultForBasketball")
  public Object inputResultForBasketball(@RequestBody HGSportDTO dto) {
    return hgSportService.inputResultForBasketball(dto);
  }

  @ApiOperation(value = "修改投注限制(停止投注，开启投注)")
  @PostMapping("/updateSportStatus")
  public Object updateSportStatus(@RequestBody HGSportDTO dto) {
    return hgSportService.updateSportStatus(dto);
  }

  @ApiOperation(value = "修改投注限制(单场投注金额)")
  @PostMapping("/updateSportBetLimitMoney")
  public Object updateSportBetLimitMoney(@RequestBody HGSportDTO dto) {
    return hgSportService.updateSportBetLimitMoney(dto);
  }

  @ApiOperation(value = "修改投注设置")
  @PostMapping("/updateBetConfig")
  public Object updateBetConfig(@RequestBody HGSportDTO dto) {
    return hgSportService.updateBetConfig(dto);
  }

  @ApiOperation(value = "重新结算")
  @PostMapping("/betUpdateActions")
  public Object betUpdateActions(@RequestBody HGSportDTO dto) {
    return hgSportService.betUpdateActions(dto);
  }

  @ApiOperation(value = "批量操作体育注单（确认， 取消）")
  @PostMapping("/batchActionsByStatus")
  public Object batchActionsByStatus(@RequestBody HGSportDTO dto) {
    return hgSportService.batchActionsByStatus(dto);
  }

  @ApiOperation(value = "手工结算足球")
  @PostMapping("/settlingResultForFoot")
  public Object settlingResultForFoot(@RequestBody HGSportDTO dto) {
    return hgSportService.settlingResultForFoot(dto);
  }

  @ApiOperation(value = "手工结算篮球")
  @PostMapping("/settlingResultForBasketball")
  public Object settlingResultForBasketball(@RequestBody HGSportDTO dto) {
    return hgSportService.settlingResultForBasketball(dto);
  }

  @ApiOperation(value = "单用户投注设置")
  @PostMapping("/saveUserLimit")
  public Object saveUserLimit(@RequestBody HGSportDTO dto) {
    return hgSportService.saveUserLimit(dto);
  }

  @ApiOperation(value = "单用户投注开关")
  @PostMapping("/updateUserLimit")
  public Object updateUserLimit(@RequestBody HGSportDTO dto) {
    return hgSportService.updateUserLimit(dto);
  }

  @ApiOperation(value = "删除单用户投注限制")
  @PostMapping("/removeUserLimit")
  public Object removeUserLimit(@RequestBody HGSportDTO dto) {
    return hgSportService.removeUserLimit(dto);
  }

  @ApiOperation(value = "添加体育公告")
  @PostMapping("/saveSportMessage")
  public Object saveSportMessage(@RequestBody HGSportDTO dto) {
    return hgSportService.saveSportMessage(dto);
  }

  @ApiOperation(value = "删除体育公告")
  @PostMapping("/removeSportMessage")
  public Object removeSportMessage(@RequestBody HGSportDTO dto) {
    return hgSportService.removeSportMessage(dto);
  }

  @ApiOperation(value = "修改体育公告")
  @PostMapping("/modifySportMessage")
  public Object modifySportMessage(@RequestBody HGSportDTO dto) {
    return hgSportService.modifySportMessage(dto);
  }

  @ApiOperation(value = "赛果变更消息处理")
  @PostMapping("/sportChangeNotice")
  public Object sportChangeNotice(@RequestBody HGSportDTO dto) {
    return hgSportService.sportChangeNotice(dto);
  }

  @ApiOperation(value = "参数查询（指定key）")
  @GetMapping("/queryHGConfigGetOne")
  public Object queryHGConfigGetOne(HGSportDTO dto) {
    return hgSportService.queryHGConfigGetOne(dto);
  }
}
