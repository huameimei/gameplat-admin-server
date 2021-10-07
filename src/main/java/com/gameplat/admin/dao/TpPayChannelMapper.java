package com.gameplat.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.TpPayChannelQueryDTO;
import com.gameplat.admin.model.entity.TpPayChannel;
import com.gameplat.admin.model.vo.TpPayChannelVO;

public interface TpPayChannelMapper extends BaseMapper<TpPayChannel> {

    IPage<TpPayChannelVO> findTpPayChannelPage(Page<TpPayChannelVO> page, TpPayChannelQueryDTO dto);
}
