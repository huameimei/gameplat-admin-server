package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.MemberBank;
import com.gameplat.admin.model.vo.MemberBankVO;
import java.util.List;

public interface MemberBankMapper extends BaseMapper<MemberBank> {

  List<MemberBankVO> getMemberBankList(Long memberId, String type);
}
