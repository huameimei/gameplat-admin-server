package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.MemberBonusReportQueryDTO;
import com.gameplat.admin.model.vo.MemberBonusReportVO;
import com.gameplat.admin.model.vo.TotalMemberBonusReportVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author aBen
 * @date 2022/3/16 22:34
 * @desc
 */
@Mapper
public interface MemberBonusReportMapper extends BaseMapper<MemberBonusReportVO> {

  Page<MemberBonusReportVO> findMemberBonusReportPage(Page<MemberBonusReportVO> page, @Param("dto") MemberBonusReportQueryDTO dto);

  TotalMemberBonusReportVO findMemberBonusReportTotal(MemberBonusReportQueryDTO dto);

  List<MemberBonusReportVO> findMemberBonusReportList(MemberBonusReportQueryDTO dto);

  Integer findMemberBonusReportCount(MemberBonusReportQueryDTO dto);
}
