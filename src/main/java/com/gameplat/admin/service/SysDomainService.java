package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.model.entity.setting.SysDomain;

/**
 * @author Lily
 */
public interface SysDomainService extends IService<SysDomain> {

  /**
   * 获取聊天室域名
   * @return
   */
  String getChatDomain();
  /**
   * 获取图片存储域名
   * @return
   */
  String getImageDomain();
}
