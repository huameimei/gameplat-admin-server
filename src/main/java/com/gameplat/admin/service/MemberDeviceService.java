package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.MemberDeviceQueryDTO;
import com.gameplat.admin.model.vo.MemberDeviceVO;
import com.gameplat.model.entity.member.MemberDevice;

public interface MemberDeviceService {

  IPage<MemberDeviceVO> page(Page<MemberDevice> page, MemberDeviceQueryDTO queryDTO);

  void delete(Long id);
}
