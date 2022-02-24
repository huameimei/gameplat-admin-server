package com.gameplat.admin.util.lottery;

import java.util.HashMap;
import java.util.Map;

/**
 * 参数构造器
 */
public class ParamBuilder {
    private ParamBuilder() {}

    /**
     * 构造 Feign 参数 Map
     *
     * @param params 多个参数.
     */
    public static Map<String, Object> build(Object... params) {
        Map<String, Object> paramMap = new HashMap<>();
        if (params == null) {
            return paramMap;
        }
        if (params.length % 2 != 0) {
            throw new RuntimeException("参数数量不对");
        }
        for (int i = 0; i < params.length; i += 2) {
            if (params[i] != null && params[i + 1] != null) {
                paramMap.put(params[i].toString(), params[i + 1]);
            }
        }
        return paramMap;
    }

}
