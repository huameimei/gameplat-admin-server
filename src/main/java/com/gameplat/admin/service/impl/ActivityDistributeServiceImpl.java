package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.ActivityDistributeConvert;
import com.gameplat.admin.enums.ActivityDistributeEnum;
import com.gameplat.admin.mapper.ActivityDistributeMapper;
import com.gameplat.admin.model.bean.PageExt;
import com.gameplat.admin.model.dto.ActivityDistributeQueryDTO;
import com.gameplat.admin.model.vo.ActivityDistributeStatisticsVO;
import com.gameplat.admin.model.vo.ActivityDistributeVO;
import com.gameplat.admin.service.ActivityDistributeService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.BooleanEnum;
import com.gameplat.model.entity.activity.ActivityDistribute;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.member.MemberWealReword;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 活动分发类
 *
 * @author kenvin
 */
@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class ActivityDistributeServiceImpl
    extends ServiceImpl<ActivityDistributeMapper, ActivityDistribute>
    implements ActivityDistributeService {

  @Autowired private ActivityDistributeConvert activityDistributeConvert;

  @Autowired private ActivityDistributeWayService activityDistributeWayService;

  @Autowired private MemberService memberService;

  @Override
  public List<ActivityDistribute> findActivityDistributeList(
      ActivityDistribute activityDistribute) {
    return this.lambdaQuery()
        .eq(
            activityDistribute.getActivityId() != null && activityDistribute.getActivityId() != 0,
            ActivityDistribute::getActivityId,
            activityDistribute.getActivityId())
        .eq(
            activityDistribute.getDeleteFlag() != null,
            ActivityDistribute::getDeleteFlag,
            activityDistribute.getDeleteFlag())
        .eq(
            activityDistribute.getStatus() != null,
            ActivityDistribute::getStatus,
            activityDistribute.getStatus())
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
  public boolean saveDistributeBatch(List<ActivityDistribute> activityDistributeList) {
    return this.saveBatch(activityDistributeList);
  }

  @Override
  public PageExt<IPage<ActivityDistributeVO>, ActivityDistributeStatisticsVO> list(
      PageDTO<ActivityDistribute> page, ActivityDistributeQueryDTO dto) {
    LambdaQueryChainWrapper<ActivityDistribute> lambdaQuery = this.lambdaQuery();
    lambdaQuery
        // 未删除
        .eq(ActivityDistribute::getDeleteFlag, BooleanEnum.YES.value())
        .like(
            StringUtils.isNotBlank(dto.getUsername()),
            ActivityDistribute::getUsername,
            dto.getUsername())
        .eq(
            dto.getActivityId() != null && dto.getActivityId() != 0,
            ActivityDistribute::getActivityId,
            dto.getActivityId())
        .eq(dto.getStatus() != null, ActivityDistribute::getStatus, dto.getStatus())
        .eq(
            dto.getGetWay() != null && dto.getGetWay() != 0,
            ActivityDistribute::getGetWay,
            dto.getGetWay())
        .ge(
            StringUtils.isNotBlank(dto.getApplyStartTime()),
            ActivityDistribute::getApplyTime,
            dto.getApplyStartTime())
        .le(
            StringUtils.isNotBlank(dto.getApplyEndTime()),
            ActivityDistribute::getApplyTime,
            dto.getApplyEndTime());

    IPage<ActivityDistributeVO> iPage =
        lambdaQuery.page(page).convert(activityDistributeConvert::toVo);
    BigDecimal subtotalMoney = BigDecimal.ZERO;
    if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
      for (ActivityDistributeVO vo : iPage.getRecords()) {
        if (ActivityDistributeEnum.ActivityDistributeStatus.SETTLED.getValue() == vo.getStatus()) {
          subtotalMoney = subtotalMoney.add(vo.getDiscountsMoney());
        }
      }
    }

    ActivityDistributeStatisticsVO activityDistributeStatisticsVO =
        new ActivityDistributeStatisticsVO();
    QueryWrapper<ActivityDistribute> queryWrapper = new QueryWrapper<>();
    queryWrapper
        .select("SUM(discounts_money) aggregate")
        .eq("status", ActivityDistributeEnum.ActivityDistributeStatus.SETTLED.getValue());
    Map<String, Object> map = this.getMap(queryWrapper);
    if (MapUtils.isNotEmpty(map) && map.get("aggregate") != null) {
      BigDecimal aggregate = new BigDecimal(map.get("aggregate").toString());
      activityDistributeStatisticsVO.setAllMoney(
          aggregate.setScale(2, BigDecimal.ROUND_UP).doubleValue());
    }
    activityDistributeStatisticsVO.setSubtotalMoney(
        subtotalMoney.setScale(2, BigDecimal.ROUND_UP).doubleValue());
    return new PageExt(iPage, activityDistributeStatisticsVO);
  }

  @Override
  public void updateStatus(String ids) {
    if (StringUtils.isBlank(ids)) {
      throw new ServiceException("ids不能为空");
    }
    List<ActivityDistribute> activityDistributeList =
        this.lambdaQuery().in(ActivityDistribute::getDistributeId, ids.split(",")).list();
    if (CollectionUtils.isNotEmpty(activityDistributeList)) {
      for (ActivityDistribute activityDistribute : activityDistributeList) {
        if (activityDistribute.getStatus()
            == ActivityDistributeEnum.ActivityDistributeStatus.INVALID.getValue()) {
          throw new ServiceException("无效状态不能结算！");
        }
        if (activityDistribute.getStatus()
            == ActivityDistributeEnum.ActivityDistributeStatus.SETTLED.getValue()) {
          throw new ServiceException("已结算的不能重复结算！");
        }
      }
    } else {
      throw new ServiceException("派发活动信息不存在，请重试！");
    }

    log.info("开始派发活动奖励,{}", System.currentTimeMillis());
    // 修改用户真币、资金流水
    for (ActivityDistribute activityDistribute : activityDistributeList) {
      Member member = memberService.getById(activityDistribute.getUserId());
      Integer getWay = activityDistribute.getGetWay();
      // 福利中心记录
      MemberWealReword wealReword = new MemberWealReword();
      wealReword.setUserType(member.getUserType());
      wealReword.setParentId(member.getParentId().longValue());
      wealReword.setParentName(member.getParentName());
      wealReword.setAgentPath(member.getSuperPath());
      wealReword.setUserId(activityDistribute.getUserId());
      wealReword.setUserName(activityDistribute.getUsername());
      wealReword.setRewordAmount(activityDistribute.getDiscountsMoney());
      wealReword.setWithdrawDml(new BigDecimal(activityDistribute.getWithdrawDml()));
      wealReword.setType(5); // 5 活动大厅奖励
      wealReword.setSerialNumber(activityDistribute.getDistributeId().toString());
      wealReword.setActivityTitle(activityDistribute.getActivityName());
      try {
        // 如果领取方式是1,则直接派发金额到会员账户
        if (getWay
            == ActivityDistributeEnum.ActivityDistributeGetWayEnum.DIRECT_RELEASE.getValue()) {
          activityDistributeWayService.directRelease(activityDistribute, wealReword);
          // 如果领取方式是2, 则将活动奖励记录插入福利中心, 会员需自己点击领取
        } else if (getWay
            == ActivityDistributeEnum.ActivityDistributeGetWayEnum.WELFARE_CENTER.getValue()) {
          activityDistributeWayService.welfareCenter(activityDistribute, wealReword);
        } else {
          throw new ServiceException("无效的领取方式参数");
        }
      } catch (Exception e) {
        log.error("活动派发异常,派发ID:{},异常原因:{}", activityDistribute.getActivityId(), e);
      }
    }
    log.info("派发活动奖励结束,{}", System.currentTimeMillis());
  }

  @Override
  public void remove(String ids) {
    String[] idArr = ids.split(",");
    List<Long> adList = new ArrayList<>();
    for (String idStr : idArr) {
      adList.add(Long.parseLong(idStr));
    }
    this.removeByIds(adList);
  }

  @Override
  public boolean updateDeleteStatus(String ids) {
    String[] idArr = ids.split(",");
    List<ActivityDistribute> activityDistributeList = new ArrayList<>();
    for (String idStr : idArr) {
      ActivityDistribute activityDistribute = new ActivityDistribute();
      activityDistribute.setDistributeId(Long.parseLong(idStr));
      activityDistribute.setDeleteFlag(BooleanEnum.NO.value());
      activityDistributeList.add(activityDistribute);
    }
    return this.updateBatchById(activityDistributeList);
  }
}
