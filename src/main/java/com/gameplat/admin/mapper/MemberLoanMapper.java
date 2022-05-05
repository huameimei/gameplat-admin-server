package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.MemberLoanQueryDTO;
import com.gameplat.admin.model.vo.MemberLoanVO;
import com.gameplat.model.entity.member.MemberLoan;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface MemberLoanMapper extends BaseMapper<MemberLoan> {

    IPage<MemberLoanVO> page(PageDTO<MemberLoan> page, @Param("dto") MemberLoanQueryDTO dto);

    BigDecimal getTotalSum();

    MemberLoanVO getNewRecord(Long memberId);
}
