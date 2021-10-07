package com.gameplat.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.PayAccountQueryDTO;
import com.gameplat.admin.model.entity.PayAccount;
import com.gameplat.admin.model.vo.PayAccountVO;

public interface PayAccountMapper extends BaseMapper<PayAccount> {

    IPage<PayAccountVO> findPayAccountPage(Page<PayAccountVO> page, PayAccountQueryDTO dto);
}
