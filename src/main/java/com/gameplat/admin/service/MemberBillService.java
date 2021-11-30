package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.domain.MemberBill;


public interface MemberBillService extends IService<MemberBill> {

  void save(Member member, MemberBill memberBill) throws Exception;

}
