package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.GameBarDTO;
import com.gameplat.admin.model.vo.GameBarVO;
import com.gameplat.model.entity.game.GameBar;

import java.util.List;

public interface GameBarService extends IService<GameBar> {

    /**
     * 获取游戏导航栏列表
     */
    List<GameBarVO> gameBarList(GameBarDTO dto);

    /**
     * 修改导航栏配置
     */
    void editGameBar(GameBarDTO dto);

    /**
     * 修改导航栏配置
     */
    void deleteGameBar(Long id);


    /**
     * 修改导航栏配置
     */
    void setHot(Long id);
}
