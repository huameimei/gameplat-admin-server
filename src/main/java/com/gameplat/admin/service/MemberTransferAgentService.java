package com.gameplat.admin.service;

import com.gameplat.admin.model.dto.MemberTransformDTO;

public interface MemberTransferAgentService {

  /** 转代理 */
  void transform(MemberTransformDTO dto);

  /**
   * 恢复
   *
   * @param serialNo 流水号
   */
  void recover(String serialNo);

  /**
   * 会员转变成代理账号
   *
   * @param memberId
   */
  void changeToAgent(Long memberId);


  /**
   * 会员设备变更认证
   *
   * @param username
   */
  void memberDevice(String username);
}
