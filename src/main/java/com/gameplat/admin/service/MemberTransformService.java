package com.gameplat.admin.service;

import com.gameplat.admin.model.dto.MemberTransformDTO;

public interface MemberTransformService {

  /** 转代理 */
  void transform(MemberTransformDTO dto);

  /**
   * 恢复
   *
   * @param serialNo 流水号
   */
  void recover(String serialNo);
}
