package com.gameplat.admin.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.enums.BlacklistConstant;
import com.gameplat.admin.enums.GameRebateReportStatus;
import com.gameplat.admin.mapper.GameBetDailyReportMapper;
import com.gameplat.admin.mapper.GameRebateConfigMapper;
import com.gameplat.admin.mapper.GameRebateDetailMapper;
import com.gameplat.admin.mapper.GameRebateReportMapper;
import com.gameplat.admin.model.dto.GameRebateReportQueryDTO;
import com.gameplat.admin.model.dto.GameRebateStatisQueryDTO;
import com.gameplat.admin.model.vo.*;
import com.gameplat.admin.service.*;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.json.JsonUtils;
import com.gameplat.base.common.snowflake.IdGeneratorSnowflake;
import com.gameplat.common.enums.GameBlacklistTypeEnum;
import com.gameplat.common.enums.GamePlatformEnum;
import com.gameplat.common.enums.LimitEnums;
import com.gameplat.common.enums.TranTypes;
import com.gameplat.common.lang.Assert;
import com.gameplat.common.model.bean.limit.MemberWithdrawLimit;
import com.gameplat.model.entity.game.*;
import com.gameplat.model.entity.limit.LimitInfo;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.member.MemberBill;
import com.gameplat.model.entity.recharge.RechargeOrder;
import com.gameplat.redis.api.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class GameRebateReportServiceImpl
    extends ServiceImpl<GameRebateReportMapper, GameRebateReport>
    implements GameRebateReportService {

  @Autowired(required = false)
  private GameRebateConfigMapper gameRebateConfigMapper;

  @Autowired private MemberService memberService;

  @Autowired private MemberBillService memberBillService;

  @Autowired private MemberInfoService memberInfoService;

  @Autowired private ValidWithdrawService validWithdrawService;

  @Autowired private GameBlacklistService gameBlacklistService;

  @Autowired(required = false)
  private GameRebateDetailMapper gameRebateDetailMapper;

  @Autowired(required = false)
  private GameBetDailyReportMapper gameBetDailyReportMapper;

  @Autowired(required = false)
  private GameRebateReportMapper gameRebateReportMapper;

  @Autowired private RedisService redisService;

  @Autowired private LimitInfoService limitInfoService;

  @Autowired private MemberDayReportService memberDayReportService;

  @Override
  public PageDtoVO<GameRebateDetail> queryPage(
      Page<GameRebateDetail> page, GameRebateReportQueryDTO dto) {
    PageDtoVO<GameRebateDetail> pageDtoVO = new PageDtoVO<>();
    // 查看非当期返水用期数倒序排序
    String orderCloumn = "rebate_money";
    if (dto.getBeginDate() != null && dto.getEndDate() != null) {
      if (!DateUtil.parseDate(dto.getBeginDate()).equals(DateUtil.parseDate(dto.getEndDate()))) {
        orderCloumn = "rebate_money,stat_time";
      }
    }

    QueryWrapper<GameRebateDetail> queryWrapper = Wrappers.query();
    queryWrapper.eq(ObjectUtils.isNotEmpty(dto.getUserAccount()), "account", dto.getUserAccount());

    if (StringUtils.isNotBlank(dto.getBeginDate())) {
      queryWrapper.apply("begin_date >= STR_TO_DATE({0}, '%Y-%m-%d')", dto.getBeginDate());
    }
    if (StringUtils.isNotBlank(dto.getEndDate())) {
      queryWrapper.apply("end_date <= STR_TO_DATE({0}, '%Y-%m-%d')", dto.getEndDate());
    }
    queryWrapper.orderByDesc(orderCloumn);

    Page<GameRebateDetail> result = gameRebateDetailMapper.selectPage(page, queryWrapper);
    GameRebateDetail total = gameRebateDetailMapper.queryAllGameRebateReport(dto);
    Map<String, Object> otherData = new HashMap<String, Object>();
    otherData.put("totalData", total);
    pageDtoVO.setPage(result);
    pageDtoVO.setOtherData(otherData);
    return pageDtoVO;
  }

  /** 游戏返水拒发 */
  @Override
  public void reject(String account, Long periodId, String periodName, String remark) {
    QueryWrapper<GameRebateDetail> query = Wrappers.query();
    query.eq("account", account).eq("period_id", periodId).eq("period_name", periodName);
    GameRebateDetail gameRebateDetail = gameRebateDetailMapper.selectOne(query);
    verifyAndUpdate(
        gameRebateDetail.getMemberId(),
        gameRebateDetail.getPeriodId(),
        GameRebateReportStatus.REJECTED.getValue(),
        remark);
  }

  /** 游戏返水数据修改 */
  @Override
  public void modify(Long id, BigDecimal realRebateMoney, String remark) {
    String key = "modify_game_rebate_" + id;
    try {
      redisService.getStringOps().set(key, 1);
      GameRebateDetail gameRebateDetail = gameRebateDetailMapper.selectById(id);
      if (gameRebateDetail == null) {
        throw new ServiceException("返点记录不存在");
      }
      gameRebateDetail.setId(id);
      gameRebateDetail.setRealRebateMoney(realRebateMoney);
      gameRebateDetail.setRemark(remark);
      gameRebateDetailMapper.updateById(gameRebateDetail);
    } finally {
      redisService.getKeyOps().delete(key);
    }
  }

  @Override
  public List<GameRebateReport> queryDetail(GameRebateReportQueryDTO dto) {
    return this.lambdaQuery()
        .eq(
            ObjectUtils.isNotEmpty(dto.getUserAccount()),
            GameRebateReport::getAccount,
            dto.getUserAccount())
        .eq(
            ObjectUtils.isNotEmpty(dto.getPeriodId()),
            GameRebateReport::getPeriodId,
            dto.getPeriodId())
        .eq(
            ObjectUtils.isNotEmpty(dto.getPeriodName()),
            GameRebateReport::getPeriodName,
            dto.getPeriodName())
        .list();
  }

  @Override
  public void createForGameRebatePeriod(GameRebatePeriod gameRebatePeriod) {
    log.info("生成" + gameRebatePeriod.getName() + "游戏返水");
    List<GameRebateConfig> gameConfigList = gameRebateConfigMapper.selectList(new QueryWrapper<>());
    Map<String, List<BigDecimal[]>> map = convertToGameKindMap(gameConfigList);
    log.info("----------返水配置----------");
    log.info(JSONUtil.toJsonStr(map));
    // 锁定层级会员
    Set<String> memberList = new HashSet<String>();
    if (StringUtils.isNotBlank(gameRebatePeriod.getBlackLevels())) {
      String[] levels = gameRebatePeriod.getBlackLevels().split(",");
      List<String> levelsLists =
          Arrays.stream(levels).filter(StringUtils::isNotEmpty).collect(Collectors.toList());
      List<String> list = memberService.findAccountByUserLevelIn(levelsLists);
      // 转小写比较
      list.forEach(item -> memberList.add(item.toLowerCase()));
    }

    // 锁定会员
    if (StringUtils.isNotBlank(gameRebatePeriod.getBlackAccounts())) {
      String[] accounts = gameRebatePeriod.getBlackAccounts().split(",");
      Arrays.stream(accounts).forEach(item -> memberList.add(item.toLowerCase()));
    }

    // 游戏返水黑名单
    GameBlacklist black = new GameBlacklist();
    black.setBlackType(GameBlacklistTypeEnum.GAME_REBATE.code());
    List<GameBlacklist> gameBlacklists = gameBlacklistService.selectGameBlackList(black);

    // 游戏日报表
    String startDate = DateUtil.formatDate(gameRebatePeriod.getBeginDate());
    String endDate = DateUtil.formatDate(gameRebatePeriod.getEndDate());
    List<GameMemberDayReportVO> list =
        gameBetDailyReportMapper.findByStatTimeBetweenAndValidBetAmountGtZero(
            gameRebatePeriod, startDate, endDate);

    List<GameRebateReport> resultList = new ArrayList<>();
    list.stream()
        .filter(v -> !memberList.contains(v.getAccount().toLowerCase()))
        .filter(
            v -> {
              for (GameBlacklist gameBlack : gameBlacklists) {
                if (Objects.equals(
                        BlacklistConstant.BizBlacklistTargetType.USER.getValue(),
                        gameBlack.getTargetType())
                    && v.getAccount().equalsIgnoreCase(gameBlack.getTarget())
                    && ("," + gameBlack.getLiveCategory())
                        .contains("," + v.getPlatformCode() + ",")) {
                  return false;
                }
                if (Objects.equals(
                        BlacklistConstant.BizBlacklistTargetType.USER_LEVEL.getValue(),
                        gameBlack.getTargetType())
                    && v.getUserLevel().equalsIgnoreCase(gameBlack.getTarget())
                    && ("," + gameBlack.getLiveCategory())
                        .contains("," + v.getPlatformCode() + ",")) {
                  return false;
                }
              }
              return true;
            })
        .filter(
            dr ->
                map.containsKey(
                    combineCodeAndKind(dr.getUserLevel(), dr.getPlatformCode(), dr.getGameKind())))
        .forEach(
            dr -> {
              Optional<BigDecimal> rebate =
                  map
                      .get(
                          combineCodeAndKind(
                              dr.getUserLevel(), dr.getPlatformCode(), dr.getGameKind()))
                      .stream()
                      .filter(a -> a[0].compareTo(dr.getValidAmount()) <= 0)
                      .map(a -> a[1])
                      .findFirst();
              if (rebate.isPresent() && rebate.get().compareTo(BigDecimal.ZERO) > 0) {
                try {
                  GameRebateReport gameRebateReport =
                      formatGameRebateReport(gameRebatePeriod, dr, rebate.get());
                  resultList.add(gameRebateReport);
                } catch (Exception e) {
                  log.error("结算异常: " + JSONUtil.toJsonStr(dr));
                  throw new RuntimeException("结算异常", e);
                }
              }
            });

    // 批量插入
    if (resultList.size() > 0) {
      try {
        this.saveBatch(resultList);
      } catch (Exception e) {
        log.error("结算异常: " + JSONUtil.toJsonStr(resultList));
        throw new RuntimeException("结算异常", e);
      }
    }
  }

  private GameRebateReport formatGameRebateReport(
      GameRebatePeriod period, GameMemberDayReportVO memberDayReport, BigDecimal rebate) {
    // 校验，此统计日期已存在的返水报表
    String statTime = memberDayReport.getStatTime();
    // 此会员当前统计日期已经结算了返水的有效投注了
    BigDecimal existRebateValidAmount =
        gameRebateReportMapper.statisValidAmountByTime(
            statTime, memberDayReport.getAccount(), memberDayReport.getGameKind(), period.getId());
    if (existRebateValidAmount.compareTo(BigDecimal.ZERO) > 0) {
      BigDecimal subValidAmount = memberDayReport.getValidAmount().subtract(existRebateValidAmount);
      memberDayReport.setValidAmount(
          subValidAmount.compareTo(BigDecimal.ZERO) > 0 ? subValidAmount : BigDecimal.ZERO);
    }
    GameRebateReport gameRebateReport = new GameRebateReport();
    BeanUtils.copyProperties(memberDayReport, gameRebateReport);
    gameRebateReport.setId(null);
    gameRebateReport.setRemark(statTime + "-游戏返水, 返点率:" + rebate + "%");
    gameRebateReport.setPeriodId(period.getId());
    gameRebateReport.setPeriodName(period.getName());
    gameRebateReport.setBeginDate(DateUtil.beginOfDay(DateUtil.parse(statTime)));
    //    gameRebateReport.setEndDate(DateUtil.endOfDay(DateUtil.parse(statTime)));
    gameRebateReport.setEndDate(DateUtil.beginOfDay(DateUtil.parse(statTime)));
    gameRebateReport.setStatus(GameRebateReportStatus.UNACCEPTED.getValue());
    BigDecimal rebateMoney =
        NumberUtil.div(
            memberDayReport.getValidAmount().multiply(rebate), 100, 4, RoundingMode.HALF_EVEN);
    gameRebateReport.setRebateMoney(rebateMoney);
    gameRebateReport.setRealRebateMoney(rebateMoney);
    log.info("----------用戶返水----------");
    log.info(JSONUtil.toJsonStr(gameRebateReport));
    return gameRebateReport;
  }

  /**
   * 转化返水配置为Map<String, List<Double[]>>结构 key: ${游戏类型}与${游戏子类型}拼接 value: 按照[${投注额阈值},
   * ${返水比率}]组成的以${投注额阈值}降序排序的列表
   */
  private Map<String, List<BigDecimal[]>> convertToGameKindMap(List<GameRebateConfig> gameRebates) {
    Map<String, List<BigDecimal[]>> map = new HashMap<>();
    gameRebates.forEach(
        lr -> {
          log.info("lr:", lr);
          log.info(lr.getJson());
          List<Map> items = JsonUtils.toObject(lr.getJson(), List.class);
          items.stream()
              .filter(
                  item -> { // 过滤无返水或返水为0的项
                    Number rebate = (Number) item.get("rebate");
                    return rebate != null && rebate.doubleValue() > 0;
                  })
              .forEach(
                  item -> {
                    String key =
                        combineCodeAndKind(
                            lr.getUserLevel(),
                            (String) item.get("game"),
                            (String) item.get("kind"));
                    List<BigDecimal[]> rebates;
                    if (map.containsKey(key)) {
                      rebates = map.get(key);
                    } else {
                      rebates = new ArrayList<>();
                      map.put(key, rebates);
                    }
                    rebates.add(
                        new BigDecimal[] {
                          lr.getMoney(), new BigDecimal(item.get("rebate").toString())
                        });
                  });
        });
    // 排序
    map.forEach(
        (k, v) ->
            v.sort(
                (a1, a2) -> {
                  double d = a1[0].subtract(a2[0]).doubleValue();
                  return Double.compare(0.0, d);
                }));
    return map;
  }

  private String combineCodeAndKind(String userLevel, String gameCode, String gameKind) {
    return String.format("%s_%s__%s", userLevel, gameCode, gameKind);
  }

  @Override
  public void accept(
      Long periodId, Long memberId, BigDecimal realRebateMoney, String remark, String statTime)
      throws Exception {
    verifyAndUpdate(memberId, periodId, GameRebateReportStatus.ACCEPTED.getValue(), remark);
    if (realRebateMoney.compareTo(BigDecimal.ZERO) <= 0) {
      return;
    }

    LimitInfo limitInfo = limitInfoService.getLimitInfo(LimitEnums.MEMBER_WITHDRAW_LIMIT.getName());
    if (ObjectUtils.isNull(limitInfo)) {
      throw new ServiceException("未找到会员提现限制配置");
    }

    // 查找返水打码量比例 默认比例为1
    MemberWithdrawLimit memberWithdrawLimit =
        JSONObject.parseObject(limitInfo.getValue(), MemberWithdrawLimit.class);
    BigDecimal dmlWaterRate =
        memberWithdrawLimit.getDmlWaterRate() != null
            ? new BigDecimal(memberWithdrawLimit.getDmlWaterRate())
            : BigDecimal.ONE;

    MemberInfoVO member = memberService.getInfo(memberId);
    // 添加打码量
    RechargeOrder rechargeOrder = new RechargeOrder();
    rechargeOrder.setMemberId(memberId);
    rechargeOrder.setAmount(BigDecimal.ZERO);
    rechargeOrder.setNormalDml(BigDecimal.ZERO);
    rechargeOrder.setDiscountAmount(realRebateMoney);
    rechargeOrder.setDiscountDml(realRebateMoney.multiply(dmlWaterRate));
    rechargeOrder.setRemarks(remark);
    rechargeOrder.setAccount(member.getAccount());
    validWithdrawService.addRechargeOrder(rechargeOrder);

    // 写账变
    MemberBill memberBill = new MemberBill();
    memberBill.setMemberId(member.getId());
    memberBill.setAccount(member.getAccount());
    memberBill.setMemberPath(member.getSuperPath());
    memberBill.setTranType(TranTypes.GAME_REBATE.getValue());
    memberBill.setAmount(realRebateMoney);
    memberBill.setBalance(member.getBalance());
    memberBill.setRemark(remark);
    memberBill.setOrderNo(String.valueOf(IdGeneratorSnowflake.getInstance().nextId()));
    memberBill.setContent(
        String.format(
            "游戏返点派发：用户 %s 于 %s 收到返点金额 %.3f 元，最新余额 %.3f 元。",
            member.getAccount(),
            DateUtil.now(),
            realRebateMoney,
            member.getBalance().add(realRebateMoney)));
    memberBill.setOperator("system");
    memberBillService.save(memberBill);

    // 修改账户余额
    memberInfoService.updateBalanceWithRecharge(
            member.getId(), realRebateMoney, realRebateMoney, 0);

    // 填充会员日报
    memberDayReportService.addUpdateWaterAmount(member, realRebateMoney, statTime);
  }

  @Override
  public void deleteByPeriodId(Long periodId) {
    LambdaQueryWrapper<GameRebateReport> query = Wrappers.lambdaQuery();
    query.eq(ObjectUtils.isNotEmpty(periodId), GameRebateReport::getPeriodId, periodId);
    this.remove(query);
  }

  private void verifyAndUpdate(Long memberId, Long periodId, Integer status, String remark) {
    LambdaQueryWrapper<GameRebateDetail> query = Wrappers.lambdaQuery();
    query
        .eq(ObjectUtils.isNotEmpty(memberId), GameRebateDetail::getMemberId, memberId)
        .eq(ObjectUtils.isNotEmpty(periodId), GameRebateDetail::getPeriodId, periodId);
    GameRebateDetail gameRebateDetail = gameRebateDetailMapper.selectOne(query);

    Assert.notNull(gameRebateDetail.getStatus(), "返点记录状态异常");
    if (!GameRebateReportStatus.UNACCEPTED.match(gameRebateDetail.getStatus())) {
      throw new ServiceException("返点记录已处理");
    }
    Assert.notNull(gameRebateDetail.getMemberId(), "会员不存在!");

    gameRebateDetail.setStatus(status);
    gameRebateDetail.setRealRebateMoney(gameRebateDetail.getRealRebateMoney());
    gameRebateDetail.setRemark(remark);
    gameRebateDetailMapper.update(gameRebateDetail, query);
  }

  private void rollBackAndUpdate(Long memberId, Long periodId, Integer status, String remark) {
    LambdaQueryWrapper<GameRebateDetail> query = Wrappers.lambdaQuery();
    query
        .eq(ObjectUtils.isNotEmpty(memberId), GameRebateDetail::getMemberId, memberId)
        .eq(ObjectUtils.isNotEmpty(periodId), GameRebateDetail::getPeriodId, periodId);
    GameRebateDetail gameRebateDetail = gameRebateDetailMapper.selectOne(query);
    if (gameRebateDetail == null) {
      throw new ServiceException("返点记录不存在");
    }
    if (gameRebateDetail.getStatus() == null) {
      throw new ServiceException("返点记录状态异常");
    }
    gameRebateDetail.setStatus(status);
    gameRebateDetail.setRemark(remark);
    gameRebateDetailMapper.update(gameRebateDetail, query);
  }

  @Override
  public void rollBack(
      Long memberId, Long periodId, String periodName, BigDecimal realRebateMoney, String remark) {

    rollBackAndUpdate(memberId, periodId, GameRebateReportStatus.ROLLBACKED.getValue(), remark);
    if (realRebateMoney.compareTo(BigDecimal.ZERO) <= 0) {
      return;
    }

    MemberInfoVO member = memberService.getInfo(memberId);
    // 批量修改打码量--游戏返水优惠打码量设为0
    validWithdrawService.rollGameRebateDml(remark);
    // 写账变
    MemberBill memberBill = new MemberBill();
    memberBill.setOrderNo(String.valueOf(IdGeneratorSnowflake.getInstance().nextId()));
    memberBill.setMemberId(member.getId());
    memberBill.setAccount(member.getAccount());
    memberBill.setMemberPath(member.getSuperPath());
    memberBill.setTranType(TranTypes.GAME_ROLLBACK.getValue());
    memberBill.setAmount(realRebateMoney.negate());
    memberBill.setBalance(member.getBalance());
    memberBill.setRemark(remark);
    memberBill.setContent(
        String.format(
            "游戏返点回收：用户 %s 于 %s 回收 %s 返点金额 %.3f 元，最新余额 %.3f 元。",
            member.getAccount(),
            DateUtil.now(),
            periodName,
            realRebateMoney,
            member.getBalance().subtract(realRebateMoney)));
    memberBill.setOperator("system");
    memberBillService.save(memberBill);

    // 修改账户余额
    memberInfoService.updateBalance(member.getId(), realRebateMoney.negate());
  }

  @Override
  public List<GameReportVO> queryGameReport(GameRebateStatisQueryDTO dto) {
    if (com.gameplat.base.common.util.StringUtils.isNotBlank(dto.getSuperAccount())) {
      Member member = memberService.getByAccount(dto.getSuperAccount()).orElse(null);
      Assert.notNull(member, "用户不存在");
      dto.setUserPaths(member.getSuperPath());
      // 是否代理账号
      dto.setAccount(null);
    }
    return gameRebateReportMapper.queryGameReport(dto);
  }

  @Override
  public void exportGameReport(GameRebateStatisQueryDTO dto, HttpServletResponse response) throws Exception {

    if (StringUtils.isNotBlank(dto.getSuperAccount())) {
      Member member = memberService.getByAccount(dto.getSuperAccount()).orElse(null);
      if (ObjectUtils.isEmpty(member)) {
        throw new Exception("用户不存在");
      }
      dto.setUserPaths(member.getSuperPath());
      dto.setAccount(StringUtils.EMPTY);
    }
    List<GameReportVO> list = gameRebateReportMapper.queryGameReport(dto);
    log.info("游戏交收报表查出{}条数据", list.size());
    String title = MessageFormat.format("{0}至{1}游戏交收数据", dto.getBeginTime(), dto.getEndTime());
    ExportParams exportParams = new ExportParams(title, "游戏交收数据");
    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename = gameReport.xls");

    List<GameSettlementReportVO> settlementReports = new ArrayList<>();
    list.forEach(o ->{
      GameSettlementReportVO settlementReport = new GameSettlementReportVO();
      BeanUtils.copyProperties(o, settlementReport);
      settlementReports.add(settlementReport);
    });
    log.info("游戏交收报表导出{}条数据", settlementReports.size());
    settlementReports.forEach(o -> o.setPlatformName(GamePlatformEnum.getName(o.getPlatformCode())));

    try (Workbook workbook = ExcelExportUtil.exportExcel(exportParams, GameSettlementReportVO.class, settlementReports)) {
      workbook.write(response.getOutputStream());
    } catch (IOException e) {
      log.error("请求导出游戏交收数据报错", e);
    }

  }
}
