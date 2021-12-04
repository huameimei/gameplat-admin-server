package com.gameplat.admin.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.enums.BlacklistConstant;
import com.gameplat.admin.enums.LiveRebateReportStatus;
import com.gameplat.admin.mapper.LiveMemberDayReportMapper;
import com.gameplat.admin.mapper.LiveRebateConfigMapper;
import com.gameplat.admin.mapper.LiveRebateDetailMapper;
import com.gameplat.admin.mapper.LiveRebateReportMapper;
import com.gameplat.admin.model.domain.LiveBlacklist;
import com.gameplat.admin.model.domain.LiveRebateConfig;
import com.gameplat.admin.model.domain.LiveRebateDetail;
import com.gameplat.admin.model.domain.LiveRebatePeriod;
import com.gameplat.admin.model.domain.LiveRebateReport;
import com.gameplat.admin.model.dto.LiveRebateReportQueryDTO;
import com.gameplat.admin.model.vo.LiveMemberDayReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.LiveBlacklistService;
import com.gameplat.admin.service.LiveRebateReportService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.json.JsonUtils;
import com.gameplat.redis.api.RedisService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class LiveRebateReportServiceImpl extends
    ServiceImpl<LiveRebateReportMapper, LiveRebateReport> implements LiveRebateReportService {

  @Autowired
  private LiveRebateConfigMapper liveRebateConfigMapper;

  @Autowired
  private MemberService memberService;

  @Autowired
  private LiveBlacklistService liveBlacklistService;

  @Autowired
  private LiveRebateDetailMapper liveRebateDetailMapper;

  @Autowired
  private LiveMemberDayReportMapper liveMemberDayReportMapper;

  @Autowired
  private RedisService redisService;

  @Override
  public PageDtoVO<LiveRebateDetail> queryPage(Page<LiveRebateDetail> page,
      LiveRebateReportQueryDTO dto) {
    PageDtoVO<LiveRebateDetail> pageDtoVO = new PageDtoVO();
    //查看非当期返水用期数倒序排序
    String orderCloumn = "rebate_money";
    if(dto.getBeginDate() != null && dto.getEndDate() != null) {
      if(!DateUtil.parseDate(dto.getBeginDate()).equals(DateUtil.parseDate(dto.getEndDate()))) {
        orderCloumn = "rebate_money,stat_time";
      }
    }

    QueryWrapper<LiveRebateDetail> queryWrapper = Wrappers.query();
    queryWrapper.eq(ObjectUtils.isNotEmpty(dto.getUserAccount()),"account",dto.getUserAccount());

    if(StringUtils.isNotBlank(dto.getBeginDate())){
      queryWrapper.apply("begin_date >= STR_TO_DATE({0}, '%Y-%m-%d')",dto.getBeginDate());
    }
    if(StringUtils.isNotBlank(dto.getEndDate())){
      queryWrapper.apply("end_date <= STR_TO_DATE({0}, '%Y-%m-%d')",dto.getEndDate());
    }
    queryWrapper.orderByDesc(orderCloumn);

    Page<LiveRebateDetail> result = liveRebateDetailMapper.selectPage(page,queryWrapper);
    LiveRebateDetail total = liveRebateDetailMapper.queryAllLiveRebateReport(dto);
    Map<String, Object> otherData = new HashMap<String, Object>();
    otherData.put("totalData", total);
    pageDtoVO.setPage(result);
    pageDtoVO.setOtherData(otherData);
    return pageDtoVO;
  }

  /**
   * 真人返水拒发
   * @param userAccount
   * @param periodName
   * @param remark
   */
  @Override
  public void reject(String account, String periodName, String remark) {
    QueryWrapper<LiveRebateDetail> query = Wrappers.query();
    query.eq("account",account)
          .eq("period_name",periodName);
    LiveRebateDetail liveRebateDetail = liveRebateDetailMapper.selectOne(query);
    verifyAndUpdate(liveRebateDetail.getMemberId(), liveRebateDetail.getPeriodId(), LiveRebateReportStatus.REJECTED.getValue(), remark);
  }

  /**
   * 真人返水数据修改
   * @param id
   * @param realRebateMoney
   * @param remark
   */
  @Override
  public void modify(Long id, BigDecimal realRebateMoney, String remark) {
    //  TODO 是否需要加锁处理
      LiveRebateDetail liveRebateDetail = liveRebateDetailMapper.selectById(id);
      if (liveRebateDetail == null) {
        throw new ServiceException("返点记录不存在");
      }
      liveRebateDetail.setId(id);
      liveRebateDetail.setRealRebateMoney(realRebateMoney);
      liveRebateDetail.setRemark(remark);
      liveRebateDetailMapper.updateById(liveRebateDetail);
  }

  @Override
  public List<LiveRebateReport> queryDetail(LiveRebateReportQueryDTO dto) {
    return this.lambdaQuery()
        .eq(ObjectUtils.isNotEmpty(dto.getUserAccount()),LiveRebateReport::getAccount,dto.getUserAccount())
        .eq(ObjectUtils.isNotEmpty(dto.getPeriodName()),LiveRebateReport::getPeriodName, dto.getPeriodName())
        .list();
  }

  @Override
  public void createForLiveRebatePeriod(LiveRebatePeriod liveRebatePeriod){
    log.info("生成" + liveRebatePeriod.getName() + "真人返水");
    List<LiveRebateConfig> liveConfigList = liveRebateConfigMapper.selectList(new QueryWrapper<>());
    Map<String, List<BigDecimal[]>> map = convertToGameKindMap(liveConfigList);
    log.info("----------返水配置----------");
    log.info(JSONUtil.toJsonStr(map));
    //锁定层级会员
    Set<String> memberList = new HashSet<String>();
    if (StringUtils.isNotBlank(liveRebatePeriod.getBlackLevels())) {
      String[] levels = liveRebatePeriod.getBlackLevels().split(",");
      List<String> levelsLists = Arrays.stream(levels).filter(StringUtils::isEmpty).collect(Collectors.toList());
      List<String> list = memberService.findAccountByUserLevelIn(levelsLists);
      list.forEach(item -> memberList.add(item.toLowerCase()));
    }

    //锁定会员
    if (StringUtils.isNotBlank(liveRebatePeriod.getBlackAccounts())) {
      String[] accounts = liveRebatePeriod.getBlackAccounts().split(",");
      Arrays.stream(accounts).forEach(item -> memberList.add(item.toLowerCase()));
    }

    //真人返水黑名单
    LiveBlacklist example = new LiveBlacklist();
    example.setBlackType(String.valueOf(3));
    List<LiveBlacklist> liveBlacklists = liveBlacklistService.selectLiveBlackList(example);

    //真人日报表
    String startDate = DateUtil.formatDate(liveRebatePeriod.getBeginDate());
    String endDate = DateUtil.formatDate(liveRebatePeriod.getEndDate());
    List<LiveMemberDayReportVO> list = liveMemberDayReportMapper.
        findByStatTimeBetweenAndValidBetAmountGtZero(liveRebatePeriod,startDate,endDate);

    List<LiveRebateReport> resultList = new ArrayList<>();
    list.stream().filter(v -> !memberList.contains(v.getAccount().toLowerCase())).filter( v -> {
      for(LiveBlacklist liveBlack : liveBlacklists) {
        if(Objects.equals(BlacklistConstant.BizBlacklistTargetType.USER.getValue(),liveBlack.getTargetType())
            && v.getAccount().equalsIgnoreCase(liveBlack.getTarget())
            && ("," + liveBlack.getLiveCategory()).contains("," + v.getGameCode() + ",")){
          return false;
        }
        if(Objects.equals(BlacklistConstant.BizBlacklistTargetType.USER_LEVEL.getValue(),liveBlack.getTargetType())
            && v.getUserLevel().equalsIgnoreCase(liveBlack.getTarget())
            && ("," + liveBlack.getLiveCategory()).contains("," + v.getGameCode() + ",")) {
          return false;
        }
      }
      return true;
    }).filter(dr -> map.containsKey(combineCodeAndKind(dr.getUserLevel(),dr.getGameCode(),dr.getGameKind()))).forEach(dr -> {
      Optional<BigDecimal> rebate = map.get(combineCodeAndKind(dr.getUserLevel(),dr.getGameCode(),dr.getGameKind()))
          .stream().filter(a -> a[0].compareTo(dr.getValidAmount()) <= 0).map(a -> a[1]).findFirst();
      if (rebate.isPresent() && rebate.get().compareTo(BigDecimal.ZERO) > 0) {
        try {
          LiveRebateReport liveRebateReport = formatLiveRebateReport(liveRebatePeriod, dr,rebate.get());
          resultList.add(liveRebateReport);
        } catch (Exception e) {
          log.error("结算异常: " + JSONUtil.toJsonStr(dr));
          throw new RuntimeException("结算异常", e);
        }
      }
    });

    //批量插入
    if(resultList.size()>0){
      try {
          this.saveBatch(resultList);
      } catch (Exception e) {
        log.error("结算异常: " + JSONUtil.toJsonStr(resultList));
        throw new RuntimeException("结算异常", e);
      }
    }

  }

  private LiveRebateReport formatLiveRebateReport(LiveRebatePeriod period, LiveMemberDayReportVO liveMemberDayReport, BigDecimal rebate) {
    LiveRebateReport liveRebateReport = new LiveRebateReport();
    BeanUtils.copyProperties(liveMemberDayReport,liveRebateReport);
    liveRebateReport.setId(null);
    liveRebateReport.setRemark(period.getName()+"-真人返水");
    liveRebateReport.setPeriodId(period.getId());
    liveRebateReport.setPeriodName(period.getName());
    liveRebateReport.setBeginDate(period.getBeginDate());
    liveRebateReport.setEndDate(period.getEndDate());
    liveRebateReport.setStatus(LiveRebateReportStatus.UNACCEPTED.getValue());
    BigDecimal rebateMoney = NumberUtil.div(liveMemberDayReport.getValidAmount().multiply(rebate),
        100,4, RoundingMode.HALF_EVEN);
    liveRebateReport.setRebateMoney(rebateMoney);
    liveRebateReport.setRealRebateMoney(rebateMoney);
//        liveRebateReport.setValidAmount(liveUserDayReport.getValidAmount());
//        liveRebateReport.setStatTime(DateUtil.dateToYMD(liveUserDayReport.getStatTime()));
    log.info("----------用戶返水----------");
    log.info(JSONUtil.toJsonStr(liveRebateReport));
    return liveRebateReport;
  }


  /**
   * 转化返水配置为Map<String, List<Double[]>>结构
   * key: ${游戏类型}与${游戏子类型}拼接
   * value: 按照[${投注额阈值}, ${返水比率}]组成的以${投注额阈值}降序排序的列表
   */
  private Map<String, List<BigDecimal[]>> convertToGameKindMap(List<LiveRebateConfig> liveRebates) {
    Map<String, List<BigDecimal[]>> map = new HashMap<>();
    liveRebates.forEach(lr -> {
     log.info("lr:",lr);
     log.info(lr.getJson());
      List<Map> items = JsonUtils.toObject(lr.getJson(),List.class);
      items.stream().filter(item -> {// 过滤无返水或返水为0的项
        Number rebate = (Number) item.get("rebate");
        return rebate != null && rebate.doubleValue() > 0;
      }).forEach(item -> {
        String key = combineCodeAndKind(lr.getUserLevel(),(String) item.get("game"), (String) item.get("kind"));
        List<BigDecimal[]> rebates;
        if (map.containsKey(key)) {
          rebates = map.get(key);
        } else {
          rebates = new ArrayList<>();
          map.put(key, rebates);
        }
        rebates.add(new BigDecimal[]{lr.getMoney(), new BigDecimal(item.get("rebate").toString())});
      });
    });
    // 排序
    map.forEach((k, v) -> v.sort((a1, a2) -> {
      double d = a1[0].subtract(a2[0]).doubleValue();
      return Double.compare(0.0, d);
    }));
    return map;
  }

  private String combineCodeAndKind(String userLevel,String gameCode, String gameKind) {
    return String.format("%s_%s__%s",userLevel, gameCode, gameKind);
  }

  @Override
  public void accept(Long periodId, Long memberId, BigDecimal realRebateMoney, String remark) {
    verifyAndUpdate(memberId, periodId, LiveRebateReportStatus.ACCEPTED.getValue(), remark);

    // TODO 添加打码量

    // TODO 写账变

    // TODO 修改账户余额

  }

  @Override
  public void deleteByPeriodId(Long periodId) {
    LambdaQueryWrapper<LiveRebateReport> query = Wrappers.lambdaQuery();
    query.eq(ObjectUtils.isNotEmpty(periodId),LiveRebateReport::getPeriodId,periodId);
    this.remove(query);
  }


  private void verifyAndUpdate(Long memberId, Long periodId, Integer status, String remark)  {
    LambdaQueryWrapper<LiveRebateDetail> query = Wrappers.lambdaQuery();
    query
        .eq(ObjectUtils.isNotEmpty(memberId),LiveRebateDetail::getMemberId,memberId)
        .eq(ObjectUtils.isNotEmpty(periodId),LiveRebateDetail::getPeriodId,periodId);
    LiveRebateDetail liveRebateDetail = liveRebateDetailMapper.selectOne(query);

    if (liveRebateDetail.getStatus() == null) {
      throw new ServiceException("返点记录状态异常");
    }
    if (liveRebateDetail.getStatus() != LiveRebateReportStatus.UNACCEPTED.getValue()) {
      throw new ServiceException("返点记录已处理");
    }
    if (liveRebateDetail.getMemberId() == null) {
      throw new ServiceException("会员不存在");
    }
    liveRebateDetail.setStatus(status);
    liveRebateDetail.setRealRebateMoney(liveRebateDetail.getRealRebateMoney());
    liveRebateDetail.setRemark(remark);
    liveRebateDetailMapper.update(liveRebateDetail,query);
  }


  private void rollBackAndUpdate(Long memberId, Long periodId, Integer status,
      String remark) {
    LambdaQueryWrapper<LiveRebateDetail> query = Wrappers.lambdaQuery();
    query
        .eq(ObjectUtils.isNotEmpty(memberId),LiveRebateDetail::getMemberId,memberId)
        .eq(ObjectUtils.isNotEmpty(periodId),LiveRebateDetail::getPeriodId,periodId);
    LiveRebateDetail liveRebateDetail = liveRebateDetailMapper.selectOne(query);
    if (liveRebateDetail == null) {
      throw new ServiceException("返点记录不存在");
    }
    if (liveRebateDetail.getStatus() == null) {
      throw new ServiceException("返点记录状态异常");
    }
    liveRebateDetail.setStatus(status);
    liveRebateDetail.setRemark(remark);
    liveRebateDetailMapper.update(liveRebateDetail,query);
  }


  @Override
  public void rollBack(Long memberId, Long periodId, String periodName, BigDecimal realRebateMoney, String remark){

    rollBackAndUpdate(memberId, periodId, LiveRebateReportStatus.ROLLBACKED.getValue(), remark);

    //TODO 批量修改打码量--真人返水优惠打码量设为0
    //validWithdrawService.rollLiveRebateDML(remark);
    // TODO  写账变
    //userBillService.save(userId, TranTypes.LIVE_ROLLBACK.getValue(), -realRebateMoney, "",
     //   rollBackUserBillContent(userId, periodName, realRebateMoney), admin.getAccount());

    // TODO 修改账户余额
    /*UserMoneyBean userMoneyBean = new UserMoneyBean(userId);
    // 修改账户余额
    userMoneyBean.setMoney(-realRebateMoney);
    userService.updateMoney(userMoneyBean);*/
  }

}
