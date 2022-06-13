package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.model.entity.member.MemberYubao;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface MemberYubaoMapper extends BaseMapper<MemberYubao> {
    void updateYubaoMoney(@Param("memberId")Long memberId, @Param("money") double money, @Param("balance") BigDecimal balance);

    void updateYubaoList(@Param("transferOutList") List<MemberYubao> transferOutList);
}