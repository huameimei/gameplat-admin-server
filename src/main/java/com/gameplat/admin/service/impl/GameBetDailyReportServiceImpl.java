package com.gameplat.admin.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.config.TenantConfig;
import com.gameplat.admin.mapper.GameBetDailyReportMapper;
import com.gameplat.admin.model.bean.ActivityStatisticItem;
import com.gameplat.admin.model.dto.GameBetDailyReportQueryDTO;
import com.gameplat.admin.model.vo.GameBetReportVO;
import com.gameplat.admin.model.vo.GameReportVO;
import com.gameplat.admin.model.vo.MemberInfoVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GameBetDailyReportService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.base.common.constant.ContextConstant;
import com.gameplat.base.common.constant.ContextConstant.ES_INDEX;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.DateUtil;
import com.gameplat.base.common.util.DateUtils;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.SettleStatusEnum;
import com.gameplat.common.enums.UserTypes;
import com.gameplat.model.entity.game.GameBetDailyReport;
import com.gameplat.model.entity.game.GamePlatform;
import com.gameplat.model.entity.member.Member;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.Strings;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilter;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedCardinality;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.aggregations.metrics.SumAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class GameBetDailyReportServiceImpl
    extends ServiceImpl<GameBetDailyReportMapper, GameBetDailyReport>
    implements GameBetDailyReportService {

  @Autowired public RestHighLevelClient restHighLevelClient;
  @Autowired private MemberService memberService;
  @Autowired private GameBetDailyReportMapper gameBetDailyReportMapper;
  @Autowired private TenantConfig tenantConfig;

  @Override
  public PageDtoVO queryPage(Page<GameBetDailyReport> page, GameBetDailyReportQueryDTO dto) {
    PageDtoVO<GameBetDailyReport> pageDtoVO = new PageDtoVO();
    if (StringUtils.isNotBlank(dto.getSuperAccount())) {
      Member member = memberService.getByAccount(dto.getSuperAccount()).orElse(null);
      if (ObjectUtil.isNotNull(member)) {
        throw new ServiceException("用户不存在");
      }
      dto.setUserPaths(member.getSuperPath());
      // 是否代理账号
      if (member.getUserType().equals(UserTypes.AGENT.value())) {
        dto.setAccount(null);
      }
    }
    QueryWrapper<GameBetDailyReport> queryWrapper = Wrappers.query();
    fillQueryWrapper(dto, queryWrapper);
    queryWrapper.orderByDesc(Lists.newArrayList("stat_time", "id"));

    Page<GameBetDailyReport> result = gameBetDailyReportMapper.selectPage(page, queryWrapper);

    QueryWrapper<GameBetDailyReport> queryOne = Wrappers.query();
    queryOne.select(
        "sum(bet_amount) as bet_amount,sum(valid_amount) as valid_amount,sum(win_amount) as win_amount");
    fillQueryWrapper(dto, queryOne);
    GameBetDailyReport gameBetDailyReport = gameBetDailyReportMapper.selectOne(queryOne);
    Map<String, Object> otherData = new HashMap<>();
    otherData.put("totalData", gameBetDailyReport);
    pageDtoVO.setPage(result);
    pageDtoVO.setOtherData(otherData);
    return pageDtoVO;
  }

  private void fillQueryWrapper(
      GameBetDailyReportQueryDTO dto, QueryWrapper<GameBetDailyReport> queryWrapper) {
    queryWrapper.eq(ObjectUtils.isNotEmpty(dto.getAccount()), "account", dto.getAccount());
    if (StringUtils.isNotEmpty(dto.getUserPaths())) {
      queryWrapper.likeRight("user_paths", dto.getUserPaths());
    }
    if (StringUtils.isNotEmpty(dto.getPlatformCode())) {
      queryWrapper.in("platform_code", Arrays.asList(dto.getPlatformCode().split(",")));
    }
    if (ObjectUtils.isNotEmpty(dto.getGameKindList())) {
      queryWrapper.in("game_kind", dto.getGameKindList());
    }
    queryWrapper.eq(ObjectUtils.isNotEmpty(dto.getGameType()), "game_type", dto.getGameType());
    queryWrapper.apply(
        ObjectUtils.isNotEmpty(dto.getBeginTime()),
        "stat_time >= STR_TO_DATE({0}, '%Y-%m-%d')",
        dto.getBeginTime());
    queryWrapper.apply(
        ObjectUtils.isNotEmpty(dto.getEndTime()),
        "stat_time <= STR_TO_DATE({0}, '%Y-%m-%d')",
        dto.getEndTime());
  }

  @Override
  public void saveGameBetDailyReport(String statTime, GamePlatform gamePlatform) {
    log.info(
        "{}[{}],statTime:[{}]> Start save game_user_day_report",
        gamePlatform.getName(),
        gamePlatform.getCode(),
        statTime);
    // 获取某一游戏平台当天的统计数据 结算时间为传入时间， 已结算的
    BoolQueryBuilder builder = QueryBuilders.boolQuery();
    builder.must(QueryBuilders.termQuery("tenant.keyword", tenantConfig.getTenantCode()));
    builder.must(QueryBuilders.termQuery("settle", SettleStatusEnum.YES.getValue()));
    builder.must(QueryBuilders.termQuery("platformCode.keyword", gamePlatform.getCode()));
    Date statDate = DateUtils.parseDate(statTime, DateUtils.DATE_PATTERN);
    String beginTime = cn.hutool.core.date.DateUtil.beginOfDay(statDate).toString();
    String endTime = cn.hutool.core.date.DateUtil.endOfDay(statDate).toString();
    builder.filter(
        QueryBuilders.rangeQuery("settleTime.keyword")
            .from(beginTime)
            .to(endTime)
            .format(DateUtils.DATE_TIME_PATTERN));

    // 统计条数
    CountRequest countRequest =
        new CountRequest(ContextConstant.ES_INDEX.BET_RECORD_ + tenantConfig.getTenantCode());
    countRequest.query(builder);
    try {
      RequestOptions.Builder optionsBuilder = RequestOptions.DEFAULT.toBuilder();
      optionsBuilder.setHttpAsyncResponseConsumerFactory(
          new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(31457280));
      CountResponse countResponse = restHighLevelClient.count(countRequest, optionsBuilder.build());
      long count = countResponse.getCount();
      if (count > 0) {
        log.info(
            "{}[{}],statTime:[{}] > game_user_day_report bet record data size:[{}]",
            gamePlatform.getName(),
            gamePlatform.getCode(),
            statTime,
            count);
        // 先删除统计数据
        LambdaUpdateWrapper<GameBetDailyReport> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper
            .eq(GameBetDailyReport::getPlatformCode, gamePlatform.getCode())
            .eq(GameBetDailyReport::getStatTime, statTime);
        int deleted = gameBetDailyReportMapper.delete(updateWrapper);
        log.info(
            "{}[{}],statTime:[{}] > game_user_day_report delete exists data size:[{}]",
            gamePlatform.getName(),
            gamePlatform.getCode(),
            statTime,
            deleted);
        log.info(
            "{}[{}],statTime:[{}]> Start save game_user_day_report",
            gamePlatform.getName(),
            gamePlatform.getCode(),
            statTime);
        // 查询出所有数据
        List<GameBetDailyReport> list = new ArrayList<>();
        SearchRequest searchRequest =
            new SearchRequest(ContextConstant.ES_INDEX.BET_RECORD_ + tenantConfig.getTenantCode());
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermsAggregationBuilder accountGroup =
            AggregationBuilders.terms("accountGroup").field("account.keyword");
        TermsAggregationBuilder gameKindGroup =
            AggregationBuilders.terms("gameKindGroup").field("gameKind.keyword");

        SumAggregationBuilder sumBetAmount =
            AggregationBuilders.sum("betAmount").field("betAmount");
        SumAggregationBuilder sumValidAmount =
            AggregationBuilders.sum("validAmount").field("validAmount");
        SumAggregationBuilder sumWinAmount =
            AggregationBuilders.sum("winAmount").field("winAmount");
        // 订单号去重
        CardinalityAggregationBuilder countBetCount =
            AggregationBuilders.cardinality("betCount").field("billNo.keyword");
        FilterAggregationBuilder countWinCount =
            AggregationBuilders.filter(
                "winCountFilter", QueryBuilders.rangeQuery("winAmount").gt(0));
        gameKindGroup.subAggregation(sumBetAmount);
        gameKindGroup.subAggregation(sumValidAmount);
        gameKindGroup.subAggregation(sumWinAmount);
        gameKindGroup.subAggregation(countBetCount);
        gameKindGroup.subAggregation(countWinCount);
        accountGroup.subAggregation(gameKindGroup);
        searchSourceBuilder.query(builder);

        searchSourceBuilder.aggregation(accountGroup);
        searchSourceBuilder.fetchSource(
            new FetchSourceContext(
                true, new String[] {"platformCode", "gameKind"}, Strings.EMPTY_ARRAY));
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse =
            restHighLevelClient.search(searchRequest, optionsBuilder.build());

        Terms accountTerms = searchResponse.getAggregations().get("accountGroup");
        for (Terms.Bucket bucket : accountTerms.getBuckets()) {
          String account = bucket.getKeyAsString();
          MemberInfoVO memberInfo = memberService.getMemberInfo(account);
          Member member =
              memberService
                  .getByAccount(account)
                  .orElseThrow(() -> new ServiceException("未找到对应会员信息"));
          Terms gameKindTerms = bucket.getAggregations().get("gameKindGroup");
          for (Terms.Bucket bucket2 : gameKindTerms.getBuckets()) {
            String gameKind = bucket2.getKeyAsString();
            double betCount =
                ((ParsedCardinality) bucket2.getAggregations().get("betCount")).getValue();
            double winCount =
                ((ParsedFilter) bucket2.getAggregations().get("winCountFilter")).getDocCount();
            double betAmount = ((ParsedSum) bucket2.getAggregations().get("betAmount")).getValue();
            double validAmount =
                ((ParsedSum) bucket2.getAggregations().get("validAmount")).getValue();
            double winAmount = ((ParsedSum) bucket2.getAggregations().get("winAmount")).getValue();
            // 保存生成数据
            GameBetDailyReport gameBetDailyReport = new GameBetDailyReport();
            gameBetDailyReport.setMemberId(memberInfo.getId());
            gameBetDailyReport.setAccount(memberInfo.getAccount());
            gameBetDailyReport.setRealName(memberInfo.getRealName());
            gameBetDailyReport.setSuperId(member.getParentId());
            gameBetDailyReport.setSuperAccount(member.getParentName());
            gameBetDailyReport.setUserPaths(memberInfo.getSuperPath());
            gameBetDailyReport.setUserType(memberInfo.getUserType());
            gameBetDailyReport.setPlatformCode(gameKind.split("_")[0]);
            gameBetDailyReport.setGameKind(gameKind);
            gameBetDailyReport.setGameType(gameKind.split("_")[1]);
            gameBetDailyReport.setBetAmount(new BigDecimal(betAmount));
            gameBetDailyReport.setValidAmount(new BigDecimal(validAmount));
            gameBetDailyReport.setWinAmount(new BigDecimal(winAmount));
            gameBetDailyReport.setBetCount((long) betCount);
            gameBetDailyReport.setWinCount((long) winCount);
            gameBetDailyReport.setStatTime(statTime);
            list.add(gameBetDailyReport);
          }
        }
        gameBetDailyReportMapper.insertGameBetDailyReport(list);
        log.info(
            "{}[{}],statTime:[{}] > game_user_day_report generate data size:[{}]",
            gamePlatform.getName(),
            gamePlatform.getCode(),
            statTime,
            list.size());
      } else {
        log.info(
            "{}[{}],statTime:[{}]> no data save to game_user_day_report",
            gamePlatform.getName(),
            gamePlatform.getCode(),
            statTime);
      }
      log.info(
          "{}[{}],statTime:[{}]> End save game_user_day_report",
          gamePlatform.getName(),
          gamePlatform.getCode(),
          statTime);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public List<GameReportVO> queryReportList(GameBetDailyReportQueryDTO dto) {
    return gameBetDailyReportMapper.queryReportList(dto);
  }

  @Override
  public PageDtoVO<GameBetReportVO> queryBetReportList(
      Page<GameBetDailyReportQueryDTO> page, GameBetDailyReportQueryDTO dto) {
    Page<GameBetReportVO> gameBetReportVOPage =
        gameBetDailyReportMapper.querybetReportList(page, dto);
    PageDtoVO<GameBetReportVO> pageDtoVO = new PageDtoVO<>();
    Map<String, Object> map = gameBetDailyReportMapper.querySumReport(dto);
    pageDtoVO.setPage(gameBetReportVOPage);
    pageDtoVO.setOtherData(map);
    return pageDtoVO;
  }

  @Override
  public List<ActivityStatisticItem> getGameReportInfo(Map map) {
    List<ActivityStatisticItem> activityStatisticItemVOList =
        gameBetDailyReportMapper.getGameReportInfo(map);
    // 连续体育打码天数和连续彩票打码天数需要返回活动期间内用户的打码日期集合，用于后续业务计算最大的连续打码天数
    if (map.get("statisItem") != null && StringUtils.isNotEmpty(activityStatisticItemVOList)) {
      if ((Integer) map.get("statisItem") == 8) {
        List<ActivityStatisticItem> gameDmlDateList =
            gameBetDailyReportMapper.findGameDmlDateList(map);
        if (StringUtils.isNotEmpty(gameDmlDateList)) {
          // 将逗号分隔的日期String转成List<Date>
          for (ActivityStatisticItem gameDmlDate : gameDmlDateList) {
            if (StringUtils.isNotEmpty(gameDmlDate.getGameCountDates())) {
              List<String> dateList = Arrays.asList(gameDmlDate.getGameCountDates().split(","));
              // 去重
              List<String> list = dateList.stream().distinct().collect(Collectors.toList());
              Collections.sort(list);
              List<Date> dates = new ArrayList<>();
              for (String date : list) {
                dates.add(DateUtil.strToDate(date, DateUtil.YYYY_MM_DD));
              }
              gameDmlDate.setGameCountDateList(dates);
            }
          }
        }

        for (ActivityStatisticItem activityStatisticItemVO : activityStatisticItemVOList) {
          for (ActivityStatisticItem gameDmlDate : gameDmlDateList) {
            if (activityStatisticItemVO.getUserName().equals(gameDmlDate.getUserName())) {
              activityStatisticItemVO.setGameCountDateList(gameDmlDate.getGameCountDateList());
            }
          }
        }
      }
    }
    return activityStatisticItemVOList;
  }

  @Override
  public void assembleBetDailyReport(List<String> list) {
    // todo 待完善
    List<GameBetDailyReport> assembleList = new ArrayList<>();
    list.forEach(
        day -> {
          DateTime parseTime = cn.hutool.core.date.DateUtil.parse(day, "yyyy-MM-dd");
          String beginTime = cn.hutool.core.date.DateUtil.beginOfDay(parseTime).toString();
          String endTime = cn.hutool.core.date.DateUtil.endOfDay(parseTime).toString();

          log.info("{}~{}游戏日报表汇总，开始执行...", beginTime, endTime);

          BoolQueryBuilder builder = QueryBuilders.boolQuery();
          builder.must(QueryBuilders.termQuery("tenant.keyword", tenantConfig.getTenantCode()));
          builder.must(QueryBuilders.termQuery("settle", SettleStatusEnum.YES.getValue()));
          builder.must(
              QueryBuilders.rangeQuery("settleTime.keyword")
                  .from(beginTime)
                  .to(endTime)
                  .format(DateUtils.DATE_TIME_PATTERN));

          SearchRequest searchRequest =
              new SearchRequest(ES_INDEX.BET_RECORD_ + tenantConfig.getTenantCode());
          SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
          TermsAggregationBuilder accountGroup =
              AggregationBuilders.terms("accountGroup").field("account.keyword");
          TermsAggregationBuilder gameKindGroup =
              AggregationBuilders.terms("gameKindGroup").field("gameKind.keyword");

          SumAggregationBuilder sumBetAmount =
              AggregationBuilders.sum("betAmount").field("betAmount");
          SumAggregationBuilder sumValidAmount =
              AggregationBuilders.sum("validAmount").field("validAmount");
          SumAggregationBuilder sumWinAmount =
              AggregationBuilders.sum("winAmount").field("winAmount");

          gameKindGroup.subAggregation(sumBetAmount);
          gameKindGroup.subAggregation(sumValidAmount);
          gameKindGroup.subAggregation(sumWinAmount);
          accountGroup.subAggregation(gameKindGroup);

          searchSourceBuilder.size(0);
          searchSourceBuilder.query(builder);
          searchSourceBuilder.aggregation(accountGroup);
          searchSourceBuilder.fetchSource(
              new FetchSourceContext(
                  true, new String[] {"platformCode", "gameKind"}, Strings.EMPTY_ARRAY));
          searchRequest.source(searchSourceBuilder);
          try {
            RequestOptions.Builder optionsBuilder = RequestOptions.DEFAULT.toBuilder();
            optionsBuilder.setHttpAsyncResponseConsumerFactory(
                new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(31457280));
            SearchResponse search =
                restHighLevelClient.search(searchRequest, optionsBuilder.build());
            Terms terms = search.getAggregations().get("accountGroup");

            for (Terms.Bucket bucket : terms.getBuckets()) {
              String account = bucket.getKeyAsString();
              Terms terms2 = bucket.getAggregations().get("gameKindGroup");
              for (Terms.Bucket bucket2 : terms2.getBuckets()) {
                String gameKind = bucket2.getKeyAsString();
                long count = bucket2.getDocCount();
                double betAmount =
                    ((ParsedSum) bucket2.getAggregations().get("betAmount")).getValue();
                double validAmount =
                    ((ParsedSum) bucket2.getAggregations().get("validAmount")).getValue();
                double winAmount =
                    ((ParsedSum) bucket2.getAggregations().get("winAmount")).getValue();

                GameBetDailyReport report = new GameBetDailyReport();
                report.setAccount(account);
                report.setPlatformCode(gameKind.split("_")[0]);
                report.setGameKind(gameKind);
                report.setGameType(gameKind.split("_")[1]);
                report.setBetCount(count);
                report.setBetAmount(new BigDecimal(betAmount));
                report.setValidAmount(new BigDecimal(validAmount));
                report.setWinAmount(new BigDecimal(winAmount));
                report.setStatTime(day);
                assembleList.add(report);
              }
            }
          } catch (IOException e) {
            e.printStackTrace();
          }
          log.info("{}~{}游戏日报表汇总，执行结束。。。", beginTime, endTime);
        });

    gameBetDailyReportMapper.insertGameBetDailyReport(assembleList);
  }

  @Override
  public List<GameReportVO> queryGamePlatformReport(GameBetDailyReportQueryDTO dto) {
    if (StringUtils.isBlank(dto.getBeginTime())) {
      String beginTime = DateUtil.getDateToString(new Date());
      dto.setBeginTime(beginTime);
    }
    if (StringUtils.isBlank(dto.getEndTime())) {
      String endTime = DateUtil.getDateToString(new Date());
      dto.setEndTime(endTime);
    }
    return gameBetDailyReportMapper.queryGamePlatformReport(dto);
  }

  //获取达到有效投注金额的会员账号
  @Override
  public List<String> getSatisfyBetAccount(String minBetAmount, String startTime, String endTime) {

    return gameBetDailyReportMapper.getSatisfyBetAccount(minBetAmount, startTime, endTime);
  }
}
