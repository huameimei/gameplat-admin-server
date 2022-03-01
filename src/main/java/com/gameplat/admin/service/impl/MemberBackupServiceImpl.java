package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.gameplat.admin.convert.MemberBackupConvert;
import com.gameplat.admin.mapper.MemberBackupMapper;
import com.gameplat.admin.model.vo.MemberBackupVO;
import com.gameplat.admin.service.MemberBackupService;
import com.gameplat.base.common.json.JsonUtils;
import com.gameplat.model.entity.member.MemberBackup;
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
public class MemberBackupServiceImpl extends ServiceImpl<MemberBackupMapper, MemberBackup>
    implements MemberBackupService {

  @Autowired private MemberBackupConvert memberBackupConvert;

  @Override
  public IPage<MemberBackupVO> queryPage(PageDTO<MemberBackup> page, Integer type) {
    LambdaQueryWrapper<MemberBackup> queryWrapper = Wrappers.lambdaQuery();
    queryWrapper
        .eq(MemberBackup::getType, type)
        .groupBy(MemberBackup::getSerialNo)
        .orderByDesc(MemberBackup::getCreateTime);

    return this.page(page, queryWrapper).convert(memberBackupConvert::toVo);
  }

  @Override
  public List<MemberBackup> getBySerialNo(String serialNo) {
    return this.lambdaQuery().eq(MemberBackup::getSerialNo, serialNo).list();
  }

  @Override
  public List<String> getContent(String serialNo, String endTime, String startTime) {
    List<String> backupContents =
        this.lambdaQuery()
            .select(MemberBackup::getContent)
            .eq(ObjectUtils.isNotNull(serialNo), MemberBackup::getSerialNo, serialNo)
            .between(
                ObjectUtils.isNotNull(endTime) && ObjectUtils.isNotNull(startTime),
                MemberBackup::getCreateTime,
                endTime,
                startTime)
            .list()
            .stream()
            .map(MemberBackup::getContent)
            .collect(Collectors.toList());

    List<String> result = new ArrayList<>();
    backupContents.forEach(
        content -> {
          List<Map<String, String>> maps = this.parseContent(content);
          if (null != maps) {
            maps.stream().map(JsonUtils::toJson).forEach(result::add);
          }
        });

    backupContents.clear();
    return result;
  }

  private List<Map<String, String>> parseContent(String content) {
    return JsonUtils.parse(content, new TypeReference<List<Map<String, String>>>() {});
  }
}
