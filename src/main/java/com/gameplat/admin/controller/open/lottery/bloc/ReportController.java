package com.gameplat.admin.controller.open.lottery.bloc;

import com.cz.framework.DateUtil;
import com.cz.framework.LogUtil;
import com.cz.framework.StringUtil;
import com.cz.framework.bean.PageBean;
import com.cz.framework.bean.PageData;
import com.cz.framework.exception.BusinessException;
import com.cz.gameplat.annotation.Session;
import com.cz.gameplat.core.export.Export;
import com.cz.gameplat.core.export.ExportCache;
import com.cz.gameplat.core.export.Status;
import com.cz.gameplat.core.export.Task;
import com.cz.gameplat.game.entity.UserBetAnalysisV0;
import com.cz.gameplat.game.entity.UserBetReport;
import com.cz.gameplat.game.entity.UserBetV0;
import com.cz.gameplat.game.service.GameService1;
import com.cz.gameplat.game.service.UserBetReportService;
import com.cz.gameplat.log.Log;
import com.cz.gameplat.log.LogType;
import com.cz.gameplat.report.bean.*;
import com.cz.gameplat.report.entity.UserBusDayReport;
import com.cz.gameplat.report.req.*;
import com.cz.gameplat.report.service.ReportService;
import com.cz.gameplat.sys.cache.GenericDataCache;
import com.cz.gameplat.sys.entity.Admin;
import com.cz.gameplat.sys.service.ConfigService;
import com.cz.gameplat.user.bean.UserInfoRep;
import com.cz.gameplat.user.entity.UserInfo;
import com.cz.gameplat.user.enums.UserStates;
import com.cz.gameplat.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2017/4/6.
 */

@Controller
@RequestMapping("/api/admin/report")
public class ReportController {

    @Resource
    UserBetReportService service;
    @Resource
    UserBetReportService userBetReportService;

    @Resource
    private ReportService reportService;

    @Resource
    private ConfigService configService;

//    @Resource
//    private LiveGameService liveGameService;

    @Resource
    ExportCache exportCache;

    @Resource
    GameService1 gameService;

    @Resource
    private UserService userService;

    @RequestMapping(value = "/agentOt", method = RequestMethod.GET)
    @ResponseBody
    @Deprecated
    public List<UserBetReport> queryAgentOt(Long agentId) {
        return service.queryOt(agentId);
    }


    /**
     * 查询充提报表的数据(包括搜索条件)
     */
    @RequestMapping("/queryReportUserRw")
    @ResponseBody
    public PageData<ReportUserRwVO> queryReportUserRw(QueryRWReport queryReport, PageBean pageBean)
            throws Exception {
        return (PageData<ReportUserRwVO>) reportService.slaveQueryListUserRw(queryReport, pageBean,true);
    }

    @RequestMapping(value = "/export", method = RequestMethod.GET)
    @Log(type = LogType.EXPORT_HY,content = "'导出充提报表'")
    @ResponseBody
    public Task export(QueryRWReport params, @Session Admin admin) throws BusinessException {
        Task<QueryRWReport> task = new Task<>(params, admin.getId(), "充提报表");
        String taskId = exportCache.add(task);
        Export.getExecutor().execute(() -> {
            Task<QueryRWReport> todoTask = exportCache.get(taskId);
            try {
                List<ReportUserRwVO> data= (List<ReportUserRwVO>) reportService.slaveQueryListUserRw(todoTask.getParams(), null,false);
                Export.exportPoi(data, ReportUserRwVO.class, new FileOutputStream(todoTask.getExportFilePath()), null, todoTask.getName());
                exportCache.update(todoTask.setStatus(Status.Success));
            } catch (Exception e) {
                exportCache.update(todoTask.setStatus(Status.Fail));
                LogUtil.error(e.toString());
            }
        });
        return task;
    }

