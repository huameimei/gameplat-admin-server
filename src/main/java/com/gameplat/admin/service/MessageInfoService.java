package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.MessageInfo;
import com.gameplat.admin.model.domain.MessageDistribute;
import com.gameplat.admin.model.dto.MessageInfoAddDTO;
import com.gameplat.admin.model.dto.MessageDistributeQueryDTO;
import com.gameplat.admin.model.dto.MessageInfoEditDTO;
import com.gameplat.admin.model.dto.MessageInfoQueryDTO;
import com.gameplat.admin.model.vo.MessageDictDataVO;
import com.gameplat.admin.model.vo.MessageDistributeVO;
import com.gameplat.admin.model.vo.MessageInfoVO;

/**
 * 消息业务处理
 *
 * @author kenvin
 */
public interface MessageInfoService extends IService<MessageInfo> {

  /**
   * 获取数据字典数据
   *
   * @return
   */
  MessageDictDataVO getDictData();

  /**
   * 分页查询
   *
   * @param page
   * @param messageInfoQueryDTO
   * @return
   */
  IPage<MessageInfoVO> findMessageList(
      PageDTO<MessageInfo> page, MessageInfoQueryDTO messageInfoQueryDTO);

  /**
   * 新增个人消息
   *
   * @param messageInfoAddDTO
   */
  void insertMessage(MessageInfoAddDTO messageInfoAddDTO);

  /**
   * 批量删除消息
   *
   * @param ids
   */
  void deleteBatchMessage(String ids);

  /**
   * 修改个人消息
   *
   * @param messageInfoEditDTO
   */
  void editMessage(MessageInfoEditDTO messageInfoEditDTO);

  /**
   * 查询分发用户列表
   *
   * @param page
   * @param messageDistributeQueryDTO
   * @return
   */
  IPage<MessageDistributeVO> findMessageDistributeList(
      PageDTO<MessageDistribute> page, MessageDistributeQueryDTO messageDistributeQueryDTO);
}
