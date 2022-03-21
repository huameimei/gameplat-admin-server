package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.MemberBonusReportQueryDTO;
import com.gameplat.admin.model.vo.GameFinancialReportVO;
import com.gameplat.admin.model.vo.MemberBonusReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;

import javax.servlet.http.HttpServletResponse;

/**
 * @author aBen
 * @date 2022/3/16 20:40
 * @desc
 */
public interface MemberBonusReportService {

    PageDtoVO<MemberBonusReportVO> findMemberBonusReportPage(Page<MemberBonusReportVO> page, MemberBonusReportQueryDTO queryDTO);

    void exportMemberBonusReport(MemberBonusReportQueryDTO queryDTO, HttpServletResponse response);
}
