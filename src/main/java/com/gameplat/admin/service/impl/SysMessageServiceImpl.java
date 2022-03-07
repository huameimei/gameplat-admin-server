package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.SysMessageMapper;
import com.gameplat.admin.model.dto.SysMessageDTO;
import com.gameplat.admin.service.SysMessageService;
import com.gameplat.base.common.enums.EnableEnum;
import com.gameplat.model.entity.sys.SysMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统版本 业务实现层
 *
 * @author three
 */
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class SysMessageServiceImpl extends ServiceImpl<SysMessageMapper, SysMessage>
        implements SysMessageService {


    @Override
    public IPage<SysMessage> pageList(IPage<SysMessage> page, SysMessageDTO dto) {
        return this.lambdaQuery()
                .eq(SysMessage::getStatus, EnableEnum.ENABLED.code())
                .ge(
                        ObjectUtils.isNotEmpty(dto.getBeginTime()),
                        SysMessage::getCreateTime,
                        dto.getBeginTime())
                .le(
                        ObjectUtils.isNotEmpty(dto.getEndTime()),
                        SysMessage::getCreateTime,
                        dto.getEndTime())
                .orderByDesc(SysMessage::getCreateTime)
                .page(page);
    }

    @Override
    public List<SysMessage> lastList(SysMessageDTO dto) {
        return this.lambdaQuery().apply(true, "TO_DAYS(NOW())-TO_DAYS(create_time) <=3").list();
    }

}
