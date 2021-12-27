package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.SpreadLinkInfo;
import com.gameplat.admin.model.dto.SpreadLinkInfoAddDTO;
import com.gameplat.admin.model.dto.SpreadLinkInfoDTO;
import com.gameplat.admin.model.dto.SpreadLinkInfoEditDTO;
import com.gameplat.admin.model.vo.SpreadConfigVO;
import java.util.List;

public interface SpreadLinkInfoService extends IService<SpreadLinkInfo> {

  IPage<SpreadConfigVO> page(PageDTO<SpreadLinkInfo> page, SpreadLinkInfoDTO dto);

  void add(SpreadLinkInfoAddDTO configAddDTO);

  void update(SpreadLinkInfoEditDTO configEditDTO);

  void deleteById(Long id);

  void changeStatus(SpreadLinkInfoEditDTO configEditDTO);

  /**
   * 增加推广码时间
   *
   * @param id
   */
  void changeReleaseTime(Long id);

  void batchEnableStatus(List<Long> ids);

  /**
   * 批量关闭状态
   *
   * @param ids
   */
  void batchDisableStatus(List<Long> ids);

  /**
   * 批量删除
   *
   * @param ids
   */
  void batchDeleteByIds(List<Long> ids);

  /**
   * 根据代理账号获取代理信息
   */
  List<SpreadLinkInfo> getSpreadList(String agentAccount);
}
