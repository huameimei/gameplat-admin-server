package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.MemberBlackList;

import java.util.List;

public interface MemberBlackListService extends IService<MemberBlackList> {

    List<MemberBlackList> findMemberBlackList(MemberBlackList memberBlack);
}
