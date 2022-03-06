package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.SysMessageDTO;
import com.gameplat.model.entity.sys.SysMessage;

import java.util.List;

/**
 * 系统消息 业务层
 *
 * @author gray
 */
public interface SysMessageService extends IService<SysMessage> {

    /**
     * page list
     */
    IPage<SysMessage> pageList(IPage<SysMessage> page, SysMessageDTO dto);

    /**
     * 最近的系统消息
     *
     * @param dto
     * @return
     */
    List<SysMessage> lastList(SysMessageDTO dto);
}
