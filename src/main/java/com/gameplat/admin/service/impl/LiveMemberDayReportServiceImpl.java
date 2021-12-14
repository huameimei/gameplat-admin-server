package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.LiveMemberDayReportMapper;
import com.gameplat.admin.model.bean.ActivityStatisticItem;
import com.gameplat.admin.model.domain.GamePlatform;
import com.gameplat.admin.model.domain.LiveMemberDayReport;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.dto.LiveMemberDayReportQueryDTO;
import com.gameplat.admin.model.vo.LiveReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.LiveMemberDayReportService;
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
public class LiveMemberDayReportServiceImpl extends
        ServiceImpl<LiveMemberDayReportMapper, LiveMemberDayReport> implements
        LiveMemberDayReportService {

    @Autowired
    private MemberService memberService;

    @Autowired
    private LiveMemberDayReportMapper liveMemberDayReportMapper;


    @Override
    public PageDtoVO queryPage(Page<LiveMemberDayReport> page, LiveMemberDayReportQueryDTO dto) {
        PageDtoVO<LiveMemberDayReport> pageDtoVO = new PageDtoVO();
        if (StringUtils.isNotBlank(dto.getSuperAccount())) {
            Member member = memberService.getByAccount(dto.getSuperAccount()).orElse(null);
            if (member == null) {
                throw new ServiceException("用户不存在");
            }
            dto.setUserPaths(member.getSuperPath());
            //是否代理账号
            if (member.getUserType().equals(UserTypes.AGENT.value())) {
                dto.setAccount(null);
            }
        }
        QueryWrapper<LiveMemberDayReport> queryWrapper = Wrappers.query();
        fillQueryWrapper(dto, queryWrapper);
        queryWrapper.orderByDesc(Lists.newArrayList("stat_time", "id"));

        Page<LiveMemberDayReport> result = liveMemberDayReportMapper.selectPage(page, queryWrapper);

        QueryWrapper<LiveMemberDayReport> queryOne = Wrappers.query();
        queryOne.select("sum(bet_amount) as bet_amount,sum(valid_amount) as valid_amount,sum(win_amount) as win_amount,sum(revenue) as revenue");
        fillQueryWrapper(dto, queryOne);
        LiveMemberDayReport liveMemberDayReport = liveMemberDayReportMapper.selectOne(queryOne);
        Map<String, Object> otherData = new HashMap<>();
        otherData.put("totalData", liveMemberDayReport);
        pageDtoVO.setPage(result);
        pageDtoVO.setOtherData(otherData);
        return pageDtoVO;
    }

    private void fillQueryWrapper(LiveMemberDayReportQueryDTO dto,
                                  QueryWrapper<LiveMemberDayReport> queryWrapper) {
        queryWrapper.eq(ObjectUtils.isNotEmpty(dto.getAccount()), "account", dto.getAccount());
        if (StringUtils.isNotEmpty(dto.getUserPaths())) {
            queryWrapper.likeRight("user_paths", dto.getUserPaths());
        }
        if (StringUtils.isNotEmpty(dto.getGameCode())) {
            queryWrapper.in("game_code",
                    Arrays.asList(dto.getGameCode().split(",")));
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
    public void saveMemberDayReport(String statTime, GamePlatform gamePlatform) {
        log.info("{}[{}],statTime:[{}]> Start save live_user_day_report", gamePlatform.getName(), gamePlatform.getCode(), statTime);
        String tableName = String.format("live_%s_bet_record", gamePlatform.getCode());
        int count = this.liveMemberDayReportMapper.getDayCount(statTime, tableName);
        if (count > 0) {
            log.info("{}[{}],statTime:[{}] > live_user_day_report bet record data size:[{}]", gamePlatform.getName(), gamePlatform.getCode(), statTime, count);
            QueryWrapper<LiveMemberDayReport> queryWrapper = Wrappers.query();
            queryWrapper.eq("game_code", gamePlatform.getCode());
            queryWrapper.eq("stat_time", statTime);
            int deleted = liveMemberDayReportMapper.delete(queryWrapper);
            log.info("{}[{}],statTime:[{}] > live_user_day_report delete exists data size:[{}]", gamePlatform.getName(), gamePlatform.getCode(), statTime, deleted);
            int generate = this.liveMemberDayReportMapper.saveMemberDayReport(statTime, gamePlatform, tableName);
            log.info("{}[{}],statTime:[{}] > live_user_day_report generate data size:[{}]", gamePlatform.getName(), gamePlatform.getCode(), statTime, generate);
        } else {
            log.info("{}[{}],statTime:[{}]> no data save to live_user_day_report", gamePlatform.getName(), gamePlatform.getCode(), statTime);
        }
        log.info("{}[{}],statTime:[{}]> End save live_user_day_report", gamePlatform.getName(), gamePlatform.getCode(), statTime);
    }

    @Override
    public List<LiveReportVO> queryReportList(LiveMemberDayReportQueryDTO dto) {
        return liveMemberDayReportMapper.queryReportList(dto);
    }

    @Override
    public List<ActivityStatisticItem> getGameReportInfo(Map map) {
        List<ActivityStatisticItem> activityStatisticItemVOList = liveMemberDayReportMapper.getGameReportInfo(map);
        //连续体育打码天数和连续彩票打码天数需要返回活动期间内用户的打码日期集合，用于后续业务计算最大的连续打码天数
        if (map.get("statisItem") != null && StringUtils.isNotEmpty(activityStatisticItemVOList)) {
            if ((Integer) map.get("statisItem") == 8) {
                List<ActivityStatisticItem> gameDmlDateList = liveMemberDayReportMapper.findGameDmlDateList(map);
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
