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

public interface SpreadLinkInfoService extends IService<SpreadLinkInfo> {

  IPage<SpreadConfigVO> page(PageDTO<SpreadLinkInfo> page, SpreadLinkInfoDTO dto);

  void exportList(SpreadLinkInfoDTO dto, HttpServletResponse response);

  void add(SpreadLinkInfoAddDTO configAddDTO);

  void update(SpreadLinkInfoEditDTO configEditDTO);

  void deleteById(Long id);

  void changeStatus(SpreadLinkInfoEditDTO configEditDTO);

  /**
   * 增加推广码时间
   *
   * @param id Long
   */
  void changeReleaseTime(Long id);

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

  /** 根据代理账号获取代理信息 */
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

  void saveOrEditDivideConfig(
      Long linkId, String agentAccount, Map<String, List<GameDivideVo>> paramOwnerConfigMap);

  BigDecimal getMaxSpreadLinkRebate(String account);
}
