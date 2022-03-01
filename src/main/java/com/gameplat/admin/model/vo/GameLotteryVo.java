package com.gameplat.admin.model.vo;

import lombok.Data;
import java.io.Serializable;

/**
 * @Author kb
 * @Date 2022/3/1 19:38
 * @Version 1.0
 */
@Data
public class GameLotteryVo implements Serializable {


    /** 彩票编码 */
    private String lotteryCode;

    /** 彩票名称 */
    private String lotteryName;

}
