package com.gameplat.admin.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.constant.MemberServiceKeyConstant;
import com.gameplat.admin.enums.ActivityDistributeEnum;
import com.gameplat.admin.enums.FinancialModeEnum;
import com.gameplat.admin.enums.MemberWealRewordEnums;
import com.gameplat.admin.enums.PushMessageEnum;
import com.gameplat.admin.mapper.ActivityDistributeMapper;
import com.gameplat.admin.model.domain.*;
import com.gameplat.admin.model.dto.PushMessageAddDTO;
import com.gameplat.admin.model.vo.MemberInfoVO;
import com.gameplat.admin.service.*;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.IPUtils;
import com.gameplat.redis.redisson.DistributedLocker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author aBen
 * @date 2021/12/12 21:06
 * @desc
 */
@Service
public class ActivityDistributeWayService extends ServiceImpl<ActivityDistributeMapper, ActivityDistribute> {

    @Autowired
    private MemberWealRewordService memberWealRewordService;

    @Autowired
    private DistributedLocker distributedLocker;

    @Autowired
    private MemberInfoService memberInfoService;

    @Autowired
    private ActivityQualificationService activityQualificationService;

    @Autowired
    private ValidWithdrawService validWithdrawService;

    @Autowired
    private FinancialService financialService;

    @Autowired
    private PushMessageService pushMessageService;

    @Autowired
    private MemberService memberService;

    @Transactional(rollbackFor = Throwable.class)
    public void directRelease(ActivityDistribute activityDistribute, MemberWealReword wealReword) {
        // 账户资金锁
        String lockKey = MessageFormat.format(MemberServiceKeyConstant.MEMBER_FINANCIAL_LOCK, activityDistribute.getUsername());
        try {
            // 获取资金锁（等待6秒，租期120秒）
            boolean flag = distributedLocker.tryLock(lockKey, TimeUnit.SECONDS, 6, 120);
            // 6秒获取不到资金锁，派发下一个会员
            if (!flag) {
                throw new ServiceException("获取资金锁失败");
            }
            ActivityDistribute activityDistribute1 = this.getById(activityDistribute.getDistributeId());
            if (activityDistribute1.getStatus() != ActivityDistributeEnum.ActivityDistributeStatus.SETTLEMENT.getValue()) {
                throw new ServiceException("该记录状态不是未结算状态");
            }
            // 初始化参数
            String sourceId = activityDistribute.getQualificationActivityId().toString();
            Integer status = null;
            String remark = null;
            BigDecimal amount = null;
            //获取会员信息
            MemberInfoVO memberInfoVO = memberService.getMemberInfo(wealReword.getUserName());
            amount = memberInfoVO.getBalance() == null ? BigDecimal.ZERO : memberInfoVO.getBalance();
            //更新用户的金额
            memberInfoService.updateBalance(activityDistribute.getUserId(), activityDistribute.getDiscountsMoney());

            //账号余额更新成功,修改派发状态
            ActivityDistribute distribute = new ActivityDistribute();
            distribute.setDistributeId(activityDistribute.getDistributeId());
            distribute.setStatus(ActivityDistributeEnum.ActivityDistributeStatus.SETTLED.getValue());
            distribute.setSettlementTime(new Date());
            this.updateById(distribute);

            //修改资格使用时间和次数
            ActivityQualification activityQualification = new ActivityQualification();
            activityQualification.setQualificationActivityId(activityDistribute.getQualificationActivityId());
            activityQualification.setEmployNum(1);
            activityQualification.setEmployTime(new Date());
            activityQualificationService.updateQualificationStatus(activityQualification);

            // 现金流水信息
            status = 3;
            remark = MessageFormat.format("会员:{0},活动派发:{1},受理成功",
                    activityDistribute.getUsername(), activityDistribute.getDiscountsMoney());
            if (NumberUtil.isGreater(new BigDecimal(activityDistribute.getWithdrawDml()), BigDecimal.ZERO)) {
                // 新增打码量记录
                ValidWithdraw validWithdraw = new ValidWithdraw();
                validWithdraw.setAccount(activityDistribute.getUsername());
                validWithdraw.setMemberId(activityDistribute.getUserId());
                validWithdraw.setRechId(sourceId);
                validWithdraw.setRechMoney(activityDistribute.getDiscountsMoney().setScale(2, RoundingMode.HALF_UP));
                validWithdraw.setMormDml(new BigDecimal(activityDistribute.getWithdrawDml()).setScale(2, RoundingMode.HALF_UP));

                validWithdraw.setType(0);
                validWithdraw.setStatus(0);
                validWithdraw.setDiscountMoney(BigDecimal.ZERO);
                validWithdraw.setRemark(getDistributeRemark(activityDistribute));

                validWithdrawService.save(validWithdraw);
            }
            // 新增现金流水
            Financial financial = new Financial();
            financial.setUsername(activityDistribute.getUsername());
            financial.setBeforeBalance(amount.setScale(2, RoundingMode.HALF_UP));
            financial.setAmount(activityDistribute.getDiscountsMoney().setScale(2, RoundingMode.HALF_UP));
            financial.setAfterBalance(status == 0 ? amount.setScale(2, RoundingMode.HALF_UP) : amount.add(activityDistribute.getDiscountsMoney()).setScale(2, RoundingMode.HALF_UP));
            financial.setSourceType(50);
            financial.setSourceId(String.valueOf(sourceId));
            financial.setState(status);
            financial.setMode(FinancialModeEnum.BACKSTAGE_DEPOSIT.getValue());
            financial.setRemark(remark);
            financial.setCreateTime(new Date());
            financial.setUserId(activityDistribute.getUserId());
            financial.setCurrencyName("真币");
            financial.setCurrencyType(1);
            financial.setIpAddress(IPUtils.getHostIp());
            financial.setIsLess(0);
            //记录现金流水
            financialService.insertFinancial(financial);

            //插入福利中心记录(已领取)
            wealReword.setStatus(MemberWealRewordEnums.MemberWealRewordStatus.COMPLETED.getValue());
            memberWealRewordService.save(wealReword);

            //通知 发个人消息
            PushMessageAddDTO pushMessage = new PushMessageAddDTO();
            pushMessage.setMessageContent(getInformationContent(activityDistribute));
            pushMessage.setMessageTitle("活动派发");
            pushMessage.setUserRange(PushMessageEnum.UserRange.SOME_MEMBERS.getValue());
            pushMessage.setUserAccount(activityDistribute.getUsername());
            pushMessageService.insertPushMessage(pushMessage);
        } finally {
            // 释放资金锁
            distributedLocker.unlock(lockKey);
        }
    }

