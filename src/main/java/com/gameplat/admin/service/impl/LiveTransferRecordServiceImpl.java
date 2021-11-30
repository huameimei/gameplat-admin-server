package com.gameplat.admin.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.LiveTransferRecordConvert;
import com.gameplat.admin.mapper.LiveTransferRecordMapper;
import com.gameplat.admin.model.domain.LiveTransferRecord;
import com.gameplat.admin.model.dto.LiveTransferRecordQueryDTO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.LiveTransferRecordService;
import com.gameplat.admin.service.MemberService;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
public class LiveTransferRecordServiceImpl extends
    ServiceImpl<LiveTransferRecordMapper, LiveTransferRecord> implements LiveTransferRecordService {

  @Autowired
  private LiveTransferRecordMapper liveTransferRecordMapper;

  @Autowired
  private MemberService memberService;

  @Autowired
  private LiveTransferRecordConvert liveTransferRecordConvert;

  @Override
  public PageDtoVO<LiveTransferRecord> queryLiveTransferRecord(Page<LiveTransferRecord> page,
      LiveTransferRecordQueryDTO dto) {
    PageDtoVO<LiveTransferRecord> pageDtoVO = new PageDtoVO();
    QueryWrapper<LiveTransferRecord> queryWrapper = Wrappers.query();
    if(StringUtils.isNotBlank(dto.getAccount())){
      if (Boolean.TRUE.equals(dto.getAccountFuzzy())){
        queryWrapper.like("account", dto.getAccount());
      }else{
        queryWrapper.eq("account", dto.getAccount());
      }
    }
    queryWrapper.eq(StringUtils.isNotBlank(dto.getOrderNo()),"order_no", dto.getOrderNo());
    queryWrapper.eq(StringUtils.isNotBlank(dto.getLiveCode()),"live_code", dto.getLiveCode());
    queryWrapper.eq(ObjectUtils.isNotNull(dto.getTransferType()),"transfer_type", dto.getTransferType());
    queryWrapper.eq(ObjectUtils.isNotNull(dto.getStatus()),"status", dto.getStatus());
    queryWrapper.eq(ObjectUtils.isNotNull(dto.getTransferStatus()),"transfer_status", dto.getTransferStatus());
    if(StringUtils.isNotBlank(dto.getCreateTimeStart())){
      queryWrapper.apply("create_time >= STR_TO_DATE({0},'%Y-%m-%d %H:%i:%S')",DateUtil.beginOfDay(DateUtil.parse(dto.getCreateTimeStart())));
    }
    if(StringUtils.isNotBlank(dto.getCreateTimeEnd())){
      queryWrapper.apply("create_time <= STR_TO_DATE({0},'%Y-%m-%d %H:%i:%S')",DateUtil.endOfDay(DateUtil.parse(dto.getCreateTimeEnd())));
    }
    queryWrapper.orderByDesc("create_time");
    Page<LiveTransferRecord> liveRebatePeriodVoPage = liveTransferRecordMapper.selectPage(page, queryWrapper);

    queryWrapper.select("SUM(CASE WHEN transfer_type=1 AND STATUS=3 THEN amount ELSE 0 END)-SUM(CASE WHEN transfer_type=2 AND STATUS=3 THEN amount ELSE 0 END) AS amount");
    LiveTransferRecord total = Optional.ofNullable(liveTransferRecordMapper.selectOne(queryWrapper)).orElse(new LiveTransferRecord());
    Map<String, Object> otherData = new HashMap<>();
    otherData.put("totalData", total);
    pageDtoVO.setPage(liveRebatePeriodVoPage);
    pageDtoVO.setOtherData(otherData);
    return pageDtoVO;
  }

}
