package com.gameplat.admin.controller.open.payment;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.DiscountTypeAddDTO;
import com.gameplat.admin.model.dto.DiscountTypeEditDTO;
import com.gameplat.admin.model.vo.DiscountTypeVO;
import com.gameplat.admin.service.DiscountTypeService;
import com.gameplat.base.common.enums.EnableEnum;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.DiscountType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "充值优惠")
@RestController
@RequestMapping("/api/admin/thirdParty/discountType")
public class DiscountTypeController {

  @Autowired private DiscountTypeService discountTypeService;

  @Operation(summary = "删除")
  @PostMapping("/remove/{id}")
  @PreAuthorize("hasAuthority('thirdParty:discountType:remove')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'删除：' + #id")
  public void remove(@PathVariable Long id) {
    discountTypeService.delete(id);
  }

  @Operation(summary = "添加")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('thirdParty:discountType:add')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'新增优惠类型'")
  public void add(@RequestBody DiscountTypeAddDTO dto) {
    discountTypeService.save(dto);
  }

  @Operation(summary = "编辑")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('thirdParty:discountType:edit')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'修改优惠类型'")
  public void edit(@RequestBody DiscountTypeEditDTO dto) {
    discountTypeService.update(dto);
  }

  @Operation(summary = "编辑状态")
  @PostMapping("/editStatus")
  @PreAuthorize("hasAuthority('thirdParty:discountType:editStatus')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'修改优惠状态：' + #status")
  public void updateStatus(Long id, Integer status) {
    discountTypeService.updateStatus(id, status);
  }

  @Operation(summary = "分页获取")
  @PostMapping("/page")
  @PreAuthorize("hasAuthority('thirdParty:discountType:view')")
  public IPage<DiscountTypeVO> queryPage(Page<DiscountType> page) {
    return discountTypeService.findDiscountTypePage(page);
  }

  @Operation(summary = "获取全部")
  @GetMapping("/queryAll")
  //  @PreAuthorize("hasAuthority('thirdParty:discountType:queryAll')")
  public List<DiscountType> queryList() {
    List<DiscountType> list = discountTypeService
        .lambdaQuery()
        .eq(DiscountType::getStatus, EnableEnum.ENABLED.code())
        .orderByAsc(DiscountType :: getSort)
        .list();
    return list;
  }
}
