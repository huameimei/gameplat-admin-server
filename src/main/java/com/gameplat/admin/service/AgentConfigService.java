package com.gameplat.admin.service;

import com.gameplat.admin.model.vo.GameDivideVo;
import com.gameplat.common.model.bean.AgentConfig;

import java.util.List;
import java.util.Map;

/** 层层代分红配置 @Author : cc @Date : 2022/4/2 */
public interface AgentConfigService {

  /**
   * 获取代理配置
   *
   * @return AgentConfig
   */
  AgentConfig getAgentConfig();

  /**
   * 获取层层代分红配置
   *
   * @return Map
   */
  Map<String, List<GameDivideVo>> getDefaultLayerDivideConfig();

  /**
   * 初始化层层代或固定比例分红模式配置预设
   *
   * @return String
   */
  String initDivideConfig();

  /**
   * 初始化裂变配置
   *
   * @return String
   */
  String initFissionDivideConfig();

  /**
   * 获取固定模式配置
   *
   * @return Map
   */
  Map<String, List<GameDivideVo>> getDefaultFixDivideConfig();

  /**
   * 获取列表模式配置
   *
   * @return Map
   */
  Map<String, Object> getDefaultFissionDivideConfig();

  /**
   * 编辑代理配置
   *
   * @param params Map
   */
  void edit(Map<String, Object> params);
}
