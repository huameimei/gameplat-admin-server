package com.gameplat.admin.controller.open.manager;

import com.gameplat.admin.enums.SysMenuEnums;
import com.gameplat.admin.model.domain.SysMenu;
import com.gameplat.admin.model.dto.MenuDTO;
import com.gameplat.admin.model.dto.OperMenuDTO;
import com.gameplat.admin.service.SysMenuService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.group.Groups;
import com.gameplat.log.annotation.Log;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 菜单管理
 *
 * @author three
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/manager/menu")
public class OpenMenuController {

  @Autowired private SysMenuService menuService;

  @GetMapping("/list")
  @PreAuthorize("hasAuthority('system:menu:view')")
  public List<SysMenu> list(MenuDTO dto) {
    return menuService.selectMenuList(dto);
  }

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

  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('system:menu:edit')")
  public void update(@Validated(Groups.UPDATE.class) @RequestBody OperMenuDTO dto) {
    if (StringUtils.isBlank(dto.getMenuName())
        && !SysMenuEnums.TYPE.BUTTON.match(dto.getMenuType())) {
      throw new ServiceException("菜单名称不能为空");
    }

    menuService.updateMenu(dto);
  }

  @DeleteMapping("/delete")
  @PreAuthorize("hasAuthority('system:menu:remove')")
  public void remove(@RequestBody List<Long> ids) {
    menuService.deleteMenuById(ids);
  }

  @GetMapping("/checkMenuNameUnique")
  public boolean checkMenuNameUnique(OperMenuDTO dto) {
    if (StringUtils.isBlank(dto.getMenuName())) {
      throw new ServiceException("缺少参数");
    }

    if (StringUtils.isNull(dto.getParentId())) {
      throw new ServiceException("缺少参数");
    }

    return menuService.checkMenuNameUnique(dto);
  }
}
