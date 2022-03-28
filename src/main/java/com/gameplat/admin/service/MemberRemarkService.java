package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.MemberRemarkAddDTO;
import com.gameplat.admin.model.vo.MemberRemarkVO;
import com.gameplat.model.entity.member.MemberRemark;

import java.util.List;

public interface MemberRemarkService extends IService<MemberRemark> {

  List<MemberRemarkVO> getByMemberId(Long memberId);

  void update(Long memberId, String remark);

  void batchAdd(MemberRemarkAddDTO dto);

  void deleteById(Long id);

  void cleanByMemberId(Long memberId);
}
