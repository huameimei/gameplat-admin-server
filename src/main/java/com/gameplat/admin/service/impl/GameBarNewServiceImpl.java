package com.gameplat.admin.service.impl;

import cn.hutool.core.lang.Assert;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.GameBarNewConvert;
import com.gameplat.admin.mapper.GameBarNewMapper;
import com.gameplat.admin.model.dto.GameBarDTO;
import com.gameplat.admin.model.dto.GameBarNewDTO;
import com.gameplat.admin.model.vo.GameBarNewVO;
import com.gameplat.admin.model.vo.GameBarVO;
import com.gameplat.admin.service.GameBarNewService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.model.entity.game.GameBar;
import com.gameplat.model.entity.game.GameBarNew;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.context.UserCredential;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class GameBarNewServiceImpl extends ServiceImpl<GameBarNewMapper, GameBarNew> implements GameBarNewService {

  @Autowired private GameBarNewConvert convert;

  @Autowired private GameBarNewMapper mapper;

  private static final Integer BAR_TYPE = 0; //0 顶级导航

  public static final String  HOT_GAME = "hotGame"; //热门导航

  /**
   * 获取游戏导航列表
   */
  @Override
  @SentinelResource(value = "gameBarNewList", fallback = "sentineFallBack")
  public List<GameBarNewVO> gameBarNewList() {
    List<GameBarNewVO> list = this.lambdaQuery().orderByAsc(GameBarNew::getSort).list().stream().map(convert::toVo).collect(Collectors.toList());
    List<GameBarNewVO> prentList = new ArrayList<>();
    list.forEach(x->{
      if (StringUtils.isEmpty(x.getParentCode())){
        prentList.add(x);
      }
    });
    List<GameBarNewVO> children;
    for (GameBarNewVO x : prentList) {
      children = getChildren(x.getBarCode(), list);
      x.setChildrenList(children);
    }
    return prentList;
  }

  /**
   * 数据迭代
   */
  private List<GameBarNewVO> getChildren(String parentCode,List<GameBarNewVO> list) {
    // 存在子节点数据
    List<GameBarNewVO> childList = new ArrayList<>();
    for (GameBarNewVO item : list) {
      if ( StringUtils.isNotEmpty(item.getParentCode()) && parentCode.equals(item.getParentCode())){
        childList.add(item);
      }
    }
    if (childList.size() == 0){
      return new ArrayList<>();
    }
    for (GameBarNewVO gameBarVO : childList) {
      if(StringUtils.isNotEmpty(gameBarVO.getBarCode())) {
        gameBarVO.setChildrenList(getChildren(gameBarVO.getBarCode(), list));
      }
    }
    return childList;
  }



  /**
   * 编辑游戏导航配置
   */
  @Override
  @SentinelResource(value = "editGameBar", fallback = "sentineFallBack")
  public void editGameBarNew(GameBarNewDTO dto) {
      this.lambdaUpdate()
              .set(ObjectUtils.isNotEmpty(dto.getName()), GameBarNew::getName, dto.getName())
              .set(ObjectUtils.isNotEmpty(dto.getBarLogo()), GameBarNew::getBarLogo, dto.getBarLogo())
              .set(ObjectUtils.isNotEmpty(dto.getPcImg()), GameBarNew::getPcImg, dto.getPcImg())
              .set(ObjectUtils.isNotEmpty(dto.getSort()), GameBarNew::getSort, dto.getSort())
              .set(ObjectUtils.isNotEmpty(dto.getGameImgConfig()), GameBarNew::getGameImgConfig, dto.getGameImgConfig())
              .eq(GameBarNew::getId, dto.getId())
              .update();
  }


  @Override
  @SentinelResource(value = "editGameBarNew", fallback = "sentineFallBack")
  public void editIsWater(GameBarNewDTO dto) {
      this.lambdaUpdate()
              .set(ObjectUtils.isNotEmpty(dto.getIsEnableWater()),GameBarNew::getIsEnableWater,dto.getIsEnableWater())
              .set(ObjectUtils.isNotEmpty(dto.getState()),GameBarNew::getState,dto.getState())
              .in(GameBarNew::getId,dto.getIdList())
              .update();
  }


//  /**
//   * 清除热门中的游戏
//   */
//  @Override
//  @SentinelResource(value = "editGameBar", fallback = "sentineFallBack")
//  public void deleteGameBar(Long id) {
//    LambdaQueryWrapper<GameBar> query = Wrappers.lambdaQuery();
//    query.eq(GameBar::getId, id).eq(GameBar::getCode, HOT_GAME);
//    GameBar gameBar = mapper.selectOne(query);
//    if (gameBar == null){
//      throw new ServiceException("异常的操作");
//    }
//    this.remove(query);
//  }
//
//
//  /**
//   * 将某个游戏设置为热门游戏
//   */
//  @Override
//  public void setHot(Long id) {
//    try {
//      GameBar one = this.lambdaQuery().eq(GameBar::getId, id).one();
//      if (one != null){
//        one.setId(null);
//        one.setCode(HOT_GAME);
//        one.setCreateBy(SecurityUserHolder.getCredential().getUsername());
//        this.save(one);
//      }
//    }catch (Exception e){
//      throw new ServiceException("重复的添加");
//    }
//
//  }


}
