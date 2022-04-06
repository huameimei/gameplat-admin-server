package com.gameplat.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.config.TenantConfig;
import com.gameplat.admin.convert.GameFinancialReportConvert;
import com.gameplat.admin.enums.TimeTypeEnum;
import com.gameplat.admin.mapper.GameFinancialReportMapper;
import com.gameplat.admin.model.bean.GameBetRecordSearchBuilder;
import com.gameplat.admin.model.dto.GameBetRecordQueryDTO;
import com.gameplat.admin.model.dto.GameFinancialReportQueryDTO;
import com.gameplat.admin.model.vo.*;
import com.gameplat.admin.service.GameFinancialReportService;
import com.gameplat.admin.util.JxlsExcelUtils;
import com.gameplat.base.common.constant.ContextConstant;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.Converts;
import com.gameplat.base.common.util.DateUtils;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.base.common.util.UUIDUtils;
import com.gameplat.common.enums.GamePlatformEnum;
import com.gameplat.common.enums.GameTypeEnum;
import com.gameplat.common.enums.KgNLLottTypeEnum;
import com.gameplat.common.enums.KgNLTypeKindEnum;
import com.gameplat.common.util.ZipUtils;
import com.gameplat.model.entity.report.GameFinancialReport;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.aggregations.metrics.SumAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author aBen
 * @date 2022/3/6 17:25
 * @desc
 */
