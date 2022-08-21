package com.gameplat.admin.controller.open.sport;

import com.gameplat.admin.model.dto.HGSportDTO;
import com.gameplat.admin.service.HGSportService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "皇冠体育")
@Slf4j
@RestController
@RequestMapping("/api/admin/sport/hGSport")
public class HGSportController {

  @Autowired private HGSportService hgSportService;

  @Operation(summary = "HG注单列表")
  @GetMapping("/queryHGBetOrder")
  @PreAuthorize("hasAuthority('sport:hGSport:queryHGBetOrder')")
  public Object queryHgBetOrder(HGSportDTO dto) {
    return hgSportService.queryHGBetOrder(dto);
  }

  @Operation(summary = "HG用户投注限制列表")
  @GetMapping("/queryHGBetLimitList")
  @PreAuthorize("hasAuthority('sport:hGSport:queryHGBetLimitList')")
  public Object queryHgBetLimitList(HGSportDTO dto) {
    return hgSportService.queryHGBetLimitList(dto);
  }

  @Operation(summary = "HG联赛列表")
  @GetMapping("/queryHGSportLeague")
  @PreAuthorize("hasAuthority('sport:hGSport:queryHGSportLeague')")
  public Object queryHgSportLeague(HGSportDTO dto) {
    return hgSportService.queryHGSportLeague(dto);
  }

  @Operation(summary = "HG赛果列表")
  @GetMapping("/queryHGSportResult")
  @PreAuthorize("hasAuthority('sport:hGSport:queryHGSportResult')")
  public Object queryHgSportResult(HGSportDTO dto) {
    return hgSportService.queryHGSportResult(dto);
  }

  @Operation(summary = "HG即时注单详情")
  @GetMapping("/queryHGCurBetOrder")
  //  @PreAuthorize("hasAuthority('sport:hGSport:queryHGCurBetOrder')")
  public Object queryHgCurBetOrder(HGSportDTO dto) {
    return hgSportService.queryHGCurBetOrder(dto);
  }

  @Operation(summary = "HG注单子列表")
  @GetMapping("/queryHGBetsDetail")
  @PreAuthorize("hasAuthority('sport:hGSport:queryHGBetsDetail')")
  public Object queryHgBetsDetail(HGSportDTO dto) {
    return hgSportService.queryHGBetsDetail(dto);
  }

  @Operation(summary = "HG投注设置列表")
  @GetMapping("/queryHGBetConfigs")
  @PreAuthorize("hasAuthority('sport:hGSport:queryHGBetConfigs')")
  public Object queryHgBetConfigs(HGSportDTO dto) {
    return hgSportService.queryHGBetConfigs(dto);
  }

  @Operation(summary = "HG没有赛果的比赛列表")
  @GetMapping("/queryHGEntryResult")
  @PreAuthorize("hasAuthority('sport:hGSport:queryHGEntryResult')")
  public Object queryHgEntryResult(HGSportDTO dto) {
    return hgSportService.queryHGEntryResult(dto);
  }

  @Operation(summary = "HG单个用户限额列表")
  @GetMapping("/queryHGUserLimit")
  @PreAuthorize("hasAuthority('sport:hGSport:queryHGUserLimit')")
  public Object queryHgUserLimit(HGSportDTO dto) {
    return hgSportService.queryHGUserLimit(dto);
  }

  @Operation(summary = "HG公告列表")
  @GetMapping("/queryHGSportMessage")
  @PreAuthorize("hasAuthority('sport:hGSport:queryHGSportMessage')")
  public Object queryHgSportMessage(HGSportDTO dto) {
    return hgSportService.queryHGSportMessage(dto);
  }

  @Operation(summary = "HG赛事变更列表")
  @GetMapping("/queryHGChangeNotice")
  @PreAuthorize("hasAuthority('sport:hGSport:queryHGChangeNotice')")
  public Object queryHgChangeNotice(HGSportDTO dto) {
    return hgSportService.queryHGChangeNotice(dto);
  }

