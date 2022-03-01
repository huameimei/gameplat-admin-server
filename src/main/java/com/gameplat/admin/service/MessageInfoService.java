package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.MessageDistributeQueryDTO;
import com.gameplat.admin.model.dto.MessageInfoAddDTO;
import com.gameplat.admin.model.dto.MessageInfoEditDTO;
import com.gameplat.admin.model.dto.MessageInfoQueryDTO;
import com.gameplat.admin.model.vo.MessageDictDataVO;
import com.gameplat.admin.model.vo.MessageDistributeVO;
import com.gameplat.admin.model.vo.MessageInfoVO;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.message.Message;

/**
 * 消息业务处理
 *
 * @author kenvin
 */
public interface MessageInfoService extends IService<Message> {

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
      PageDTO<Message> page, MessageInfoQueryDTO messageInfoQueryDTO);

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
   * @return
   */
  IPage<MessageDistributeVO> findMessageDistributeList(
      Page<Member> page, MessageDistributeQueryDTO messageDistributeQueryDTO);
}
