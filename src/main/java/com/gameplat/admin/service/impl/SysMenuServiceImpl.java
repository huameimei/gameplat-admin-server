package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.dao.SysMenuMapper;
import com.gameplat.admin.model.entity.SysMenu;
import com.gameplat.admin.service.SysMenuService;
import org.springframework.stereotype.Service;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements
    SysMenuService {

}
