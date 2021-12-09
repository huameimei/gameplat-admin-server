package com.gameplat.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.enums.LiveRebateReportStatus;
import com.gameplat.admin.mapper.LiveRebateDetailMapper;
import com.gameplat.admin.mapper.LiveRebateReportMapper;
import com.gameplat.admin.model.domain.LiveRebateConfig;
import com.gameplat.admin.model.domain.LiveRebateDetail;
import com.gameplat.admin.model.domain.LiveRebatePeriod;
import com.gameplat.admin.model.domain.LiveRebateReport;
import com.gameplat.admin.service.LiveRebateConfigService;
import com.gameplat.admin.service.LiveRebateDetailService;
import com.gameplat.base.common.util.StringUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class LiveRebateDetailServiceImpl extends
    ServiceImpl<LiveRebateDetailMapper, LiveRebateDetail> implements LiveRebateDetailService {

  @Autowired
  private LiveRebateDetailMapper liveRebateDetailMapper;

  @Autowired
  private LiveRebateReportMapper liveRebateReportMapper;

  @Autowired
  private LiveRebateConfigService liveRebateConfigService;

  /**
   * 判断是否修改返点金额
   *
   * @param rebateMoney 根据返水比例计算出的返水总额
   * @param rebateMoney 真人有效打码量
   * @return
   */
  public BigDecimal isChangeRebateMoney(String hyLevel, BigDecimal rebateMoney, BigDecimal validAmount) {
    List<LiveRebateConfig> liveConfigList = liveRebateConfigService.queryAll(hyLevel);

    //根据有效打码量算出上限返点
    BigDecimal temp = BigDecimal.ZERO;

    if (rebateMoney.compareTo(BigDecimal.ZERO) == 0 || validAmount.compareTo(BigDecimal.ZERO) == 0) {
      return temp;
    }

    if (liveConfigList != null) {
      String expand = "";
      for (int i = 0; i < liveConfigList.size(); i++) {
        if (liveConfigList.get(i).getMoney().compareTo(validAmount) > 0) {
          if (i == 0) {
            break;
          }
          expand = liveConfigList.get(i - 1).getExpand();
          if (StringUtils.isNotBlank(expand)) {
            JSONObject jsonObj = JSONUtil.parseObj(expand);
            temp = jsonObj.getBigDecimal("maxPreferential");
            break;
          }
        }else{
          //最后一个区间,则按最后一个算
          if (i == liveConfigList.size() - 1) {
            expand = liveConfigList.get(i).getExpand();
            if (StringUtils.isNotBlank(expand)) {
              JSONObject jsonObj = JSONUtil.parseObj(expand);
              temp = jsonObj.getBigDecimal("maxPreferential");
              break;
            }
          }
        }
      }
    }
    //如果为0，则无上限，算出返多少就返多少
    if (temp.compareTo(BigDecimal.ZERO) == 0) {
      return rebateMoney;
    } else {
      //返水超过上限，则返上限金额
      return rebateMoney.compareTo(temp) > 0 ? temp : rebateMoney;
    }
  }


  @Override
  public List<LiveRebateDetail> liveRebateDetailByStatus(Long periodId, int status) {
    return this.lambdaQuery()
        .eq(ObjectUtils.isNotEmpty(periodId), LiveRebateDetail::getPeriodId, periodId)
        .eq(ObjectUtils.isNotEmpty(status), LiveRebateDetail::getStatus, status)
        .list();
  }

  @Override
  public void deleteByPeriodId(Long periodId) {
    LambdaQueryWrapper<LiveRebateDetail> query = Wrappers.lambdaQuery();
    query.eq(ObjectUtils.isNotEmpty(periodId), LiveRebateDetail::getPeriodId, periodId);
    this.remove(query);
  }


  @Override
  public void createLiveRebateDetail(LiveRebatePeriod liveRebatePeriod) {
    List<LiveRebateReport> liveRebateReportList = liveRebateReportMapper
        .queryLiveRebateReportByStatus(liveRebatePeriod.getId(), LiveRebateReportStatus.UNACCEPTED.getValue());
    List<LiveRebateDetail> temp = new ArrayList<>();
    if (CollectionUtil.isNotEmpty(liveRebateReportList)){
      for (LiveRebateReport lr : liveRebateReportList) {
        if (lr.getMemberId() != null) {
          LiveRebateDetail liveRebateDetail = new LiveRebateDetail();
          BeanUtils.copyProperties(lr,liveRebateDetail);
          BigDecimal realRebateMoney = isChangeRebateMoney(lr.getUserLevel(),
              lr.getRealRebateMoney(), lr.getValidAmount());
          liveRebateDetail.setRealRebateMoney(realRebateMoney);
          temp.add(liveRebateDetail);
        }
      }
      //批量插入
      if (CollectionUtil.isNotEmpty(temp)){
        temp.forEach(item -> liveRebateDetailMapper.insert(item));
      }
    }
  }
}
