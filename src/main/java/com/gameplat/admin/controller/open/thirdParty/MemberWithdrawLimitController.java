package com.gameplat.admin.controller.open.thirdParty;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.bean.UserWithdrawLimitInfo;
import com.gameplat.admin.model.vo.MemberWithdrawDictDataVo;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.enums.DictDataEnum;
import com.gameplat.common.enums.DictTypeEnum;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.sys.SysDictData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/thirdParty/userWithdrawLimit")
public class MemberWithdrawLimitController {

  @Autowired private SysDictDataService dictDataService;

  @DeleteMapping("/remove/{timesForWithdrawal}")
  @PreAuthorize("hasAuthority('thirdParty:memberWithdrawLimit:remove')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.WITHDRAW, desc = "'删除提现次数限制:' + #timesForWithdrawal")
  public void remove(@PathVariable Integer timesForWithdrawal) {
    dictDataService.delete(
        DictTypeEnum.USER_WITHDRAW_LIMIT.getValue(),
        DictDataEnum.WITHDRAW_LIMIT.getLabel() + timesForWithdrawal);
  }

  @PostMapping("/addOrEdit")
  @PreAuthorize("hasAuthority('thirdParty:memberWithdrawLimit:add')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.WITHDRAW, desc = "新增或修改提现次数限制")
  public void addOrEdit(@RequestBody UserWithdrawLimitInfo limitInfo) {
    String dictType = DictTypeEnum.USER_WITHDRAW_LIMIT.getValue();
    String dictLabel =
        DictTypeEnum.USER_WITHDRAW_LIMIT.getValue() + limitInfo.getTimesForWithdrawal();
    dictDataService.addOrUpdateUserWithdrawLimit(dictType, dictLabel, limitInfo);
  }

  @PostMapping("/page")
  @PreAuthorize("hasAuthority('thirdParty:memberWithdrawLimit:page')")
  public IPage<MemberWithdrawDictDataVo> queryPage(Page<SysDictData> page) {
    return dictDataService.queryWithdrawPage(page);
  }
}
