package com.gameplat.admin.service;

import com.gameplat.admin.model.vo.ChatSideMenuVO;
import com.gameplat.common.enums.ChatConfigEnum;

import java.util.List;

/** 聊天室侧滑菜单业务层处理 */
public interface ChatSideMenuService {

  /** 侧边菜单列表 */
  List<ChatSideMenuVO> queryAllSideMenu();

  /** 修改侧边栏 */
  void edit(String config);

  String queryChatConfig(ChatConfigEnum dataEnum);
}
