package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.MemberBillDTO;
import com.gameplat.admin.model.vo.MemberBillVO;
import com.gameplat.common.model.bean.TranTypeBean;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.member.MemberBill;
import java.util.List;

public interface MemberBillService extends IService<MemberBill> {

  void save(Member member, MemberBill memberBill) throws Exception;

  IPage<MemberBillVO> queryPage(PageDTO<MemberBill> page, MemberBillDTO dto);

  List<MemberBillVO> queryList(MemberBillDTO dto);

  MemberBill queryGameMemberBill(Long memberId, String orderNo, int transType);

  List<TranTypeBean> findTranTypes();
}
