package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberVipSignHistoryConvert;
import com.gameplat.admin.mapper.MemberVipSignHistoryMapper;
import com.gameplat.admin.model.dto.MemberVipSignHistoryDTO;
import com.gameplat.admin.model.vo.MemberVipSignHistoryVO;
import com.gameplat.admin.service.MemberVipSignHistoryService;
import com.gameplat.model.entity.member.MemberVipSignHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lily
 * @description 签到历史
 * @date 2021/12/7
 */
@Service
public class MemberVipSignHistoryServiceImpl
    extends ServiceImpl<MemberVipSignHistoryMapper, MemberVipSignHistory>
    implements MemberVipSignHistoryService {

  @Autowired private MemberVipSignHistoryConvert memberVipSignHistoryConvert;

  @Override
  public IPage<MemberVipSignHistoryVO> findPageList(
      PageDTO<MemberVipSignHistory> page, MemberVipSignHistoryDTO dto) {
    return this.lambdaQuery()
        .eq(
            ObjectUtils.isNotEmpty(dto.getUserId()),
            MemberVipSignHistory::getUserId,
            dto.getUserId())
        .like(
            ObjectUtils.isNotEmpty(dto.getUserName()),
            MemberVipSignHistory::getUserName,
            dto.getUserName())
        .ge(
            ObjectUtils.isNotEmpty(dto.getSignBeginTime()),
            MemberVipSignHistory::getSignTime,
            dto.getSignBeginTime())
        .le(
            ObjectUtils.isNotEmpty(dto.getSignEndTime()),
            MemberVipSignHistory::getSignTime,
            dto.getSignEndTime())
        .orderByDesc(MemberVipSignHistory::getSignTime)
        .page(page)
        .convert(memberVipSignHistoryConvert::toVo);
  }
}
