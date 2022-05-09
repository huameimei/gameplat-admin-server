package com.gameplat.admin.controller.open.payment;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.PayTypeAddDTO;
import com.gameplat.admin.model.dto.PayTypeEditDTO;
import com.gameplat.admin.model.vo.PayTypeVO;
import com.gameplat.admin.service.PayTypeService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.pay.PayType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "支付方式")
@RestController
@RequestMapping("/api/admin/thirdParty/payTypes")
public class PayTypeController {

  @Autowired private PayTypeService payTypeService;

  @ApiOperation("删除")
  @PostMapping("/remove/{id}")
  @PreAuthorize("hasAuthority('thirdParty:payTypes:remove')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'删除支付方式id=' + #id")
  public void remove(@PathVariable Long id) {
    payTypeService.delete(id);
  }

  @ApiOperation("获取全部")
  @PostMapping("/list")
  //  @PreAuthorize("hasAuthority('thirdParty:payTypes:view')")
  public List<PayTypeVO> findPayTypes(String name) {
    return payTypeService.queryList(name);
  }

  @ApiOperation("转账类型")
  @GetMapping("/transferQueryList")
  @PreAuthorize("hasAuthority('transferPay:payTypes:view')")
  public List<PayTypeVO> transferQueryList() {
    return payTypeService.payTypeQueryList(1);
  }

  @ApiOperation("在线支付类型")
  @GetMapping("/onlineQueryList")
  @PreAuthorize("hasAuthority('onlinePay:payTypes:view')")
  public List<PayTypeVO> onlineQueryList() {
    return payTypeService.payTypeQueryList(2);
  }

  @ApiOperation("添加")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('thirdParty:payTypes:add')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.RECHARGE,
      desc = "'新增支付方式code=' + #dto.code")
  public void add(@RequestBody PayTypeAddDTO dto) {
    payTypeService.save(dto);
  }

  @ApiOperation("编辑")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('thirdParty:payTypes:edit')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'修改支付方式id=' + #dto.id")
  public void edit(@RequestBody PayTypeEditDTO dto) {
    payTypeService.update(dto);
  }

  @ApiOperation("修改状态")
  @PostMapping("/editStatus")
  @PreAuthorize("hasAuthority('thirdParty:payTypes:editStatus')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'修改支付方式状态id=' + #id")
  public void updateStatus(Long id, Integer status) {
    payTypeService.updateStatus(id, status);
  }

  @ApiOperation("查询")
  @PostMapping("/page")
  @PreAuthorize("hasAuthority('thirdParty:payTypes:view')")
  public IPage<PayType> queryPage(Page<PayType> page) {
    return payTypeService.queryPage(page);
  }

  @ApiOperation("获取可用虚拟币支付")
  @GetMapping("/queryEnableVirtual")
  //  @PreAuthorize("hasAuthority('thirdParty:payTypes:queryEnableVirtual')")
  public List<PayTypeVO> queryEnableVirtual() {
    return payTypeService.queryEnableVirtual();
  }
}
