package com.gameplat.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.GameBetDailyReportMapper;
import com.gameplat.admin.mapper.MemberMapper;
import com.gameplat.admin.model.bean.ActivityStatisticItem;
import com.gameplat.admin.model.domain.GameBetDailyReport;
import com.gameplat.admin.model.domain.GamePlatform;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.dto.GameBetDailyReportQueryDTO;
import com.gameplat.admin.model.vo.GameReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GameBetDailyReportService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.DateUtil;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.SettleStatusEnum;
import com.gameplat.common.enums.UserTypes;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.aggregations.metrics.SumAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class GameBetDailyReportServiceImpl extends ServiceImpl<GameBetDailyReportMapper, GameBetDailyReport>
        implements GameBetDailyReportService {

    @Autowired
    private MemberService memberService;

    @Resource
    private GameBetDailyReportMapper gameBetDailyReportMapper;

    @Resource
    private MemberMapper memberMapper;

    @Resource
    public RestHighLevelClient restHighLevelClient;

    @Override
    public PageDtoVO queryPage(Page<GameBetDailyReport> page, GameBetDailyReportQueryDTO dto) {
        PageDtoVO<GameBetDailyReport> pageDtoVO = new PageDtoVO();
        if (StringUtils.isNotBlank(dto.getSuperAccount())) {
            Member member = memberService.getByAccount(dto.getSuperAccount()).orElse(null);
            if (ObjectUtil.isNotNull(member)) {
                throw new ServiceException("用户不存在");
            }
            dto.setUserPaths(member.getSuperPath());
            //是否代理账号
            if (member.getUserType().equals(UserTypes.AGENT.value())) {
                dto.setAccount(null);
            }
        }
        QueryWrapper<GameBetDailyReport> queryWrapper = Wrappers.query();
        fillQueryWrapper(dto, queryWrapper);
        queryWrapper.orderByDesc(Lists.newArrayList("stat_time", "id"));

        Page<GameBetDailyReport> result = gameBetDailyReportMapper.selectPage(page, queryWrapper);

        QueryWrapper<GameBetDailyReport> queryOne = Wrappers.query();
        queryOne.select("sum(bet_amount) as bet_amount,sum(valid_amount) as valid_amount,sum(win_amount) as win_amount,sum(revenue) as revenue");
        fillQueryWrapper(dto, queryOne);
        GameBetDailyReport gameBetDailyReport = gameBetDailyReportMapper.selectOne(queryOne);
        Map<String, Object> otherData = new HashMap<>();
        otherData.put("totalData", gameBetDailyReport);
        pageDtoVO.setPage(result);
        pageDtoVO.setOtherData(otherData);
        return pageDtoVO;
    }

    private void fillQueryWrapper(GameBetDailyReportQueryDTO dto,
                                  QueryWrapper<GameBetDailyReport> queryWrapper) {
        queryWrapper.eq(ObjectUtils.isNotEmpty(dto.getAccount()), "account", dto.getAccount());
        if (StringUtils.isNotEmpty(dto.getUserPaths())) {
            queryWrapper.likeRight("user_paths", dto.getUserPaths());
        }
        if (StringUtils.isNotEmpty(dto.getPlatformCode())) {
            queryWrapper.in("platform_code",
                    Arrays.asList(dto.getPlatformCode().split(",")));
        }
        if (ObjectUtils.isNotEmpty(dto.getGameKindList())) {
            queryWrapper.in("game_kind",
                    Arrays.asList(dto.getLiveGameKindList().split(",")));
        }
        queryWrapper.eq(ObjectUtils.isNotEmpty(dto.getLiveGameKind()), "game_kind", dto.getLiveGameKind());
        queryWrapper.eq(ObjectUtils.isNotEmpty(dto.getLiveGameSuperType()), "first_kind", dto.getLiveGameSuperType());
        queryWrapper.apply(ObjectUtils.isNotEmpty(dto.getBeginTime()), "stat_time >= STR_TO_DATE({0}, '%Y-%m-%d')",
                dto.getBeginTime());
        queryWrapper.apply(ObjectUtils.isNotEmpty(dto.getEndTime()), "stat_time <= STR_TO_DATE({0}, '%Y-%m-%d')",
                dto.getEndTime());
    }


    @Override
    public void saveGameBetDailyReport(String statTime, GamePlatform gamePlatform) {
        log.info("{}[{}],statTime:[{}]> Start saveSysBannerInfo game_bet_daily_report", gamePlatform.getName(), gamePlatform.getCode(), statTime);
        String tableName = String.format("live_%s_bet_record", gamePlatform.getCode());
        int count = gameBetDailyReportMapper.getDayCount(statTime, tableName);
        if (count > 0) {
            log.info("{}[{}],statTime:[{}] > game_bet_daily_report bet record data size:[{}]", gamePlatform.getName(), gamePlatform.getCode(), statTime, count);
            QueryWrapper<GameBetDailyReport> queryWrapper = Wrappers.query();
            queryWrapper.eq("game_code", gamePlatform.getCode());
            queryWrapper.eq("stat_time", statTime);
            int deleted = gameBetDailyReportMapper.delete(queryWrapper);
            log.info("{}[{}],statTime:[{}] > game_bet_daily_report delete exists data size:[{}]", gamePlatform.getName(), gamePlatform.getCode(), statTime, deleted);
            int generate = this.gameBetDailyReportMapper.saveMemberDayReport(statTime, gamePlatform, tableName);
            log.info("{}[{}],statTime:[{}] > game_bet_daily_report generate data size:[{}]", gamePlatform.getName(), gamePlatform.getCode(), statTime, generate);
        } else {
            log.info("{}[{}],statTime:[{}]> no data saveSysBannerInfo to game_bet_daily_report", gamePlatform.getName(), gamePlatform.getCode(), statTime);
        }
        log.info("{}[{}],statTime:[{}]> End saveSysBannerInfo game_bet_daily_report", gamePlatform.getName(), gamePlatform.getCode(), statTime);
    }

    @Override
    public List<GameReportVO> queryReportList(GameBetDailyReportQueryDTO dto) {
        return gameBetDailyReportMapper.queryReportList(dto);
    }

    @Override
    public List<ActivityStatisticItem> getGameReportInfo(Map map) {
        List<ActivityStatisticItem> activityStatisticItemVOList = gameBetDailyReportMapper.getGameReportInfo(map);
        //连续体育打码天数和连续彩票打码天数需要返回活动期间内用户的打码日期集合，用于后续业务计算最大的连续打码天数
        if (map.get("statisItem") != null && StringUtils.isNotEmpty(activityStatisticItemVOList)) {
            if ((Integer) map.get("statisItem") == 8) {
                List<ActivityStatisticItem> gameDmlDateList = gameBetDailyReportMapper.findGameDmlDateList(map);
                if (StringUtils.isNotEmpty(gameDmlDateList)) {
                    //将逗号分隔的日期String转成List<Date>
                    for (ActivityStatisticItem gameDmlDate : gameDmlDateList) {
                        if (StringUtils.isNotEmpty(gameDmlDate.getGameCountDates())) {
                            List<String> dateList = Arrays.asList(gameDmlDate.getGameCountDates().split(","));
                            //去重
                            List<String> list = dateList.stream()
                                    .distinct()
                                    .collect(Collectors.toList());
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

        List<GameBetDailyReport> assembleList = new ArrayList<>();
        list.forEach(day -> {
            DateTime parseTime = cn.hutool.core.date.DateUtil.parse(day, "yyyy-MM-dd");
            String beginTime = cn.hutool.core.date.DateUtil.beginOfDay(parseTime).toString();
            String endTime = cn.hutool.core.date.DateUtil.endOfDay(parseTime).toString();

            log.info("{}~{}游戏日报表汇总，开始执行！", beginTime, endTime);

            BoolQueryBuilder builder = QueryBuilders.boolQuery();
            builder.must(QueryBuilders.termQuery("tenant", "kgsit"));
            builder.must(QueryBuilders.termQuery("settle", SettleStatusEnum.YES.getValue()));

            // 会员游戏表集合（北京时间） 按投注时间
//            builder.must(QueryBuilders.rangeQuery("betTime.keyword")
//                    .from(beginTime).to(endTime).format(DateUtils.DATE_TIME_PATTERN));
            //todo
            SearchRequest searchRequest = new SearchRequest(new String[]{"bet-record_kgsit"});
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            TermsAggregationBuilder userNameGroup = AggregationBuilders.terms("userNameGroup").field("account.keyword");
            TermsAggregationBuilder firstKindGroup = AggregationBuilders.terms("firstKindGroup").field("firstKind.keyword");

            SumAggregationBuilder sumBetAmount = AggregationBuilders.sum("betAmount").field("betAmount");
            SumAggregationBuilder sumValidAmount = AggregationBuilders.sum("validAmount").field("validAmount");
            SumAggregationBuilder sumWinAmount = AggregationBuilders.sum("winAmount").field("winAmount");

            firstKindGroup.subAggregation(sumBetAmount);
            firstKindGroup.subAggregation(sumValidAmount);
            firstKindGroup.subAggregation(sumWinAmount);
            userNameGroup.subAggregation(firstKindGroup);

            searchSourceBuilder.size(0);
            searchSourceBuilder.query(builder);
            searchSourceBuilder.aggregation(userNameGroup);
            searchSourceBuilder.fetchSource(new FetchSourceContext(
                    true, new String[]{"platformCode", "gameKind"}, Strings.EMPTY_ARRAY));
            searchRequest.source(searchSourceBuilder);
            try {
                RequestOptions.Builder optionsBuilder = RequestOptions.DEFAULT.toBuilder();
                optionsBuilder.setHttpAsyncResponseConsumerFactory(
                        new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(31457280));
                SearchResponse search = restHighLevelClient.search(searchRequest, optionsBuilder.build());
                Terms terms = search.getAggregations().get("userNameGroup");

                for (Terms.Bucket bucket : terms.getBuckets()) {
                    String account = bucket.getKeyAsString();
                    Terms terms2 = bucket.getAggregations().get("firstKindGroup");
                    for (Terms.Bucket bucket2 : terms2.getBuckets()) {
                        String firstKind = bucket2.getKeyAsString();
                        long count = bucket2.getDocCount();
                        double betAmount = ((ParsedSum) bucket2.getAggregations().get("betAmount")).getValue();
                        double validAmount = ((ParsedSum) bucket2.getAggregations().get("validAmount")).getValue();
                        double winAmount = ((ParsedSum) bucket2.getAggregations().get("winAmount")).getValue();

                        GameBetDailyReport report = new GameBetDailyReport();
                        report.setAccount(account);
                        report.setPlatformCode(firstKind.split("_")[0]);
                        report.setGameKind(firstKind.split("_")[1]);
                        report.setFirstKind(firstKind);
                        report.setBetCount(count);
                        report.setBetAmount(new BigDecimal(betAmount));
                        report.setValidAmount(new BigDecimal(validAmount));
                        report.setWinAmount(new BigDecimal(winAmount));
                        assembleList.add(report);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (CollectionUtil.isNotEmpty(assembleList)) {
                assembleList.forEach(o -> o.setStatTime(day));
            }
            log.info("{}~{}游戏日报表汇总，执行结束！", beginTime, endTime);
        });

        if (CollectionUtil.isNotEmpty(assembleList)) {
            List<String> accountList = assembleList.stream().map(GameBetDailyReport::getAccount).collect(Collectors.toList());
            List<Member> infoList = memberMapper.getInfoByAccount(accountList);
            assembleList.forEach(betDailyReport -> {
                Optional<Member> any = infoList.stream().filter(o -> betDailyReport.getAccount().equals(o.getAccount())).findAny();
                if (any.isPresent()) {
                    Member member = any.get();
                    betDailyReport.setMemberId(member.getId());
                    betDailyReport.setRealName(member.getRealName());
                    betDailyReport.setUserPaths(member.getSuperPath());
                }
            });
            gameBetDailyReportMapper.insertGameBetDailyReport(assembleList);
        }
    }
}
