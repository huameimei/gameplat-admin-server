package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.MemberWealConfigAddDTO;
import com.gameplat.admin.model.dto.MemberWealConfigEditDTO;
import com.gameplat.model.entity.member.MemberWealConfig;

public interface MemberWealConfigService extends IService<MemberWealConfig> {

  /** 增 */
  void addWealConfig(MemberWealConfigAddDTO dto);

  /** 删 */
  void removeWealConfig(Long id);

  /** 改 */
  void updateWealConfig(MemberWealConfigEditDTO dto);

  /** 查 */
  IPage<MemberWealConfig> page(PageDTO<MemberWealConfig> page, String language);
}
