package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.GoldCoinDescUpdateDTO;
import com.gameplat.admin.model.dto.MemberGrowthConfigEditDto;
import com.gameplat.admin.model.vo.MemberGrowthConfigVO;
import com.gameplat.model.entity.member.MemberGrowthConfig;

public interface MemberGrowthConfigService extends IService<MemberGrowthConfig> {

  /** 只查询一条 */
  MemberGrowthConfigVO findOneConfig();

  MemberGrowthConfig getOneConfig();

  /** 修改成长值配置 */
  void updateGrowthConfig(MemberGrowthConfigEditDto dto);

  /** 后台获取金币说明配置 */
  MemberGrowthConfig getGoldCoinDesc();

  /** 后台修改金币说明配置 */
  void updateGoldCoinDesc(GoldCoinDescUpdateDTO dto);

  Integer getLimitLevel();
}
