package com.gameplat.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.entity.TpMerchant;
import com.gameplat.admin.model.vo.TpMerchantVO;

public interface TpMerchantMapper extends BaseMapper<TpMerchant> {

    IPage<TpMerchantVO> findTpMerchantPage(Page<TpMerchantVO> page, Integer status, String name);
}
