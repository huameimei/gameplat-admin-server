package com.gameplat.admin.service;

import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.MemberInfoVO;
import com.gameplat.admin.model.vo.MemberLevelVO;
import com.gameplat.admin.model.vo.MemberVO;
import com.gameplat.admin.model.vo.MessageDistributeVO;
import com.gameplat.model.entity.member.Member;
import java.util.List;
import java.util.Optional;

/**
 * 会员信息服务层
 *
 * @author three
 */
public interface MemberService extends IService<Member> {

  IPage<MemberVO> queryPage(Page<Member> page, MemberQueryDTO dto);

  IPage<MessageDistributeVO> pageMessageDistribute(Page<Member> page, MemberQueryDTO dto);

  List<MemberVO> queryList(MemberQueryDTO dto);

  MemberInfoVO getInfo(Long id);

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

  List<MemberLevelVO> getUserLevelAccountNum();

  Integer getUserLevelTotalAccountNum(Integer userLevel);

  List<Member> getMemberListByAgentAccount(MemberQueryDTO memberQueryDTO);

  JSONArray getRebateForAdd(String agentAccount);

  JSONArray getRebateForEdit(String agentAccount);
}