    @RequestMapping(value = "/exportIP", method = RequestMethod.GET)
    @Log(type = LogType.EXPORT_HY,content = "'导出IP分析报表'")
    @ResponseBody
    public Task exportIP(QueryReport params, @Session Admin admin) throws BusinessException {
        Task<QueryReport> task = new Task<>(params, admin.getId(), "IP分析报表");
        String taskId = exportCache.add(task);
        Export.getExecutor().execute(() -> {
            Task<QueryReport> todoTask = exportCache.get(taskId);
            try {
                List<ReportIpVO> data= (List<ReportIpVO>) reportService.slaveQueryPageReportIp(todoTask.getParams(), null,false);
                Export.exportPoi(data, ReportIpVO.class, new FileOutputStream(todoTask.getExportFilePath()), null, todoTask.getName());
                exportCache.update(todoTask.setStatus(Status.Success));
            } catch (Exception e) {
                exportCache.update(todoTask.setStatus(Status.Fail));
                LogUtil.error(e.toString());
            }
        });
        return task;
    }

    /**
     * 查询公司报表数据统计
     */
    @RequestMapping("/queryReportCompany")
    @ResponseBody
    public ReportCompanyVO queryReportCompany(@Validated QueryCompanyReportReq queryReport, @Session Admin admin) throws Exception {
        if (StringUtils.equalsIgnoreCase("ZZH", admin.getType()) && StringUtils.isBlank(queryReport.getAccount())) {
            throw new BusinessException("请输入会员帐号");
        }
        return reportService.slaveQueryListCompany(queryReport);
    }
    /**
     * 查询公司报表优惠分类详情
     */
    @RequestMapping("/queryReportDiscountInfo")
    @ResponseBody
    public List<Map> queryReportDiscountInfo(QueryCompanyReportReq queryReport) throws BusinessException {
        return reportService.getDiscountInfo(queryReport);
    }


    /**
     * 查询会员投注额数据统计
     */
    @RequestMapping("/queryReportBetting")
    @ResponseBody
    public PageData<ReportBettingVO> queryReportBetting(BetReportReq req, PageBean pageBean)
            throws Exception {
        return (PageData<ReportBettingVO>) reportService.slaveQueryPageBetting(req, pageBean, true);
    }
    @RequestMapping(value = "/exportReportBetting", method = RequestMethod.GET)
    @Log(type = LogType.EXPORT_HY,content = "'导出投注额报表'")
    @ResponseBody
    public Task exportReportBetting(BetReportReq params, @Session Admin admin) throws BusinessException {
        Task<BetReportReq> task = new Task<>(params, admin.getId(), "投注额报表");
        String taskId = exportCache.add(task);
        Export.getExecutor().execute(() -> {
            Task<BetReportReq> todoTask = exportCache.get(taskId);
            try {
                List<ReportBettingVO> data = (List<ReportBettingVO>) reportService.slaveQueryPageBetting(todoTask.getParams(), null, false);
                Export.exportPoi(data, ReportBettingVO.class, new FileOutputStream(todoTask.getExportFilePath()), null, todoTask.getName());
                exportCache.update(todoTask.setStatus(Status.Success));
            } catch (Exception e) {
                exportCache.update(todoTask.setStatus(Status.Fail));
                LogUtil.error(e.toString());
            }
        });
        return task;
    }

    /**
     * 查询会员投注额数据统计
     */
    @RequestMapping("/queryUserCpbetSummary")
    @ResponseBody
    public PageData<UserCpBetSummaryReportVo> queryReportBetting(PageBean pageBean, ReportParam reportParam)
            throws Exception {
        return reportService.queryUserCpbetSummary(pageBean,reportParam);
    }

    @RequestMapping(value = "/exportUserCpbetSummary" ,method = RequestMethod.GET)
    @Log(type = LogType.EXPORT_HY,content = "'导出彩票输赢记录报表'")
    @ResponseBody
    public Task exportUserCpbetSummary(ReportParam reportParam, @Session Admin admin) throws BusinessException {
        Task<ReportParam> task = new Task<>(reportParam, admin.getId(), "彩票输赢记录报表");
        String taskId = exportCache.add(task);
        Export.getExecutor().execute(() -> {
            Task<ReportParam> todoTask = exportCache.get(taskId);
            try {
                List<UserCpBetSummaryReportVo> data = reportService.queryUserCpbetSummaryList(reportParam);
                Export.exportPoi(data, UserCpBetSummaryReportVo.class, new FileOutputStream(todoTask.getExportFilePath()), null, todoTask.getName());
                exportCache.update(todoTask.setStatus(Status.Success));
            } catch (Exception ep) {
                exportCache.update(todoTask.setStatus(Status.Fail));
                LogUtil.error(ep.toString());
            }
        });
        return task;
    }


