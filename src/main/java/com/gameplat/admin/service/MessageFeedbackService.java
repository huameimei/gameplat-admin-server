package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.MessageFeedback;
import com.gameplat.admin.model.dto.MessageFeedbackAddDTO;
import com.gameplat.admin.model.dto.MessageFeedbackQueryDTO;
import com.gameplat.admin.model.dto.MessageFeedbackUpdateDTO;
import com.gameplat.admin.model.vo.MessageFeedbackVO;

import java.util.List;

public interface MessageFeedbackService extends IService<MessageFeedback> {

    /** 写反馈 */
    void insertMessage(MessageFeedbackAddDTO dto);

    /** 读反馈 */
    void updateMessage(MessageFeedbackUpdateDTO dto);

    /** 删除反馈 */
    void removeMessage(Long id);

    /** 反馈列表 */
    IPage<MessageFeedbackVO> getList(PageDTO<MessageFeedback> page, MessageFeedbackQueryDTO dto);

    /** 根据id查询反馈内容 */
    MessageFeedbackVO getById(Long id);

    /** 查看已回复信件 */
    IPage<MessageFeedbackVO> getReplyContent(PageDTO<MessageFeedback> page);

}
