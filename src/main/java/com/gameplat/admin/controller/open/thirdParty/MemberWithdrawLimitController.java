package com.gameplat.admin.controller.open.thirdParty;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.enums.DictTypeEnum;
import com.gameplat.admin.model.bean.UserWithdrawLimitInfo;
import com.gameplat.admin.model.domain.SysDictData;
import com.gameplat.admin.model.vo.MemberWithdrawDictDataVo;
import com.gameplat.admin.service.SysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/thirdParty/userWithdrawLimit")
public class MemberWithdrawLimitController {

  @Autowired
  private SysDictDataService dictDataService;

  @DeleteMapping("/remove/{timesForWithdrawal}")
  @PreAuthorize("hasAuthority('thirdParty:memberWithdrawLimit:remove')")
  public void remove(@PathVariable Long timesForWithdrawal) {
    dictDataService.deleteByDictLabel(
        DictTypeEnum.USER_WITHDRAW_LIMIT.getValue() + timesForWithdrawal);
  }

  @PostMapping("/addOrEdit")
  @PreAuthorize("hasAuthority('thirdParty:memberWithdrawLimit:add')")
  public void addOrEdit(@RequestBody UserWithdrawLimitInfo userWithdrawLimitInfo) {
    dictDataService.insertOrUpdate(userWithdrawLimitInfo);
  }

  @PostMapping("/page")
  @PreAuthorize("hasAuthority('thirdParty:memberWithdrawLimit:page')")
  public IPage<MemberWithdrawDictDataVo> queryPage(Page<SysDictData> page) {
    return dictDataService.queryWithdrawPage(page);
  }

}