    @Transactional
    public void welfareCenter(ActivityDistribute activityDistribute, MemberWealReword wealReword) {
        wealReword.setStatus(MemberWealRewordEnums.MemberWealRewordStatus.UNACCALIMED.getValue());
        memberWealRewordService.save(wealReword);

        //账变成功,修改派发状态
        ActivityDistribute distribute = new ActivityDistribute();
        distribute.setDistributeId(activityDistribute.getDistributeId());
        distribute.setStatus(ActivityDistributeEnum.ActivityDistributeStatus.SETTLED.getValue());
        distribute.setSettlementTime(new Date());
        this.updateById(distribute);

        //修改资格使用时间和次数
        ActivityQualification activityQualification = new ActivityQualification();
        activityQualification.setQualificationActivityId(activityDistribute.getQualificationActivityId());
        activityQualification.setEmployNum(1);
        activityQualification.setEmployTime(new Date());
        activityQualificationService.updateQualificationStatus(activityQualification);

        //通知 发个人消息
        PushMessageAddDTO pushMessage = new PushMessageAddDTO();
        pushMessage.setMessageContent(getInformationContent(activityDistribute));
        pushMessage.setMessageTitle("活动派发");
        pushMessage.setUserRange(PushMessageEnum.UserRange.SOME_MEMBERS.getValue());
        pushMessage.setUserAccount(activityDistribute.getUsername());
        pushMessageService.insertPushMessage(pushMessage);
    }

    /**
     * 派发说明
     *
     * @param distributeVO
     * @return
     */
    private String getDistributeRemark(ActivityDistribute distributeVO) {
        StringBuilder str = new StringBuilder("用户:")
                .append(distributeVO.getUsername())
                .append(",活动派发:")
                .append(distributeVO.getDiscountsMoney())
                .append(",打码量要求:")
                .append(distributeVO.getWithdrawDml())
                .append("。");
        return str.toString();
    }

    /**
     * 生成消息字符串
     *
     * @param distributeVO
     * @return
     */
    private String getInformationContent(ActivityDistribute distributeVO) {
        StringBuilder str = new StringBuilder();
        if (ActivityDistributeEnum.ActivityDistributeGetWayEnum.DIRECT_RELEASE.getValue() == distributeVO.getGetWay()) {
            str.append(distributeVO.getUsername())
                    .append("奖励已派发,金额:")
                    .append(distributeVO.getDiscountsMoney())
                    .append("。");
        } else if (ActivityDistributeEnum.ActivityDistributeGetWayEnum.WELFARE_CENTER.getValue() == distributeVO.getGetWay()) {
            str.append(distributeVO.getUsername())
                    .append(",奖励已发送到福利中心,请到福利中心领取,金额:")
                    .append(distributeVO.getDiscountsMoney())
                    .append("。");
        }
        return str.toString();
    }
}
