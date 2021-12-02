package com.gameplat.admin.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.ActivityLobbyConvert;
import com.gameplat.admin.convert.ActivityLobbyDiscountConvert;
import com.gameplat.admin.enums.DetailDateEnum;
import com.gameplat.admin.mapper.ActivityLobbyMapper;
import com.gameplat.admin.model.domain.ActivityDistribute;
import com.gameplat.admin.model.domain.ActivityLobby;
import com.gameplat.admin.model.domain.ActivityLobbyDiscount;
import com.gameplat.admin.model.domain.ActivityQualification;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.ActivityLobbyDiscountVO;
import com.gameplat.admin.model.vo.ActivityLobbyVO;
import com.gameplat.admin.service.ActivityDistributeService;
import com.gameplat.admin.service.ActivityLobbyDiscountService;
import com.gameplat.admin.service.ActivityLobbyService;
import com.gameplat.admin.service.ActivityQualificationService;
import com.gameplat.admin.util.DateUtil2;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.util.BeanUtils;
import com.gameplat.common.util.DateUtil;
import com.gameplat.common.util.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 活动大厅业务
 */
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class ActivityLobbyServiceImpl extends ServiceImpl<ActivityLobbyMapper, ActivityLobby>
        implements ActivityLobbyService {

    @Autowired
    private ActivityLobbyConvert activityLobbyConvert;

    @Autowired
    private ActivityLobbyDiscountConvert activityLobbyDiscountConvert;

    @Autowired
    private ActivityLobbyDiscountService activityLobbyDiscountService;

    @Autowired
    private ActivityQualificationService activityQualificationService;

    @Autowired
    private ActivityDistributeService activityDistributeService;


    @Override
    public IPage<ActivityLobbyVO> findActivityLobbyList(PageDTO<ActivityLobby> page, ActivityLobbyDTO activityLobbyDTO) {
        LambdaQueryChainWrapper<ActivityLobby> queryWrapper = this.lambdaQuery();
        queryWrapper.like(StringUtils.isNotBlank(activityLobbyDTO.getTitle()), ActivityLobby::getTitle, activityLobbyDTO.getTitle())
                .eq(activityLobbyDTO.getStatus() != null, ActivityLobby::getStatus, activityLobbyDTO.getStatus());

        IPage<ActivityLobbyVO> activityLobbyVOIPage = queryWrapper.page(page).convert(activityLobbyConvert::toVo);
        if (CollectionUtils.isNotEmpty(activityLobbyVOIPage.getRecords())) {
            for (ActivityLobbyVO activityLobbyVO : activityLobbyVOIPage.getRecords()) {
                List<ActivityLobbyDiscountVO> activityLobbyDiscounts = activityLobbyDiscountService.listByActivityLobbyId(activityLobbyVO.getId());
                activityLobbyVO.setLobbyDiscountList(activityLobbyDiscounts);
            }
        }
        return activityLobbyVOIPage;
    }

    @Override
    public void add(ActivityLobbyAddDTO activityLobbyAddDTO) {
        ActivityLobby activityLobby = activityLobbyConvert.toEntity(activityLobbyAddDTO);
        //优惠打折列表
        List<ActivityLobbyDiscount> activityLobbyDiscounts = BeanUtils.mapList(activityLobbyAddDTO.getLobbyDiscountList(), ActivityLobbyDiscount.class);
        if (activityLobbyAddDTO.getStatisItem() == 10) {
            if (StringUtils.isNull(activityLobbyAddDTO.getMatchId())) {
                throw new ServiceException("指定比赛不能为空");
            }
            String matchTimeStr = DateUtil.dateToStr(activityLobbyAddDTO.getMatchTime(), DateUtil.YYYYMMDD);
            String startTimeStr = DateUtil.dateToStr(activityLobbyAddDTO.getStartTime(), DateUtil.YYYYMMDD);
            String endTimeStr = DateUtil.dateToStr(activityLobbyAddDTO.getEndTime(), DateUtil.YYYYMMDD);
            if (!matchTimeStr.equals(startTimeStr) || !matchTimeStr.equals(endTimeStr)) {
                throw new ServiceException("活动开始和结束时间必须和指定比赛的比赛时间是同一天");
            }
            if (activityLobbyAddDTO.getNextDayApply() == null || activityLobbyAddDTO.getNextDayApply() != 1) {
                throw new ServiceException("指定比赛必须选择隔天申请");
            }
        }

        List<String> applyDateList = new ArrayList<>();
        //计算活动的参与日期集合
        Integer detailDate = activityLobbyAddDTO.getDetailDate();
        String startTime;
        String endTime;
        if (activityLobbyAddDTO.getNextDayApply() == 1) {
            detailDate = detailDate + 1;
            if (detailDate == 7) {
                detailDate = 0;
            }
            startTime = DateUtil.dateToStr(cn.hutool.core.date.DateUtil.date(activityLobbyAddDTO.getStartTime()).offset(DateField.DAY_OF_YEAR, 1), "yyyy-MM-dd");
            endTime = DateUtil.dateToStr(cn.hutool.core.date.DateUtil.date(activityLobbyAddDTO.getEndTime()).offset(DateField.DAY_OF_YEAR, 1), "yyyy-MM-dd");
        } else {
            startTime = DateUtil.dateToStr(activityLobbyAddDTO.getStartTime(), DateUtil.YYYY_MM_DD);
            endTime = DateUtil.dateToStr(activityLobbyAddDTO.getEndTime(), DateUtil.YYYY_MM_DD);
        }

        if (activityLobbyAddDTO.getStatisDate() == 1) {
            applyDateList.add("每天");
        }

        //如果统计周期是每周，需要判断活动的结束时间是不是周日
        if (activityLobbyAddDTO.getStatisDate() == 2) {
            if (DateUtil2.getWeekNumOfDate(activityLobbyAddDTO.getEndTime()) != 7) {
                throw new ServiceException("统计日期选择每周，活动的结束日期应该为某周的星期天");
            }
            applyDateList = DateUtil2.getDayOfWeekWithinDateInterval(startTime, endTime, detailDate);
        }
        //如果统计周期是每月，需要判断活动的结束时间是不是某月的最后一天
        if (activityLobbyAddDTO.getStatisDate() == 3) {
            if (!DateUtil2.isSameDate(cn.hutool.core.date.DateUtil.endOfMonth(activityLobbyAddDTO.getEndTime()),
                    activityLobbyAddDTO.getEndTime())) {
                throw new ServiceException("统计日期选择每月，活动的结束日期应该为某月的最后一天");
            }
            List<Date> endDates = DateUtil2.findEndDates(activityLobbyAddDTO.getStartTime(), activityLobbyAddDTO.getEndTime());
            if (activityLobbyAddDTO.getNextDayApply() == 1) {
                for (Date date : endDates) {
                    applyDateList.add(DateUtil.dateToStr(cn.hutool.core.date.DateUtil.date(date).offset(DateField.DAY_OF_YEAR, 1), "yyyy-MM-dd"));
                }
            } else {
                for (Date date : endDates) {
                    applyDateList.add(DateUtil.dateToStr(date, "yyyy-MM-dd"));
                }
            }
        }
        //如果统计周期是每周X，需要判断活动的结束时间是不是每周X
        if (activityLobbyAddDTO.getStatisDate() == 4) {
            if (DateTime.of(activityLobbyAddDTO.getEndTime()).dayOfWeek() != activityLobbyAddDTO.getDetailDate()) {
                throw new ServiceException("统计日期选择" + DetailDateEnum.getWeek(activityLobbyAddDTO.getDetailDate())
                        + ",活动的结束日期应该为" + DetailDateEnum.getWeek(activityLobbyAddDTO.getDetailDate()));
            }
            applyDateList = DateUtil2.getDayOfWeekWithinDateInterval(startTime, endTime, detailDate);
        }

        //如果统计周期是每月X，需要判断活动的结束时间是不是每月X
        if (activityLobbyAddDTO.getStatisDate() == 5) {
            if (Integer.parseInt(String.format("%td", activityLobbyAddDTO.getEndTime())) != activityLobbyAddDTO.getDetailDate()) {
                throw new ServiceException("统计日期选择每月" + activityLobbyAddDTO.getDetailDate() + "号" + ",活动的结束日期那天应该为" + activityLobbyAddDTO.getDetailDate() + "号");
            }
            List<Date> dates = DateUtil.findDates(activityLobbyAddDTO.getStartTime(), activityLobbyAddDTO.getEndTime());
            for (Date date : dates) {
                String day = String.format("%td", date);
                if (Integer.parseInt(day) == activityLobbyAddDTO.getDetailDate()) {
                    applyDateList.add(DateUtil.dateToStr(date, DateUtil.YYYY_MM_DD));
                }
            }
            if (activityLobbyAddDTO.getNextDayApply() == 1) {
                List<String> copyDateList = new ArrayList<>();
                copyDateList.addAll(applyDateList);
                applyDateList.clear();
                for (String applyDate : copyDateList) {
                    applyDate = DateUtil.dateToStr(cn.hutool.core.date.DateUtil.date(DateUtil.strToDate(applyDate,
                            DateUtil.YYYY_MM_DD)).offset(DateField.DAY_OF_YEAR, 1), DateUtil.YYYY_MM_DD);
                    applyDateList.add(applyDate);
                }
            }
        }

        if (StringUtils.isEmpty(applyDateList)) {
            throw new ServiceException("统计日期不在活动有效期内！");
        }

        List<Integer> targetValueList = activityLobbyDiscounts.stream().map(ActivityLobbyDiscount::getTargetValue)
                .collect(Collectors.toList());
        long countTargetValue = targetValueList.stream().distinct().count();
        if (targetValueList.size() != countTargetValue) {
            throw new ServiceException("奖励赠送列表中,设定的目标值不能重复");
        }

        List<Integer> presenterValueList = activityLobbyDiscounts.stream().map(ActivityLobbyDiscount::getPresenterValue)
                .collect(Collectors.toList());
        long countPresenterValue = presenterValueList.stream().distinct().count();
        if (targetValueList.size() != countPresenterValue) {
            throw new ServiceException("奖励赠送列表中,设定的赠送金额不能重复");
        }

        if (activityLobbyAddDTO.getStatisItem() == 2 || activityLobbyAddDTO.getStatisItem() == 3
                || activityLobbyAddDTO.getStatisItem() == 7 || activityLobbyAddDTO.getStatisItem() == 8) {
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
//            activityLobbyDiscount.setCreateBy(SysUserUtil.getUserName());
            activityLobbyDiscount.setCreateTime(new Date());
        }
        boolean isSaveLobbyDiscount = activityLobbyDiscountService.saveBatchLobbyDiscount(activityLobbyDiscounts);
        if (!isSaveLobbyDiscount) {
            throw new ServiceException("添加优惠失败！");
        }
    }

    @Override
    public void update(ActivityLobbyUpdateDTO activityLobbyUpdateDTO) {
        //根据id查询活动大厅
        ActivityLobby activityLobbyOrigin = this.getById(activityLobbyUpdateDTO.getId());
        if (activityLobbyOrigin == null) {
            throw new ServiceException("该活动大厅不存在！");
        }
        //判断活动是否已上线
        if (activityLobbyOrigin.getStatus() == 1 || activityLobbyOrigin.getStatus() == 0) {
            boolean b = DateUtil2.compareCurrentDate(activityLobbyOrigin.getStartTime());
            boolean endTime = DateUtil2.compareCurrentDate(activityLobbyOrigin.getEndTime());
            if (!b && endTime) {
                throw new ServiceException("活动进行中，不可修改！");
            }
        }

        if (DateUtil2.compareCurrentDateLess(activityLobbyOrigin.getEndTime())) {
            throw new ServiceException("该活动已过期，如要创建一个新的活动，请点击新增添加");
        }

        if (activityLobbyUpdateDTO.getStatisItem() == 10) {
            if (StringUtils.isNull(activityLobbyUpdateDTO.getMatchId())) {
                throw new ServiceException("指定比赛不能为空");
            }
            if (!DateUtil2.isSameDate(activityLobbyUpdateDTO.getMatchTime(), activityLobbyUpdateDTO.getStartTime()) &&
                    !DateUtil2.isSameDate(activityLobbyUpdateDTO.getMatchTime(), activityLobbyUpdateDTO.getEndTime())) {
                throw new ServiceException("活动开始和结束时间必须和指定比赛的比赛时间是同一天");
            }
            if (activityLobbyUpdateDTO.getNextDayApply() == null || activityLobbyUpdateDTO.getNextDayApply() != 1) {
                throw new ServiceException("指定比赛必须选择隔天申请");
            }
        }

        List<String> applyDateList = new ArrayList<>();
        //计算活动的参与日期集合
        Integer detailDate = activityLobbyUpdateDTO.getDetailDate();
        String startTime;
        String endTime;
        if (activityLobbyUpdateDTO.getNextDayApply() == 1) {
            detailDate = detailDate + 1;
            if (detailDate == 7) {
                detailDate = 0;
            }
            startTime = DateUtil.dateToStr(
                    cn.hutool.core.date.DateUtil.date(activityLobbyUpdateDTO.getStartTime()).offset(DateField.DAY_OF_YEAR, 1), DateUtil.YYYY_MM_DD);
            endTime = DateUtil.dateToStr(cn.hutool.core.date.DateUtil.date(activityLobbyUpdateDTO.getEndTime()).offset(DateField.DAY_OF_YEAR, 1), DateUtil.YYYY_MM_DD);
        } else {
            startTime = DateUtil.dateToStr(activityLobbyUpdateDTO.getStartTime(), DateUtil.YYYY_MM_DD);
            endTime = DateUtil.dateToStr(activityLobbyUpdateDTO.getEndTime(), DateUtil.YYYY_MM_DD);
        }

        if (activityLobbyUpdateDTO.getStatisDate() == 1) {
            applyDateList.add("每天");
        }

        //如果统计周期是每周，需要判断活动的结束时间是不是周日
        if (activityLobbyUpdateDTO.getStatisDate() == 2) {
            if (DateUtil2.getWeekNumOfDate(activityLobbyUpdateDTO.getEndTime()) != 7) {
                throw new ServiceException("统计日期选择每周，活动的结束日期应该为某周的星期天");
            }

            applyDateList = DateUtil2.getDayOfWeekWithinDateInterval(startTime, endTime, detailDate);
        }
        //如果统计周期是每月，需要判断活动的结束时间是不是某月的最后一天
        if (activityLobbyUpdateDTO.getStatisDate() == 3) {
            if (!DateUtil2.isSameDate(cn.hutool.core.date.DateUtil.endOfMonth(activityLobbyUpdateDTO.getEndTime()), activityLobbyUpdateDTO.getEndTime())) {
                throw new ServiceException("统计日期选择每月，活动的结束日期应该为某月的最后一天");
            }
            List<Date> endDates = DateUtil2.findEndDates(activityLobbyUpdateDTO.getStartTime(), activityLobbyUpdateDTO.getEndTime());
            if (activityLobbyUpdateDTO.getNextDayApply() == 1) {
                for (Date date : endDates) {
                    applyDateList.add(DateUtil.dateToStr(cn.hutool.core.date.DateUtil.date(date).offset(DateField.DAY_OF_YEAR, 1), DateUtil.YYYY_MM_DD));
                }
            } else {
                for (Date date : endDates) {
                    applyDateList.add(DateUtil.dateToStr(date, DateUtil.YYYY_MM_DD));
                }
            }
        }
        //如果统计周期是每周X，需要判断活动的结束时间是不是每周X
        if (activityLobbyUpdateDTO.getStatisDate() == 4) {
            if (DateTime.of(activityLobbyUpdateDTO.getEndTime()).dayOfWeek() == activityLobbyUpdateDTO.getDetailDate().intValue()) {
                throw new ServiceException("统计日期选择" + DetailDateEnum.getWeek(activityLobbyUpdateDTO.getDetailDate())
                        + ",活动的结束日期应该为" + DetailDateEnum.getWeek(activityLobbyUpdateDTO.getDetailDate()));
            }
            applyDateList = DateUtil2.getDayOfWeekWithinDateInterval(startTime, endTime, detailDate);
        }

        //如果统计周期是每月X，需要判断活动的结束时间是不是每月X
        if (activityLobbyUpdateDTO.getStatisDate() == 5) {
            if (Integer.parseInt(String.format("%td", activityLobbyUpdateDTO.getEndTime())) != activityLobbyUpdateDTO.getDetailDate()) {
                throw new ServiceException("统计日期选择每月" + activityLobbyUpdateDTO.getDetailDate() + "号" + ",活动的结束日期那天应该为" + activityLobbyUpdateDTO.getDetailDate() + "号");
            }
            List<Date> dates = DateUtil.findDates(activityLobbyUpdateDTO.getStartTime(), activityLobbyUpdateDTO.getEndTime());
            for (Date date : dates) {
                String day = String.format("%td", date);
                if (Integer.parseInt(day) == activityLobbyUpdateDTO.getDetailDate()) {
                    applyDateList.add(DateUtil.dateToStr(date, DateUtil.YYYY_MM_DD));
                }
            }
            if (activityLobbyUpdateDTO.getNextDayApply() == 1) {
                List<String> copyDateList = new ArrayList<>();
                copyDateList.addAll(applyDateList);
                applyDateList.clear();
                for (String applyDate : copyDateList) {
                    applyDate = DateUtil.dateToStr(cn.hutool.core.date.DateUtil.date(DateUtil.strToDate(applyDate,
                            DateUtil.YYYY_MM_DD)).offset(DateField.DAY_OF_YEAR, 1), DateUtil.YYYY_MM_DD);
                    applyDateList.add(applyDate);
                }
            }
        }

        if (StringUtils.isEmpty(applyDateList)) {
            throw new ServiceException("统计日期不在活动有效期内！");
        }

        // 含有id的数据
        List<ActivityLobbyDiscount> hasIdList = new ArrayList<>();
        // 没有id的列表
        List<ActivityLobbyDiscount> noIdList = new ArrayList<>();
        // 所有的列表数据
        List<ActivityLobbyDiscount> allList = new ArrayList<>();
        Set<Long> hasIdSet = new HashSet<>();

        List<ActivityLobbyDiscountDTO> lobbyDiscountList = activityLobbyUpdateDTO.getLobbyDiscountList();
        // 遍历大厅优惠信息，完善优惠信息，有id就修改，没有id就新增
        for (ActivityLobbyDiscountDTO lobbyDiscountDTO : lobbyDiscountList) {
            ActivityLobbyDiscount activityLobbyDiscount = activityLobbyDiscountConvert.toEntity(lobbyDiscountDTO);
            if (lobbyDiscountDTO.getLobbyDiscountId() != null && lobbyDiscountDTO.getLobbyDiscountId() != 0) {
                hasIdList.add(activityLobbyDiscount);
                hasIdSet.add(lobbyDiscountDTO.getLobbyDiscountId());
            } else {
                activityLobbyDiscount.setLobbyId(activityLobbyOrigin.getId());
                noIdList.add(activityLobbyDiscount);
            }
        }

        // 需要删除的列表数据
        List<ActivityLobbyDiscount> deleteList = new ArrayList<>();
        //查询历史折扣列表
        List<ActivityLobbyDiscountVO> activityLobbyDiscounts = activityLobbyDiscountService.listByActivityLobbyId(activityLobbyUpdateDTO.getId());
        if (CollectionUtils.isNotEmpty(activityLobbyDiscounts)) {
            for (ActivityLobbyDiscountVO activityLobbyDiscountVO : activityLobbyDiscounts) {
                if (!hasIdSet.contains(activityLobbyDiscountVO.getLobbyDiscountId())) {
                    deleteList.add(activityLobbyDiscountConvert.toEntity(activityLobbyDiscountVO));
                }
            }
        }

        if (StringUtils.isNotEmpty(hasIdList)) {
            allList.addAll(hasIdList);
        }
        if (StringUtils.isNotEmpty(noIdList)) {
            allList.addAll(noIdList);
        }

        if (StringUtils.isNotEmpty(allList)) {
            List<Integer> targetValueList = allList.stream().map(ActivityLobbyDiscount::getTargetValue)
                    .collect(Collectors.toList());
            long countTargetValue = targetValueList.stream().distinct().count();
            if (targetValueList.size() != countTargetValue) {
                throw new ServiceException("奖励赠送列表目标数值不能重复");
            }

            List<Integer> presenterValueList = lobbyDiscountList.stream().map(ActivityLobbyDiscountDTO::getPresenterValue)
                    .collect(Collectors.toList());
            long countPresenterValue = presenterValueList.stream().distinct().count();
            if (targetValueList.size() != countPresenterValue) {
                throw new ServiceException("奖励赠送列表赠送金额数值不能重复");
            }
        }

        if (activityLobbyUpdateDTO.getStatisItem() == 2 || activityLobbyUpdateDTO.getStatisItem() == 3
                || activityLobbyUpdateDTO.getStatisItem() == 7 || activityLobbyUpdateDTO.getStatisItem() == 8) {
            for (ActivityLobbyDiscountDTO activityLobbyDiscountDTO : lobbyDiscountList) {
                if (activityLobbyDiscountDTO.getTargetValue() < 2) {
                    throw new ServiceException("奖励赠送列表中,设定的目标天数最小值为2");
                }
            }
        }

        activityLobbyUpdateDTO.setApplyDateList(String.join(",", applyDateList));
        ActivityLobby activityLobby = activityLobbyConvert.toEntity(activityLobbyUpdateDTO);
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
        //校验是否有活动不结束
        for (String idStr : idArr) {
            Long id = Long.parseLong(idStr);
            ActivityQualification activityQualification = new ActivityQualification();
            activityQualification.setActivityId(id);
            activityQualification.setDeleteFlag(1);
            activityQualification.setStatus(1);
            List<ActivityQualification> qualificationList = activityQualificationService.findQualificationList(activityQualification);
            if (StringUtils.isNotEmpty(qualificationList)) {
                throw new ServiceException("你要删除的活动中有未审核的会员资格记录");
            }

            ActivityDistribute activityDistribute = new ActivityDistribute();
            activityDistribute.setActivityId(id);
            activityDistribute.setDeleteFlag(1);
            activityDistribute.setStatus(1);
            List<ActivityDistribute> distributeList = activityDistributeService.findActivityDistributeList(activityDistribute);
            if (StringUtils.isNotEmpty(distributeList)) {
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
    public void updateStatus(ActivityLobbyUpdateStatusDTO activityLobbyUpdateStatusDTO) {
        ActivityLobby activityLobby = this.getById(activityLobbyUpdateStatusDTO.getId());
        if (activityLobby == null) {
            throw new ServiceException("活动大厅不存在，请重试！");
        }
        ActivityLobby activityLobby2 = activityLobbyConvert.toEntity(activityLobbyUpdateStatusDTO);
        this.updateById(activityLobby2);
    }

    @Override
    public List<ActivityLobbyVO> findUnboundLobbyList() {
        List<ActivityLobby> activityLobbyList = this.lambdaQuery().list();
        List<ActivityLobbyVO> lobbyList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(activityLobbyList)) {
            for (ActivityLobby activityLobby : activityLobbyList) {
                ActivityLobbyVO activityLobbyVO = activityLobbyConvert.toVo(activityLobby);
                lobbyList.add(activityLobbyVO);
            }
        }
        return lobbyList;
    }
}
