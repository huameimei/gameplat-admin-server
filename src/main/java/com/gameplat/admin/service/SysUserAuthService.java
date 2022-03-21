package com.gameplat.admin.service;

import com.gameplat.admin.model.dto.SysUserAuthDto;
import com.gameplat.admin.model.vo.SysUserAuthVo;

import java.util.List;

public interface SysUserAuthService {

  void save(SysUserAuthDto dto);

  List<SysUserAuthVo> findAuth();
}
