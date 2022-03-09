package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.UserRedEnvelopMapper;
import com.gameplat.admin.model.dto.UserRedEnvelopeDTO;
import com.gameplat.admin.model.vo.UserRedEnvelopeVO;
import com.gameplat.admin.service.UserRedEnvelopeService;
import com.gameplat.model.entity.recharge.UserRedEnvelope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class UserRedEnvelopServiceImpl extends ServiceImpl<UserRedEnvelopMapper, UserRedEnvelope>
    implements UserRedEnvelopeService {

  @Autowired
  private UserRedEnvelopMapper mapper;

  /**
   * 红包记录列表
   */
  @Override
  public IPage<UserRedEnvelopeVO> recordList(UserRedEnvelopeDTO dto) {
    mapper.recordList(dto);
    return null;
  }

  /**
   * 红包回收
   */
  @Override
  public Object redRecycle(UserRedEnvelopeDTO dto) {
    return null;
  }
}
