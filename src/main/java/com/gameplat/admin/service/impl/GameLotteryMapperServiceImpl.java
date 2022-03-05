package com.gameplat.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.config.TenantConfig;
import com.gameplat.admin.convert.GameLotteryConvert;
import com.gameplat.admin.mapper.GameLotteryMapper;
import com.gameplat.admin.model.doc.GameBetRecord;
import com.gameplat.admin.model.domain.GameLottery;
import com.gameplat.admin.model.dto.GameKGLotteryDto;
import com.gameplat.admin.model.vo.GameKGLotteryVo;
import com.gameplat.admin.model.vo.GameLotteryVo;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GameLotteryMapperService;
import com.gameplat.base.common.constant.ContextConstant;
import com.gameplat.elasticsearch.page.PageResponse;
import com.gameplat.elasticsearch.service.IBaseElasticsearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.aggregations.metrics.SumAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
@Log4j2
public class GameLotteryMapperServiceImpl
    extends ServiceImpl<GameLotteryMapper, GameLottery>
    implements GameLotteryMapperService {

    @Autowired(required = false)
    private GameLotteryConvert gameLotteryConvert;

    @Resource
    private IBaseElasticsearchService baseElasticsearchService;

    @Resource
    private TenantConfig tenantConfig;

    @Resource
    private RestHighLevelClient restHighLevelClient;

  @Override
  public List<GameLotteryVo> findGameLotteryType(int code) {
     return this.lambdaQuery().eq(GameLottery::getLotteryType,code)
              .list().stream().map(gameLotteryConvert::toVo).collect(Collectors.toList());
  }


  public PageDtoVO<GameKGLotteryVo> page(Page<GameKGLotteryVo> page, GameKGLotteryDto dto) {
      log.info("彩票日报表入参：{}", JSON.toJSONString(dto));
      String indexName = ContextConstant.ES_INDEX.BET_RECORD_ + tenantConfig.getTenantCode();
      SortBuilder<FieldSortBuilder> sortBuilder = SortBuilders.fieldSort("betTime.keyword").order(SortOrder.DESC);
      QueryBuilder queryBuilder = GameKGLotteryDto.buildBetRecordSearch(dto);


      PageResponse<GameKGLotteryVo> result = baseElasticsearchService.search(queryBuilder, indexName, GameKGLotteryVo.class,
              (int) page.getCurrent() - 1, (int) page.getSize(), sortBuilder);
        return new PageDtoVO<>();



/*


      BoolQueryBuilder builder = GameKGLotteryDto.buildBetRecordSearch(dto);

      SearchRequest searchRequest = new SearchRequest(indexName);
      SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
      TermsAggregationBuilder accountGroup = AggregationBuilders.terms("gameCodeGroup").field("gameCode.keyword");


      SumAggregationBuilder sumBetAmount = AggregationBuilders.sum("betAmount").field("betAmount");
      SumAggregationBuilder sumValidAmount = AggregationBuilders.sum("validAmount").field("validAmount");
      SumAggregationBuilder sumWinAmount = AggregationBuilders.sum("winAmount").field("winAmount");
      SumAggregationBuilder waterAmount = AggregationBuilders.sum("waterAmount").field("waterAmount");
      CardinalityAggregationBuilder count = AggregationBuilders.cardinality("distinct_accout").field("account");
      searchSourceBuilder.aggregation(sumBetAmount).aggregation(sumValidAmount).aggregation(sumWinAmount).aggregation(waterAmount).aggregation(count);

      searchSourceBuilder.size(0);
      searchSourceBuilder.query(builder);
      searchSourceBuilder.aggregation(accountGroup);
      searchRequest.source(searchSourceBuilder);

      try {
          RequestOptions.Builder optionsBuilder = RequestOptions.DEFAULT.toBuilder();
          optionsBuilder.setHttpAsyncResponseConsumerFactory(
                  new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(31457280));
          SearchResponse search = restHighLevelClient.search(searchRequest, optionsBuilder.build());
          Terms terms = search.getAggregations().get("gameCodeGroup");

          for (Terms.Bucket bucket : terms.getBuckets()) {
              long countOrder = bucket.getDocCount();
              double betAmount = ((ParsedSum) bucket.getAggregations().get("betAmount")).getValue();
              double betnum = ((ParsedSum) bucket.getAggregations().get("account")).getValue();
              double validAmount = ((ParsedSum) bucket.getAggregations().get("validAmount")).getValue();
              double winAmount = ((ParsedSum) bucket.getAggregations().get("winAmount")).getValue();
              //返水
              double sumWaterAmount = ((ParsedSum) bucket.getAggregations().get("waterAmount")).getValue();
              //投注人数
              double accountNum = ((ParsedSum) bucket.getAggregations().get("distinct_accout")).getValue();

          }
      } catch (IOException e) {
          e.printStackTrace();
      }


*/

  }
}
