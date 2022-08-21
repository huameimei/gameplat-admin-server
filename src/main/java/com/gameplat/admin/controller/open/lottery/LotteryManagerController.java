package com.gameplat.admin.controller.open.lottery;

import com.gameplat.admin.service.NewLotteryForwardService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@Tag(name = "彩票管理")
@RestController
@RequestMapping("/api/admin/lottery/")
public class LotteryManagerController {

  @Autowired private NewLotteryForwardService serviceHandler;

  @Operation(summary = "获取彩系和彩种列表")
  @PostMapping("/getLottTypeList")
  //  @PreAuthorize("hasAuthority('lottery:lottTypeList')")
  public Object getLottTypeList(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "获取号码对应的12生肖")
  @PostMapping("/get12Sx")
  @PreAuthorize("hasAuthority('lottery:preset:get12Sx')")
  public Object get12Sx(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "开奖结果--列表")
  @PostMapping("/getOpenResult")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object getOpenResult(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "开奖结果--编辑")
  @PostMapping("/editOpenNumber")
  @PreAuthorize("hasAuthority('lottery:open:editOpenNumber')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "彩票管理-->开奖结果--编辑" )
  public Object editOpenNumber(HttpServletRequest request, String lottCode, Long expectNo) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "开奖结果--补彩列表")
  @PostMapping("/buCaiList")
  @PreAuthorize("hasAuthority('lottery:open:buCaiList')")
  public Object buCaiList(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "开奖结果--补彩")
  @PostMapping("/buCai")
  @PreAuthorize("hasAuthority('lottery:open:buCai')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "彩票管理-->开奖结果--补彩" )
  public Object buCai(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "开奖结果--结算")
  @PostMapping("/settlement")
  @PreAuthorize("hasAuthority('lottery:open:settlement')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "彩票管理-->开奖结果--结算" )
  public Object settlement(HttpServletRequest request, String lottCode, Long expectNo) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "新接入--预设开奖--彩系和彩种列表")
  @PostMapping("/getTypeLottList")
  @PreAuthorize("hasAuthority('lottery:open:getTypeLottList')")
  public Object getTypeLottList(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "预设开奖--彩系和彩种列表")
  @PostMapping("/getPresetLottList")
  @PreAuthorize("hasAuthority('lottery:preset:getPresetLottList')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "彩票管理-->预设开奖--彩系和彩种列表" )
  public Object getPresetLottList(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "开奖预设开奖--批量添加预设号码")
  @PostMapping("/addPresetNumber")
  @PreAuthorize("hasAuthority('lottery:preset:addPresetNumber')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "彩票管理-->开奖预设开奖--批量添加预设号码" )
  public Object addPresetNumber(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "预设开奖--列表")
  @PostMapping("/getPresetNumberList")
  @PreAuthorize("hasAuthority('lottery:preset:getOpenResult')")
  public Object getPresetNumberList(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "预设开奖--批量删除/删除")
  @PostMapping("/deletePresetNumber")
  @PreAuthorize("hasAuthority('lottery:preset:deletePresetNumber')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "彩票管理-->预设开奖--批量删除/删除" )
  public Object deletePresetNumber(HttpServletRequest request, String lottCode, String expectNos) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "预设开奖--批量查看/查看")
  @PostMapping("/showPresetNumber")
  @PreAuthorize("hasAuthority('lottery:preset:showPresetNumber')")
  public Object showPresetNumber(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "预设开奖--批量查看/查看--校验谷歌验证码")
  @PostMapping("/googleEvrify")
  @PreAuthorize("hasAuthority('lottery:preset:googleEvrify')")
  public Object googleEvrify(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "检查是否有编辑权限")
  @PostMapping("/checkEdit")
  @PreAuthorize("hasAuthority('lottery:checkEdit')")
  public Object checkEdit(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "预设开奖--可见彩种管理--权限列表")
  @PostMapping("/roleList")
  @PreAuthorize("hasAuthority('lottery:role:view')")
  public Object roleList(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "预设开奖--可见彩种管理--保存/撤销权限/保存权限")
  @PostMapping("/roleUpdate")
  @PreAuthorize("hasAuthority('lottery:role:edit')")
  public Object roleUpdate(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "获取开奖时间")
  @PostMapping("/getOpenTime")
  @PreAuthorize("hasAuthority('lottery:open:getOpenTime')")
  public Object getOpenTime(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "获取彩系列表")
  @PostMapping("/getLottTypeLists")
  @PreAuthorize("hasAuthority('lottery:type:view')")
  public Object getLottTypeLists(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "彩系修改")
  @PostMapping("/updateLottType")
  @PreAuthorize("hasAuthority('lottery:type:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "彩票管理-->彩系修改" )
  public Object updateLottType(HttpServletRequest request, String groupCode) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "获取彩种列表")
  @PostMapping("/getLottList")
  @PreAuthorize("hasAuthority('lottery:kind:getLottList')")
  public Object getLottList(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "修改彩种信息")
  @PostMapping("/updateLottInfo")
  @PreAuthorize("hasAuthority('lottery:kind:updateLottInfo')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "彩票管理-->修改彩种信息" )
  public Object updateLottInfo(HttpServletRequest request, String lottCode) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "彩票设置--批量修改")
  @PostMapping("/batchUpdateLottInfo")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "彩票管理-->彩票设置--批量修改" )
  public Object batchUpdateLottInfo(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "投注额配置(信用/官方)--一级玩法列表")
  @PostMapping("/getOnePlayList")
  @PreAuthorize("hasAuthority('lottery:bet:getOnePlayList')")
  public Object getOnePlayList(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/getBetConfine")
  @PreAuthorize("hasAuthority('lottery:bet:getBetConfine')")
  @Operation(summary = "投注额配置(信用/官方)--投注限制列表")
  public Object getBetConfine(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/getBetRestriction")
  @Operation(summary = "投注额配置(信用/官方)--获取单期最高投注额")
  @PreAuthorize("hasAuthority('lottery:bet:getBetRestriction')")
  public Object getBetRestriction(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/updateBetRestriction")
  @Operation(summary = "投注额配置(信用/官方)--修改单期最高投注额")
  @PreAuthorize("hasAuthority('lottery:bet:updateBetRestriction')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "彩票管理-->修改单期最高投注额" )
  public Object updateBetRestriction(
      HttpServletRequest request, String lottCode, BigDecimal expectRestriction) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/updateBetConfine")
  @Operation(summary = "投注额配置(信用/官方)--保存修改")
  @PreAuthorize("hasAuthority('lottery:bet:updateBetConfine')")
  public Object updateBetConfine(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/lottConfineList")
  @Operation(summary = "彩种配置--列表")
  @PreAuthorize("hasAuthority('lottery:play:lottConfineList')")
  public Object lottConfineList(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/updateLottConfine")
  @Operation(summary = "玩法配置--保存修改")
  @PreAuthorize("hasAuthority('lottery:play:updateLottConfine')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "彩票管理-->玩法配置--保存修改" )
  public Object updateLottConfine(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/getPlayList")
  @PreAuthorize("hasAuthority('lottery:playList:view')")
  @Operation(summary = "赔率设置(信用/官方)--获取玩法赔率列表")
  public Object getPlayList(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "彩种玩法配置--一级玩法开关修改")
  @PostMapping("/onePlayUpdate")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "彩票管理-->彩种玩法配置--一级玩法开关修改" )
  public Object onePlayUpdate(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "赔率设置(信用/官方)--批量修改赔率")
  @PostMapping("/updatePlayOdds")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "彩票管理-->彩种玩法配置--赔率设置(信用/官方)--批量修改赔率" )
  public Object updatePlayOdds(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "赔率设置(信用/官方)--同步其它相同彩种相同玩法的赔率")
  @PostMapping("/syncUpdatePlayOdds")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object syncUpdatePlayOdds(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "赔率设置(信用/官方)--同彩种同步相同玩法的赔率")
  @PostMapping("/tSyncUpdatePlayOdds")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object tSyncUpdatePlayOdds(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "未结算注单--列表")
  @PostMapping("/noSettlementBet")
  @PreAuthorize("hasAuthority('lottery:noSettlementBet:view')")
  public Object noSettlementBet(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "未结算注单--删除")
  @PostMapping("/deleteNoSettlementBet")
  @PreAuthorize("hasAuthority('lottery:noSettlementBet:remove')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "彩票管理-->未结算注单--删除" )
  public Object deleteNoSettlementBet(HttpServletRequest request, String infoOrderNo) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "未结算注单--批量注销记录/注销")
  @PostMapping("/updateNoSettlementBet")
  @PreAuthorize("hasAuthority('lottery:noSettlementBet:update')")
  public Object updateNoSettlementBet(HttpServletRequest request, String infoOrderNos) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "获取一级玩法和二级玩法 ")
  @PostMapping("/getOnePlay")
  @PreAuthorize("hasAuthority('lottery:play:getOnePlay')")
  public Object getOnePlay(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "即时注单-列表数据")
  @PostMapping("/geFourPlay")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object geFourPlay(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "即时注单")
  @PostMapping("/betList")
  @PreAuthorize("hasAuthority('lottery:bet:betList')")
  public Object betList(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "获取当前期号信息")
  @PostMapping("/getExpectNo")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object getExpectNo(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "即时注单--玩法和彩种统计")
  @PostMapping("/playLottCount")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object playLottCount(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "即时注单汇总")
  @PostMapping("/betSum")
  @PreAuthorize("hasAuthority('lottery:bet:betSum')")
  public Object betSum(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "注单记录")
  @PostMapping("/betRecord")
  @PreAuthorize("hasAuthority('lottery:bet:record')")
  public Object betRecord(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @SneakyThrows
  @Operation(summary = "导出注单记录")
  @PostMapping("/exportBetRecord")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "彩票管理-->导出注单记录" )
  public Object exportBetRecord(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "注单分析--列表")
  @PostMapping("/betAnalysis")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object betAnalysis(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "注单分析--详情")
  @PostMapping("/betAnalysisDetails")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object betAnalysisDetails(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "注单分析--详情--详情")
  @PostMapping("/betAnalysisBetDetails")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object betAnalysisBetDetails(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "注单分析--对打分析")
  @PostMapping("/betAnalysisVsAnalysis")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object betAnalysisVsAnalysis(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "彩票输赢记录")
  @PostMapping("/lottWinRecord")
  @PreAuthorize("hasAuthority('lottery:winRecord:view')")
  public Object lottWinRecord(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "杀率设置--获取杀率信息")
  @PostMapping("/getKillInfo")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "彩票管理-->杀率设置--获取杀率信息" )
  public Object getKillInfo(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "杀率设置--更新杀率信息")
  @PostMapping("/updateKillInfo")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "彩票管理-->杀率设置--更新杀率信息" )
  public Object updateKillInfo(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "批量获取随机号码")
  @PostMapping("/getRandomNumber")
  @PreAuthorize("hasAuthority('lottery:racing:getRandomNumber')")
  public Object getRandomNumber(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "获取对打注单列表")
  @PostMapping("/getSparringOrderList")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object getSparringOrderList(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "获取对打注单详情")
  @GetMapping("/getSparringOrderDetails")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object getSparringOrderDetails(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/playRuleExplain")
  @PreAuthorize("hasAuthority('lottery:play:playRuleExplain')")
  @Operation(summary = "玩法说明-列表数据")
  public Object playRuleExplain(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/getBannerList")
  @Operation(summary = "彩票banner配置--列表数据")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object getBannerList(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/updateBanner")
  @Operation(summary = "彩票banner配置--修改数据/删除")
  @PreAuthorize("hasAuthority('lottery:open:editOpenResult')")
  public Object updateBanner(HttpServletRequest request, Integer id) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/addBanner")
  @Operation(summary = "彩票banner配置--添加数据")
  @PreAuthorize("hasAuthority('lottery:open:addOpenResult')")
  public Object addBanner(HttpServletRequest request, String lang) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/getNotice")
  @Operation(summary = "公告配置-列表")
  @PreAuthorize("hasAuthority('lottery:notice:view')")
  public Object getNotice(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/addNotice")
  @Operation(summary = "公告配置-新增")
  @PreAuthorize("hasAuthority('lottery:notice:add')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "彩票管理-->公告配置-新增" )
  public Object addNotice(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/updateNotice")
  @Operation(summary = "公告配置-编辑")
  @PreAuthorize("hasAuthority('lottery:notice:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "彩票管理-->公告配置-编辑" )
  public Object updateNotice(HttpServletRequest request, Integer id) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/deleteNotice")
  @Operation(summary = "公告配置-删除")
  @PreAuthorize("hasAuthority('lottery:notice:remove')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "彩票管理-->公告配置-删除" )
  public Object deleteNotice(HttpServletRequest request, String id) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/getLhcTmb")
  @Operation(summary = "特码B-查询反水比例")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object getLhcTmb(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/updateLhcTmb")
  @Operation(summary = "特码B-反水比例修改")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object updateLhcTmb(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/getToDayPayoutSet")
  @Operation(summary = "今日派奖设置-设置")
  @PreAuthorize("hasAuthority('lottery:distribute:getToDayPayoutSet')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "彩票管理-->今日派奖设置-设置" )
  public Object getToDayPayoutSet(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/toDayPayoutSet")
  @Operation(summary = "今日派奖设置-设置")
  @PreAuthorize("hasAuthority('lottery:distribute:toDayPayoutSet')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "彩票管理-->今日派奖设置-设置" )
  public Object toDayPayoutSet(
      HttpServletRequest request,
      BigDecimal initNumber,
      BigDecimal initMoney,
      BigDecimal addNumber,
      BigDecimal addMoney) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/getFundRecord")
  @Operation(summary = "资金流水记录")
  //  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object getFundRecord(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/changesType")
  @Operation(summary = "资金类型")
  //  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object changesType(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/getUserInfo")
  @Operation(summary = "获取用户信息")
  //  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object getUserInfo(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/updateBlack")
  @Operation(summary = "更新用户官彩黑名单")
  //  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "彩票管理-->更新用户官彩黑名单" )
  public Object updateBlack(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/updateBetLimit")
  @Operation(summary = "更新用户投注限额")
  //  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "彩票管理-->更新用户投注限额" )
  public Object updateBetLimit(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/updateRebate")
  @Operation(summary = "更新用户反水等级")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "彩票管理-->更新用户反水等级" )
  //  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object updateRebate(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "竞速彩预设开奖--彩系和彩种列表")
  @PostMapping("getJscPresetLottList")
  @PreAuthorize("hasAuthority('lottery:racing:getJscPresetLottList')")
  public Object getJscPresetLottList(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "竞速彩预设开奖--列表")
  @PostMapping("getJscPresetNumberList")
  @PreAuthorize("hasAuthority('lottery:racing:getJscPresetNumberList')")
  public Object getJscPresetNumberList(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "竞速彩预设开奖--批量删除-删除")
  @PostMapping("deleteJscPresetNumber")
  @PreAuthorize("hasAuthority('lottery:racing:deleteJscPresetNumber')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "彩票管理-->竞速彩预设开奖--批量删除-删除" )
  public Object deleteJscPresetNumber(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "竞速彩开奖预设--批量添加预设号码")
  @PostMapping("addJscPresetNumber")
  @PreAuthorize("hasAuthority('lottery:racing:addJscPresetNumber')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "彩票管理-->竞速彩开奖预设--批量添加预设号码" )
  public Object addJscPresetNumber(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @Operation(summary = "批量竞速彩获取随机号码")
  @PostMapping("getJscRandomNumber")
  @PreAuthorize("hasAuthority('lottery:racing:getJscRandomNumber')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "彩票管理-->批量竞速彩获取随机号码" )
  public Object getJscRandomNumber(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }
}
