package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.MemberBlackListMapper;
import com.gameplat.admin.service.MemberBlackListService;
import com.gameplat.model.entity.member.MemberBlackList;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lily
 * @description 会员黑名单业务处理层
 * @date 2021/11/25
 */
@Service
public class MemberBlackListServiceImpl extends ServiceImpl<MemberBlackListMapper, MemberBlackList>
    implements MemberBlackListService {

  @Override
  public List<MemberBlackList> findMemberBlackList(MemberBlackList memberBlack) {
    return this.lambdaQuery()
        .eq(
            ObjectUtils.isNotEmpty(memberBlack.getUserAccount()),
            MemberBlackList::getUserAccount,
            memberBlack.getUserAccount())
        .orderByDesc(MemberBlackList::getCreateTime)
        .list();
  }
}
