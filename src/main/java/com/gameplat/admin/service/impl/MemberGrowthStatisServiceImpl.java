package com.gameplat.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.constant.TrueFalse;
import com.gameplat.admin.convert.MemberGrowthStatisConvert;
import com.gameplat.admin.enums.BlacklistConstant;
import com.gameplat.admin.enums.PushMessageEnum;
import com.gameplat.admin.mapper.BizBlacklistMapper;
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
import com.gameplat.model.entity.blacklist.BizBlacklist;
import com.gameplat.model.entity.member.*;
import com.gameplat.model.entity.message.Message;
import com.gameplat.model.entity.recharge.RechargeOrder;
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
 * @description vip???????????????????????????
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

  @Autowired private BizBlacklistMapper blacklistMapper;

    public static final Object lockHelper = new Object();

    public static final String kindName = "{\"en-US\": \"platform\", \"in-ID\": \"peron\", \"th-TH\": \"???????????????????????????\", \"vi-VN\": \"n???n t???ng\", \"zh-CN\": \"??????\"}";

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
     * ?????????????????? ???????????????????????????
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
            find.setCreateBy("???????????????");
      // ????????????????????????0
      int i = memberGrowthStatisMapper.insertOrUpdate(find);
            if (i <= 0) {
                throw new ServiceException("?????????????????????????????????????????????");
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
     * ???????????????
     */
//    @Transactional
    @Override
    public void changeGrowth(MemberGrowthChangeDto dto) {

    // ?????????????????????VIP
    // 2.?????????????????????
    MemberGrowthConfig growthConfig = memberGrowthConfigService.getOneConfig();

        if (growthConfig.getIsEnableVip() == 0) {
            log.info("?????????????????????-?????????VIP");
            return;
        }
        Long memberId = dto.getUserId();
        Member member = memberService.getById(memberId);
        if (BeanUtil.isEmpty(member)) {
            log.info("VIP ??????????????? ???????????????");
            return;
        }
        log.info("???????????????:" + dto.toString());
        //???????????????
        Integer type = dto.getType();
        //??????????????????
        Long changeGrowth = dto.getChangeGrowth();

    // 1.?????????????????????????????????
    MemberGrowthStatis memberGrowthStatis = this.findOrInsert(memberId, member.getAccount());
        //??????????????????
        Long oldGrowth = memberGrowthStatis.getVipGrowth();

        //?????????????????????
        Long changeFinalGrowth = 0L;

        MemberGrowthRecord saveRecord = new MemberGrowthRecord();

        //??????????????????
        MemberGoldCoinRecord savaGoldCoinRecord = new MemberGoldCoinRecord();

    // 3.?????????????????????????????????
    if (type == GrowthChangeEnum.recharge.getCode()) {
            //???????????????????????????
            if (growthConfig.getIsEnableRecharge() == EnableEnum.ENABLED.code()) {
                //???????????? ????????? ????????????
                changeFinalGrowth = changeGrowth;

                memberGrowthStatis.setRechargeGrowth(memberGrowthStatis.getRechargeGrowth() + changeFinalGrowth);

                saveRecord.setKindName(kindName);
                saveRecord.setKindCode("plat");
                saveRecord.setChangeMult(growthConfig.getRechageRate());

                //??????????????????
                savaGoldCoinRecord.setRemark(member.getAccount() + "??????????????????,??????????????????" + changeFinalGrowth + ",???????????????????????????" + growthConfig.getCoinRate());
            } else {
                return;
            }
        } else if (type == GrowthChangeEnum.sign.getCode()) {
            //???????????????????????????
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

                //??????????????????
                savaGoldCoinRecord.setRemark(member.getAccount() + "??????????????????,??????????????????" + changeFinalGrowth + ",???????????????????????????" + growthConfig.getCoinRate());
            } else {
                return;
            }
        } else if (type == GrowthChangeEnum.dama.getCode()) {
            //??????????????????????????????
            if (growthConfig.getIsEnableDama() == EnableEnum.ENABLED.code()) {
                // ?????????????????????????????????????????????????????????
                List<MemberGrowthRecord> list = memberGrowthRecordService.findRecordList(new MemberGrowthRecordDTO() {{
                    setMemberId(memberId);
                    setType(GrowthChangeEnum.dama.getCode());
                    setKindCode(dto.getKindCode());
                    setCalcTime(DateUtil.today());
                }});

                if (CollectionUtil.isNotEmpty(list)) {
                    log.info("??????ID==={}===??????==={}???????????????????????????,????????????", memberId, DateUtil.today());
                    return;
                }

                changeFinalGrowth = growthConfig.getDamaRate().multiply(BigDecimal.valueOf(changeGrowth)).longValue();
                memberGrowthStatis.setDamaGrowth(memberGrowthStatis.getDamaGrowth() + changeFinalGrowth);
                saveRecord.setKindName(dto.getKindName());
                saveRecord.setKindCode(dto.getKindCode());
                saveRecord.setChangeMult(growthConfig.getDamaRate().multiply(dto.getChangeMult()).setScale(2));
                //??????????????????
                savaGoldCoinRecord.setRemark(member.getAccount() + "?????????????????????,??????????????????" + changeFinalGrowth + ",???????????????????????????" + growthConfig.getCoinRate());
            } else {
                return;
            }
        } else if (type == GrowthChangeEnum.backEdit.getCode()) {
            //????????????
            changeFinalGrowth = changeGrowth;

            memberGrowthStatis.setBackGrowth(memberGrowthStatis.getBackGrowth() + changeFinalGrowth);

            saveRecord.setKindName(kindName);
            saveRecord.setKindCode("plat");
            saveRecord.setChangeMult(new BigDecimal("1"));
            if (dto.getRemark() != null) {
                saveRecord.setRemark(dto.getRemark());
            }
            //??????????????????
            savaGoldCoinRecord.setRemark(member.getAccount() + "?????????????????????????????????,??????????????????" + changeFinalGrowth + ",???????????????????????????" + growthConfig.getCoinRate());
        } else if (type == GrowthChangeEnum.finishInfo.getCode()) {
            //????????????
        } else if (type == GrowthChangeEnum.bindBankCard.getCode()) {
            //???????????????????????????????????????????????????
            if (memberGrowthStatis.getBindGrowth() == 0) {
                changeFinalGrowth = growthConfig.getBindBankGrowth();
                memberGrowthStatis.setBindGrowth(changeFinalGrowth);

                //??????????????????
                savaGoldCoinRecord.setRemark(member.getAccount() + "??????????????????????????????,??????????????????" + changeFinalGrowth + ",???????????????????????????" + growthConfig.getCoinRate());
            }
        }

        //??????????????????
        MemberGrowthLevel memberGrowthLevel = growthLevelService.getLevel(memberGrowthStatis.getVipLevel());
        Integer changeGoldCoin = growthConfig.getCoinRate().multiply(BigDecimal.valueOf(changeFinalGrowth)).intValue();
        savaGoldCoinRecord.setMemberId(memberId);
        savaGoldCoinRecord.setAccount(member.getAccount());
        savaGoldCoinRecord.setSourceType(1);
    savaGoldCoinRecord.setOrderNo(RandomUtil.randomNumbers(11));
        savaGoldCoinRecord.setCreateBy(member.getAccount());

        String userDailtKey = new StringBuilder().append("TENANT_USER_DAILY_COIN").append(":").append(member.getAccount()).toString();
        //???????????????????????????
        Integer dailyCoin = (Integer) redisTemplate.opsForValue().get(userDailtKey);
        //????????????????????????
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
            //????????????????????????????????????????????????????????????0????????????????????????????????????
            changeGoldCoin = 0;
        }

        //????????????????????????
        memberGrowthStatis.setVipGrowth(memberGrowthStatis.getVipGrowth() + changeFinalGrowth);

        //????????????????????????
        Integer beforeLevel = memberGrowthStatis.getVipLevel();

    // 4.????????????????????????????????? ?????? ?????????????????????
    if (memberGrowthStatis.getVipGrowth() < 0L) {
            memberGrowthStatis.setVipGrowth(0L);
        }
        Integer afterLevel = this.dealUpLevel(memberGrowthStatis.getVipGrowth(), growthConfig);
        memberGrowthStatis.setVipLevel(afterLevel);

    // 5.???????????????????????????  ???????????? ?????????????????????
    Long tempGrowth = changeFinalGrowth;
        //???????????????????????????
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

        //?????????????????????
        log.info("VIP?????????????????????" + memberGrowthStatis);
        this.insertOrUpdate(memberGrowthStatis);

    // 6.???????????????????????????????????? ????????????????????????
    if (beforeLevel.compareTo(afterLevel) < 0) {
            this.dealPayUpReword(beforeLevel, afterLevel, growthConfig, member);

            //VIP??????????????????vip
            LambdaUpdateWrapper<MemberInfo> wrapper = new LambdaUpdateWrapper<>();
            wrapper.set(MemberInfo::getVipGrowth, memberInfo.getVipGrowth() + changeFinalGrowth)
                    .set(MemberInfo::getVipLevel, afterLevel)
                    .set(MemberInfo::getGoldCoin, memberInfo.getGoldCoin() + changeGoldCoin)
                    .eq(MemberInfo::getMemberId, memberId);
            memberInfoService.update(wrapper);
        } else {
            //VIP??????????????????vip
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
     * ???????????????????????????  ??????????????????
     */
    @Override
    public BigDecimal calcRewordAmount(Integer beforeLevel, Integer afterLevel, MemberGrowthConfig growthConfig) {
        //?????????????????????????????????  ?????????????????????
        Integer limitLevel = growthConfig.getLimitLevel();
        if (limitLevel == null) {
            limitLevel = 50;
        }
        List<MemberGrowthLevel> levels = growthLevelService.getList(limitLevel);
        Map<Integer, MemberGrowthLevel> levelMap = levels.stream().collect(Collectors.toMap(MemberGrowthLevel::getLevel, MemberGrowthLevel -> MemberGrowthLevel));
        //??????????????????
        BigDecimal rewordAmount = new BigDecimal("0");
        for (int i = beforeLevel + 1; i <= afterLevel; i++) {
            rewordAmount = rewordAmount.add(levelMap.get(i).getUpReward());
        }
        return rewordAmount;
    }

    /**
     * ????????????
     */
    @Override
    public void dealPayUpReword(Integer beforeLevel, Integer afterLevel, MemberGrowthConfig growthConfig, Member member) {
    boolean blackFlag = this.checkBizBlack(member);
        String content = "VIP????????????";
    // ???????????????????????? ???????????????????????????
    if (BooleanEnum.YES.match(growthConfig.getIsPayUpReword()) && blackFlag) {

            BigDecimal rewordAmount = new BigDecimal("0");

            //?????????????????????????????????  ?????????????????????
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

            log.info("VIP????????????:" + growthConfig.toString());

            //???????????????????????????????????? 0 ?????????????????????   1 ??????????????????
            Integer isRepeatPayUpReword =
                    growthConfig.getIsRepeatPayUpReword() == null ? 0 : growthConfig.getIsRepeatPayUpReword();

            //??????????????????
            for (int i = beforeLevel + 1; i <= afterLevel; i++) {
                //??????????????????????????????????????? ???????????????i??????????????????
                if (isRepeatPayUpReword == 0) {
                    //?????????????????????i??????????????????
                    MemberWealRewordDTO queryDto = new MemberWealRewordDTO();
                    queryDto.setUserId(member.getId());
                    queryDto.setVipLevel(i);
                    Long isPayCount = memberWealRewordService.findCountReword(queryDto);
                    log.info("?????????????????????????????????:??????({0}),??????????????????:VIP{1},????????????:{2}"
                            , member.getId(), i, isPayCount);

                    if (isPayCount > 0) {
                        continue;
                    }
                }
                rewordAmount = rewordAmount.add(levelMap.get(i).getUpReward());
            }
            //????????????????????????????????????0
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

                //??????????????????
                if (EnableEnum.ENABLED.match(growthConfig.getIsAutoPayReword())) {
          BigDecimal payUpRewordDama =
              growthConfig.getPayUpRewordDama() == null
                  ? BigDecimal.ONE
                  : growthConfig.getPayUpRewordDama();
          BigDecimal bigDecimal = rewordAmount.setScale(2, RoundingMode.HALF_UP);
          String sourceId = RandomUtil.randomNumbers(22);
          // ???????????????
          RechargeOrder rechargeOrder = new RechargeOrder();
          rechargeOrder.setMemberId(member.getId());
          rechargeOrder.setAmount(BigDecimal.ZERO);
          rechargeOrder.setNormalDml(BigDecimal.ZERO);
          rechargeOrder.setDiscountAmount(bigDecimal);
          rechargeOrder.setDiscountDml(bigDecimal.multiply(payUpRewordDama));
          rechargeOrder.setRemarks(
              member.getAccount() + "???VIP" + beforeLevel + "??????VIP" + afterLevel + "???????????????????????????");
          rechargeOrder.setAccount(member.getAccount());
          validWithdrawService.addRechargeOrder(rechargeOrder);

                    synchronized (lockHelper) {
                        MemberInfo memberInfo = memberInfoService.lambdaQuery()
                                .eq(MemberInfo::getMemberId, member.getId())
                                .one();
                        BigDecimal afterBlance = memberInfo.getBalance();
            memberInfoService.updateBalance(member.getId(), rewordAmount);

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

                saveRewordObj.setParentId(member.getParentId());
                saveRewordObj.setParentName(member.getParentName());
                saveRewordObj.setAgentPath(member.getSuperPath());
                saveRewordObj.setUserType(member.getUserType());

                memberWealRewordService.insertMemberWealReword(saveRewordObj);
            }
        }
        //?????? ???????????????
        Message message = new Message();
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
    }

    /**
     * ?????? ???????????????
     */
    @Override
    @Async
    public void asyncChangeGrowth(MemberGrowthChangeDto dto) {
        changeGrowth(dto);
    }

    /**
     * ???????????????????????????
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

  public boolean checkBizBlack(Member member) {
    // ???????????? ??????????????? ??????
    QueryWrapper<BizBlacklist> queryBizWrapper = new QueryWrapper<>();
    queryBizWrapper.like(
        "types", BlacklistConstant.BizBlacklistType.LEVEL_UPGRADE_REWARD.getValue());
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
    boolean blackFlag = true;
    if (accountBlacks.contains(member.getAccount())
        || userLevelBlacks.contains(member.getUserLevel().toString())) {
      blackFlag = false;
      log.info("?????????{}????????????????????????", member.getAccount());
    }
    return blackFlag;
  }
}
