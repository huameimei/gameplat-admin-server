package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.vo.MemberBillVO;
import com.gameplat.model.entity.member.MemberBill;

import java.util.List;

public interface MemberBillMapper extends BaseMapper<MemberBill> {

  IPage<MemberBillVO> findy(
      PageDTO<MemberBill> page,
      String account,
      String orderNo,
      List<Integer> tranTypes,
      String beginTime,
      String endTime);

  List<MemberBillVO> findy(
      String account, String orderNo, List<Integer> tranTypes, String beginTime, String endTime);

  MemberBill findBill(String orderNo, int tranTypes);
}
