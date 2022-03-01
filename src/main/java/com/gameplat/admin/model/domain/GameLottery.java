package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author kb
 * @Date 2022/3/1 19:38
 * @Version 1.0
 */
@Data
@TableName("game_kgnl_lottery_mapper")
public class GameLottery implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;


    /** 彩票编码 */
    private String lotteryCode;

    /** 彩票名称 */
    private String lotteryName;

    /**
     * 彩票类型（1 官彩 2私彩 3香港彩 ）
     */
    private int lotteryType;
}
