package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.TpPayChannelQueryDTO;
import com.gameplat.admin.model.vo.TpPayChannelVO;
import com.gameplat.model.entity.pay.TpPayChannel;
import org.springframework.stereotype.Repository;

@Repository
public interface TpPayChannelMapper extends BaseMapper<TpPayChannel> {

  IPage<TpPayChannelVO> findTpPayChannelPage(Page<TpPayChannel> page, TpPayChannelQueryDTO dto);
}
