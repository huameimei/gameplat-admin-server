package com.gameplat.admin.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.config.TenantConfig;
import com.gameplat.admin.mapper.GameRebateDataMapper;
import com.gameplat.admin.model.dto.GameRebateDataQueryDTO;
import com.gameplat.admin.model.vo.GameReportVO;
import com.gameplat.admin.model.vo.MemberInfoVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GameRebateDataService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.base.common.constant.ContextConstant;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.DateUtils;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.SettleStatusEnum;
import com.gameplat.common.enums.UserTypes;
import com.gameplat.common.lang.Assert;
import com.gameplat.model.entity.game.GamePlatform;
import com.gameplat.model.entity.game.GameRebateData;
import com.gameplat.model.entity.member.Member;
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
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class GameRebateDataServiceImpl extends ServiceImpl<GameRebateDataMapper, GameRebateData>
    implements GameRebateDataService {

  @Autowired private MemberService memberService;

  @Autowired private GameRebateDataMapper gameRebateDataMapper;

  @Autowired private TenantConfig tenantConfig;

  @Autowired public RestHighLevelClient restHighLevelClient;

  @Override
  public PageDtoVO<GameRebateData> queryPageData(
      Page<GameRebateData> page, GameRebateDataQueryDTO dto) {
    PageDtoVO<GameRebateData> pageDtoVO = new PageDtoVO();
    if (StringUtils.isNotBlank(dto.getSuperAccount())) {
      Member member = memberService.getByAccount(dto.getSuperAccount()).orElse(null);
      Assert.isNull(member,"用户不存在");
      dto.setUserPaths(member.getSuperPath());
      // 是否代理账号
      if (member.getUserType().equals(UserTypes.AGENT.value())) {
        dto.setAccount(null);
      }
    }
    QueryWrapper<GameRebateData> queryWrapper = fillGameRebateDataWrapper(dto);
    queryWrapper.orderByDesc("stat_time");
    Page<GameRebateData> result = gameRebateDataMapper.selectPage(page, queryWrapper);
    if (result != null) {
      QueryWrapper<GameRebateData> queryOne = fillGameRebateDataWrapper(dto);
      queryOne.select("sum(bet_amount) as bet_amount,sum(valid_amount) as valid_amount,sum(win_amount) as win_amount");
      GameRebateData total = gameRebateDataMapper.selectOne(queryOne);
      Map<String, Object> otherData = new HashMap<String, Object>();
      otherData.put("totalData", total);
      pageDtoVO.setPage(result);
      pageDtoVO.setOtherData(otherData);
    }
    return pageDtoVO;
  }

  @NotNull
  private QueryWrapper<GameRebateData> fillGameRebateDataWrapper(GameRebateDataQueryDTO dto) {
    QueryWrapper<GameRebateData> queryWrapper = Wrappers.query();
    queryWrapper.eq(ObjectUtils.isNotEmpty(dto.getAccount()), "account", dto.getAccount());
    if (ObjectUtils.isNotEmpty(dto.getPlatformCodeList())) {
      queryWrapper.in("platform_code", dto.getPlatformCodeList());
    }
    if (ObjectUtils.isNotEmpty(dto.getGameKindList())) {
      queryWrapper.in("game_kind", dto.getGameKindList());
    }
    queryWrapper.apply(
        ObjectUtils.isNotEmpty(dto.getBeginTime()),
        "stat_time >= STR_TO_DATE({0}, '%Y-%m-%d')",
        dto.getBeginTime());
    queryWrapper.apply(
        ObjectUtils.isNotEmpty(dto.getEndTime()),
        "stat_time <= STR_TO_DATE({0}, '%Y-%m-%d')",
        dto.getEndTime());
    if (ObjectUtils.isNotEmpty(dto.getUserPaths())) {
      queryWrapper.likeRight("user_paths", dto.getUserPaths());
    }
    return queryWrapper;
  }

  /**
   *  交收日报表
   */
  @Override
  public void saveRebateDayReport(String statTime, GamePlatform gamePlatform) {
    log.info("{}[{}],statTime:[{}]> Start save game_rebate_data", gamePlatform.getName(), gamePlatform.getCode(), statTime);
    // 获取某一游戏平台当天的统计数据 结算时间为传入时间， 已结算的
    BoolQueryBuilder builder = QueryBuilders.boolQuery();
    builder.must(QueryBuilders.termQuery("tenant.keyword", tenantConfig.getTenantCode()));
    builder.must(QueryBuilders.termQuery("settle", SettleStatusEnum.YES.getValue()));
    builder.must(QueryBuilders.termQuery("platformCode.keyword", gamePlatform.getCode()));
    Date statDate = DateUtils.parseDate(statTime, DateUtils.DATE_PATTERN);
    String beginTime = cn.hutool.core.date.DateUtil.beginOfDay(statDate).toString();
    String endTime = cn.hutool.core.date.DateUtil.endOfDay(statDate).toString();
    builder.filter(QueryBuilders.rangeQuery("statTime.keyword")
            .from(beginTime)
            .to(endTime)
            .format(DateUtils.DATE_TIME_PATTERN));
    CountRequest countRequest =
            new CountRequest(ContextConstant.ES_INDEX.BET_RECORD_ + tenantConfig.getTenantCode());
    countRequest.query(builder);
    try {
      RequestOptions.Builder optionsBuilder = RequestOptions.DEFAULT.toBuilder();
      optionsBuilder.setHttpAsyncResponseConsumerFactory(
              new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(31457280));
      CountResponse countResponse = restHighLevelClient.count(countRequest, optionsBuilder.build());
      long count = countResponse.getCount();
      if ( count > 0){
        log.info("{}[{}],statTime:[{}]> game_rebate_data Rebate bet record data size:[{}]", gamePlatform.getName(), gamePlatform.getCode(), statTime,count);
        // 先删除统计数据
        LambdaUpdateWrapper<GameRebateData> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(GameRebateData::getPlatformCode,gamePlatform.getCode())
                .eq(GameRebateData::getStatTime,statTime);
        int deleted = gameRebateDataMapper.delete(updateWrapper);
        log.info("{}[{}],statTime:[{}]> game_rebate_data deleted data size:[{}]", gamePlatform.getName(), gamePlatform.getCode(), statTime,deleted);
        log.info("{}[{}],statTime:[{}]> Start save game_rebate_data", gamePlatform.getName(), gamePlatform.getCode(), statTime);
        //查询出所有数据
        List<GameRebateData>  list = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest(ContextConstant.ES_INDEX.BET_RECORD_ + tenantConfig.getTenantCode());
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermsAggregationBuilder accountGroup =
                AggregationBuilders.terms("accountGroup").field("account.keyword");
        TermsAggregationBuilder gameKindGroup =
                AggregationBuilders.terms("gameKindGroup").field("gameKind.keyword");

        SumAggregationBuilder sumBetAmount = AggregationBuilders.sum("betAmount").field("betAmount");
        SumAggregationBuilder sumValidAmount = AggregationBuilders.sum("validAmount").field("validAmount");
        SumAggregationBuilder sumWinAmount = AggregationBuilders.sum("winAmount").field("winAmount");
        //订单号去重
        CardinalityAggregationBuilder countBetCount = AggregationBuilders.cardinality("betCount").field("billNo.keyword");
        FilterAggregationBuilder countWinCount = AggregationBuilders.filter("winCountFilter", QueryBuilders.rangeQuery("winAmount").gt(0));
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
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest,optionsBuilder.build());
        Terms accountTerms = searchResponse.getAggregations().get("accountGroup");

        for (Terms.Bucket bucket : accountTerms.getBuckets()) {
          String account = bucket.getKeyAsString();
          MemberInfoVO memberInfo = memberService.getMemberInfo(account);
          Terms gameKindTerms = bucket.getAggregations().get("gameKindGroup");
          for (Terms.Bucket bucket2 : gameKindTerms.getBuckets()) {
            String gameKind = bucket2.getKeyAsString();
            double betCount =  ((ParsedCardinality) bucket2.getAggregations().get("betCount")).getValue();
            double winCount =  ((ParsedFilter) bucket2.getAggregations().get("winCountFilter")).getDocCount();
            double betAmount =  ((ParsedSum) bucket2.getAggregations().get("betAmount")).getValue();
            double validAmount = ((ParsedSum) bucket2.getAggregations().get("validAmount")).getValue();
            double winAmount = ((ParsedSum) bucket2.getAggregations().get("winAmount")).getValue();

            GameRebateData gameRebateData = new GameRebateData();
            gameRebateData.setMemberId(memberInfo.getId());
            gameRebateData.setAccount(account);
            gameRebateData.setRealName(memberInfo.getRealName());
            gameRebateData.setUserPaths(memberInfo.getSuperPath());
            gameRebateData.setPlatformCode(gameKind.split("_")[0]);
            gameRebateData.setGameType(gameKind.split("_")[1]);
            gameRebateData.setGameKind(gameKind);
            gameRebateData.setStatTime(statTime);
            gameRebateData.setBetAmount(new BigDecimal(betAmount));
            gameRebateData.setValidAmount(new BigDecimal(validAmount));
            gameRebateData.setWinAmount(new BigDecimal(winAmount));
            gameRebateData.setBetCount((int) betCount);
            gameRebateData.setWinCount((int) winCount);
            list.add(gameRebateData);
          }
        }
          // 生成统计数据
        gameRebateDataMapper.insertAllBatch(list);
      } else {
        log.info("{}[{}],statTime:[{}]> no data save to game_rebate_data", gamePlatform.getName(), gamePlatform.getCode(), statTime);
      }
      log.info("{}[{}],statTime:[{}]> End  save game_rebate_data", gamePlatform.getName(), gamePlatform.getCode(), statTime);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public List<GameReportVO> queryGameReport(GameRebateDataQueryDTO dto) {
    if (StringUtils.isNotBlank(dto.getSuperAccount())) {
      Member member = memberService.getByAccount(dto.getSuperAccount()).orElse(null);
      if (member == null) {
        throw new ServiceException("用户不存在");
      }
      dto.setUserPaths(member.getSuperPath());
      // 是否代理账号
      dto.setAccount(null);
    }
    return gameRebateDataMapper.queryGameReport(dto);
  }
}
