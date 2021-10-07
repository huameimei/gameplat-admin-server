package com.gameplat.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.entity.PpMerchant;
import com.gameplat.admin.model.vo.PpMerchantVO;

public interface PpMerchantMapper extends BaseMapper<PpMerchant> {

    IPage<PpMerchantVO> findPpMerchantPage(Page<PpMerchantVO> page, Integer status, String name);
}