    /**
     * 查询单期盈亏报表
     */
   /* @RequestMapping("/queryReportSingalProfit")
    @ResponseBody
    @Deprecated
    public PageData<ReportSingalProfitVO> queryReportSingalProfit(QueryReport queryReport,
                                                                  PageBean pageBean) throws Exception {
        return reportService.slaveQueryPageSingalProfit(queryReport, pageBean);
    }*/

    /**
     * 游戏输赢报表
     */
    @RequestMapping("/queryReportGameWinLose")
    @ResponseBody
    public List<ReportGameWinLoseVO> queryReportGameWinLose(ReportGameWinLoseReq queryReport)
            throws Exception {

        if(StringUtils.isNotBlank(queryReport.getDlAccount())){
            UserInfo user = userService.getUserInfo(queryReport.getDlAccount());
            if(user == null) {
                throw new BusinessException("无效的代理帐号");
            }else if(!user.getIsDl()){
                throw new BusinessException(queryReport.getDlAccount()+"不是代理帐号");
            }else {
                queryReport.setSuperPath(user.getSuperPath());
            }
        }

        return reportService.slaveQueryListGameWinLose(queryReport);
    }
    @RequestMapping(value = "/exportReportGameWinLose" ,method = RequestMethod.GET)
    @Log(type = LogType.EXPORT_HY,content = "'导出游戏输赢明细报表'")
    @ResponseBody
    public Task exportReportGameWinLose(ReportGameWinLoseReq params, @Session Admin admin) throws Exception{
        if(StringUtils.isNotBlank(params.getDlAccount())){
            UserInfo user = userService.getUserInfo(params.getDlAccount());
            if(user == null) {
                throw new BusinessException("无效的代理帐号");
            }else if(!user.getIsDl()){
                throw new BusinessException(params.getDlAccount()+"不是代理帐号");
            }else {
                params.setSuperPath(user.getSuperPath());
            }
        }
        Task<ReportGameWinLoseReq> task = new Task<>(params, admin.getId(), "游戏输赢明细报表");
        String taskId = exportCache.add(task);
        Export.getExecutor().execute(() -> {
            Task<ReportGameWinLoseReq> todoTask = exportCache.get(taskId);
            try {
                List<ReportGameWinLoseVO> data = reportService.slaveQueryListGameWinLose(params);
                Export.exportPoi(data, ReportGameWinLoseVO.class, new FileOutputStream(todoTask.getExportFilePath()), null, todoTask.getName());
                exportCache.update(todoTask.setStatus(Status.Success));
            } catch (Exception ep) {
                exportCache.update(todoTask.setStatus(Status.Fail));
                LogUtil.error(ep.toString());
            }
        });
        return task;
    }

    @RequestMapping(value = "/batchExportReportGameWinLose" ,method = RequestMethod.GET)
    @ResponseBody
    public Task batchExportReportGameWinLose(ReportGameWinLoseReq params, @Session Admin admin) throws Exception {
        if(StringUtils.isNotBlank(params.getDlAccount())){
            UserInfo user = userService.getUserInfo(params.getDlAccount());
            if(user == null) {
                throw new BusinessException("无效的代理帐号");
            }else if(!user.getIsDl()){
                throw new BusinessException(params.getDlAccount()+"不是代理帐号");
            }else {
                params.setSuperPath(user.getSuperPath());
            }
        }
        Task<ReportGameWinLoseReq> task = new Task<>(params, admin.getId(), "游戏输赢明细报表");
        String taskId = exportCache.add(task);
        Export.getExecutor().execute(() -> {
            Task<ReportGameWinLoseReq> todoTask = exportCache.get(taskId);
            try {
                List<ReportGameWinLoseDetailVO> personList=reportService.slaveQueryListGameWinLosePersons(params);
                Export.exportPoi(personList, ReportGameWinLoseDetailVO.class, new FileOutputStream(todoTask.getExportFilePath()), null, todoTask.getName());
                exportCache.update(todoTask.setStatus(Status.Success));
            } catch (Exception e) {
                exportCache.update(todoTask.setStatus(Status.Fail));
                LogUtil.error(e.toString());
            }
        });
        return task;
    }

