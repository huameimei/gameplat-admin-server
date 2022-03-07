package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.MemberLoanQueryDTO;
import com.gameplat.admin.model.vo.MemberLoanVO;
import com.gameplat.model.entity.member.MemberLoan;

import java.math.BigDecimal;

public interface MemberLoanService extends IService<MemberLoan> {

    /** 分页查 */
    IPage<MemberLoanVO> page(PageDTO<MemberLoan> page, MemberLoanQueryDTO dto);

    void editOrUpdate(MemberLoan memberLoan);

    /** 回收 */
    void recycle(String account);

}
