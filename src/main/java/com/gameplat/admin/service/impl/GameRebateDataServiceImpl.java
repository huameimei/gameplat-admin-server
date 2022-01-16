package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.GameRebateDataMapper;
import com.gameplat.admin.model.domain.GamePlatform;
import com.gameplat.admin.model.domain.GameRebateData;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.dto.GameRebateDataQueryDTO;
import com.gameplat.admin.model.vo.GameReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GameRebateDataService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.UserTypes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class GameRebateDataServiceImpl extends
    ServiceImpl<GameRebateDataMapper, GameRebateData> implements GameRebateDataService {

  @Autowired
  private MemberService memberService;

  @Autowired
  private GameRebateDataMapper gameRebateDataMapper;

  @Override
  public PageDtoVO<GameRebateData> queryPageData(Page<GameRebateData> page,
      GameRebateDataQueryDTO dto) {
    PageDtoVO<GameRebateData> pageDtoVO = new PageDtoVO();
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
    QueryWrapper<GameRebateData> queryWrapper = Wrappers.query();
    queryWrapper.eq(ObjectUtils.isNotEmpty(dto.getAccount()),"account",dto.getAccount());
    if(ObjectUtils.isNotEmpty(dto.getLiveCodeList())){
      queryWrapper.in("game_code", dto.getLiveCodeList());
    }
    if(ObjectUtils.isNotEmpty(dto.getGameKindList())){
      queryWrapper.in("game_kind",dto.getGameKindList());
    }
    queryWrapper.apply(ObjectUtils.isNotEmpty(dto.getBetStartDate()),"stat_time >= STR_TO_DATE({0}, '%Y-%m-%d')",dto.getBetStartDate());
    queryWrapper.apply(ObjectUtils.isNotEmpty(dto.getBetEndDate()),"stat_time <= STR_TO_DATE({0}, '%Y-%m-%d')",dto.getBetEndDate());
    if(ObjectUtils.isNotEmpty(dto.getUserPaths())){
      queryWrapper.likeRight("user_paths",dto.getUserPaths());
    }
    queryWrapper.orderByDesc("stat_time");
    Page<GameRebateData> result =  gameRebateDataMapper.selectPage(page,queryWrapper);

    if (result != null) {
      QueryWrapper<GameRebateData> queryOne = Wrappers.query();
      queryOne.select("sum(bet_amount) as bet_amount,sum(valid_amount) as valid_amount,sum(win_amount) as win_amount");
      queryOne.eq(ObjectUtils.isNotEmpty(dto.getAccount()),"account",dto.getAccount());
      if(ObjectUtils.isNotEmpty(dto.getUserPaths())){
        queryOne.likeRight("user_paths",dto.getUserPaths());
      }
      if(ObjectUtils.isNotEmpty(dto.getLiveCodeList())){
        queryOne.in("game_code", dto.getLiveCodeList());
      }
      if(ObjectUtils.isNotEmpty(dto.getGameKindList())){
        queryOne.in("game_kind",dto.getGameKindList());
      }
      queryOne.apply(ObjectUtils.isNotEmpty(dto.getBetStartDate()),"stat_time >= STR_TO_DATE({0}, '%Y-%m-%d')",dto.getBetStartDate());
      queryOne.apply(ObjectUtils.isNotEmpty(dto.getBetEndDate()),"stat_time <= STR_TO_DATE({0}, '%Y-%m-%d')",dto.getBetEndDate());
      GameRebateData total = gameRebateDataMapper.selectOne(queryOne);
      Map<String, Object> otherData = new HashMap<String, Object>();
      otherData.put("totalData", total);
      pageDtoVO.setPage(result);
      pageDtoVO.setOtherData(otherData);
    }
    return pageDtoVO;
  }


  /**
   * 保存真人返水数据 每个游戏采集回来后 都要调用
   * @param statTime
   * @param gamePlatform
   */
  @Override
  public void saveRebateReport(String statTime, GamePlatform gamePlatform) {
    log.info("{}[{}],statTime:[{}]> Start saveSysBannerInfo live_rebate_data", gamePlatform.getName(), gamePlatform.getCode(), statTime);
    // 获取某一游戏平台当天的统计数据
    int count = this.gameRebateDataMapper.getDayCount(statTime, gamePlatform);
    if (count > 0) {
      log.info("{}[{}],statTime:[{}]> live_rebate_data Rebate bet record data size:[{}]", gamePlatform.getName(), gamePlatform.getCode(), statTime,count);
      // 先删除统计数据
      QueryWrapper<GameRebateData> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq("stat_time",statTime);
      queryWrapper.eq("game_code",gamePlatform.getCode());
      int deleted = gameRebateDataMapper.delete(queryWrapper);
      log.info("{}[{}],statTime:[{}]> live_rebate_data deleted data size:[{}]", gamePlatform.getName(), gamePlatform.getCode(), statTime,deleted);
      //生成统计数据  TODO 具体时间问题待确认
      String statTimeType = "";
      int generate = this.gameRebateDataMapper.saveDayReport(statTime, gamePlatform,statTimeType);

      log.info("{}[{}],statTime:[{}]> live_rebate_data saved data size:[{}]", gamePlatform.getName(), gamePlatform.getCode(), statTime,generate);
    } else {
      log.info("{}[{}],statTime:[{}]> no data saveSysBannerInfo to live_rebate_data", gamePlatform.getName(), gamePlatform.getCode(), statTime);
    }
    log.info("{}[{}],statTime:[{}]> End  saveSysBannerInfo live_rebate_data", gamePlatform.getName(), gamePlatform.getCode(), statTime);
  }

  @Override
  public List<GameReportVO> queryGameReport(GameRebateDataQueryDTO dto) {
    if(StringUtils.isNotBlank(dto.getSuperAccount())) {
      Member member = memberService.getByAccount(dto.getSuperAccount()).orElse(null);
      if (member == null) {
        throw new ServiceException("用户不存在");
      }
      dto.setUserPaths(member.getSuperPath());
      //是否代理账号
      dto.setAccount(null);
    }
    return gameRebateDataMapper.queryGameReport(dto);
  }


  public List<GameRebateData> queryBetRecordList(GameRebateDataQueryDTO dto){
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
    return gameRebateDataMapper.queryBetRecordList(dto);
  }




}
