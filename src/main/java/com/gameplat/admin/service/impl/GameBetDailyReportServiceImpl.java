package com.gameplat.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.GameBetDailyReportMapper;
import com.gameplat.admin.model.bean.ActivityStatisticItem;
import com.gameplat.admin.model.domain.GamePlatform;
import com.gameplat.admin.model.domain.GameBetDailyReport;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.dto.GameBetDailyReportQueryDTO;
import com.gameplat.admin.model.vo.GameReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GameBetDailyReportService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.DateUtil;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.UserTypes;
import com.google.common.collect.Lists;

import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class GameBetDailyReportServiceImpl extends
        ServiceImpl<GameBetDailyReportMapper, GameBetDailyReport> implements
    GameBetDailyReportService {

    @Autowired
    private MemberService memberService;

    @Autowired
    private GameBetDailyReportMapper gameBetDailyReportMapper;


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
        queryWrapper.apply(ObjectUtils.isNotEmpty(dto.getBetStartDate()), "stat_time >= STR_TO_DATE({0}, '%Y-%m-%d')",
                dto.getBetStartDate());
        queryWrapper.apply(ObjectUtils.isNotEmpty(dto.getBetEndDate()), "stat_time <= STR_TO_DATE({0}, '%Y-%m-%d')",
                dto.getBetEndDate());
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
}
