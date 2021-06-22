package com.gameplat.admin.controller.open;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gameplat.admin.model.entity.MemberInfo;
import com.gameplat.admin.service.MemberInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gameplat-admin-service/api/internal/memberInfo")
public class MemberInfoController {

  @Autowired
  private MemberInfoService memberInfoService;

  /**
   * 查询当前登录用户下的账号信息
   */
  @RequestMapping(value = "/query", method = RequestMethod.GET)
  @ResponseBody
  public IPage<MemberInfo>  queryAll(){

     return null;
  }

}
