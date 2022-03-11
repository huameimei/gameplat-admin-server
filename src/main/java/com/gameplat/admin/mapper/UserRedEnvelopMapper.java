package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.UserRedEnvelopeDTO;
import com.gameplat.admin.model.vo.PpMerchantVO;
import com.gameplat.admin.model.vo.UserRedEnvelopeVO;
import com.gameplat.model.entity.pay.PpMerchant;
import com.gameplat.model.entity.recharge.UserRedEnvelope;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRedEnvelopMapper extends BaseMapper<UserRedEnvelope> {

    /**
     * 会员红包记录
     */
    List<UserRedEnvelopeVO> recordList(UserRedEnvelopeDTO dto);

}
