package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.vo.MemberBankVO;
import com.gameplat.model.entity.member.MemberBank;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberBankMapper extends BaseMapper<MemberBank> {

  List<MemberBankVO> getMemberBankList(@Param("memberId") Long memberId, @Param("type") String type);
}
