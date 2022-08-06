package com.gameplat.admin.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.config.SysTheme;
import com.gameplat.admin.convert.GameKindConvert;
import com.gameplat.admin.mapper.GameKindMapper;
import com.gameplat.admin.model.dto.GameKindQueryDTO;
import com.gameplat.admin.model.dto.OperGameKindDTO;
import com.gameplat.admin.model.vo.GameKindVO;
import com.gameplat.admin.service.GameKindService;
import com.gameplat.base.common.enums.EnableEnum;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.GameDemoEnableEnum;
import com.gameplat.common.enums.GameKindEnum;
import com.gameplat.common.enums.GameKindStatusEnum;
import com.gameplat.common.lang.Assert;
import com.gameplat.common.model.bean.OssProperty;
import com.gameplat.common.model.vo.MaintainSportVO;
import com.gameplat.common.model.vo.MaintainVO;
import com.gameplat.common.util.Encryption;
import com.gameplat.common.util.OssFileHandler;
import com.gameplat.common.util.OssUtil;
import com.gameplat.model.entity.game.GameKind;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class GameKindServiceImpl extends ServiceImpl<GameKindMapper, GameKind>
    implements GameKindService {

  @Autowired private GameKindConvert gameKindConvert;

  @Resource private GameKindMapper gameKindMapper;

  @Resource private OssFileHandler ossFileHandler;

  @Resource private OssProperty ossProperty;

  @Autowired private SysTheme sysTheme;

  @Override
  public IPage<GameKindVO> selectGameKindList(PageDTO<GameKind> page, GameKindQueryDTO dto) {
    return this.lambdaQuery()
        .eq(ObjectUtils.isNotEmpty(dto.getGameType()), GameKind::getGameType, dto.getGameType())
        .eq(
            ObjectUtils.isNotEmpty(dto.getPlatformCode()),
            GameKind::getPlatformCode,
            dto.getPlatformCode())
        .eq(ObjectUtils.isNotEmpty(dto.getHot()), GameKind::getHot, dto.getHot())
        .ne(GameKind::getStatus, GameKindStatusEnum.REMOVED.getCode())
        .orderByAsc(Lists.newArrayList(GameKind::getPlatformCode, GameKind::getSort))
        .page(page)
        .convert(gameKindConvert::toVo);
  }

  @Override
  public void updateGameKind(OperGameKindDTO operGameKindDTO) {
    GameKind gameKind = gameKindConvert.toEntity(operGameKindDTO);
    if (!this.updateById(gameKind)) {
      throw new ServiceException("更新游戏分类信息失败!");
    }
    /*new Thread(() -> {
      try {
        Thread.sleep(200);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }*/
      GameKind gameDB = this.getById(gameKind.getId());
    log.info("------------------------------{}", JSON.toJSONString(gameDB));
      //当租户的游戏状态为维护时，要避免出现维护起止时间为空的情况
      if (gameDB.getEnable() == 0) {
        gameDB.setMaintenanceTimeStart(gameDB.getMaintenanceTimeStart() == null ? new Date() : gameDB.getMaintenanceTimeStart());
        gameDB.setMaintenanceTimeEnd(gameDB.getMaintenanceTimeEnd() == null ? new Date() : gameDB.getMaintenanceTimeEnd());
      }
      this.processNativeSportMaintain(gameDB, sysTheme.getTenantCode());
    /*}).start();*/
  }

  @Override
  public void updateEnable(OperGameKindDTO operGameKindDTO) {
    Assert.isTrue(
        this.lambdaUpdate()
            .set(
                ObjectUtils.isNotNull(operGameKindDTO.getEnable()),
                GameKind::getEnable,
                operGameKindDTO.getEnable())
            .set(GameKind::getUpdateTime, new Date())
            .ne(GameKind::getEnable, operGameKindDTO.getEnable())
            .update(new GameKind()),
        "更新游戏类型失败!");
  }

  @Override
  public void updateDemoEnable(OperGameKindDTO operGameKindDTO) {
    Assert.isTrue(
        this.lambdaUpdate()
            .set(
                ObjectUtils.isNotNull(operGameKindDTO.getDemoEnable()),
                GameKind::getDemoEnable,
                operGameKindDTO.getDemoEnable())
            .set(GameKind::getUpdateTime, new Date())
            .ne(GameKind::getDemoEnable, GameDemoEnableEnum.NOT_SUPPORT.getCode())
            .update(new GameKind()),
        "启用试玩失败!");
  }

  /** 根据gameType(LIVE，CHESS 。。。) 获取游戏平台 */
  @Override
  @SentinelResource(value = "getGameKindInBanner")
  public List<GameKindVO> getGameKindInBanner(String gameType) {
    return this.lambdaQuery()
        .eq(StringUtils.isNotBlank(gameType), GameKind::getGameType, gameType)
        .eq(GameKind::getStatus, 2)
        .eq(GameKind::getEnable, 1)
        .list()
        .stream()
        .map(gameKindConvert::toVo)
        .collect(Collectors.toList());
  }

  @Override
  @SentinelResource(value = "getByCode")
  public GameKindVO getByCode(String code) {
    return gameKindMapper.getByCode(code);
  }

  @Override
  public List<GameKind> queryGameKindListByPlatformCode(String platformCode) {
    return this.lambdaQuery()
        .eq(ObjectUtils.isNotEmpty(platformCode), GameKind::getPlatformCode, platformCode)
        .list();
  }

  @Override
  public List<GameKind> getList() {
    return this.lambdaQuery().eq(GameKind::getEnable, EnableEnum.ENABLED.code()).list();
  }

  @Override
  public List<GameKind> queryUnDownGameKindList() {
    return this.lambdaQuery().list().stream()
        .filter(kind -> !kind.getStatus().equals(GameKindStatusEnum.REMOVED.getCode()))
        .collect(Collectors.toList());
  }

  /**
   * 处理原生体育的维护
   * @param gameKind
   */
  private void processNativeSportMaintain(GameKind gameKind, String tenant) {
    try {
      if (Objects.equals(GameKindEnum.IM_SPORT.code(), gameKind.getCode())
              || Objects.equals(GameKindEnum.BTI_SPORT.code(), gameKind.getCode())
              || Objects.equals(GameKindEnum.SB_SPORT.code(), gameKind.getCode())) {

        String fileName = String.format("%s.json", Encryption.encrypByMD5(tenant));
        String maintainUrl = String.format("%s/maintain/%s", ossProperty.getForeignUrl(), fileName);
        log.info("------------------------------{}", maintainUrl);
        if (OssUtil.isExist(maintainUrl)) {
          MaintainVO maintain = OssUtil.readOssFile(maintainUrl);
          MaintainSportVO sport = new MaintainSportVO();
          sport.setFirstCode(gameKind.getCode());
          /**
           * 租户游戏状态维护时，以租户的游戏状态为准，反之取云总的游戏状态
           */
          sport.setStatus(gameKind.getEnable()==0 ? gameKind.getEnable() : gameKind.getTemplateEnable());
          if (gameKind.getMaintenanceTimeStart() != null) {
            sport.setStartDate(gameKind.getMaintenanceTimeStart().getTime());
          }
          if (gameKind.getMaintenanceTimeEnd() != null) {
            sport.setEndDate(gameKind.getMaintenanceTimeEnd().getTime());
          }
          if (Objects.equals(GameKindEnum.IM_SPORT.code(), gameKind.getCode())) {
            maintain.setIm(sport);
          } else if (Objects.equals(GameKindEnum.BTI_SPORT.code(), gameKind.getCode())) {
            maintain.setBti(sport);
          } else if (Objects.equals(GameKindEnum.SB_SPORT.code(), gameKind.getCode())) {
            maintain.setSb(sport);
          }

          String content =
                  JSON.toJSONString(
                          maintain,
                          SerializerFeature.PrettyFormat,
                          SerializerFeature.WriteMapNullValue,
                          SerializerFeature.WriteDateUseDateFormat);
          ossFileHandler.updateJsonFile("maintain/", fileName, content);
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
      log.error("原生体育平台维护更新失败");
    }
  }
}