  @Operation(summary = "手动结算-注单详情")
  @GetMapping("/queryHGHandOrdersDetail")
  @PreAuthorize("hasAuthority('sport:hGSport:queryHGHandOrdersDetail')")
  public Object queryHgHandOrdersDetail(HGSportDTO dto) {
    return hgSportService.queryHGHandOrdersDetail(dto);
  }

  @Operation(summary = "参数设置/修改")
  @PostMapping("/updateHgConfigModify")
  @PreAuthorize("hasAuthority('sport:hGSport:updateHgConfigModify')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'皇冠体育-->参数设置/修改:' + #dto")
  public Object updateHgConfigModify(@RequestBody HGSportDTO dto) {
    return hgSportService.updateHgConfigModify(dto);
  }

  @Operation(summary = "参数设置列表")
  @GetMapping("/queryHgSportConfig")
  @PreAuthorize("hasAuthority('sport:hGSport:queryHgSportConfig')")
  public Object queryHgSportConfig(HGSportDTO dto) {
    return hgSportService.queryHgSportConfig(dto);
  }

  @Operation(summary = "输入足球赛果")
  @PostMapping("/inputResultForFoot")
  @PreAuthorize("hasAuthority('sport:hGSport:inputResultForFoot')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'皇冠体育-->输入足球赛果:' + #dto")
  public Object inputResultForFoot(@RequestBody HGSportDTO dto) {
    return hgSportService.inputResultForFoot(dto);
  }

  @Operation(summary = "输入篮球赛果")
  @PostMapping("/inputResultForBasketball")
  @PreAuthorize("hasAuthority('sport:hGSport:inputResultForBasketball')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'皇冠体育-->输入篮球赛果:' + #dto")
  public Object inputResultForBasketball(@RequestBody HGSportDTO dto) {
    return hgSportService.inputResultForBasketball(dto);
  }

  @Operation(summary = "修改投注限制(停止投注，开启投注)")
  @PostMapping("/updateSportStatus")
  @PreAuthorize("hasAuthority('sport:hGSport:updateSportStatus')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'皇冠体育-->修改投注限制(停止投注，开启投注):' + #dto")
  public Object updateSportStatus(@RequestBody HGSportDTO dto) {
    return hgSportService.updateSportStatus(dto);
  }

  @Operation(summary = "修改投注限制(单场投注金额)")
  @PostMapping("/updateSportBetLimitMoney")
  @PreAuthorize("hasAuthority('sport:hGSport:updateSportBetLimitMoney')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'皇冠体育-->修改投注限制(单场投注金额):' + #dto")
  public Object updateSportBetLimitMoney(@RequestBody HGSportDTO dto) {
    return hgSportService.updateSportBetLimitMoney(dto);
  }

  @Operation(summary = "修改投注设置")
  @PostMapping("/updateBetConfig")
  @PreAuthorize("hasAuthority('sport:hGSport:updateBetConfig')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'皇冠体育-->修改投注设置:' + #dto")
  public Object updateBetConfig(@RequestBody HGSportDTO dto) {
    return hgSportService.updateBetConfig(dto);
  }

  @Operation(summary = "重新结算")
  @PostMapping("/betUpdateActions")
  @PreAuthorize("hasAuthority('sport:hGSport:betUpdateActions')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'皇冠体育-->重新结算:' + #dto")
  public Object betUpdateActions(@RequestBody HGSportDTO dto) {
    return hgSportService.betUpdateActions(dto);
  }

  @Operation(summary = "批量操作体育注单（确认， 取消）")
  @PostMapping("/batchActionsByStatus")
  @PreAuthorize("hasAuthority('sport:hGSport:batchActionsByStatus')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'皇冠体育-->批量操作体育注单:' + #dto")
  public Object batchActionsByStatus(@RequestBody HGSportDTO dto) {
    return hgSportService.batchActionsByStatus(dto);
  }

