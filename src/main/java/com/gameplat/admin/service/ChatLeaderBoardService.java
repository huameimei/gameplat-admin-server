package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.ChatLeaderBoard;
import com.gameplat.base.common.exception.ServiceException;

/**
 * @author lily
 * @description
 * @date 2022/2/16
 */
public interface ChatLeaderBoardService extends IService<ChatLeaderBoard> {

    /** 创建聊天室排行榜 */
    void creatLeaderBoard(String jsonStrParam) throws ServiceException;
}
