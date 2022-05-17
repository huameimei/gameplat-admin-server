package com.gameplat.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberGrowthStatisConvert;
import com.gameplat.admin.enums.PushMessageEnum;
import com.gameplat.admin.mapper.MemberGrowthStatisMapper;
import com.gameplat.admin.mapper.MessageMapper;
import com.gameplat.admin.model.bean.ActivityMemberInfo;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.MemberGrowthLevelVO;
import com.gameplat.admin.model.vo.MemberGrowthStatisVO;
import com.gameplat.admin.service.*;
import com.gameplat.base.common.context.GlobalContextHolder;
import com.gameplat.base.common.enums.EnableEnum;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.enums.BooleanEnum;
import com.gameplat.common.enums.GrowthChangeEnum;
import com.gameplat.common.enums.TranTypes;
import com.gameplat.model.entity.ValidWithdraw;
import com.gameplat.model.entity.member.*;
import com.gameplat.model.entity.message.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author lily
 * @description vip等级汇总业务处理层
 * @date 2021/11/24
 */
@Service
@Slf4j
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberGrowthStatisServiceImpl
        extends ServiceImpl<MemberGrowthStatisMapper, MemberGrowthStatis>
        implements MemberGrowthStatisService {

    @Autowired
    private MemberGrowthStatisConvert statisConvert;

    @Resource(name = "memberGrowthLevelServiceImpl")
    @Lazy
    private MemberGrowthLevelService growthLevelService;

    @Autowired private MemberGrowthStatisMapper memberGrowthStatisMapper;

    @Autowired
    private MemberGrowthConfigService memberGrowthConfigService;

    @Resource(name = "memberGrowthRecordServiceImpl")
    @Lazy
    private MemberGrowthRecordService memberGrowthRecordService;

    @Autowired
    private MemberGoldCoinRecordService memberGoldCoinRecordService;

    @Autowired
    private MemberBlackListService memberBlackListService;

    @Autowired
    private MemberWealRewordService memberWealRewordService;

    @Autowired
    private ValidWithdrawService validWithdrawService;

    @Autowired
    private MemberBillService memberBillService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private MemberInfoService memberInfoService;

    public static final Object lockHelper = new Object();

    public static final String kindName = "{\"en-US\": \"platform\", \"in-ID\": \"peron\", \"th-TH\": \"แพลตฟอร์ม\", \"vi-VN\": \"nền tảng\", \"zh-CN\": \"平台\"}";

    @Override
    public IPage<MemberGrowthStatisVO> findStatisList(
            PageDTO<MemberGrowthStatis> page, MemberGrowthStatisDTO dto) {
        return this.lambdaQuery()
                .like(
                        ObjectUtils.isNotEmpty(dto.getAccount()),
                        MemberGrowthStatis::getAccount,
                        dto.getAccount())
                .ge(
                        ObjectUtils.isNotEmpty(dto.getStartTime()),
                        MemberGrowthStatis::getCreateTime,
                        dto.getStartTime())
                .le(
                        ObjectUtils.isNotEmpty(dto.getEndTime()),
                        MemberGrowthStatis::getCreateTime,
                        dto.getEndTime())
                .orderByDesc(MemberGrowthStatis::getCreateTime)
                .page(page)
                .convert(statisConvert::toVo);
    }

    @Override
    public List<MemberGrowthStatis> findList(MemberGrowthStatisDTO dto) {
        return this.lambdaQuery()
                .like(
                        ObjectUtils.isNotEmpty(dto.getAccount()),
                        MemberGrowthStatis::getAccount,
                        dto.getAccount())
                .ge(
                        ObjectUtils.isNotEmpty(dto.getStartTime()),
                        MemberGrowthStatis::getCreateTime,
                        dto.getStartTime())
                .le(
                        ObjectUtils.isNotEmpty(dto.getEndTime()),
                        MemberGrowthStatis::getCreateTime,
                        dto.getEndTime())
                .orderByDesc(MemberGrowthStatis::getCreateTime)
                .list();
    }

    /**
     * 处理随成长值 升级发消息相关操作
     */
    @Override
    public Integer dealUpLevel(Long afterGrowth, MemberGrowthConfig memberGrowthConfig) {
    // 1.先获取所有成长值等级
    Integer limitLevel = Optional.ofNullable(memberGrowthConfig.getLimitLevel()).orElse(50);

        List<MemberGrowthLevelVO> levels = growthLevelService.findList(limitLevel);

        MemberGrowthLevelVO maxGrowthLevel = levels.get(levels.size() - 1);

        // 如果比最大等级所需升级成长值还要大  则直接返回最大等级
        if (afterGrowth >= maxGrowthLevel.getGrowth()) {
            return maxGrowthLevel.getLevel();
        }
        for (MemberGrowthLevelVO level : levels) {
            if (afterGrowth < level.getGrowth()) {
                return level.getLevel();
            }
        }
        throw new ServiceException("计算成长等级失败！");
    }

    @Override
    public void insertOrUpdate(MemberGrowthStatis userGrowthStatis) {
        memberGrowthStatisMapper.insertOrUpdate(userGrowthStatis);
    }

    @Override
    public MemberGrowthStatis findOrInsert(Long memberId, String account) {
        MemberGrowthStatis find = this.lambdaQuery()
                .eq(MemberGrowthStatis::getMemberId, memberId)
                .one();
        if (BeanUtil.isEmpty(find)) {
            find = new MemberGrowthStatis();
            find.setMemberId(memberId);
            find.setAccount(account);
            find.setCreateBy("系统操作员");
      // 其余字段均默认为0
      int i = memberGrowthStatisMapper.insertOrUpdate(find);
            if (i <= 0) {
                throw new ServiceException("添加或修改会员成长值汇总失败！");
            }
            find = this.lambdaQuery()
                    .eq(MemberGrowthStatis::getMemberId, memberId)
                    .one();
        }
        return find;
    }

    @Override
    public List<ActivityMemberInfo> findActivityMemberInfo(Map map) {
    return memberGrowthStatisMapper.findActivityMemberInfo(map);
    }

    /**
     * 成长值变动
     */
//    @Transactional
    @Override
    public void changeGrowth(MemberGrowthChangeDto dto) {

    // 判断是否开启了VIP
    // 2.获取成长值配置
    MemberGrowthConfig growthConfig = memberGrowthConfigService.getOneConfig();

        if (growthConfig.getIsEnableVip() == 0) {
            log.info("成长值计算失败-未开启VIP");
            return;
        }
        Long memberId = dto.getUserId();
        Member member = memberService.getById(memberId);
        if (BeanUtil.isEmpty(member)) {
            log.info("VIP 成长值变动 会员不存在");
            return;
        }
        log.info("变动的参数:" + dto.toString());
        //变动的类型
        Integer type = dto.getType();
        //变动的成长值
        Long changeGrowth = dto.getChangeGrowth();

    // 1.获取用户成长值汇总数据
    MemberGrowthStatis memberGrowthStatis = this.findOrInsert(memberId, member.getAccount());
        //变动前成长值
        Long oldGrowth = memberGrowthStatis.getVipGrowth();

        //最终变动成长值
        Long changeFinalGrowth = 0L;

        MemberGrowthRecord saveRecord = new MemberGrowthRecord();

        //玩家金币记录
        MemberGoldCoinRecord savaGoldCoinRecord = new MemberGoldCoinRecord();

    // 3.按变动类型执行不同逻辑
    if (type == GrowthChangeEnum.recharge.getCode()) {
            //判断是否开启了充值
            if (growthConfig.getIsEnableRecharge() == EnableEnum.ENABLED.code()) {
                //获取充值 成长值 兑换比例
                changeFinalGrowth = changeGrowth;

                memberGrowthStatis.setRechargeGrowth(memberGrowthStatis.getRechargeGrowth() + changeFinalGrowth);

                saveRecord.setKindName(kindName);
                saveRecord.setKindCode("plat");
                saveRecord.setChangeMult(growthConfig.getRechageRate());

                //金币记录备注
                savaGoldCoinRecord.setRemark(member.getAccount() + "充值赠送金币,操作成长值：" + changeFinalGrowth + ",操作实时兑换比例：" + growthConfig.getCoinRate());
            } else {
                return;
            }
        } else if (type == GrowthChangeEnum.sign.getCode()) {
            //判断是否开启了签到
            if (growthConfig.getIsEnableSign() == EnableEnum.ENABLED.code()) {
                Long oldSignGrowth = memberGrowthStatis.getSignGrowth();
                if (oldSignGrowth >= growthConfig.getSignMaxGrowth()) {
                    return;
                }
                changeFinalGrowth = changeGrowth;
                Long currentSignGrowth = oldSignGrowth + changeFinalGrowth;
                if (currentSignGrowth > growthConfig.getSignMaxGrowth()) {
                    return;
                }
                memberGrowthStatis.setSignGrowth(currentSignGrowth);
                saveRecord.setKindName(kindName);
                saveRecord.setKindCode("plat");
                saveRecord.setChangeMult(new BigDecimal("1"));

                //金币记录备注
                savaGoldCoinRecord.setRemark(member.getAccount() + "签到赠送金币,操作成长值：" + changeFinalGrowth + ",操作实时兑换比例：" + growthConfig.getCoinRate());
            } else {
                return;
            }
        } else if (type == GrowthChangeEnum.dama.getCode()) {
            //判断是否开启了打码量
            if (growthConfig.getIsEnableDama() == EnableEnum.ENABLED.code()) {
                // 如果已存在打码量转换成长值记录则不添加
                List<MemberGrowthRecord> list = memberGrowthRecordService.findRecordList(new MemberGrowthRecordDTO() {{
                    setMemberId(memberId);
                    setType(GrowthChangeEnum.dama.getCode());
                    setKindCode(dto.getKindCode());
                    setCalcTime(DateUtil.today());
                }});

                if (CollectionUtil.isNotEmpty(list)) {
                    log.info("会员ID==={}===日期==={}已计算打码量成长值,不再计算", memberId, DateUtil.today());
                    return;
                }

                changeFinalGrowth = growthConfig.getDamaRate().multiply(BigDecimal.valueOf(changeGrowth)).longValue();
                memberGrowthStatis.setDamaGrowth(memberGrowthStatis.getDamaGrowth() + changeFinalGrowth);
                saveRecord.setKindName(dto.getKindName());
                saveRecord.setKindCode(dto.getKindCode());
                saveRecord.setChangeMult(growthConfig.getDamaRate().multiply(dto.getChangeMult()).setScale(2));
                //金币记录备注
                savaGoldCoinRecord.setRemark(member.getAccount() + "打码量赠送金币,操作成长值：" + changeFinalGrowth + ",操作实时兑换比例：" + growthConfig.getCoinRate());
            } else {
                return;
            }
        } else if (type == GrowthChangeEnum.backEdit.getCode()) {
            //后台修改
            changeFinalGrowth = changeGrowth;

            memberGrowthStatis.setBackGrowth(memberGrowthStatis.getBackGrowth() + changeFinalGrowth);

            saveRecord.setKindName(kindName);
            saveRecord.setKindCode("plat");
            saveRecord.setChangeMult(new BigDecimal("1"));
            if (dto.getRemark() != null) {
                saveRecord.setRemark(dto.getRemark());
            }
            //金币记录备注
            savaGoldCoinRecord.setRemark(member.getAccount() + "后台修改成长值赠送金币,操作成长值：" + changeFinalGrowth + ",操作实时兑换比例：" + growthConfig.getCoinRate());
        } else if (type == GrowthChangeEnum.finishInfo.getCode()) {
            //完善资料
        } else if (type == GrowthChangeEnum.bindBankCard.getCode()) {
            //判断是否已经绑定过银行卡加过成长值
            if (memberGrowthStatis.getBindGrowth() == 0) {
                changeFinalGrowth = growthConfig.getBindBankGrowth();
                memberGrowthStatis.setBindGrowth(changeFinalGrowth);

                //金币记录备注
                savaGoldCoinRecord.setRemark(member.getAccount() + "绑定过银行卡赠送金币,操作成长值：" + changeFinalGrowth + ",操作实时兑换比例：" + growthConfig.getCoinRate());
            }
        }

        //记录金币数量
        MemberGrowthLevel memberGrowthLevel = growthLevelService.getLevel(memberGrowthStatis.getVipLevel());
        Integer changeGoldCoin = growthConfig.getCoinRate().multiply(BigDecimal.valueOf(changeFinalGrowth)).intValue();
        savaGoldCoinRecord.setMemberId(memberId);
        savaGoldCoinRecord.setAccount(member.getAccount());
        savaGoldCoinRecord.setSourceType(1);
    savaGoldCoinRecord.setOrderNo(RandomUtil.randomNumbers(11));
        savaGoldCoinRecord.setCreateBy(member.getAccount());

        String userDailtKey = new StringBuilder().append("TENANT_USER_DAILY_COIN").append(":").append(member.getAccount()).toString();
        //玩家每日领取的金币
        Integer dailyCoin = (Integer) redisTemplate.opsForValue().get(userDailtKey);
        //每日最大领取金币
        Integer dailyMaxCoin = memberGrowthLevel.getDailyMaxCoin();

        MemberInfo memberInfo = memberInfoService.lambdaQuery().eq(MemberInfo::getMemberId, memberId).one();

        if (dailyCoin == null && dailyMaxCoin != null && dailyMaxCoin != 0 && changeGoldCoin > 0) {
            if (changeGoldCoin > dailyMaxCoin) {
                changeGoldCoin = dailyMaxCoin;
                savaGoldCoinRecord.setAmount(changeGoldCoin);
                savaGoldCoinRecord.setBeforeBalance(memberInfo.getGoldCoin());
                savaGoldCoinRecord.setAfterBalance(changeGoldCoin + memberInfo.getGoldCoin());
                memberGoldCoinRecordService.save(savaGoldCoinRecord);
                redisTemplate.opsForValue().set(userDailtKey, changeGoldCoin, getNowToNextDaySeconds().intValue(), TimeUnit.SECONDS);
            } else {
                savaGoldCoinRecord.setAmount(changeGoldCoin);
                savaGoldCoinRecord.setBeforeBalance(memberInfo.getGoldCoin());
                savaGoldCoinRecord.setAfterBalance(changeGoldCoin + memberInfo.getGoldCoin());
                memberGoldCoinRecordService.save(savaGoldCoinRecord);
                redisTemplate.opsForValue().set(userDailtKey, changeGoldCoin, getNowToNextDaySeconds().intValue(), TimeUnit.SECONDS);
            }
        } else if (dailyCoin != null && dailyCoin < dailyMaxCoin && changeGoldCoin > 0) {
            if (changeGoldCoin + dailyCoin > dailyMaxCoin) {
                changeGoldCoin = dailyMaxCoin - dailyCoin;
                savaGoldCoinRecord.setAmount(changeGoldCoin);
                savaGoldCoinRecord.setBeforeBalance(memberInfo.getGoldCoin());
                savaGoldCoinRecord.setAfterBalance(changeGoldCoin + memberInfo.getGoldCoin());
                memberGoldCoinRecordService.save(savaGoldCoinRecord);
                redisTemplate.opsForValue().set(userDailtKey, dailyMaxCoin, getNowToNextDaySeconds().intValue(), TimeUnit.SECONDS);
            } else {
                savaGoldCoinRecord.setAmount(changeGoldCoin);
                savaGoldCoinRecord.setBeforeBalance(memberInfo.getGoldCoin());
                savaGoldCoinRecord.setAfterBalance(changeGoldCoin + memberInfo.getGoldCoin());
                memberGoldCoinRecordService.save(savaGoldCoinRecord);
                redisTemplate.opsForValue().set(userDailtKey, (dailyCoin + changeGoldCoin), getNowToNextDaySeconds().intValue(), TimeUnit.SECONDS);
            }
        } else {
            //如果玩家到达日常领取上限就设置改变金币为0，防止更新用户表添加金币
            changeGoldCoin = 0;
        }

        //设置最新总成长值
        memberGrowthStatis.setVipGrowth(memberGrowthStatis.getVipGrowth() + changeFinalGrowth);

        //当前的成长值等级
        Integer beforeLevel = memberGrowthStatis.getVipLevel();

    // 4.通过变动后的最新成长值 执行 增加成长值升级
    if (memberGrowthStatis.getVipGrowth() < 0L) {
            memberGrowthStatis.setVipGrowth(0L);
        }
        Integer afterLevel = this.dealUpLevel(memberGrowthStatis.getVipGrowth(), growthConfig);
        memberGrowthStatis.setVipLevel(afterLevel);

    // 5.记录成长值变动记录  重新更新 会员成长值汇总
    Long tempGrowth = changeFinalGrowth;
        //添加成长值变动记录
        saveRecord.setUserId(memberId);
        saveRecord.setUserName(member.getAccount());
        saveRecord.setType(type);
        saveRecord.setOldLevel(beforeLevel);
        saveRecord.setCurrentLevel(afterLevel);
        saveRecord.setChangeGrowth(tempGrowth);
        saveRecord.setOldGrowth(oldGrowth);
        saveRecord.setCurrentGrowth(memberGrowthStatis.getVipGrowth());
        saveRecord.setCreateBy(GlobalContextHolder.getContext().getUsername());
        saveRecord.setRemark(dto.getRemark());
        memberGrowthRecordService.insertMemberGrowthRecord(saveRecord);

        //修改成长值汇总
        log.info("VIP成长值汇总数据" + memberGrowthStatis);
        this.insertOrUpdate(memberGrowthStatis);

    // 6.传入变动前和变动后的等级 处理发放升级奖励
    if (beforeLevel.compareTo(afterLevel) < 0) {
            this.dealPayUpReword(beforeLevel, afterLevel, growthConfig, member);

            //VIP变动更新会员vip
            LambdaUpdateWrapper<MemberInfo> wrapper = new LambdaUpdateWrapper<>();
            wrapper.set(MemberInfo::getVipGrowth, memberInfo.getVipGrowth() + changeFinalGrowth)
                    .set(MemberInfo::getVipLevel, afterLevel)
                    .set(MemberInfo::getGoldCoin, memberInfo.getGoldCoin() + changeGoldCoin)
                    .eq(MemberInfo::getMemberId, memberId);
            memberInfoService.update(wrapper);
        } else {
            //VIP变动更新会员vip
            LambdaUpdateWrapper<MemberInfo> wrapper = new LambdaUpdateWrapper<>();
            wrapper.set(MemberInfo::getVipGrowth, memberInfo.getVipGrowth() + changeFinalGrowth)
                    .set(MemberInfo::getVipLevel, afterLevel)
                    .set(MemberInfo::getGoldCoin, memberInfo.getGoldCoin() + changeGoldCoin)
                    .eq(MemberInfo::getMemberId, memberId);
            memberInfoService.update(wrapper);
        }
        return;
    }

    /**
     * 根据变动前后的等级  计算奖励金额
     */
    @Override
    public BigDecimal calcRewordAmount(Integer beforeLevel, Integer afterLevel, MemberGrowthConfig growthConfig) {
        //先计算出升级奖励的金额  可能会连升几级
        Integer limitLevel = growthConfig.getLimitLevel();
        if (limitLevel == null) {
            limitLevel = 50;
        }
        List<MemberGrowthLevel> levels = growthLevelService.getList(limitLevel);
        Map<Integer, MemberGrowthLevel> levelMap = levels.stream().collect(Collectors.toMap(MemberGrowthLevel::getLevel, MemberGrowthLevel -> MemberGrowthLevel));
        //总的奖励金额
        BigDecimal rewordAmount = new BigDecimal("0");
        for (int i = beforeLevel + 1; i <= afterLevel; i++) {
            rewordAmount = rewordAmount.add(levelMap.get(i).getUpReward());
        }
        return rewordAmount;
    }

    /**
     * 处理升级
     */
    @Override
    public void dealPayUpReword(Integer beforeLevel, Integer afterLevel, MemberGrowthConfig growthConfig, Member member) {
        // 查询用户是否在黑名单中
        List<MemberBlackList> memberBlackList =
                memberBlackListService.findMemberBlackList(
                        new MemberBlackList() {
                            {
                                setUserAccount(member.getAccount());
                            }
                        });

        String content = "VIP等级升级";

        //判断是否发放奖励 并且是否在黑名单中
        if (BooleanEnum.YES.match(growthConfig.getIsPayUpReword()) && memberBlackList.size() <= 0) {

            BigDecimal rewordAmount = new BigDecimal("0");

            //先计算出升级奖励的金额  可能会连升几级
            Integer limitLevel = growthConfig.getLimitLevel();
            if (limitLevel == null) {
                limitLevel = 50;
            }
            List<MemberGrowthLevel> levels = growthLevelService.getList(limitLevel);
            Map<Integer, MemberGrowthLevel> levelMap =
                    levels.stream()
                            .collect(
                                    Collectors.toMap(
                                            MemberGrowthLevel::getLevel, MemberGrowthLevel -> MemberGrowthLevel));

            log.info("VIP等级配置:" + growthConfig.toString());

            //获取是否重复派发升级奖励 0 不允许重复派发   1 允许重复派发
            Integer isRepeatPayUpReword =
                    growthConfig.getIsRepeatPayUpReword() == null ? 0 : growthConfig.getIsRepeatPayUpReword();

            //总的奖励金额
            for (int i = beforeLevel + 1; i <= afterLevel; i++) {
                //当不允许重复派发升级奖励时 去掉发放过i级的升级奖励
                if (isRepeatPayUpReword == 0) {
                    //得到是否发放过i级的升级奖励
                    MemberWealRewordDTO queryDto = new MemberWealRewordDTO();
                    queryDto.setUserId(member.getId());
                    queryDto.setVipLevel(i);
                    Long isPayCount = memberWealRewordService.findCountReword(queryDto);
                    log.info("不允许重复派发升级奖励:用户({0}),当前判断等级:VIP{1},是否发放:{2}"
                            , member.getId(), i, isPayCount);

                    if (isPayCount > 0) {
                        continue;
                    }
                }
                rewordAmount = rewordAmount.add(levelMap.get(i).getUpReward());
            }
            //判断升级奖励金额是否大于0
            if (rewordAmount.compareTo(new BigDecimal("0")) > 0) {
                MemberWealRewordAddDTO saveRewordObj = new MemberWealRewordAddDTO();
                saveRewordObj.setUserId(member.getId());
                saveRewordObj.setUserName(member.getAccount());
                saveRewordObj.setOldLevel(beforeLevel);
                saveRewordObj.setCurrentLevel(afterLevel);
                saveRewordObj.setRewordAmount(rewordAmount);
                saveRewordObj.setType(0);
                saveRewordObj.setParentId(member.getParentId());
                saveRewordObj.setParentName(member.getParentName());
                saveRewordObj.setAgentPath(member.getSuperPath());
                saveRewordObj.setUserType(member.getUserType());
        saveRewordObj.setSerialNumber(RandomUtil.randomNumbers(18));

                //是否自动派发
                if (EnableEnum.ENABLED.match(growthConfig.getIsAutoPayReword())) {
          String sourceId = RandomUtil.randomNumbers(22);
                    // 新增打码量记录
                    ValidWithdraw validWithdraw = new ValidWithdraw();
                    validWithdraw.setAccount(member.getAccount());
                    validWithdraw.setMemberId(member.getId());
                    validWithdraw.setRechId(sourceId);
                    validWithdraw.setRechMoney(rewordAmount.setScale(2, RoundingMode.HALF_UP));
                    validWithdraw.setCreateTime(new Date());
                    validWithdraw.setType(0);
                    validWithdraw.setStatus(0);
                    validWithdraw.setDiscountMoney(BigDecimal.ZERO);
                    validWithdraw.setRemark(member.getAccount() + "从VIP" + beforeLevel + "升至VIP" + afterLevel + "奖励金额增加打码量");
                    validWithdrawService.saveValidWithdraw(validWithdraw);

                    synchronized (lockHelper) {

                        MemberInfo memberInfo = memberInfoService.lambdaQuery()
                                .eq(MemberInfo::getMemberId, member.getId())
                                .one();
                        BigDecimal afterBlance = memberInfo.getBalance();

                        LambdaUpdateWrapper<MemberInfo> wrapper = new LambdaUpdateWrapper<>();
                        wrapper.set(MemberInfo::getBalance, afterBlance.add(rewordAmount))
                                .eq(MemberInfo::getMemberId, member.getId());
                        memberInfoService.update(wrapper);

                        // 添加流水记录
                        MemberBill memberBill = new MemberBill();
                        memberBill.setMemberId(member.getId());
                        memberBill.setAccount(member.getAccount());
                        memberBill.setMemberPath(member.getSuperPath());
                        memberBill.setTranType(TranTypes.UPGRADE_REWARD.getValue());
                        memberBill.setOrderNo(sourceId);
                        memberBill.setAmount(rewordAmount);
                        memberBill.setBalance(afterBlance);
                        memberBill.setRemark(
                                member.getAccount() + "从VIP" + beforeLevel + "升至VIP" + afterLevel + "奖励金额");
                        memberBill.setContent(
                                member.getAccount() + "从VIP" + beforeLevel + "升至VIP" + afterLevel + "奖励金额");
                        memberBill.setOperator("system");
                        memberBillService.save(memberBill);

                        saveRewordObj.setStatus(2);
                        content = "VIP等级升级奖励已派发 金额:" + rewordAmount;
                        saveRewordObj.setDrawTime(DateTime.now());
                    }
                } else {
                    saveRewordObj.setStatus(1);
                    content = "VIP等级升级奖励已派发 金额:" + rewordAmount + "，请领取";
                }
                log.info("添加奖励记录");

                saveRewordObj.setParentId(member.getParentId());
                saveRewordObj.setParentName(member.getParentName());
                saveRewordObj.setAgentPath(member.getSuperPath());
                saveRewordObj.setUserType(member.getUserType());

                memberWealRewordService.insertMemberWealReword(saveRewordObj);
            }
        }
        //通知 发个人消息
        Message message = new Message();
        message.setTitle("VIP等级升级");
        message.setContent(content);
        message.setCategory(PushMessageEnum.MessageCategory.SYS_SEND.getValue());
        message.setPosition(PushMessageEnum.MessageCategory.CATE_DEF.getValue());
        message.setShowType(PushMessageEnum.MessageShowType.SHOW_DEF.value());
        message.setPopsCount(PushMessageEnum.PopCount.POP_COUNT_DEF.getValue());
        message.setPushRange(PushMessageEnum.UserRange.SOME_MEMBERS.getValue());
        message.setLinkAccount(member.getAccount());
        message.setType(1);
        message.setStatus(1);
        message.setCreateBy("System");
        messageMapper.saveReturnId(message);
    }

    /**
     * 异步 变动成长值
     */
    @Override
    @Async
    public void asyncChangeGrowth(MemberGrowthChangeDto dto) {
        changeGrowth(dto);
    }

    /**
     * 修改成长值汇总数据
     */
    @Override
    public Boolean updateMemberGrowthStatis(MemberGrowthStatis entity) {
        LambdaUpdateWrapper<MemberGrowthStatis> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(ObjectUtils.isNotEmpty(entity.getVipLevel()), MemberGrowthStatis::getVipLevel, entity.getVipLevel())
                .set(ObjectUtils.isNotEmpty(entity.getVipGrowth()), MemberGrowthStatis::getVipGrowth, entity.getVipGrowth())
                .set(ObjectUtils.isNotEmpty(entity.getRechargeGrowth()), MemberGrowthStatis::getRechargeGrowth, entity.getRechargeGrowth())
                .set(ObjectUtils.isNotEmpty(entity.getSignGrowth()), MemberGrowthStatis::getSignGrowth, entity.getSignGrowth())
                .set(ObjectUtils.isNotEmpty(entity.getDamaGrowth()), MemberGrowthStatis::getDamaGrowth, entity.getDamaGrowth())
                .set(ObjectUtils.isNotEmpty(entity.getBackGrowth()), MemberGrowthStatis::getBackGrowth, entity.getBackGrowth())
                .set(ObjectUtils.isNotEmpty(entity.getInfoGrowth()), MemberGrowthStatis::getInfoGrowth, entity.getInfoGrowth())
                .set(ObjectUtils.isNotEmpty(entity.getBindGrowth()), MemberGrowthStatis::getBindGrowth, entity.getBindGrowth())
                .set(ObjectUtils.isNotEmpty(entity.getDemoteGrowth()), MemberGrowthStatis::getDemoteGrowth, entity.getDemoteGrowth())
                .eq(MemberGrowthStatis::getMemberId, entity.getMemberId());
        return this.update(wrapper);
    }

    //获取到第二天的秒数
    public Long getNowToNextDaySeconds() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }
}