  @Operation(summary = "手工结算足球")
  @PostMapping("/settlingResultForFoot")
  @PreAuthorize("hasAuthority('sport:hGSport:settlingResultForFoot')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'皇冠体育-->手工结算足球:' + #dto")
  public Object settlingResultForFoot(@RequestBody HGSportDTO dto) {
    return hgSportService.settlingResultForFoot(dto);
  }

  @Operation(summary = "手工结算篮球")
  @PostMapping("/settlingResultForBasketball")
  @PreAuthorize("hasAuthority('sport:hGSport:settlingResultForBasketball')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'皇冠体育-->手工结算篮球:' + #dto")
  public Object settlingResultForBasketball(@RequestBody HGSportDTO dto) {
    return hgSportService.settlingResultForBasketball(dto);
  }

  @Operation(summary = "单用户投注设置")
  @PostMapping("/saveUserLimit")
  @PreAuthorize("hasAuthority('sport:hGSport:saveUserLimit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'皇冠体育-->单用户投注开关:' + #dto")
  public Object saveUserLimit(@RequestBody HGSportDTO dto) {
    return hgSportService.saveUserLimit(dto);
  }

  @Operation(summary = "单用户投注开关")
  @PostMapping("/updateUserLimit")
  @PreAuthorize("hasAuthority('sport:hGSport:updateUserLimit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'皇冠体育-->单用户投注开关:' + #dto")
  public Object updateUserLimit(@RequestBody HGSportDTO dto) {
    return hgSportService.updateUserLimit(dto);
  }

  @Operation(summary = "删除单用户投注限制")
  @PostMapping("/removeUserLimit")
  @PreAuthorize("hasAuthority('sport:hGSport:removeUserLimit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'皇冠体育-->删除单用户投注限制:' + #dto")
  public Object removeUserLimit(@RequestBody HGSportDTO dto) {
    return hgSportService.removeUserLimit(dto);
  }

  @Operation(summary = "添加体育公告")
  @PostMapping("/saveSportMessage")
  @PreAuthorize("hasAuthority('sport:hGSport:saveSportMessage')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'皇冠体育-->添加体育公告:' + #dto")
  public Object saveSportMessage(@RequestBody HGSportDTO dto) {
    return hgSportService.saveSportMessage(dto);
  }

  @Operation(summary = "删除体育公告")
  @PostMapping("/removeSportMessage")
  @PreAuthorize("hasAuthority('sport:hGSport:removeSportMessage')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'皇冠体育-->删除体育公告:' + #dto")
  public Object removeSportMessage(@RequestBody HGSportDTO dto) {
    return hgSportService.removeSportMessage(dto);
  }

  @Operation(summary = "修改体育公告")
  @PostMapping("/modifySportMessage")
  @PreAuthorize("hasAuthority('sport:hGSport:modifySportMessage')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'皇冠体育-->修改体育公告:' + #dto")
  public Object modifySportMessage(@RequestBody HGSportDTO dto) {
    return hgSportService.modifySportMessage(dto);
  }

  @Operation(summary = "赛果变更消息处理")
  @PostMapping("/sportChangeNotice")
  @PreAuthorize("hasAuthority('sport:hGSport:sportChangeNotice')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'皇冠体育-->赛果变更消息处理:' + #dto")
  public Object sportChangeNotice(@RequestBody HGSportDTO dto) {
    return hgSportService.sportChangeNotice(dto);
  }

  @Operation(summary = "参数查询（指定key）")
  @GetMapping("/queryHGConfigGetOne")
  @PreAuthorize("hasAuthority('sport:hGSport:queryHGConfigGetOne')")
  public Object queryHgConfigGetOne(HGSportDTO dto) {
    return hgSportService.queryHGConfigGetOne(dto);
  }
}
