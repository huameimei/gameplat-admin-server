package com.gameplat.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MessageFeedbackConvert;
import com.gameplat.admin.mapper.MessageFeedbackMapper;
import com.gameplat.admin.model.domain.MessageFeedback;
import com.gameplat.admin.model.dto.MessageFeedbackAddDTO;
import com.gameplat.admin.model.dto.MessageFeedbackQueryDTO;
import com.gameplat.admin.model.dto.MessageFeedbackUpdateDTO;
import com.gameplat.admin.model.vo.MessageFeedbackVO;
import com.gameplat.admin.service.MessageFeedbackService;
import com.gameplat.base.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 * @author lily
 * @description 意见反馈
 * @date 2022/1/12
 */

@Service
@Slf4j
public class MessageFeedbackServiceImpl extends ServiceImpl<MessageFeedbackMapper, MessageFeedback> implements MessageFeedbackService {

    @Autowired
    private MessageFeedbackConvert messageFeedbackConvert;

    /** 写反馈 */
    @Override
    public void insertMessage(MessageFeedbackAddDTO dto) {
        MessageFeedback messageFeedback = messageFeedbackConvert.toEntity(dto);
        this.save(messageFeedback);
    }

    /** 读反馈 */
    @Override
    public void updateMessage(MessageFeedbackUpdateDTO dto) {
        dto.setIsRead(1);
        this.update(messageFeedbackConvert.toEntity(dto),
                new LambdaQueryWrapper<MessageFeedback>()
                        .eq(MessageFeedback::getId, dto.getId())
        );
    }

    /** 删除反馈 */
    @Override
    public void removeMessage(Long id) {
        this.removeById(id);
    }

    /** 反馈列表 */
    @Override
    public IPage<MessageFeedbackVO> getList(PageDTO<MessageFeedback> page, MessageFeedbackQueryDTO dto) {
        return this.lambdaQuery()
                .like(ObjectUtil.isNotEmpty(dto.getTitle()), MessageFeedback::getTitle, dto.getTitle())
                .eq(ObjectUtil.isNotEmpty(dto.getIsRead()), MessageFeedback::getIsRead, dto.getIsRead())
                .eq(ObjectUtil.isNotEmpty(dto.getType()), MessageFeedback::getType, dto.getType())
                .page(page)
                .convert(messageFeedbackConvert::toVo);
    }

    /** 根据id查询反馈内容 */
    @Override
    public MessageFeedbackVO getById(Long id){
        if (ObjectUtil.isEmpty(id)){
            throw new ServiceException("反馈id不能为空");
        }
        MessageFeedback messageFeedback = this.lambdaQuery()
                .eq(MessageFeedback::getId, id)
                .one();
        return messageFeedbackConvert.toVo(messageFeedback);
    }


}
