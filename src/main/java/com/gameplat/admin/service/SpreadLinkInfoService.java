package com.gameplat.admin.service;

import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.SpreadLinkInfoAddDTO;
import com.gameplat.admin.model.dto.SpreadLinkInfoDTO;
import com.gameplat.admin.model.dto.SpreadLinkInfoEditDTO;
import com.gameplat.admin.model.vo.GameDivideVo;
import com.gameplat.admin.model.vo.SpreadConfigVO;
import com.gameplat.model.entity.spread.SpreadLinkInfo;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/** @Description : 代理推广管理 @Author : cc @Date : 2022/4/2 */
public interface SpreadLinkInfoService extends IService<SpreadLinkInfo> {

  /**
   * 分页列表
   *
   * @param page
   * @param dto
   * @return
   */
  IPage<SpreadConfigVO> page(PageDTO<SpreadLinkInfo> page, SpreadLinkInfoDTO dto);

  /**
   * 导出
   *
   * @param dto
   * @param response
   */
  void exportList(SpreadLinkInfoDTO dto, HttpServletResponse response);

  /**
   * 添加
   *
   * @param configAddDTO
   */
  void add(SpreadLinkInfoAddDTO configAddDTO);

  /**
   * 修改
   *
   * @param configEditDTO
   */
  void update(SpreadLinkInfoEditDTO configEditDTO);

  /**
   * 根据主键删除
   *
   * @param id
   */
  void deleteById(Long id);

  /**
   * 改变状态
   *
   * @param configEditDTO
   */
  void changeStatus(SpreadLinkInfoEditDTO configEditDTO);

  /**
   * 增加推广码时间
   *
   * @param id Long
   */
  void changeReleaseTime(Long id);

  /**
   * 批量启用
   *
   * @param ids
   */
  void batchEnableStatus(List<Long> ids);

  /**
   * 批量关闭状态
   *
   * @param ids List
   */
  void batchDisableStatus(List<Long> ids);

  /**
   * 批量删除
   *
   * @param ids List
   */
  void batchDeleteByIds(List<Long> ids);

  /**
   * 根据代理账号获取代理信息
   *
   * @param agentAccount
   * @return
   */
  List<SpreadLinkInfo> getSpreadList(String agentAccount);

  /**
   * 校验推广码
   *
   * @param code String
   */
  void checkCode(String code, Integer agentMinCodeNum);

  /**
   * 根据用户名获取返点等级下拉
   *
   * @param account String
   * @param statisMax Boolean
   * @param statisMin Boolean
   * @return JSONArray
   */
  JSONArray getSpreadLinkRebate(String account, Boolean statisMax, Boolean statisMin);

  /**
   * 添加或编辑推广码分红配置预设
   *
   * @param linkId
   * @param agentAccount
   * @param paramOwnerConfigMap
   */
  void saveOrEditDivideConfig(
      Long linkId, String agentAccount, Map<String, List<GameDivideVo>> paramOwnerConfigMap);

  /**
   * 获取最大推广码代理返点等级配置列表
   *
   * @param account
   * @return
   */
  BigDecimal getMaxSpreadLinkRebate(String account);

  /**
   * 获取默认的推广链接信息
   */
  List<Map<String, Object>> getDefaultLink();
}
