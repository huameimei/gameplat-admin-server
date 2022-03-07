package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.UserRedEnvelopeDTO;
import com.gameplat.admin.model.vo.UserRedEnvelopeVO;
import com.gameplat.model.entity.recharge.UserRedEnvelope;


/** 用户红包记录 */
public interface UserRedEnvelopeService extends IService<UserRedEnvelope> {

    /**
     * 条件获取用户红包记录
     * @return
     */
    IPage<UserRedEnvelopeVO> recordList(UserRedEnvelopeDTO dto);

    /**
     * 红包回收
     */
    Object redRecycle(UserRedEnvelopeDTO dto);
}
