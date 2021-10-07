package com.gameplat.admin.enums;

import java.util.Optional;
import java.util.stream.Stream;

public enum BizBlacklistStatusEnum {
    DISABLED(0, "禁用"),
    ENABLED(1, "启用");
    private int value;
    private String text;

    BizBlacklistStatusEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public static Optional<BizBlacklistStatusEnum> matches(Integer value) {
        return Optional.ofNullable(value)
                .map(
                        v ->
                                Stream.of(BizBlacklistStatusEnum.values())
                                        .filter(e -> e.getValue() == v)
                                        .findAny()
                                        .orElse(null));
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
