package com.gameplat.admin.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gameplat.admin.feign.RemoteLogService;
import com.gameplat.common.model.dto.LogDTO;
import com.gameplat.common.model.entity.MemberLogLogin;
import com.gameplat.common.model.entity.MemberLogOper;
import com.gameplat.common.model.vo.MemberLogVO;
import com.gameplat.common.model.vo.UserLogVO;
import com.gameplat.base.common.util.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 日志 业务层处理
 *
 * @author three
 */
@Slf4j
@Service
public class SysLogService {

  @Autowired private RemoteLogService remoteLogService;

  /** 获取当前操作员的日志 */
  @SentinelResource(value = "getCurrentOperList")
  public IPage<UserLogVO> getCurrentOperList(LogDTO logDTO) {
    return remoteLogService.operList(logDTO).getData();
  }

  /**
   * 获取会员登录日志列表
   *
   * @param logDTO
   * @return
   */
  @SentinelResource(value = "selectLoginList")
  public IPage<MemberLogLogin> selectLoginList(LogDTO logDTO) {
    return null;
  }

  /**
   * 获取会员操作日志列表
   *
   * @param logDTO
   * @return
   */
  @SentinelResource(value = "selectOperList")
  public IPage<MemberLogOper> selectOperList(LogDTO logDTO) {
    return null;
  }

  /**
   * 会员登录日志导出
   *
   * @param logDTO
   * @return
   */
  @SentinelResource(value = "loginExport")
  public List<MemberLogVO> loginExport(LogDTO logDTO) {
    List<MemberLogLogin> list =
        Optional.ofNullable(this.selectLoginList(logDTO))
            .map(IPage::getRecords)
            .orElse(Collections.emptyList());
    return BeanUtils.mapList(list, MemberLogVO.class);
  }

  /**
   * 会员操作日志导出
   *
   * @param logDTO
   * @return
   */
  @SentinelResource(value = "operExport")
  public List<MemberLogVO> operExport(LogDTO logDTO) {
    List<MemberLogOper> list =
        Optional.ofNullable(this.selectOperList(logDTO))
            .map(IPage::getRecords)
            .orElse(Collections.emptyList());

    return BeanUtils.mapList(list, MemberLogVO.class);
  }
}
