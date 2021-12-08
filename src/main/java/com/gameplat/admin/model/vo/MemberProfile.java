package com.gameplat.admin.model.vo;

import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.domain.MemberBank;
import com.gameplat.admin.model.domain.MemberInfo;
import com.gameplat.admin.model.domain.MemberRemark;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class MemberProfile implements Serializable {

  private Member member;

  private MemberInfo memberInfo;

  private List<MemberRemark> memberRemarkList;

  private List<MemberBank> memberBankList;

  private List<MemberBank> memberVirtualList;

}
