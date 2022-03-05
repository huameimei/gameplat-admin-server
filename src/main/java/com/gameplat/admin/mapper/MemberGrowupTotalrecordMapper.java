package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.model.entity.member.MemberGrowupTotalrecord;
import org.apache.ibatis.annotations.Param;

/**
 * 会员成长值汇总
 *
 * @author lily
 * @date 2021/11/27
 * @version
 */
public interface MemberGrowupTotalrecordMapper extends BaseMapper<MemberGrowupTotalrecord> {

  /** 查询当前的用户的汇总的充值成长值 */
  MemberGrowupTotalrecord searchGrowUpToalByMemberId(@Param(value = "userId") Long userId);
}
