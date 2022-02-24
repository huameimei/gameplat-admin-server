package com.gameplat.admin.util.lottery;

/**
 * 检查参数
 */
public class ParamCheck {
    private ParamCheck() {
    }

    /**
     * 检查两个参数是否 <i>同时为空 或 同时传参</i>
     *
     * @param paramA 参数A
     * @param paramB 参数B
     * @param errMsg 错误信息
     */
    public static void together(Object paramA, Object paramB, String errMsg) {
        if ((paramA == null) != (paramB == null)) {
            throw new RuntimeException(errMsg);
        }
    }
    public static void together2(Object paramA, Object paramB, String errMsg) {
        if (paramA == null || paramA.equals("undefined")){
            throw new RuntimeException("您未做任何修改");
        }else if ((paramA == null) != (paramB == null)) {
            throw new RuntimeException(errMsg);
        }
    }
}