    /**
     * 游戏输赢会员名单
     */
    @RequestMapping("/queryReportGameWinLosePerson")
    @ResponseBody
    public PageData<ReportGameWinLoseVO> queryReportGameWinLosePerson(ReportGameWinLoseReq queryReport,
                                                                      PageBean pageBean)
            throws Exception {
        if(StringUtils.isNotBlank(queryReport.getDlAccount())){
            UserInfo user = userService.getUserInfo(queryReport.getDlAccount());
            if(user == null) {
                throw new BusinessException("无效的代理帐号");
            }else if(!user.getIsDl()){
                throw new BusinessException(queryReport.getDlAccount()+"不是代理帐号");
            }else {
                queryReport.setSuperPath(user.getSuperPath());
            }
        }
        return reportService.slaveQueryListGameWinLosePerson(queryReport,pageBean);
    }

//    /**
//     * 用户活跃度统计
//     */
//    @RequestMapping(value = "/queryReportUserActivity", method = RequestMethod.GET)
//    @ResponseBody
//    public PageData<ReportUserActivityVO> queryReportUserActivity(UserActivityReq queryReport,
//                                                                  PageBean pageBean) throws Exception {
//        //查询代理线
//        if (StringUtil.isNotBlank(queryReport.getSuperPathByAccount())) {
//            UserInfo userInfo = userService.getUserInfo(queryReport.getSuperPathByAccount());
//            if (userInfo != null) {
//                queryReport.setSuperPath(userInfo.getSuperPath());
//            } else {
//                return null;
//            }
//        }
//        return (PageData<ReportUserActivityVO>) reportService.slaveQueryPageUserActivity(queryReport, pageBean,true);
//    }


//    @RequestMapping(value = "/exportReportUserActivity", method = RequestMethod.GET)
//    @Log(type = LogType.EXPORT_HY,content = "'导出用户活跃度报表，导出时间区间：'+T(com.cz.gameplat.log.LogUtil).logExportTime(#params.getBeginDate(),#params.getEndDate())")
//    @ResponseBody
//    public Task exportReportUserActivity(UserActivityReq params, @Session Admin admin) throws BusinessException {
//        String title = DateUtil.dateToYMD(params.getBeginDate()) + "~" + DateUtil.dateToYMD(params.getEndDate())+"用户活跃度报表";
//        Task<UserActivityReq> task = new Task<>(params, admin.getId(), title);
//        String taskId = exportCache.add(task);
//        params.setOutput(true);
//        Export.getExecutor().execute(() -> {
//            Task<UserActivityReq> todoTask = exportCache.get(taskId);
//            try {
//                List<ReportUserActivityVO> data= null;
//                if (StringUtil.isNotBlank(params.getSuperPathByAccount())) {
//                    UserInfo userInfo = userService.getUserInfo(params.getSuperPathByAccount());
//                    if (userInfo != null) {
//                        params.setSuperPath(userInfo.getSuperPath());
//                        data = (List<ReportUserActivityVO>) reportService.slaveQueryPageUserActivity(params, null,false);
//                    } else {
//                        data = null;
//                    }
//                }else {
//                    data = (List<ReportUserActivityVO>) reportService.slaveQueryPageUserActivity(params, null,false);
//                }
//                GenericDataCache.cacheGenericData(GenericDataCache.ConfigName_Rech_Level, configService, "getList", String.class,
//                    GenericDataCache.ConfigName_Rech_Level);
//                Export.exportPoi(data, ReportUserActivityVO.class, new FileOutputStream(todoTask.getExportFilePath()), null, todoTask.getName());
//                exportCache.update(todoTask.setStatus(Status.Success));
//            } catch (Exception e) {
//                exportCache.update(todoTask.setStatus(Status.Fail));
//                LogUtil.error(e.toString());
//            }
//        });
//        return task;
//    }
    /**
     * 代理用户统计
     */
    @RequestMapping(value = "/queryReportProxy", method = RequestMethod.GET)
    @ResponseBody
    public PageData<ReportProxyVO> queryReportProxy(ProxyReportReq queryReport, PageBean pageBean)
            throws Exception {
        return (PageData<ReportProxyVO>) reportService.slaveQueryPageReportProxy(queryReport,true, pageBean);
    }

