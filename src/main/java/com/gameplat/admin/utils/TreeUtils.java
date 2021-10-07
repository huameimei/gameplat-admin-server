package com.gameplat.admin.utils;

public class TreeUtils {
    /**
     * 用于检查Update，Delete等SQL语句是否产生了影响，没产生影响时将抛出异常
     *
     * @param rows 影响行数
     * @throws IllegalArgumentException 如果没有影响任何行
     */
    public static void checkEffective(int rows) {
        if (rows <= 0) {
            throw new IllegalArgumentException();
        }
    }

    public static void checkPositive(Long value, String valname) {
        if (value <= 0) {
            throw new IllegalArgumentException("参数" + valname + "必须是正数:" + value);
        }
    }

    public static void checkPositive(int value, String valname) {
        if (value <= 0) {
            throw new IllegalArgumentException("参数" + valname + "必须是正数:" + value);
        }
    }

    public static void checkNotNegative(Long value, String valname) {
        if (value < 0) {
            throw new IllegalArgumentException("参数" + valname + "不能为负:" + value);
        }
    }

    public static void checkNotNegative(int value, String valname) {
        if (value < 0) {
            throw new IllegalArgumentException("参数" + valname + "不能为负:" + value);
        }
    }

}
