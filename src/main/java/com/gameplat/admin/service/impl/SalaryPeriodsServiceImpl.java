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
import com.gameplat.admin.enums.MemberBillTransTypeEnum;
import com.gameplat.admin.enums.PushMessageEnum;
import com.gameplat.admin.mapper.*;
import com.gameplat.admin.model.dto.MessageInfoAddDTO;
import com.gameplat.admin.model.dto.SalaryConfigDTO;
import com.gameplat.admin.model.dto.SalaryGrantDTO;
import com.gameplat.admin.model.dto.SalaryPeriodsDTO;
import com.gameplat.admin.model.vo.DivideGameReportVO;
import com.gameplat.admin.model.vo.SalaryConfigVO;
import com.gameplat.admin.model.vo.SalaryPeriodsVO;
import com.gameplat.admin.model.vo.SalaryRechargeVO;
import com.gameplat.admin.service.*;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.snowflake.IdGeneratorSnowflake;
import com.gameplat.common.enums.MemberEnums;
import com.gameplat.model.entity.blacklist.BizBlacklist;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.member.MemberBill;
import com.gameplat.model.entity.member.MemberInfo;
import com.gameplat.model.entity.message.Message;
import com.gameplat.model.entity.message.MessageDistribute;
import com.gameplat.model.entity.proxy.*;
import com.gameplat.model.entity.recharge.RechargeOrder;
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
@SuppressWarnings("all")
@Slf4j
public class SalaryPeriodsServiceImpl
        extends ServiceImpl<SalaryPeriodsMapper, SalaryPeriods>
        implements SalaryPeriodsService {

    @Autowired private SalaryPeriodsMapper salaryPeriodsMapper;
    @Autowired private SalaryPeriodsConvert salaryPeriodsConvert;
    @Autowired private SysDictDataMapper sysDictDataMapper;
    @Autowired private SalaryGrantMapper salaryGrantMapper;
    @Autowired private StringRedisTemplate redisTemplate;
    @Autowired private DistributedLocker distributedLocker;
    @Autowired private MemberService memberService;
    @Autowired private RecommendConfigService recommendConfigService;
    @Autowired private SalaryConfigMapper salaryConfigMapper;
    @Autowired private BizBlacklistMapper blacklistMapper;
    @Autowired private RechargeOrderMapper rechargeOrderMapper;
    @Autowired private GameBetDailyReportMapper dailyReportMapper;
    @Autowired private SalaryGrantConvert salaryGrantConvert;
    @Autowired private MemberInfoService memberInfoService;
    @Autowired private MemberBillService memberBillService;
    @Autowired private MessageInfoConvert messageInfoConvert;
    @Autowired private MessageMapper messageMapper;
    @Autowired private MessageDistributeService messageDistributeService;

    @Override
    public IPage<SalaryPeriodsVO> queryPage(PageDTO<SalaryPeriods> page, SalaryPeriodsDTO dto) {
        QueryWrapper<SalaryPeriods> queryWrapper = new QueryWrapper();
        queryWrapper
                .eq(ObjectUtils.isNotNull(dto.getStatus()), "status", dto.getStatus());
        queryWrapper.orderByDesc("create_time");
        IPage<SalaryPeriodsVO> pageResult = salaryPeriodsMapper.selectPage(page, queryWrapper)
                .convert(salaryPeriodsConvert::toVo);
        List<SysDictData> liveGameTypeList = sysDictDataMapper.findDataByType("LIVE_GAME_TYPE", "1");
        Map<String, List<SysDictData>> map = liveGameTypeList.stream().collect(Collectors.groupingBy(SysDictData::getDictValue));
        for (SalaryPeriodsVO vo : pageResult.getRecords()) {
            String gameType = vo.getGameType();
            String gameTypeName = "";
            if (StrUtil.isNotBlank(gameType)){
                String[] split = gameType.split(",");
                vo.setGameCheckList(split);
                for (String s : split) {
                    String dictLabel = map.get(s).get(0).getDictLabel();
                    gameTypeName += (dictLabel+",");
                }
                if (StrUtil.isNotBlank(gameTypeName)) {
                    gameTypeName = gameTypeName.endsWith(",") ? gameTypeName.substring(0,gameTypeName.length()-1) : gameTypeName;
                    vo.setGameTypeName(gameTypeName);
                }
            }

            if (StrUtil.isNotBlank(vo.getAgentLevel())) {
                String[] split = vo.getAgentLevel().split(",");
                vo.setAgentCheckList(Arrays.stream(split).mapToInt(Integer::valueOf).toArray());
                String agentLevelLebel = "";
                for (String s : split) {
                    agentLevelLebel += (s+"级,");
                }
                if (StrUtil.isNotBlank(agentLevelLebel)) {
                    agentLevelLebel = agentLevelLebel.endsWith(",") ? agentLevelLebel.substring(0,agentLevelLebel.length()-1) : agentLevelLebel;
                    vo.setAgentLevelLebel(agentLevelLebel);
                }
            }
        }
        return pageResult;
    }

    @Override
    public void add(SalaryPeriodsDTO dto) {
        SalaryPeriods saveObj = salaryPeriodsConvert.toEntity(dto);
        Assert.isTrue(this.save(saveObj),"添加工资期数失败！");
    }

    @Override
    public void edit(SalaryPeriodsDTO dto) {
        Assert.isTrue(dto.getId() != null, "主键ID参数缺失！");
        SalaryPeriods editObj = salaryPeriodsConvert.toEntity(dto);
        Assert.isTrue(this.updateById(editObj),"编辑工资期数失败！");
    }

    @Override
    public void delete(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "参数为空！");
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            QueryWrapper<SalaryGrant> deleteDetailWrapper = new QueryWrapper();
            deleteDetailWrapper.eq("periods_id", Long.valueOf(id));
            int delete = salaryGrantMapper.delete(deleteDetailWrapper);
            Assert.isTrue(this.removeById(id), ("删除失败！"));
        }
    }

    /**
     * 工资期数结算
     * @param id
     */
    @Override
    public void settle(Long id) {
        Assert.isTrue(id != null, "期数主键ID参数缺失！");
        SalaryPeriods periods = this.getById(id);
        Assert.isTrue(BeanUtil.isNotEmpty(periods), "期数不存在！");
        Assert.isTrue((periods.getStatus() != DivideStatusEnum.SALARY_PERIODS_STATUS_GRANTED.getValue()
                && periods.getStatus() != DivideStatusEnum.SALARY_PERIODS_STATUS_RECYCLE.getValue()), "状态不能结算！");
        // 外层先锁住  防止重复点击
        String key = "salary:settle:" + id;
        Boolean isLock = redisTemplate.hasKey(key);
        Assert.isTrue(!isLock, "您的操作过于频繁！请稍后重试！");
        distributedLocker.lock(key);
        try {
            // todo 调用封装方法 处理grant结算
            this.salarySettle(periods);
            // 修改期数的状态为 已结算
            Assert.isTrue(
                this.lambdaUpdate()
                        .set(SalaryPeriods::getStatus, DivideStatusEnum.SALARY_PERIODS_STATUS_SETTLED.getValue())
                        .eq(SalaryPeriods::getId, id)
                        .update(new SalaryPeriods()),
                "工资期数结算状态失败！");
        } catch (Exception e) {
            throw new ServiceException("工资期数结算失败！");
        } finally {
            distributedLocker.unlock(key);
        }
    }

    /**
     * 工资期数派发
     * @param id
     */
    @Override
    public void grant(Long id) {
        Assert.isTrue(id != null, "期数主键ID参数缺失！");
        SalaryPeriods periods = this.getById(id);
        Assert.isTrue(BeanUtil.isNotEmpty(periods), "期数不存在！");
        Assert.isTrue(periods.getStatus() == DivideStatusEnum.SALARY_PERIODS_STATUS_SETTLED.getValue(), "非已结算状态不能派发！");
        // 外层先锁住  防止重复点击
        String key = "salary:grant:" + id;
        Boolean isLock = redisTemplate.hasKey(key);
        Assert.isTrue(!isLock, "您的操作过于频繁！请稍后重试！");
        distributedLocker.lock(key);
        try {
            UserCredential userCredential = SecurityUserHolder.getCredential();
            // todo 调用封装方法 处理工资派发
            this.periodsGrant(periods, userCredential.getUsername());
            // 修改期数的状态为已派发
            Assert.isTrue(
                    this.lambdaUpdate()
                            .set(SalaryPeriods::getStatus, DivideStatusEnum.SALARY_PERIODS_STATUS_GRANTED.getValue())
                            .eq(SalaryPeriods::getId, id)
                            .update(new SalaryPeriods()),
                    "编辑工资期数派发状态失败！");
        }  catch (Exception e) {
            throw new ServiceException("工资期数派发失败！");
        } finally {
            distributedLocker.unlock(key);
        }
    }

    /**
     * 工资期数回收
     * @param id
     */
    @Override
    public void recycle(Long id) {
        Assert.isTrue(id != null, "期数主键ID参数缺失！");
        SalaryPeriods periods = this.getById(id);
        Assert.isTrue(BeanUtil.isNotEmpty(periods), "期数不存在！");
        Assert.isTrue(periods.getStatus() == DivideStatusEnum.SALARY_PERIODS_STATUS_GRANTED.getValue(), "未派发不能回收！");
        // 外层先锁住  防止重复点击
        String key = "salary:recycle:" + id;
        Boolean isLock = redisTemplate.hasKey(key);
        Assert.isTrue(!isLock, "您的操作过于频繁！请稍后重试！");
        distributedLocker.lock(key);
        try {
            UserCredential userCredential = SecurityUserHolder.getCredential();
            // todo 调用封装方法 处理工资回收
            this.periodsRecycle(periods, userCredential.getUsername());
            // 修改期数的状态为已派发
            Assert.isTrue(
                    this.lambdaUpdate()
                            .set(SalaryPeriods::getStatus, DivideStatusEnum.SALARY_PERIODS_STATUS_RECYCLE.getValue())
                            .eq(SalaryPeriods::getId, id)
                            .update(new SalaryPeriods()),
                    "编辑工资期数派发状态失败！");
        }  catch (Exception e) {
            throw new ServiceException("工资期数回收失败！");
        } finally {
            distributedLocker.unlock(key);
        }
    }

    /**
     * 期数派发 处理 流水 账变 派发统计
     * @param periods
     */
    public void periodsGrant(SalaryPeriods periods, String operatorName) {
        // 根据期数获取到 已达标 未派发 状态的 grant数据
        LambdaQueryWrapper<SalaryGrant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SalaryGrant::getPeriodsId, periods.getId());
        queryWrapper.eq(SalaryGrant::getReachStatus, DivideStatusEnum.SALARY_REACH_STATUS_REACHED.getValue());
        queryWrapper.eq(SalaryGrant::getGrantStatus, DivideStatusEnum.SALARY_GRANT_STATUS_UNGRANT.getValue());
        List<SalaryGrant> salaryGrants = salaryGrantMapper.selectList(queryWrapper);
        if (CollectionUtil.isNotEmpty(salaryGrants)) {
            for (SalaryGrant grant : salaryGrants) {
                SalaryGrantDTO dto = SalaryGrantDTO.builder()
                        .id(grant.getId())
                        .grantStatus(DivideStatusEnum.SALARY_GRANT_STATUS_GRANTED.getValue())
                        .build();
                SalaryGrant editObj = salaryGrantConvert.toEntity(dto);
                int i = salaryGrantMapper.updateById(editObj);
                if (i < 1) {
                    continue;
                }
                if (grant.getAccount().equalsIgnoreCase(SystemConstant.DEFAULT_WEB_ROOT) ||
                        grant.getAccount().equalsIgnoreCase(SystemConstant.DEFAULT_TEST_ROOT) ||
                        grant.getAccount().equalsIgnoreCase(SystemConstant.DEFAULT_WAP_ROOT)) {
                    continue;
                }
                // 如果 真实分红金额 小于等于0 不用发生账变 资金变动
                if (grant.getSalaryAmount().compareTo(BigDecimal.ZERO) <= 0) {
                    continue;
                }

                // 校验会员账户状态
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
                    // todo 资金变动
                    this.financialGrant(periods, grant, member, memberInfo, operatorName);
                } catch (Exception e) {
                    log.error("会员{}工资期数{}派发资金处理失败！", member.getAccount(), periods.getId());
                    continue;
                }
            }
        }
    }

    /**
     * 工资结算
     * @param periods
     */
    public void salarySettle(SalaryPeriods periods){
        // 删除grant
        // 删除分红详情
        QueryWrapper<SalaryGrant> deleteGrantWrapper = new QueryWrapper();
        deleteGrantWrapper.eq("periods_id", periods.getId());
        salaryGrantMapper.delete(deleteGrantWrapper);

        // 获取得到工资期数的支持的代理层级和游戏大类
        String kindCodes = periods.getGameType();
        Assert.isTrue(StrUtil.isNotBlank(kindCodes),"工资未选择任何游戏类型");
        String levelNums = periods.getAgentLevel();
        Assert.isTrue(StrUtil.isNotBlank(levelNums),"工资未选择任何代理层级");

        kindCodes = kindCodes.startsWith(",") ? kindCodes.substring(1) : kindCodes;
        kindCodes = kindCodes.endsWith(",") ? kindCodes.substring(0,kindCodes.length() - 1) : kindCodes;
        String[] codes = kindCodes.split(",");

        levelNums = levelNums.startsWith(",") ? levelNums.substring(1) : levelNums;
        levelNums = levelNums.endsWith(",") ? levelNums.substring(0,levelNums.length() - 1) : levelNums;
        String[] perLevels = levelNums.split(",");
        List<Integer> perLevelList = new ArrayList<>();
        for (String level : perLevels) {
            perLevelList.add(Integer.parseInt(level));
        }
        // 获取满足工资配置且开启了工资的代理
        List<Member> openSalaryAgents = memberService.getOpenSalaryAgent(perLevelList);
        if (CollectionUtil.isEmpty(openSalaryAgents)) {
            throw new ServiceException("请确认您勾选的代理层级的代理是否开启了工资统计！");
        }

        Map<Integer, Map<String,SalaryConfig>> configMap = new TreeMap<>();
        // 先把所有需要的工资配置查询出来
        for (Member user : openSalaryAgents){
            Map<String,SalaryConfig> tmpMap = new TreeMap<>();
            for (String kindCode : codes) {
                // 获取此等级 此游戏类型的排序值最小的工资配置
                SalaryConfig salaryConfig = salaryConfigMapper.getConfig(user.getAgentLevel(), kindCode);
                if (BeanUtil.isEmpty(salaryConfig)) {
                    continue;
                }
                tmpMap.put(kindCode,salaryConfig);
            }
            configMap.put(user.getAgentLevel(),tmpMap);
        }
        if (CollectionUtil.isEmpty(configMap)) {
            throw new ServiceException("请先配置对应代理层级游戏的工资配置！");
        }
        // 获取业务 分红黑名单 集合
        QueryWrapper<BizBlacklist> queryBizWrapper = new QueryWrapper();
        queryBizWrapper.like("types", BlacklistConstant.BizBlacklistType.DL_DAY_WAGE.getValue());
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
        // todo 调用封装的方法 处理 grant数据
        this.grantSettle(openSalaryAgents, accountBlacks, userLevelBlacks, periods, configMap, codes);
    }

    /**
     * 工资派发统计结算
     * @param openSalaryAgents
     * @param accountBlacks
     * @param userLevelBlacks
     * @param periods
     */
    public void grantSettle(List<Member> openSalaryAgents,
                            List<String> accountBlacks,
                            List<String> userLevelBlacks,
                            SalaryPeriods periods,
                            Map<Integer, Map<String,SalaryConfig>> configMap,
                            String[] codes){
        RecommendConfig recommendConfig = recommendConfigService.getRecommendConfig();
        // 是否包含代理数据
        Integer isIncludeAgent = recommendConfig.getIsIncludeAgent();
        // 有效会员 最低打码量定义
        BigDecimal validAmountLower = recommendConfig.getValidAmountLimit();
        // 有效会员 最低充值定义
        BigDecimal rechargeAmountLower = recommendConfig.getRechargeAmountLimit();
        for (Member member : openSalaryAgents) {
            Integer agentLevel = member.getAgentLevel();
            String account = member.getAccount();
            if (CollectionUtil.isNotEmpty(accountBlacks) && accountBlacks.contains(account)) {
                log.info("日工资结算--账号{}--用户名黑名单",account);
                continue;
            }
            if (CollectionUtil.isNotEmpty(userLevelBlacks) && userLevelBlacks.contains(agentLevel)){
                log.info("日工资结算--账号{}--层级黑名单",account);
                continue;
            }

            // 获取此代理的所有下级的充值数据 需要区分是否包含代理数据
            List<SalaryRechargeVO> rechargeReport =
                    rechargeOrderMapper.getRechargeForSalary(periods.getStartDate(), periods.getEndDate(), account, isIncludeAgent);
            // 获取此代理所有下级的有效投注 输赢金额 上级名称 需要区分是否包含自己本身的数据
            List<SalaryRechargeVO> gameReports =
                    dailyReportMapper.findReportForSalary(periods.getStartDate(), periods.getEndDate(), account, isIncludeAgent);
            // 按游戏大类分组
            Map<String,List<SalaryRechargeVO>> gameTypeReportMap =
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
                        CollectionUtil.isEmpty(gameTypeReportMap.get(gameType)) ? new ArrayList<>() : gameTypeReportMap.get(gameType);

                BigDecimal winAmountLimit = finalConfig.getWinAmountLimit();           // 盈利金额下限
                Integer validUserLimit = finalConfig.getValidUserLimit();              // 有效会员数下限
                BigDecimal validAmountLimit = finalConfig.getValidAmountLimit();       // 有效投注下限
                BigDecimal rechargeAmountLimit = finalConfig.getRechargeAmountLimit(); // 充值金额下限
                Integer isOnlyChild = finalConfig.getIsDirect();                    // 是否只计算下级有效投注
                Integer salaryType = finalConfig.getType();                      // 工资类型 1 百分比  2 定额
                BigDecimal salaryAmount = finalConfig.getAmount();               // 工资奖励金额或百分比
                BigDecimal salaryAmountLimit = finalConfig.getAmountLimit();     //  工资上限

                SalaryGrantDTO saveObj = SalaryGrantDTO.builder()
                        .periodsId(periods.getId())// 期数ID
                        .userId(member.getId())// 代理ID
                        .account(member.getAccount())// 代理名称
                        .agentLevel(member.getAgentLevel())// 代理层级
                        .superPath(member.getSuperPath())//代理路径
                        .parentId(member.getParentId())//上级ID
                        .parentName(member.getParentName())//上级名称
                        .gameType(gameType)//游戏大类
                        .build();
                Boolean isReach = true;// 是否达标标识'

                // 计算有效会员数  不区分是否只查直接下级
                if (validUserLimit > 0) {
                    List<String> filterRechargeList =
                            rechargeReport.stream().filter(item -> item.getRechargeAmount().compareTo(rechargeAmountLower) >= 0).map(SalaryRechargeVO::getAccount).collect(Collectors.toList());
                    List<String> filterGameList =
                            tmpGameReport.stream().filter(item -> item.getValidAmount().compareTo(validAmountLower) >= 0).map(SalaryRechargeVO::getAccount).collect(Collectors.toList());
                    // 再求交集（保证都满足）
                    List<String> nList =
                            filterRechargeList.stream().filter(item -> filterGameList.contains(item)).collect(Collectors.toList());
                    saveObj.setValidUserNum(nList.size());
                    if (nList.size() < validUserLimit) {
                        isReach = false;
                    }
                } else {// 直接求充值和投注的并集人数
                    List<String> l1 = rechargeReport.stream().map(SalaryRechargeVO::getAccount).collect(Collectors.toList());
                    List<String> l2 = tmpGameReport.stream().map(SalaryRechargeVO::getAccount).collect(Collectors.toList());
                    l1.addAll(l2);
                    List<String> UList = l1.stream().distinct().collect(Collectors.toList());
                    saveObj.setValidUserNum(UList.size());
                }

                // 计算总的充值金额 不区分是否只查直接下级
                BigDecimal totalRecharge = rechargeReport.stream().map(SalaryRechargeVO::getRechargeAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                saveObj.setRechargeAmount(totalRecharge);
                if (totalRecharge.compareTo(rechargeAmountLimit) < 0) {
                    isReach = false;
                }

                // 计算总盈利金额 从游戏报表取出是会员角度 因此业主角度需要取反 不区分是否只查直接下级
                BigDecimal totalWinAmount = tmpGameReport.stream().map(SalaryRechargeVO::getWinAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                totalWinAmount = totalWinAmount.negate();
                saveObj.setWinAmount(totalWinAmount);
                if (winAmountLimit.compareTo(BigDecimal.ZERO) > 0 && totalWinAmount.compareTo(winAmountLimit) < 0) {
                    isReach = false;
                }

                // 计算总有效投注金额  需要区分是否只查下级
                if (isOnlyChild == 1) {
                    tmpGameReport = tmpGameReport.stream().filter(item -> item.getSuperAccount().equalsIgnoreCase(account)).collect(Collectors.toList());
                }
                BigDecimal totalValidAmount = tmpGameReport.stream().map(SalaryRechargeVO::getValidAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                saveObj.setValidAmount(totalValidAmount);
                if (totalValidAmount.compareTo(validAmountLimit) < 0) {
                    isReach = false;
                }

                // 计算工资
                BigDecimal finalSalaryAmount = BigDecimal.ZERO;
                if (salaryType == 1) {// 百分比 --- 有效投注 * 百分比
                    finalSalaryAmount = salaryAmount.divide(new BigDecimal("100")).multiply(totalValidAmount);
                } else {              // 定额
                    finalSalaryAmount = salaryAmount;
                }

                // 是否超过了 工资上限
                if (salaryAmountLimit.compareTo(BigDecimal.ZERO) > 0){
                    finalSalaryAmount = finalSalaryAmount.compareTo(salaryAmountLimit) > 0 ? salaryAmountLimit : finalSalaryAmount;
                }
                saveObj.setSalaryAmount(finalSalaryAmount);// 工资接内

                saveObj.setReachStatus(isReach ? DivideStatusEnum.SALARY_REACH_STATUS_REACHED.getValue() : DivideStatusEnum.SALARY_REACH_STATUS_UNREACH.getValue());
                saveObj.setGrantStatus(DivideStatusEnum.SALARY_GRANT_STATUS_UNGRANT.getValue());
                SalaryGrant grantSaveObj = salaryGrantConvert.toEntity(saveObj);
                // 添加工资派发统计
                int grantSaveCount = salaryGrantMapper.insert(grantSaveObj);
            }
        }
    }

    /**
     * 工资期数资金账变
     * @param periods
     * @param salaryGrant
     * @param member
     * @param memberInfo
     * @param operatorName
     */
    public void financialGrant(SalaryPeriods periods, SalaryGrant salaryGrant, Member member, MemberInfo memberInfo, String operatorName) {
        String sb = new StringBuilder()
                .append(periods.getStartDate())
                .append("~")
                .append(periods.getEndDate())
                .append("期工资派发 ")
                .append(salaryGrant.getSalaryAmount())
                .append("元")
                .toString();

        // 账户资金锁
        String lockKey =
                MessageFormat.format(
                        MemberServiceKeyConstant.MEMBER_FINANCIAL_LOCK, member.getAccount());

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
            memberBill.setTranType(MemberBillTransTypeEnum.SALARY_AMOUNT.getCode());
            memberBill.setOrderNo(String.valueOf(IdGeneratorSnowflake.getInstance().nextId()));
            memberBill.setAmount(salaryGrant.getSalaryAmount());
            memberBill.setBalance(memberInfo.getBalance());
            memberBill.setOperator(operatorName);
            memberBill.setRemark(sb);
            memberBill.setContent(sb);
            memberBillService.save(memberBill);
            // 计算变更后余额
            BigDecimal newBalance = memberInfo.getBalance().add(salaryGrant.getSalaryAmount());
            if (newBalance.compareTo(BigDecimal.ZERO) <= 0) {
                return;
            }
            MemberInfo entity = MemberInfo.builder()
                    .memberId(member.getId())
                    .balance(newBalance)
                    .version(memberInfo.getVersion())
                    .build();
            boolean b = memberInfoService.updateById(entity);
            if (!b) {
                log.error("会员{}期数工资派发钱包余额变更失败！",member.getAccount());
            }
        } catch (Exception e) {
            log.error(
                    MessageFormat.format("会员{}工资账变, 失败原因：{}", member.getAccount(), e));
            // 释放资金锁
            distributedLocker.unlock(lockKey);
        } finally {
            // 释放资金锁
            distributedLocker.unlock(lockKey);
        }
        // 6.5 通知 发个人消息
        MessageInfoAddDTO dto = new MessageInfoAddDTO();
        Message message = messageInfoConvert.toEntity(dto);
        message.setTitle("层层代工资派发");
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

        MessageDistribute messageDistribute = new MessageDistribute();
        messageDistribute.setMessageId(message.getId());
        messageDistribute.setUserId(member.getId());
        messageDistribute.setUserAccount(member.getAccount());
        messageDistribute.setRechargeLevel(member.getUserLevel());
        messageDistribute.setVipLevel(
                memberInfoService.getById(member.getId()).getVipLevel());
        messageDistribute.setReadStatus(TrueFalse.FALSE.getValue());
        messageDistribute.setCreateBy(operatorName);
        messageDistributeService.save(messageDistribute);
    }

    /**
     * 工资期数回收
     * @param periods
     * @param operatorName
     */
    public void periodsRecycle(SalaryPeriods periods, String operatorName){
        // 根据期数获取到 已派发 状态的 grant数据
        LambdaQueryWrapper<SalaryGrant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SalaryGrant::getPeriodsId, periods.getId());
        queryWrapper.eq(SalaryGrant::getGrantStatus, DivideStatusEnum.SALARY_GRANT_STATUS_GRANTED.getValue());
        List<SalaryGrant> salaryGrants = salaryGrantMapper.selectList(queryWrapper);
        if (CollectionUtil.isNotEmpty(salaryGrants)) {
            for (SalaryGrant grant : salaryGrants) {
                SalaryGrantDTO dto = SalaryGrantDTO.builder()
                        .id(grant.getId())
                        .grantStatus(DivideStatusEnum.SALARY_GRANT_STATUS_RECYCLE.getValue())
                        .build();
                SalaryGrant editObj = salaryGrantConvert.toEntity(dto);
                int i = salaryGrantMapper.updateById(editObj);
                if (i < 1) {
                    continue;
                }
                if (grant.getAccount().equalsIgnoreCase(SystemConstant.DEFAULT_WEB_ROOT) ||
                        grant.getAccount().equalsIgnoreCase(SystemConstant.DEFAULT_TEST_ROOT) ||
                        grant.getAccount().equalsIgnoreCase(SystemConstant.DEFAULT_WAP_ROOT)) {
                    continue;
                }
                // 如果 真实分红金额 小于等于0 不用发生账变 资金变动
                if (grant.getSalaryAmount().compareTo(BigDecimal.ZERO) <= 0) {
                    continue;
                }

                // 校验会员账户状态
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
                    // todo 资金变动
                    this.financialRecycle(periods, grant, member, memberInfo, operatorName);
                } catch (Exception e) {
                    log.error("会员{}工资期数{}派发资金处理失败！", member.getAccount(), periods.getId());
                    continue;
                }
            }
        }
    }

    public void financialRecycle(SalaryPeriods periods, SalaryGrant salaryGrant, Member member, MemberInfo memberInfo, String operatorName){
        String sb = new StringBuilder()
                .append(periods.getStartDate())
                .append("~")
                .append(periods.getEndDate())
                .append("期工资回收 ")
                .append(salaryGrant.getSalaryAmount().negate())
                .append("元")
                .toString();

        // 账户资金锁
        String lockKey =
                MessageFormat.format(
                        MemberServiceKeyConstant.MEMBER_FINANCIAL_LOCK, member.getAccount());

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
            memberBill.setTranType(MemberBillTransTypeEnum.SALARY_AMOUNT_BACK.getCode());
            memberBill.setOrderNo(String.valueOf(IdGeneratorSnowflake.getInstance().nextId()));
            memberBill.setAmount(salaryGrant.getSalaryAmount().negate());
            memberBill.setBalance(memberInfo.getBalance());
            memberBill.setRemark(sb);
            memberBill.setContent(sb);
            memberBill.setOperator(operatorName);
            memberBillService.save(memberBill);
        } catch (Exception e) {
            log.error(
                    MessageFormat.format("会员：{}，工资回收, 失败原因：{}", member.getAccount(), e));
            // 释放资金锁
            distributedLocker.unlock(lockKey);
        } finally {
            // 释放资金锁
            distributedLocker.unlock(lockKey);
        }

        // 计算变更后余额
        BigDecimal newBalance = memberInfo.getBalance().add(salaryGrant.getSalaryAmount().negate());
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            return;
        }
        MemberInfo entity = MemberInfo.builder()
                .memberId(member.getId())
                .balance(newBalance)
                .version(memberInfo.getVersion())
                .build();
        boolean b = memberInfoService.updateById(entity);
        if (!b) {
            log.error("会员{}工资回收钱包余额变更失败！",member.getAccount());
        }
    }
}
