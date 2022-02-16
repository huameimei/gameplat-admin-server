package com.gameplat.admin.service;

import com.alibaba.fastjson.JSON;
import com.gameplat.admin.enums.ChatConfigEnum;
import com.gameplat.admin.model.domain.SysDictData;
import com.gameplat.admin.model.vo.ChatSideMenuVO;
import com.gameplat.admin.model.vo.LotteryCodeVo;
import com.gameplat.base.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 聊天室侧滑菜单业务层处理
 */


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class ChatSideMenuService {

    @Autowired
    private SysDictDataService dictDataService;
    @Autowired
    private OtthService otthService;

    /** 侧边菜单列表 */
    public List<ChatSideMenuVO> queryAllSideMenu() {
        //获取聊天室侧边菜单默认配置字典数据
        SysDictData sideMenu = dictDataService.getDictData(ChatConfigEnum.CHAT_ROOM_SIDE_MENU.getType().getValue(), ChatConfigEnum.CHAT_ROOM_SIDE_MENU.getLabel());
        //初始化菜单
        List<ChatSideMenuVO> initMenu = JSON.parseArray(sideMenu.getDictValue(), ChatSideMenuVO.class);
        List<LotteryCodeVo> gameAll = new ArrayList<>();
        try {
            //获取游戏列表
            gameAll = otthService.getLottTypeList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        gameAll.forEach(item -> {
            ChatSideMenuVO chatSideMenu = new ChatSideMenuVO();
            chatSideMenu.setType(item.getLottCode());
            chatSideMenu.setName(item.getLottName());
            chatSideMenu.setOpen(0);
            initMenu.add(chatSideMenu);
        });
        List<ChatSideMenuVO> result;
        //获取聊天室侧边菜单配置字典数据
        SysDictData setting = dictDataService.getDictData(ChatConfigEnum.CHAT_ROOM_SETTING_MENU.getType().getValue(), ChatConfigEnum.CHAT_ROOM_SETTING_MENU.getLabel());
        if (StringUtils.isNoneBlank(setting.getDictValue()) && setting.getDictValue() != "[]") {
            List<ChatSideMenuVO> settingMenuList = JSON.parseArray(setting.getDictValue(), ChatSideMenuVO.class);
            List<String> menuNames = settingMenuList.stream().map(ChatSideMenuVO::getName).collect(Collectors.toList());
            List<String> initNames = initMenu.stream().map(ChatSideMenuVO::getName).collect(Collectors.toList());
            // 添加新增的数据
            List<ChatSideMenuVO> newMenu = initMenu.stream()
                    .filter(m -> !menuNames.contains(m.getName()))
                    .peek(m -> {
                        m.setSort(0);
                        m.setRemark("");
                        m.setOpen(0);
                    }).collect(Collectors.toList());
            settingMenuList.addAll(newMenu);
            // 删除不存在的数据
            result = settingMenuList.stream()
                    .filter(distinctByKey(ChatSideMenuVO::getName))
                    .filter(m -> initNames.contains(m.getName()))
                    .collect(Collectors.toList());
            // 更新
            for (ChatSideMenuVO menu : result) {
                initMenu.stream()
                        .filter(m -> Objects.equals(m.getName(), menu.getName()))
                        .findAny().ifPresent(_im -> menu.setType(_im.getType()));
            }
        } else {
            // 初始化
            result = initMenu.stream()
                    .peek(m -> {
                        m.setSort(0);
                        m.setRemark("");
                    })
                    .collect(Collectors.toList());
            for (int i = 0; i < result.size(); i++) {
                result.get(i).setSort(i + 1);
            }
        }
        return result;
    }

    /** 修改侧边栏 */
    public void edit(String config){
        List<ChatSideMenuVO> settingMenuList = JSON.parseArray(config, ChatSideMenuVO.class);
        settingMenuList = settingMenuList.stream().filter(x -> x.getOpen() == 1).collect(Collectors.toList());
        String initDoc = dictDataService
                .getDictData(ChatConfigEnum.CHAT_ROOM_SIDE_MENU.getType().getValue(), ChatConfigEnum.CHAT_ROOM_SIDE_MENU.getLabel())
                .getDictValue();
        List<ChatSideMenuVO> initMenu = JSON.parseArray(initDoc, ChatSideMenuVO.class);
        List<String> menuNames = settingMenuList.stream().map(ChatSideMenuVO::getName).collect(Collectors.toList());
        List<ChatSideMenuVO> newMenu = initMenu.stream()
                .filter(m -> !menuNames.contains(m.getName()))
                .peek(m -> {
                    m.setSort(0);
                    m.setRemark("");
                    m.setOpen(0);
                }).collect(Collectors.toList());
        settingMenuList.addAll(newMenu);
        settingMenuList.sort(Comparator.comparingInt(ChatSideMenuVO::getSort));
        String jsonObj = JSON.toJSONString(settingMenuList);
        dictDataService.updateByTypeAndLabel(new SysDictData(){{
            setDictType(ChatConfigEnum.CHAT_ROOM_SETTING_MENU.getType().getValue());
            setDictLabel(ChatConfigEnum.CHAT_ROOM_SETTING_MENU.getLabel());
            setDictValue(jsonObj);
        }});
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
