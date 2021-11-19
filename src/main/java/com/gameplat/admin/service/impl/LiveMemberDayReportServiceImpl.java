package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.LiveMemberDayReportMapper;
import com.gameplat.admin.model.domain.GamePlatform;
import com.gameplat.admin.model.domain.LiveMemberDayReport;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.dto.LiveMemberDayReportQueryDTO;
import com.gameplat.admin.model.vo.LiveReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.LiveMemberDayReportService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.common.enums.UserTypes;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.util.StringUtils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
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
    if(StringUtils.isNotBlank(dto.getSuperAccount())) {
      Member member = memberService.getByAccount(dto.getSuperAccount()).orElse(null);
      if (member == null) {
        throw new ServiceException("用户不存在");
      }
      dto.setUserPaths(member.getSuperPath());
      //是否代理账号
      if(member.getUserType().equals(UserTypes.AGENT.value())) {
        dto.setAccount(null);
      }
    }
    QueryWrapper<LiveMemberDayReport> queryWrapper = Wrappers.query();
    queryWrapper.eq(ObjectUtils.isNotEmpty(dto.getAccount()),"account",dto.getAccount());
    if(StringUtils.isNotEmpty(dto.getUserPaths())) {
      queryWrapper.likeRight("user_paths", dto.getUserPaths());
    }
    if(StringUtils.isNotEmpty(dto.getGameCode())){
      queryWrapper.in("game_code",
          Arrays.asList(dto.getGameCode().split(",")));
    }
    if(ObjectUtils.isNotEmpty(dto.getGameKindList())){
      queryWrapper.in("game_kind",
          Arrays.asList(dto.getLiveGameKindList().split(",")));
    }
    queryWrapper.eq(ObjectUtils.isNotEmpty(dto.getLiveGameKind()),"game_kind",dto.getLiveGameKind());
    queryWrapper.eq(ObjectUtils.isNotEmpty(dto.getLiveGameSuperType()),"first_kind",dto.getLiveGameSuperType());
    queryWrapper.apply(ObjectUtils.isNotEmpty(dto.getBetStartDate()),"stat_time >= STR_TO_DATE({0}, '%Y-%m-%d')",dto.getBetStartDate());
    queryWrapper.apply(ObjectUtils.isNotEmpty(dto.getBetEndDate()),"stat_time <= STR_TO_DATE({0}, '%Y-%m-%d')",dto.getBetEndDate());
    queryWrapper.orderByDesc("stat_time","id");

    Page<LiveMemberDayReport> result = liveMemberDayReportMapper.selectPage(page,queryWrapper);

    QueryWrapper<LiveMemberDayReport> queryOne = Wrappers.query();
    queryOne.select("sum(bet_amount) as bet_amount,sum(valid_amount) as valid_amount,sum(win_amount) as win_amount,sum(revenue) as revenue");
    if(StringUtils.isNotEmpty(dto.getUserPaths())) {
      queryOne.likeRight("user_paths", dto.getUserPaths());
    }
    queryOne.eq(ObjectUtils.isNotEmpty(dto.getAccount()),"account",dto.getAccount());
    if(ObjectUtils.isNotEmpty(dto.getGameCode())){
      queryOne.in("game_code",
          Arrays.asList(dto.getGameCode().split(",")));
    }
    if(ObjectUtils.isNotEmpty(dto.getGameKindList())){
      queryOne.in("game_kind",
          Arrays.asList(dto.getLiveGameKindList().split(",")));
    }

    queryOne.eq(ObjectUtils.isNotEmpty(dto.getLiveGameKind()),"game_kind",dto.getLiveGameKind());
    queryOne.eq(ObjectUtils.isNotEmpty(dto.getLiveGameSuperType()),"first_kind",dto.getLiveGameSuperType());
    queryOne.apply(ObjectUtils.isNotEmpty(dto.getBetStartDate()),"stat_time >= STR_TO_DATE({0}, '%Y-%m-%d')",dto.getBetStartDate());
    queryOne.apply(ObjectUtils.isNotEmpty(dto.getBetEndDate()),"stat_time <= STR_TO_DATE({0}, '%Y-%m-%d')",dto.getBetEndDate());
    LiveMemberDayReport liveMemberDayReport =  liveMemberDayReportMapper.selectOne(queryOne);
    Map<String, Object> otherData = new HashMap<String, Object>();
    otherData.put("totalData", liveMemberDayReport);
    pageDtoVO.setPage(result);
    pageDtoVO.setOtherData(otherData);
    return pageDtoVO;
  }


  @Override
  public void saveMemberDayReport(String statTime, GamePlatform gamePlatform) {
      log.info("{}[{}],statTime:[{}]> Start save live_user_day_report", gamePlatform.getName(), gamePlatform.getCode(), statTime);
     int count = this.liveMemberDayReportMapper.getDayCount(statTime, gamePlatform);
     if (count > 0) {
        log.info("{}[{}],statTime:[{}] > live_user_day_report bet record data size:[{}]", gamePlatform.getName(), gamePlatform.getCode(), statTime, count);
        QueryWrapper<LiveMemberDayReport> queryWrapper = Wrappers.query();
        queryWrapper.eq("game_code",gamePlatform.getCode());
        queryWrapper.eq("stat_time",statTime);
        int deleted = liveMemberDayReportMapper.delete(queryWrapper);
        log.info("{}[{}],statTime:[{}] > live_user_day_report delete exists data size:[{}]", gamePlatform.getName(), gamePlatform.getCode(), statTime, deleted);
        int generate = this.liveMemberDayReportMapper.saveMemberDayReport(statTime, gamePlatform);
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
}
