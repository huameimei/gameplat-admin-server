package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.Message;
import com.gameplat.admin.model.domain.MessageDistribute;
import com.gameplat.admin.model.dto.MessageAddDTO;
import com.gameplat.admin.model.dto.MessageDistributeQueryDTO;
import com.gameplat.admin.model.dto.MessageEditDTO;
import com.gameplat.admin.model.dto.MessageQueryDTO;
import com.gameplat.admin.model.vo.MessageDistributeVO;
import com.gameplat.admin.model.vo.MessageVO;
import com.gameplat.admin.model.vo.PushMessageVO;

/**
 * 消息业务处理
 *
 * @author kenvin
 */
public interface MessageService extends IService<Message> {


    /**
     * 分页查询
     *
     * @param page
     * @param messageQueryDTO
     * @return
     */
    IPage<MessageVO> findMessageList(PageDTO<Message> page, MessageQueryDTO messageQueryDTO);

    /**
     * 新增个人消息
     *
     * @param messageAddDTO
     */
    void insertMessage(MessageAddDTO messageAddDTO);

    /**
     * 批量删除消息
     *
     * @param ids
     */
    void deleteBatchMessage(String ids);

    /**
     * 修改个人消息
     *
     * @param messageEditDTO
     */
    void editMessage(MessageEditDTO messageEditDTO);

    /**
     * 查询分发用户列表
     *
     * @param page
     * @param messageDistributeQueryDTO
     * @return
     */
    IPage<MessageDistributeVO> findMessageDistributeList(PageDTO<MessageDistribute> page, MessageDistributeQueryDTO messageDistributeQueryDTO);
}
