package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.SysAuthIp;
import com.gameplat.admin.model.dto.AuthIpDTO;
import com.gameplat.admin.model.dto.OperAuthIpDTO;
import com.gameplat.admin.model.vo.AuthIpVo;
import java.util.Set;

/**
 * IP白名单 业务层
 *
 * @author Lenovo
 */
public interface SysAuthIpService extends IService<SysAuthIp> {

  /**
   * 获取白名单列表
   *
   * @param authIpDTO
   * @return
   */
  IPage<AuthIpVo> selectAuthIpList(IPage<SysAuthIp> page, AuthIpDTO authIpDTO);

  /**
   * 新增ip
   *
   * @param operAuthIpDTO
   * @return
   */
  void insertAuthip(OperAuthIpDTO operAuthIpDTO);

  /**
   * 修改ip
   *
   * @param operAuthIpDTO
   * @return
   */
  void updateAuthIp(OperAuthIpDTO operAuthIpDTO);

  /**
   * 删除ip
   *
   * @param id
   * @return
   */
  void deleteAuthIp(Long id);

  /**
   * 批量删除
   *
   * @param ids
   * @return
   */
  void deleteBatch(String ids);

  /**
   * 校验ip是否唯一
   *
   * @param ip
   * @return
   */
  boolean checkAuthIpUnique(String ip);

  /**
   * 取所有ip列表
   *
   * @return
   */
  Set<String> getAllList();

  /**
   * 是否有权限
   *
   * @param ip
   * @return
   */
  boolean isPermitted(String ip);

  /**
   * 是否存在
   *
   * @param ip
   * @return
   */
  boolean isExist(String ip);
}
