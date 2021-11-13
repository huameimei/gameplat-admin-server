package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.MemberRemark;
import com.gameplat.admin.model.dto.MemberRemarkAddDTO;
import com.gameplat.admin.model.vo.MemberRemarkVO;

import java.util.List;

public interface MemberRemarkService extends IService<MemberRemark> {

  void update(MemberRemarkAddDTO dto);

  List<MemberRemarkVO> getByMemberId(Long memberId);
}
