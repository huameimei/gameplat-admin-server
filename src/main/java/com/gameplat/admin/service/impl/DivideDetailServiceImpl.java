package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.DivideDetailConvert;
import com.gameplat.admin.mapper.DivideDetailMapper;
import com.gameplat.admin.model.dto.DivideDetailQueryDTO;
import com.gameplat.admin.model.vo.DivideDetailVO;
import com.gameplat.admin.model.vo.GameKindVO;
import com.gameplat.admin.service.DivideDetailService;
import com.gameplat.admin.service.GameKindService;
import com.gameplat.model.entity.proxy.DivideDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DivideDetailServiceImpl extends ServiceImpl<DivideDetailMapper, DivideDetail>
    implements DivideDetailService {

  @Autowired private DivideDetailMapper divideDetailMapper;

  @Autowired private DivideDetailConvert divideDetailConvert;

  @Autowired private GameKindService gameKindService;

  @Override
  public IPage<DivideDetailVO> queryPage(PageDTO<DivideDetail> page, DivideDetailQueryDTO dto) {
    QueryWrapper<DivideDetail> queryWrapper = new QueryWrapper<>();
    queryWrapper
        .eq(ObjectUtils.isNotNull(dto.getId()), "id", dto.getId())
        .eq(ObjectUtils.isNotNull(dto.getPeriodsId()), "periods_id", dto.getPeriodsId())
        .eq(ObjectUtils.isNotNull(dto.getProxyId()), "proxy_id", dto.getProxyId())
        .orderByAsc("agent_level");

    IPage<DivideDetailVO> pageResult =
        divideDetailMapper.selectPage(page, queryWrapper).convert(divideDetailConvert::toVo);

    for (DivideDetailVO vo : pageResult.getRecords()) {
      GameKindVO byCode = gameKindService.getByCode(vo.getCode());
      vo.setName(byCode.getName());
    }

    return pageResult;
  }
}
