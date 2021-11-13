package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.vo.MemberInfoVO;
import com.gameplat.admin.model.vo.MemberVO;
import org.apache.ibatis.annotations.Param;

public interface MemberMapper extends BaseMapper<Member> {

  IPage<MemberVO> queryPage(Page<Member> page, @Param(Constants.WRAPPER) Wrapper<Member> wrapper);

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
}
