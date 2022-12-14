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

//  @Autowired private MessageDistributeService messageDistributeService;

  @Autowired private RedissonClient redissonClient;

  /**
   * ????????????
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
   * ??????
   *
   * @param dto
   */
  @Override
  @SentinelResource("add")
  public void add(DividePeriodsDTO dto) {
    Assert.isTrue(StrUtil.isNotBlank(dto.getStartDate()), "?????????????????????????????????");
    Assert.isTrue(StrUtil.isNotBlank(dto.getEndDate()), "?????????????????????????????????");
    DividePeriods saveObj = periodsConvert.toEntity(dto);
    saveObj.setStartDate(DateUtil.parse(saveObj.getStartDate()).toDateStr());
    saveObj.setEndDate(DateUtil.parse(saveObj.getEndDate()).toDateStr());
    AgentConfig agentConfig = agentConfigService.getAgentConfig();
    Integer divideModel = agentConfig.getDivideModel();
    divideModel = ObjectUtil.defaultIfNull(divideModel, DivideStatusEnum.DIVIDE_MODEL_LAYER);
    saveObj.setDivideType(divideModel);
    Assert.isTrue(this.save(saveObj), "???????????????");
  }

  @Override
  @SentinelResource("edit")
  public void edit(DividePeriodsDTO dto) {
    Assert.isTrue(StrUtil.isNotBlank(dto.getStartDate()), "?????????????????????????????????");
    Assert.isTrue(StrUtil.isNotBlank(dto.getEndDate()), "?????????????????????????????????");
    DividePeriods editObj = periodsConvert.toEntity(dto);
    editObj.setStartDate(DateUtil.parse(dto.getStartDate()).toDateStr());
    editObj.setEndDate(DateUtil.parse(dto.getEndDate()).toDateStr());
    Assert.isTrue(this.updateById(editObj), "???????????????");
  }

  @Override
  @SentinelResource("delete")
  public void delete(String ids) {
    Assert.isTrue(StrUtil.isNotBlank(ids), "???????????????");
    String[] idArr = ids.split(",");
    for (String id : idArr) {
      DividePeriods dividePeriods = dividePeriodsMapper.selectById(id);
      if (dividePeriods.getGrantStatus()
          == DivideStatusEnum.PERIODS_SETTLE_STATUS_SETTLEED.getValue()) {
        throw new ServiceException("????????????????????????");
      }
      QueryWrapper<DivideDetail> deleteDetailWrapper = new QueryWrapper<>();
      deleteDetailWrapper.eq("periods_id", Long.valueOf(id));
      detailMapper.delete(deleteDetailWrapper);

      QueryWrapper<DivideSummary> deleteSummaryWrapper = new QueryWrapper<>();
      deleteSummaryWrapper.eq("periods_id", Long.valueOf(id));
      summaryMapper.delete(deleteSummaryWrapper);

      Assert.isTrue(this.removeById(id), ("????????????:" + id + "?????????"));
    }
  }

  @Override
  @SentinelResource(value = "settle")
  public void settle(DividePeriodsDTO dto) {
    Assert.isTrue(dto.getId() != null, "????????????ID???????????????");
    // ???????????????  ??????????????????
    String key = "divide:settle:" + dto.getId();
    Boolean isLock = redisTemplate.hasKey(key);
    Assert.isTrue(!isLock, "?????????????????????????????????????????????");
    distributedLocker.lock(key);
    try {
      // ?????? ???????????????????????????
      this.settleOther(dto.getId());

      Assert.isTrue(
          this.lambdaUpdate()
              .set(
                  DividePeriods::getSettleStatus,
                  DivideStatusEnum.PERIODS_SETTLE_STATUS_SETTLEED.getValue())
              .eq(DividePeriods::getId, dto.getId())
              .update(new DividePeriods()),
          "?????????????????????????????????");
    } catch (Exception e) {
      throw new ServiceException("?????????????????????");
    } finally {
      distributedLocker.unlock(key);
    }
  }

  @Override
  @SentinelResource(value = "grant")
  public void grant(DividePeriodsDTO dto) {
    UserCredential userCredential = SecurityUserHolder.getCredential();
    // ???????????????????????????
    Assert.isTrue(dto.getId() != null, "????????????ID???????????????");
    DividePeriods dividePeriods = dividePeriodsMapper.selectById(dto.getId());
    Assert.isTrue(
        dividePeriods.getSettleStatus()
            == DivideStatusEnum.PERIODS_SETTLE_STATUS_SETTLEED.getValue(),
        "???????????????");
    Assert.isTrue(
        dividePeriods.getGrantStatus() == DivideStatusEnum.PERIODS_GRANT_STATUS_UNGRANT.getValue(),
        "???????????????????????????");
    // ???????????????  ??????????????????
    String key = "divide:grant:" + dto.getId();
    RLock lock = redissonClient.getLock(key);
    if (lock.isLocked()) {
      return;
    }
    distributedLocker.lock(key);
    try {
      // ???????????????????????????????????????????????????
      QueryWrapper<DivideSummary> querySummaryWrapper = new QueryWrapper<>();
      querySummaryWrapper.eq("periods_id", dto.getId());
      querySummaryWrapper.eq("status", DivideStatusEnum.SUMMARY_STATUS_SETTLEED.getValue());
      List<DivideSummary> divideSummaries = summaryMapper.selectList(querySummaryWrapper);
      if (CollectionUtil.isNotEmpty(divideSummaries)) {
        // ???????????????????????????
        this.summaryGrant(dividePeriods, divideSummaries, userCredential);
      }
      // ?????????????????????
      DividePeriodsDTO editDto =
          DividePeriodsDTO.builder()
              .id(dto.getId())
              .grantStatus(DivideStatusEnum.PERIODS_GRANT_STATUS_GRANTED.getValue())
              .build();
      DividePeriods editPo = periodsConvert.toEntity(editDto);
      int i = dividePeriodsMapper.updateById(editPo);
      Assert.isTrue(i > NumberConstant.ZERO, "???????????????");
    } catch (Exception e) {
      throw new ServiceException("?????????????????????");
    } finally {
      distributedLocker.unlock(key);
    }
  }

  @Override
  @SentinelResource(value = "recycle")
  public void recycle(DividePeriodsDTO dto) {
    UserCredential userCredential = SecurityUserHolder.getCredential();
    // ???????????????????????????
    Assert.isTrue(dto.getId() != null, "????????????ID???????????????");
    DividePeriods dividePeriods = dividePeriodsMapper.selectById(dto.getId());
    Assert.isTrue(
        dividePeriods.getGrantStatus() == DivideStatusEnum.PERIODS_GRANT_STATUS_GRANTED.getValue(),
        "???????????????????????????");
    // ???????????????  ??????????????????
    String key = "divide:grant:" + dto.getId();
    RLock lock = redissonClient.getLock(key);
    if (lock.isLocked()) {
      return;
    }
    distributedLocker.lock(key);
    try {
      // ????????????????????????????????????
      QueryWrapper<DivideSummary> querySummaryWrapper = new QueryWrapper<>();
      querySummaryWrapper.eq("periods_id", dto.getId());
      querySummaryWrapper.eq("status", DivideStatusEnum.SUMMARY_STATUS_GRANTED.getValue());
      List<DivideSummary> divideSummaries = summaryMapper.selectList(querySummaryWrapper);
      if (CollectionUtil.isNotEmpty(divideSummaries)) {
        // ???????????????????????????
        this.summaryRecycle(dividePeriods, divideSummaries, userCredential);
      }
      // ?????????????????????
      DividePeriodsDTO editDto =
          DividePeriodsDTO.builder()
              .id(dto.getId())
              .grantStatus(DivideStatusEnum.PERIODS_GRANT_STATUS_RECYCLE.getValue())
              .build();
      DividePeriods editPo = periodsConvert.toEntity(editDto);
      int i = dividePeriodsMapper.updateById(editPo);
      Assert.isTrue(i > NumberConstant.ZERO, "?????????????????????");
    } catch (Exception e) {
      throw new ServiceException("??????????????????!");
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
    // ??????????????????
    QueryWrapper<DivideDetail> deleteDetailWrapper = new QueryWrapper<>();
    deleteDetailWrapper.eq("periods_id", periodsId);
    detailMapper.delete(deleteDetailWrapper);
    // ??????????????????
    QueryWrapper<DivideSummary> deleteSummaryWrapper = new QueryWrapper<>();
    deleteSummaryWrapper.eq("periods_id", periodsId);
    summaryMapper.delete(deleteSummaryWrapper);

    // ???????????? 1 ??????  2 ??????  3 ????????? 4 ??????
    Integer divideModel = periods.getDivideType();
    AgentConfig agentConfig = agentConfigService.getAgentConfig();
    Integer isIncludeAgent = agentConfig.getIsIncludeAgent();

    // ????????????????????????????????????
    List<DivideGameReportVO> periodsGameReport =
        betDailyReportMapper.findReportForDivide(
            periods.getStartDate(), periods.getEndDate(), isIncludeAgent);
    // ????????????????????????????????????
    periodsGameReport =
        periodsGameReport.stream()
            .filter(item -> StrUtil.isNotBlank(item.getUserPaths()))
            .collect(Collectors.toList());
    // ???????????? ??????????????? ??????
    QueryWrapper<BizBlacklist> queryBizWrapper = new QueryWrapper<>();
    queryBizWrapper.like("types", BlacklistConstant.BizBlacklistType.DL_RATIO.getValue());
    List<BizBlacklist> bizBlacklists = blacklistMapper.selectList(queryBizWrapper);
    // ???????????????????????????
    List<BizBlacklist> accountBlackList =
        bizBlacklists.stream()
            .filter(item -> item.getTargetType() == TrueFalse.FALSE.getValue())
            .collect(Collectors.toList());
    // ???????????????????????????
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
        String userType = gameReportVO.getUserType(); // M ??????  A ??????
        String account = gameReportVO.getAccount();
        Integer agentLevel = gameReportVO.getAgentLevel();
        String userPaths = gameReportVO.getUserPaths();
        if (StrUtil.isBlank(userPaths)) {
          continue;
        }
        userPaths = getFinalAgentPath(userPaths);
        // ?????????????????????
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
        if (divideModel == DivideStatusEnum.DIVIDE_MODEL_FIX) { // ????????????
          detailList.addAll(
              fixSettle(periodsId, userPaths, gameReportVO, accountBlacks, userLevelBlacks));
        } else if (divideModel == DivideStatusEnum.DIVIDE_MODEL_FISSION) { // ??????
          detailList.addAll(
              fissionSettle(periodsId, userPaths, gameReportVO, accountBlacks, userLevelBlacks));
        } else if (divideModel == DivideStatusEnum.DIVIDE_MODEL_LAYER) { // ?????????
          detailList.addAll(
              layerSettle(periodsId, userPaths, gameReportVO, accountBlacks, userLevelBlacks));
        }
      }

      // ?????? ??????????????????
      if (CollectionUtil.isNotEmpty(detailList)) {
        // ???proxyId ????????????id??????
        Map<String, List<DivideDetailDto>> groupByPname =
            detailList.stream().collect(Collectors.groupingBy(DivideDetailDto::getProxyName));
        // ??????  ????????????webRoot
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
   * ????????????????????????
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
    // ????????????
    Member superMember =
        memberService
            .getByAccount(tmpName)
            .orElseThrow(() -> new ServiceException(tmpName + "???????????????!"));
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
      // ???????????????
      String username = nameStr[i];
      // ????????????
      Member currentMember =
          memberService
              .getAgentByAccount(username)
              .orElseThrow(() -> new ServiceException(username + "?????????????????????!"));
      if (CollectionUtil.isNotEmpty(accountBlacks)) {
        if (accountBlacks.contains(username)) {
          log.info("??????===??????????????????-??????==={}-?????????????????????,????????????", username);
          return new ArrayList<>();
        }
      }
      if (CollectionUtil.isNotEmpty(userLevelBlacks)) {
        if (userLevelBlacks.contains(currentMember.getAgentLevel())) {
          log.info("??????===??????????????????-??????==={}-?????????????????????,????????????", username);
          return new ArrayList<>();
        }
      }
      // ?????????agentLevel??????????????????????????? ???????????????
      if (currentMember.getAgentLevel() > userLevelNum) {
        continue;
      }

      DivideDetailDto saveDetailDto =
          DivideDetailDto.builder()
              .periodsId(periodsId)
              .userId(gameReportVO.getMemberId()) // ??????????????????Id
              .userName(gameReportVO.getAccount()) // ????????????????????????
              .agentLevel(gameReportVO.getAgentLevel()) // ??????????????????
              .superPath(gameReportVO.getUserPaths()) // ????????????????????????
              .userType(gameReportVO.getUserType()) // ????????????????????????
              .proxyId(currentMember.getId()) // ???????????????ID
              .proxyName(currentMember.getAccount()) // ?????????????????????
              .superId(currentMember.getParentId()) // ?????????????????????
              .superName(currentMember.getParentName()) // ?????????????????????
              .proxyAgentLevel(currentMember.getAgentLevel())
              .proxyAgentPath(currentMember.getSuperPath())
              .liveCode(gameReportVO.getGameType())
              .code(gameReportVO.getGameKind())
              .validAmount(gameReportVO.getValidAmount())
              .winAmount(gameReportVO.getWinAmount().negate())
              .settleType(settleType)
              .amountRatio(amountRatio)
              .build();
      // ???????????????????????????????????? - ?????????????????????)??????
      Integer subLevel = userLevelNum - currentMember.getAgentLevel();
      if (subLevel < 0) {
        continue;
      }
      if (subLevel == 0) {
        subLevel = 1;
      }
      BigDecimal pow = divideRatio.pow((subLevel));
      saveDetailDto.setDivideRatio(pow); // ??????????????????
      // ??????????????????
      BigDecimal baseAmount =
          settleType == NumberConstant.ONE
              ? gameReportVO.getWinAmount().negate()
              : gameReportVO.getValidAmount();
      // ????????????
      saveDetailDto.setDivideAmount(baseAmount.multiply(amountRatio).multiply(pow));
      // ????????????
      saveDetailDto.setDivideFormula(
          baseAmount.toString()
              + "x"
              + amountRatio.multiply(BigDecimal.valueOf(100)).toString()
              + "%x"
              + divideRatio.multiply(BigDecimal.valueOf(100)).toString()
              + "%^"
              + (subLevel));
      // ??????????????????
      DivideDetail detail = detailConvert.toEntity(saveDetailDto);
      int insertCount = detailMapper.insert(detail);
      if (insertCount > NumberConstant.ZERO) {
        returnList.add(saveDetailDto);
      }
    }
    return returnList;
  }

  /**
   * ??????????????????
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
    // ????????????
    Member superMember =
        memberService
            .getByAccount(tmpName)
            .orElseThrow(() -> new ServiceException(tmpName + "???????????????!"));
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
      // ???????????????
      String username = nameStr[i];
      if (username.equalsIgnoreCase(gameReportVO.getAccount())) {
        log.info("?????????????????????:{}", gameReportVO.getAccount());
        continue;
      }
      // ????????????
      Member currentMember =
          memberService
              .getAgentByAccount(username)
              .orElseThrow(() -> new ServiceException(username + "?????????????????????!"));
      if (CollectionUtil.isNotEmpty(accountBlacks)) {
        if (accountBlacks.contains(username)) {
          log.info("??????===??????????????????-??????==={}-?????????????????????,????????????", username);
          return new ArrayList<>();
        }
      }
      if (CollectionUtil.isNotEmpty(userLevelBlacks)) {
        if (userLevelBlacks.contains(currentMember.getAgentLevel())) {
          log.info("??????===??????????????????-??????==={}-?????????????????????,????????????", username);
          return new ArrayList<>();
        }
      }

      Integer curLevelNum = currentMember.getAgentLevel(); // ???????????????
      Integer subLevelNum = userLevelNum - curLevelNum; // ?????????
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
        // ???????????????
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
              .userId(gameReportVO.getMemberId()) // ??????????????????Id
              .userName(gameReportVO.getAccount()) // ????????????????????????
              .agentLevel(gameReportVO.getAgentLevel()) // ??????????????????
              .superPath(gameReportVO.getUserPaths()) // ????????????????????????
              .userType(gameReportVO.getUserType()) // ????????????????????????
              .proxyId(currentMember.getId()) // ???????????????ID
              .proxyName(currentMember.getAccount()) // ?????????????????????
              .superId(currentMember.getParentId()) // ?????????????????????
              .superName(currentMember.getParentName()) // ?????????????????????
              .proxyAgentLevel(currentMember.getAgentLevel())
              .proxyAgentPath(currentMember.getSuperPath())
              .liveCode(gameReportVO.getGameType()) // ??????????????????
              .code(gameReportVO.getGameKind()) // ??????????????????
              .validAmount(gameReportVO.getValidAmount()) // ????????????
              .winAmount(gameReportVO.getWinAmount().negate()) // ???????????? ???????????????????????????????????????????????? ??????????????????
              .settleType(settleType)
              .amountRatio(amountRatio)
              .divideRatio(divideLevelRatio)
              .build();

      BigDecimal baseAmount =
          settleType == NumberConstant.ONE
              ? gameReportVO.getWinAmount().negate()
              : gameReportVO.getValidAmount();

      // ????????????
      saveDetailDto.setDivideAmount(baseAmount.multiply(amountRatio).multiply(divideLevelRatio));

      // ????????????
      saveDetailDto.setDivideFormula(
          baseAmount.toString()
              + "x"
              + amountRatio.multiply(BigDecimal.valueOf(100)).toString()
              + "%x"
              + divideLevelRatio.multiply(BigDecimal.valueOf(100)).toString()
              + "%");

      // ??????????????????
      DivideDetail detail = detailConvert.toEntity(saveDetailDto);
      int insertCount = detailMapper.insert(detail);
      if (insertCount > NumberConstant.ZERO) {
        returnList.add(saveDetailDto);
      }
    }

    return returnList;
  }

  /**
   * ?????????????????????
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
      // ???????????????
      String username = nameStr[i];
      // ????????????
      Member currentMember =
          memberService
              .getAgentByAccount(username)
              .orElseThrow(() -> new ServiceException(username + "?????????????????????!"));
      if (CollectionUtil.isNotEmpty(accountBlacks)) {
        if (accountBlacks.contains(username)) {
          log.info("??????===??????????????????-??????==={}-?????????????????????,????????????", username);
          return new ArrayList<>();
        }
      }
      if (CollectionUtil.isNotEmpty(userLevelBlacks)) {
        if (userLevelBlacks.contains(currentMember.getAgentLevel())) {
          log.info("??????===??????????????????-??????==={}-?????????????????????,????????????", username);
          return new ArrayList<>();
        }
      }
      Integer settleType = NumberConstant.ZERO;
      BigDecimal amountRatio = BigDecimal.ZERO;
      BigDecimal divideRatio = BigDecimal.ZERO;
      GameDivideVo gameDivideVo = new GameDivideVo();
      // ???????????? ????????????????????????
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
      } else { // ???????????? ????????????????????????
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
      // ???????????? 1 ??????  2  ?????????
      settleType = gameDivideVo.getSettleType();

      DivideDetailDto saveDetailDto =
          DivideDetailDto.builder()
              .periodsId(periodsId)
              .userId(gameReportVO.getMemberId()) // ??????????????????Id
              .userName(gameReportVO.getAccount()) // ????????????????????????
              .agentLevel(gameReportVO.getAgentLevel()) // ??????????????????
              .superPath(gameReportVO.getUserPaths()) // ????????????????????????
              .userType(gameReportVO.getUserType()) // ????????????????????????
              .proxyId(currentMember.getId()) // ???????????????ID
              .proxyName(currentMember.getAccount()) // ?????????????????????
              .superId(currentMember.getParentId()) // ?????????????????????
              .superName(currentMember.getParentName()) // ?????????????????????
              .proxyAgentLevel(currentMember.getAgentLevel())
              .proxyAgentPath(currentMember.getSuperPath())
              .liveCode(gameReportVO.getGameType()) // ??????????????????
              .code(gameReportVO.getGameKind()) // ??????????????????
              .validAmount(gameReportVO.getValidAmount()) // ????????????
              .winAmount(gameReportVO.getWinAmount().negate()) // ???????????? ???????????????????????????????????????????????? ??????????????????
              .settleType(settleType)
              .amountRatio(amountRatio)
              .divideRatio(divideRatio)
              .build();
      // ????????????
      BigDecimal baseAmount =
          settleType == NumberConstant.ONE
              ? gameReportVO.getWinAmount().negate()
              : gameReportVO.getValidAmount();
      // ????????????
      saveDetailDto.setDivideAmount(baseAmount.multiply(amountRatio).multiply(divideRatio));
      // ????????????
      saveDetailDto.setDivideFormula(
          baseAmount.toString()
              + "x"
              + amountRatio.multiply(BigDecimal.valueOf(100)).toString()
              + "%x"
              + divideRatio.multiply(BigDecimal.valueOf(100)).toString()
              + "%");

      // ??????????????????
      DivideDetail detail = detailConvert.toEntity(saveDetailDto);
      int insertCount = detailMapper.insert(detail);
      if (insertCount > NumberConstant.ZERO) {
        returnList.add(saveDetailDto);
      }
    }

    return returnList;
  }

  /**
   * ??????????????????
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
      // ?????? ?????????????????? ????????????0 ?????????????????? ????????????
      if (summary.getRealDivideAmount().compareTo(BigDecimal.ZERO) <= NumberConstant.ZERO) {
        continue;
      }

      // ????????????????????????
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
        log.error("??????{}????????????{}???????????????????????????", member.getAccount(), dividePeriods.getId());
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
      // ?????? ?????????????????? ????????????0 ?????????????????? ????????????
      if (summary.getRealDivideAmount().compareTo(BigDecimal.ZERO) <= NumberConstant.ZERO) {
        continue;
      }
      // ????????????????????????
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
        log.error("??????{}????????????{}???????????????????????????", member.getAccount(), dividePeriods.getId());
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
            .append("????????????????????? ")
            .append(summary.getRealDivideAmount())
            .append("???")
            .toString();

    // ???????????????
    String lockKey =
        MessageFormat.format(MemberServiceKeyConstant.MEMBER_FINANCIAL_LOCK, member.getAccount());
    try {
      // ????????????????????????6????????????120??????
      boolean flag = distributedLocker.tryLock(lockKey, TimeUnit.SECONDS, 6, 120);
      // 6????????????????????????
      if (!flag) {
        return;
      }
      // ??????????????????
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
      log.error(MessageFormat.format("??????{}????????????, ???????????????{}", member.getAccount(), e));
      // ???????????????
      distributedLocker.unlock(lockKey);
    } finally {
      // ???????????????
      distributedLocker.unlock(lockKey);
    }
    // 6.5 ?????? ???????????????
    MessageInfoAddDTO dto = new MessageInfoAddDTO();
    Message message = messageInfoConvert.toEntity(dto);
    message.setTitle("?????????????????????");
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

//    MessageDistribute messageDistribute = new MessageDistribute();
//    messageDistribute.setMessageId(message.getId());
//    messageDistribute.setUserId(member.getId());
//    messageDistribute.setUserAccount(member.getAccount());
//    messageDistribute.setRechargeLevel(member.getUserLevel());
//    messageDistribute.setVipLevel(
//        memberInfoService
//            .lambdaQuery()
//            .eq(MemberInfo::getMemberId, member.getId())
//            .one()
//            .getVipLevel());
//    messageDistribute.setReadStatus(NumberConstant.ZERO);
//    messageDistribute.setCreateBy(userCredential.getUsername());
//    messageDistributeService.save(messageDistribute);
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
            + "????????????????????? "
            + summary.getRealDivideAmount().negate()
            + "???";

    // ???????????????
    String lockKey =
        MessageFormat.format(MemberServiceKeyConstant.MEMBER_FINANCIAL_LOCK, member.getAccount());

    try {
      // ????????????????????????8????????????120??????
      boolean flag = distributedLocker.tryLock(lockKey, TimeUnit.SECONDS, 8, 120);
      if (!flag) {
        return;
      }
      // ??????????????????
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
      log.error(MessageFormat.format("?????????{}???????????????, ???????????????{}", member.getAccount(), e));
      // ???????????????
      distributedLocker.unlock(lockKey);
    } finally {
      // ???????????????
      distributedLocker.unlock(lockKey);
    }

    // ?????????????????????
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
      log.error("??????{}???????????????????????????????????????", member.getAccount());
    }
  }

  /**
   * ??????????????? ??????????????????
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
      // ?????????????????????
      BigDecimal sumValidAmount =
          groupList.stream().map(DivideDetailDto::getValidAmount).reduce(BigDecimal::add).get();
      // ?????????????????????
      BigDecimal sumWinAmount =
          groupList.stream().map(DivideDetailDto::getWinAmount).reduce(BigDecimal::add).get();
      // ?????????????????????
      BigDecimal sumDivideAmount =
          groupList.stream().map(DivideDetailDto::getDivideAmount).reduce(BigDecimal::add).get();
      summaryDto.setValidAmount(sumValidAmount);
      summaryDto.setWinAmount(sumWinAmount);
      // ???????????????  ??????????????????????????????????????? ??????????????????????????????????????????
      if (isGrand == EnableEnum.ENABLED.code()) {
        // ??????????????????  ???????????????????????????????????????????????????
        // ?????????????????????????????????
        DivideSummary lastSummary = summaryMapper.getByUserName(divideDetail.getProxyName());
        if (BeanUtil.isEmpty(lastSummary)) {
          summaryDto.setLastPeriodsAmount(BigDecimal.ZERO);
          summaryDto.setDivideAmount(sumDivideAmount);
          summaryDto.setRealDivideAmount(sumDivideAmount);
        } else {
          // ????????????????????????
          BigDecimal lastRealDivideAmount = lastSummary.getRealDivideAmount();
          summaryDto.setLastPeriodsAmount(lastRealDivideAmount);
          summaryDto.setDivideAmount(sumDivideAmount);
          // ???????????????
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
        log.info(divideSummary.getAccount() + "???????????????????????????");
      }
    }
  }

  /**
   * ???????????????????????????/
   *
   * @param oldPath
   * @return
   */
  public String getFinalAgentPath(String oldPath) {
    // ????????????/??????
    oldPath = oldPath.startsWith("/") ? oldPath.substring(1) : oldPath;
    // ????????????/??????
    if (oldPath.endsWith("/")) {
      int j = oldPath.lastIndexOf("/");
      oldPath = j >= 0 ? oldPath.substring(0, j) : oldPath;
    }
    return oldPath;
  }
}
