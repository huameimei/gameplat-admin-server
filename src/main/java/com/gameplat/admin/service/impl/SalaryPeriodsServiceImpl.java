package com.gameplat.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.constant.MemberServiceKeyConstant;
import com.gameplat.admin.constant.SystemConstant;
import com.gameplat.admin.constant.TrueFalse;
import com.gameplat.admin.convert.MessageInfoConvert;
import com.gameplat.admin.convert.SalaryGrantConvert;
import com.gameplat.admin.convert.SalaryPeriodsConvert;
import com.gameplat.admin.enums.BlacklistConstant;
import com.gameplat.admin.enums.DivideStatusEnum;
import com.gameplat.admin.enums.PushMessageEnum;
import com.gameplat.admin.mapper.*;
import com.gameplat.admin.model.dto.MessageInfoAddDTO;
import com.gameplat.admin.model.dto.SalaryGrantDTO;
import com.gameplat.admin.model.dto.SalaryPeriodsDTO;
import com.gameplat.admin.model.vo.SalaryPeriodsVO;
import com.gameplat.admin.model.vo.SalaryRechargeVO;
import com.gameplat.admin.service.*;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.snowflake.IdGeneratorSnowflake;
import com.gameplat.common.enums.MemberEnums;
import com.gameplat.common.enums.TranTypes;
import com.gameplat.common.model.bean.AgentConfig;
import com.gameplat.model.entity.blacklist.BizBlacklist;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.member.MemberBill;
import com.gameplat.model.entity.member.MemberInfo;
import com.gameplat.model.entity.message.Message;
import com.gameplat.model.entity.proxy.SalaryConfig;
import com.gameplat.model.entity.proxy.SalaryGrant;
import com.gameplat.model.entity.proxy.SalaryPeriods;
import com.gameplat.model.entity.sys.SysDictData;
import com.gameplat.redis.redisson.DistributedLocker;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.context.UserCredential;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
@Slf4j
public class SalaryPeriodsServiceImpl extends ServiceImpl<SalaryPeriodsMapper, SalaryPeriods>
    implements SalaryPeriodsService {

  @Autowired private SalaryPeriodsMapper salaryPeriodsMapper;

  @Autowired private SalaryPeriodsConvert salaryPeriodsConvert;

  @Autowired private SysDictDataMapper sysDictDataMapper;

  @Autowired private SalaryGrantMapper salaryGrantMapper;

  @Autowired private StringRedisTemplate redisTemplate;

  @Autowired private DistributedLocker distributedLocker;

  @Autowired private MemberService memberService;

  @Autowired private AgentConfigService agentConfigService;

  @Autowired private SalaryConfigMapper salaryConfigMapper;

  @Autowired private BizBlacklistMapper blacklistMapper;

  @Autowired private RechargeOrderMapper rechargeOrderMapper;

  @Autowired private GameBetDailyReportMapper dailyReportMapper;

  @Autowired private SalaryGrantConvert salaryGrantConvert;

  @Autowired private MemberInfoService memberInfoService;

  @Autowired private MemberBillService memberBillService;

  @Autowired private MessageInfoConvert messageInfoConvert;

  @Autowired private MessageMapper messageMapper;

//  @Autowired private MessageDistributeService messageDistributeService;

  /**
   * ????????????
   *
   * @param page
   * @param dto
   * @return
   */
  @Override
  public IPage<SalaryPeriodsVO> queryPage(PageDTO<SalaryPeriods> page, SalaryPeriodsDTO dto) {
    QueryWrapper<SalaryPeriods> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(ObjectUtils.isNotNull(dto.getStatus()), "status", dto.getStatus());
    queryWrapper.orderByDesc("create_time");
    IPage<SalaryPeriodsVO> pageResult =
        salaryPeriodsMapper.selectPage(page, queryWrapper).convert(salaryPeriodsConvert::toVo);
    List<SysDictData> liveGameTypeList = sysDictDataMapper.findDataByType("LIVE_GAME_TYPE", "1");
    Map<String, List<SysDictData>> map =
        liveGameTypeList.stream().collect(Collectors.groupingBy(SysDictData::getDictValue));
    for (SalaryPeriodsVO vo : pageResult.getRecords()) {
      String gameType = vo.getGameType();
      String gameTypeName = "";
      if (StrUtil.isNotBlank(gameType)) {
        String[] split = gameType.split(",");
        vo.setGameCheckList(split);
        for (String s : split) {
          String dictLabel = map.get(s).get(0).getDictLabel();
          gameTypeName += (dictLabel + ",");
        }
        if (StrUtil.isNotBlank(gameTypeName)) {
          gameTypeName =
              gameTypeName.endsWith(",")
                  ? gameTypeName.substring(0, gameTypeName.length() - 1)
                  : gameTypeName;
          vo.setGameTypeName(gameTypeName);
        }
      }

      if (StrUtil.isNotBlank(vo.getAgentLevel())) {
        String[] split = vo.getAgentLevel().split(",");
        vo.setAgentCheckList(Arrays.stream(split).mapToInt(Integer::valueOf).toArray());
        String agentLevelLebel = "";
        for (String s : split) {
          agentLevelLebel += (s + "???,");
        }
        if (StrUtil.isNotBlank(agentLevelLebel)) {
          agentLevelLebel =
              agentLevelLebel.endsWith(",")
                  ? agentLevelLebel.substring(0, agentLevelLebel.length() - 1)
                  : agentLevelLebel;
          vo.setAgentLevelLebel(agentLevelLebel);
        }
      }
    }
    return pageResult;
  }

  /**
   * ??????
   *
   * @param dto
   */
  @Override
  public void add(SalaryPeriodsDTO dto) {
    SalaryPeriods saveObj = salaryPeriodsConvert.toEntity(dto);
    Assert.isTrue(this.save(saveObj), "???????????????????????????");
  }

  /**
   * ??????
   *
   * @param dto
   */
  @Override
  public void edit(SalaryPeriodsDTO dto) {
    Assert.isTrue(dto.getId() != null, "??????ID???????????????");
    SalaryPeriods editObj = salaryPeriodsConvert.toEntity(dto);
    Assert.isTrue(this.updateById(editObj), "???????????????????????????");
  }

  /**
   * ??????
   *
   * @param ids
   */
  @Override
  public void delete(String ids) {
    Assert.isTrue(StrUtil.isNotBlank(ids), "???????????????");
    String[] idArr = ids.split(",");
    for (String id : idArr) {
      QueryWrapper<SalaryGrant> deleteDetailWrapper = new QueryWrapper();
      deleteDetailWrapper.eq("periods_id", Long.valueOf(id));
      int delete = salaryGrantMapper.delete(deleteDetailWrapper);
      Assert.isTrue(this.removeById(id), ("???????????????"));
    }
  }

  /**
   * ??????????????????
   *
   * @param id
   */
  @Override
  public void settle(Long id) {
    Assert.isTrue(id != null, "????????????ID???????????????");
    SalaryPeriods periods = this.getById(id);
    Assert.isTrue(BeanUtil.isNotEmpty(periods), "??????????????????");
    Assert.isTrue(
        (periods.getStatus() != DivideStatusEnum.SALARY_PERIODS_STATUS_GRANTED.getValue()
            && periods.getStatus() != DivideStatusEnum.SALARY_PERIODS_STATUS_RECYCLE.getValue()),
        "?????????????????????");
    // ???????????????  ??????????????????
    String key = "salary:settle:" + id;
    Boolean isLock = redisTemplate.hasKey(key);
    Assert.isTrue(!isLock, "?????????????????????????????????????????????");
    distributedLocker.lock(key);
    try {
      // todo ?????????????????? ??????grant??????
      this.salarySettle(periods);
      // ???????????????????????? ?????????
      Assert.isTrue(
          this.lambdaUpdate()
              .set(
                  SalaryPeriods::getStatus,
                  DivideStatusEnum.SALARY_PERIODS_STATUS_SETTLED.getValue())
              .eq(SalaryPeriods::getId, id)
              .update(new SalaryPeriods()),
          "?????????????????????????????????");
    } catch (Exception e) {
      throw new ServiceException("???????????????????????????");
    } finally {
      distributedLocker.unlock(key);
    }
  }

  /**
   * ??????????????????
   *
   * @param id Long
   */
  @Override
  public void grant(Long id) {
    Assert.isTrue(id != null, "????????????ID???????????????");
    SalaryPeriods periods = this.getById(id);
    Assert.isTrue(BeanUtil.isNotEmpty(periods), "??????????????????");
    Assert.isTrue(
        periods.getStatus() == DivideStatusEnum.SALARY_PERIODS_STATUS_SETTLED.getValue(),
        "?????????????????????????????????");
    // ???????????????  ??????????????????
    String key = "salary:grant:" + id;
    Boolean isLock = redisTemplate.hasKey(key);
    Assert.isTrue(Boolean.FALSE.equals(isLock), "?????????????????????????????????????????????");
    distributedLocker.lock(key);
    try {
      UserCredential userCredential = SecurityUserHolder.getCredential();
      // todo ?????????????????? ??????????????????
      this.periodsGrant(periods, userCredential.getUsername());
      // ?????????????????????????????????
      Assert.isTrue(
          this.lambdaUpdate()
              .set(
                  SalaryPeriods::getStatus,
                  DivideStatusEnum.SALARY_PERIODS_STATUS_GRANTED.getValue())
              .eq(SalaryPeriods::getId, id)
              .update(new SalaryPeriods()),
          "???????????????????????????????????????");
    } catch (Exception e) {
      throw new ServiceException("???????????????????????????");
    } finally {
      distributedLocker.unlock(key);
    }
  }

  /**
   * ??????????????????
   *
   * @param id
   */
  @Override
  public void recycle(Long id) {
    Assert.isTrue(id != null, "????????????ID???????????????");
    SalaryPeriods periods = this.getById(id);
    Assert.isTrue(BeanUtil.isNotEmpty(periods), "??????????????????");
    Assert.isTrue(
        periods.getStatus() == DivideStatusEnum.SALARY_PERIODS_STATUS_GRANTED.getValue(),
        "????????????????????????");
    // ???????????????  ??????????????????
    String key = "salary:recycle:" + id;
    Boolean isLock = redisTemplate.hasKey(key);
    Assert.isTrue(!isLock, "?????????????????????????????????????????????");
    distributedLocker.lock(key);
    try {
      UserCredential userCredential = SecurityUserHolder.getCredential();
      // todo ?????????????????? ??????????????????
      this.periodsRecycle(periods, userCredential.getUsername());
      // ?????????????????????????????????
      Assert.isTrue(
          this.lambdaUpdate()
              .set(
                  SalaryPeriods::getStatus,
                  DivideStatusEnum.SALARY_PERIODS_STATUS_RECYCLE.getValue())
              .eq(SalaryPeriods::getId, id)
              .update(new SalaryPeriods()),
          "???????????????????????????????????????");
    } catch (Exception e) {
      throw new ServiceException("???????????????????????????");
    } finally {
      distributedLocker.unlock(key);
    }
  }

  /**
   * ???????????? ?????? ?????? ?????? ????????????
   *
   * @param periods
   */
  public void periodsGrant(SalaryPeriods periods, String operatorName) {
    // ????????????????????? ????????? ????????? ????????? grant??????
    LambdaQueryWrapper<SalaryGrant> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(SalaryGrant::getPeriodsId, periods.getId());
    queryWrapper.eq(
        SalaryGrant::getReachStatus, DivideStatusEnum.SALARY_REACH_STATUS_REACHED.getValue());
    queryWrapper.eq(
        SalaryGrant::getGrantStatus, DivideStatusEnum.SALARY_GRANT_STATUS_UNGRANT.getValue());
    List<SalaryGrant> salaryGrants = salaryGrantMapper.selectList(queryWrapper);
    if (CollectionUtil.isNotEmpty(salaryGrants)) {
      for (SalaryGrant grant : salaryGrants) {
        SalaryGrantDTO dto =
            SalaryGrantDTO.builder()
                .id(grant.getId())
                .grantStatus(DivideStatusEnum.SALARY_GRANT_STATUS_GRANTED.getValue())
                .build();
        SalaryGrant editObj = salaryGrantConvert.toEntity(dto);
        int i = salaryGrantMapper.updateById(editObj);
        if (i < 1) {
          continue;
        }
        if (grant.getAccount().equalsIgnoreCase(SystemConstant.DEFAULT_WEB_ROOT)
            || grant.getAccount().equalsIgnoreCase(SystemConstant.DEFAULT_TEST_ROOT)
            || grant.getAccount().equalsIgnoreCase(SystemConstant.DEFAULT_WAP_ROOT)) {
          continue;
        }
        // ?????? ?????????????????? ????????????0 ?????????????????? ????????????
        if (grant.getSalaryAmount().compareTo(BigDecimal.ZERO) <= 0) {
          continue;
        }

        // ????????????????????????
        Member member = memberService.getById(grant.getUserId());
        if (BeanUtil.isEmpty(member)) {
          continue;
        }
        MemberInfo memberInfo = memberInfoService.getById(grant.getUserId());
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
          // todo ????????????
          this.financialGrant(periods, grant, member, memberInfo, operatorName);
        } catch (Exception e) {
          log.error("??????{}????????????{}???????????????????????????", member.getAccount(), periods.getId());
        }
      }
    }
  }

  /**
   * ????????????
   *
   * @param periods
   */
  public void salarySettle(SalaryPeriods periods) {
    // ??????grant
    // ??????????????????
    QueryWrapper<SalaryGrant> deleteGrantWrapper = new QueryWrapper<>();
    deleteGrantWrapper.eq("periods_id", periods.getId());
    salaryGrantMapper.delete(deleteGrantWrapper);

    // ???????????????????????????????????????????????????????????????
    String kindCodes = periods.getGameType();
    Assert.isTrue(StrUtil.isNotBlank(kindCodes), "?????????????????????????????????");
    String levelNums = periods.getAgentLevel();
    Assert.isTrue(StrUtil.isNotBlank(levelNums), "?????????????????????????????????");

    kindCodes = kindCodes.startsWith(",") ? kindCodes.substring(1) : kindCodes;
    kindCodes =
        kindCodes.endsWith(",") ? kindCodes.substring(0, kindCodes.length() - 1) : kindCodes;
    String[] codes = kindCodes.split(",");

    levelNums = levelNums.startsWith(",") ? levelNums.substring(1) : levelNums;
    levelNums =
        levelNums.endsWith(",") ? levelNums.substring(0, levelNums.length() - 1) : levelNums;
    String[] perLevels = levelNums.split(",");
    List<Integer> perLevelList = new ArrayList<>();
    for (String level : perLevels) {
      perLevelList.add(Integer.parseInt(level));
    }
    // ???????????????????????????????????????????????????
    List<Member> openSalaryAgents = memberService.getOpenSalaryAgent(perLevelList);
    if (CollectionUtil.isEmpty(openSalaryAgents)) {
      throw new ServiceException("????????????????????????????????????????????????????????????????????????");
    }

    Map<Integer, Map<String, SalaryConfig>> configMap = new TreeMap<>();
    // ?????????????????????????????????????????????
    for (Member user : openSalaryAgents) {
      Map<String, SalaryConfig> tmpMap = new TreeMap<>();
      for (String kindCode : codes) {
        // ??????????????? ????????????????????????????????????????????????
        SalaryConfig salaryConfig = salaryConfigMapper.getConfig(user.getAgentLevel(), kindCode);
        if (BeanUtil.isEmpty(salaryConfig)) {
          continue;
        }
        tmpMap.put(kindCode, salaryConfig);
      }
      configMap.put(user.getAgentLevel(), tmpMap);
    }
    if (CollectionUtil.isEmpty(configMap)) {
      throw new ServiceException("??????????????????????????????????????????????????????");
    }
    // ???????????? ??????????????? ??????
    QueryWrapper<BizBlacklist> queryBizWrapper = new QueryWrapper();
    queryBizWrapper.like("types", BlacklistConstant.BizBlacklistType.DL_DAY_WAGE.getValue());
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
    // todo ????????????????????? ?????? grant??????
    this.grantSettle(openSalaryAgents, accountBlacks, userLevelBlacks, periods, configMap, codes);
  }

  /**
   * ????????????????????????
   *
   * @param openSalaryAgents List
   * @param accountBlacks List
   * @param userLevelBlacks List
   * @param periods SalaryPeriods
   */
  public void grantSettle(
      List<Member> openSalaryAgents,
      List<String> accountBlacks,
      List<String> userLevelBlacks,
      SalaryPeriods periods,
      Map<Integer, Map<String, SalaryConfig>> configMap,
      String[] codes) {
    AgentConfig agentConfig = agentConfigService.getAgentConfig();
    // ????????????????????????
    Integer isIncludeAgent = agentConfig.getIsIncludeAgent();
    // ???????????? ?????????????????????
    BigDecimal validAmountLower = agentConfig.getValidAmountLimit();
    // ???????????? ??????????????????
    BigDecimal rechargeAmountLower = agentConfig.getRechargeAmountLimit();
    for (Member member : openSalaryAgents) {
      Integer agentLevel = member.getAgentLevel();
      String account = member.getAccount();
      if (CollectionUtil.isNotEmpty(accountBlacks) && accountBlacks.contains(account)) {
        log.info("???????????????--??????{}--??????????????????", account);
        continue;
      }
      if (CollectionUtil.isNotEmpty(userLevelBlacks) && userLevelBlacks.contains(agentLevel)) {
        log.info("???????????????--??????{}--???????????????", account);
        continue;
      }

      // ????????????????????????????????????????????? ????????????????????????????????????
      List<SalaryRechargeVO> rechargeReport =
          rechargeOrderMapper.getRechargeForSalary(
              periods.getStartDate(), periods.getEndDate(), account, isIncludeAgent);
      // ?????????????????????????????????????????? ???????????? ???????????? ?????????????????????????????????????????????
      List<SalaryRechargeVO> gameReports =
          dailyReportMapper.findReportForSalary(
              periods.getStartDate(), periods.getEndDate(), account, isIncludeAgent);
      // ?????????????????????
      Map<String, List<SalaryRechargeVO>> gameTypeReportMap =
          gameReports.stream().collect(Collectors.groupingBy(SalaryRechargeVO::getGameType));
      for (String gameType : codes) {
        Map<String, SalaryConfig> levelMap = configMap.get(agentLevel);
        if (CollectionUtil.isEmpty(levelMap)) {
          continue;
        }
        SalaryConfig finalConfig = levelMap.get(gameType);
        if (BeanUtil.isEmpty(finalConfig)) {
          continue;
        }
        List<SalaryRechargeVO> tmpGameReport =
            CollectionUtil.isEmpty(gameTypeReportMap.get(gameType))
                ? new ArrayList<>()
                : gameTypeReportMap.get(gameType);

        BigDecimal winAmountLimit = finalConfig.getWinAmountLimit(); // ??????????????????
        Integer validUserLimit = finalConfig.getValidUserLimit(); // ?????????????????????
        BigDecimal validAmountLimit = finalConfig.getValidAmountLimit(); // ??????????????????
        BigDecimal rechargeAmountLimit = finalConfig.getRechargeAmountLimit(); // ??????????????????
        Integer isOnlyChild = finalConfig.getIsDirect(); // ?????????????????????????????????
        Integer salaryType = finalConfig.getType(); // ???????????? 1 ?????????  2 ??????
        BigDecimal salaryAmount = finalConfig.getAmount(); // ??????????????????????????????
        BigDecimal salaryAmountLimit = finalConfig.getAmountLimit(); //  ????????????

        SalaryGrantDTO saveObj =
            SalaryGrantDTO.builder()
                .periodsId(periods.getId()) // ??????ID
                .userId(member.getId()) // ??????ID
                .account(member.getAccount()) // ????????????
                .agentLevel(member.getAgentLevel()) // ????????????
                .superPath(member.getSuperPath()) // ????????????
                .parentId(member.getParentId()) // ??????ID
                .parentName(member.getParentName()) // ????????????
                .gameType(gameType) // ????????????
                .build();
        // ??????????????????'
        boolean isReach = true;

        // ?????????????????????  ?????????????????????????????????
        if (validUserLimit > 0) {
          List<String> filterRechargeList =
              rechargeReport.stream()
                  .filter(item -> item.getRechargeAmount().compareTo(rechargeAmountLower) >= 0)
                  .map(SalaryRechargeVO::getAccount)
                  .collect(Collectors.toList());
          List<String> filterGameList =
              tmpGameReport.stream()
                  .filter(item -> item.getValidAmount().compareTo(validAmountLower) >= 0)
                  .map(SalaryRechargeVO::getAccount)
                  .collect(Collectors.toList());
          // ?????????????????????????????????
          List<String> nList =
              filterRechargeList.stream()
                  .filter(filterGameList::contains)
                  .collect(Collectors.toList());
          saveObj.setValidUserNum(nList.size());
          if (nList.size() < validUserLimit) {
            isReach = false;
          }
        } else { // ???????????????????????????????????????
          List<String> l1 =
              rechargeReport.stream()
                  .map(SalaryRechargeVO::getAccount)
                  .collect(Collectors.toList());
          List<String> l2 =
              tmpGameReport.stream().map(SalaryRechargeVO::getAccount).collect(Collectors.toList());
          l1.addAll(l2);
          List<String> uList = l1.stream().distinct().collect(Collectors.toList());
          saveObj.setValidUserNum(uList.size());
        }

        // ???????????????????????? ?????????????????????????????????
        BigDecimal totalRecharge =
            rechargeReport.stream()
                .map(SalaryRechargeVO::getRechargeAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        saveObj.setRechargeAmount(totalRecharge);
        if (totalRecharge.compareTo(rechargeAmountLimit) < 0) {
          isReach = false;
        }

        // ????????????????????? ???????????????????????????????????? ?????????????????????????????? ?????????????????????????????????
        BigDecimal totalWinAmount =
            tmpGameReport.stream()
                .map(SalaryRechargeVO::getWinAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalWinAmount = totalWinAmount.negate();
        saveObj.setWinAmount(totalWinAmount);
        if (winAmountLimit.compareTo(BigDecimal.ZERO) > 0
            && totalWinAmount.compareTo(winAmountLimit) < 0) {
          isReach = false;
        }
        // ???????????????????????????  ??????????????????????????????
        if (isOnlyChild == 1) {
          tmpGameReport =
              tmpGameReport.stream()
                  .filter(item -> item.getSuperAccount().equalsIgnoreCase(account))
                  .collect(Collectors.toList());
        }
        BigDecimal totalValidAmount =
            tmpGameReport.stream()
                .map(SalaryRechargeVO::getValidAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        saveObj.setValidAmount(totalValidAmount);
        if (totalValidAmount.compareTo(validAmountLimit) < 0) {
          isReach = false;
        }

        // ????????????
        BigDecimal finalSalaryAmount = BigDecimal.ZERO;
        if (salaryType == 1) { // ????????? --- ???????????? * ?????????
          finalSalaryAmount =
              salaryAmount.divide(new BigDecimal("100")).setScale(4).multiply(totalValidAmount);
        } else { // ??????
          finalSalaryAmount = salaryAmount;
        }
        // ??????????????? ????????????
        if (salaryAmountLimit.compareTo(BigDecimal.ZERO) > 0) {
          finalSalaryAmount =
              finalSalaryAmount.compareTo(salaryAmountLimit) > 0
                  ? salaryAmountLimit
                  : finalSalaryAmount;
        }
        saveObj.setSalaryAmount(finalSalaryAmount); // ????????????

        saveObj.setReachStatus(
            isReach
                ? DivideStatusEnum.SALARY_REACH_STATUS_REACHED.getValue()
                : DivideStatusEnum.SALARY_REACH_STATUS_UNREACH.getValue());
        saveObj.setGrantStatus(DivideStatusEnum.SALARY_GRANT_STATUS_UNGRANT.getValue());
        SalaryGrant grantSaveObj = salaryGrantConvert.toEntity(saveObj);
        // ????????????????????????
        int grantSaveCount = salaryGrantMapper.insert(grantSaveObj);
      }
    }
  }

  /**
   * ????????????????????????
   *
   * @param periods SalaryPeriods
   * @param salaryGrant SalaryGrant
   * @param member Member
   * @param memberInfo MemberInfo
   * @param operatorName String
   */
  public void financialGrant(
      SalaryPeriods periods,
      SalaryGrant salaryGrant,
      Member member,
      MemberInfo memberInfo,
      String operatorName) {
    String sb =
        periods.getStartDate()
            + "~"
            + periods.getEndDate()
            + "??????????????? "
            + salaryGrant.getSalaryAmount()
            + "???";

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
      memberBill.setTranType(TranTypes.SALARY_AMOUNT.getValue());
      memberBill.setOrderNo(String.valueOf(IdGeneratorSnowflake.getInstance().nextId()));
      memberBill.setAmount(salaryGrant.getSalaryAmount());
      memberBill.setBalance(memberInfo.getBalance());
      memberBill.setOperator(operatorName);
      memberBill.setRemark(sb);
      memberBill.setContent(sb);
      memberBillService.save(memberBill);
      memberInfoService.updateBalance(member.getId(), salaryGrant.getSalaryAmount());
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
    message.setType(TrueFalse.TRUE.getValue());
    message.setStatus(TrueFalse.TRUE.getValue());
    message.setCreateBy(operatorName);
    messageMapper.saveReturnId(message);
//
//    MessageDistribute messageDistribute = new MessageDistribute();
//    messageDistribute.setMessageId(message.getId());
//    messageDistribute.setUserId(member.getId());
//    messageDistribute.setUserAccount(member.getAccount());
//    messageDistribute.setRechargeLevel(member.getUserLevel());
//    messageDistribute.setVipLevel(memberInfoService.getById(member.getId()).getVipLevel());
//    messageDistribute.setReadStatus(TrueFalse.FALSE.getValue());
//    messageDistribute.setCreateBy(operatorName);
//    messageDistributeService.save(messageDistribute);
  }

  /**
   * ??????????????????
   *
   * @param periods SalaryPeriods
   * @param operatorName String
   */
  public void periodsRecycle(SalaryPeriods periods, String operatorName) {
    // ????????????????????? ????????? ????????? grant??????
    LambdaQueryWrapper<SalaryGrant> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(SalaryGrant::getPeriodsId, periods.getId());
    queryWrapper.eq(
        SalaryGrant::getGrantStatus, DivideStatusEnum.SALARY_GRANT_STATUS_GRANTED.getValue());
    List<SalaryGrant> salaryGrants = salaryGrantMapper.selectList(queryWrapper);
    if (CollectionUtil.isNotEmpty(salaryGrants)) {
      for (SalaryGrant grant : salaryGrants) {
        SalaryGrantDTO dto =
            SalaryGrantDTO.builder()
                .id(grant.getId())
                .grantStatus(DivideStatusEnum.SALARY_GRANT_STATUS_RECYCLE.getValue())
                .build();
        SalaryGrant editObj = salaryGrantConvert.toEntity(dto);
        int i = salaryGrantMapper.updateById(editObj);
        if (i < 1) {
          continue;
        }
        if (grant.getAccount().equalsIgnoreCase(SystemConstant.DEFAULT_WEB_ROOT)
            || grant.getAccount().equalsIgnoreCase(SystemConstant.DEFAULT_TEST_ROOT)
            || grant.getAccount().equalsIgnoreCase(SystemConstant.DEFAULT_WAP_ROOT)) {
          continue;
        }
        // ?????? ?????????????????? ????????????0 ?????????????????? ????????????
        if (grant.getSalaryAmount().compareTo(BigDecimal.ZERO) <= 0) {
          continue;
        }

        // ????????????????????????
        Member member = memberService.getById(grant.getUserId());
        if (BeanUtil.isEmpty(member)) {
          continue;
        }
        MemberInfo memberInfo = memberInfoService.getById(grant.getUserId());
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
          // todo ????????????
          this.financialRecycle(periods, grant, member, memberInfo, operatorName);
        } catch (Exception e) {
          log.error("??????{}????????????{}???????????????????????????", member.getAccount(), periods.getId());
        }
      }
    }
  }

  /**
   * ??????????????????
   *
   * @param periods
   * @param salaryGrant
   * @param member
   * @param memberInfo
   * @param operatorName
   */
  public void financialRecycle(
      SalaryPeriods periods,
      SalaryGrant salaryGrant,
      Member member,
      MemberInfo memberInfo,
      String operatorName) {
    String sb =
        periods.getStartDate()
            + "~"
            + periods.getEndDate()
            + "??????????????? "
            + salaryGrant.getSalaryAmount().negate()
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
      memberBill.setTranType(TranTypes.SALARY_AMOUNT_BACK.getValue());
      memberBill.setOrderNo(String.valueOf(IdGeneratorSnowflake.getInstance().nextId()));
      memberBill.setAmount(salaryGrant.getSalaryAmount().negate());
      memberBill.setBalance(memberInfo.getBalance());
      memberBill.setRemark(sb);
      memberBill.setContent(sb);
      memberBill.setOperator(operatorName);
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
    BigDecimal newBalance = memberInfo.getBalance().add(salaryGrant.getSalaryAmount().negate());
    if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
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
}
