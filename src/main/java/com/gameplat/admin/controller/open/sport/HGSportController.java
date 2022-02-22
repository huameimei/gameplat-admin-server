package com.gameplat.admin.controller.open.sport;

import com.alibaba.fastjson.JSONObject;
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

    @Autowired
    private HGSportService hgSportService;

    @ApiOperation(value = "HG注单列表")
    @GetMapping("/queryHGBetOrder")
    public JSONObject queryHGBetOrder(HGSportDTO param) {
        return hgSportService.queryHGBetOrder(param);
    }

    @ApiOperation(value = "HG用户投注限制列表")
    @GetMapping("/queryHGBetLimitList")
    public JSONObject queryHGBetLimitList(HGSportDTO param) {
        return hgSportService.queryHGBetLimitList(param);
    }

    @ApiOperation(value = "HG联赛列表")
    @GetMapping("/queryHGSportLeague")
    public JSONObject queryHGSportLeague(HGSportDTO param) {
        return hgSportService.queryHGSportLeague(param);
    }

    @ApiOperation(value = "HG赛果列表")
    @GetMapping("/queryHGSportResult")
    public JSONObject queryHGSportResult(HGSportDTO param) {
        return hgSportService.queryHGSportResult(param);
    }

    @ApiOperation(value = "HG即时注单详情")
    @GetMapping("/queryHGCurBetOrder")
    public JSONObject queryHGCurBetOrder(HGSportDTO param) {
        return hgSportService.queryHGCurBetOrder(param);
    }

    @ApiOperation(value = "HG注单子列表")
    @GetMapping("/queryHGBetsDetail")
    public JSONObject queryHGBetsDetail(HGSportDTO param) {
        return hgSportService.queryHGBetsDetail(param);
    }

    @ApiOperation(value = "HG投注设置列表")
    @GetMapping("/queryHGBetConfigs")
    public JSONObject queryHGBetConfigs(HGSportDTO param) {
        return hgSportService.queryHGBetConfigs(param);
    }

    @ApiOperation(value = "HG没有赛果的比赛列表")
    @GetMapping("/queryHGEntryResult")
    public JSONObject queryHGEntryResult(HGSportDTO param) {
        return hgSportService.queryHGEntryResult(param);
    }

    @ApiOperation(value = "HG单个用户限额列表")
    @GetMapping("/queryHGUserLimit")
    public JSONObject queryHGUserLimit(HGSportDTO param) {
        return hgSportService.queryHGUserLimit(param);
    }

    @ApiOperation(value = "HG公告列表")
    @GetMapping("/queryHGSportMessage")
    public JSONObject queryHGSportMessage(HGSportDTO param) {
        return hgSportService.queryHGSportMessage(param);
    }

    @ApiOperation(value = "HG赛事变更列表")
    @GetMapping("/queryHGChangeNotice")
    public JSONObject queryHGChangeNotice(HGSportDTO param) {
        return hgSportService.queryHGChangeNotice(param);
    }

    @ApiOperation(value = "手动结算-注单详情")
    @GetMapping("/queryHGHandOrdersDetail")
    public JSONObject queryHGHandOrdersDetail(HGSportDTO param) {
        return hgSportService.queryHGHandOrdersDetail(param);
    }

    @ApiOperation(value = "参数设置/修改")
    @PostMapping("/updateHgConfigModify")
    public JSONObject updateHgConfigModify(@RequestBody HGSportDTO param) {
        return hgSportService.updateHgConfigModify(param);
    }

    @ApiOperation(value = "参数设置列表")
    @GetMapping("/queryHgSportConfig")
    public JSONObject queryHgSportConfig(HGSportDTO param) {
        return hgSportService.queryHgSportConfig(param);
    }

    @ApiOperation(value = "输入足球赛果")
    @PostMapping("/inputResultForFoot")
    public JSONObject inputResultForFoot(@RequestBody HGSportDTO param) {
        return hgSportService.inputResultForFoot(param);
    }

    @ApiOperation(value = "输入篮球赛果")
    @PostMapping("/inputResultForBasketball")
    public JSONObject inputResultForBasketball(@RequestBody HGSportDTO param) {
        return hgSportService.inputResultForBasketball(param);
    }

    @ApiOperation(value = "修改投注限制(停止投注，开启投注)")
    @PostMapping("/updateSportStatus")
    public JSONObject updateSportStatus(@RequestBody HGSportDTO param) {
        return hgSportService.updateSportStatus(param);
    }

    @ApiOperation(value = "修改投注限制(单场投注金额)")
    @PostMapping("/updateSportBetLimitMoney")
    public JSONObject updateSportBetLimitMoney(@RequestBody HGSportDTO param) {
        return hgSportService.updateSportBetLimitMoney(param);
    }

    @ApiOperation(value = "修改投注设置")
    @PostMapping("/updateBetConfig")
    public JSONObject updateBetConfig(@RequestBody HGSportDTO param) {
        return hgSportService.updateBetConfig(param);
    }

    @ApiOperation(value = "重新结算")
    @PostMapping("/betUpdateActions")
    public JSONObject betUpdateActions(@RequestBody HGSportDTO param) {
        return hgSportService.betUpdateActions(param);
    }

    @ApiOperation(value = "批量操作体育注单（确认， 取消）")
    @PostMapping("/batchActionsByStatus")
    public JSONObject batchActionsByStatus(@RequestBody HGSportDTO param) {
        return hgSportService.batchActionsByStatus(param);
    }

    @ApiOperation(value = "手工结算足球")
    @PostMapping("/settlingResultForFoot")
    public JSONObject settlingResultForFoot(@RequestBody HGSportDTO param) {
        return hgSportService.settlingResultForFoot(param);
    }

    @ApiOperation(value = "手工结算篮球")
    @PostMapping("/settlingResultForBasketball")
    public JSONObject settlingResultForBasketball(@RequestBody HGSportDTO param) {
        return hgSportService.settlingResultForBasketball(param);
    }

    @ApiOperation(value = "单用户投注设置")
    @PostMapping("/saveUserLimit")
    public JSONObject saveUserLimit(@RequestBody HGSportDTO param) {
        return hgSportService.saveUserLimit(param);
    }

    @ApiOperation(value = "单用户投注开关")
    @PostMapping("/updateUserLimit")
    public JSONObject updateUserLimit(@RequestBody HGSportDTO param) {
        return hgSportService.updateUserLimit(param);
    }

    @ApiOperation(value = "删除单用户投注限制")
    @PostMapping("/removeUserLimit")
    public JSONObject removeUserLimit(@RequestBody HGSportDTO param) {
        return hgSportService.removeUserLimit(param);
    }

    @ApiOperation(value = "添加体育公告")
    @PostMapping("/saveSportMessage")
    public JSONObject saveSportMessage(@RequestBody HGSportDTO param) {
        return hgSportService.saveSportMessage(param);
    }

    @ApiOperation(value = "删除体育公告")
    @PostMapping("/removeSportMessage")
    public JSONObject removeSportMessage(@RequestBody HGSportDTO param) {
        return hgSportService.removeSportMessage(param);
    }

    @ApiOperation(value = "修改体育公告")
    @PostMapping("/modifySportMessage")
    public JSONObject modifySportMessage(@RequestBody HGSportDTO param) {
        return hgSportService.modifySportMessage(param);
    }

    @ApiOperation(value = "赛果变更消息处理")
    @PostMapping("/sportChangeNotice")
    public JSONObject sportChangeNotice(@RequestBody HGSportDTO param) {
        return hgSportService.sportChangeNotice(param);
    }

    @ApiOperation(value = "参数查询（指定key）")
    @GetMapping("/queryHGConfigGetOne")
    public JSONObject queryHGConfigGetOne(HGSportDTO param) {
        return hgSportService.queryHGConfigGetOne(param);
    }

}
