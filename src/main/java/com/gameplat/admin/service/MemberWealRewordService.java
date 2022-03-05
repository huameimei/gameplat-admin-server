package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.MemberWealRewordAddDTO;
import com.gameplat.admin.model.dto.MemberWealRewordDTO;
import com.gameplat.admin.model.vo.MemberWealRewordVO;
import com.gameplat.model.entity.member.MemberWealReword;

import java.util.List;

public interface MemberWealRewordService extends IService<MemberWealReword> {

  /** 获取VIP福利记录列表 分页 */
  IPage<MemberWealRewordVO> findWealRewordList(
      IPage<MemberWealReword> page, MemberWealRewordDTO queryDTO);

  Long findCountReword(MemberWealRewordDTO dto);
  /** 新增福利记录 */
  void insertMemberWealReword(MemberWealRewordAddDTO dto);

  /** 获取VIP福利记录列表 不分页 */
  List<MemberWealReword> findList(MemberWealRewordDTO queryDTO);

  /** 修改VIP福利记录 */
  void updateWealRecord(MemberWealReword entity);
}
