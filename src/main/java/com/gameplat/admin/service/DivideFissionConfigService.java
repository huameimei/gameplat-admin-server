package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.DivideConfigDTO;
import com.gameplat.admin.model.vo.GameDivideVo;
import com.gameplat.model.entity.proxy.DivideFissionConfig;

import java.util.Map;

/**
 * 裂变分红模式配置
 *
 * @author cc
 */
public interface DivideFissionConfigService extends IService<DivideFissionConfig> {

  /**
   * 添加
   *
   * @param userName
   * @param s
   */
  void add(String userName, String s);

  /**
   * 获取裂变分红配置编辑前
   *
   * @param userName
   * @param s
   * @return
   */
  Map<String, Object> getFissionConfigForEdit(String userName, String s);

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
   * 根据一级游戏编码获取配置
   *
   * @param superName
   * @param code
   * @return
   */
  GameDivideVo getConfigByFirstCode(String superName, String code);

  /**
   * 根据账号获取配置
   *
   * @param account
   * @return
   */
  DivideFissionConfig getByAccount(String account);
}
