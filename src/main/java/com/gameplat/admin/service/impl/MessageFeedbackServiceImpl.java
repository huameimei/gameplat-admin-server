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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lily
 * @description 意见反馈
 * @date 2022/1/12
 */

@Service
public class MessageFeedbackServiceImpl extends ServiceImpl<MessageFeedbackMapper, MessageFeedback> implements MessageFeedbackService {

    @Autowired
    private MessageFeedbackConvert messageFeedbackConvert;

    /** 写反馈 */
    @Override
    public void insertMessage(MessageFeedbackAddDTO dto) {
        this.save(messageFeedbackConvert.toEntity(dto));
    }

    /** 读反馈 */
    @Override
    public void updateMessage(MessageFeedbackUpdateDTO dto) {
        dto.setIsRead(1);
        this.update(messageFeedbackConvert.toEntity(dto),
                new LambdaQueryWrapper<MessageFeedback>().eq(MessageFeedback::getId, dto.getId())
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


}