    /**
     * 代理用户统计导出
     */
    @RequestMapping(value = "/exportReportProxy", method = RequestMethod.GET)
    @Log(type = LogType.EXPORT_HY,content = "'导出代理统计报表，导出时间区间：'+T(com.cz.gameplat.log.LogUtil).logExportTime(#params.getBeginDate(),#params.getEndDate())")
    @ResponseBody
    public Task exportReportProxy(ProxyReportReq params, @Session Admin admin) throws BusinessException {
        Task<ProxyReportReq> task = new Task<>(params, admin.getId(), "代理统计报表导出");
        String taskId = exportCache.add(task);
        Export.getExecutor().execute(() -> {
            Task<ProxyReportReq> todoTask = exportCache.get(taskId);
            try {
                List<ReportProxyVO> data = (List<ReportProxyVO>) reportService.slaveQueryPageReportProxy(todoTask.getParams(), false, null);
                Export.exportPoi(data, ReportProxyVO.class, new FileOutputStream(todoTask.getExportFilePath()), null, todoTask.getName());
                exportCache.update(todoTask.setStatus(Status.Success));
            } catch (Exception e) {
                exportCache.update(todoTask.setStatus(Status.Fail));
                LogUtil.error(e.toString());
            }
        });
        return task;
    }


    /**
     * 会员账目报表
     */
    @RequestMapping(value = "/queryReportUserWorks", method = RequestMethod.GET)
    @ResponseBody
    public ReportUserWorksVO queryReportUserWorks(QueryReport queryReport) throws Exception {
        return reportService.slaveQueryReportUserWorks(queryReport);
    }

    /**
     * 会员IP分析报表
     */
    @RequestMapping(value = "/queryReportIp", method = RequestMethod.GET)
    @ResponseBody
    public PageData<ReportIpVO> queryReportIp(QueryReport queryReport, PageBean pageBean){
        return (PageData<ReportIpVO>) reportService.slaveQueryPageReportIp(queryReport, pageBean,true);
    }


    /**
     * 数据统计报表查看注册数量的用户列表
     */
    @RequestMapping(value = "/queryReportDetail/register", method = RequestMethod.GET)
    @ResponseBody
    public PageData<UserInfo> queryReportDetailRegister(QueryReport queryReport, PageBean pageBean)
            throws Exception {
        return reportService.slaveQueryReportDetailRegister(queryReport, pageBean);
    }

    /**
     * 数据统计报表查看登录数量的用户列表
     */
    @RequestMapping(value = "/queryReportDetail/login", method = RequestMethod.GET)
    @ResponseBody
    public PageData<UserInfoRep> queryReportDetailLogin(QueryReport queryReport, PageBean pageBean)
            throws Exception {
        return reportService.slaveQueryReportDetailLogin(queryReport, pageBean);
    }

    /**
     * 数据统计报表查看首次充值的用户列表
     */
    @RequestMapping(value = "/queryReportDetail/firstRech", method = RequestMethod.GET)
    @ResponseBody
    public PageData<UserInfoRep> queryReportDetailFirstRech(QueryReport queryReport,
                                                            PageBean pageBean) throws Exception {
        return (PageData<UserInfoRep>)reportService.slaveQueryReportDetailFirstRech(queryReport, pageBean);
    }

    /**
     * 数据统计报表查看二次充值的用户列表
     */
    @RequestMapping(value = "/queryReportDetail/againRech", method = RequestMethod.GET)
    @ResponseBody
    public PageData<UserInfoRep> queryReportDetailAgainRech(QueryReport queryReport,
                                                            PageBean pageBean) throws Exception {
        return (PageData<UserInfoRep>)reportService.slaveQueryReportDetailAgainRech(queryReport, pageBean);
    }


