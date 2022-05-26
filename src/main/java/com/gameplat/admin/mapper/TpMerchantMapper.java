package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.vo.TpMerchantVO;
import com.gameplat.model.entity.pay.TpMerchant;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TpMerchantMapper extends BaseMapper<TpMerchant> {

  IPage<TpMerchantVO> findTpMerchantPage(Page<TpMerchant> page, @Param("status") Integer status, @Param("name") String name);
}
