package com.gameplat.admin.service.impl;

import cn.hutool.core.lang.Assert;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.GameBarConvert;
import com.gameplat.admin.mapper.GameBarMapper;
import com.gameplat.admin.model.dto.GameBarDTO;
import com.gameplat.admin.model.vo.GameBarVO;
import com.gameplat.admin.service.GameBarService;
import com.gameplat.base.common.exception.ServiceException;
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

  public static final String  HOT_GAME = "hotGame"; //热门导航

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
              .set(ObjectUtils.isNotEmpty(dto.getCode()), GameBar::getCode, dto.getCode())
              .set(ObjectUtils.isNotEmpty(dto.getPcImg()), GameBar::getPcImg, dto.getPcImg())
              .set(ObjectUtils.isNotEmpty(dto.getIsShow()), GameBar::getIsShow, dto.getIsShow())
              .set(ObjectUtils.isNotEmpty(dto.getSort()), GameBar::getSort, dto.getSort())
              .set(ObjectUtils.isNotEmpty(dto.getGameImgConfig()), GameBar::getGameImgConfig, dto.getGameImgConfig())
              .set(ObjectUtils.isNotEmpty(dto.getGameLogo()), GameBar::getGameLogo, dto.getGameLogo())
              .set(ObjectUtils.isNotEmpty(dto.getUpdateBy()), GameBar::getUpdateBy, dto.getUpdateBy())
              .set(GameBar::getUpdateTime, new Date())
              .eq(GameBar::getId, dto.getId())
              .update();
      log.info("修改游戏导航{}",update);
    }catch (Exception e){
      log.error("编辑游戏导航异常{0}",e);
      Assert.isFalse(true,"编辑游戏导航异常");
    }
  }


  /**
   * 清除热门中的游戏
   */
  @Override
  @SentinelResource(value = "editGameBar", fallback = "sentineFallBack")
  public void deleteGameBar(Long id) {
    LambdaQueryWrapper<GameBar> query = Wrappers.lambdaQuery();
    query.eq(GameBar::getId, id).eq(GameBar::getCode, HOT_GAME);
    GameBar gameBar = mapper.selectOne(query);
    if (gameBar == null){
      throw new ServiceException("异常的操作");
    }
    this.remove(query);
  }


  /**
   * 将某个游戏设置为热门游戏
   */
  @Override
  public void setHot(Long id) {
    try {
      GameBar one = this.lambdaQuery().eq(GameBar::getId, id).one();
      if (one != null){
        one.setId(null);
        one.setCode(HOT_GAME);
        one.setCreateBy(SecurityUserHolder.getCredential().getUsername());
        this.save(one);
      }
    }catch (Exception e){
      throw new ServiceException("重复的添加");
    }

  }


}
