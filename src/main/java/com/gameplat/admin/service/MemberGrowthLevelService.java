package com.gameplat.admin.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.GrowthLevelLogoEditDTO;
import com.gameplat.admin.model.dto.MemberGrowthLevelEditDto;
import com.gameplat.admin.model.vo.MemberConfigLevelVO;
import com.gameplat.admin.model.vo.MemberGrowthLevelVO;
import com.gameplat.model.entity.member.MemberGrowthLevel;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Lily
 */
public interface MemberGrowthLevelService extends IService<MemberGrowthLevel> {

  /** 查询所有等级 */
  List<MemberGrowthLevelVO> findList(Integer limitLevel, String language);

  /** 查询所有等级 */
  List<MemberGrowthLevel> getList(Integer limitLevel, String language);

  MemberGrowthLevel getLevel(Integer level);

  /** 批量修改VIP等级 */
  void batchUpdateLevel(List<MemberGrowthLevelEditDto> list, String language);

  /** 修改logo */
  void updateLogo(GrowthLevelLogoEditDTO dto);

  /** VIP配置和VIP等级列表/查询logo配置列表 */
  MemberConfigLevelVO getLevelConfig(String language);

  /** 后台批量修改VIP等级 */
  void batchUpdateLevel(JSONObject obj);

  /** VIP等级列表 */
  List<MemberGrowthLevelVO> vipList(String language);
}
