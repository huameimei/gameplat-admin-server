package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.PayAccountQueryDTO;
import com.gameplat.admin.model.vo.PayAccountVO;
import com.gameplat.model.entity.pay.PayAccount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PayAccountMapper extends BaseMapper<PayAccount> {

  IPage<PayAccountVO> findPayAccountPage(Page<PayAccount> page, @Param("dto") PayAccountQueryDTO dto);
}
