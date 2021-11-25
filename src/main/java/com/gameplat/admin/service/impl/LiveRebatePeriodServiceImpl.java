package com.gameplat.admin.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.LiveRebatePeriodConvert;
import com.gameplat.admin.enums.LiveRebatePeriodStatus;
import com.gameplat.admin.mapper.LiveRebatePeriodMapper;
import com.gameplat.admin.model.domain.LiveRebatePeriod;
import com.gameplat.admin.model.dto.LiveRebatePeriodQueryDTO;
import com.gameplat.admin.model.dto.OperLiveRebatePeriodDTO;
import com.gameplat.admin.model.vo.LiveRebatePeriodVO;
import com.gameplat.admin.service.LiveRebateDetailService;
import com.gameplat.admin.service.LiveRebatePeriodService;
import com.gameplat.admin.service.LiveRebateReportService;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.util.DateUtils;
import com.gameplat.common.util.StringUtils;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class LiveRebatePeriodServiceImpl extends
    ServiceImpl<LiveRebatePeriodMapper, LiveRebatePeriod> implements LiveRebatePeriodService {

  @Autowired
  private LiveRebatePeriodMapper liveRebatePeriodMapper;

  @Autowired
  private LiveRebatePeriodConvert liveRebatePeriodConvert;

  @Autowired
  private LiveRebateReportService liveRebateReportService;

  @Autowired
  private LiveRebateDetailService liveRebateDetailService;

  @Override
  public IPage<LiveRebatePeriodVO> queryLiveRebatePeriod(Page<LiveRebatePeriod> page,
      LiveRebatePeriodQueryDTO dto) {
    QueryWrapper<LiveRebatePeriod> queryWrapper = Wrappers.query();
    queryWrapper.eq(ObjectUtils.isNotEmpty(dto.getName()), "name", dto.getName())
        .eq(ObjectUtils.isNotEmpty(dto.getStatus()), "status", dto.getStatus());
    if (StringUtils.isNotBlank(dto.getStartTime())){
      queryWrapper.apply("begin_date >= STR_TO_DATE({0}, '%Y-%m-%d')",dto.getStartTime());
    }
    if (StringUtils.isNotBlank(dto.getEndTime())){
      queryWrapper.apply("end_date <= STR_TO_DATE({0}, '%Y-%m-%d')",dto.getEndTime());
    }
    queryWrapper.orderByDesc("end_date");
    IPage<LiveRebatePeriodVO> liveRebatePeriodVoPage = liveRebatePeriodMapper.selectPage(page, queryWrapper)
        .convert(liveRebatePeriodConvert::toVo);

    List<LiveRebatePeriodVO> liveRebatePeriods = liveRebatePeriodMapper.queryLiveRebateCount();
    for (LiveRebatePeriodVO liveRebatePeriodVO : liveRebatePeriodVoPage.getRecords()) {
      for (LiveRebatePeriodVO liveRebateCount : liveRebatePeriods) {
        if (liveRebatePeriodVO.getId().equals(liveRebateCount.getId())) {
          liveRebatePeriodVO.setLiveRebateCount(liveRebateCount.getLiveRebateCount());
          liveRebatePeriodVO.setRealRebateMoney(liveRebateCount.getRealRebateMoney());
        }
      }
    }
    return liveRebatePeriodVoPage;
  }

  @Override
  public void addLiveRebatePeriod(OperLiveRebatePeriodDTO dto) {
    LiveRebatePeriod liveRebatePeriod = liveRebatePeriodConvert.toEntity(dto);
    liveRebatePeriod.setStatus(LiveRebatePeriodStatus.UNSETTLED.getValue());
    prePersist(liveRebatePeriod);
    if (!this.save(liveRebatePeriod)) {
      throw new ServiceException("新增真人返点期数配置失败!");
    }
  }

  @Override
  public void updateLiveRebatePeriod(OperLiveRebatePeriodDTO dto) {
    LiveRebatePeriod liveRebatePeriod = liveRebatePeriodConvert.toEntity(dto);
    prePersist(liveRebatePeriod);
    if (!this.updateById(liveRebatePeriod)) {
      throw new ServiceException("修改真人返点期数配置失败!");
    }
  }

  @Override
  public void deleteLiveRebatePeriod(Long id,boolean only) {
    if (!this.removeById(id)) {
      throw new ServiceException("删除真人返点期数配置失败!");
    }
    if(!only) {
      //删除真人返点报表记录
      liveRebateReportService.deleteByPeriodId(id);
      //删除真人返点详情记录
      liveRebateDetailService.deleteByPeriodId(id);
    }
  }

  @Override
  public void settle(Long periodId) {
    LiveRebatePeriod liveRebatePeriod = this.getById(periodId);
    if (liveRebatePeriod == null) {
      throw new ServiceException("期数配置不存在");
    }
    //先修改结算状态
    LiveRebatePeriod period = new LiveRebatePeriod();
    period.setStatus(LiveRebatePeriodStatus.SETTLED.getValue());
    LambdaUpdateWrapper<LiveRebatePeriod> wrapper = Wrappers.lambdaUpdate();
    wrapper.eq(LiveRebatePeriod::getId,periodId);
    if(!this.update(period,wrapper)){
      throw new ServiceException("更新真人期数配置失败！");
    }
    //按平台生成返水报表
    liveRebateReportService.deleteByPeriodId(liveRebatePeriod.getId());
    liveRebateReportService.createForLiveRebatePeriod(liveRebatePeriod);
    //按最大返水限额生成返水详情
    liveRebateDetailService.deleteByPeriodId(liveRebatePeriod.getId());
    liveRebateDetailService.createLiveRebateDetail(liveRebatePeriod);
  }

  private void prePersist(LiveRebatePeriod liveRebatePeriod){
    Date beginDate = DateUtil.beginOfDay(liveRebatePeriod.getBeginDate());
    Date endDate = DateUtil.beginOfDay(liveRebatePeriod.getEndDate());
    if (beginDate.getTime() > endDate.getTime()) {
      throw new ServiceException("起始时间必须在截止时间之前");
    }
    if (countByDateRange(liveRebatePeriod.getId(), beginDate, endDate) > 0) {
      throw new ServiceException("时间范围与其它期号重复");
    }
    liveRebatePeriod.setBeginDate(beginDate);
    liveRebatePeriod.setEndDate(endDate);
    liveRebatePeriod.setName(beginDate.getTime() == endDate.getTime() ? DateUtils.format(beginDate)
        : DateUtils.format(beginDate) + " ~ " + DateUtils.format(endDate));
  }


  public int countByDateRange(Long id, Date beginDate, Date endDate) {
    return this.lambdaQuery()
        .ne(ObjectUtil.isNotEmpty(id), LiveRebatePeriod::getId, id)
        .and(wrapper -> wrapper.and(i ->
            i.le(ObjectUtils.isNotEmpty(beginDate), LiveRebatePeriod::getBeginDate, beginDate)
                .ge(ObjectUtils.isNotEmpty(beginDate), LiveRebatePeriod::getEndDate, beginDate)))
        .or()
        .and(i -> i.le(ObjectUtils.isNotEmpty(endDate), LiveRebatePeriod::getBeginDate, endDate)
            .ge(ObjectUtils.isNotEmpty(endDate), LiveRebatePeriod::getEndDate, endDate))
        .count();
  }

}
