package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.config.TenantConfig;
import com.gameplat.admin.mapper.GameRebateDataMapper;
import com.gameplat.admin.model.dto.GameRebateDataQueryDTO;
import com.gameplat.admin.model.vo.GameReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GameRebateDataService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.base.common.constant.ContextConstant;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.DateUtils;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.SettleStatusEnum;
import com.gameplat.common.enums.UserTypes;
import com.gameplat.model.entity.game.GamePlatform;
import com.gameplat.model.entity.game.GameRebateData;
import com.gameplat.model.entity.member.Member;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.Strings;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.SumAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
      if (member == null) {
        throw new ServiceException("用户不存在");
      }
      dto.setUserPaths(member.getSuperPath());
      // 是否代理账号
      if (member.getUserType().equals(UserTypes.AGENT.value())) {
        dto.setAccount(null);
      }
    }
    QueryWrapper<GameRebateData> queryWrapper = Wrappers.query();
    queryWrapper.eq(ObjectUtils.isNotEmpty(dto.getAccount()), "account", dto.getAccount());
    if (ObjectUtils.isNotEmpty(dto.getLiveCodeList())) {
      queryWrapper.in("game_code", dto.getLiveCodeList());
    }
    if (ObjectUtils.isNotEmpty(dto.getGameKindList())) {
      queryWrapper.in("game_kind", dto.getGameKindList());
    }
    queryWrapper.apply(
        ObjectUtils.isNotEmpty(dto.getBetStartDate()),
        "stat_time >= STR_TO_DATE({0}, '%Y-%m-%d')",
        dto.getBetStartDate());
    queryWrapper.apply(
        ObjectUtils.isNotEmpty(dto.getBetEndDate()),
        "stat_time <= STR_TO_DATE({0}, '%Y-%m-%d')",
        dto.getBetEndDate());
    if (ObjectUtils.isNotEmpty(dto.getUserPaths())) {
      queryWrapper.likeRight("user_paths", dto.getUserPaths());
    }
    queryWrapper.orderByDesc("stat_time");
    Page<GameRebateData> result = gameRebateDataMapper.selectPage(page, queryWrapper);

    if (result != null) {
      QueryWrapper<GameRebateData> queryOne = Wrappers.query();
      queryOne.select(
          "sum(bet_amount) as bet_amount,sum(valid_amount) as valid_amount,sum(win_amount) as win_amount");
      queryOne.eq(ObjectUtils.isNotEmpty(dto.getAccount()), "account", dto.getAccount());
      if (ObjectUtils.isNotEmpty(dto.getUserPaths())) {
        queryOne.likeRight("user_paths", dto.getUserPaths());
      }
      if (ObjectUtils.isNotEmpty(dto.getLiveCodeList())) {
        queryOne.in("game_code", dto.getLiveCodeList());
      }
      if (ObjectUtils.isNotEmpty(dto.getGameKindList())) {
        queryOne.in("game_kind", dto.getGameKindList());
      }
      queryOne.apply(
          ObjectUtils.isNotEmpty(dto.getBetStartDate()),
          "stat_time >= STR_TO_DATE({0}, '%Y-%m-%d')",
          dto.getBetStartDate());
      queryOne.apply(
          ObjectUtils.isNotEmpty(dto.getBetEndDate()),
          "stat_time <= STR_TO_DATE({0}, '%Y-%m-%d')",
          dto.getBetEndDate());
      GameRebateData total = gameRebateDataMapper.selectOne(queryOne);
      Map<String, Object> otherData = new HashMap<String, Object>();
      otherData.put("totalData", total);
      pageDtoVO.setPage(result);
      pageDtoVO.setOtherData(otherData);
    }
    return pageDtoVO;
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
    builder.must(QueryBuilders.termQuery("settleTime.keyword",statTime));
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
        SearchRequest searchRequest = new SearchRequest(ContextConstant.ES_INDEX.BET_RECORD_ + tenantConfig.getTenantCode());
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
        searchSourceBuilder.query(builder);

        searchSourceBuilder.aggregation(accountGroup);
        searchSourceBuilder.fetchSource(
                new FetchSourceContext(
                        true, new String[] {"platformCode", "gameKind"}, Strings.EMPTY_ARRAY));
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest,optionsBuilder.build());

          // 生成统计数据  TODO 具体时间问题待确认
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

  public List<GameRebateData> queryBetRecordList(GameRebateDataQueryDTO dto) {
    if (StringUtils.isNotBlank(dto.getSuperAccount())) {
      Member member = memberService.getByAccount(dto.getSuperAccount()).orElse(null);
      if (member == null) {
        throw new ServiceException("用户不存在");
      }
      dto.setUserPaths(member.getSuperPath());
      // 是否代理账号
      if (member.getUserType().equals(UserTypes.AGENT.value())) {
        dto.setAccount(null);
      }
    }
    return gameRebateDataMapper.queryBetRecordList(dto);
  }
}
