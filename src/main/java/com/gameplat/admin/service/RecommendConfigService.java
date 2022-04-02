package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.RecommendConfigDto;
import com.gameplat.admin.model.vo.GameDivideVo;
import com.gameplat.model.entity.proxy.RecommendConfig;

import java.util.List;
import java.util.Map;

/** @Description : 层层代分红配置 @Author : cc @Date : 2022/4/2 */
public interface RecommendConfigService extends IService<RecommendConfig> {
  /**
   * 获取代理配置
   *
   * @return RecommendConfig
   */
  RecommendConfig getRecommendConfig();

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
   * @param dto RecommendConfigDto
   */
  void edit(RecommendConfigDto dto);
}
