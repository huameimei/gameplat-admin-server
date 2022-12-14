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
import org.springframework.context.annotation.Lazy;
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
 * @description vip???????????????????????????
 * @date 2021/11/24
 */
@Service
@Slf4j
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberGrowthRecordServiceImpl
        extends ServiceImpl<MemberGrowthRecordMapper, MemberGrowthRecord>
        implements MemberGrowthRecordService {

    public static final String kindName =
            "{\"en-US\": \"platform\", \"in-ID\": \"peron\", \"th-TH\": \"???????????????????????????\", \"vi-VN\": \"n???n t???ng\", \"zh-CN\": \"??????\"}";
    @Autowired
    private MemberGrowthRecordConvert recordConvert;

    @Autowired
    private MemberGrowthRecordMapper memberGrowthRecordMapper;

    @Resource(name = "memberGrowthLevelServiceImpl")
    @Lazy
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
    @Lazy
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
     * ??????????????????????????????????????????
     */
    @Override
    public Integer dealUpLevel(Long afterGrowth, MemberGrowthConfig memberGrowthConfig) {
    // 1.??????????????????????????????
    Integer limitLevel = Optional.ofNullable(memberGrowthConfig.getLimitLevel()).orElse(50);
        List<MemberGrowthLevelVO> levels = growthLevelService.findList(limitLevel);
        MemberGrowthLevelVO maxGrowthLevel = levels.get(levels.size() - 1);
        // ???????????????????????????????????????????????????  ???????????????????????????
        if (afterGrowth >= maxGrowthLevel.getGrowth()) {
            return maxGrowthLevel.getLevel();
        }

        for (MemberGrowthLevelVO level : levels) {
            if (afterGrowth < level.getGrowth()) {
                return level.getLevel();
            }
        }
        throw new ServiceException("???????????????????????????");
    }

    @Override
    public List<MemberGrowthRecord> findRecordGroupBy(MemberGrowthRecord entity) {
        return memberGrowthRecordMapper.findRecordGroupBy(entity);
    }

    /**
     * ??????????????????????????? ??????????????????
     */
    @Override
    public void editMemberGrowth(MemberGrowthChangeDto dto, HttpServletRequest request) {
    // ?????????????????????VIP
    // 2.?????????????????????
    MemberGrowthConfig growthConfig = memberGrowthConfigService.getOneConfig();
        if (growthConfig.getIsEnableVip() == 0) {
            log.info("?????????????????????????????????VIP??????");
            return;
        }
        // ??????id
        Long memberId = dto.getUserId();
        Member member = memberService.getById(memberId);
        MemberInfo memberInfo = memberInfoService.lambdaQuery().eq(MemberInfo::getMemberId, memberId).one();
        if (BeanUtil.isEmpty(member)) {
            log.info("VIP ??????????????? ???????????????");
            return;
        }
        // ???????????????
        Integer type = dto.getType();
//        if (dto.getChangeGrowth() == null) {
//            throw new ServiceException("?????????????????????????????????");
//        }
        // ??????????????????
        Long changeGrowth = dto.getChangeGrowth();

    // 1.?????????????????????????????????
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
        // ???????????????
//        Long oldGrowth = memberInfoService.lambdaQuery().eq(MemberInfo::getMemberId, dto.getUserId()).one().getVipGrowth();
        Long oldGrowth = memberGrowthRecord.getCurrentGrowth();
        // ?????????????????????  ??????????????????  ?????????????????????????????????????????????
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

    // 3.?????????????????????????????????
    if (type == GrowthChangeEnum.recharge.getCode()) {
            // ???????????????????????????
            if (BooleanEnum.YES.match(growthConfig.getIsEnableRecharge())) {
                // ???????????? ????????? ????????????
                changeFinalGrowth = changeGrowth;

                saveRecord.setKindName(kindName);
                saveRecord.setKindCode("plat");
                saveRecord.setChangeMult(growthConfig.getRechageRate());

                wrapperGrowthStatis.set(MemberGrowthStatis::getRechargeGrowth, changeFinalGrowth);

                saveGoldCoinRecord.setRemark(member.getAccount() + "??????????????????,??????????????????" + changeFinalGrowth + ",???????????????????????????" + growthConfig.getCoinRate());
            } else {
                return;
            }
        } else if (type == GrowthChangeEnum.sign.getCode()) {
            // ??????
            // ???????????????????????????
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
                //??????????????????
                saveGoldCoinRecord.setRemark(member.getAccount() + "??????????????????,??????????????????" + changeFinalGrowth + ",???????????????????????????" + growthConfig.getCoinRate());
            } else {
                return;
            }

        } else if (type == GrowthChangeEnum.dama.getCode()) {
            // ??????????????????????????????
            if (BooleanEnum.YES.match(growthConfig.getIsEnableDama())) {
                changeFinalGrowth =
                        growthConfig.getDamaRate().multiply(BigDecimal.valueOf(changeGrowth)).longValue();
                saveRecord.setKindName(dto.getKindName());
                saveRecord.setKindCode(dto.getKindCode());
                saveRecord.setChangeMult(
                        growthConfig.getDamaRate().multiply(dto.getChangeMult()).setScale(2));

                wrapperGrowthStatis.set(MemberGrowthStatis::getDamaGrowth, changeFinalGrowth);

                //??????????????????
                saveGoldCoinRecord.setRemark(member.getAccount() + "?????????????????????,??????????????????" + changeFinalGrowth + ",???????????????????????????" + growthConfig.getCoinRate());
            } else {
                return;
            }
        } else if (type == GrowthChangeEnum.backEdit.getCode()) {
            // ????????????
            changeFinalGrowth = changeGrowth;
            saveRecord.setKindName(kindName);
            saveRecord.setKindCode("plat");
            saveRecord.setChangeMult(new BigDecimal("1"));
            if (dto.getRemark() != null) {
                saveRecord.setRemark(dto.getRemark());
            }

            wrapperGrowthStatis.set(MemberGrowthStatis::getBackGrowth, changeFinalGrowth);

            //??????????????????
            saveGoldCoinRecord.setRemark(member.getAccount() + "?????????????????????????????????,??????????????????" + changeFinalGrowth + ",???????????????????????????" + growthConfig.getCoinRate());
        } else if (type == GrowthChangeEnum.finishInfo.getCode()) {
            // ????????????
        } else if (type == GrowthChangeEnum.bindBankCard.getCode()) {
            // ???????????????
            // ???????????????????????????????????????????????????
            if (this.lambdaQuery()
                    .eq(MemberGrowthRecord::getUserId, dto.getUserId())
                    .eq(MemberGrowthRecord::getType, GrowthChangeEnum.bindBankCard.getCode())
                    .list()
                    .size()
                    == 0) {
                changeFinalGrowth = growthConfig.getBindBankGrowth();

                //??????????????????
                saveGoldCoinRecord.setRemark(member.getAccount() + "??????????????????????????????,??????????????????" + changeFinalGrowth + ",???????????????????????????" + growthConfig.getCoinRate());
            }
        }
        //??????????????????????????????
        MemberGrowthLevel memberGrowthLevel = growthLevelService.getLevel(memberInfo.getVipLevel());
        Integer changeGoldCoin = 0;
        if (changeFinalGrowth > 0) {
            //??????????????????????????????
            changeGoldCoin = growthConfig.getCoinRate().multiply(BigDecimal.valueOf(changeFinalGrowth)).intValue();
        }
        saveGoldCoinRecord.setMemberId(memberId);
        saveGoldCoinRecord.setAccount(member.getAccount());
        saveGoldCoinRecord.setSourceType(1);
    saveGoldCoinRecord.setOrderNo(RandomUtil.randomNumbers(11));
        String userDailtKey = new StringBuilder().append("TENANT_USER_DAILY_COIN").append(":").append(member.getAccount()).toString();
        //???????????????????????????
        Integer dailyCoin = (Integer) redisTemplate.opsForValue().get(userDailtKey);
        //????????????????????????
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
            //????????????????????????????????????????????????????????????0????????????????????????????????????
            changeGoldCoin = 0;
        }

        // ????????????????????????
        memberGrowthRecord.setCurrentGrowth(memberGrowthRecord.getCurrentGrowth() + changeFinalGrowth);
        // ?????????????????????
        Integer beforeLevel = memberGrowthRecord.getCurrentLevel();
    // 4.????????????????????????????????? ?????? ?????????????????????
    if (memberGrowthRecord.getCurrentGrowth() < 0) {
            memberGrowthRecord.setCurrentGrowth(0L);
        }
        // ??????????????????????????????????????????
        Integer afterLevel = this.dealUpLevel(memberGrowthRecord.getCurrentGrowth(), growthConfig);
        MemberVO memberVo = memberService.queryList(new MemberQueryDTO() {{
            setId(memberId);
        }}).get(0);
        memberGrowthRecord.setCurrentLevel(afterLevel);
    // 5.???????????????????????????  ???????????? ?????????????????????
    Long tempGrowth = changeFinalGrowth;
        // ???????????????????????????
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
            throw new ServiceException("?????????????????????????????????");
        }


        memberGrowthStatisService.update(wrapperGrowthStatis);

    // 6.???????????????????????????????????? ????????????????????????
    LambdaUpdateWrapper<MemberInfo> wrapper = new LambdaUpdateWrapper<>();
        if (beforeLevel.compareTo(afterLevel) < 0) {
            this.dealPayUpReword(beforeLevel, afterLevel, growthConfig, member, request);
            // VIP??????????????????vip
            wrapper.set(MemberInfo::getVipLevel, afterLevel)
                    .set(MemberInfo::getVipGrowth, oldGrowth + changeFinalGrowth)
                    .set(MemberInfo::getGoldCoin, memberInfo.getGoldCoin() + changeGoldCoin)
                    .eq(MemberInfo::getMemberId, memberId);
            memberInfoService.update(wrapper);
        } else {
            // VIP??????????????????vip
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
     * ?????????
     */
    @Override
    public GrowthScaleVO progressBar(Integer level, Long memberId) {

        if (BeanUtil.isEmpty(memberService.getById(memberId))) {
            throw new ServiceException("??????????????????");
        }
        MemberGrowthLevel growthLevel = growthLevelService.getLevel(level);
        if (BeanUtil.isEmpty(growthLevel)) {
            throw new ServiceException("????????????????????????");
        }
        // ?????????????????????????????????
        Long growth = growthLevel.getGrowth();
        Long currentGrowth = 0L;
        // ???????????????
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
     * ????????????
     */
    @Override
    public void dealPayUpReword(
            Integer beforeLevel,
            Integer afterLevel,
            MemberGrowthConfig growthConfig,
            Member member,
            HttpServletRequest request) {
        // ?????????????????????????????????
        List<MemberBlackList> memberBlackList =
                memberBlackListService.findMemberBlackList(
                        new MemberBlackList() {
                            {
                                setUserAccount(member.getAccount());
                            }
                        });
        String content = "VIP????????????";
        // ???????????????????????? ???????????????????????????
        if (BooleanEnum.YES.match(growthConfig.getIsPayUpReword()) && memberBlackList.size() <= 0) {
            BigDecimal rewordAmount = new BigDecimal("0");
            // ?????????????????????????????????  ?????????????????????
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
            // ???????????????????????????????????? 0 ?????????????????????   1 ??????????????????
            Integer isRepeatPayUpReword =
                    growthConfig.getIsRepeatPayUpReword() == null ? 0 : growthConfig.getIsRepeatPayUpReword();
            // ??????????????????
            for (int i = beforeLevel + 1; i <= afterLevel; i++) {
                // ??????????????????????????????????????? ???????????????i??????????????????
                if (isRepeatPayUpReword == 0) {
                    // ?????????????????????i??????????????????
                    MemberWealRewordDTO queryDto = new MemberWealRewordDTO();
                    queryDto.setUserId(member.getId());
                    queryDto.setVipLevel(i);
                    Long isPayCount = memberWealRewordService.findCountReword(queryDto);
                    log.info("?????????????????????????????????:??????({0}),??????????????????:VIP{1},????????????:{2}", member.getId(), i, isPayCount);
                    if (isPayCount > 0) {
                        continue;
                    }
                }
                rewordAmount = rewordAmount.add(levelMap.get(i).getUpReward());
            }
            // ????????????????????????????????????0
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
                // ??????????????????
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
                        // ?????????????????????
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
                                member.getAccount() + "???VIP" + beforeLevel + "??????VIP" + afterLevel + "???????????????????????????");
                        validWithdrawService.saveValidWithdraw(validWithdraw);

                        // ??????????????????
                        MemberBill memberBill = new MemberBill();
                        memberBill.setMemberId(member.getId());
                        memberBill.setAccount(member.getAccount());
                        memberBill.setMemberPath(member.getSuperPath());
                        memberBill.setTranType(TranTypes.UPGRADE_REWARD.getValue());
                        memberBill.setOrderNo(sourceId);
                        memberBill.setAmount(rewordAmount);
                        memberBill.setBalance(afterBlance);
                        memberBill.setRemark(
                                member.getAccount() + "???VIP" + beforeLevel + "??????VIP" + afterLevel + "????????????");
                        memberBill.setContent(
                                member.getAccount() + "???VIP" + beforeLevel + "??????VIP" + afterLevel + "????????????");
                        memberBill.setOperator("system");
                        memberBillService.save(memberBill);

                        saveRewordObj.setStatus(2);
                        content = "VIP??????????????????????????? ??????:" + rewordAmount;
                        saveRewordObj.setDrawTime(DateTime.now());
                    }

                } else {
                    saveRewordObj.setStatus(1);
                    content = "VIP??????????????????????????? ??????:" + rewordAmount + "????????????";
                }
                log.info("??????????????????");
                memberWealRewordService.insertMemberWealReword(saveRewordObj);
            }
        }
        // ?????? ???????????????
        log.info("????????????");
        MessageInfoAddDTO dto = new MessageInfoAddDTO();
        Message message = messageInfoConvert.toEntity(dto);
        message.setTitle("VIP????????????");
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

        log.info("??????????????????");
    }

    public List<MemberGrowthRecord> findOne(MemberGrowthChangeDto dto) {
        return this.lambdaQuery()
                .eq(MemberGrowthRecord::getUserId, dto.getUserId())
                .orderByDesc(MemberGrowthRecord::getCreateTime)
                .list();
    }

    //???????????????????????????
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
