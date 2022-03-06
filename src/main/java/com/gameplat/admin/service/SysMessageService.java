package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.SysMessageDTO;
import com.gameplat.model.entity.sys.SysMessage;

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
}
