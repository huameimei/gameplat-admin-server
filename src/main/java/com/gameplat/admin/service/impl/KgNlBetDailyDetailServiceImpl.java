package com.gameplat.admin.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.config.SysTheme;
import com.gameplat.admin.mapper.KgNlBetDailyDetailMapper;
import com.gameplat.admin.model.dto.KgNlWinReportQueryDTO;
import com.gameplat.admin.model.vo.KgNlBetDailyDetailVO;
import com.gameplat.admin.model.vo.KgNlWinReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.KgNlBetDailyDetailService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.admin.util.ListUtils;
import com.gameplat.base.common.constant.ContextConstant;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.enums.GameKindEnum;
import com.gameplat.common.enums.GamePlatformEnum;
import com.gameplat.common.enums.GameTypeEnum;
import com.gameplat.common.enums.SettleStatusEnum;
import com.gameplat.model.entity.game.KgNlBetDailyDetail;
import com.gameplat.model.entity.member.Member;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.aggregations.metrics.SumAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class KgNlBetDailyDetailServiceImpl extends ServiceImpl<KgNlBetDailyDetailMapper, KgNlBetDailyDetail>
  implements KgNlBetDailyDetailService {

  @Autowired
  private SysTheme sysTheme;

  @Autowired
  private KgNlBetDailyDetailMapper kgNlBetDailyDetailMapper;

  @Autowired
  private MemberService memberService;

  @Autowired
  private RestHighLevelClient restHighLevelClient;

  public List<KgNlWinReportVO> findWinReportData(KgNlWinReportQueryDTO dto) {
    return kgNlBetDailyDetailMapper.findWinReportData(dto);
  }

  public PageDtoVO<KgNlBetDailyDetailVO> findDetailPage(Page<KgNlBetDailyDetailVO> page, KgNlWinReportQueryDTO dto) {
    PageDtoVO<KgNlBetDailyDetailVO> pageDtoVO = new PageDtoVO<>();
    Page<KgNlBetDailyDetailVO> list = kgNlBetDailyDetailMapper.findDetailPage(page, dto);
    if (ObjectUtil.isEmpty(list)) {
      return pageDtoVO;
    }

    // 查询总计
    KgNlBetDailyDetail totalData = kgNlBetDailyDetailMapper.findTotalData(dto);

    Map<String, Object> otherData = new HashMap<>();
    otherData.put("totalData", totalData);
    pageDtoVO.setPage(list);
    pageDtoVO.setOtherData(otherData);
    return pageDtoVO;
  }

  @Override
  public void exportDetail(HttpServletResponse response, KgNlWinReportQueryDTO dto) {
    String title = dto.getBeginTime() + "-" + dto.getEndTime() + " 彩票输赢详情";
    ExportParams exportParams = new ExportParams(title, "彩票输赢详情");
    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename = vipWealReword.xls");
    List<KgNlBetDailyDetailVO> list = kgNlBetDailyDetailMapper.findDetailList(dto);
    try (Workbook workbook = ExcelExportUtil.exportExcel(exportParams, KgNlBetDailyDetailVO.class, list)) {
      workbook.write(response.getOutputStream());
    } catch (IOException e) {
      log.error("彩票输赢详情导出IO异常", e);
    }
  }

  @Override
  public void assembleKgNlBetDailyDetail(List<String> dayList, List<String> accountList, String platformCode) {
    String tenant = sysTheme.getTenantCode();
    List<KgNlBetDailyDetail> assembleList = Collections.synchronizedList(new ArrayList<>());

    for (String day : dayList) {
      DateTime parseTime = cn.hutool.core.date.DateUtil.parse(day, "yyyy-MM-dd");
      long beginTime = cn.hutool.core.date.DateUtil.beginOfDay(parseTime).getTime();
      long endTime = cn.hutool.core.date.DateUtil.endOfDay(parseTime).getTime();

      log.info("{}~{}KG新彩票投注日明细汇总开始执行，会员：{}，租户：{}，游戏平台：{}",
        beginTime, endTime, accountList, tenant, platformCode);
      try {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        builder.must(QueryBuilders.termQuery("tenant.keyword", tenant));
        if (CollectionUtil.isNotEmpty(accountList)) {
          builder.must(QueryBuilders.termsQuery("account.keyword", accountList));
        }
        builder.must(QueryBuilders.termQuery("platformCode.keyword", platformCode));
        builder.must(QueryBuilders.termQuery("gameKind.keyword", GameKindEnum.KGNL_LOTTERY.code()));
        builder.must(QueryBuilders.termQuery("settle", SettleStatusEnum.YES.getValue()));
        builder.must(QueryBuilders.rangeQuery("settleTime").gte(beginTime).lte(endTime));

        // 统计条数
        CountRequest countRequest =
          new CountRequest(ContextConstant.ES_INDEX.BET_RECORD_ + sysTheme.getTenantCode());
        countRequest.query(builder);
        RequestOptions.Builder optionsBuilder = RequestOptions.DEFAULT.toBuilder();
        optionsBuilder.setHttpAsyncResponseConsumerFactory(
          new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(31457280));
        CountResponse countResponse = restHighLevelClient.count(countRequest, optionsBuilder.build());
        long sumCount = countResponse.getCount();
        if (sumCount <= 0) {
          continue;
        }

        SearchRequest searchRequest =
          new SearchRequest(ContextConstant.ES_INDEX.BET_RECORD_ + tenant);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermsAggregationBuilder accountGroup =
          AggregationBuilders.terms("accountGroup").field("account.keyword").size((int) sumCount);
        TermsAggregationBuilder gameCodeGroup =
          AggregationBuilders.terms("gameCodeGroup").field("gameCode.keyword");

        SumAggregationBuilder sumBetAmount =
          AggregationBuilders.sum("betAmount").field("betAmount");
        SumAggregationBuilder sumValidAmount =
          AggregationBuilders.sum("validAmount").field("validAmount");
        SumAggregationBuilder sumWinAmount =
          AggregationBuilders.sum("winAmount").field("winAmount");
        SumAggregationBuilder sumLoseWin =
          AggregationBuilders.sum("loseWin").field("loseWin");

        gameCodeGroup.subAggregation(sumBetAmount);
        gameCodeGroup.subAggregation(sumValidAmount);
        gameCodeGroup.subAggregation(sumWinAmount);
        gameCodeGroup.subAggregation(sumLoseWin);
        accountGroup.subAggregation(gameCodeGroup);

        searchSourceBuilder.size(0);
        searchSourceBuilder.query(builder);
        searchSourceBuilder.aggregation(accountGroup);
        searchRequest.source(searchSourceBuilder);

        SearchResponse search =
          restHighLevelClient.search(searchRequest, optionsBuilder.build());
        Terms terms = search.getAggregations().get("accountGroup");
        for (Terms.Bucket bucket : terms.getBuckets()) {
          String account = bucket.getKeyAsString();
          Terms terms2 = bucket.getAggregations().get("gameCodeGroup");
          for (Terms.Bucket bucket2 : terms2.getBuckets()) {
            String gameCode = bucket2.getKeyAsString();
            long count = bucket2.getDocCount();
            BigDecimal betAmount =
              Convert.toBigDecimal(
                ((ParsedSum) bucket2.getAggregations().get("betAmount")).getValue())
                .divide(new BigDecimal("1000"), 3, BigDecimal.ROUND_DOWN);
            BigDecimal validAmount =
              Convert.toBigDecimal(
                ((ParsedSum) bucket2.getAggregations().get("validAmount")).getValue())
                .divide(new BigDecimal("1000"), 3, BigDecimal.ROUND_DOWN);
            BigDecimal winAmount =
              Convert.toBigDecimal(
                ((ParsedSum) bucket2.getAggregations().get("winAmount")).getValue())
                .divide(new BigDecimal("1000"), 3, BigDecimal.ROUND_DOWN).negate();
            long loseWin =
              Convert.toLong(
                ((ParsedSum) bucket2.getAggregations().get("loseWin")).getValue());

            KgNlBetDailyDetail report = new KgNlBetDailyDetail();
            Member member =
              memberService
                .getByAccount(account)
                .orElseThrow(() -> new ServiceException("未找到对应会员信息"));

            report.setMemberId(member.getId());
            report.setAccount(member.getAccount());
            report.setRealName(member.getRealName());
            report.setSuperId(member.getParentId());
            report.setSuperAccount(member.getParentName());
            report.setUserPaths(member.getSuperPath());
            report.setUserType(member.getUserType());

            report.setAccount(account);
            report.setPlatformCode(GamePlatformEnum.KGNL.getCode());
            report.setGameKind(GameKindEnum.KGNL_LOTTERY.code());
            report.setGameType(GameTypeEnum.LOTTERY.code());
            report.setBetAmount(betAmount);
            report.setValidAmount(validAmount);
            report.setWinAmount(winAmount);
            report.setWinCount(loseWin);
            report.setBetCount(count);
            report.setStatTime(day);
            report.setGameCode(gameCode);
            assembleList.add(report);
          }
        }
        log.info("{}~{}KG新彩票投注日明细汇总，执行结束。。。", beginTime, endTime);
      } catch (Exception e) {
        log.info("KG新彩票投注日明细汇总异常，异常原因：", e);
      }

    }

    if (CollectionUtil.isNotEmpty(assembleList)) {
      log.info("KG新彩票投注日明细异步入库开始(结算时间),共{}条", assembleList.size());
      List<List<KgNlBetDailyDetail>> splitList = ListUtils.splitListBycapacity(assembleList, 500);
      for(List<KgNlBetDailyDetail> list : splitList){
        log.info("KG新彩票投注日明细分批入库,共{}条", list.size());
        kgNlBetDailyDetailMapper.batchInsertKgNlBetDailyDetail(list);
      }
    }
  }
}
