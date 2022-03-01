package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.SysBannerInfoAddDTO;
import com.gameplat.admin.model.dto.SysBannerInfoEditDTO;
import com.gameplat.admin.model.dto.SysBannerInfoUpdateStatusDTO;
import com.gameplat.admin.model.vo.SysBannerInfoVO;
import com.gameplat.model.entity.sys.SysBannerInfo;

import java.util.List;

/**
 * banner业务类
 *
 * @author kenvin
 */
public interface SysBannerInfoService {
  /**
   * 查询banner列表
   *
   * @param banner
   * @return
   */
  List<SysBannerInfo> getByBanner(SysBannerInfo banner);

  /**
   * banner信息保存
   *
   * @param sysBannerInfo
   */
  boolean saveSysBannerInfo(SysBannerInfo sysBannerInfo);

  /**
   * 分页查询
   *
   * @param page
   * @param language
   * @param type
   * @return
   */
  IPage<SysBannerInfoVO> list(PageDTO<SysBannerInfo> page, String language, Integer type);

  /**
   * 添加
   *
   * @param sysBannerInfoAddDTO
   */
  void add(SysBannerInfoAddDTO sysBannerInfoAddDTO);

  /**
   * 编辑
   *
   * @param sysBannerInfoEditDTO
   */
  void edit(SysBannerInfoEditDTO sysBannerInfoEditDTO);

  /**
   * 删除bannner
   *
   * @param ids
   */
  void delete(String ids);

  /**
   * 更新banner状态
   *
   * @param sysBannerInfoUpdateStatusDTO
   */
  void updateStatus(SysBannerInfoUpdateStatusDTO sysBannerInfoUpdateStatusDTO);
}
