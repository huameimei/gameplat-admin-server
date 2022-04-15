package com.gameplat.admin.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.config.TenantConfig;
import com.gameplat.admin.convert.GameBetRecordConvert;
import com.gameplat.admin.model.bean.ActivityStatisticItem;
import com.gameplat.admin.model.bean.GameBetRecordSearchBuilder;
import com.gameplat.admin.model.dto.GameBetRecordQueryDTO;
import com.gameplat.admin.model.vo.GameBetRecordVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GameBetRecordInfoService;
import com.gameplat.admin.service.GameConfigService;
import com.gameplat.base.common.constant.ContextConstant;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.enums.GamePlatformEnum;
import com.gameplat.common.enums.TransferTypesEnum;
import com.gameplat.common.game.GameBizBean;
import com.gameplat.common.game.GameResult;
import com.gameplat.common.game.api.GameApi;
import com.gameplat.common.util.MathUtils;
import com.gameplat.elasticsearch.page.PageResponse;
import com.gameplat.elasticsearch.service.IBaseElasticsearchService;
import com.gameplat.model.entity.game.GameBetRecord;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.aggregations.metrics.SumAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class GameBetRecordInfoServiceImpl implements GameBetRecordInfoService {

  @Autowired private ApplicationContext applicationContext;

  @Autowired private IBaseElasticsearchService baseElasticsearchService;

  @Autowired private GameBetRecordConvert gameBetRecordConvert;

  @Autowired private GameConfigService gameConfigService;

  @Autowired private TenantConfig tenantConfig;

  @Resource private RestHighLevelClient restHighLevelClient;

  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public GameApi getGameApi(String platformCode) {
    GameApi api =
        applicationContext.getBean(platformCode.toLowerCase() + GameApi.SUFFIX, GameApi.class);
    TransferTypesEnum tt = TransferTypesEnum.get(platformCode);
    // 1代表是否额度转换
    if (tt == null || tt.getType() != 1) {
      throw new ServiceException("游戏未接入");
    }
    return api;
  }

  @Override
  public PageDtoVO<GameBetRecordVO> queryPageBetRecord(
      Page<GameBetRecordVO> page, GameBetRecordQueryDTO dto) {
    log.info("请求下注记录分页数据参数：{}", JSONUtil.toJsonStr(dto));
    QueryBuilder builder = GameBetRecordSearchBuilder.buildBetRecordSearch(dto);
    SortBuilder<FieldSortBuilder> sortBuilder =
        SortBuilders.fieldSort(GameBetRecordSearchBuilder.convertTimeType(dto.getTimeType()))
            .order(SortOrder.DESC);
    String indexName = ContextConstant.ES_INDEX.BET_RECORD_ + tenantConfig.getTenantCode();
    // 调用封装的分页 页码不用减1
    PageResponse<GameBetRecord> result =
        baseElasticsearchService.search(
            builder,
            indexName,
            GameBetRecord.class,
            (int) page.getCurrent(),
            (int) page.getSize(),
            sortBuilder);

    List<GameBetRecordVO> betRecordList = new ArrayList<>();
    log.info("分页查询返回数据,总数{}", result.getTotal());
    if (CollectionUtils.isNotEmpty(result.getList())) {
      result.getList().forEach(o -> betRecordList.add(gameBetRecordConvert.toVo(o)));
    }
    Page<GameBetRecordVO> resultPage = new Page<>();
    resultPage.setRecords(betRecordList);
    resultPage.setTotal(result.getTotal());
    resultPage.setCurrent(result.getPageNum());
    resultPage.setSize(result.getPageSize());

    Map<String, Object> otherData = new HashMap<>(8);
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    SumAggregationBuilder betAmountSumBuilder =
        AggregationBuilders.sum("betAmountSum").field("betAmount");
    SumAggregationBuilder validAmountSumBuilder =
        AggregationBuilders.sum("validAmountSum").field("validAmount");
    SumAggregationBuilder winAmountSumBuilder =
        AggregationBuilders.sum("winAmountSum").field("winAmount");
    searchSourceBuilder
        .aggregation(betAmountSumBuilder)
        .aggregation(validAmountSumBuilder)
        .aggregation(winAmountSumBuilder);
    searchSourceBuilder.query(builder);
    searchSourceBuilder.sort(sortBuilder);

    SearchRequest searchRequest = new SearchRequest(indexName);
    searchRequest.source(searchSourceBuilder);
    log.info("queryPageBetRecord DSL语句为：{}", searchRequest.source().toString());
    RequestOptions.Builder optionsBuilder = RequestOptions.DEFAULT.toBuilder();
    optionsBuilder.setHttpAsyncResponseConsumerFactory(
        new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(31457280));
    try {
      SearchResponse searchResponse =
          restHighLevelClient.search(searchRequest, optionsBuilder.build());
      Map<String, Aggregation> aggregationMap = searchResponse.getAggregations().getAsMap();
      BigDecimal betAmount =
          MathUtils.divide1000(((ParsedSum) aggregationMap.get("betAmountSum")).getValue());
      BigDecimal validAmount =
          MathUtils.divide1000(((ParsedSum) aggregationMap.get("validAmountSum")).getValue());
      BigDecimal winAmount =
          MathUtils.divide1000(((ParsedSum) aggregationMap.get("winAmountSum")).getValue());
      otherData.put("betAmount", betAmount);
      otherData.put("validAmount", validAmount);
      otherData.put("winAmount", winAmount);
    } catch (IOException e) {
      e.printStackTrace();
    }

    PageDtoVO<GameBetRecordVO> pageDtoVO = new PageDtoVO<>();
    pageDtoVO.setPage(resultPage);
    pageDtoVO.setOtherData(otherData);
    return pageDtoVO;
  }

  @Override
  public GameResult getGameResult(GameBetRecordQueryDTO dto) throws Exception {
    GameApi gameApi = getGameApi(dto.getPlatformCode());
    GameBizBean gameBizBean = new GameBizBean();
    gameBizBean.setOrderNo(dto.getBillNo());
    gameBizBean.setGameType(dto.getGameType());
    gameBizBean.setPlatformCode(dto.getPlatformCode());
    gameBizBean.setGameAccount(dto.getGameAccount());
    gameBizBean.setPlayCode(dto.getPlayCode());
    gameBizBean.setSettle(dto.getSettle());
    gameBizBean.setTime(dto.getTime());
    gameBizBean.setConfig(gameConfigService.queryGameConfigInfoByPlatCode(dto.getPlatformCode()));
    GameResult gameResult = gameApi.getGameResult(gameBizBean);
    if (StringUtils.isBlank(gameResult.getData())) {
      throw new ServiceException(GamePlatformEnum.getName(dto.getPlatformCode()) + "暂不支持查看游戏结果");
    }
    return gameResult;
  }

  @Override
  public List<ActivityStatisticItem> xjAssignMatchDml(Map map) {
    return new ArrayList<>();
  }
}
