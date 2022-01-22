package com.gameplat.admin.controller.open.thirdParty;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.domain.DiscountType;
import com.gameplat.admin.model.dto.DiscountTypeAddDTO;
import com.gameplat.admin.model.dto.DiscountTypeEditDTO;
import com.gameplat.admin.service.DiscountTypeService;
import com.gameplat.base.common.enums.EnableEnum;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/thirdParty/discountType")
public class DiscountTypeController {

  @Autowired
  private DiscountTypeService discountTypeService;

  @DeleteMapping("/remove/{id}")
  @PreAuthorize("hasAuthority('thirdParty:discountType:remove')")
  public void remove(@PathVariable Long id) {
    discountTypeService.delete(id);
  }

  @PostMapping("/add")
  @PreAuthorize("hasAuthority('thirdParty:discountType:add')")
  public void add(@RequestBody DiscountTypeAddDTO dto) {
    discountTypeService.save(dto);
  }

  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('thirdParty:discountType:edit')")
  public void edit(@RequestBody DiscountTypeEditDTO dto) {
    discountTypeService.update(dto);
  }

  @PostMapping("/editStatus")
  @PreAuthorize("hasAuthority('thirdParty:discountType:editStatus')")
  public void updateStatus(Long id, Integer status) {
    discountTypeService.updateStatus(id, status);
  }

  @PostMapping("/page")
  @PreAuthorize("hasAuthority('thirdParty:discountType:page')")
  public IPage<DiscountType> queryPage(Page<DiscountType> page) {
    return discountTypeService.findDiscountTypePage(page);
  }

  @GetMapping("/queryAll")
  @PreAuthorize("hasAuthority('thirdParty:discountType:queryAll')")
  public List<DiscountType> queryList() {
    return discountTypeService.lambdaQuery().eq(DiscountType::getStatus, EnableEnum.ENABLED.code()).list();
  }

}
