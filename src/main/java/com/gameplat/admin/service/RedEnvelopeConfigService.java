package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.RedEnvelopeConfigDTO;
import com.gameplat.model.entity.recharge.RedEnvelopeConfig;

import java.util.List;

/** 红包配置信息 */
public interface RedEnvelopeConfigService extends IService<RedEnvelopeConfig> {

  /** 新增红包配置 */
  boolean redAdd(RedEnvelopeConfigDTO dto);

  /** 红包配置列表 */
  IPage<RedEnvelopeConfig> redList(PageDTO<RedEnvelopeConfig> page, RedEnvelopeConfigDTO dto);

  /** 红包配置信息修改 */
  boolean redEdit(RedEnvelopeConfigDTO dto);

  /** 红包配置信息删除 */
  boolean redDelete(List<Integer> ids);
}