@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class GameFinancialReportServiceImpl
        extends ServiceImpl<GameFinancialReportMapper, GameFinancialReport>
        implements GameFinancialReportService {

  @Autowired
  private GameFinancialReportMapper gameFinancialReportMapper;

  @Autowired
  private TenantConfig tenantConfig;

  @Autowired
  private GameFinancialReportConvert gameFinancialReportConvert;

  @Resource
  private RestHighLevelClient restHighLevelClient;

  /**
   * 财务报表模板
   */
  private static final String GAME_FINANCIAL_REPORT = "gameFinancialReport.xlsx";

  @Override
  public List<GameFinancialReportVO> findGameFinancialReportList(GameFinancialReportQueryDTO dto) {
    List<GameFinancialReportVO> gameFinancialReportVOList =
            gameFinancialReportMapper.findGameFinancialReportList(dto);
    assembleKgNewLottery(gameFinancialReportVOList);
    return gameFinancialReportVOList;
  }

  @Override
  public PageDtoVO<GameFinancialReportVO> findReportPage(
          Page<GameFinancialReport> page, GameFinancialReportQueryDTO queryDTO) {
    PageDtoVO<GameFinancialReportVO> pageDtoVO = new PageDtoVO<>();
    Page<GameFinancialReportVO> result =
            gameFinancialReportMapper.findGameFinancialReportPage(page, queryDTO);

    // 对KG新彩票的三个彩种做特殊处理
    assembleKgNewLottery(result.getRecords());

    // 查询总计
    QueryWrapper<GameFinancialReport> queryOne = Wrappers.query();
    queryOne.select(
            "ifNull(sum(valid_amount), 0) as valid_amount,ifNull(sum(win_amount), 0) as win_amount,ifNull(sum(accumulate_win_amount),0) as accumulateWinAmount");
    fillQueryWrapper(queryDTO, queryOne);
    GameFinancialReport gameFinancialReport = gameFinancialReportMapper.selectOne(queryOne);
    TotalGameFinancialReportVO totalGameFinancialReport =
            gameFinancialReportConvert.toTotalVO(gameFinancialReport);

    BigDecimal totalLastWinAmount = gameFinancialReportMapper.findTotalLastWinAmount(queryDTO);
    totalGameFinancialReport.setLastWinAmount(totalLastWinAmount);
    Map<String, Object> otherData = new HashMap<>();
    otherData.put("totalData", totalGameFinancialReport);
    pageDtoVO.setPage(result);
    pageDtoVO.setOtherData(otherData);
    return pageDtoVO;
  }

  @Override
  public void initGameFinancialReport(String statisticsTime) {
    HashMap<String, Object> map = new HashMap<>();
    map.put("statistics_time", statisticsTime);
    // 先删除统计月份的所有数据
    this.removeByMap(map);

    List<GameFinancialReport> allGameFinancialReportList = statisticsGameReportList(statisticsTime);

    // 入库
    if (CollectionUtil.isNotEmpty(allGameFinancialReportList)) {
      this.saveBatch(allGameFinancialReportList);
    }
  }

  @Override
  public void exportGameFinancialReport(String statisticsTime, HttpServletResponse response) {
    // 根据年月查询需要导出报表数据
    GameFinancialReportQueryDTO queryDTO = new GameFinancialReportQueryDTO();
    List<GameFinancialReportVO> reportList =
            gameFinancialReportMapper.findGameFinancialReportList(queryDTO);

    if (StringUtils.isEmpty(reportList)) {
      throw new ServiceException("请先初始化" + statisticsTime + "的游戏财务报表");
    }

    // 对KG新彩票的三个彩种做特殊处理
    assembleKgNewLottery(reportList);

    // 根据游戏类型编号对List进行分组
    Map<String, List<GameFinancialReportVO>> listMap =
            reportList.stream().collect(Collectors.groupingBy(GameFinancialReportVO::getGameTypeName));
    List<GameReportExportVO> gameReportExportVOList = new ArrayList<>();
    // 组装模板渲染对象
    for (String key : listMap.keySet()) {
      GameReportExportVO gameReportExportVO = new GameReportExportVO();
      gameReportExportVO.setGameTypeId(listMap.get(key).get(0).getGameTypeId());
      gameReportExportVO.setGameTypeName(key);
      gameReportExportVO.setGameFinancialReportList(listMap.get(key));
      gameReportExportVOList.add(gameReportExportVO);
    }
    List<GameReportExportVO> finalExportList =
            gameReportExportVOList.stream()
                    .sorted(Comparator.comparing(GameReportExportVO::getGameTypeId))
                    .collect(Collectors.toList());
    Map<String, Object> map = new HashMap<>();
    map.put("statisticsTime", statisticsTime);
    map.put("gameList", finalExportList);
    final BigDecimal[] bigDecimal = {new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0)};
    reportList.forEach(
            a -> {
              bigDecimal[0] = bigDecimal[0].add(a.getValidAmount());
              bigDecimal[1] = bigDecimal[1].add(a.getWinAmount());
              bigDecimal[2] = bigDecimal[2].add(a.getLastWinAmount());
              bigDecimal[3] = bigDecimal[3].add(a.getAccumulateWinAmount());
            });
    map.put("totalValidAmount", bigDecimal[0]);
    map.put("totalWinAmount", bigDecimal[1]);
    map.put("totalLastWinAmount", bigDecimal[2]);
    map.put("totalAccumulateWinAmount", bigDecimal[3]);
    // 将数据渲染到excel模板上
    // 定义ZIP包的包名
    String zipFileName = statisticsTime + "财务报表导出";

    try {
      response.setHeader(
              "Content-Disposition",
              "attachment;fileName=" + URLEncoder.encode(zipFileName + ".zip", "UTF-8"));
      response.setContentType("application/zip");

      final File dir =
              new File(
                      System.getProperty("java.io.tmpdir")
                              + File.separator
                              + "excel-"
                              + UUIDUtils.getUUID32());
      if (!dir.exists()) {
        dir.mkdirs();
      }

      // 单个Excel文件的fileName
      String fileName =
              new StringBuilder()
                      .append("(")
                      .append("财务报表")
                      .append(")")
                      .append("-")
                      .append(statisticsTime)
                      .append(".xlsx")
                      .toString();
      FileOutputStream fo = null;
      try {
        fo = new FileOutputStream(new File(dir + File.separator + fileName));
      } catch (FileNotFoundException e1) {
        e1.printStackTrace();
      }
      try {
        JxlsExcelUtils.downLoadExcel(map, GAME_FINANCIAL_REPORT, fo);
      } catch (InvalidFormatException | IOException e1) {
        e1.printStackTrace();
      } finally {
        if (fo != null) {
          try {
            fo.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }

      OutputStream out = response.getOutputStream();
      ZipUtils.zipDir(out, dir);
      ZipUtils.del(dir);
      out.flush();
    } catch (IOException e) {
      log.error("财务报表导出IO错误", e);
    }
  }

  /**
   * 统计三方游戏的财务报表数据
   *
   * @param statisticsTime
   * @return
   */
  public List<GameFinancialReport> statisticsGameReportList(String statisticsTime) {
    String tenant = tenantConfig.getTenantCode();
    // 统计开始时间
    String startTime = statisticsTime + "-01 00:00:00";
    // 当前统计月份的最后一天的日期
    Date endDate = DateUtil.endOfMonth(DateUtils.parseDate(startTime, "yyyy-MM-dd"));
    // 统计结束时间
    String endTime = DateUtils.formatDate(endDate, "yyyy-MM-dd HH:mm:ss");

    List<GameFinancialReport> allGameFinancialReportList = new ArrayList<>();

    // 初始化全部游戏的财务报表（无游戏数据）
    List<GameFinancialReport> gameFinancialReportList = gameFinancialReportMapper.initGameFinancialReport(statisticsTime, startTime, endTime, tenant);
    if (CollectionUtil.isEmpty(gameFinancialReportList)) {
      throw new ServiceException("游戏已全部下架");
    }

    // 获取游戏的有效投注额和输赢金额
    Map<String, List<GameDataVO>> gameDataMap = initGameData(startTime, endTime, tenant);

    // 组装数据
    if (StringUtils.isNotNull(gameDataMap)) {
      gameFinancialReportList.parallelStream().forEach(gameFinancialReport -> {
        List<GameDataVO> gameDataVO = gameDataMap.get(gameFinancialReport.getGameKind());
        // 如果有对应的游戏数据则填充，没有则是0
        if (StringUtils.isNotEmpty(gameDataVO)) {
          gameFinancialReport.setValidAmount(gameDataVO.get(0).getValidAmount());
          gameFinancialReport.setWinAmount(gameDataVO.get(0).getWinAmount());
        }
      });
    }

    // 初始化KG新彩票的游戏数据（按彩种划分）
    List<GameFinancialReport> KgNlReportList = iniKgNlData(statisticsTime, startTime, endTime, tenant);
    if (StringUtils.isNotEmpty(KgNlReportList)) {
      allGameFinancialReportList.addAll(KgNlReportList);
    }

    allGameFinancialReportList.addAll(gameFinancialReportList);
    return allGameFinancialReportList;
  }

  public Map<String, List<GameDataVO>> initGameData(String startTime, String endTime, String tenant) {
    long startTimestamp = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm:ss").getTime();
    long endTimestamp = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm:ss").getTime();
    GameBetRecordQueryDTO dto = new GameBetRecordQueryDTO();
    dto.setTimeType(TimeTypeEnum.STAT_TIME.getValue());
    dto.setBeginTime(Long.toString(startTimestamp));
    dto.setEndTime(Long.toString(endTimestamp));
    QueryBuilder builder = GameBetRecordSearchBuilder.buildBetRecordSearch(dto);

    String indexName = ContextConstant.ES_INDEX.BET_RECORD_ + tenant;
    SearchRequest searchRequest = new SearchRequest(indexName);

    TermsAggregationBuilder groupTerms =
            AggregationBuilders.terms("gameKindGroup").field("gameKind.keyword");

    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    SumAggregationBuilder validAmountSumBuilder = AggregationBuilders.sum("validAmount").field("validAmount");
    SumAggregationBuilder winAmountSumBuilder = AggregationBuilders.sum("winAmount").field("winAmount");

    groupTerms.subAggregation(validAmountSumBuilder);
    groupTerms.subAggregation(winAmountSumBuilder);

    searchSourceBuilder.size(0);
    searchSourceBuilder.aggregation(groupTerms);
    searchSourceBuilder.query(builder);

    searchRequest.source(searchSourceBuilder);

    RequestOptions.Builder optionsBuilder = RequestOptions.DEFAULT.toBuilder();
    optionsBuilder.setHttpAsyncResponseConsumerFactory(
            new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(31457280));

    SearchResponse searchResponse = null;
    try {
      searchResponse = restHighLevelClient.search(searchRequest, optionsBuilder.build());
    } catch (IOException e) {
      e.printStackTrace();
    }

    Terms terms = searchResponse.getAggregations().get("gameKindGroup");
    if (StringUtils.isNull(terms)) {
      return null;
    }
    List<GameDataVO> gameDataList = new ArrayList<>();
    for (Terms.Bucket bucket : terms.getBuckets()) {
      Object gameKind = bucket.getKey();
      GameDataVO gameDataVO = new GameDataVO();
      gameDataVO.setGameKind(gameKind.toString());
      BigDecimal validAmount = Converts.toBigDecimal(((ParsedSum) bucket.getAggregations().get("validAmount")).getValue())
              .divide(new BigDecimal("1000"), 2, BigDecimal.ROUND_DOWN);
      BigDecimal winAmount = Converts.toBigDecimal(((ParsedSum) bucket.getAggregations().get("winAmount")).getValue()).abs()
              .divide(new BigDecimal("1000"), 2, BigDecimal.ROUND_DOWN);

      gameDataVO.setValidAmount(validAmount);
      gameDataVO.setWinAmount(winAmount);
      gameDataList.add(gameDataVO);
    }

    return gameDataList.stream().collect(Collectors.groupingBy(GameDataVO::getGameKind));
  }

  public List<GameFinancialReport> iniKgNlData(String statisticsTime, String startTime, String endTime, String tenant) {
    long startTimestamp = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm:ss").getTime();
    long endTimestamp = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm:ss").getTime();
    GameBetRecordQueryDTO dto = new GameBetRecordQueryDTO();
    dto.setTimeType(TimeTypeEnum.STAT_TIME.getValue());
    dto.setBeginTime(Long.toString(startTimestamp));
    dto.setEndTime(Long.toString(endTimestamp));
    dto.setGameKind("KGNL_LOTTERY");
    QueryBuilder builder = GameBetRecordSearchBuilder.buildBetRecordSearch(dto);

    String indexName = ContextConstant.ES_INDEX.BET_RECORD_ + tenant;
    SearchRequest searchRequest = new SearchRequest(indexName);

    TermsAggregationBuilder groupTerms =
            AggregationBuilders.terms("lottTypeGroup").field("lottType");

    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    SumAggregationBuilder validAmountSumBuilder = AggregationBuilders.sum("validAmount").field("validAmount");
    SumAggregationBuilder winAmountSumBuilder = AggregationBuilders.sum("winAmount").field("winAmount");

    groupTerms.subAggregation(validAmountSumBuilder);
    groupTerms.subAggregation(winAmountSumBuilder);

    searchSourceBuilder.size(0);
    searchSourceBuilder.aggregation(groupTerms);
    searchSourceBuilder.query(builder);

    searchRequest.source(searchSourceBuilder);

    RequestOptions.Builder optionsBuilder = RequestOptions.DEFAULT.toBuilder();
    optionsBuilder.setHttpAsyncResponseConsumerFactory(
            new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(31457280));

    SearchResponse searchResponse = null;
    try {
      searchResponse = restHighLevelClient.search(searchRequest, optionsBuilder.build());
    } catch (IOException e) {
      e.printStackTrace();
    }

    Terms terms = searchResponse.getAggregations().get("lottTypeGroup");
    if (StringUtils.isNull(terms)) {
      return null;
    }
    List<GameFinancialReport> KgNlReportList = new ArrayList<>();
    for (Terms.Bucket bucket : terms.getBuckets()) {
      Object lottType = bucket.getKey();
      GameFinancialReport gameFinancialReport = new GameFinancialReport();
      // 官彩
      if (lottType.toString().equals(KgNLLottTypeEnum.OFFICIAL.value())) {
        gameFinancialReport.setGameKind(KgNLTypeKindEnum.OFFICIAL.value());
        // 私彩
      } else if (lottType.toString().equals(KgNLLottTypeEnum.SELF.value())) {
        gameFinancialReport.setGameKind(KgNLTypeKindEnum.SELF.value());
        // 六合彩
      } else if (lottType.toString().equals(KgNLLottTypeEnum.LHC.value())) {
        gameFinancialReport.setGameKind(KgNLTypeKindEnum.LHC.value());
      }
      BigDecimal validAmount = Converts.toBigDecimal(((ParsedSum) bucket.getAggregations().get("validAmount")).getValue())
              .divide(new BigDecimal("1000"), 2, BigDecimal.ROUND_DOWN);
      BigDecimal winAmount = Converts.toBigDecimal(((ParsedSum) bucket.getAggregations().get("winAmount")).getValue()).abs()
              .divide(new BigDecimal("1000"), 2, BigDecimal.ROUND_DOWN);
      gameFinancialReport.setValidAmount(validAmount);
      gameFinancialReport.setWinAmount(winAmount);
      gameFinancialReport.setStatisticsTime(statisticsTime);
      gameFinancialReport.setStartTime(startTime);
      gameFinancialReport.setEndTime(endTime);
      gameFinancialReport.setCustomerCode(tenant);
      gameFinancialReport.setPlatformCode(GamePlatformEnum.KGNL.getCode());
      gameFinancialReport.setGameType(GameTypeEnum.LOTTERY.code());
      KgNlReportList.add(gameFinancialReport);
    }

    return KgNlReportList;
  }

  /**
   * 对KG新彩票的三个彩种做特殊处理
   *
   * @param list
   */
  public void assembleKgNewLottery(List<GameFinancialReportVO> list) {
    if (StringUtils.isNotEmpty(list)) {
      for (GameFinancialReportVO vo : list) {
        if (vo.getGameKind().equals(KgNLTypeKindEnum.OFFICIAL.value())) {
          vo.setGameTypeId(GameTypeEnum.LOTTERY.id());
          vo.setGameTypeName(GameTypeEnum.LOTTERY.desc());
          vo.setGameName(KgNLTypeKindEnum.OFFICIAL.desc());
        }
        if (vo.getGameKind().equals(KgNLTypeKindEnum.SELF.value())) {
          vo.setGameTypeId(GameTypeEnum.LOTTERY.id());
          vo.setGameTypeName(GameTypeEnum.LOTTERY.desc());
          vo.setGameName(KgNLTypeKindEnum.SELF.desc());
        }
        if (vo.getGameKind().equals(KgNLTypeKindEnum.LHC.value())) {
          vo.setGameTypeId(GameTypeEnum.LOTTERY.id());
          vo.setGameTypeName(GameTypeEnum.LOTTERY.desc());
          vo.setGameName(KgNLTypeKindEnum.LHC.desc());
        }
      }
    }
  }

  private void fillQueryWrapper(
          GameFinancialReportQueryDTO queryDTO, QueryWrapper<GameFinancialReport> queryWrapper) {
    queryWrapper.eq(
            ObjectUtils.isNotEmpty(queryDTO.getStatisticsTime()),
            "statistics_time",
            queryDTO.getStatisticsTime());
    queryWrapper.eq(
            ObjectUtils.isNotEmpty(queryDTO.getGameType()), "game_type", queryDTO.getGameType());
    queryWrapper.eq(
            ObjectUtils.isNotEmpty(queryDTO.getPlatformCode()),
            "platform_code",
            queryDTO.getPlatformCode());
  }
}
