package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.MemberBankAddDTO;
import com.gameplat.admin.model.dto.MemberBankEditDTO;
import com.gameplat.admin.model.vo.MemberBankVO;
import com.gameplat.model.entity.member.MemberBank;

import java.util.List;

public interface MemberBankService extends IService<MemberBank> {

  List<MemberBankVO> getMemberBankList(Long memberId, String type);

  void add(MemberBankAddDTO dto);

  void edit(MemberBankEditDTO dto);

  void setDefault(Long id);
}
