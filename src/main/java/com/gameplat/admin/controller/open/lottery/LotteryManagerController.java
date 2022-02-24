package com.gameplat.admin.controller.open.lottery;

import com.gameplat.admin.service.NewLotteryForwardService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * @Author kb
 * @Date 2022/2/24 19:22
 * @Version 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/lottery/")
@Slf4j
@ApiOperation("彩票管理")
public class LotteryManagerController {

    @Autowired(required = false)
    private NewLotteryForwardService serviceHandler;



    @ApiOperation("获取彩系和彩种列表")
    @PostMapping("/getLottTypeList")
    public Object getLottTypeList(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("获取号码对应的12生肖")
    @PostMapping("/get12Sx")
    public Object get12Sx(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("开奖结果--列表")
    @PostMapping("/getOpenResult")
    public Object getOpenResult(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("开奖结果--编辑")
    @PostMapping("/editOpenNumber")
    public Object editOpenNumber(HttpServletRequest request, String lottCode, Long expectNo) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("开奖结果--补彩列表")
    @PostMapping("/buCaiList")
    public Object buCaiList(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("开奖结果--补彩")
    @PostMapping("/buCai")
    public Object buCai(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("开奖结果--结算")
    @PostMapping("/settlement")
    public Object settlement(HttpServletRequest request, String lottCode, Long expectNo) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("新接入--预设开奖--彩系和彩种列表")
    @PostMapping("/getTypeLottList")
    public Object getTypeLottList(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("预设开奖--彩系和彩种列表")
    @PostMapping("/getPresetLottList")
    public Object getPresetLottList(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("开奖预设开奖--批量添加预设号码")
    @PostMapping("/addPresetNumber")
    public Object addPresetNumber(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("预设开奖--列表")
    @PostMapping("/getPresetNumberList")
    public Object getPresetNumberList(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("预设开奖--批量删除/删除")
    @PostMapping("/deletePresetNumber")
    public Object deletePresetNumber(HttpServletRequest request, String lottCode, String expectNos) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("预设开奖--批量查看/查看")
    @PostMapping("/showPresetNumber")
    public Object showPresetNumber(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("预设开奖--批量查看/查看--校验谷歌验证码")
    @PostMapping("/googleEvrify")
    public Object googleEvrify(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("检查是否有编辑权限")
    @PostMapping("/checkEdit")
    public Object checkEdit(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("预设开奖--可见彩种管理--权限列表")
    @PostMapping("/roleList")
    public Object roleList(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("预设开奖--可见彩种管理--保存/撤销权限/保存权限")
    @PostMapping("/roleUpdate")
    public Object roleUpdate(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("获取开奖时间")
    @PostMapping("/getOpenTime")
    public Object getOpenTime(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("获取彩系列表")
    @PostMapping("/getLottTypeLists")
    public Object getLottTypeLists(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("彩系修改")
    @PostMapping("/updateLottType")
    public Object updateLottType(HttpServletRequest request, String groupCode) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("获取彩种列表")
    @PostMapping("/getLottList")
    public Object getLottList(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("修改彩种信息")
    @PostMapping("/updateLottInfo")
    public Object updateLottInfo(HttpServletRequest request, String lottCode) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("彩票设置--批量修改")
    @PostMapping("/batchUpdateLottInfo")
    public Object batchUpdateLottInfo(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("投注额配置(信用/官方)--一级玩法列表")
    @PostMapping("/getOnePlayList")
    public Object getOnePlayList(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @PostMapping("/getBetConfine")
    @ApiOperation("投注额配置(信用/官方)--投注限制列表")
    public Object getBetConfine(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @PostMapping("/getBetRestriction")
    @ApiOperation("投注额配置(信用/官方)--获取单期最高投注额")
    public Object getBetRestriction(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @PostMapping("/updateBetRestriction")
    @ApiOperation("投注额配置(信用/官方)--修改单期最高投注额")
    public Object updateBetRestriction(HttpServletRequest request, String lottCode,  BigDecimal expectRestriction) {
        return serviceHandler.serviceHandler(request);
    }

    @PostMapping("/updateBetConfine")
    @ApiOperation("投注额配置(信用/官方)--保存修改")
    public Object updateBetConfine(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @PostMapping("/lottConfineList")
    @ApiOperation("彩种配置--列表")
    public Object lottConfineList(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @PostMapping("/updateLottConfine")
    @ApiOperation("玩法配置--保存修改")
    public Object updateLottConfine(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @PostMapping("/getPlayList")
    @ApiOperation("赔率设置(信用/官方)--获取玩法赔率列表")
    public Object getPlayList(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("彩种玩法配置--一级玩法开关修改")
    @PostMapping("/onePlayUpdate")
    public Object onePlayUpdate(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("赔率设置(信用/官方)--批量修改赔率")
    @PostMapping("/updatePlayOdds")
    public Object updatePlayOdds(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("赔率设置(信用/官方)--同步其它相同彩种相同玩法的赔率")
    @PostMapping("/syncUpdatePlayOdds")
    public Object syncUpdatePlayOdds(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("赔率设置(信用/官方)--同彩种同步相同玩法的赔率")
    @PostMapping("/tSyncUpdatePlayOdds")
    public Object tSyncUpdatePlayOdds(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("未结算注单--列表")
    @PostMapping("/noSettlementBet")
    public Object noSettlementBet(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("未结算注单--删除")
    @PostMapping("/deleteNoSettlementBet")
    public Object deleteNoSettlementBet(HttpServletRequest request, String infoOrderNo) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("未结算注单--批量注销记录/注销")
    @PostMapping("/updateNoSettlementBet")
    public Object updateNoSettlementBet(HttpServletRequest request, String infoOrderNos) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("获取一级玩法和二级玩法 ")
    @PostMapping("/getOnePlay")
    public Object getOnePlay(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("即时注单-列表数据")
    @PostMapping("/geFourPlay")
    public Object geFourPlay(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("即时注单")
    @PostMapping("/betList")
    public Object betList(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("获取当前期号信息")
    @PostMapping("/getExpectNo")
    public Object getExpectNo(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("即时注单--玩法和彩种统计")
    @PostMapping("/playLottCount")
    public Object playLottCount(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("即时注单汇总")
    @PostMapping("/betSum")
    public Object betSum(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("注单记录")
    @PostMapping("/betRecord")
    public Object betRecord(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @SneakyThrows
    @ApiOperation("导出注单记录")
    @PostMapping("/exportBetRecord")
    public Object exportBetRecord(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("注单分析--列表")
    @PostMapping("/betAnalysis")
    public Object betAnalysis(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("注单分析--详情")
    @PostMapping("/betAnalysisDetails")
    public Object betAnalysisDetails(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("注单分析--详情--详情")
    @PostMapping("/betAnalysisBetDetails")
    public Object betAnalysisBetDetails(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("注单分析--对打分析")
    @PostMapping("/betAnalysisVsAnalysis")
    public Object betAnalysisVsAnalysis(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("彩票输赢记录")
    @PostMapping("/lottWinRecord")
    public Object lottWinRecord(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("杀率设置--获取杀率信息")
    @PostMapping("/getKillInfo")
    public Object getKillInfo(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("杀率设置--更新杀率信息")
    @PostMapping("/updateKillInfo")
    public Object updateKillInfo(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }


    @ApiOperation("批量获取随机号码")
    @PostMapping("/getRandomNumber")
    public Object getRandomNumber(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("获取对打注单列表")
    @PostMapping("/getSparringOrderList")
    public Object getSparringOrderList(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @ApiOperation("获取对打注单详情")
    @GetMapping("/getSparringOrderDetails")
    public Object getSparringOrderDetails(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @PostMapping("/playRuleExplain")
    @ApiOperation("玩法说明-列表数据")
    public Object playRuleExplain(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @PostMapping("/getBannerList")
    @ApiOperation("彩票banner配置--列表数据")
    public Object getBannerList(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @PostMapping("/updateBanner")
    @ApiOperation("彩票banner配置--修改数据/删除")
    public Object updateBanner(HttpServletRequest request, Integer id) {
        return serviceHandler.serviceHandler(request);
    }

    @PostMapping("/addBanner")
    @ApiOperation("彩票banner配置--添加数据")
    public Object addBanner(HttpServletRequest request, String lang) {
        return serviceHandler.serviceHandler(request);
    }

    @PostMapping("/getNotice")
    @ApiOperation("公告配置-列表")
    public Object getNotice(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }


    @PostMapping("/addNotice")
    @ApiOperation("公告配置-新增")
    public Object addNotice(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @PostMapping("/updateNotice")
    @ApiOperation("公告配置-编辑")
    public Object updateNotice(HttpServletRequest request, Integer id) {
        return serviceHandler.serviceHandler(request);
    }

    @PostMapping("/deleteNotice")
    @ApiOperation("公告配置-删除")
    public Object deleteNotice(HttpServletRequest request, String id) {
        return serviceHandler.serviceHandler(request);
    }

    @PostMapping("/getLhcTmb")
    @ApiOperation("特码B-查询反水比例")
    public Object getLhcTmb(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @PostMapping("/updateLhcTmb")
    @ApiOperation("特码B-反水比例修改")
    public Object updateLhcTmb(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @PostMapping("/getToDayPayoutSet")
    @ApiOperation("今日派奖设置-设置")
    public Object getToDayPayoutSet(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @PostMapping("/toDayPayoutSet")
    @ApiOperation("今日派奖设置-设置")
    public Object toDayPayoutSet(HttpServletRequest request,
                                 BigDecimal initNumber,
                                 BigDecimal initMoney,
                                 BigDecimal addNumber,
                                 BigDecimal addMoney) {
        return serviceHandler.serviceHandler(request);
    }

    @PostMapping("/getFundRecord")
    @ApiOperation("资金流水记录")
    public Object getFundRecord(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @PostMapping("/changesType")
    @ApiOperation("资金类型")
    public Object changesType(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @PostMapping("/getUserInfo")
    @ApiOperation("获取用户信息")
    public Object getUserInfo(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @PostMapping("/updateBlack")
    @ApiOperation("更新用户官彩黑名单")
    public Object updateBlack(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @PostMapping("/updateBetLimit")
    @ApiOperation("更新用户投注限额")
    public Object updateBetLimit(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }

    @PostMapping("/updateRebate")
    @ApiOperation("更新用户反水等级")
    public Object updateRebate(HttpServletRequest request) {
        return serviceHandler.serviceHandler(request);
    }
}
