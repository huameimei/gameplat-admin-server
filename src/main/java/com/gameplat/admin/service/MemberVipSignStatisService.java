package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.MemberVipSignStatisDTO;
import com.gameplat.admin.model.vo.MemberVipSignStatisVO;
import com.gameplat.model.entity.member.MemberVipSignStatis;

import java.util.List;

/**
 * VIP会员签到汇总
 *
 * @author lily
 * @date 2021/11/24
 */
public interface MemberVipSignStatisService extends IService<MemberVipSignStatis> {

  /** 查询VIP会员签到记录列表 */
  IPage<MemberVipSignStatisVO> findSignListPage(
      IPage<MemberVipSignStatis> page, MemberVipSignStatisDTO queryDTO);

  List<MemberVipSignStatis> findSignList(MemberVipSignStatisDTO queryDTO);
}
