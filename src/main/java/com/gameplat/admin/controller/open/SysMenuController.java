package com.gameplat.admin.controller.open;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gameplat.admin.convert.SysMenuConvert;
import com.gameplat.admin.enums.AdminTypeEnum;
import com.gameplat.admin.interceptor.Session;
import com.gameplat.admin.model.entity.SysMenu;
import com.gameplat.admin.model.entity.SysUser;
import com.gameplat.admin.model.vo.MetaVo;
import com.gameplat.admin.model.vo.SysMenuVo;
import com.gameplat.admin.model.vo.SysMyMenuVo;
import com.gameplat.admin.service.SysMenuService;
import com.gameplat.admin.service.SysUserService;
import com.gameplat.common.util.BeanUtils;
import com.gameplat.common.web.Result;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/menus")
@Slf4j
public class SysMenuController {

  @Autowired private SysMenuService sysMenuService;

  @Autowired private SysUserService sysUserService;

  @Autowired private SysMenuConvert sysMenuConvert;

  @DeleteMapping("/removeById")
  public void remove(@RequestBody Long id) {
    sysMenuService.remove(new QueryWrapper<SysMenu>().eq("id", id));
  }

  @RequestMapping("/findMenusByRoleId")
  public Result findMenusByRoleId(@Session Long adminId, @RequestBody Long roleId) {
    List<Map<String, Object>> authTrees = new ArrayList<>();
    SysUser admin = this.sysUserService.getById(adminId);
    if (AdminTypeEnum.isAdmin(admin.getUserType())) {
      List<SysMenu> menus =
          sysMenuService.list().stream()
              .filter(s -> s.getHidden() == 0)
              .collect(Collectors.toList());
      List<SysMenuVo> sysMenus = TreeBuilderNoButton(menus);
      List<SysMyMenuVo> sysMyMenuVOS = BeanUtils.mapList(sysMenus, SysMyMenuVo.class);
      return Result.succeed(sysMyMenuVOS,"操作成功");
    }
    return Result.succeed(authTrees);
  }

  public List<SysMenuVo> TreeBuilderNoButton(List<SysMenu> sysMenus) {
    // 数据格式转换
    List<SysMenuVo> sysMenuVoList = sysMenus.stream().map(i -> sysMenuConvert.toVo(i)).collect(Collectors.toList());
    List<SysMenuVo> menus = new ArrayList<>();
    for (SysMenuVo sysMenu : sysMenuVoList) {
      // 顶级菜单
      if (ObjectUtils.equals(-1L, sysMenu.getParentId())) {
        menus.add(sysMenu);
      }
      MetaVo metaVo = new MetaVo();
      metaVo.setIcon(sysMenu.getIcon());
      metaVo.setTitle(sysMenu.getTitle());
      sysMenu.setMeta(metaVo);
      for (SysMenuVo menu : sysMenuVoList) {
        if (menu.getType() == 0) { // 如果是目录层级（布局设置成菜单名称）
          menu.setComponent(menu.getPath());
          menu.setHidden(menu.getHidden());
        }
        if (menu.getType() != 2) {
          if (menu.getParentId().equals(sysMenu.getId())) {
            if (sysMenu.getSubMenus() == null) {
              sysMenu.setSubMenus(new ArrayList<>());
            }
            metaVo = new MetaVo();
            metaVo.setIcon(menu.getIcon());
            metaVo.setTitle(menu.getTitle());
            menu.setMeta(metaVo);
            sysMenu.getSubMenus().add(menu);
          }
        }
      }
    }
    return menus;
  }

  @DeleteMapping("delete/{id}")
  public void deleteMenu(@PathVariable("id") Long id){
    sysMenuService.deleteMenu(id);
  }
}
