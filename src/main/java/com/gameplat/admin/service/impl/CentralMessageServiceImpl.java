package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.CentralMessageConvert;
import com.gameplat.admin.mapper.CentralMessageMapper;
import com.gameplat.admin.model.vo.CentralMessageVO;
import com.gameplat.admin.service.CentralMessageService;
import com.gameplat.model.entity.CentralMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 中心通知业务处理层
 *
 * @author lily
 * @date 2021/11/18
 */
@Service
public class CentralMessageServiceImpl extends ServiceImpl<CentralMessageMapper, CentralMessage>
    implements CentralMessageService {

  @Autowired private CentralMessageConvert convert;

  @Override
  public IPage<CentralMessageVO> selectCentralMessageList(IPage<CentralMessage> page) {
    LambdaQueryChainWrapper<CentralMessage> queryChainWrapper = this.lambdaQuery();
    return queryChainWrapper.page(page).convert(convert::toVo);
  }
}
