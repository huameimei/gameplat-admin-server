package com.gameplat.admin.controller.open.manager;

import com.gameplat.admin.enums.SysMenuEnums;
import com.gameplat.admin.model.dto.MenuDTO;
import com.gameplat.admin.model.dto.OperMenuDTO;
import com.gameplat.admin.service.SysMenuService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.group.Groups;
import com.gameplat.common.lang.Assert;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.sys.SysMenu;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "菜单管理")
@RestController
@RequestMapping("/api/admin/manager/menu")
public class OpenMenuController {

  @Autowired private SysMenuService menuService;

  @Operation(summary = "查询")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('system:menu:view')")
  public List<SysMenu> list(MenuDTO dto) {
    return menuService.selectMenuList(dto);
  }

  @Operation(summary = "添加")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('system:menu:add')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'菜单管理-->添加' + #dto" )
  public void save(@Validated(Groups.INSERT.class) @RequestBody OperMenuDTO dto) {
    if (StringUtils.isBlank(dto.getMenuName())
        && !SysMenuEnums.TYPE.BUTTON.match(dto.getMenuType())) {
      throw new ServiceException("菜单名称不能为空");
    }

    menuService.insertMenu(dto);
  }

  @Operation(summary = "编辑")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('system:menu:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'菜单管理-->编辑' + #dto" )
  public void update(@Validated(Groups.UPDATE.class) @RequestBody OperMenuDTO dto) {
    if (StringUtils.isBlank(dto.getMenuName())
        && !SysMenuEnums.TYPE.BUTTON.match(dto.getMenuType())) {
      throw new ServiceException("菜单名称不能为空");
    }

    menuService.updateMenu(dto);
  }

  @Operation(summary = "删除")
  @PostMapping("/delete")
  @PreAuthorize("hasAuthority('system:menu:remove')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'菜单管理-->删除' + #ids" )
  public void remove(@RequestBody List<Long> ids) {
    menuService.deleteMenuById(ids);
  }

  @Operation(summary = "检查菜单名称是否唯一")
  @GetMapping("/checkMenuNameUnique")
  public boolean checkMenuNameUnique(OperMenuDTO dto) {
    Assert.notNull(dto.getParentId(), "缺少参数");
    Assert.notEmpty(dto.getMenuName(), "缺少参数");
    return menuService.checkMenuNameUnique(dto);
  }
}
