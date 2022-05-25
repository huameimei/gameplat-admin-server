package com.gameplat.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.ActivityLobbyConvert;
import com.gameplat.admin.convert.ActivityLobbyDiscountConvert;
import com.gameplat.admin.mapper.ActivityLobbyMapper;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.ActivityLobbyDiscountVO;
import com.gameplat.admin.model.vo.ActivityLobbyVO;
import com.gameplat.admin.service.ActivityDistributeService;
import com.gameplat.admin.service.ActivityLobbyDiscountService;
import com.gameplat.admin.service.ActivityLobbyService;
import com.gameplat.admin.service.ActivityQualificationService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.BeanUtils;
import com.gameplat.base.common.util.DateUtil;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.ActivityInfoEnum;
import com.gameplat.common.enums.BooleanEnum;
import com.gameplat.common.util.DateUtils;
import com.gameplat.model.entity.activity.ActivityDistribute;
import com.gameplat.model.entity.activity.ActivityLobby;
import com.gameplat.model.entity.activity.ActivityLobbyDiscount;
import com.gameplat.model.entity.activity.ActivityQualification;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/** 活动大厅业务 * */
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class ActivityLobbyServiceImpl extends ServiceImpl<ActivityLobbyMapper, ActivityLobby>
    implements ActivityLobbyService {

  @Autowired private ActivityLobbyConvert activityLobbyConvert;

  @Autowired private ActivityLobbyDiscountConvert activityLobbyDiscountConvert;

  @Autowired private ActivityLobbyDiscountService activityLobbyDiscountService;

  @Lazy @Autowired private ActivityQualificationService activityQualificationService;

  @Autowired private ActivityDistributeService activityDistributeService;

  @Override
  public IPage<ActivityLobbyVO> findActivityLobbyList(
      PageDTO<ActivityLobby> page, ActivityLobbyQueryDTO dto) {
    LambdaQueryChainWrapper<ActivityLobby> queryWrapper = this.lambdaQuery();
    queryWrapper
        .like(StringUtils.isNotBlank(dto.getTitle()), ActivityLobby::getTitle, dto.getTitle())
        .eq(dto.getStatus() != null, ActivityLobby::getStatus, dto.getStatus())
        .eq(dto.getType() != null, ActivityLobby::getType, dto.getType())
        .orderByDesc(Lists.newArrayList(ActivityLobby::getCreateTime, ActivityLobby::getId));

    IPage<ActivityLobbyVO> activityLobbyVOIPage =
        queryWrapper.page(page).convert(activityLobbyConvert::toVo);
    if (CollectionUtils.isNotEmpty(activityLobbyVOIPage.getRecords())) {
      for (ActivityLobbyVO activityLobbyVO : activityLobbyVOIPage.getRecords()) {
        List<ActivityLobbyDiscountVO> activityLobbyDiscounts =
            activityLobbyDiscountService.listByActivityLobbyId(activityLobbyVO.getId());
        activityLobbyVO.setLobbyDiscountList(activityLobbyDiscounts);
      }
    }
    return activityLobbyVOIPage;
  }

  @Override
  @Transactional(rollbackFor = Throwable.class)
  public void add(ActivityLobbyAddDTO dto) {
    ActivityLobby activityLobby = activityLobbyConvert.toEntity(dto);

    // 指定比赛
    if (dto.getStatisItem()
        == ActivityInfoEnum.StatisItem.CUMULATIVE_SPORTS_RECHARGE_AMOUNT.getValue()) {
      if (StringUtils.isNull(dto.getMatchId())) {
        throw new ServiceException("指定比赛不能为空");
      }
      String matchTimeStr = DateUtil.dateToStr(dto.getMatchTime(), DateUtil.YYYYMMDD);
      String startTimeStr = DateUtil.dateToStr(dto.getStartTime(), DateUtil.YYYYMMDD);
      String endTimeStr = DateUtil.dateToStr(dto.getEndTime(), DateUtil.YYYYMMDD);
      if (!matchTimeStr.equals(startTimeStr) || !matchTimeStr.equals(endTimeStr)) {
        throw new ServiceException("活动开始和结束时间必须和指定比赛的比赛时间是同一天");
      }
      if (dto.getNextDayApply() == null
          || dto.getNextDayApply() != ActivityInfoEnum.NextDayApply.YES.value()) {
        throw new ServiceException("指定比赛必须选择隔天申请");
      }
    }

    List<String> applyDateList = new ArrayList<>();
    // 计算活动的参与日期集合
    Integer detailDate = dto.getDetailDate();
    String startTime;
    String endTime;
    // 隔天申请（0 否，1 是）
    if (dto.getNextDayApply() == ActivityInfoEnum.NextDayApply.YES.value()) {
      detailDate = detailDate + 1;
      if (detailDate == 7) {
        detailDate = 0;
      }
      startTime =
          DateUtil.dateToStr(
              cn.hutool.core.date.DateUtil.date(dto.getStartTime())
                  .offset(DateField.DAY_OF_YEAR, 1),
              "yyyy-MM-dd");
      endTime =
          DateUtil.dateToStr(
              cn.hutool.core.date.DateUtil.date(dto.getEndTime()).offset(DateField.DAY_OF_YEAR, 1),
              "yyyy-MM-dd");
    } else {
      startTime = DateUtil.dateToStr(dto.getStartTime(), DateUtil.YYYY_MM_DD);
      endTime = DateUtil.dateToStr(dto.getEndTime(), DateUtil.YYYY_MM_DD);
    }

    if (dto.getStatisDate()
        == ActivityInfoEnum.StatisItem.CUMULATIVE_RECHARGE_AMOUNT.getValue()) { // 1 每日
      applyDateList.add("每天");
    }
    // 如果统计周期是每周，需要判断活动的结束时间是不是周日
    else if (dto.getStatisDate()
        == ActivityInfoEnum.StatisItem.CUMULATIVE_RECHARGE_DAYS.getValue()) { // 2 每周
      if (DateUtils.getWeekNumOfDate(dto.getEndTime()) != 7) {
        throw new ServiceException("统计日期选择每周，活动的结束日期应该为某周的星期天");
      }
      applyDateList = DateUtils.getDayOfWeekWithinDateInterval(startTime, endTime, detailDate);
    }
    // 如果统计周期是每月，需要判断活动的结束时间是不是某月的最后一天
    else if (dto.getStatisDate()
        == ActivityInfoEnum.StatisItem.CONTINUOUS_RECHARGE_DAYS.getValue()) { // 3 每月
      if (!DateUtils.isSameDate(
          cn.hutool.core.date.DateUtil.endOfMonth(dto.getEndTime()), dto.getEndTime())) {
        throw new ServiceException("统计日期选择每月，活动的结束日期应该为某月的最后一天");
      }
      List<Date> endDates = DateUtils.findEndDates(dto.getStartTime(), dto.getEndTime());
      if (dto.getNextDayApply() == ActivityInfoEnum.NextDayApply.YES.value()) {
        for (Date date : endDates) {
          applyDateList.add(
              DateUtil.dateToStr(
                  cn.hutool.core.date.DateUtil.date(date).offset(DateField.DAY_OF_YEAR, 1),
                  "yyyy-MM-dd"));
        }
      } else {
        for (Date date : endDates) {
          applyDateList.add(DateUtil.dateToStr(date, "yyyy-MM-dd"));
        }
      }
    }
    // 如果统计周期是每周X，需要判断活动的结束时间是不是每周X
    else if (dto.getStatisDate()
        == ActivityInfoEnum.StatisItem.SINGLE_DAY_DEPOSIT_AMOUNT.getValue()) { // 4 每周X
      if (DateTime.of(dto.getEndTime()).dayOfWeek() != dto.getDetailDate()) {
        throw new ServiceException(
            "统计日期选择"
                + ActivityInfoEnum.DetailDateEnum.getWeek(dto.getDetailDate())
                + ",活动的结束日期应该为"
                + ActivityInfoEnum.DetailDateEnum.getWeek(dto.getDetailDate()));
      }
      applyDateList = DateUtils.getDayOfWeekWithinDateInterval(startTime, endTime, detailDate);
    }
    // 如果统计周期是每月X，需要判断活动的结束时间是不是每月X
    else if (dto.getStatisDate()
        == ActivityInfoEnum.StatisItem.FIRST_DEPOSIT_AMOUNT.getValue()) { // 5 每月X日
      if (Integer.parseInt(String.format("%td", dto.getEndTime())) != dto.getDetailDate()) {
        throw new ServiceException(
            "统计日期选择每月" + dto.getDetailDate() + "号" + ",活动的结束日期那天应该为" + dto.getDetailDate() + "号");
      }
      List<Date> dates = DateUtil.findDates(dto.getStartTime(), dto.getEndTime());
      for (Date date : dates) {
        String day = String.format("%td", date);
        if (Integer.parseInt(day) == dto.getDetailDate()) {
          applyDateList.add(DateUtil.dateToStr(date, DateUtil.YYYY_MM_DD));
        }
      }
      if (dto.getNextDayApply() == ActivityInfoEnum.NextDayApply.YES.value()) {
        List<String> copyDateList = new ArrayList<>();
        copyDateList.addAll(applyDateList);
        applyDateList.clear();
        for (String applyDate : copyDateList) {
          applyDate =
              DateUtil.dateToStr(
                  cn.hutool.core.date.DateUtil.date(
                          DateUtil.strToDate(applyDate, DateUtil.YYYY_MM_DD))
                      .offset(DateField.DAY_OF_YEAR, 1),
                  DateUtil.YYYY_MM_DD);
          applyDateList.add(applyDate);
        }
      }
    }

    if (CollectionUtil.isEmpty(applyDateList)) {
      throw new ServiceException("统计日期不在活动有效期内！");
    }

    // 优惠打折列表
    List<ActivityLobbyDiscount> activityLobbyDiscounts =
        BeanUtils.mapList(dto.getLobbyDiscountList(), ActivityLobbyDiscount.class);

    List<Integer> targetValueList =
        activityLobbyDiscounts.stream()
            .map(ActivityLobbyDiscount::getTargetValue)
            .collect(Collectors.toList());
    long countTargetValue = targetValueList.stream().distinct().count();
    if (targetValueList.size() != countTargetValue) {
      throw new ServiceException("奖励赠送列表中,设定的目标值不能重复");
    }

    List<Integer> presenterValueList =
        activityLobbyDiscounts.stream()
            .map(ActivityLobbyDiscount::getPresenterValue)
            .collect(Collectors.toList());
    long countPresenterValue = presenterValueList.stream().distinct().count();
    if (targetValueList.size() != countPresenterValue) {
      throw new ServiceException("奖励赠送列表中,设定的赠送金额不能重复");
    }

    if (dto.getStatisItem() == ActivityInfoEnum.StatisItem.CUMULATIVE_RECHARGE_DAYS.getValue()
        || dto.getStatisItem() == ActivityInfoEnum.StatisItem.CONTINUOUS_RECHARGE_DAYS.getValue()
        || dto.getStatisItem() == ActivityInfoEnum.StatisItem.CUMULATIVE_GAME_DML_DAYS.getValue()
        || dto.getStatisItem()
            == ActivityInfoEnum.StatisItem.CONSECUTIVE_GAME_DML_DAYS.getValue()) {
      for (ActivityLobbyDiscount activityLobbyDiscount : activityLobbyDiscounts) {
        if (activityLobbyDiscount.getTargetValue() < 2) {
          throw new ServiceException("奖励赠送列表中,设定的目标天数最小值为2");
        }
      }
    }

    activityLobby.setApplyDateList(String.join(",", applyDateList));
    if (!this.save(activityLobby)) {
      throw new ServiceException("保存活动大厅异常！");
    }

    for (ActivityLobbyDiscount activityLobbyDiscount : activityLobbyDiscounts) {
      activityLobbyDiscount.setLobbyId(activityLobby.getId());
      activityLobbyDiscount.setCreateTime(new Date());
    }
    boolean isSaveLobbyDiscount =
        activityLobbyDiscountService.saveBatchLobbyDiscount(activityLobbyDiscounts);
    if (!isSaveLobbyDiscount) {
      throw new ServiceException("添加优惠失败！");
    }
  }

  @Override
  @Transactional(rollbackFor = Throwable.class)
  public void update(ActivityLobbyUpdateDTO dto) {
    // 根据id查询活动大厅
    ActivityLobby activityLobbyOrigin = this.getById(dto.getId());
    if (activityLobbyOrigin == null) {
      throw new ServiceException("该活动大厅不存在！");
    }
    // 判断活动是否已上线
    if (activityLobbyOrigin.getStatus() == BooleanEnum.YES.value()
        || activityLobbyOrigin.getStatus() == BooleanEnum.NO.value()) {
      boolean b = DateUtils.compareCurrentDate(activityLobbyOrigin.getStartTime());
      boolean endTime = DateUtils.compareCurrentDate(activityLobbyOrigin.getEndTime());
      if (!b && endTime) {
        throw new ServiceException("活动进行中，不可修改！");
      }
    }

    if (DateUtils.compareCurrentDateLess(activityLobbyOrigin.getEndTime())) {
      throw new ServiceException("该活动已过期，如要创建一个新的活动，请点击新增添加");
    }

    // 指定比赛
    if (dto.getStatisItem()
        == ActivityInfoEnum.StatisItem.CUMULATIVE_SPORTS_RECHARGE_AMOUNT.getValue()) {
      if (StringUtils.isNull(dto.getMatchId())) {
        throw new ServiceException("指定比赛不能为空");
      }
      if (!DateUtils.isSameDate(dto.getMatchTime(), dto.getStartTime())
          && !DateUtils.isSameDate(dto.getMatchTime(), dto.getEndTime())) {
        throw new ServiceException("活动开始和结束时间必须和指定比赛的比赛时间是同一天");
      }
      if (dto.getNextDayApply() == null
          || dto.getNextDayApply() != ActivityInfoEnum.NextDayApply.YES.value()) {
        throw new ServiceException("指定比赛必须选择隔天申请");
      }
    }

    List<String> applyDateList = new ArrayList<>();
    // 计算活动的参与日期集合
    Integer detailDate = dto.getDetailDate();
    String startTime;
    String endTime;
    if (dto.getNextDayApply() == ActivityInfoEnum.NextDayApply.YES.value()) {
      detailDate = detailDate + 1;
      if (detailDate == 7) {
        detailDate = 0;
      }
      startTime =
          DateUtil.dateToStr(
              cn.hutool.core.date.DateUtil.date(dto.getStartTime())
                  .offset(DateField.DAY_OF_YEAR, 1),
              DateUtil.YYYY_MM_DD);
      endTime =
          DateUtil.dateToStr(
              cn.hutool.core.date.DateUtil.date(dto.getEndTime()).offset(DateField.DAY_OF_YEAR, 1),
              DateUtil.YYYY_MM_DD);
    } else {
      startTime = DateUtil.dateToStr(dto.getStartTime(), DateUtil.YYYY_MM_DD);
      endTime = DateUtil.dateToStr(dto.getEndTime(), DateUtil.YYYY_MM_DD);
    }

    if (dto.getStatisDate() == ActivityInfoEnum.StatisDate.DAILY.getValue()) {
      applyDateList.add("每天");
    }
    // 如果统计周期是每周，需要判断活动的结束时间是不是周日
    else if (dto.getStatisDate() == ActivityInfoEnum.StatisDate.WEEKLY.getValue()) {
      if (DateUtils.getWeekNumOfDate(dto.getEndTime()) != 7) {
        throw new ServiceException("统计日期选择每周，活动的结束日期应该为某周的星期天");
      }

      applyDateList = DateUtils.getDayOfWeekWithinDateInterval(startTime, endTime, detailDate);
    }
    // 如果统计周期是每月，需要判断活动的结束时间是不是某月的最后一天
    else if (dto.getStatisDate() == ActivityInfoEnum.StatisDate.PER_MONTH.getValue()) {
      if (!DateUtils.isSameDate(
          cn.hutool.core.date.DateUtil.endOfMonth(dto.getEndTime()), dto.getEndTime())) {
        throw new ServiceException("统计日期选择每月，活动的结束日期应该为某月的最后一天");
      }
      List<Date> endDates = DateUtils.findEndDates(dto.getStartTime(), dto.getEndTime());
      if (dto.getNextDayApply() == ActivityInfoEnum.NextDayApply.YES.value()) {
        for (Date date : endDates) {
          applyDateList.add(
              DateUtil.dateToStr(
                  cn.hutool.core.date.DateUtil.date(date).offset(DateField.DAY_OF_YEAR, 1),
                  DateUtil.YYYY_MM_DD));
        }
      } else {
        for (Date date : endDates) {
          applyDateList.add(DateUtil.dateToStr(date, DateUtil.YYYY_MM_DD));
        }
      }
    }
    // 如果统计周期是每周X，需要判断活动的结束时间是不是每周X
    else if (dto.getStatisDate() == ActivityInfoEnum.StatisDate.SPECIFY_DAY_WEEK.getValue()) {
      if (DateTime.of(dto.getEndTime()).dayOfWeek() == dto.getDetailDate()) {
        throw new ServiceException(
            "统计日期选择"
                + ActivityInfoEnum.DetailDateEnum.getWeek(dto.getDetailDate())
                + ",活动的结束日期应该为"
                + ActivityInfoEnum.DetailDateEnum.getWeek(dto.getDetailDate()));
      }
      applyDateList = DateUtils.getDayOfWeekWithinDateInterval(startTime, endTime, detailDate);
    }
    // 如果统计周期是每月X，需要判断活动的结束时间是不是每月X
    else if (dto.getStatisDate() == ActivityInfoEnum.StatisDate.DAY_OF_THE_MONTH.getValue()) {
      if (Integer.parseInt(String.format("%td", dto.getEndTime())) != dto.getDetailDate()) {
        throw new ServiceException(
            "统计日期选择每月" + dto.getDetailDate() + "号" + ",活动的结束日期那天应该为" + dto.getDetailDate() + "号");
      }
      List<Date> dates = DateUtil.findDates(dto.getStartTime(), dto.getEndTime());
      for (Date date : dates) {
        String day = String.format("%td", date);
        if (Integer.parseInt(day) == dto.getDetailDate()) {
          applyDateList.add(DateUtil.dateToStr(date, DateUtil.YYYY_MM_DD));
        }
      }
      if (dto.getNextDayApply() == ActivityInfoEnum.NextDayApply.YES.value()) {
        List<String> copyDateList = new ArrayList<>(applyDateList);
        applyDateList.clear();
        for (String applyDate : copyDateList) {
          applyDate =
              DateUtil.dateToStr(
                  cn.hutool.core.date.DateUtil.date(
                          DateUtil.strToDate(applyDate, DateUtil.YYYY_MM_DD))
                      .offset(DateField.DAY_OF_YEAR, 1),
                  DateUtil.YYYY_MM_DD);
          applyDateList.add(applyDate);
        }
      }
    }

    if (CollectionUtil.isEmpty(applyDateList)) {
      throw new ServiceException("统计日期不在活动有效期内！");
    }

    // 含有id的数据
    List<ActivityLobbyDiscount> hasIdList = new ArrayList<>();
    // 没有id的列表
    List<ActivityLobbyDiscount> noIdList = new ArrayList<>();
    // 所有的列表数据
    List<ActivityLobbyDiscount> allList = new ArrayList<>();
    Set<Long> hasIdSet = new HashSet<>();

    List<ActivityLobbyDiscountDTO> lobbyDiscountList = dto.getLobbyDiscountList();
    // 遍历大厅优惠信息，完善优惠信息，有id就修改，没有id就新增
    for (ActivityLobbyDiscountDTO lobbyDiscountDTO : lobbyDiscountList) {
      ActivityLobbyDiscount activityLobbyDiscount =
          activityLobbyDiscountConvert.toEntity(lobbyDiscountDTO);
      if (lobbyDiscountDTO.getLobbyDiscountId() != null
          && lobbyDiscountDTO.getLobbyDiscountId() != 0) {
        hasIdList.add(activityLobbyDiscount);
        hasIdSet.add(lobbyDiscountDTO.getLobbyDiscountId());
      } else {
        activityLobbyDiscount.setLobbyId(activityLobbyOrigin.getId());
        noIdList.add(activityLobbyDiscount);
      }
    }

    // 需要删除的列表数据
    List<ActivityLobbyDiscount> deleteList = new ArrayList<>();
    // 查询历史折扣列表
    List<ActivityLobbyDiscountVO> activityLobbyDiscounts =
        activityLobbyDiscountService.listByActivityLobbyId(dto.getId());
    if (CollectionUtils.isNotEmpty(activityLobbyDiscounts)) {
      for (ActivityLobbyDiscountVO activityLobbyDiscountVO : activityLobbyDiscounts) {
        if (!hasIdSet.contains(activityLobbyDiscountVO.getLobbyDiscountId())) {
          deleteList.add(activityLobbyDiscountConvert.toEntity(activityLobbyDiscountVO));
        }
      }
    }

    if (CollectionUtil.isNotEmpty(hasIdList)) {
      allList.addAll(hasIdList);
    }
    if (CollectionUtil.isNotEmpty(noIdList)) {
      allList.addAll(noIdList);
    }

    if (CollectionUtil.isNotEmpty(allList)) {
      List<Integer> targetValueList =
          allList.stream().map(ActivityLobbyDiscount::getTargetValue).collect(Collectors.toList());
      long countTargetValue = targetValueList.stream().distinct().count();
      if (targetValueList.size() != countTargetValue) {
        throw new ServiceException("奖励赠送列表目标数值不能重复");
      }

      List<Integer> presenterValueList =
          lobbyDiscountList.stream()
              .map(ActivityLobbyDiscountDTO::getPresenterValue)
              .collect(Collectors.toList());
      long countPresenterValue = presenterValueList.stream().distinct().count();
      if (targetValueList.size() != countPresenterValue) {
        throw new ServiceException("奖励赠送列表赠送金额数值不能重复");
      }
    }

    if (dto.getStatisItem() == ActivityInfoEnum.StatisItem.CUMULATIVE_RECHARGE_DAYS.getValue()
        || dto.getStatisItem() == ActivityInfoEnum.StatisItem.CONTINUOUS_RECHARGE_DAYS.getValue()
        || dto.getStatisItem() == ActivityInfoEnum.StatisItem.CUMULATIVE_GAME_DML_DAYS.getValue()
        || dto.getStatisItem()
            == ActivityInfoEnum.StatisItem.CONSECUTIVE_GAME_DML_DAYS.getValue()) {
      for (ActivityLobbyDiscountDTO activityLobbyDiscountDTO : lobbyDiscountList) {
        if (activityLobbyDiscountDTO.getTargetValue() < 2) {
          throw new ServiceException("奖励赠送列表中,设定的目标天数最小值为2");
        }
      }
    }

    dto.setApplyDateList(String.join(",", applyDateList));
    ActivityLobby activityLobby = activityLobbyConvert.toEntity(dto);
    if (!this.updateById(activityLobby)) {
      throw new ServiceException("修改失败！");
    }

    // 有id的进行批量更新
    if (hasIdList.size() > 0) {
      activityLobbyDiscountService.updateBatchLobbyDiscount(hasIdList);
    }
    // 无id的批量添加
    if (noIdList.size() > 0) {
      activityLobbyDiscountService.saveBatchLobbyDiscount(noIdList);
    }
    // 如有要删除的就删除
    if (deleteList.size() > 0) {
      activityLobbyDiscountService.deleteBatchLobbyDiscount(deleteList);
    }
  }

  @Override
  public void remove(String ids) {
    String[] idArr = ids.split(",");
    // 校验是否有活动不结束
    for (String idStr : idArr) {
      Long id = Long.parseLong(idStr);
      ActivityQualification activityQualification = new ActivityQualification();
      activityQualification.setActivityId(id);
      activityQualification.setDeleteFlag(BooleanEnum.YES.value());
      activityQualification.setStatus(BooleanEnum.YES.value());
      List<ActivityQualification> qualificationList =
          activityQualificationService.findQualificationList(activityQualification);
      if (CollectionUtil.isNotEmpty(qualificationList)) {
        throw new ServiceException("你要删除的活动中有未审核的会员资格记录");
      }

      ActivityDistribute activityDistribute = new ActivityDistribute();
      activityDistribute.setActivityId(id);
      activityDistribute.setDeleteFlag(BooleanEnum.YES.value());
      activityDistribute.setStatus(BooleanEnum.YES.value());
      List<ActivityDistribute> distributeList =
          activityDistributeService.findActivityDistributeList(activityDistribute);
      if (CollectionUtil.isNotEmpty(distributeList)) {
        throw new ServiceException("你要删除的活动中有未派发的会员派发记录");
      }
    }
    this.deleteActivityLobby(ids);
    activityDistributeService.deleteByLobbyIds(ids);
  }

  @Override
  public void deleteActivityLobby(String ids) {
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
  public void updateStatus(ActivityLobbyUpdateStatusDTO dto) {
    ActivityLobby activityLobby = this.getById(dto.getId());
    if (activityLobby == null) {
      throw new ServiceException("活动大厅不存在，请重试！");
    }
    ActivityLobby activityLobby2 = activityLobbyConvert.toEntity(dto);
    this.updateById(activityLobby2);
  }

  @Override
  public List<ActivityLobbyVO> findAllLobbyList() {
    List<ActivityLobby> activityLobbies =
        this.lambdaQuery()
            .eq(ActivityLobby::getStatus, BooleanEnum.YES.value())
            .le(ActivityLobby::getStartTime, DateUtil.getNowTime())
            .ge(ActivityLobby::getEndTime, DateUtil.getNowTime())
            .list();
    if (CollectionUtils.isEmpty(activityLobbies)) {
      return new ArrayList<>();
    }
    return activityLobbies.stream().map(activityLobbyConvert::toVo).collect(Collectors.toList());
  }

  @Override
  public ActivityLobbyVO getActivityLobbyVOById(Long activityLobbyId) {
    ActivityLobbyVO result = baseMapper.getActivityLobbyVOById(activityLobbyId);
    if (CollectionUtils.isNotEmpty(result.getLobbyDiscountList())) {
      List<ActivityLobbyDiscountVO> activityLobbyDiscountVOList = new ArrayList<>();
      for (int i = 0; i < result.getLobbyDiscountList().size(); i++) {
        Object o = result.getLobbyDiscountList().get(i);
        if (o instanceof ActivityLobbyDiscount) {
          activityLobbyDiscountVOList.add(
              activityLobbyDiscountConvert.toVO(((ActivityLobbyDiscount) o)));
        }
      }
      result.setLobbyDiscountList(activityLobbyDiscountVOList);
    }
    return result;
  }
}
