package com.gameplat.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.constant.MemberServiceKeyConstant;
import com.gameplat.admin.constant.SystemConstant;
import com.gameplat.admin.constant.TrueFalse;
import com.gameplat.admin.convert.DivideDetailConvert;
import com.gameplat.admin.convert.DividePeriodsConvert;
import com.gameplat.admin.convert.DivideSummaryConvert;
import com.gameplat.admin.convert.MessageInfoConvert;
import com.gameplat.admin.enums.BlacklistConstant;
import com.gameplat.admin.enums.DivideStatusEnum;
import com.gameplat.admin.enums.PushMessageEnum;
import com.gameplat.admin.mapper.*;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.DivideGameReportVO;
import com.gameplat.admin.model.vo.DividePeriodsVO;
import com.gameplat.admin.model.vo.FissionConfigLevelVo;
import com.gameplat.admin.model.vo.GameDivideVo;
import com.gameplat.admin.service.*;
import com.gameplat.base.common.enums.EnableEnum;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.snowflake.IdGeneratorSnowflake;
import com.gameplat.common.constant.NumberConstant;
import com.gameplat.common.enums.MemberEnums;
import com.gameplat.common.enums.TranTypes;
import com.gameplat.common.enums.UserTypes;
import com.gameplat.common.lang.Assert;
import com.gameplat.common.model.bean.AgentConfig;
import com.gameplat.model.entity.blacklist.BizBlacklist;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.member.MemberBill;
import com.gameplat.model.entity.member.MemberInfo;
import com.gameplat.model.entity.message.Message;
import com.gameplat.model.entity.message.MessageDistribute;
import com.gameplat.model.entity.proxy.DivideDetail;
import com.gameplat.model.entity.proxy.DivideFissionConfig;
import com.gameplat.model.entity.proxy.DividePeriods;
import com.gameplat.model.entity.proxy.DivideSummary;
import com.gameplat.redis.redisson.DistributedLocker;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.context.UserCredential;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class DividePeriodsServiceImpl extends ServiceImpl<DividePeriodsMapper, DividePeriods>
    implements DividePeriodsService {

  @Autowired private DividePeriodsMapper dividePeriodsMapper;

  @Autowired private DividePeriodsConvert periodsConvert;

  @Autowired private AgentConfigService agentConfigService;

  @Autowired private DivideDetailMapper detailMapper;

  @Autowired private DivideSummaryMapper summaryMapper;

  @Autowired private StringRedisTemplate redisTemplate;

  @Autowired private DistributedLocker distributedLocker;

  @Autowired private GameBetDailyReportMapper betDailyReportMapper;

  @Autowired private BizBlacklistMapper blacklistMapper;

  @Autowired private MemberService memberService;

  @Autowired private DivideLayerConfigService layerConfigService;

  @Autowired private DivideFixConfigService fixConfigService;

  @Autowired private DivideDetailConvert detailConvert;

  @Autowired private DivideFissionConfigService fissionConfigService;

  @Autowired private DivideSummaryConvert summaryConvert;

  @Autowired private MemberInfoService memberInfoService;

  @Autowired private MemberBillService memberBillService;

  @Autowired private MessageInfoConvert messageInfoConvert;

  @Autowired private MessageMapper messageMapper;

  @Autowired private MessageDistributeService messageDistributeService;

  @Autowired private RedissonClient redissonClient;

  /**
   * 分页列表
   *
   * @param page
   * @param dto
   * @return
   */
  @Override
  @SentinelResource(value = "queryPage")
  public IPage<DividePeriodsVO> queryPage(PageDTO<DividePeriods> page, DividePeriodsQueryDTO dto) {
    QueryWrapper<DividePeriods> queryWrapper = new QueryWrapper<>();
    queryWrapper.orderByDesc("create_time");
    return dividePeriodsMapper.selectPage(page, queryWrapper).convert(periodsConvert::toVo);
  }

  /**
   * 添加
   *
   * @param dto
   */
  @Override
  @SentinelResource("add")
  public void add(DividePeriodsDTO dto) {
    Assert.isTrue(StrUtil.isNotBlank(dto.getStartDate()), "期数起始时间不能为空！");
    Assert.isTrue(StrUtil.isNotBlank(dto.getEndDate()), "期数截止时间不能为空！");
    DividePeriods saveObj = periodsConvert.toEntity(dto);
    saveObj.setStartDate(DateUtil.parse(saveObj.getStartDate()).toDateStr());
    saveObj.setEndDate(DateUtil.parse(saveObj.getEndDate()).toDateStr());
    AgentConfig agentConfig = agentConfigService.getAgentConfig();
    Integer divideModel = agentConfig.getDivideModel();
    divideModel = ObjectUtil.defaultIfNull(divideModel, DivideStatusEnum.DIVIDE_MODEL_LAYER);
    saveObj.setDivideType(divideModel);
    Assert.isTrue(this.save(saveObj), "添加失败！");
  }

  /**
   * 编辑
   *
   * @param dto
   */
  @Override
  @SentinelResource("edit")
  public void edit(DividePeriodsDTO dto) {
    Assert.isTrue(StrUtil.isNotBlank(dto.getStartDate()), "期数起始时间不能为空！");
    Assert.isTrue(StrUtil.isNotBlank(dto.getEndDate()), "期数截止时间不能为空！");
    DividePeriods editObj = periodsConvert.toEntity(dto);
    editObj.setStartDate(DateUtil.parse(dto.getStartDate()).toDateStr());
    editObj.setEndDate(DateUtil.parse(dto.getEndDate()).toDateStr());
    Assert.isTrue(this.updateById(editObj), "编辑失败！");
  }

  /**
   * 删除
   *
   * @param ids
   */
  @Override
  @SentinelResource("delete")
  public void delete(String ids) {
    Assert.isTrue(StrUtil.isNotBlank(ids), "参数为空！");
    String[] idArr = ids.split(",");
    for (String id : idArr) {
      DividePeriods dividePeriods = dividePeriodsMapper.selectById(id);
      if (dividePeriods.getGrantStatus()
          == DivideStatusEnum.PERIODS_SETTLE_STATUS_SETTLEED.getValue()) {
        throw new ServiceException("已派发不能删除！");
      }
      QueryWrapper<DivideDetail> deleteDetailWrapper = new QueryWrapper<>();
      deleteDetailWrapper.eq("periods_id", Long.valueOf(id));
      detailMapper.delete(deleteDetailWrapper);

      QueryWrapper<DivideSummary> deleteSummaryWrapper = new QueryWrapper<>();
      deleteSummaryWrapper.eq("periods_id", Long.valueOf(id));
      summaryMapper.delete(deleteSummaryWrapper);

      Assert.isTrue(this.removeById(id), ("删除期数:" + id + "失败！"));
    }
  }

  /**
   * 期数结算
   *
   * @param dto
   */
  @Override
  @SentinelResource(value = "settle")
  public void settle(DividePeriodsDTO dto) {
    Assert.isTrue(dto.getId() != null, "期数主键ID参数缺失！");
    // 外层先锁住  防止重复点击
    String key = "divide:settle:" + dto.getId();
    Boolean isLock = redisTemplate.hasKey(key);
    Assert.isTrue(!isLock, "您的操作过于频繁！请稍后重试！");
    distributedLocker.lock(key);
    try {
      // 结算 分红汇总，分红详情
      this.settleOther(dto.getId());

      Assert.isTrue(
          this.lambdaUpdate()
              .set(
                  DividePeriods::getSettleStatus,
                  DivideStatusEnum.PERIODS_SETTLE_STATUS_SETTLEED.getValue())
              .eq(DividePeriods::getId, dto.getId())
              .update(new DividePeriods()),
          "修改期数结算状态失败！");
    } catch (Exception e) {
      throw new ServiceException("期数结算失败！");
    } finally {
      distributedLocker.unlock(key);
    }
  }

  /**
   * 期数派发
   *
   * @param dto
   */
  @Override
  @SentinelResource(value = "grant")
  public void grant(DividePeriodsDTO dto) {
    UserCredential userCredential = SecurityUserHolder.getCredential();
    // 获取需要派发的期数
    Assert.isTrue(dto.getId() != null, "期数主键ID参数缺失！");
    DividePeriods dividePeriods = dividePeriodsMapper.selectById(dto.getId());
    Assert.isTrue(
        dividePeriods.getSettleStatus()
            == DivideStatusEnum.PERIODS_SETTLE_STATUS_SETTLEED.getValue(),
        "请先结算！");
    Assert.isTrue(
        dividePeriods.getGrantStatus() == DivideStatusEnum.PERIODS_GRANT_STATUS_UNGRANT.getValue(),
        "必须为未派发状态！");
    // 外层先锁住  防止重复点击
    String key = "divide:grant:" + dto.getId();
    RLock lock = redissonClient.getLock(key);
    if (lock.isLocked()) {
      return;
    }
    distributedLocker.lock(key);
    try {
      // 先获取得到当前未派发的分红汇总数据
      QueryWrapper<DivideSummary> querySummaryWrapper = new QueryWrapper<>();
      querySummaryWrapper.eq("periods_id", dto.getId());
      querySummaryWrapper.eq("status", DivideStatusEnum.SUMMARY_STATUS_SETTLEED.getValue());
      List<DivideSummary> divideSummaries = summaryMapper.selectList(querySummaryWrapper);
      if (CollectionUtil.isNotEmpty(divideSummaries)) {
        // 按分红汇总数据派发
        this.summaryGrant(dividePeriods, divideSummaries, userCredential);
      }
      // 修改期数的状态
      DividePeriodsDTO editDto =
          DividePeriodsDTO.builder()
              .id(dto.getId())
              .grantStatus(DivideStatusEnum.PERIODS_GRANT_STATUS_GRANTED.getValue())
              .build();
      DividePeriods editPo = periodsConvert.toEntity(editDto);
      int i = dividePeriodsMapper.updateById(editPo);
      Assert.isTrue(i > NumberConstant.ZERO, "派发失败！");
    } catch (Exception e) {
      throw new ServiceException("期数派发失败！");
    } finally {
      distributedLocker.unlock(key);
    }
  }

  /**
   * 期数回收
   *
   * @param dto
   */
  @Override
  @SentinelResource(value = "recycle")
  public void recycle(DividePeriodsDTO dto) {
    UserCredential userCredential = SecurityUserHolder.getCredential();
    // 获取需要派发的期数
    Assert.isTrue(dto.getId() != null, "期数主键ID参数缺失！");
    DividePeriods dividePeriods = dividePeriodsMapper.selectById(dto.getId());
    Assert.isTrue(
        dividePeriods.getGrantStatus() == DivideStatusEnum.PERIODS_GRANT_STATUS_GRANTED.getValue(),
        "必须为已派发状态！");
    // 外层先锁住  防止重复点击
    String key = "divide:grant:" + dto.getId();
    RLock lock = redissonClient.getLock(key);
    if (lock.isLocked()) {
      return;
    }
    distributedLocker.lock(key);
    try {
      // 查询此期已派发的汇总数据
      QueryWrapper<DivideSummary> querySummaryWrapper = new QueryWrapper<>();
      querySummaryWrapper.eq("periods_id", dto.getId());
      querySummaryWrapper.eq("status", DivideStatusEnum.SUMMARY_STATUS_GRANTED.getValue());
      List<DivideSummary> divideSummaries = summaryMapper.selectList(querySummaryWrapper);
      if (CollectionUtil.isNotEmpty(divideSummaries)) {
        // 按分红汇总数据派发
        this.summaryRecycle(dividePeriods, divideSummaries, userCredential);
      }
      // 修改期数的状态
      DividePeriodsDTO editDto =
          DividePeriodsDTO.builder()
              .id(dto.getId())
              .grantStatus(DivideStatusEnum.PERIODS_GRANT_STATUS_RECYCLE.getValue())
              .build();
      DividePeriods editPo = periodsConvert.toEntity(editDto);
      int i = dividePeriodsMapper.updateById(editPo);
      Assert.isTrue(i > NumberConstant.ZERO, "期数回收失败！");
    } catch (Exception e) {
      throw new ServiceException("期数回收失败!");
    } finally {
      distributedLocker.unlock(key);
    }
  }

  //    @Async
  public void settleOther(Long periodsId) {
    DividePeriods periods = this.getById(periodsId);
    if (BeanUtil.isEmpty(periods)
        || periods.getGrantStatus() == DivideStatusEnum.PERIODS_GRANT_STATUS_GRANTED.getValue()) {
      return;
    }
    // 删除分红详情
    QueryWrapper<DivideDetail> deleteDetailWrapper = new QueryWrapper<>();
    deleteDetailWrapper.eq("periods_id", periodsId);
    detailMapper.delete(deleteDetailWrapper);
    // 删除分红汇总
    QueryWrapper<DivideSummary> deleteSummaryWrapper = new QueryWrapper<>();
    deleteSummaryWrapper.eq("periods_id", periodsId);
    summaryMapper.delete(deleteSummaryWrapper);

    // 分红模式 1 固定  2 裂变  3 层层代 4 平级
    Integer divideModel = periods.getDivideType();
    AgentConfig agentConfig = agentConfigService.getAgentConfig();
    Integer isIncludeAgent = agentConfig.getIsIncludeAgent();

    // 获取用户游戏分组统计数据
    List<DivideGameReportVO> periodsGameReport =
        betDailyReportMapper.findReportForDivide(
            periods.getStartDate(), periods.getEndDate(), isIncludeAgent);
    // 过滤下无代理路径的脏数据
    periodsGameReport =
        periodsGameReport.stream()
            .filter(item -> StrUtil.isNotBlank(item.getUserPaths()))
            .collect(Collectors.toList());
    // 获取业务 分红黑名单 集合
    QueryWrapper<BizBlacklist> queryBizWrapper = new QueryWrapper<>();
    queryBizWrapper.like("types", BlacklistConstant.BizBlacklistType.DL_RATIO.getValue());
    List<BizBlacklist> bizBlacklists = blacklistMapper.selectList(queryBizWrapper);
    // 会员账号业务黑名单
    List<BizBlacklist> accountBlackList =
        bizBlacklists.stream()
            .filter(item -> item.getTargetType() == TrueFalse.FALSE.getValue())
            .collect(Collectors.toList());
    // 用户层级业务黑名单
    List<BizBlacklist> userLevelBlackList =
        bizBlacklists.stream()
            .filter(item -> item.getTargetType() == TrueFalse.TRUE.getValue())
            .collect(Collectors.toList());
    List<String> accountBlacks = new ArrayList<>();
    List<String> userLevelBlacks = new ArrayList<>();

    if (CollectionUtil.isNotEmpty(accountBlackList)) {
      accountBlacks =
          accountBlackList.stream().map(BizBlacklist::getTarget).collect(Collectors.toList());
    }
    if (CollectionUtil.isNotEmpty(userLevelBlackList)) {
      userLevelBlacks =
          userLevelBlackList.stream().map(BizBlacklist::getTarget).collect(Collectors.toList());
    }

    List<DivideDetailDto> detailList = new ArrayList<>();

    if (CollectionUtil.isNotEmpty(periodsGameReport)) {
      for (DivideGameReportVO gameReportVO : periodsGameReport) {
        String userType = gameReportVO.getUserType(); // M 会员  A 代理
        String account = gameReportVO.getAccount();
        Integer agentLevel = gameReportVO.getAgentLevel();
        String userPaths = gameReportVO.getUserPaths();
        if (StrUtil.isBlank(userPaths)) {
          continue;
        }
        userPaths = getFinalAgentPath(userPaths);
        // 会员不参与分红
        if (userType.equalsIgnoreCase(UserTypes.MEMBER.value())) {
          if (userPaths.endsWith("/" + account)) {
            int j = userPaths.lastIndexOf("/" + account);
            userPaths =
                j >= NumberConstant.ZERO ? userPaths.substring(NumberConstant.ZERO, j) : userPaths;
          }
        }
        if (StrUtil.isBlank(userPaths)) {
          continue;
        }
        userPaths =
            userPaths.startsWith(SystemConstant.DEFAULT_WEB_ROOT + "/")
                ? userPaths.replaceFirst(SystemConstant.DEFAULT_WEB_ROOT + "/", "")
                : userPaths;
        userPaths =
            userPaths.startsWith(SystemConstant.DEFAULT_WAP_ROOT + "/")
                ? userPaths.replaceFirst(SystemConstant.DEFAULT_WAP_ROOT + "/", "")
                : userPaths;
        userPaths =
            userPaths.startsWith(SystemConstant.DEFAULT_TEST_ROOT + "/")
                ? userPaths.replaceFirst(SystemConstant.DEFAULT_TEST_ROOT + "/", "")
                : userPaths;
        if (divideModel == DivideStatusEnum.DIVIDE_MODEL_FIX) { // 固定模式
          detailList.addAll(
              fixSettle(periodsId, userPaths, gameReportVO, accountBlacks, userLevelBlacks));
        } else if (divideModel == DivideStatusEnum.DIVIDE_MODEL_FISSION) { // 裂变
          detailList.addAll(
              fissionSettle(periodsId, userPaths, gameReportVO, accountBlacks, userLevelBlacks));
        } else if (divideModel == DivideStatusEnum.DIVIDE_MODEL_LAYER) { // 层层代
          detailList.addAll(
              layerSettle(periodsId, userPaths, gameReportVO, accountBlacks, userLevelBlacks));
        }
      }

      // 处理 分红汇总数据
      if (CollectionUtil.isNotEmpty(detailList)) {
        // 按proxyId 分红代理id分组
        Map<String, List<DivideDetailDto>> groupByPname =
            detailList.stream().collect(Collectors.groupingBy(DivideDetailDto::getProxyName));
        // 求稳  再过滤掉webRoot
        groupByPname.remove(SystemConstant.DEFAULT_WEB_ROOT);
        groupByPname.remove(SystemConstant.DEFAULT_WAP_ROOT);
        groupByPname.remove(SystemConstant.DEFAULT_TEST_ROOT);
        if (BeanUtil.isNotEmpty(groupByPname)) {
          saveSummaryForSettle(groupByPname, periodsId, agentConfig.getIsGrand());
        }
      }
    }
  }

  /**
   * 固定比例模式结算
   *
   * @param periodsId
   * @param userPaths
   * @param gameReportVO
   * @param accountBlacks
   * @param userLevelBlacks
   * @return
   */
  public List<DivideDetailDto> fixSettle(
      Long periodsId,
      String userPaths,
      DivideGameReportVO gameReportVO,
      List<String> accountBlacks,
      List<String> userLevelBlacks) {
    List<DivideDetailDto> returnList = new ArrayList<>();
    String[] nameStr = userPaths.split("/");
    if (ArrayUtil.isEmpty(nameStr)) {
      return new ArrayList<>();
    }
    String tmpName = nameStr[0];
    // 顶级代理
    Member superMember =
        memberService
            .getByAccount(tmpName)
            .orElseThrow(() -> new ServiceException(tmpName + "账号不存在!"));
    GameDivideVo configByFirstCode =
        fixConfigService.getConfigByGameCode(superMember.getAccount(), gameReportVO.getGameKind());
    if (BeanUtil.isEmpty(configByFirstCode)) {
      return new ArrayList<>();
    }
    if (configByFirstCode.getAmountRatio().compareTo(BigDecimal.ZERO) <= NumberConstant.ZERO
        || configByFirstCode.getDivideRatio().compareTo(BigDecimal.ZERO) <= NumberConstant.ZERO) {
      return new ArrayList<>();
    }
    Integer settleType = configByFirstCode.getSettleType();
    BigDecimal amountRatio = configByFirstCode.getAmountRatio().divide(new BigDecimal("100"));
    BigDecimal divideRatio = configByFirstCode.getDivideRatio().divide(new BigDecimal("100"));
    Integer userLevelNum = gameReportVO.getAgentLevel();
    for (int i = 0; i < nameStr.length; i++) {
      // 过滤黑名单
      String username = nameStr[i];
      // 顶级代理
      Member currentMember =
          memberService
              .getAgentByAccount(username)
              .orElseThrow(() -> new ServiceException(username + "代理账号不存在!"));
      if (CollectionUtil.isNotEmpty(accountBlacks)) {
        if (accountBlacks.contains(username)) {
          log.info("业务===固定比例分红-账号==={}-存在业务黑名单,无法结算", username);
          return new ArrayList<>();
        }
      }
      if (CollectionUtil.isNotEmpty(userLevelBlacks)) {
        if (userLevelBlacks.contains(currentMember.getAgentLevel())) {
          log.info("业务===固定比例分红-账号==={}-存在业务黑名单,无法结算", username);
          return new ArrayList<>();
        }
      }
      // 上级的agentLevel值比投注人的还要大 则直接返回
      if (currentMember.getAgentLevel() > userLevelNum) {
        continue;
      }

      DivideDetailDto saveDetailDto =
          DivideDetailDto.builder()
              .periodsId(periodsId)
              .userId(gameReportVO.getMemberId()) // 分红来源用户Id
              .userName(gameReportVO.getAccount()) // 分红来源用户名称
              .agentLevel(gameReportVO.getAgentLevel()) // 分红源的级别
              .superPath(gameReportVO.getUserPaths()) // 分红源的代理路径
              .userType(gameReportVO.getUserType()) // 分红源的用户类型
              .proxyId(currentMember.getId()) // 分红代理的ID
              .proxyName(currentMember.getAccount()) // 分红代理的名称
              .superId(currentMember.getParentId()) // 分红代理的上级
              .superName(currentMember.getParentName()) // 分红代理的上级
              .proxyAgentLevel(currentMember.getAgentLevel())
              .proxyAgentPath(currentMember.getSuperPath())
              .liveCode(gameReportVO.getGameType())
              .code(gameReportVO.getGameKind())
              .validAmount(gameReportVO.getValidAmount())
              .winAmount(gameReportVO.getWinAmount().negate())
              .settleType(settleType)
              .amountRatio(amountRatio)
              .build();
      // 分红比例的（源用户的层级 - 分红代理的层级)次幂
      Integer subLevel = userLevelNum - currentMember.getAgentLevel();
      if (subLevel < 0) {
        continue;
      }
      if (subLevel == 0) {
        subLevel = 1;
      }
      BigDecimal pow = divideRatio.pow((subLevel));
      saveDetailDto.setDivideRatio(pow); // 代理分红比例
      // 分红基数金额
      BigDecimal baseAmount =
          settleType == NumberConstant.ONE
              ? gameReportVO.getWinAmount().negate()
              : gameReportVO.getValidAmount();
      // 分红金额
      saveDetailDto.setDivideAmount(baseAmount.multiply(amountRatio).multiply(pow));
      // 分红公式
      saveDetailDto.setDivideFormula(
          baseAmount.toString()
              + "x"
              + amountRatio.multiply(BigDecimal.valueOf(100)).toString()
              + "%x"
              + divideRatio.multiply(BigDecimal.valueOf(100)).toString()
              + "%^"
              + (subLevel));
      // 添加分红详情
      DivideDetail detail = detailConvert.toEntity(saveDetailDto);
      int insertCount = detailMapper.insert(detail);
      if (insertCount > NumberConstant.ZERO) {
        returnList.add(saveDetailDto);
      }
    }
    return returnList;
  }

  /**
   * 裂变模式结算
   *
   * @param periodsId
   * @param userPaths
   * @param gameReportVO
   * @param accountBlacks
   * @param userLevelBlacks
   * @return
   */
  public List<DivideDetailDto> fissionSettle(
      Long periodsId,
      String userPaths,
      DivideGameReportVO gameReportVO,
      List<String> accountBlacks,
      List<String> userLevelBlacks) {
    List<DivideDetailDto> returnList = new ArrayList<>();
    String[] nameStr = userPaths.split("/");
    if (ArrayUtil.isEmpty(nameStr)) {
      return new ArrayList<>();
    }
    String tmpName = nameStr[0];
    // 顶级代理
    Member superMember =
        memberService
            .getByAccount(tmpName)
            .orElseThrow(() -> new ServiceException(tmpName + "账号不存在!"));
    GameDivideVo fissionConfig =
        fissionConfigService.getConfigByFirstCode(
            superMember.getAccount(), gameReportVO.getGameKind());
    if (BeanUtil.isEmpty(fissionConfig)) {
      return new ArrayList<>();
    }
    if (fissionConfig.getAmountRatio().compareTo(BigDecimal.ZERO) <= NumberConstant.ZERO) {
      return new ArrayList<>();
    }
    DivideFissionConfig tmpFissionConfig =
        fissionConfigService.getByAccount(superMember.getAccount());
    if (BeanUtil.isEmpty(tmpFissionConfig)) {
      return new ArrayList<>();
    }
    String recycleConfig = tmpFissionConfig.getRecycleConfig();
    List<FissionConfigLevelVo> fissionConfigLevelVos = new ArrayList<>();
    if (StrUtil.isNotBlank(recycleConfig)) {
      fissionConfigLevelVos =
          JSONUtil.toList(JSONUtil.parseArray(recycleConfig), FissionConfigLevelVo.class);
    }
    BigDecimal outRecycleConfig = tmpFissionConfig.getRecycleOutConfig();
    if (CollectionUtil.isEmpty(fissionConfigLevelVos)
        && outRecycleConfig.compareTo(BigDecimal.ZERO) <= NumberConstant.ZERO) {
      return new ArrayList<>();
    }

    Integer settleType = fissionConfig.getSettleType();
    BigDecimal amountRatio = fissionConfig.getAmountRatio().divide(new BigDecimal("100"));
    Map<Integer, List<FissionConfigLevelVo>> levelDivideRatioMap =
        fissionConfigLevelVos.stream()
            .collect(Collectors.groupingBy(FissionConfigLevelVo::getLevel));
    BigDecimal divideLevelRatio = BigDecimal.ZERO;
    Integer userLevelNum = gameReportVO.getAgentLevel();
    for (int i = 0; i < nameStr.length; i++) {
      // 过滤黑名单
      String username = nameStr[i];
      // 顶级代理
      Member currentMember =
          memberService
              .getAgentByAccount(username)
              .orElseThrow(() -> new ServiceException(username + "代理账号不存在!"));
      if (CollectionUtil.isNotEmpty(accountBlacks)) {
        if (accountBlacks.contains(username)) {
          log.info("业务===固定比例分红-账号==={}-存在业务黑名单,无法结算", username);
          return new ArrayList<>();
        }
      }
      if (CollectionUtil.isNotEmpty(userLevelBlacks)) {
        if (userLevelBlacks.contains(currentMember.getAgentLevel())) {
          log.info("业务===固定比例分红-账号==={}-存在业务黑名单,无法结算", username);
          return new ArrayList<>();
        }
      }

      Integer curLevelNum = currentMember.getAgentLevel(); // 享分红代理
      Integer subLevelNum = userLevelNum - curLevelNum; // 等级差
      if (subLevelNum < NumberConstant.ZERO) {
        continue;
      }
      if (subLevelNum == 0) {
        subLevelNum = 1;
      }

      if (CollectionUtil.isEmpty(fissionConfigLevelVos)
          || (subLevelNum > fissionConfigLevelVos.size())) {
        divideLevelRatio = outRecycleConfig.divide(new BigDecimal("100"));
      } else {
        // 当前代理的
        List<FissionConfigLevelVo> tmpLevelVo = levelDivideRatioMap.get(subLevelNum);
        if (CollectionUtil.isEmpty(tmpLevelVo)) {
          if (outRecycleConfig.compareTo(BigDecimal.ZERO) <= NumberConstant.ZERO) {
            continue;
          }
          divideLevelRatio = outRecycleConfig.divide(new BigDecimal("100"));
        } else {
          FissionConfigLevelVo fissionConfigLevelVo = tmpLevelVo.get(0);
          if (fissionConfigLevelVo.getLevelDivideRatio().compareTo(BigDecimal.ZERO)
              <= NumberConstant.ZERO) {
            continue;
          }
          divideLevelRatio =
              fissionConfigLevelVo.getLevelDivideRatio().divide(new BigDecimal("100"));
        }
      }

      if (divideLevelRatio.compareTo(BigDecimal.ZERO) <= NumberConstant.ZERO) {
        continue;
      }

      DivideDetailDto saveDetailDto =
          DivideDetailDto.builder()
              .periodsId(periodsId)
              .userId(gameReportVO.getMemberId()) // 分红来源用户Id
              .userName(gameReportVO.getAccount()) // 分红来源用户名称
              .agentLevel(gameReportVO.getAgentLevel()) // 分红源的级别
              .superPath(gameReportVO.getUserPaths()) // 分红源的代理路径
              .userType(gameReportVO.getUserType()) // 分红源的用户类型
              .proxyId(currentMember.getId()) // 分红代理的ID
              .proxyName(currentMember.getAccount()) // 分红代理的名称
              .superId(currentMember.getParentId()) // 分红代理的上级
              .superName(currentMember.getParentName()) // 分红代理的上级
              .proxyAgentLevel(currentMember.getAgentLevel())
              .proxyAgentPath(currentMember.getSuperPath())
              .liveCode(gameReportVO.getGameType()) // 游戏大类编码
              .code(gameReportVO.getGameKind()) // 一级游戏编码
              .validAmount(gameReportVO.getValidAmount()) // 有效投注
              .winAmount(gameReportVO.getWinAmount().negate()) // 输赢金额 游戏报表中的输赢金额是会员的角度 这里需要取反
              .settleType(settleType)
              .amountRatio(amountRatio)
              .divideRatio(divideLevelRatio)
              .build();

      BigDecimal baseAmount =
          settleType == NumberConstant.ONE
              ? gameReportVO.getWinAmount().negate()
              : gameReportVO.getValidAmount();

      // 分红金额
      saveDetailDto.setDivideAmount(baseAmount.multiply(amountRatio).multiply(divideLevelRatio));

      // 分红公式
      saveDetailDto.setDivideFormula(
          baseAmount.toString()
              + "x"
              + amountRatio.multiply(BigDecimal.valueOf(100)).toString()
              + "%x"
              + divideLevelRatio.multiply(BigDecimal.valueOf(100)).toString()
              + "%");

      // 添加分红详情
      DivideDetail detail = detailConvert.toEntity(saveDetailDto);
      int insertCount = detailMapper.insert(detail);
      if (insertCount > NumberConstant.ZERO) {
        returnList.add(saveDetailDto);
      }
    }

    return returnList;
  }

  /**
   * 层层代模式结算
   *
   * @param periodsId
   * @param userPaths
   * @param gameReportVO
   * @param accountBlacks
   * @param userLevelBlacks
   * @return
   */
  public List<DivideDetailDto> layerSettle(
      Long periodsId,
      String userPaths,
      DivideGameReportVO gameReportVO,
      List<String> accountBlacks,
      List<String> userLevelBlacks) {
    List<DivideDetailDto> returnList = new ArrayList<>();
    String[] nameStr = userPaths.split("/");
    if (ArrayUtil.isEmpty(nameStr)) {
      return new ArrayList<>();
    }
    for (int i = 0; i < nameStr.length; i++) {
      // 过滤黑名单
      String username = nameStr[i];
      // 顶级代理
      Member currentMember =
          memberService
              .getAgentByAccount(username)
              .orElseThrow(() -> new ServiceException(username + "代理账号不存在!"));
      if (CollectionUtil.isNotEmpty(accountBlacks)) {
        if (accountBlacks.contains(username)) {
          log.info("业务===固定比例分红-账号==={}-存在业务黑名单,无法结算", username);
          return new ArrayList<>();
        }
      }
      if (CollectionUtil.isNotEmpty(userLevelBlacks)) {
        if (userLevelBlacks.contains(currentMember.getAgentLevel())) {
          log.info("业务===固定比例分红-账号==={}-存在业务黑名单,无法结算", username);
          return new ArrayList<>();
        }
      }
      Integer settleType = NumberConstant.ZERO;
      BigDecimal amountRatio = BigDecimal.ZERO;
      BigDecimal divideRatio = BigDecimal.ZERO;
      GameDivideVo gameDivideVo = new GameDivideVo();
      // 最后一个 直接取自己的配置
      if (i == (nameStr.length - 1)) {
        gameDivideVo =
            layerConfigService.getConfigByGameCode(nameStr[i], gameReportVO.getGameKind());
        if (BeanUtil.isEmpty(gameDivideVo)) {
          continue;
        }
        if (gameDivideVo.getDivideRatio().compareTo(BigDecimal.ZERO) <= NumberConstant.ZERO) {
          continue;
        }
        divideRatio = gameDivideVo.getDivideRatio().divide(new BigDecimal("100"));
      } else { // 取这条线 它直接下级的配置
        gameDivideVo =
            layerConfigService.getConfigByGameCode(nameStr[i + 1], gameReportVO.getGameKind());
        if (BeanUtil.isEmpty(gameDivideVo)) {
          continue;
        }
        if (gameDivideVo.getParentDivideRatio().compareTo(BigDecimal.ZERO) <= NumberConstant.ZERO) {
          continue;
        }
        divideRatio = gameDivideVo.getParentDivideRatio().divide(new BigDecimal("100"));
      }

      if (gameDivideVo.getAmountRatio().compareTo(BigDecimal.ZERO) <= NumberConstant.ZERO) {
        continue;
      }
      amountRatio = gameDivideVo.getAmountRatio().divide(new BigDecimal("100"));
      // 结算方式 1 输赢  2  投注额
      settleType = gameDivideVo.getSettleType();

      DivideDetailDto saveDetailDto =
          DivideDetailDto.builder()
              .periodsId(periodsId)
              .userId(gameReportVO.getMemberId()) // 分红来源用户Id
              .userName(gameReportVO.getAccount()) // 分红来源用户名称
              .agentLevel(gameReportVO.getAgentLevel()) // 分红源的级别
              .superPath(gameReportVO.getUserPaths()) // 分红源的代理路径
              .userType(gameReportVO.getUserType()) // 分红源的用户类型
              .proxyId(currentMember.getId()) // 分红代理的ID
              .proxyName(currentMember.getAccount()) // 分红代理的名称
              .superId(currentMember.getParentId()) // 分红代理的上级
              .superName(currentMember.getParentName()) // 分红代理的上级
              .proxyAgentLevel(currentMember.getAgentLevel())
              .proxyAgentPath(currentMember.getSuperPath())
              .liveCode(gameReportVO.getGameType()) // 游戏大类编码
              .code(gameReportVO.getGameKind()) // 一级游戏编码
              .validAmount(gameReportVO.getValidAmount()) // 有效投注
              .winAmount(gameReportVO.getWinAmount().negate()) // 输赢金额 游戏报表中的输赢金额是会员的角度 这里需要取反
              .settleType(settleType)
              .amountRatio(amountRatio)
              .divideRatio(divideRatio)
              .build();
      // 基础金额
      BigDecimal baseAmount =
          settleType == NumberConstant.ONE
              ? gameReportVO.getWinAmount().negate()
              : gameReportVO.getValidAmount();
      // 分红金额
      saveDetailDto.setDivideAmount(baseAmount.multiply(amountRatio).multiply(divideRatio));
      // 分红公式
      saveDetailDto.setDivideFormula(
          baseAmount.toString()
              + "x"
              + amountRatio.multiply(BigDecimal.valueOf(100)).toString()
              + "%x"
              + divideRatio.multiply(BigDecimal.valueOf(100)).toString()
              + "%");

      // 添加分红详情
      DivideDetail detail = detailConvert.toEntity(saveDetailDto);
      int insertCount = detailMapper.insert(detail);
      if (insertCount > NumberConstant.ZERO) {
        returnList.add(saveDetailDto);
      }
    }

    return returnList;
  }

  /**
   * 汇总数据派发
   *
   * @param divideSummaries
   */
  public void summaryGrant(
      DividePeriods dividePeriods,
      List<DivideSummary> divideSummaries,
      UserCredential userCredential) {
    for (DivideSummary summary : divideSummaries) {
      DivideSummaryDto editDto =
          DivideSummaryDto.builder()
              .id(summary.getId())
              .status(DivideStatusEnum.SUMMARY_STATUS_GRANTED.getValue())
              .build();
      DivideSummary editObj = summaryConvert.toEntity(editDto);
      int i = summaryMapper.updateById(editObj);
      if (i < NumberConstant.ONE) {
        continue;
      }
      if (summary.getAccount().equalsIgnoreCase(SystemConstant.DEFAULT_WEB_ROOT)
          || summary.getAccount().equalsIgnoreCase(SystemConstant.DEFAULT_TEST_ROOT)
          || summary.getAccount().equalsIgnoreCase(SystemConstant.DEFAULT_WAP_ROOT)) {
        continue;
      }
      // 如果 真实分红金额 小于等于0 不用发生账变 资金变动
      if (summary.getRealDivideAmount().compareTo(BigDecimal.ZERO) <= NumberConstant.ZERO) {
        continue;
      }

      // 校验会员账户状态
      Member member = memberService.getById(summary.getUserId());
      if (BeanUtil.isEmpty(member)) {
        continue;
      }
      MemberInfo memberInfo =
          memberInfoService.lambdaQuery().eq(MemberInfo::getMemberId, summary.getUserId()).one();
      if (BeanUtil.isEmpty(memberInfo)) {
        continue;
      }
      if (!MemberEnums.Status.ENABlED.match(member.getStatus())) {
        log.error(
            "--account:{},userId:{},state:{}--",
            member.getAccount(),
            member.getId(),
            member.getStatus());
        continue;
      }
      try {
        this.financialGrant(member, memberInfo, dividePeriods, summary, userCredential);
      } catch (Exception e) {
        log.error("会员{}分红期数{}派发资金处理失败！", member.getAccount(), dividePeriods.getId());
      }
    }
  }

  public void summaryRecycle(
      DividePeriods dividePeriods,
      List<DivideSummary> divideSummaries,
      UserCredential userCredential) {
    for (DivideSummary summary : divideSummaries) {
      DivideSummaryDto editDto =
          DivideSummaryDto.builder()
              .id(summary.getId())
              .status(DivideStatusEnum.SUMMARY_STATUS_RECYCLE.getValue())
              .build();
      DivideSummary editObj = summaryConvert.toEntity(editDto);
      int i = summaryMapper.updateById(editObj);
      if (i < NumberConstant.ONE) {
        continue;
      }
      if (summary.getAccount().equalsIgnoreCase(SystemConstant.DEFAULT_WEB_ROOT)
          || summary.getAccount().equalsIgnoreCase(SystemConstant.DEFAULT_TEST_ROOT)
          || summary.getAccount().equalsIgnoreCase(SystemConstant.DEFAULT_WAP_ROOT)) {
        continue;
      }
      // 如果 真实分红金额 小于等于0 不用发生账变 资金变动
      if (summary.getRealDivideAmount().compareTo(BigDecimal.ZERO) <= NumberConstant.ZERO) {
        continue;
      }
      // 校验会员账户状态
      Member member = memberService.getById(summary.getUserId());
      if (BeanUtil.isEmpty(member)) {
        continue;
      }
      MemberInfo memberInfo =
          memberInfoService.lambdaQuery().eq(MemberInfo::getMemberId, summary.getUserId()).one();
      if (BeanUtil.isEmpty(memberInfo)) {
        continue;
      }

      try {
        this.financialRecycle(member, memberInfo, dividePeriods, summary, userCredential);
      } catch (Exception e) {
        log.error("会员{}分红期数{}回收资金处理失败！", member.getAccount(), dividePeriods.getId());
        continue;
      }
    }
  }

  public void financialGrant(
      Member member,
      MemberInfo memberInfo,
      DividePeriods dividePeriods,
      DivideSummary summary,
      UserCredential userCredential) {
    String sb =
        new StringBuilder()
            .append(dividePeriods.getStartDate())
            .append("~")
            .append(dividePeriods.getEndDate())
            .append("期分红资金派发 ")
            .append(summary.getRealDivideAmount())
            .append("元")
            .toString();

    // 账户资金锁
    String lockKey =
        MessageFormat.format(MemberServiceKeyConstant.MEMBER_FINANCIAL_LOCK, member.getAccount());
    try {
      // 获取资金锁（等待6秒，租期120秒）
      boolean flag = distributedLocker.tryLock(lockKey, TimeUnit.SECONDS, 6, 120);
      // 6秒获取不到资金锁
      if (!flag) {
        return;
      }
      // 添加流水记录
      MemberBill memberBill = new MemberBill();
      memberBill.setMemberId(member.getId());
      memberBill.setAccount(member.getAccount());
      memberBill.setMemberPath(member.getSuperPath());
      memberBill.setTranType(TranTypes.DIVIDE_AMOUNT.getValue());
      memberBill.setOrderNo(String.valueOf(IdGeneratorSnowflake.getInstance().nextId()));
      memberBill.setAmount(summary.getRealDivideAmount());
      memberBill.setBalance(memberInfo.getBalance());
      memberBill.setOperator(userCredential.getUsername());
      memberBill.setRemark(sb);
      memberBill.setContent(sb);
      memberBillService.save(memberBill);
      memberInfoService.updateBalance(member.getId(), summary.getRealDivideAmount());
    } catch (Exception e) {
      log.error(MessageFormat.format("会员{}分红账变, 失败原因：{}", member.getAccount(), e));
      // 释放资金锁
      distributedLocker.unlock(lockKey);
    } finally {
      // 释放资金锁
      distributedLocker.unlock(lockKey);
    }
    // 6.5 通知 发个人消息
    MessageInfoAddDTO dto = new MessageInfoAddDTO();
    Message message = messageInfoConvert.toEntity(dto);
    message.setTitle("层层代分红派发");
    message.setContent(sb);
    message.setCategory(PushMessageEnum.MessageCategory.SYS_SEND.getValue());
    message.setPosition(PushMessageEnum.MessageCategory.CATE_DEF.getValue());
    message.setShowType(PushMessageEnum.MessageShowType.SHOW_DEF.value());
    message.setPopsCount(PushMessageEnum.PopCount.POP_COUNT_DEF.getValue());
    message.setPushRange(PushMessageEnum.UserRange.SOME_MEMBERS.getValue());
    message.setLinkAccount(member.getAccount());
    message.setType(NumberConstant.ONE);
    message.setStatus(NumberConstant.ONE);
    message.setCreateBy(userCredential.getUsername());
    messageMapper.saveReturnId(message);

    MessageDistribute messageDistribute = new MessageDistribute();
    messageDistribute.setMessageId(message.getId());
    messageDistribute.setUserId(member.getId());
    messageDistribute.setUserAccount(member.getAccount());
    messageDistribute.setRechargeLevel(member.getUserLevel());
    messageDistribute.setVipLevel(
        memberInfoService
            .lambdaQuery()
            .eq(MemberInfo::getMemberId, member.getId())
            .one()
            .getVipLevel());
    messageDistribute.setReadStatus(NumberConstant.ZERO);
    messageDistribute.setCreateBy(userCredential.getUsername());
    messageDistributeService.save(messageDistribute);
  }

  public void financialRecycle(
      Member member,
      MemberInfo memberInfo,
      DividePeriods dividePeriods,
      DivideSummary summary,
      UserCredential userCredential) {
    String sb =
        dividePeriods.getStartDate()
            + "~"
            + dividePeriods.getEndDate()
            + "期分红资金回收 "
            + summary.getRealDivideAmount().negate()
            + "元";

    // 账户资金锁
    String lockKey =
        MessageFormat.format(MemberServiceKeyConstant.MEMBER_FINANCIAL_LOCK, member.getAccount());

    try {
      // 获取资金锁（等待8秒，租期120秒）
      boolean flag = distributedLocker.tryLock(lockKey, TimeUnit.SECONDS, 8, 120);
      if (!flag) {
        return;
      }
      // 添加流水记录
      MemberBill memberBill = new MemberBill();
      memberBill.setMemberId(member.getId());
      memberBill.setAccount(member.getAccount());
      memberBill.setMemberPath(member.getSuperPath());
      memberBill.setTranType(TranTypes.DIVIDE_AMOUNT_BACK.getValue());
      memberBill.setOrderNo(String.valueOf(IdGeneratorSnowflake.getInstance().nextId()));
      memberBill.setAmount(summary.getRealDivideAmount().negate());
      memberBill.setBalance(memberInfo.getBalance());
      memberBill.setRemark(sb);
      memberBill.setContent(sb);
      memberBill.setOperator(userCredential.getUsername());
      memberBillService.save(memberBill);
    } catch (Exception e) {
      log.error(MessageFormat.format("会员：{}，期数回收, 失败原因：{}", member.getAccount(), e));
      // 释放资金锁
      distributedLocker.unlock(lockKey);
    } finally {
      // 释放资金锁
      distributedLocker.unlock(lockKey);
    }

    // 计算变更后余额
    BigDecimal newBalance = memberInfo.getBalance().add(summary.getRealDivideAmount().negate());
    if (newBalance.compareTo(BigDecimal.ZERO) < NumberConstant.ZERO) {
      return;
    }
    MemberInfo entity =
        MemberInfo.builder()
            .memberId(member.getId())
            .balance(newBalance)
            .version(memberInfo.getVersion())
            .build();
    boolean b = memberInfoService.updateById(entity);
    if (!b) {
      log.error("会员{}期数回收钱包余额变更失败！", member.getAccount());
    }
  }

  /**
   * 期数结算时 保存分红汇总
   *
   * @param groupByPname
   * @param periodsId
   * @param isGrand
   */
  public void saveSummaryForSettle(
      Map<String, List<DivideDetailDto>> groupByPname, Long periodsId, Integer isGrand) {
    for (Map.Entry<String, List<DivideDetailDto>> map : groupByPname.entrySet()) {
      List<DivideDetailDto> groupList = map.getValue();
      DivideDetailDto divideDetail = groupList.get(0);
      DivideSummaryDto summaryDto =
          DivideSummaryDto.builder()
              .periodsId(periodsId)
              .userId(divideDetail.getProxyId())
              .account(divideDetail.getProxyName())
              .agentLevel(divideDetail.getProxyAgentLevel())
              .agentPath(divideDetail.getProxyAgentPath())
              .parentId(divideDetail.getSuperId())
              .parentName(divideDetail.getSuperName())
              .status(TrueFalse.TRUE.getValue())
              .build();
      // 将有效投注求和
      BigDecimal sumValidAmount =
          groupList.stream().map(DivideDetailDto::getValidAmount).reduce(BigDecimal::add).get();
      // 将输赢金额求和
      BigDecimal sumWinAmount =
          groupList.stream().map(DivideDetailDto::getWinAmount).reduce(BigDecimal::add).get();
      // 将分红金额求和
      BigDecimal sumDivideAmount =
          groupList.stream().map(DivideDetailDto::getDivideAmount).reduce(BigDecimal::add).get();
      summaryDto.setValidAmount(sumValidAmount);
      summaryDto.setWinAmount(sumWinAmount);
      // 如果开启了  负数分红金额累计至下一期则 需要取到上一期的真实分红金额
      if (isGrand == EnableEnum.ENABLED.code()) {
        // 上期累计金额  如果上期的汇总分红金额为负数则填充
        // 获取此用户上期分红汇总
        DivideSummary lastSummary = summaryMapper.getByUserName(divideDetail.getProxyName());
        if (BeanUtil.isEmpty(lastSummary)) {
          summaryDto.setLastPeriodsAmount(BigDecimal.ZERO);
          summaryDto.setDivideAmount(sumDivideAmount);
          summaryDto.setRealDivideAmount(sumDivideAmount);
        } else {
          // 上期真实分红金额
          BigDecimal lastRealDivideAmount = lastSummary.getRealDivideAmount();
          summaryDto.setLastPeriodsAmount(lastRealDivideAmount);
          summaryDto.setDivideAmount(sumDivideAmount);
          // 如果是正数
          if (lastRealDivideAmount.compareTo(BigDecimal.ZERO) >= NumberConstant.ZERO) {
            summaryDto.setRealDivideAmount(sumDivideAmount);
          } else {
            summaryDto.setRealDivideAmount(sumDivideAmount.add(summaryDto.getLastPeriodsAmount()));
          }
        }
      } else {
        summaryDto.setLastPeriodsAmount(BigDecimal.ZERO);
        summaryDto.setDivideAmount(sumDivideAmount);
        summaryDto.setRealDivideAmount(sumDivideAmount);
      }
      DivideSummary divideSummary = summaryConvert.toEntity(summaryDto);
      int insertSummaryCount = summaryMapper.insert(divideSummary);
      if (insertSummaryCount < NumberConstant.ONE) {
        log.info(divideSummary.getAccount() + "保存分红汇总失败！");
      }
    }
  }

  /**
   * 去掉代理路径首尾的/
   *
   * @param oldPath
   * @return
   */
  public String getFinalAgentPath(String oldPath) {
    // 把开头的/去掉
    oldPath = oldPath.startsWith("/") ? oldPath.substring(1) : oldPath;
    // 把结尾的/去掉
    if (oldPath.endsWith("/")) {
      int j = oldPath.lastIndexOf("/");
      oldPath = j >= 0 ? oldPath.substring(0, j) : oldPath;
    }
    return oldPath;
  }
}
