package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.bean.RechargeMemberFileBean;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.*;
import com.gameplat.model.entity.member.Member;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 会员信息服务层
 *
 * @author three
 */
public interface MemberService extends IService<Member> {

  IPage<MemberVO> queryPage(Page<Member> page, MemberQueryDTO dto);

  IPage<MessageDistributeVO> pageMessageDistribute(Page<Member> page, MemberQueryDTO dto);

  IPage<MemberBalanceVO> findPromoteMemberBalance(Page<Member> page, MemberQueryDTO dto);

  List<MemberVO> queryList(MemberQueryDTO dto);

  MemberInfoVO getInfo(Long id);

  MemberInfoVO getMemberInfo(Long id);

  MemberInfoVO getMemberDateils(Long id);

  Member getById(Long id);

  Optional<Member> getByAccount(String account);

  Optional<Member> getAgentByAccount(String account);

  Optional<String> getSupperPath(String account);

  List<Member> getByParentName(String parentName);

  MemberInfoVO getMemberInfo(String account);

  void add(MemberAddDTO dto);

  void update(MemberEditDTO dto);

  void disable(List<Long> ids);

  void enable(List<Long> ids);

  void clearContact(MemberContactCleanDTO dto);

  void updateContact(MemberContactUpdateDTO dto);

  void resetPassword(MemberPwdUpdateDTO dto);

  void resetWithdrawPassword(MemberWithdrawPwdUpdateDTO dto);

  void changeWithdrawFlag(Long id, String flag);

  void resetRealName(MemberResetRealNameDTO dto);

  List<String> findAccountByUserLevelIn(List<String> levelsLists);

  void updateRealName(Long memberId, String realName);

  /**
   * 通过用户层级查询用户list
   *
   * @param userLevelList List
   * @return List
   */
  List<Member> getListByUserLevel(List<String> userLevelList);

  /**
   * 查询代理线的会员列表
   *
   * @param agentAccount String
   * @return List
   */
  List<Member> getListByAgentAccount(String agentAccount);

  void updateRemark(Long memberId, String remark);

  void updateRemark(List<Long> memberIds, String remark);

  Member getMemberAndFillGameAccount(String account);

  /**
   * 获取当前最高等级
   *
   * @return Integer
   */
  Integer getMaxLevel();

  /**
   * 获取开启了工资的代理
   *
   * @param list List
   * @return List
   */
  List<Member> getOpenSalaryAgent(List<Integer> list);

  /**
   * 根据多个会员账号批量查询会员信息
   *
   * @param accountList List
   * @return List
   */
  List<Member> getListByAccountList(List<String> accountList);

  /**
   * 获取各个充值层级下会员数量和锁定会员数量
   *
   * @return List
   */
  List<MemberLevelVO> getUserLevelAccountNum();

  Integer getUserLevelTotalAccountNum(Integer userLevel);

  /**
   * 获取代理线下的会员账号信息
   *
   * @param memberQueryDTO MemberQueryDTO
   * @return List
   */
  List<Member> getMemberListByAgentAccount(MemberQueryDTO memberQueryDTO);

  /**
   * 添加账号或添加下级时 彩票投注返点下拉
   *
   * @param agentAccount String
   * @return JSONArray
   */
  List<Map<String, String>> getRebateForAdd(String agentAccount);

  /**
   * 编辑用户时 彩票投注返点数据集
   *
   * @param agentAccount String
   * @return JSONArray
   */
  List<Map<String, String>> getRebateForEdit(String agentAccount);

  void updateDaySalary(String ids, Integer state);

  /**
   * 清除推广会员余额
   *
   * @param dto CleanAccountDTO
   */
  void clearPromoteMemberBalance(CleanAccountDTO dto);

  /**
   * 解除登录限制
   *
   * @param id Long
   */
  void releaseLoginLimit(Long id);


  /**
   * 根据会员账号 层级  vip 等级去查询会员
   */
  MemberBalanceVO findMemberVip(String username);


  List<RechargeMemberFileBean> findMemberRechVip(String level, String vipGrade);

  MemberContactVo getMemberDetail(Long id);

  void batchLevel(MemberLevelBatchDTO dto);

  void exportMembersReport(MemberQueryDTO dto, HttpServletResponse response);
}
