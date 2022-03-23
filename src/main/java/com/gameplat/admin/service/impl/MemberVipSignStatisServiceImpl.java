package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberVipSignStatisConvert;
import com.gameplat.admin.mapper.MemberVipSignStatisMapper;
import com.gameplat.admin.model.dto.MemberVipSignStatisDTO;
import com.gameplat.admin.model.vo.MemberVipSignStatisVO;
import com.gameplat.admin.service.MemberVipSignStatisService;
import com.gameplat.model.entity.member.MemberVipSignStatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lily
 * @description VIP会员签到汇总
 * @date 2021/11/24
 */
@Service
public class MemberVipSignStatisServiceImpl
    extends ServiceImpl<MemberVipSignStatisMapper, MemberVipSignStatis>
    implements MemberVipSignStatisService {

  @Autowired private MemberVipSignStatisConvert signStatisConvert;

  @Override
  public IPage<MemberVipSignStatisVO> findSignListPage(
      IPage<MemberVipSignStatis> page, MemberVipSignStatisDTO queryDTO) {
    return this.lambdaQuery()
        .like(
            ObjectUtils.isNotEmpty(queryDTO.getUserName()),
            MemberVipSignStatis::getUserName,
            queryDTO.getUserName())
        .ge(
            ObjectUtils.isNotEmpty(queryDTO.getBeginTime()),
            MemberVipSignStatis::getCreateTime,
            queryDTO.getBeginTime())
        .le(
            ObjectUtils.isNotEmpty(queryDTO.getEndTime()),
            MemberVipSignStatis::getCreateTime,
            queryDTO.getEndTime())
        .orderByDesc(MemberVipSignStatis::getCreateTime)
        .page(page)
        .convert(signStatisConvert::toVo);
  }

  @Override
  public List<MemberVipSignStatis> findSignList(MemberVipSignStatisDTO queryDTO) {
    return this.lambdaQuery()
        .like(
            ObjectUtils.isNotEmpty(queryDTO.getUserName()),
            MemberVipSignStatis::getUserName,
            queryDTO.getUserName())
        .ge(
            ObjectUtils.isNotEmpty(queryDTO.getBeginTime()),
            MemberVipSignStatis::getCreateTime,
            queryDTO.getBeginTime())
        .le(
            ObjectUtils.isNotEmpty(queryDTO.getEndTime()),
            MemberVipSignStatis::getCreateTime,
            queryDTO.getEndTime())
        .list();
  }
}
