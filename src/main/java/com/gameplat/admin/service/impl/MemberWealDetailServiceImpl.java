package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberWealDetailConvert;
import com.gameplat.admin.mapper.MemberWealDetailMapper;
import com.gameplat.admin.mapper.MemberWealMapper;
import com.gameplat.admin.model.dto.MemberWealDetailDTO;
import com.gameplat.admin.model.vo.MemberWealDetailVO;
import com.gameplat.admin.service.MemberWealDetailService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.model.entity.member.MemberWeal;
import com.gameplat.model.entity.member.MemberWealDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 会员福利派发详情
 *
 * @author lily
 * @date 2021/11/25
 */
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberWealDetailServiceImpl
    extends ServiceImpl<MemberWealDetailMapper, MemberWealDetail>
    implements MemberWealDetailService {

  @Autowired private MemberWealDetailConvert detailConvert;

  @Autowired private MemberWealDetailMapper detailMapper;

  @Autowired private MemberWealMapper wealMapper;

  @Override
  public IPage<MemberWealDetailVO> findWealDetailList(
      PageDTO<MemberWealDetail> page, MemberWealDetailDTO queryDTO) {
    return this.lambdaQuery()
        .eq(
            ObjectUtils.isNotEmpty(queryDTO.getWealId()),
            MemberWealDetail::getWealId,
            queryDTO.getWealId())
        .like(
            ObjectUtils.isNotEmpty(queryDTO.getUserName()),
            MemberWealDetail::getUserName,
            queryDTO.getUserName())
        .eq(
            ObjectUtils.isNotEmpty(queryDTO.getStatus()),
            MemberWealDetail::getStatus,
            queryDTO.getStatus())
        .orderByDesc(MemberWealDetail::getId)
        .page(page)
        .convert(detailConvert::toVo);
  }

  @Override
  public void removeWealDetail(Long wealId) {
    if (ObjectUtils.isEmpty(wealId)) {
      throw new ServiceException("福利编号不能为空!");
    }
    LambdaQueryWrapper<MemberWealDetail> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(MemberWealDetail::getWealId, wealId);
    this.remove(wrapper);
  }

  @Override
  public int batchSave(List<MemberWealDetail> list) {
    return detailMapper.batchSave(list);
  }

  @Override
  public List<MemberWealDetail> findSatisfyMember(MemberWealDetail wealDetail) {
    return this.lambdaQuery()
        .eq(
            ObjectUtils.isNotEmpty(wealDetail.getWealId()),
            MemberWealDetail::getWealId,
            wealDetail.getWealId())
        .like(
            ObjectUtils.isNotEmpty(wealDetail.getUserName()),
            MemberWealDetail::getUserName,
            wealDetail.getUserName())
        .eq(
            ObjectUtils.isNotEmpty(wealDetail.getStatus()),
            MemberWealDetail::getStatus,
            wealDetail.getStatus())
        .orderByDesc(MemberWealDetail::getCreateTime)
        .list();
  }

  @Override
  public void updateByWealStatus(Long id, Integer status) {
    if (ObjectUtils.isEmpty(id)) {
      throw new ServiceException("福利编号不能为空!");
    }

    this.update(
        new LambdaUpdateWrapper<MemberWealDetail>()
            .eq(ObjectUtils.isNotEmpty(id), MemberWealDetail::getWealId, id)
            .set(MemberWealDetail::getStatus, status));
  }

  @Override
  public void deleteById(Long id) {
    if (ObjectUtils.isNull(id)) {
      throw new ServiceException("id不能为空");
    }
    MemberWealDetail memberWealDetail = detailMapper.selectById(id);
    BigDecimal amount = new BigDecimal("0");
    if (memberWealDetail.getRewordAmount() != null) {
      amount = memberWealDetail.getRewordAmount();
    }
    this.removeById(id);
    MemberWeal memberWeal = wealMapper.selectById(memberWealDetail.getWealId());
    if (memberWeal.getTotalPayMoney() != null) {
      amount = memberWeal.getTotalPayMoney().subtract(amount);
    }
    memberWeal.setTotalPayMoney(amount);
    memberWeal.setTotalUserCount(memberWeal.getTotalUserCount() - 1);
    wealMapper.updateById(memberWeal);
  }

  @Override
  public void editRewordAmount(Long id, BigDecimal rewordAmount) {
    if (ObjectUtils.isNull(id)) {
      throw new ServiceException("id不能为空");
    }
    MemberWealDetail memberWealDetail = detailMapper.selectById(id);
    BigDecimal amount = new BigDecimal("0");
    if (memberWealDetail.getRewordAmount() != null) {
      amount = memberWealDetail.getRewordAmount();
    }
    this.update(
        new LambdaUpdateWrapper<MemberWealDetail>()
            .eq(MemberWealDetail::getId, id)
            .set(MemberWealDetail::getRewordAmount, rewordAmount));

    MemberWeal memberWeal = wealMapper.selectById(memberWealDetail.getWealId());
    if (memberWeal.getTotalPayMoney() != null) {
      amount = memberWeal.getTotalPayMoney().subtract(amount).add(rewordAmount);
    }
    memberWeal.setTotalPayMoney(amount);
    wealMapper.updateById(memberWeal);
  }
}
