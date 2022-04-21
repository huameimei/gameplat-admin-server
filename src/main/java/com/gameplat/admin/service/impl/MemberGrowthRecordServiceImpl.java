package com.gameplat.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberGrowthRecordConvert;
import com.gameplat.admin.convert.MessageInfoConvert;
import com.gameplat.admin.enums.GrowthChangeEnum;
import com.gameplat.admin.enums.PushMessageEnum;
import com.gameplat.admin.mapper.MemberGrowthRecordMapper;
import com.gameplat.admin.mapper.MessageMapper;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.GrowthScaleVO;
import com.gameplat.admin.model.vo.MemberGrowthLevelVO;
import com.gameplat.admin.model.vo.MemberGrowthRecordVO;
import com.gameplat.admin.model.vo.MemberVO;
import com.gameplat.admin.service.*;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.enums.BooleanEnum;
import com.gameplat.common.enums.TranTypes;
import com.gameplat.model.entity.ValidWithdraw;
import com.gameplat.model.entity.member.*;
import com.gameplat.model.entity.message.Message;
import com.gameplat.redis.redisson.DistributedLocker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author lily
 * @description vip成长记录业务处理层
 * @date 2021/11/24
 */
@Service
@Slf4j
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberGrowthRecordServiceImpl
        extends ServiceImpl<MemberGrowthRecordMapper, MemberGrowthRecord>
        implements MemberGrowthRecordService {

    public static final String kindName =
            "{\"en-US\": \"platform\", \"in-ID\": \"peron\", \"th-TH\": \"แพลตฟอร์ม\", \"vi-VN\": \"nền tảng\", \"zh-CN\": \"平台\"}";
    @Autowired
    private MemberGrowthRecordConvert recordConvert;

    @Autowired
    private MemberGrowthRecordMapper memberGrowthRecordMapper;

    @Resource(name = "memberGrowthLevelServiceImpl")
    private MemberGrowthLevelService growthLevelService;

    @Autowired
    private MemberGrowthConfigService memberGrowthConfigService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberBlackListService memberBlackListService;

    @Autowired
    private DistributedLocker distributedLocker;

    @Autowired
    private MemberWealRewordService memberWealRewordService;

    @Autowired
    private ValidWithdrawService validWithdrawService;

    @Autowired
    private MessageMapper messageMapper;

    @Resource(name = "memberGrowthStatisServiceImpl")
    private MemberGrowthStatisService memberGrowthStatisService;

    @Autowired
    private MemberInfoService memberInfoService;

    @Autowired
    private MemberBillService memberBillService;

    @Autowired
    private MessageInfoConvert messageInfoConvert;

    @Autowired
    private MemberLoanService memberLoanService;

    @Autowired
    private MemberGoldCoinRecordService memberGoldCoinRecordService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public static final Object lockHelper = new Object();

    @Override
    public IPage<MemberGrowthRecordVO> findRecordList(
            PageDTO<MemberGrowthRecord> page, MemberGrowthRecordDTO dto) {

        IPage<MemberGrowthRecordVO> result =
                this.lambdaQuery()
                        .eq(
                                ObjectUtils.isNotEmpty(dto.getMemberId()),
                                MemberGrowthRecord::getUserId,
                                dto.getMemberId())
                        .ne(MemberGrowthRecord::getType, 6)
                        .like(
                                ObjectUtils.isNotEmpty(dto.getUserName()),
                                MemberGrowthRecord::getUserName,
                                dto.getUserName())
                        .eq(ObjectUtils.isNotEmpty(dto.getType()), MemberGrowthRecord::getType, dto.getType())
                        .ge(
                                ObjectUtils.isNotEmpty(dto.getStartTime()),
                                MemberGrowthRecord::getCreateTime,
                                dto.getStartTime())
                        .le(
                                ObjectUtils.isNotEmpty(dto.getEndTime()),
                                MemberGrowthRecord::getCreateTime,
                                dto.getEndTime())
                        .eq(
                                ObjectUtils.isNotEmpty(dto.getCalcTime()),
                                MemberGrowthRecord::getCreateTime,
                                dto.getCalcTime())
                        .orderByDesc(MemberGrowthRecord::getCreateTime)
                        .page(page)
                        .convert(recordConvert::toVo);

        List<MemberGrowthRecordVO> list = result.getRecords();
        for (MemberGrowthRecordVO vo : list) {
            if (ObjectUtils.isNotNull(vo.getKindName())) {
                JSONObject jsonKindName = JSONObject.parseObject(vo.getKindName());
                vo.setKindName(jsonKindName.getString(dto.getLanguage()));
            }
        }

        return result;
    }

    /**
     * 成长值变动后重新计算新的等级
     */
    @Override
    public Integer dealUpLevel(Long afterGrowth, MemberGrowthConfig memberGrowthConfig) {
        // todo 1.先获取所有成长值等级
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
    public List<MemberGrowthRecord> findRecordGroupBy(MemberGrowthRecord entity) {
        return memberGrowthRecordMapper.findRecordGroupBy(entity);
    }

    /**
     * 修改单个会员成长值 成成长值变动
     */
    @Override
    public void editMemberGrowth(MemberGrowthChangeDto dto, HttpServletRequest request) {
        // 判断是否开启了VIP
        // todo 2.获取成长值配置
        MemberGrowthConfig growthConfig = memberGrowthConfigService.getOneConfig();
        if (growthConfig.getIsEnableVip() == 0) {
            log.info("成长值计算失败，未开启VIP功能");
            return;
        }
        // 会员id
        Long memberId = dto.getUserId();
        Member member = memberService.getById(memberId);
        MemberInfo memberInfo = memberInfoService.lambdaQuery().eq(MemberInfo::getMemberId, memberId).one();
        if (BeanUtil.isEmpty(member)) {
            log.info("VIP 成长值变动 会员不存在");
            return;
        }
        // 变动的类型
        Integer type = dto.getType();
//        if (dto.getChangeGrowth() == null) {
//            throw new ServiceException("此次变动金额参数不存在");
//        }
        // 变动的成长值
        Long changeGrowth = dto.getChangeGrowth();

        // todo 1.获取用户成长值汇总数据
        List<MemberGrowthRecord> memberGrowthRecords = this.findOne(dto);
        MemberGrowthRecord memberGrowthRecord = new MemberGrowthRecord();
        if (ObjectUtils.isNotEmpty(memberGrowthRecords)) {
            memberGrowthRecord = memberGrowthRecords.get(0);
        } else {
            memberGrowthRecord.setOldGrowth(0L);
            memberGrowthRecord.setCurrentGrowth(0L);
            memberGrowthRecord.setOldLevel(0);
            memberGrowthRecord.setCurrentLevel(0);
        }
        // 当前成长值
//        Long oldGrowth = memberInfoService.lambdaQuery().eq(MemberInfo::getMemberId, dto.getUserId()).one().getVipGrowth();
        Long oldGrowth = memberGrowthRecord.getCurrentGrowth();
        // 最终变动成长值  由于类型不同  可能最终变成的成长值倍数也不同
        Long changeFinalGrowth = 0L;

        MemberGrowthRecord saveRecord = new MemberGrowthRecord();

        MemberGoldCoinRecord saveGoldCoinRecord = new MemberGoldCoinRecord();

        if(memberGrowthStatisService.lambdaQuery().eq(MemberGrowthStatis::getMemberId, memberId).one() == null){
            MemberGrowthStatis growthStatis = new MemberGrowthStatis();
            growthStatis.setMemberId(memberId);
            growthStatis.setAccount(member.getAccount());
            memberGrowthStatisService.save(growthStatis);
        }

        LambdaUpdateWrapper<MemberGrowthStatis> wrapperGrowthStatis = new LambdaUpdateWrapper();


        // todo 3.按变动类型执行不同逻辑
        if (type == GrowthChangeEnum.recharge.getCode()) {
            // 判断是否开启了充值
            if (BooleanEnum.YES.match(growthConfig.getIsEnableRecharge())) {
                // 获取充值 成长值 兑换比例
                changeFinalGrowth = changeGrowth;

                saveRecord.setKindName(kindName);
                saveRecord.setKindCode("plat");
                saveRecord.setChangeMult(growthConfig.getRechageRate());

                wrapperGrowthStatis.set(MemberGrowthStatis::getRechargeGrowth, changeFinalGrowth);

                saveGoldCoinRecord.setRemark(member.getAccount() + "充值赠送金币,操作成长值：" + changeFinalGrowth + ",操作实时兑换比例：" + growthConfig.getCoinRate());
            } else {
                return;
            }
        } else if (type == GrowthChangeEnum.sign.getCode()) {
            // 签到
            // 判断是否开启了签到
            if (BooleanEnum.YES.match(growthConfig.getIsEnableSign())) {
                Long oldSignGrowth = memberGrowthRecord.getOldGrowth();
                if (oldSignGrowth >= growthConfig.getSignMaxGrowth()) {
                    return;
                }
                changeFinalGrowth = changeGrowth;
                Long currentSignGrowth = oldSignGrowth + changeFinalGrowth;
                if (currentSignGrowth > growthConfig.getSignMaxGrowth()) {
                    return;
                }
                saveRecord.setKindName(kindName);
                saveRecord.setKindCode("plat");
                saveRecord.setChangeMult(new BigDecimal("1"));
                saveRecord.setRemark(dto.getRemark());

                wrapperGrowthStatis.set(MemberGrowthStatis::getSignGrowth, changeFinalGrowth);
                //金币记录备注
                saveGoldCoinRecord.setRemark(member.getAccount() + "签到赠送金币,操作成长值：" + changeFinalGrowth + ",操作实时兑换比例：" + growthConfig.getCoinRate());
            } else {
                return;
            }

        } else if (type == GrowthChangeEnum.dama.getCode()) {
            // 判断是否开启了打码量
            if (BooleanEnum.YES.match(growthConfig.getIsEnableDama())) {
                changeFinalGrowth =
                        growthConfig.getDamaRate().multiply(BigDecimal.valueOf(changeGrowth)).longValue();
                saveRecord.setKindName(dto.getKindName());
                saveRecord.setKindCode(dto.getKindCode());
                saveRecord.setChangeMult(
                        growthConfig.getDamaRate().multiply(dto.getChangeMult()).setScale(2));

                wrapperGrowthStatis.set(MemberGrowthStatis::getDamaGrowth, changeFinalGrowth);

                //金币记录备注
                saveGoldCoinRecord.setRemark(member.getAccount() + "打码量赠送金币,操作成长值：" + changeFinalGrowth + ",操作实时兑换比例：" + growthConfig.getCoinRate());
            } else {
                return;
            }
        } else if (type == GrowthChangeEnum.backEdit.getCode()) {
            // 后台修改
            changeFinalGrowth = changeGrowth;
            saveRecord.setKindName(kindName);
            saveRecord.setKindCode("plat");
            saveRecord.setChangeMult(new BigDecimal("1"));
            if (dto.getRemark() != null) {
                saveRecord.setRemark(dto.getRemark());
            }

            wrapperGrowthStatis.set(MemberGrowthStatis::getBackGrowth, changeFinalGrowth);

            //金币记录备注
            saveGoldCoinRecord.setRemark(member.getAccount() + "后台修改成长值赠送金币,操作成长值：" + changeFinalGrowth + ",操作实时兑换比例：" + growthConfig.getCoinRate());
        } else if (type == GrowthChangeEnum.finishInfo.getCode()) {
            // 完善资料
        } else if (type == GrowthChangeEnum.bindBankCard.getCode()) {
            // 绑定银行卡
            // 判断是否已经绑定过银行卡加过成长值
            if (this.lambdaQuery()
                    .eq(MemberGrowthRecord::getUserId, dto.getUserId())
                    .eq(MemberGrowthRecord::getType, GrowthChangeEnum.bindBankCard.getCode())
                    .list()
                    .size()
                    == 0) {
                changeFinalGrowth = growthConfig.getBindBankGrowth();

                //金币记录备注
                saveGoldCoinRecord.setRemark(member.getAccount() + "绑定过银行卡赠送金币,操作成长值：" + changeFinalGrowth + ",操作实时兑换比例：" + growthConfig.getCoinRate());
            }
        }
        //当前等级对应金币上限
        MemberGrowthLevel memberGrowthLevel = growthLevelService.getLevel(memberInfo.getVipLevel());
        Integer changeGoldCoin = 0;
        if (changeFinalGrowth > 0) {
            //成长值兑换的金币数量
            changeGoldCoin = growthConfig.getCoinRate().multiply(BigDecimal.valueOf(changeFinalGrowth)).intValue();
        }
        saveGoldCoinRecord.setMemberId(memberId);
        saveGoldCoinRecord.setAccount(member.getAccount());
        saveGoldCoinRecord.setSourceType(1);
    saveGoldCoinRecord.setOrderNo(RandomUtil.randomNumbers(11));
        String userDailtKey = new StringBuilder().append("TENANT_USER_DAILY_COIN").append(":").append(member.getAccount()).toString();
        //玩家每日领取的金币
        Integer dailyCoin = (Integer) redisTemplate.opsForValue().get(userDailtKey);
        //每日最大领取金币
        Integer dailyMaxCoin = memberGrowthLevel.getDailyMaxCoin();
        if (dailyCoin == null && dailyMaxCoin != null && dailyMaxCoin != 0 && changeGoldCoin > 0) {
            if (changeGoldCoin > dailyMaxCoin) {
                changeGoldCoin = dailyMaxCoin;
                saveGoldCoinRecord.setAmount(changeGoldCoin);
                saveGoldCoinRecord.setBeforeBalance(memberInfo.getGoldCoin());
                saveGoldCoinRecord.setAfterBalance(changeGoldCoin + memberInfo.getGoldCoin());
                memberGoldCoinRecordService.save(saveGoldCoinRecord);
                redisTemplate.opsForValue().set(userDailtKey, changeGoldCoin, getNowToNextDaySeconds().intValue(), TimeUnit.SECONDS);
            } else {
                saveGoldCoinRecord.setAmount(changeGoldCoin);
                saveGoldCoinRecord.setBeforeBalance(memberInfo.getGoldCoin());
                saveGoldCoinRecord.setAfterBalance(changeGoldCoin + memberInfo.getGoldCoin());
                memberGoldCoinRecordService.save(saveGoldCoinRecord);
                redisTemplate.opsForValue().set(userDailtKey, changeGoldCoin, getNowToNextDaySeconds().intValue(), TimeUnit.SECONDS);
            }
        } else if (dailyCoin != null && dailyCoin < dailyMaxCoin && changeGoldCoin > 0) {
            if (changeGoldCoin + dailyCoin > dailyMaxCoin) {
                changeGoldCoin = dailyMaxCoin - dailyCoin;
                saveGoldCoinRecord.setAmount(changeGoldCoin);
                saveGoldCoinRecord.setBeforeBalance(memberInfo.getGoldCoin());
                saveGoldCoinRecord.setAfterBalance(changeGoldCoin + memberInfo.getGoldCoin());
                memberGoldCoinRecordService.save(saveGoldCoinRecord);
                redisTemplate.opsForValue().set(userDailtKey, dailyMaxCoin, getNowToNextDaySeconds().intValue(), TimeUnit.SECONDS);
            } else {
                saveGoldCoinRecord.setAmount(changeGoldCoin);
                saveGoldCoinRecord.setBeforeBalance(memberInfo.getGoldCoin());
                saveGoldCoinRecord.setAfterBalance(changeGoldCoin + memberInfo.getGoldCoin());
                memberGoldCoinRecordService.save(saveGoldCoinRecord);
                redisTemplate.opsForValue().set(userDailtKey, (dailyCoin + changeGoldCoin), getNowToNextDaySeconds().intValue(), TimeUnit.SECONDS);
            }
        } else {
            //如果玩家到达日常领取上限就设置改变金币为0，防止更新用户表添加金币
            changeGoldCoin = 0;
        }

        // 设置最新总成长值
        memberGrowthRecord.setCurrentGrowth(memberGrowthRecord.getCurrentGrowth() + changeFinalGrowth);
        // 当前的会员等级
        Integer beforeLevel = memberGrowthRecord.getCurrentLevel();
        // todo 4.通过变动后的最新成长值 执行 增加成长值升级
        if (memberGrowthRecord.getCurrentGrowth() < 0) {
            memberGrowthRecord.setCurrentGrowth(0L);
        }
        // 成长值变动后重新计算新的等级
        Integer afterLevel = this.dealUpLevel(memberGrowthRecord.getCurrentGrowth(), growthConfig);
        MemberVO memberVo = memberService.queryList(new MemberQueryDTO() {{
            setId(memberId);
        }}).get(0);
        //更新借呗表额度
        BigDecimal loanMoney = growthLevelService.lambdaQuery()
                .eq(MemberGrowthLevel::getLevel, afterLevel)
                .one()
                .getLoanMoney();
        int money = loanMoney.compareTo(BigDecimal.ZERO);
        if (money == 1) {
            memberLoanService.editOrUpdate(new MemberLoan() {{
                setLoanMoney(loanMoney);
                setMemberId(memberId);
                setAccount(memberVo.getAccount());
            }});
        }
        memberGrowthRecord.setCurrentLevel(afterLevel);
        // todo 5.记录成长值变动记录  重新更新 会员成长值汇总
        Long tempGrowth = changeFinalGrowth;
        // 添加成长值变动记录
        saveRecord.setUserId(memberId);
        saveRecord.setUserName(member.getAccount());
        saveRecord.setType(type);
        saveRecord.setOldLevel(beforeLevel);
        saveRecord.setCurrentLevel(afterLevel);
        saveRecord.setChangeGrowth(tempGrowth);
        saveRecord.setOldGrowth(oldGrowth);
        saveRecord.setCurrentGrowth(memberGrowthRecord.getCurrentGrowth());
        Boolean save = this.insertMemberGrowthRecord(saveRecord);
        if (!save) {
            throw new ServiceException("服务器繁忙，请稍后再试");
        }


        memberGrowthStatisService.update(wrapperGrowthStatis);

        // todo 6.传入变动前和变动后的等级 处理发放升级奖励
        LambdaUpdateWrapper<MemberInfo> wrapper = new LambdaUpdateWrapper<>();
        if (beforeLevel.compareTo(afterLevel) < 0) {
            this.dealPayUpReword(beforeLevel, afterLevel, growthConfig, member, request);
            // VIP变动更新会员vip
            wrapper.set(MemberInfo::getVipLevel, afterLevel)
                    .set(MemberInfo::getVipGrowth, oldGrowth + changeFinalGrowth)
                    .set(MemberInfo::getGoldCoin, memberInfo.getGoldCoin() + changeGoldCoin)
                    .eq(MemberInfo::getMemberId, memberId);
            memberInfoService.update(wrapper);
        } else {
            // VIP变动更新会员vip
            wrapper.set(MemberInfo::getVipLevel, afterLevel)
                    .set(MemberInfo::getVipGrowth, oldGrowth + changeFinalGrowth)
                    .set(MemberInfo::getGoldCoin, memberInfo.getGoldCoin() + changeGoldCoin)
                    .eq(MemberInfo::getMemberId, memberId);
            memberInfoService.update(wrapper);
        }
    }

    @Override
    public Boolean insertMemberGrowthRecord(MemberGrowthRecord userGrowthRecord) {
        return this.save(userGrowthRecord);
    }

    /**
     * 进度条
     */
    @Override
    public GrowthScaleVO progressBar(Integer level, Long memberId) {

        if (BeanUtil.isEmpty(memberService.getById(memberId))) {
            throw new ServiceException("该会员不存在");
        }
        MemberGrowthLevel growthLevel = growthLevelService.getLevel(level);
        if (BeanUtil.isEmpty(growthLevel)) {
            throw new ServiceException("无此等级配置信息");
        }
        // 晋级到下级所需的成长值
        Long growth = growthLevel.getGrowth();
        Long currentGrowth = 0L;
        // 当前成长值
        MemberInfo memberInfo = memberInfoService.lambdaQuery().eq(MemberInfo::getMemberId, memberId).one();
        currentGrowth = memberInfo.getVipGrowth();

        Long finalCurrentGrowth = currentGrowth;
        return new GrowthScaleVO() {
            {
                setLowerGrowth(growth);
                setCurrentGrowth(finalCurrentGrowth);
            }
        };
    }

    @Override
    public List<MemberGrowthRecord> findRecordList(MemberGrowthRecordDTO dto) {
        List<MemberGrowthRecord> list = this.lambdaQuery()
                .eq(
                        ObjectUtils.isNotEmpty(dto.getMemberId()),
                        MemberGrowthRecord::getUserId,
                        dto.getMemberId())
                .like(
                        ObjectUtils.isNotEmpty(dto.getUserName()),
                        MemberGrowthRecord::getUserName,
                        dto.getUserName())
                .eq(
                        ObjectUtils.isNotEmpty(dto.getType()),
                        MemberGrowthRecord::getType,
                        dto.getType())
                .ge(
                        ObjectUtils.isNotEmpty(dto.getStartTime()),
                        MemberGrowthRecord::getCreateTime,
                        dto.getStartTime())
                .le(
                        ObjectUtils.isNotEmpty(dto.getEndTime()),
                        MemberGrowthRecord::getCreateTime,
                        dto.getEndTime())
                .eq(
                        ObjectUtils.isNotEmpty(dto.getCalcTime()),
                        MemberGrowthRecord::getCreateTime,
                        dto.getCalcTime())
                .orderByDesc(MemberGrowthRecord::getCreateTime)
                .list();

        for (MemberGrowthRecord memberGrowthRecord : list) {
            if (ObjectUtils.isNotNull(memberGrowthRecord.getKindName())) {
                JSONObject jsonKindName = JSONObject.parseObject(memberGrowthRecord.getKindName());
                memberGrowthRecord.setKindName(jsonKindName.getString(dto.getLanguage()));
            }
        }

        return list;
    }

    /**
     * 处理升级
     */
    @Override
    public void dealPayUpReword(
            Integer beforeLevel,
            Integer afterLevel,
            MemberGrowthConfig growthConfig,
            Member member,
            HttpServletRequest request) {
        // 查询用户是否在黑名单中
        List<MemberBlackList> memberBlackList =
                memberBlackListService.findMemberBlackList(
                        new MemberBlackList() {
                            {
                                setUserAccount(member.getAccount());
                            }
                        });
        String content = "VIP等级升级";
        // 判断是否发放奖励 并且是否在黑名单中
        if (BooleanEnum.YES.match(growthConfig.getIsPayUpReword()) && memberBlackList.size() <= 0) {
            BigDecimal rewordAmount = new BigDecimal("0");
            // 先计算出升级奖励的金额  可能会连升几级
            Integer limitLevel = growthConfig.getLimitLevel();
            if (limitLevel == null) {
                limitLevel = 50;
            }
            List<MemberGrowthLevel> levels =
                    growthLevelService.getList(limitLevel);
            Map<Integer, MemberGrowthLevel> levelMap =
                    levels.stream()
                            .collect(
                                    Collectors.toMap(
                                            MemberGrowthLevel::getLevel, MemberGrowthLevel -> MemberGrowthLevel));
            // 获取是否重复派发升级奖励 0 不允许重复派发   1 允许重复派发
            Integer isRepeatPayUpReword =
                    growthConfig.getIsRepeatPayUpReword() == null ? 0 : growthConfig.getIsRepeatPayUpReword();
            // 总的奖励金额
            for (int i = beforeLevel + 1; i <= afterLevel; i++) {
                // 当不允许重复派发升级奖励时 去掉发放过i级的升级奖励
                if (isRepeatPayUpReword == 0) {
                    // 得到是否发放过i级的升级奖励
                    MemberWealRewordDTO queryDto = new MemberWealRewordDTO();
                    queryDto.setUserId(member.getId());
                    queryDto.setVipLevel(i);
                    Long isPayCount = memberWealRewordService.findCountReword(queryDto);
                    log.info("不允许重复派发升级奖励:用户({0}),当前判断等级:VIP{1},是否发放:{2}", member.getId(), i, isPayCount);
                    if (isPayCount > 0) {
                        continue;
                    }
                }
                rewordAmount = rewordAmount.add(levelMap.get(i).getUpReward());
            }
            // 判断升级奖励金额是否大于0
            if (rewordAmount.compareTo(new BigDecimal("0")) > 0) {
                MemberWealRewordAddDTO saveRewordObj = new MemberWealRewordAddDTO();
                saveRewordObj.setUserId(member.getId());
                saveRewordObj.setUserName(member.getAccount());
                saveRewordObj.setOldLevel(beforeLevel);
                saveRewordObj.setCurrentLevel(afterLevel);
                saveRewordObj.setRewordAmount(rewordAmount);
                saveRewordObj.setType(0);
                saveRewordObj.setSerialNumber(String.valueOf(IdWorker.getId()));
                saveRewordObj.setParentId(member.getParentId());
                saveRewordObj.setParentName(member.getParentName());
                saveRewordObj.setAgentPath(member.getSuperPath());
                saveRewordObj.setUserType(member.getUserType());
                // 是否自动派发
                if (BooleanEnum.YES.match(growthConfig.getIsAutoPayReword())) {
                    synchronized (lockHelper) {
                        BigDecimal afterBlance = memberInfoService.lambdaQuery()
                                .eq(MemberInfo::getMemberId, member.getId())
                                .one()
                                .getBalance();

                        LambdaUpdateWrapper<MemberInfo> wrapper = new LambdaUpdateWrapper<>();
                        wrapper.set(MemberInfo::getBalance, afterBlance.add(rewordAmount))
                                .eq(MemberInfo::getMemberId, member.getId());
                        memberInfoService.update(wrapper);

                        String sourceId = String.valueOf(IdWorker.getId());
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
                        validWithdraw.setRemark(
                                member.getAccount() + "从VIP" + beforeLevel + "升至VIP" + afterLevel + "奖励金额增加打码量");
                        validWithdrawService.saveValidWithdraw(validWithdraw);

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
                memberWealRewordService.insertMemberWealReword(saveRewordObj);
            }
        }
        // 通知 发个人消息
        log.info("发送消息");
        MessageInfoAddDTO dto = new MessageInfoAddDTO();
        Message message = messageInfoConvert.toEntity(dto);
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

        log.info("发送消息成功");
    }

    public List<MemberGrowthRecord> findOne(MemberGrowthChangeDto dto) {
        return this.lambdaQuery()
                .eq(MemberGrowthRecord::getUserId, dto.getUserId())
                .orderByDesc(MemberGrowthRecord::getCreateTime)
                .list();
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
