package com.gameplat.admin.model.vo;

import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.member.MemberBank;
import com.gameplat.model.entity.member.MemberInfo;
import com.gameplat.model.entity.member.MemberRemark;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MemberProfile implements Serializable {

  private Member member;

  private MemberInfo memberInfo;

  private List<MemberRemark> memberRemarkList;

  private List<MemberBank> memberBankList;

  private List<MemberBank> memberVirtualList;
}