    /**
     * 数据统计报表查看首次充值的用户列表(导出)
     * @param queryReport
     * @return
     */
    @RequestMapping("/queryReportDetail/exportReportFirstRech")
    @ResponseBody
    @Log(type = LogType.EXPORT_HY, content = "'导出数据统计首次入款报表'")
    public Task exportReportFirstRech(QueryReport queryReport, @Session Admin admin) throws BusinessException {
        Task<QueryReport> task = new Task<>(queryReport, admin.getId(), "导出数据统计首次入款报表");
        String taskId = exportCache.add(task);
        new Thread(() -> {
            Task<QueryReport> todoTask = exportCache.get(taskId);
            try {
                List<UserInfoRep> data= (List<UserInfoRep>) reportService.slaveQueryReportDetailFirstRech(todoTask.getParams(), null);
                List<ReportFirstRechVO> result  = new ArrayList();
                data.stream().forEach(user->{
                    ReportFirstRechVO reportFirstRechVO   = new ReportFirstRechVO();
                    BeanUtils.copyProperties(user,reportFirstRechVO);
                    result.add(reportFirstRechVO);
                });
                GenericDataCache.cacheGenericData(GenericDataCache.ConfigName_Rech_Level, configService, "getList",String.class, GenericDataCache.ConfigName_Rech_Level);
                Export.exportPoi(result, ReportFirstRechVO.class, new FileOutputStream(todoTask.getExportFilePath()), null, todoTask.getName());
                GenericDataCache.removeGenericData(GenericDataCache.ConfigName_Rech_Level);
                exportCache.update(todoTask.setStatus(Status.Success));
            } catch (Exception e) {
                exportCache.update(todoTask.setStatus(Status.Fail));
                LogUtil.error(e.toString());
            }
        }).start();
        return task;
    }


    /**
     * 数据统计报表查看首次取现的用户列表
     */
    @RequestMapping(value = "/queryReportDetail/firstCash", method = RequestMethod.GET)
    @ResponseBody
    public PageData<UserInfoRep> queryReportDetailFirstCash(QueryReport queryReport,
                                                            PageBean pageBean) throws Exception {
        return reportService.slaveQueryReportDetailFirstCash(queryReport, pageBean);
    }

    /**
     * 数据统计报表查看游戏人数的用户列表
     */
    @RequestMapping(value = "/queryReportDetail/gameUserBetDetail", method = RequestMethod.GET)
    @ResponseBody
    public PageData<UserInfoRep> queryReportDetailGameBetUserDetail(QueryReport queryReport,
                                                                    PageBean pageBean) throws Exception {
        return reportService.slaveQueryGameBetUserDetail(queryReport, pageBean);
    }

    /**
     * 数据统计报表查看玩家钱包的用户列表
     */
    @RequestMapping(value = "/queryReportDetail/userMoneyDetail", method = RequestMethod.GET)
    @ResponseBody
    public PageData<UserInfoRep> queryReportDetailUserMoneyDetail(QueryReport queryReport,
                                                                  PageBean pageBean) throws Exception {
        if (queryReport.getUserStatus() != null && UserStates.get(queryReport.getUserStatus()) == null) {
            throw new BusinessException("错误的用户状态：" + queryReport.getUserStatus());
        }
        return reportService.slaveQueryUserMoneyDetail(queryReport, pageBean);
    }

    @RequestMapping(value = "/queryReportDetail/yubaoDetail", method = RequestMethod.GET)
    @ResponseBody
    public PageData<UserInfoRep> queryReportDetailYubaoDetail(QueryReport queryReport,
                                                              PageBean pageBean) throws Exception {
        if (queryReport.getUserStatus() != null && UserStates.get(queryReport.getUserStatus()) == null) {
            throw new BusinessException("错误的用户状态：" + queryReport.getUserStatus());
        }
        return reportService.slaveQueryYubaoDetail(queryReport, pageBean);
    }

