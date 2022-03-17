package com.gameplat.admin.service.impl;

import cn.hutool.core.lang.Assert;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.GameBarConvert;
import com.gameplat.admin.mapper.GameBarMapper;
import com.gameplat.admin.model.dto.GameBarDTO;
import com.gameplat.admin.model.vo.GameBarVO;
import com.gameplat.admin.service.GameBarService;
import com.gameplat.model.entity.game.GameBar;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.context.UserCredential;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class GameBarServiceImpl extends ServiceImpl<GameBarMapper, GameBar> implements GameBarService {

  @Autowired private GameBarConvert convert;

  @Autowired private GameBarMapper mapper;

  private static final Integer BAR_TYPE = 0; //0 顶级导航

  /**
   * 获取游戏导航列表
   */
  @Override
  @SentinelResource(value = "gameBarList", fallback = "sentineFallBack")
  public List<GameBarVO> gameBarList(GameBarDTO dto) {
    List<GameBar> list = this.lambdaQuery().orderByDesc(GameBar::getSort).list();
    if (list != null && list.size() > 0){
      List<GameBarVO> barList = new ArrayList<>();
      list.forEach(x->{
        if (x.getGameType().equals(BAR_TYPE)){
          barList.add(convert.toVo(x));
        }
      });

      barList.forEach(x->{
        list.forEach(y->{
          if (!y.getGameType().equals(BAR_TYPE) && y.getCode().equals(x.getCode())){
            x.getGameBarList().add(y);
          }
        });
      });

      return barList;
    }
    return null;
  }

  /**
   * 编辑游戏导航配置
   */
  @Override
  @SentinelResource(value = "editGameBar", fallback = "sentineFallBack")
  public void editGameBar(GameBarDTO dto) {
    try {
      UserCredential credential = SecurityUserHolder.getCredential();
      log.info("{}开始修改游戏导航配置",credential.getUsername());
      dto.setUpdateBy(credential.getUsername());
      boolean update = this.lambdaUpdate()
              .set(ObjectUtils.isNotEmpty(dto.getName()), GameBar::getName, dto.getName())
              .set(ObjectUtils.isNotEmpty(dto.getPcImg()), GameBar::getPcImg, dto.getPcImg())
              .set(ObjectUtils.isNotEmpty(dto.getIsShow()), GameBar::getIsShow, dto.getIsShow())
              .set(ObjectUtils.isNotEmpty(dto.getSort()), GameBar::getSort, dto.getSort())
              .set(ObjectUtils.isNotEmpty(dto.getGameImgConfig()), GameBar::getGameImgConfig, dto.getGameImgConfig())
              .set(ObjectUtils.isNotEmpty(dto.getGameLogo()), GameBar::getGameLogo, dto.getGameLogo())
              .set(ObjectUtils.isNotEmpty(dto.getUpdateBy()), GameBar::getUpdateBy, dto.getUpdateBy())
              .set(GameBar::getUpdateTime, new Date())
              .eq(GameBar::getId, dto.getId())
              .update();
    }catch (Exception e){
      log.error("编辑游戏导航异常{0}",e);
      Assert.isFalse(true,"编辑游戏导航异常");
    }
  }
}
