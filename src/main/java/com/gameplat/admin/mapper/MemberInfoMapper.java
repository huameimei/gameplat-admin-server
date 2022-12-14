package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.CleanAccountDTO;
import com.gameplat.admin.model.dto.GameRWDataReportDto;
import com.gameplat.admin.model.dto.QueryIpStatReportDTO;
import com.gameplat.admin.model.vo.IpStatisticsVO;
import com.gameplat.admin.model.vo.TestVO;
import com.gameplat.admin.model.vo.YuBaoMemberBalanceVo;
import com.gameplat.admin.model.vo.YuBaoReportDataVo;
import com.gameplat.model.entity.member.MemberInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface MemberInfoMapper extends BaseMapper<MemberInfo> {

  /** 登录IP统计 */
  List<IpStatisticsVO> findIp(QueryIpStatReportDTO dto);

  /**
   * 获取用户的返点等级
   *
   * @param account
   * @return
   */
  @Select(
      "select mi.rebate from member_info as mi inner join member as m on mi.member_id = m.id where m.account = #{account}")
  BigDecimal findUserRebate(@Param("account") String account);

  /**
   * 获取用户直属下级中的最大返点等级 ---- 作控制校验使用
   *
   * @param account
   * @return
   */
  @Select(
      "select ifnull(max(mi.rebate),0) from member_info as mi inner join member as m on mi.member_id = m.id where m.parent_name = #{account}")
  BigDecimal findUserLowerMaxRebate(@Param("account") String account);

  int updateClearGTMember(CleanAccountDTO dto);


  List<TestVO> getTest(Integer type, Long memberId, String remark);

  List<TestVO> getTest1(Long memberId);

  Page<YuBaoReportDataVo> findYuBaoBalance(Page<YuBaoMemberBalanceVo> page, @Param("dto") GameRWDataReportDto dto);
}
