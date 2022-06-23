package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.model.entity.member.MemberYubaoInterest;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface MemberYubaoInterestMapper extends BaseMapper<MemberYubaoInterest> {

    void addYubaoInterest(@Param("yubaoId") Long yubaoId,@Param("memberId") Long memberId,@Param("money") double money);

    boolean isSettleInterest(@Param("memberId") Long memberId,@Param("startDate") Date startDate,@Param("endDate")Date endDate);

    double getTotalYubaoInterest(@Param("memberId") Long memberId);
}