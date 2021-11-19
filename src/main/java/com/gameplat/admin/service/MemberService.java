package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.MemberInfoVO;
import com.gameplat.admin.model.vo.MemberVO;

import java.util.List;
import java.util.Optional;

/**
 * 会员信息服务层
 *
 * @author three
 */
public interface MemberService extends IService<Member> {

  IPage<MemberVO> queryPage(Page<Member> page, MemberQueryDTO dto);

  MemberInfoVO getInfo(Long id);

  Optional<Member> getByAccount(String account);

  Optional<Member> getAgentByAccount(String account);

  List<Member> getByParentName(String parentName);

  MemberInfoVO getMemberInfo(String account);

  void add(MemberAddDTO dto);

  void update(MemberEditDTO dto);

  void disable(List<Long> ids);

  void enable(List<Long> ids);

  void clearContact(MemberContactCleanDTO dto);

  void updateContact(MemberContactUpdateDTO dto);

  List<String> findAccountByUserLevelIn(List<String> levelsLists);
}
