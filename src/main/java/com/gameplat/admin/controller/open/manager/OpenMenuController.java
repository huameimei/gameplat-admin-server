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
import com.gameplat.model.entity.sys.SysMenu;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "菜单管理")
@Slf4j
@RestController
@RequestMapping("/api/admin/manager/menu")
public class OpenMenuController {

  @Autowired private SysMenuService menuService;

  @ApiOperation("查询")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('system:menu:view')")
  public List<SysMenu> list(MenuDTO dto) {
    return menuService.selectMenuList(dto);
  }

  @ApiOperation("添加")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('system:menu:add')")
  @Log(module = ServiceName.ADMIN_SERVICE, desc = "添加菜单")
  public void save(@Validated(Groups.INSERT.class) @RequestBody OperMenuDTO dto) {
    if (StringUtils.isBlank(dto.getMenuName())
        && !SysMenuEnums.TYPE.BUTTON.match(dto.getMenuType())) {
      throw new ServiceException("菜单名称不能为空");
    }

    menuService.insertMenu(dto);
  }

  @ApiOperation("编辑")
  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('system:menu:edit')")
  public void update(@Validated(Groups.UPDATE.class) @RequestBody OperMenuDTO dto) {
    if (StringUtils.isBlank(dto.getMenuName())
        && !SysMenuEnums.TYPE.BUTTON.match(dto.getMenuType())) {
      throw new ServiceException("菜单名称不能为空");
    }

    menuService.updateMenu(dto);
  }

  @ApiOperation("删除")
  @DeleteMapping("/delete")
  @PreAuthorize("hasAuthority('system:menu:remove')")
  public void remove(@RequestBody List<Long> ids) {
    menuService.deleteMenuById(ids);
  }

  @ApiOperation("检查菜单名称是否唯一")
  @GetMapping("/checkMenuNameUnique")
  public boolean checkMenuNameUnique(OperMenuDTO dto) {
    Assert.notNull(dto.getParentId(), "缺少参数");
    Assert.notEmpty(dto.getMenuName(), "缺少参数");
    return menuService.checkMenuNameUnique(dto);
  }
}
