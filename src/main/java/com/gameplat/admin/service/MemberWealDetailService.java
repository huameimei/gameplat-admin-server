package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.MemberWealDetailDTO;
import com.gameplat.admin.model.vo.MemberWealDetailVO;
import com.gameplat.model.entity.member.MemberWealDetail;

import java.math.BigDecimal;
import java.util.List;

public interface MemberWealDetailService extends IService<MemberWealDetail> {

  IPage<MemberWealDetailVO> findWealDetailList(
      PageDTO<MemberWealDetail> page, MemberWealDetailDTO queryDTO);

  void removeWealDetail(Long wealId);

  int batchSave(List<MemberWealDetail> list);

  List<MemberWealDetail> findSatisfyMember(MemberWealDetail wealDetail);

  void updateByWealStatus(Long id, Integer status);

  void deleteById(Long id);

  void editRewordAmount(Long id, BigDecimal rewordAmount);
}
