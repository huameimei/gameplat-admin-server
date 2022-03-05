package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.IpAnalysisDTO;
import com.gameplat.admin.model.dto.MemberQueryDTO;
import com.gameplat.admin.model.vo.*;
import com.gameplat.model.entity.member.Member;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MemberMapper extends BaseMapper<Member> {

  IPage<MemberVO> queryPage(Page<Member> page, @Param(Constants.WRAPPER) Wrapper<Member> wrapper);

  List<MemberVO> queryList(@Param(Constants.WRAPPER) Wrapper<Member> wrapper);

  MemberInfoVO getMemberInfo(Long id);

  /**
   * 批量修改代理路径
   *
   * @param member Member
   */
  void batchUpdateSuperPath(Member member);

  /**
   * 批量修改代理路径(不包含本身)
   *
   * @param member Member
   */
  void batchUpdateSuperPathExcludeSelf(Member member);

  /**
   * 更新下级人数
   *
   * @param account String
   * @param lowerNum int
   */
  void updateLowerNumByAccount(String account, int lowerNum);

  /**
   * 根据会员账号获取会员详情
   *
   * @param account String
   * @return MemberInfoVO
   */
  MemberInfoVO getMemberInfoByAccount(String account);

  /**
   * 查询代理线的会员
   *
   * @param agentAccout
   * @return
   */
  List<Member> getListByAgentAccout(String agentAccout);

  /** 根据用户名查生日信息 */
  List<Member> findByUserNameList(List<String> userNames);

  /**
   * 根据账号查询投注日报表所需数据
   *
   * @param accountList List
   * @return List<Member>
   */
  List<Member> getInfoByAccount(@Param("accountList") List<String> accountList);

  /** 注册ip分析 */
  IPage<IpAnalysisVO> page(PageDTO<IpAnalysisVO> page, @Param("dto") IpAnalysisDTO dto);

  /** 获取代理下的所有用户 */
  List<Member> getAgentMember(@Param("list")List<SpreadUnionVO> list, @Param("startTime")String startTime, @Param("endTime")String endTime);

    @Select("select max(agent_level) from member")
    Integer getMaxLevel();

  List<Member> getOpenSalaryAgent(@Param("list") List<Integer> list);

  /** 获取各个充值层级下会员数量和锁定会员数量 */
  List<MemberLevelVO> getUserLevelAccountNum();

    /** 获取某个充值层级下会员数量总数 */
  Integer getUserLevelTotalAccountNum(@Param("userLevel") Integer userLevel);

  /** 获取代理线下的会员账号信息 */
  List<Member> getMemberListByAgentAccount(MemberQueryDTO memberQueryDTO);
}