    /**
     * 数据统计报表查看代理统计列表
     */
    @RequestMapping(value = "/queryDlReportDetail", method = RequestMethod.GET)
    @ResponseBody
    public PageData<UserBusDayReport> queryDlReportDetail(QueryReport queryReport,
                                                          PageBean pageBean) throws Exception {
        return reportService.slaveQueryDlReportDetail(queryReport,pageBean);
    }

//    /**
//     * 用户注单分析
//     *
//     * @param account
//     * @param beginDate
//     * @param endDate
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping(value = "/queryUserAnalysis", method = RequestMethod.GET)
//    @ResponseBody
//    public UserBetAnalysisV0 queryUserAnalysis(String account, String beginDate, String endDate, boolean includeSubUsers) throws Exception {
//        return reportService.slaveQueryUserAnalysis(account, beginDate, endDate,includeSubUsers);
//    }


    @RequestMapping(value = "/queryReportGeneral")
    @ResponseBody
    public Map<String, List> queryReportGeneral(@RequestBody QueryGeneralReport report) {
        return reportService.slaveQueryGeneralReportManager(report);
    }

//    @RequestMapping(value = "/queryReportGeneralForUserDetail")
//    @ResponseBody
//    public PageData<Map<String, Object>> queryGeneralReportForUserDetail(QueryGeneralReportForUserDetail detail, PageBean pageBean) {
//        return reportService.slaveQueryGeneralReportForUserDetail(detail,pageBean);
//    }

//    /**
//     * 查询盈亏报表的投注详情
//     */
//    @RequestMapping(value = "/queryReportDetail/bet", method = RequestMethod.GET)
//    @ResponseBody
//    public PageData<UserBet> queryReportDetailBet(QueryReport queryReport, PageBean pageBean)
//            throws Exception {
//        return reportService.slaveQueryReportDetailBet(queryReport, pageBean);
//    }

    /**
     * * 查询盈亏报表的投注详情
     */
    @RequestMapping(value = "/queryReportDetail/bet", method = RequestMethod.GET)
    @ResponseBody
    public PageData<UserBetV0> queryReportDetailBetDml(QueryReport queryReport, PageBean pageBean)
            throws Exception {
        return reportService.slaveQueryReportDetailBet(queryReport, pageBean);
    }


//    @RequestMapping(value = "/queryReportGeneralManagerForGameType")
//    @ResponseBody
//    public Map<String, List> queryReportGeneralManagerForGameType() {
//        Map<String, List> map = new HashMap();
//        List<Map> sportList = new ArrayList();
//        Map sportItemMapAll = new HashMap();
//        sportItemMapAll.put("text", -1);
//        sportItemMapAll.put("name", "全部");
//
//        Map sportItemMapFoot = new HashMap();
//
//        sportItemMapFoot.put("text", 0);
//        sportItemMapFoot.put("name", "足球");
//
//        Map sportItemMapBasket = new HashMap();
//        sportItemMapBasket.put("text", 1);
//        sportItemMapBasket.put("name", "篮球");
//
//        sportList.add(sportItemMapAll);
//        sportList.add(sportItemMapFoot);
//        sportList.add(sportItemMapBasket);
//
//        map.put("sport", sportList);
//        List cp = new ArrayList();
//        Map cpAll = new HashMap();
//        cpAll.put("text", -1);
//        cpAll.put("name", "全部");
//        cp.add(cpAll);
//        gameService.queryAll().forEach(g -> {
//            Map cpItemMap = new HashMap();
//            cpItemMap.put("text", g.getId());
//            cpItemMap.put("name", g.getName());
//            cp.add(cpItemMap);
//        });
//        map.put("cp", cp);
//        List liveList = new ArrayList();
//
//      List<LiveGame> kinds = liveGameService.slaveLiveAll();
//
//        Map liveAll = new HashMap();
//        liveAll.put("text", "");
//        liveAll.put("name", "全部");
//        liveList.add(liveAll);
//
//        kinds.forEach(e -> {
//            Map tmpMap = new HashMap();
//            tmpMap.put("text", e.getCode());
//            tmpMap.put("name", e.getName());
//            liveList.add(tmpMap);
//        });
//        map.put("live", liveList);
//        return map;
//    }

