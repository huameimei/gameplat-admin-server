package com.gameplat.admin.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.GrowthLevelLogoEditDTO;
import com.gameplat.admin.model.dto.MemberGrowthLevelEditDto;
import com.gameplat.admin.model.vo.MemberConfigLevelVO;
import com.gameplat.admin.model.vo.MemberGrowthLevelVO;
import com.gameplat.model.entity.member.MemberGrowthLevel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Lily
 */
public interface MemberGrowthLevelService extends IService<MemberGrowthLevel> {

  /** 查询所有等级 */
  List<MemberGrowthLevelVO> findList(Integer limitLevel);

  /** 查询所有等级 */
  List<MemberGrowthLevel> getList(Integer limitLevel);

  MemberGrowthLevel getLevel(Integer level);

  /** 批量修改VIP等级 */
  void batchUpdateLevel(List<MemberGrowthLevelEditDto> list, HttpServletRequest request);

  /** 修改logo */
  void updateLogo(GrowthLevelLogoEditDTO dto);

  /** VIP配置和VIP等级列表/查询logo配置列表 */
  MemberConfigLevelVO getLevelConfig();

  /** 后台批量修改VIP等级 */
  void batchUpdateLevel(JSONObject obj, HttpServletRequest request);

  /** VIP等级列表 */
  List<MemberGrowthLevelVO> vipList();
}
