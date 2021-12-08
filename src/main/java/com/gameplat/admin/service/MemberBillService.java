package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.domain.MemberBill;
import com.gameplat.admin.model.dto.MemberBillDTO;
import com.gameplat.admin.model.vo.MemberBillVO;
import javax.servlet.http.HttpServletResponse;

public interface MemberBillService extends IService<MemberBill> {

  void save(Member member, MemberBill memberBill) throws Exception;

  IPage<MemberBillVO> findMemberBilllist(PageDTO<MemberBill> page, MemberBillDTO dto);

  void exportList(MemberBillDTO dto, HttpServletResponse response);

  MemberBill queryLiveBill(Long id, String orderNo, int transType);
}
