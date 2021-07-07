package com.gameplat.admin.controller.open;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gameplat.admin.model.dto.MemberInfoAddDTO;
import com.gameplat.admin.model.dto.MemberInfoEditDTO;
import com.gameplat.admin.model.dto.MemberInfoQueryDTO;
import com.gameplat.admin.model.entity.MemberInfo;
import com.gameplat.admin.model.vo.MemberInfoVO;
import com.gameplat.admin.service.MemberInfoService;
import com.gameplat.common.constant.ServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ServiceApi.OPEN_API + "/memberInfo")
public class MemberInfoController {

  @Autowired private MemberInfoService memberInfoService;

  /** 查询会员信息 */
  @GetMapping(value = "/queryAll")
  public IPage<MemberInfoVO> queryPage(
      IPage<MemberInfo> page, @RequestBody MemberInfoQueryDTO memberInfoQueryDto) {
    // 当前登录用户权限检查
    // 是否允许账号模糊查询
    return memberInfoService.queryPage(page, memberInfoQueryDto);
  }

  /**
   * 新增会员
   *
   * @param memberInfoAddDto
   */
  @PostMapping(value = "/save")
  public void save(MemberInfoAddDTO memberInfoAddDto) {
    memberInfoService.save(memberInfoAddDto);
  }

  /**
   * 更新会员信息
   *
   * @param memberInfoEditDto
   */
  @PostMapping(value = "/update")
  public void update(MemberInfoEditDTO memberInfoEditDto) {
    memberInfoService.update(memberInfoEditDto);
  }
}
