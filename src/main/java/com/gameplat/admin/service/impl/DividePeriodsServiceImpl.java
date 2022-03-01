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
import com.gameplat.admin.constant.SystemConstant;
import com.gameplat.admin.constant.TrueFalse;
import com.gameplat.admin.convert.DivideDetailConvert;
import com.gameplat.admin.convert.DividePeriodsConvert;
import com.gameplat.admin.convert.DivideSummaryConvert;
import com.gameplat.admin.enums.BlacklistConstant;
import com.gameplat.admin.mapper.*;
import com.gameplat.admin.model.domain.BizBlacklist;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.domain.proxy.*;
import com.gameplat.admin.model.dto.DivideDetailDto;
import com.gameplat.admin.model.dto.DividePeriodsDTO;
import com.gameplat.admin.model.dto.DividePeriodsQueryDTO;
import com.gameplat.admin.model.dto.DivideSummaryDto;
import com.gameplat.admin.model.vo.DivideGameReportVO;
import com.gameplat.admin.model.vo.DividePeriodsVO;
import com.gameplat.admin.model.vo.FissionConfigLevelVo;
import com.gameplat.admin.model.vo.GameDivideVo;
import com.gameplat.admin.service.*;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.enums.UserTypes;
import com.gameplat.common.lang.Assert;
import com.gameplat.redis.redisson.DistributedLocker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@SuppressWarnings("all")
@Slf4j
public class DividePeriodsServiceImpl extends ServiceImpl<DividePeriodsMapper, DividePeriods> implements DividePeriodsService {
    @Autowired
    private DividePeriodsMapper dividePeriodsMapper;
    @Autowired
    private DividePeriodsConvert periodsConvert;
    @Autowired
    private RecommendConfigService recommendConfigService;
    @Autowired
    private DivideDetailMapper detailMapper;
    @Autowired
    private DivideSummaryMapper summaryMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private DistributedLocker distributedLocker;
    @Autowired
    private GameBetDailyReportMapper betDailyReportMapper;
    @Autowired
    private BizBlacklistMapper blacklistMapper;
    @Autowired
    private MemberService memberServicel;
    @Autowired
    private DivideLayerConfigService layerConfigService;
    @Autowired
    private DivideFixConfigService fixConfigService;
    @Autowired
    private DivideDetailConvert detailConvert;
    @Autowired
    private DivideFissionConfigService fissionConfigService;
    @Autowired
    private DivideSummaryConvert summaryConvert;

    @Override
    public IPage<DividePeriodsVO> queryPage(PageDTO<DividePeriods> page, DividePeriodsQueryDTO dto) {
        QueryWrapper<DividePeriods> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("create_time");
        return dividePeriodsMapper.selectPage(page, queryWrapper).convert(periodsConvert::toVo);
    }

    /**
     * 期数新增
     * @param dto
     */
    @Override
    @SentinelResource("add")
    public void add(DividePeriodsDTO dto) {
        Assert.isTrue(StrUtil.isNotBlank(dto.getStartDate()),"期数起始时间不能为空！");
        Assert.isTrue(StrUtil.isNotBlank(dto.getEndDate()),"期数截止时间不能为空！");
        DividePeriods saveObj = periodsConvert.toEntity(dto);
        saveObj.setStartDate(DateUtil.parse(saveObj.getStartDate()).toDateStr());
        saveObj.setEndDate(DateUtil.parse(saveObj.getEndDate()).toDateStr());
        RecommendConfig recommendConfig = recommendConfigService.getRecommendConfig();
        Integer divideModel = recommendConfig.getDivideModel();
        divideModel = ObjectUtil.defaultIfNull(divideModel, 3);
        saveObj.setDivideType(divideModel);
        Assert.isTrue(this.save(saveObj),"添加失败！");
    }

