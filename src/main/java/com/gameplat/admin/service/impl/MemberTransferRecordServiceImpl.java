package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.gameplat.admin.convert.MemberTransferRecordConvert;
import com.gameplat.admin.mapper.MemberTransferRecordMapper;
import com.gameplat.admin.model.vo.MemberTransferRecordVO;
import com.gameplat.admin.service.MemberTransferRecordService;
import com.gameplat.base.common.json.JsonUtils;
import com.gameplat.model.entity.member.MemberTransferRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberTransferRecordServiceImpl
    extends ServiceImpl<MemberTransferRecordMapper, MemberTransferRecord>
    implements MemberTransferRecordService {

  @Autowired private MemberTransferRecordConvert memberTransferRecordConvert;

  @Override
  public IPage<MemberTransferRecordVO> queryPage(PageDTO<MemberTransferRecord> page, Integer type) {
    LambdaQueryWrapper<MemberTransferRecord> queryWrapper = Wrappers.lambdaQuery();
    queryWrapper
        .eq(MemberTransferRecord::getType, type)
        .groupBy(MemberTransferRecord::getSerialNo)
        .orderByDesc(MemberTransferRecord::getCreateTime);

    return this.page(page, queryWrapper).convert(memberTransferRecordConvert::toVo);
  }

  @Override
  public List<MemberTransferRecord> getBySerialNo(String serialNo) {
    return this.lambdaQuery().eq(MemberTransferRecord::getSerialNo, serialNo).list();
  }

  @Override
  public List<String> getContent(String serialNo, String endTime, String startTime) {
    List<String> contents =
        this.lambdaQuery()
            .select(MemberTransferRecord::getContent)
            .eq(ObjectUtils.isNotNull(serialNo), MemberTransferRecord::getSerialNo, serialNo)
            .between(
                ObjectUtils.isNotNull(endTime) && ObjectUtils.isNotNull(startTime),
                MemberTransferRecord::getCreateTime,
                endTime,
                startTime)
            .list()
            .stream()
            .map(MemberTransferRecord::getContent)
            .collect(Collectors.toList());

    List<String> result = new ArrayList<>();
    contents.forEach(
        content -> {
          List<Map<String, String>> maps = this.parseContent(content);
          if (null != maps) {
            maps.stream().map(JsonUtils::toJson).forEach(result::add);
          }
        });

    contents.clear();
    return result;
  }

  private List<Map<String, String>> parseContent(String content) {
    return JsonUtils.parse(content, new TypeReference<List<Map<String, String>>>() {});
  }
}
