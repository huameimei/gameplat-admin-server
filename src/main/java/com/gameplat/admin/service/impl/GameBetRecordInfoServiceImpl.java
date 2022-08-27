package com.gameplat.admin.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.config.SysTheme;
import com.gameplat.admin.convert.GameBetRecordConvert;
import com.gameplat.admin.model.bean.ActivityStatisticItem;
import com.gameplat.admin.model.bean.GameBetRecordSearchBuilder;
import com.gameplat.admin.model.dto.GameBetRecordQueryDTO;
import com.gameplat.admin.model.dto.UserGameBetRecordDto;
import com.gameplat.admin.model.vo.GameBetRecordExportVO;
import com.gameplat.admin.model.vo.GameBetRecordVO;
import com.gameplat.admin.model.vo.GameReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GameBetRecordInfoService;
import com.gameplat.admin.service.GameConfigService;
import com.gameplat.admin.service.GameKindService;
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
import com.gameplat.model.entity.game.GameKind;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
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
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
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

  @Autowired private SysTheme sysTheme;

  @Resource private RestHighLevelClient restHighLevelClient;

  @Autowired private GameKindService gameKindService;

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
    String indexName = ContextConstant.ES_INDEX.BET_RECORD_ + sysTheme.getTenantCode();
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
    } catch (Exception e) {
      log.error("统计es游戏投注总计报错:", e);
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

  @Override
  public void exportReport(GameBetRecordQueryDTO dto, HttpServletResponse response) {
    log.info("请求导出下注记录数据参数：{}", JSONUtil.toJsonStr(dto));
    QueryBuilder builder = GameBetRecordSearchBuilder.buildBetRecordSearch(dto);
    String indexName = ContextConstant.ES_INDEX.BET_RECORD_ + sysTheme.getTenantCode();
    try {
      // 统计条数
      CountRequest countRequest = new CountRequest(indexName);
      countRequest.query(builder);
      RequestOptions.Builder optionsBuilder = RequestOptions.DEFAULT.toBuilder();
      optionsBuilder.setHttpAsyncResponseConsumerFactory(
          new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(31457280));
      CountResponse countResponse = restHighLevelClient.count(countRequest, optionsBuilder.build());
      long sumCount = countResponse.getCount();
      log.info("导出下注数据总条数为：{}", sumCount);
      if (sumCount > 0) {
        SortBuilder<FieldSortBuilder> sortBuilder =
            SortBuilders.fieldSort(GameBetRecordSearchBuilder.convertTimeType(dto.getTimeType()))
                .order(SortOrder.DESC);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(builder);
        searchSourceBuilder.sort(sortBuilder);
        searchSourceBuilder.size((int) sumCount);
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(searchSourceBuilder);
        log.info("exportReport DSL语句为：{}", searchRequest.source().toString());
        List<GameBetRecord> resultList =
            baseElasticsearchService.searchDocList(
                indexName, searchSourceBuilder, GameBetRecord.class);
        Map<String, String> gameKindMap =
            gameKindService.list().stream()
                .collect(Collectors.toMap(GameKind::getCode, GameKind::getName));

        List<GameBetRecordExportVO> list =
            resultList.stream()
                .map(gameBetRecordConvert::toExportVo)
                .peek(
                    item -> {
                      item.setGameName(geti18nText(item.getGameName()));
                      item.setGameKind(gameKindMap.get(item.getGameKind()));
                      if (StringUtils.isNotBlank(item.getBetTime())) {
                        item.setBetTime(DateUtil.date(Long.parseLong(item.getBetTime())).toString(DatePattern.NORM_DATETIME_FORMAT));
                      }
                      if (StringUtils.isNotBlank(item.getSettleTime())) {
                        item.setSettleTime(DateUtil.date(Long.parseLong(item.getSettleTime())).toString(DatePattern.NORM_DATETIME_FORMAT));
                      }
                      if (StringUtils.isNotBlank(item.getStatTime())) {
                        item.setStatTime(DateUtil.date(Long.parseLong(item.getStatTime())).toString(DatePattern.NORM_DATETIME_FORMAT));
                      }
                    })
                .collect(Collectors.toList());
        ExportParams exportParams = new ExportParams("游戏投注记录", "游戏投注记录");
        response.setHeader(
            HttpHeaders.CONTENT_DISPOSITION, "attachment;filename = gameBetRecord.xls");
        Workbook workbook =
            ExcelExportUtil.exportExcel(exportParams, GameBetRecordExportVO.class, list);
        workbook.write(response.getOutputStream());
      }
    } catch (IOException e) {
      log.error(e.getMessage());
      throw new ServiceException("导出游戏投注记录失败");
    }
  }

  private static final Integer startIndex = 1;

  private static final Integer endIndex = 10000;

  @Override
  public void exportUserGameBetRecord(GameBetRecordQueryDTO dto, HttpServletResponse response) throws IOException {

    log.info("请求会员游戏报表参数：{}", dto);
    QueryBuilder builder = GameBetRecordSearchBuilder.buildBetRecordSearch(dto);
    String indexName = ContextConstant.ES_INDEX.BET_RECORD_ + sysTheme.getTenantCode();
    // 统计条数
    CountRequest countRequest = new CountRequest(indexName);
    try {
      countRequest.query(builder);
      RequestOptions.Builder optionsBuilder = RequestOptions.DEFAULT.toBuilder();
      optionsBuilder.setHttpAsyncResponseConsumerFactory(
        new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(31457280));
      CountResponse countResponse = restHighLevelClient.count(countRequest, optionsBuilder.build());
      long sumCount = countResponse.getCount();
      log.info("导出会员游戏报表总条数为: {}", sumCount);
      if (sumCount > 0) {
        SortBuilder<FieldSortBuilder> sortBuilder =
          SortBuilders.fieldSort(GameBetRecordSearchBuilder.convertTimeType(dto.getTimeType()))
            .order(SortOrder.DESC);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(builder);
        searchSourceBuilder.sort(sortBuilder);
        searchSourceBuilder.size((int) sumCount);
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(searchSourceBuilder);
        log.info("会员游戏报表 DSL语句为：{}", searchRequest.source().toString());
        List<GameBetRecord> resultList =
          baseElasticsearchService.searchDocList(
            indexName, searchSourceBuilder, GameBetRecord.class);
        Map<String, String> gameKindMap =
          gameKindService.list().stream()
            .collect(Collectors.toMap(GameKind::getCode, GameKind::getName));

        List<GameBetRecordExportVO> list =
          resultList.stream()
            .map(gameBetRecordConvert::toExportVo)
            .peek(
              item -> {
                item.setGameName(geti18nText(item.getGameName()));
                item.setGameKind(gameKindMap.get(item.getGameKind()));
                item.setBetTime(DateUtil.date(Long.parseLong(item.getBetTime())).toString(DatePattern.NORM_DATETIME_FORMAT));
                if (!Objects.isNull(item.getSettleTime())) {
                  item.setSettleTime(DateUtil.date(Long.parseLong(item.getSettleTime())).toString(DatePattern.NORM_DATETIME_FORMAT));
                  item.setStatTime(DateUtil.date(Long.parseLong(item.getStatTime())).toString(DatePattern.NORM_DATETIME_FORMAT));
                }
              })
            .collect(Collectors.toList());
        String title = String.format("%s至%s会员游戏报表", dto.getBeginTime(), dto.getEndTime());
        ExportParams exportParams = new ExportParams(title, "会员游戏报表");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename = userGameBetRecord.xls");
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, GameBetRecordExportVO.class, list);
        workbook.write(response.getOutputStream());
      }
    } catch (IOException e) {
      log.error("导出会员游戏报表报错", e);
      throw new ServiceException("导出会员游戏报表失败");
    }

  }

  private String geti18nText(String value) {
    Locale locale = LocaleContextHolder.getLocale();
    JSONObject jsonObject = JSONUtil.parseObj(value);
    String str = jsonObject.getStr(locale.toLanguageTag());
    // 如果浏览器语言不在5种语言内，则默认返回中文
    if (com.gameplat.base.common.util.StringUtils.isEmpty(str)) {
      str = jsonObject.getStr("zh-CN");
    }
    return str;
  }
}
