package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.model.entity.member.MemberBill;

public interface MemberBillMapper extends BaseMapper<MemberBill> {

  MemberBill findBill(String orderNo, int tranTypes);
}
