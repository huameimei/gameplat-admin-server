package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.domain.TpPayChannel;
import com.gameplat.admin.model.dto.TpPayChannelQueryDTO;
import com.gameplat.admin.model.vo.TpPayChannelVO;

public interface TpPayChannelMapper extends BaseMapper<TpPayChannel> {

  IPage<TpPayChannelVO> findTpPayChannelPage(Page<TpPayChannel> page, TpPayChannelQueryDTO dto);
}
