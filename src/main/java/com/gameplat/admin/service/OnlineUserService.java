package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.bean.OnlineCount;
import com.gameplat.admin.model.bean.PageExt;
import com.gameplat.admin.model.dto.OnlineUserDTO;
import com.gameplat.admin.model.vo.OnlineUserVo;
import com.gameplat.security.context.UserCredential;

import java.util.List;

/**
 * 在线会员服务
 *
 * @author three
 */
public interface OnlineUserService {

  List<UserCredential> getOnlineUsers();

  Boolean isOnline(String account);

  /**
   * 取在线用户列表
   *
   * @param dto OnlineUserDTO
   * @param page PageDTO
   * @return PageExt
   */
  PageExt<OnlineUserVo, OnlineCount> selectOnlineList(
      PageDTO<OnlineUserVo> page, OnlineUserDTO dto);

  /**
   * 踢用户下线
   *
   * @param uuid String
   */
  void kick(String uuid);

  /** 踢出所有用户 */
  void kickAll();
}
