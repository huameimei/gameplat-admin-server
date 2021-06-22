package com.gameplat.admin.controller.open;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gameplat.admin.model.dto.MemberInfoQueryDto;
import com.gameplat.admin.model.entity.MemberInfo;
import com.gameplat.admin.model.vo.MemberInfoVo;
import com.gameplat.admin.service.MemberInfoService;
import com.gameplat.common.constant.ServiceApi;
import com.gameplat.common.constant.ServiceName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ServiceApi.OPEN_API +"/memberInfo")
public class MemberInfoController {

  @Autowired
  private MemberInfoService memberInfoService;

  /**
   * 查询会员信息
   */
  @GetMapping(value = "/queryAll")
  @ResponseBody
  public IPage<MemberInfoVo> queryPage(IPage<MemberInfo> page,@RequestBody MemberInfoQueryDto memberInfoQueryDto){
    // 当前登录用户权限检查
    // 是否允许账号模糊查询
    return memberInfoService.queryPage(page, memberInfoQueryDto);
  }




}