    /**
     * 期数编辑
     * @param dto
     */
    @Override
    @SentinelResource("edit")
    public void edit(DividePeriodsDTO dto) {
        Assert.isTrue(StrUtil.isNotBlank(dto.getStartDate()),"期数起始时间不能为空！");
        Assert.isTrue(StrUtil.isNotBlank(dto.getEndDate()),"期数截止时间不能为空！");
        DividePeriods editObj = periodsConvert.toEntity(dto);
        editObj.setStartDate(DateUtil.parse(dto.getStartDate()).toDateStr());
        editObj.setEndDate(DateUtil.parse(dto.getEndDate()).toDateStr());
        Assert.isTrue(this.updateById(editObj),"编辑失败！");
    }

    /**
     * 期数删除
     * @param ids
     */
    @Override
    @Transactional
    @SentinelResource("delete")
    public void delete(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids),"参数为空！");
        String[] idArr = ids.split(",");
        for (String id: idArr) {
            DividePeriods dividePeriods = dividePeriodsMapper.selectById(id);
            if (dividePeriods.getGrantStatus() == 2) {
                throw new ServiceException("已派发不能删除！");
            }
            QueryWrapper<DivideDetail> deleteDetailWrapper = new QueryWrapper();
            deleteDetailWrapper.eq("periods_id", Long.valueOf(id));
            detailMapper.delete(deleteDetailWrapper);

            QueryWrapper<DivideSummary> deleteSummaryWrapper = new QueryWrapper();
            deleteSummaryWrapper.eq("periods_id", Long.valueOf(id));
            summaryMapper.delete(deleteSummaryWrapper);

            Assert.isTrue(this.removeById(id),("删除期数:" + id + "失败！"));
        }
    }

    /**
     * 期数结算
     * @param dto
     */
    @Override
    public void settle(DividePeriodsDTO dto) {
        Assert.isTrue(dto.getId() != null,"期数主键ID参数缺失！");
        // 外层先锁住  防止重复点击
        String key = "divide:settle:" + dto.getId();
        Boolean isLock = redisTemplate.hasKey(key);
        Assert.isTrue(!isLock,"您的操作过于频繁！请稍后重试！");
        distributedLocker.lock(key);
        try {
            // 结算 分红汇总，分红详情
            this.settleOther(dto.getId());

            Assert.isTrue(this.lambdaUpdate()
                    .set(DividePeriods::getSettleStatus,2)
                    .eq(DividePeriods::getId, dto.getId())
                    .update(new DividePeriods()),"修改期数结算状态失败！");
        } catch (Exception e) {
            throw new ServiceException("期数结算失败！");
        } finally {
            distributedLocker.unlock(key);
        }
    }

    @Async
    public void settleOther(Long periodsId) {
        DividePeriods periods = this.getById(periodsId);
        if (BeanUtil.isEmpty(periods) || periods.getGrantStatus() == 2) {
            return;
        }
        // 删除分红详情
        QueryWrapper<DivideDetail> deleteDetailWrapper = new QueryWrapper();
        deleteDetailWrapper.eq("periods_id", periodsId);
        detailMapper.delete(deleteDetailWrapper);
        // 删除分红汇总
        QueryWrapper<DivideSummary> deleteSummaryWrapper = new QueryWrapper();
        deleteSummaryWrapper.eq("periods_id", periodsId);
        summaryMapper.delete(deleteSummaryWrapper);

        // 分红模式 1 固定  2 裂变  3 层层代 4 平级
        Integer divideModel = periods.getDivideType();
        RecommendConfig recommendConfig = recommendConfigService.getRecommendConfig();
        Integer isIncludeAgent = recommendConfig.getIsIncludeAgent();

        // 获取用户游戏分组统计数据
        List<DivideGameReportVO> periodsGameReport
                = betDailyReportMapper.findReportForDivide(periods.getStartDate(), periods.getEndDate(), isIncludeAgent);
        // 过滤下无代理路径的脏数据
        periodsGameReport = periodsGameReport.stream().filter(
                item -> StrUtil.isNotBlank(item.getUserPaths())
        ).collect(Collectors.toList());
        // 获取业务 分红黑名单 集合
        QueryWrapper<BizBlacklist> queryBizWrapper = new QueryWrapper();
        queryBizWrapper.like("types", BlacklistConstant.BizBlacklistType.DL_RATIO.getValue());
        List<BizBlacklist> bizBlacklists = blacklistMapper.selectList(queryBizWrapper);
        // 会员账号业务黑名单
        List<BizBlacklist> accountBlackList = bizBlacklists.stream().filter(item -> item.getTargetType() == TrueFalse.FALSE.getValue()).collect(Collectors.toList());
        // 用户层级业务黑名单
        List<BizBlacklist> userLevelBlackList = bizBlacklists.stream().filter(item -> item.getTargetType() == TrueFalse.TRUE.getValue()).collect(Collectors.toList());
        List<String> accountBlacks = new ArrayList<>();
        List<String> userLevelBlacks = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(accountBlackList)) {
            accountBlacks = accountBlackList.stream().map(BizBlacklist::getTarget).collect(Collectors.toList());
        }
        if (CollectionUtil.isNotEmpty(userLevelBlackList)) {
            userLevelBlacks = userLevelBlackList.stream().map(BizBlacklist::getTarget).collect(Collectors.toList());
        }

        List<DivideDetailDto> detailList = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(periodsGameReport)) {
            for (DivideGameReportVO gameReportVO : periodsGameReport) {
                String userType = gameReportVO.getUserType();//M 会员  A 代理
                String account = gameReportVO.getAccount();
                Integer agentLevel = gameReportVO.getAgentLevel();
                String userPaths = gameReportVO.getUserPaths();
                if (StrUtil.isBlank(userPaths)){
                    continue;
                }
                userPaths = getFinalAgentPath(userPaths);
                // 会员不参与分红
                if (userType.equalsIgnoreCase(UserTypes.MEMBER.value())) {
                    if (userPaths.endsWith("/"+account)) {
                        int j = userPaths.lastIndexOf("/" + account);
                        userPaths = j >= 0 ? userPaths.substring(0, j) : userPaths;
                    }
                }
                if (StrUtil.isBlank(userPaths)) {
                    continue;
                }
                userPaths = userPaths.startsWith(SystemConstant.DEFAULT_WEB_ROOT + "/") ? userPaths.replaceFirst(SystemConstant.DEFAULT_WEB_ROOT + "/", "") : userPaths;
                userPaths = userPaths.startsWith(SystemConstant.DEFAULT_WAP_ROOT + "/") ? userPaths.replaceFirst(SystemConstant.DEFAULT_WAP_ROOT + "/", "") : userPaths;
                userPaths = userPaths.startsWith(SystemConstant.DEFAULT_TEST_ROOT + "/") ? userPaths.replaceFirst(SystemConstant.DEFAULT_TEST_ROOT + "/", "") : userPaths;
                if (divideModel == 1) {//固定模式
                    detailList.addAll(fixSettle(periodsId, userPaths, gameReportVO, accountBlacks, userLevelBlacks));
                } else if (divideModel == 2) {//裂变
                    detailList.addAll(fissionSettle(periodsId, userPaths, gameReportVO, accountBlacks, userLevelBlacks));
                } else if (divideModel == 3) {//层层代
                    detailList.addAll(layerSettle(periodsId, userPaths, gameReportVO, accountBlacks, userLevelBlacks));
                }
            }

            // 处理 分红汇总数据
            if (CollectionUtil.isNotEmpty(detailList)) {
                //按proxyId 分红代理id分组
                Map<String, List<DivideDetailDto>> groupByPname = detailList.stream().collect(Collectors.groupingBy(DivideDetailDto::getProxyName));
                // 求稳  再过滤掉webRoot
                groupByPname.remove(SystemConstant.DEFAULT_WEB_ROOT);
                groupByPname.remove(SystemConstant.DEFAULT_WAP_ROOT);
                groupByPname.remove(SystemConstant.DEFAULT_TEST_ROOT);
                if (BeanUtil.isNotEmpty(groupByPname)) {
                    saveSummaryForSettle(groupByPname, periodsId, recommendConfig.getIsGrand());
                }
            }
        }
    }

    /**
     * 固定比例模式结算
     * @param periodsId
     * @param userPaths
     * @param gameReportVO
     * @param accountBlacks
     * @param userLevelBlacks
     * @return
     */
    public List<DivideDetailDto> fixSettle(Long periodsId, String userPaths, DivideGameReportVO gameReportVO,
                                        List<String> accountBlacks, List<String> userLevelBlacks) {
        List<DivideDetailDto> returnList = new ArrayList<>();
        String[] nameStr = userPaths.split("/");
        if (ArrayUtil.isEmpty(nameStr)) {
            return new ArrayList<>();
        }
        String tmpName = nameStr[0];
        // 顶级代理
        Member superMember =
                memberServicel.getByAccount(tmpName).orElseThrow(() -> new ServiceException(tmpName + "账号不存在!"));
        GameDivideVo configByFirstCode = fixConfigService.getConfigByGameCode(superMember.getAccount(), gameReportVO.getGameKind());
        if (BeanUtil.isEmpty(configByFirstCode)) {
            return new ArrayList<>();
        }
        if (configByFirstCode.getAmountRatio().compareTo(BigDecimal.ZERO) <= 0 || configByFirstCode.getDivideRatio().compareTo(BigDecimal.ZERO) <= 0) {
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
                    memberServicel.getAgentByAccount(username).orElseThrow(() -> new ServiceException(username + "代理账号不存在!"));
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
            if (currentMember.getAgentLevel() >= userLevelNum) {
                continue;
            }

            DivideDetailDto saveDetailDto = DivideDetailDto.builder()
                    .periodsId(periodsId)
                    .userId(gameReportVO.getMemberId())
                    .userName(gameReportVO.getAccount())
                    .proxyId(gameReportVO.getSuperId())
                    .proxyName(gameReportVO.getSuperAccount())
                    .agentLevel(currentMember.getAgentLevel())
                    .superPath(currentMember.getSuperPath())
                    .superId(currentMember.getParentId())
                    .superName(currentMember.getParentName())
                    .liveCode(gameReportVO.getGameType())
                    .code(gameReportVO.getGameKind())
                    .validAmount(gameReportVO.getValidAmount())
                    .winAmount(gameReportVO.getWinAmount().negate())
                    .settleType(settleType)
                    .amountRatio(amountRatio)
                    .build();
            // 分红比例的（源用户的层级 - 分红代理的层级)次幂
            BigDecimal pow = divideRatio.pow((userLevelNum - currentMember.getAgentLevel()));
            saveDetailDto.setDivideRatio(pow);//代理分红比例
            // 分红基数金额
            BigDecimal baseAmount = settleType == 1 ? gameReportVO.getWinAmount().negate() : gameReportVO.getValidAmount();
            //分红金额
            saveDetailDto.setDivideAmount(
                    baseAmount.multiply(amountRatio).multiply(pow)
            );
            //分红公式
            saveDetailDto.setDivideFormula(
                    baseAmount.toString() + "x" + amountRatio.multiply(BigDecimal.valueOf(100)).toString() + "%x" + divideRatio.multiply(BigDecimal.valueOf(100)).toString() + "%^" +
                            (userLevelNum - currentMember.getAgentLevel())
            );
            // 添加分红详情
            DivideDetail detail = detailConvert.toEntity(saveDetailDto);
            int insertCount = detailMapper.insert(detail);
            if (insertCount > 0) {
                returnList.add(saveDetailDto);
            }
        }
        return returnList;
    }

    /**
     * 裂变模式结算
     * @param periodsId
     * @param userPaths
     * @param gameReportVO
     * @param accountBlacks
     * @param userLevelBlacks
     * @return
     */
    public List<DivideDetailDto> fissionSettle(Long periodsId, String userPaths, DivideGameReportVO gameReportVO,
                                            List<String> accountBlacks, List<String> userLevelBlacks){
        List<DivideDetailDto> returnList = new ArrayList<>();
        String[] nameStr = userPaths.split("/");
        if (ArrayUtil.isEmpty(nameStr)) {
            return new ArrayList<>();
        }
        String tmpName = nameStr[0];
        // 顶级代理
        Member superMember =
                memberServicel.getByAccount(tmpName).orElseThrow(() -> new ServiceException(tmpName + "账号不存在!"));
        GameDivideVo fissionConfig = fissionConfigService.getConfigByFirstCode(superMember.getAccount(), gameReportVO.getGameKind());
        if (BeanUtil.isEmpty(fissionConfig)) {
            return new ArrayList<>();
        }
        if (fissionConfig.getAmountRatio().compareTo(BigDecimal.ZERO) <= 0) {
            return new ArrayList<>();
        }
        DivideFissionConfig tmpFissionConfig = fissionConfigService.getByAccount(superMember.getAccount());
        if (BeanUtil.isEmpty(tmpFissionConfig)) {
            return new ArrayList<>();
        }
        String recycleConfig = tmpFissionConfig.getRecycleConfig();
        List<FissionConfigLevelVo> fissionConfigLevelVos = new ArrayList<>();
        if (StrUtil.isNotBlank(recycleConfig)) {
            fissionConfigLevelVos = JSONUtil.toList(JSONUtil.parseArray(recycleConfig), FissionConfigLevelVo.class);
        }
        BigDecimal outRecycleConfig = tmpFissionConfig.getRecycleOutConfig();
        if (CollectionUtil.isEmpty(fissionConfigLevelVos) && outRecycleConfig.compareTo(BigDecimal.ZERO) <= 0) {
            return new ArrayList<>();
        }

        Integer settleType = fissionConfig.getSettleType();
        BigDecimal amountRatio = fissionConfig.getAmountRatio().divide(new BigDecimal("100"));
        Map<Integer, List<FissionConfigLevelVo>> levelDivideRatioMap = fissionConfigLevelVos.stream().collect(Collectors.groupingBy(FissionConfigLevelVo::getLevel));
        BigDecimal divideLevelRatio = BigDecimal.ZERO;
        Integer userLevelNum = gameReportVO.getAgentLevel();
        for (int i = 0; i < nameStr.length; i++) {
            // 过滤黑名单
            String username = nameStr[i];
            // 顶级代理
            Member currentMember =
                    memberServicel.getAgentByAccount(username).orElseThrow(() -> new ServiceException(username + "代理账号不存在!"));
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

            Integer curLevelNum = currentMember.getAgentLevel();// 享分红代理
            Integer subLevelNum = userLevelNum - curLevelNum;// 等级差
            if (subLevelNum <= 0) {
                continue;
            }

            if (CollectionUtil.isEmpty(fissionConfigLevelVos) || (subLevelNum > fissionConfigLevelVos.size())) {
                divideLevelRatio = outRecycleConfig.divide(new BigDecimal("100"));
            } else {
                // 当前代理的
                List<FissionConfigLevelVo> tmpLevelVo = levelDivideRatioMap.get(subLevelNum);
                if (CollectionUtil.isEmpty(tmpLevelVo)) {
                    if (outRecycleConfig.compareTo(BigDecimal.ZERO) <= 0) {
                        continue;
                    }
                    divideLevelRatio = outRecycleConfig.divide(new BigDecimal("100"));
                } else {
                    FissionConfigLevelVo fissionConfigLevelVo = tmpLevelVo.get(0);
                    if (fissionConfigLevelVo.getLevelDivideRatio().compareTo(BigDecimal.ZERO) <= 0) {
                        continue;
                    }
                    divideLevelRatio = fissionConfigLevelVo.getLevelDivideRatio()
                            .divide(new BigDecimal("100"));
                }
            }

            if (divideLevelRatio.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            DivideDetailDto saveDetailDto = DivideDetailDto.builder()
                    .periodsId(periodsId)
                    .userId(gameReportVO.getMemberId())//分红来源用户Id
                    .userName(gameReportVO.getAccount())//分红来源用户名称
                    .proxyId(gameReportVO.getSuperId())//分红代理的ID
                    .proxyName(gameReportVO.getSuperAccount())//分红代理的名称
                    .agentLevel(currentMember.getAgentLevel())//分红代理的级别
                    .superPath(currentMember.getSuperPath())//分红代理的代理路径
                    .superId(currentMember.getParentId())
                    .superName(currentMember.getParentName())
                    .liveCode(gameReportVO.getGameType())//游戏大类编码
                    .code(gameReportVO.getGameKind())//一级游戏编码
                    .validAmount(gameReportVO.getValidAmount())//有效投注
                    .winAmount(gameReportVO.getWinAmount().negate())//输赢金额 游戏报表中的输赢金额是会员的角度 这里需要取反
                    .settleType(settleType)
                    .amountRatio(amountRatio)
                    .divideRatio(divideLevelRatio)
                    .build();

            BigDecimal baseAmount = settleType == 1 ?
                    gameReportVO.getWinAmount().negate() : gameReportVO.getValidAmount();

            //分红金额
            saveDetailDto.setDivideAmount(
                    baseAmount.multiply(amountRatio).multiply(divideLevelRatio)
            );

            //分红公式
            saveDetailDto.setDivideFormula(
                    baseAmount.toString() + "x" + amountRatio.multiply(BigDecimal.valueOf(100)).toString() + "%x" + divideLevelRatio.multiply(BigDecimal.valueOf(100)).toString() + "%"
            );

            // 添加分红详情
            DivideDetail detail = detailConvert.toEntity(saveDetailDto);
            int insertCount = detailMapper.insert(detail);
            if (insertCount > 0) {
                returnList.add(saveDetailDto);
            }
        }


        return returnList;
    }

    /**
     * 层层代模式结算
     * @param periodsId
     * @param userPaths
     * @param gameReportVO
     * @param accountBlacks
     * @param userLevelBlacks
     * @return
     */
    public List<DivideDetailDto> layerSettle(Long periodsId, String userPaths, DivideGameReportVO gameReportVO,
                                          List<String> accountBlacks, List<String> userLevelBlacks){
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
                    memberServicel.getAgentByAccount(username).orElseThrow(() -> new ServiceException(username + "代理账号不存在!"));
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
            Integer settleType = 0;
            BigDecimal amountRatio = BigDecimal.ZERO;
            BigDecimal divideRatio = BigDecimal.ZERO;
            GameDivideVo gameDivideVo = new GameDivideVo();
            // 最后一个 直接取自己的配置
            if (i == (nameStr.length - 1)) {
                gameDivideVo = layerConfigService.getConfigByGameCode(nameStr[i], gameReportVO.getGameKind());
                if (BeanUtil.isEmpty(gameDivideVo)) {
                    continue;
                }
                if (gameDivideVo.getDivideRatio().compareTo(BigDecimal.ZERO) <= 0) {
                    continue;
                }
                divideRatio = gameDivideVo.getDivideRatio().divide(new BigDecimal("100"));
            } else {// 取这条线 它直接下级的配置
                gameDivideVo = layerConfigService.getConfigByGameCode(nameStr[i + 1], gameReportVO.getGameKind());
                if (BeanUtil.isEmpty(gameDivideVo)) {
                    continue;
                }
                if (gameDivideVo.getParentDivideRatio().compareTo(BigDecimal.ZERO) <= 0) {
                    continue;
                }
                divideRatio = gameDivideVo.getParentDivideRatio().divide(new BigDecimal("100"));
            }

            if (gameDivideVo.getAmountRatio().compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            amountRatio = gameDivideVo.getAmountRatio().divide(new BigDecimal("100"));
            // 结算方式 1 输赢  2  投注额
            settleType = gameDivideVo.getSettleType();

            DivideDetailDto saveDetailDto = DivideDetailDto.builder()
                    .periodsId(periodsId)
                    .userId(gameReportVO.getMemberId())//分红来源用户Id
                    .userName(gameReportVO.getAccount())//分红来源用户名称
                    .proxyId(gameReportVO.getSuperId())//分红代理的ID
                    .proxyName(gameReportVO.getSuperAccount())//分红代理的名称
                    .agentLevel(currentMember.getAgentLevel())//分红代理的级别
                    .superPath(currentMember.getSuperPath())//分红代理的代理路径
                    .superId(currentMember.getParentId())
                    .superName(currentMember.getParentName())
                    .liveCode(gameReportVO.getGameType())//游戏大类编码
                    .code(gameReportVO.getGameKind())//一级游戏编码
                    .validAmount(gameReportVO.getValidAmount())//有效投注
                    .winAmount(gameReportVO.getWinAmount().negate())//输赢金额 游戏报表中的输赢金额是会员的角度 这里需要取反
                    .settleType(settleType)
                    .amountRatio(amountRatio)
                    .divideRatio(divideRatio)
                    .build();
            //基础金额
            BigDecimal baseAmount = settleType == 1 ?
                    gameReportVO.getWinAmount().negate() : gameReportVO.getValidAmount();
            //分红金额
            saveDetailDto.setDivideAmount(
                    baseAmount.multiply(amountRatio).multiply(divideRatio)
            );
            //分红公式
            saveDetailDto.setDivideFormula(
                    baseAmount.toString() + "x" + amountRatio.multiply(BigDecimal.valueOf(100)).toString() + "%x" + divideRatio.multiply(BigDecimal.valueOf(100)).toString() + "%"
            );

            // 添加分红详情
            DivideDetail detail = detailConvert.toEntity(saveDetailDto);
            int insertCount = detailMapper.insert(detail);
            if (insertCount > 0) {
                returnList.add(saveDetailDto);
            }
        }

        return returnList;
    }

    /**
     * 期数结算时 保存分红汇总
     * @param groupByPname
     * @param periodsId
     * @param isGrand
     */
    public void saveSummaryForSettle(Map<String, List<DivideDetailDto>> groupByPname, Long periodsId, Integer isGrand){
        for (Map.Entry<String, List<DivideDetailDto>> map : groupByPname.entrySet()) {
            List<DivideDetailDto> groupList = map.getValue();
            DivideDetailDto divideDetail = groupList.get(0);
            DivideSummaryDto summaryDto = DivideSummaryDto.builder()
                    .periodsId(periodsId)
                    .userId(divideDetail.getProxyId())
                    .account(divideDetail.getProxyName())
                    .agentLevel(divideDetail.getAgentLevel())
                    .agentPath(divideDetail.getSuperPath())
                    .parentId(divideDetail.getSuperId())
                    .parentName(divideDetail.getSuperName())
                    .status(TrueFalse.TRUE.getValue())
                    .build();
            // 将有效投注求和
            BigDecimal sumValidAmount = groupList.stream().map(DivideDetailDto::getValidAmount).reduce(BigDecimal::add).get();
            // 将输赢金额求和
            BigDecimal sumWinAmount = groupList.stream().map(DivideDetailDto::getWinAmount).reduce(BigDecimal::add).get();
            // 将分红金额求和
            BigDecimal sumDivideAmount = groupList.stream().map(DivideDetailDto::getDivideAmount).reduce(BigDecimal::add).get();
            summaryDto.setValidAmount(sumValidAmount);
            summaryDto.setWinAmount(sumWinAmount);
            // 如果开启了  负数分红金额累计至下一期则 需要取到上一期的真实分红金额
            if (isGrand == 1) {
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
                    if (lastRealDivideAmount.compareTo(BigDecimal.ZERO) >= 0) {
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
            if (insertSummaryCount < 1) {
                log.info(divideSummary.getAccount() + "保存分红汇总失败！");
            }
        }
    }

    /**
     * 去掉代理路径首尾的/
     * @param oldPath
     * @return
     */
    public String getFinalAgentPath(String oldPath){
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
