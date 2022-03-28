package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.AuthIpDTO;
import com.gameplat.admin.model.dto.OperAuthIpDTO;
import com.gameplat.admin.model.vo.AuthIpVo;
import com.gameplat.model.entity.sys.SysAuthIp;

import java.util.List;

/**
 * IP白名单 业务层
 *
 * @author Lenovo
 */
public interface SysAuthIpService extends IService<SysAuthIp> {

  /**
   * 获取白名单列表
   *
   * @param page IPage
   * @param dto AuthIpDTO
   * @return IPage
   */
  IPage<AuthIpVo> selectAuthIpList(IPage<SysAuthIp> page, AuthIpDTO dto);

  /**
   * 新增ip
   *
   * @param dto OperAuthIpDTO
   */
  void addAuthIp(OperAuthIpDTO dto);

  /**
   * 修改ip
   *
   * @param dto OperAuthIpDTO
   */
  void updateAuthIp(OperAuthIpDTO dto);

  /**
   * 删除ip
   *
   * @param id Long
   */
  void deleteAuthIp(Long id);

  /**
   * 批量删除
   *
   * @param ids String
   */
  void deleteBatch(String ids);

  /**
   * 校验ip是否唯一
   *
   * @param ip String
   * @return boolean
   */
  boolean checkAuthIpUnique(String ip);

  /**
   * 获取所有IP
   *
   * @return List
   */
  List<SysAuthIp> getAll();

  /**
   * 是否存在
   *
   * @param ip String
   * @return boolean
   */
  boolean isExist(String ip);
}
