package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.domain.PayAccount;
import com.gameplat.admin.model.dto.PayAccountQueryDTO;
import com.gameplat.admin.model.vo.PayAccountVO;

public interface PayAccountMapper extends BaseMapper<PayAccount> {

  IPage<PayAccountVO> findPayAccountPage(Page<PayAccount> page, PayAccountQueryDTO dto);
}