    @RequestMapping(value = "/exportGeneralReport", method = RequestMethod.GET)
    @ResponseBody
    @Log(type = LogType.EXPORT_HY,content = "'导出一般报表'")
    public Task exportGeneralReport(QueryGeneralReport params, @Session Admin admin) throws BusinessException {
        Task<QueryGeneralReport> task = new Task<>(params, admin.getId(), "一般报表");
        String taskId = exportCache.add(task);
        Export.getExecutor().execute(() -> {
            Task<QueryGeneralReport> todoTask = exportCache.get(taskId);
            try {
                reportService.export(todoTask,params);
                exportCache.update(todoTask.setStatus(Status.Success));
            } catch (Exception e) {
                exportCache.update(todoTask.setStatus(Status.Fail));
                LogUtil.error(e.toString());
            }
        });
        return task;
    }

    /**
     * 点击数据统计人数查看会员信息
     * @param queryReport
     * @param pageBean
     * @return
     */
    @RequestMapping("/queryReportDetail/betUser")
    @ResponseBody
    public PageData<UserInfoRep> queryReportDetailBetUser(QueryReport queryReport, PageBean pageBean) throws BusinessException {
        return reportService.slaveQueryReportDetailBetUser(queryReport,pageBean);
    }


    /**
     * 导出数据统计报表
     *
     * @param queryReport QueryCompanyReportReq
     * @param admin       Admin
     * @return Task Task<QueryCompanyReportReq>
     */
    @RequestMapping(value = "/exportReportCompany", method = RequestMethod.GET)
    @ResponseBody
    @Log(type = LogType.EXPORT_HY, content = "'导出数据统计报表'")
    public Task exportReportCompany(QueryCompanyReportReq queryReport, @Session Admin admin) throws Exception {
        if (StringUtils.equalsIgnoreCase("ZZH", admin.getType()) && StringUtils.isBlank(queryReport.getAccount())) {
            throw new BusinessException("请输入会员帐号");
        }
        Task<QueryCompanyReportReq> task = new Task<>(queryReport, admin.getId(), "导出数据统计报表");
        String taskId = exportCache.add(task);
        Export.getExecutor().execute(() -> {
            Task<QueryCompanyReportReq> todoTask = exportCache.get(taskId);
            Status flag = Status.Success;
            try {
                reportService.exportReportCompany(todoTask, queryReport);
            } catch (Exception e) {
                LogUtil.error(e.toString());
                flag = Status.Fail;
            } finally {
                exportCache.update(todoTask.setStatus(flag));
            }
        });
        return task;
    }

    /**
     * IP统计报表
     */
    @RequestMapping(value = "/queryIpStatReport", method = RequestMethod.GET)
    @ResponseBody
    public PageData<IpStatReportVO> queryIpStatReport(QueryIpStatReportVO queryIpStatReportVO, PageBean pageBean)
        throws Exception {
        return (PageData<IpStatReportVO>) reportService.slaveQueryPageIpStatReport(queryIpStatReportVO, pageBean);
    }

    /**
     * 导出IP统计
     * @param params
     * @param admin
     * @return
     */
    @RequestMapping(value = "/exportIpStat", method = RequestMethod.GET)
    @Log(type = LogType.EXPORT_HY,content = "'导出IP统计报表'")
    @ResponseBody
    public Task exportIpStat(QueryIpStatReportVO params, @Session Admin admin) throws BusinessException {
        Task<QueryIpStatReportVO> task = new Task<>(params, admin.getId(), "IP统计报表");
        String taskId = exportCache.add(task);
        Export.getExecutor().execute(() -> {
            Task<QueryIpStatReportVO> todoTask = exportCache.get(taskId);
            try {
                List<IpStatReportVO> data= (List<IpStatReportVO>) reportService.slaveQueryPageIpStatReport(todoTask.getParams(), null);
                Export.exportPoi(data, IpStatReportVO.class, new FileOutputStream(todoTask.getExportFilePath()), null, todoTask.getName());
                exportCache.update(todoTask.setStatus(Status.Success));
            } catch (Exception e) {
                exportCache.update(todoTask.setStatus(Status.Fail));
                LogUtil.error(e.toString());
            }
        });
        return task;
    }
}

