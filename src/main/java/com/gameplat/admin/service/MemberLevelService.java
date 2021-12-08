package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.MemberLevel;
import com.gameplat.admin.model.dto.MemberLevelAddDTO;
import com.gameplat.admin.model.dto.MemberLevelAllocateDTO;
import com.gameplat.admin.model.dto.MemberLevelEditDTO;
import com.gameplat.admin.model.vo.MemberLevelVO;
import java.util.List;

public interface MemberLevelService extends IService<MemberLevel> {

  List<MemberLevelVO> getList();

  void add(MemberLevelAddDTO dto);

  void update(MemberLevelEditDTO dto);

  void delete(Long id);

  void lock(Long id);

  void unlock(Long id);

  void enable(Long id);

  void disable(Long id);

  void enableWithdraw(Long id);

  void disableWithdraw(Long id);

  void batchAllocate(List<MemberLevelAllocateDTO> dtos);
}
