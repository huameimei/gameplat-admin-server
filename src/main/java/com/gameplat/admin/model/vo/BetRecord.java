package com.gameplat.admin.model.vo;

import com.gameplat.elasticsearch.constant.Constants;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: asky
 * @date: 2021/12/10 17:57
 * @desc:
 */
@Data
public class BetRecord implements Serializable {

    @Id
    private String id;

    /**
     * 唯一编码
     */
    @Field(type = FieldType.Keyword, store = true)
    private String billNo;

    /**
     * 租户编码
     */
    private String tenant;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 用户真实姓名
     */
    private String realName;

    /**
     * 用户名
     */
    @Field(type = FieldType.Keyword, store = true,
            analyzer = Constants.ES_ANALYZER.KEYWORD,
            searchAnalyzer = Constants.ES_ANALYZER.KEYWORD)
    private String account;

    /**
     * 游戏账号
     */
    private String gameAccount;

    /** 货币编码 */
    private String currency;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 上级名称
     */
    private String parentName;

    /**
     * 代理路径
     */
    private String superPath;

    /**
     * 平台编码
     */
    @Field(type = FieldType.Keyword, store = true)
    private String platformCode;

    /**
     * 平台游戏种类
     */
    @Field(type = FieldType.Keyword, store = true,
            analyzer = Constants.ES_ANALYZER.KEYWORD,
            searchAnalyzer = Constants.ES_ANALYZER.KEYWORD)
    private String gameKind;

    /**
     * 平台游戏种类名称
     */
    private String gameKindName;

    /**
     * 游戏分类
     */
    @Field(type = FieldType.Keyword, store = true)
    private String gameType;

    /**
     * 游戏编码
     */
    @Field(type = FieldType.Keyword, store = true)
    private String gameCode;

    /**
     * 游戏名称
     */
    private String gameName;

    /**
     * 结算状态
     */
    @Field(type = FieldType.Integer, store = true)
    private Integer settle;

    /**
     * 投注金额
     */
    @Field(type = FieldType.Long, store = true)
    private BigDecimal betAmount;

    /**
     * 有效投注额
     */
    @Field(type = FieldType.Long, store = true)
    private BigDecimal validAmount;

    /**
     * 输赢金额
     */
    @Field(type = FieldType.Long, store = true)
    private BigDecimal winAmount;

    /**
     * 输赢(0输 1赢)
     */
    private Integer loseWin;

    /**
     * 投注内容
     */
    private String betContent;

    /**
     * 体育（电竞）赔率
     */
    private BigDecimal odds;

    /**
     * 彩种大类
     */
    @Field(type = FieldType.Keyword, store = true)
    private Integer lottType;

    /**
     * 盘口类型 1滚球 2今日 3早盘
     */
    @Field(type = FieldType.Keyword, store = true)
    private Integer handicapType;

    /**
     * 体育类型 1足球 2篮球
     */
    @Field(type = FieldType.Keyword, store = true)
    private Integer sportType;

    /**
     * 投注时间
     */
    @Field(type = FieldType.Long, store = true)
    private Date betTime;

    /**
     * 下注美东时间
     */
    @Field(type = FieldType.Long, store = true)
    private Date amesTime;

    /**
     * 结算时间
     */
    @Field(type = FieldType.Long, store = true)
    private Date settleTime;

    /**
     * 报表统计时间(美东)
     */
    @Field(type = FieldType.Long, store = true)
    private Date statTime;

    /**
     * 添加时间
     */
    @Field(type = FieldType.Long, store = true)
    private Date createTime;

    /**
     * 更新时间
     */
    @Field(type = FieldType.Long, store = true)
    private Date updateTime;
}
