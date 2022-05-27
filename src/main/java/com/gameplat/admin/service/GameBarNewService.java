package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.GameBarNewDTO;
import com.gameplat.admin.model.vo.GameBarNewVO;
import com.gameplat.model.entity.game.GameBarNew;

import java.util.List;

public interface GameBarNewService extends IService<GameBarNew> {

    /**
     * 获取游戏导航栏列表
     */
    List<GameBarNewVO> gameBarNewList();

    /**
     * 修改导航栏配置
     */
    void editGameBarNew(GameBarNewDTO dto);


    /**
     * 修改导航栏配置
     */
    void editIsWater(GameBarNewDTO dto);

//    /**
//     * 修改导航栏配置
//     */
//    void deleteGameBar(Long id);
//
//
//    /**
//     * 修改导航栏配置
//     */
//    void setHot(Long id);
}
