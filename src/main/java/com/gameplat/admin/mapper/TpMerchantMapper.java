package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.domain.TpMerchant;
import com.gameplat.admin.model.vo.TpMerchantVO;

public interface TpMerchantMapper extends BaseMapper<TpMerchant> {

  IPage<TpMerchantVO> findTpMerchantPage(Page<TpMerchant> page, Integer status, String name);
}
