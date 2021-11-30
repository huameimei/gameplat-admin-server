package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.ActivityDistributeConvert;
import com.gameplat.admin.mapper.ActivityDistributeMapper;
import com.gameplat.admin.model.domain.ActivityDistribute;
import com.gameplat.admin.model.domain.MemberWealReword;
import com.gameplat.admin.model.dto.ActivityDistributeDTO;
import com.gameplat.admin.model.vo.ActivityDistributeVO;
import com.gameplat.admin.service.ActivityDistributeService;
import com.gameplat.admin.service.MemberInfoService;
import com.gameplat.admin.service.MemberWealRewordService;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.util.StringUtils;
import com.gameplat.redis.redisson.DistributedLocker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 活动分发类
 *
 * @author admin
 */
@Slf4j
@Service
public class ActivityDistributeServiceImpl
        extends ServiceImpl<ActivityDistributeMapper, ActivityDistribute>
        implements ActivityDistributeService {

    @Autowired
    private ActivityDistributeConvert activityDistributeConvert;

    @Autowired
    private MemberWealRewordService memberWealRewordService;

    @Autowired
    private DistributedLocker distributedLocker;

    @Autowired
    private MemberInfoService memberInfoService;

    @Override
    public List<ActivityDistribute> findActivityDistributeList(ActivityDistribute activityDistribute) {
        return this.lambdaQuery()
                .eq(activityDistribute.getActivityId() != null && activityDistribute.getActivityId() != 0
                        , ActivityDistribute::getActivityId, activityDistribute.getActivityId())
                .eq(activityDistribute.getDeleteFlag() != null && activityDistribute.getDeleteFlag() != 0
                        , ActivityDistribute::getDeleteFlag, activityDistribute.getDeleteFlag())
                .eq(activityDistribute.getStatus() != null && activityDistribute.getStatus() != 0
                        , ActivityDistribute::getStatus, activityDistribute.getStatus())
                .list();
    }

    @Override
    public void deleteByLobbyIds(String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new ServiceException("ids不能为空");
        }
        String[] idArr = ids.split(",");
        List<Long> idList = new ArrayList<>();
        for (String idStr : idArr) {
            idList.add(Long.parseLong(idStr));
        }
        this.removeByIds(idList);
    }

    @Override
    public boolean saveBatch(List<ActivityDistribute> activityDistributeList) {
        return this.saveBatch(activityDistributeList);
    }

    @Override
    public IPage<ActivityDistributeVO> list(PageDTO<ActivityDistribute> page, ActivityDistributeDTO activityDistributeDTO) {
        LambdaQueryChainWrapper<ActivityDistribute> lambdaQuery = this.lambdaQuery();
        lambdaQuery.like(StringUtils.isNotBlank(activityDistributeDTO.getUsername())
                , ActivityDistribute::getUsername, activityDistributeDTO.getUsername())
                .eq(activityDistributeDTO.getActivityId() != null && activityDistributeDTO.getActivityId() != 0
                        , ActivityDistribute::getActivityId, activityDistributeDTO.getActivityId())
                .eq(activityDistributeDTO.getStatus() != null && activityDistributeDTO.getStatus() != 0
                        , ActivityDistribute::getStatus, activityDistributeDTO.getStatus())
                .eq(activityDistributeDTO.getGetWay() != null && activityDistributeDTO.getGetWay() != 0
                        , ActivityDistribute::getGetWay, activityDistributeDTO.getGetWay())
                .ge(StringUtils.isNotBlank(activityDistributeDTO.getApplyStartTime())
                        , ActivityDistribute::getApplyTime, activityDistributeDTO.getApplyStartTime())
                .le(StringUtils.isNotBlank(activityDistributeDTO.getApplyEndTime())
                        , ActivityDistribute::getApplyTime, activityDistributeDTO.getApplyEndTime())
        ;
        return lambdaQuery.page(page).convert(activityDistributeConvert::toVo);
    }

    @Override
    public void updateStatus(String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new ServiceException("ids不能为空");
        }
        List<ActivityDistribute> activityDistributeList = this.lambdaQuery()
                .in(ActivityDistribute::getDistributeId, ids.split(",")).list();
        if (CollectionUtils.isNotEmpty(activityDistributeList)) {
            for (ActivityDistribute activityDistribute : activityDistributeList) {
                if (activityDistribute.getStatus() == 0) {
                    throw new ServiceException("无效状态不能结算！");
                }
                if (activityDistribute.getStatus() == 2) {
                    throw new ServiceException("已结算的不能重复结算！");
                }
            }
        }

        log.info("开始派发活动奖励", System.currentTimeMillis());
        //修改用户真币、资金流水
        for (ActivityDistribute activityDistribute : activityDistributeList) {
            Integer getWay = activityDistribute.getGetWay();
            //福利中心记录
            MemberWealReword wealReword = new MemberWealReword();
            wealReword.setUserId(activityDistribute.getUserId());
            wealReword.setUserName(activityDistribute.getUsername());
            wealReword.setRewordAmount(activityDistribute.getDiscountsMoney());
            wealReword.setWithdrawDml(new BigDecimal(activityDistribute.getWithdrawDml()));
            wealReword.setType(5);//5 活动大厅奖励
            wealReword.setSerialNumber(activityDistribute.getDistributeId().toString());
            wealReword.setActivityTitle(activityDistribute.getActivityName());

            //如果领取方式是1,则直接派发金额到会员账户
//            if (getWay == 1) {
//                // 账户资金锁
//                String lockKey = MessageFormat.format(MemberServiceKeyConstant.MEMBER_FINANCIAL_LOCK, activityDistribute.getUsername());
//                try {
//                    // 获取资金锁（等待6秒，租期120秒）
//                    boolean flag = distributedLocker.tryLock(lockKey, TimeUnit.SECONDS, 6, 120);
//                    // 6秒获取不到资金锁，派发下一个会员
//                    if (!flag) {
//                        continue;
//                    }
//                    ActivityDistribute activityDistribute1 = this.getById(activityDistribute.getDistributeId());
//                    if (activityDistribute1.getStatus() != 1) {
//                        continue;
//                    }
//                    // 初始化参数
//                    String sourceId = activityDistribute.getQualificationActivityId();
//                    Integer status = null;
//                    String remark = null;
//                    Double amount = null;
////                    获取用户余额
//                    BigDecimal userbalance = memberInfoService.getUserbalance(activityDistribute.getUsername());
//                    amount = userbalance.doubleValue();
//                    // 账变
//                    Map<String, String> map1 = new HashMap<>();
//                    map1.put("type", "1");
//                    map1.put("money", String.valueOf(activityDistribute.getDiscountsMoney()));
//                    map1.put("id", activityDistribute.getUserId().toString());
////                    map1.put("nonce", IdWorker.getInstance().nextSId());
//                    map1.put("kind", String.valueOf(14));
//                    AjaxResult addMoenyAjaxResult = sysUserFeignService.addMoeny(map1);
//                    JSONObject object1 = (JSONObject) JSONObject.toJSON(addMoenyAjaxResult);
//                    if (null == object1 || !"200".equals(object1.getString("code"))) {
//                        throw new ServiceException("活动派发账变异常");
//                    }
//                    //账变成功,修改派发状态
//                    ActivityDistribute distribute = new ActivityDistribute();
//                    distribute.setDistributeId(activityDistribute.getDistributeId());
//                    distribute.setStatus(2);
//                    distribute.setUpdateBy(SysUserUtil.getUserName());
//                    distribute.setSettlementTime(new Date());
//                    activityDistributeDao.updateByPrimaryKeySelective(distribute);
//                    //修改资格使用时间和次数
//                    QualificationManage qualificationManage = new QualificationManage();
//                    qualificationManage.setQualificationActivityId(activityDistribute.getQualificationActivityId());
//                    qualificationManage.setEmployNum(1);
//                    qualificationManage.setEmployTime(new Date());
//                    qualificationManageDao.updateQualificationManage(qualificationManage);
//
//                    // 现金流水信息
//                    status = 3;
//                    remark = MessageFormat.format("会员:{0},活动派发:{1},受理成功",
//                            activityDistribute.getUsername(), activityDistribute.getDiscountsMoney());
//                    if (NumberUtil.isGreater(new BigDecimal(activityDistribute.getWithdrawDml()), BigDecimal.ZERO)) {
//                        // 新增打码量记录
//                        ValidWithdraw validWithdraw = new ValidWithdraw();
//                        validWithdraw.setUserAccount(activityDistribute.getUsername());
//                        validWithdraw.setUserId(activityDistribute.getUserId());
//                        validWithdraw.setRechId(sourceId);
//                        validWithdraw.setRechMoney(activityDistribute.getDiscountsMoney().setScale(2, RoundingMode.HALF_UP));
//                        validWithdraw.setDmlClaim(new BigDecimal(activityDistribute.getWithdrawDml()).setScale(2, RoundingMode.HALF_UP));
//                        validWithdraw.setAddTime(new Date());
//                        validWithdraw.setType("0");
//                        validWithdraw.setStatus("0");
//                        validWithdraw.setDiscountMoney(BigDecimal.ZERO);
//                        validWithdraw.setRemark(getDistributeRemark(activityDistribute));
//                        validWithdrawfeignService.saveValidWithdraw(validWithdraw);
//                    }
//                    // 新增现金流水
//                    Financial2 financialParam = new Financial2();
//                    financialParam.setUsername(activityDistribute.getUsername());
//                    financialParam.setBeforeBalance(BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP));
//                    financialParam.setAmount(activityDistribute.getDiscountsMoney().setScale(2, RoundingMode.HALF_UP));
//                    financialParam.setAfterBalance(status == 0 ? BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP) : BigDecimal.valueOf(amount).add(activityDistribute.getDiscountsMoney()).setScale(2, RoundingMode.HALF_UP));
//                    financialParam.setSourceType(50);
//                    financialParam.setSourceId(sourceId);
//                    financialParam.setState(status);
//                    financialParam.setMode(3);
//                    financialParam.setRemark(remark);
//                    financialParam.setCreateTime(new Date());
//                    financialParam.setUserId(activityDistribute.getUserId());
//                    financialParam.setCurrencyName("真币");
//                    financialParam.setCurrencyType(1);
//                    financialParam.setIpAddress(IPUtils.getHostIp());
//                    financialParam.setIsLess(0);
//                    financialParam.setCreateBy(SysUserUtil.getUserName());
//                    financialParam.setTenantName(DyDataSourceContextHolder.getDBSuffix());
//                    // 异步插入现金流水
//                    Financial1 financial1 = new Financial1();
//                    BeanUtils.copyBeanProp(financial1, financialParam);
//                    financialFeignService.asyncInsertFinancial(financial1);
//
//                    //插入福利中心记录(已领取)
//                    wealReword.setStatus(2);
//                    userActivityFeignService.insertWealReword(wealReword);
//
//                    //通知 发个人消息
//                    SysInformation sysInformationDto = new SysInformation();
//                    sysInformationDto.setContent(getInformationContent(activityDistribute, getWay));
//                    sysInformationDto.setTitle("活动派发");
//                    sysInformationDto.setType((byte) 1);
//                    sysInformationDto.setLetterType((byte) 11);
//                    sysInformationDto.setUserName(activityDistribute.getUsername());
//                    sysInformationDto.setUserId(activityDistribute.getUserId());
//                    sysInformationDto.setTenantCode(DyDataSourceContextHolder.getDBSuffix());
//                    userActivityFeignService.addSysInformation(sysInformationDto);
//
//                    // 派发成功，释放资金锁
//                    distributedLocker.unlock(lockKey);
//                } catch (Exception e) {
//                    log.error(MessageFormat.format("租户：{0}，会员{1}，活动派发失败, 失败原因：{2}",
//                            DyDataSourceContextHolder.getDBSuffix(), activityDistribute.getUsername(), e));
//                    // 释放资金锁
//                    distributedLocker.unlock(lockKey);
//                    continue;
//                } finally {
//
//                }
//            } else if (getWay == 2) {
////                如果领取方式是2, 则将活动奖励记录插入福利中心, 会员需自己点击领取
//                wealReword.setStatus(1);
//                userActivityFeignService.insertWealReword(wealReword);
//
//                //账变成功,修改派发状态
//                ActivityDistribute distribute = new ActivityDistribute();
//                distribute.setDistributeId(activityDistribute.getDistributeId());
//                distribute.setStatus(2);
//                distribute.setUpdateBy(SysUserUtil.getUserName());
//                distribute.setSettlementTime(new Date());
//                activityDistributeDao.updateByPrimaryKeySelective(distribute);
//                //修改资格使用时间和次数
//                QualificationManage qualificationManage = new QualificationManage();
//                qualificationManage.setQualificationActivityId(activityDistribute.getQualificationActivityId());
//                qualificationManage.setEmployNum(1);
//                qualificationManage.setEmployTime(new Date());
//                qualificationManageDao.updateQualificationManage(qualificationManage);
//
//                //通知 发个人消息
//                SysInformation sysInformationDto = new SysInformation();
//                sysInformationDto.setContent(getInformationContent(activityDistribute, getWay));
//                sysInformationDto.setTitle("活动派发");
//                sysInformationDto.setType((byte) 1);
//                sysInformationDto.setLetterType((byte) 11);
//                sysInformationDto.setUserName(activityDistribute.getUsername());
//                sysInformationDto.setUserId(activityDistribute.getUserId());
//                sysInformationDto.setTenantCode(DyDataSourceContextHolder.getDBSuffix());
//                userActivityFeignService.addSysInformation(sysInformationDto);
//            } else {
//                throw new ServiceException("无效的领取方式参数");
//            }
        }
        log.info("派发活动奖励结束", System.currentTimeMillis());


    }
}
