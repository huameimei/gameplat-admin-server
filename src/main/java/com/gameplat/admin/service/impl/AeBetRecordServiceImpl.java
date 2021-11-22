package com.gameplat.admin.service.impl;

import com.gameplat.admin.mapper.AeBetRecordMapper;
import com.gameplat.admin.model.domain.AeBetRecord;
import com.gameplat.admin.service.GameBetRecordService;
import com.gameplat.admin.service.MemberDmlService;
import com.gameplat.common.enums.GameCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("aeBetRecordService")
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class AeBetRecordServiceImpl implements GameBetRecordService<AeBetRecord> {

  @Autowired private AeBetRecordMapper aeBetRecordMapper;

  @Autowired private MemberDmlService memberDmlService;

  @Override
  public void saveRecords(List<AeBetRecord> records) {
    records.forEach(c -> c.setCreateTime(new Date()));
    aeBetRecordMapper.insertIgnoreAllBatch(records);
  }

  @Override
  public void calcGameDml() {
    // 计算打码量
    memberDmlService.calcGameDml(GameCode.AE_LIVE);
  }
}
