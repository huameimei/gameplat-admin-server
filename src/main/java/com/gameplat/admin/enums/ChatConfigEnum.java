package com.gameplat.admin.enums;

import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.enums.DictTypeEnum;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum ChatConfigEnum {

    CHAT_ROOM_SIDE_MENU(DictTypeEnum.CHAT_SIDE, "CHAT_ROOM_SIDE_MENU", "聊天室侧边菜单默認配置"),
    CHAT_ROOM_SETTING_MENU(DictTypeEnum.CHAT_SIDE, "CHAT_ROOM_SETTING_MENU", "聊天室侧边菜单配置"),
    CHAT_ROOM_CONFIG(DictTypeEnum.CHAT_CONFIG, "CHAT_ROOM_CONFIG", "聊天室配置"),
    CHAT_ROOM_WEB_FLOAT(DictTypeEnum.CHAT_SIDE, "CHAT_ROOM_WEB_FLOAT", "聊天室PC端浮窗配置"),
    CHAT_UPDATE_AVATAR(DictTypeEnum.CHAT_UPDATE_AVATAR, "CHAT_UPDATE_AVATAR", "聊天室修改头像列表"),
    CHAT_PUSH_CP_WIN(DictTypeEnum.CHAT_CONFIG, "CHAT_PUSH_CP_WIN", "聊天室彩票中奖推送设置"),
    CHAT_PUSH_CP_BET(DictTypeEnum.CHAT_CONFIG, "CHAT_PUSH_CP_BET", "聊天室彩票下注推送配置");

    private final String label;
    private final String name;
    private final DictTypeEnum type;

    private ChatConfigEnum(DictTypeEnum type, String lable, String name) {
        this.type = type;
        this.label = lable;
        this.name = name;
    }

    public String getLabel() {
        return this.label;
    }

    public String getName() {
        return this.name;
    }

    public DictTypeEnum getType() {
        return this.type;
    }

    public static final Map<String, ChatConfigEnum> map =
            Arrays.stream(ChatConfigEnum.values()).collect(Collectors.toMap(k -> k.label, v -> v));

    public static ChatConfigEnum getEnumByLabel(String label) {
        if (!map.containsKey(label)) {
            throw new ServiceException("未知的配置编码: " + label);
        }
        return map.get(label);
    }
}
