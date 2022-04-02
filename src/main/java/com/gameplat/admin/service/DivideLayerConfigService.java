package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.DivideConfigDTO;
import com.gameplat.admin.model.vo.DivideLayerConfigVo;
import com.gameplat.admin.model.vo.GameDivideVo;
import com.gameplat.model.entity.proxy.DivideLayerConfig;

import java.util.Map;

/**
 * 层层代分红模式配置
 *
 * @author : cc @Date : 2022/2/22
 */
public interface DivideLayerConfigService extends IService<DivideLayerConfig> {

  /**
   * 分页列表
   *
   * @param page
   * @param dto
   * @return
   */
  IPage<DivideLayerConfigVo> page(PageDTO<DivideLayerConfig> page, DivideConfigDTO dto);

  /**
   * 添加
   *
   * @param userName
   * @param lang
   */
  void add(String userName, String lang);

  /**
   * 编辑
   *
   * @param divideConfigDTO
   * @param s
   */
  void edit(DivideConfigDTO divideConfigDTO, String s);

  /**
   * 编辑前获取
   *
   * @param userName
   * @param lang
   * @return
   */
  Map<String, Object> getLayerConfigForEdit(String userName, String lang);

  /**
   * 删除
   *
   * @param ids
   */
  void remove(String ids);

  /**
   * 添加前获取
   *
   * @param userName
   * @param lang
   * @return
   */
  Map<String, Object> getLayerConfigForLinkAdd(String userName, String lang);

  /**
   * 编辑前获取
   *
   * @param id
   * @param lang
   * @return
   */
  Map<String, Object> getLayerConfigForLinkEdit(Long id, String lang);

  /**
   * 获取真实顶级代理
   *
   * @param superPath
   * @param userLevel
   * @return
   */
  String getRealSuperName(String superPath, Integer userLevel);

  /**
   * 根据游戏编码获取配置
   *
   * @param userName
   * @param code
   * @return
   */
  GameDivideVo getConfigByGameCode(String userName, String code);
}
