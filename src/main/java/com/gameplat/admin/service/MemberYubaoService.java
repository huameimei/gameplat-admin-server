package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.model.entity.member.MemberYubao;
import com.gameplat.model.entity.member.MemberYubaoInterest;

public interface MemberYubaoService extends IService<MemberYubao> {

  /**
   * 查询余额宝收益
   * @param account
   * @param startDate
   * @param endDate
   * @param page
   * @return
   */
  IPage<MemberYubaoInterest> queryYubaoInterest(String account, String startDate, String endDate, PageDTO<MemberYubaoInterest> page);

  /**
   * 回收 
   */
  void recycle(String account,Long memberId,Double money);

  /**
   * 结算
   */
  void settle();

}
