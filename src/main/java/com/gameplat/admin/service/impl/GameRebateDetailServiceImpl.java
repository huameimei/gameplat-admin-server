package com.gameplat.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.enums.GameRebateReportStatus;
import com.gameplat.admin.mapper.GameRebateDetailMapper;
import com.gameplat.admin.mapper.GameRebateReportMapper;
import com.gameplat.admin.service.GameRebateConfigService;
import com.gameplat.admin.service.GameRebateDetailService;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.model.entity.game.GameRebateConfig;
import com.gameplat.model.entity.game.GameRebateDetail;
import com.gameplat.model.entity.game.GameRebatePeriod;
import com.gameplat.model.entity.game.GameRebateReport;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class GameRebateDetailServiceImpl
    extends ServiceImpl<GameRebateDetailMapper, GameRebateDetail>
    implements GameRebateDetailService {

  @Autowired private GameRebateDetailMapper gameRebateDetailMapper;

  @Autowired private GameRebateReportMapper gameRebateReportMapper;

  @Autowired private GameRebateConfigService gameRebateConfigService;

  /**
   * 判断是否修改返点金额
   *
   * @param rebateMoney 根据返水比例计算出的返水总额
   * @param rebateMoney 真人有效打码量
   * @return
   */
  public BigDecimal isChangeRebateMoney(
      String hyLevel, BigDecimal rebateMoney, BigDecimal validAmount) {
    List<GameRebateConfig> gameConfigList = gameRebateConfigService.queryAll(hyLevel);

    // 根据有效打码量算出上限返点
    BigDecimal temp = BigDecimal.ZERO;

    if (rebateMoney.compareTo(BigDecimal.ZERO) == 0
        || validAmount.compareTo(BigDecimal.ZERO) == 0) {
      return temp;
    }

    if (gameConfigList != null) {
      String expand = "";
      for (int i = 0; i < gameConfigList.size(); i++) {
        if (gameConfigList.get(i).getMoney().compareTo(validAmount) > 0) {
          if (i == 0) {
            break;
          }
          expand = gameConfigList.get(i - 1).getExpand();
          if (StringUtils.isNotBlank(expand)) {
            JSONObject jsonObj = JSONUtil.parseObj(expand);
            temp = jsonObj.getBigDecimal("maxPreferential");
            break;
          }
        } else {
          // 最后一个区间,则按最后一个算
          if (i == gameConfigList.size() - 1) {
            expand = gameConfigList.get(i).getExpand();
            if (StringUtils.isNotBlank(expand)) {
              JSONObject jsonObj = JSONUtil.parseObj(expand);
              temp = jsonObj.getBigDecimal("maxPreferential");
              break;
            }
          }
        }
      }
    }
    // 如果为0，则无上限，算出返多少就返多少
    if (temp.compareTo(BigDecimal.ZERO) == 0) {
      return rebateMoney;
    } else {
      // 返水超过上限，则返上限金额
      return rebateMoney.compareTo(temp) > 0 ? temp : rebateMoney;
    }
  }

  @Override
  public List<GameRebateDetail> gameRebateDetailByStatus(Long periodId, int status) {
    return this.lambdaQuery()
        .eq(ObjectUtils.isNotEmpty(periodId), GameRebateDetail::getPeriodId, periodId)
        .eq(ObjectUtils.isNotEmpty(status), GameRebateDetail::getStatus, status)
        .list();
  }

  @Override
  public void deleteByPeriodId(Long periodId) {
    LambdaQueryWrapper<GameRebateDetail> query = Wrappers.lambdaQuery();
    query.eq(ObjectUtils.isNotEmpty(periodId), GameRebateDetail::getPeriodId, periodId);
    this.remove(query);
  }

  @Override
  public void createGameRebateDetail(GameRebatePeriod gameRebatePeriod) {
    List<GameRebateReport> gameRebateReportList =
        gameRebateReportMapper.queryGameRebateReportByStatus(
            gameRebatePeriod.getId(), GameRebateReportStatus.UNACCEPTED.getValue());
    List<GameRebateDetail> temp = new ArrayList<>();
    if (CollectionUtil.isNotEmpty(gameRebateReportList)) {
      for (GameRebateReport lr : gameRebateReportList) {
        if (lr.getMemberId() != null) {
          GameRebateDetail gameRebateDetail = new GameRebateDetail();
          BeanUtils.copyProperties(lr, gameRebateDetail);
          BigDecimal realRebateMoney =
              isChangeRebateMoney(lr.getUserLevel(), lr.getRealRebateMoney(), lr.getValidAmount());
          gameRebateDetail.setRealRebateMoney(realRebateMoney);
          temp.add(gameRebateDetail);
        }
      }
      // 批量插入
      if (CollectionUtil.isNotEmpty(temp)) {
        temp.forEach(item -> gameRebateDetailMapper.insert(item));
      }
    }
  }
}
