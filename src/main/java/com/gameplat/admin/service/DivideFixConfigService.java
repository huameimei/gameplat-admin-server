package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.DivideConfigDTO;
import com.gameplat.admin.model.vo.GameDivideVo;
import com.gameplat.model.entity.proxy.DivideFixConfig;

import java.util.Map;

/** 固定比例分红模式配置 @Author : cc @Date : 2022/2/22 */
public interface DivideFixConfigService extends IService<DivideFixConfig> {
  /**
   * 添加
   *
   * @param userName
   * @param lang
   */
  void add(String userName, String lang);

  /**
   * 编辑前获取
   *
   * @param userName
   * @param s
   * @return
   */
  Map<String, Object> getFixConfigForEdit(String userName, String s);

  /**
   * 编辑
   *
   * @param divideConfigDTO
   * @param lang
   */
  void edit(DivideConfigDTO divideConfigDTO, String lang);

  /**
   * 删除
   *
   * @param ids
   */
  void remove(String ids);

  /**
   * 根据游戏编码获取
   *
   * @param superName
   * @param code
   * @return
   */
  GameDivideVo getConfigByGameCode(String superName, String code);
}
