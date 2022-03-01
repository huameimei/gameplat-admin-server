package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.vo.PpMerchantVO;
import com.gameplat.model.entity.pay.PpMerchant;
import org.springframework.stereotype.Repository;

@Repository
public interface PpMerchantMapper extends BaseMapper<PpMerchant> {

  IPage<PpMerchantVO> findPpMerchantPage(Page<PpMerchant> page, Integer status, String name);
}
