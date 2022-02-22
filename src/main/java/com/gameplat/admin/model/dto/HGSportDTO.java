package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@Data
public class HGSportDTO implements Serializable {

    private static final long serialVersionUID = -8970711682296680702L;

    @ApiModelProperty(value = "false 未结算 true 结算")
    private Boolean isHistory;

    @ApiModelProperty(value = "用户类型")
    private String userTypes;

    @ApiModelProperty(value = "用户类型")
    private String userType;

    @ApiModelProperty(value = "比赛时间")
    private String gameDate;

    @ApiModelProperty(value = "结算时间")
    private String startSettledDate;

    @ApiModelProperty(value = "会员账号")
    private String userAccount;

    @ApiModelProperty(value = "订单ID")
    private String orderId;

    @ApiModelProperty(value = "联赛名")
    private String league;

    @ApiModelProperty(value = "主队")
    private String teamH;

    @ApiModelProperty(value = "客队")
    private String teamC;

    @ApiModelProperty(value = "结算状态")
    private String settleStatus;

    @ApiModelProperty(value = "是否模糊查询主队")
    private Boolean queryLikeTeamH;

    @ApiModelProperty(value = "是否模糊查询客队")
    private Boolean queryLikeTeamC;

    @ApiModelProperty(value = "是否模糊查询联赛")
    private Boolean queryLikeLeague;

    @ApiModelProperty(value = "当前记录起始索引  默认0")
    private Integer pageNum;

    @ApiModelProperty(value = "每页显示记录数 默认10")
    private Integer pageSize;

    private String username;

    private Integer gameType;

    private String resultType;

    private String startDate;

    private String endDate;

    private Integer timeType;

    private String teamHName;

    private String teamCName;

    private String entryType;

    private String gameSportType;

    private String type;

    private String subType;

    private String sportType;

    private String betType;

    private Integer gid;

    private String beginTime;

    private String endTime;

    private String teamName;

    private Double cutOdds;

    private String isCreate;

    private String status;

    private String leagueML;

    private String teamHML;

    private String teamCML;

    private String viewAll;

    private Integer configId;

    private String configName;

    private String configKey;

    private String configValue;

    private Integer configType;

    private String configRemark;

    private Integer configSort;

    private Integer enableStatus;

    private String enlargeMemo;

    private String id;

    private String updateDate;

    private Integer hHalf;

    private Integer cHalf;

    private String hFull;

    private String cFull;

    private String hHalfStr;

    private String cHalfStr;

    private String hFullStr;

    private String cFullStr;

    private Integer halfSize;

    private Integer fullSize;

    private String settled;

    private String tempHfull;

    private String tempCfull;

    private String tempHhalf;

    private String tempChalf;

    private String hfirst;

    private String cfirst;

    private String hsecond;

    private String csecond;

    private String hthird;

    private String cthird;

    private String hforth;

    private String cforth;

    private String hnext;

    private String cnext;

    private String hdelay;

    private String cdelay;

    private String hfirstStr;

    private String hsecondStr;

    private String hthirdStr;

    private String hforthStr;

    private String hnextStr;

    private String hdelayStr;

    private String cfirstStr;

    private String csecondStr;

    private String cthirdStr;

    private String cforthStr;

    private String cnextStr;

    private String cdelayStr;

    private Integer firstSize;

    private Integer secondSize;

    private Integer thirdSize;

    private Integer forthSize;

    private Integer nextSize;

    private Integer delaySize;

    private Integer money;

    private String comprehensiveMinLimit;
    private String comprehensiveMaxLimit;
    private String maintainStartDateTime;
    private String maintainEndDateTime;
    private String blackList;
    private Double blackMaxLimitMoney;
    private Integer ftRMinLimit;
    private Integer ftRMaxLimit;
    private Integer ftSMinLimit;
    private Integer ftSMaxLimit;
    private Integer ftHPMinLimit;
    private Integer ftHPMaxLimit;
    private Integer ftPMinLimit;
    private Integer ftPMaxLimit;
    private Integer ftTMinLimit;
    private Integer ftTMaxLimit;
    private Integer ftHTMinLimit;
    private Integer ftHTMaxLimit;
    private Integer ftCMinLimit;
    private Integer ftCMaxLimit;
    private Integer ftMSMinLimit;
    private Integer ftMSMaxLimit;
    private Integer ftMHPMinLimit;
    private Integer ftMHPMaxLimit;
    private Integer ftMPMinLimit;
    private Integer ftMPMaxLimit;
    private Integer ftMTMinLimit;
    private Integer ftMTMaxLimit;
    private Integer ftMHTMinLimit;
    private Integer ftMHTMaxLimit;
    private Integer ftMCMinLimit;
    private Integer ftMCMaxLimit;
    private Integer ftMFMinLimit;
    private Integer ftMFMaxLimit;
    private Integer bkRunFirstMinLimit;
    private Integer bkRunFirstMaxLimit;
    private Integer bkRunSecondMinLimit;
    private Integer bkRunSecondMaxLimit;
    private Integer bkRunThirdMinLimit;
    private Integer bkRunThirdMaxLimit;
    private Integer bkRunFourthMinLimit;
    private Integer bkRunFourthMaxLimit;
    private Integer bkRMinLimit;
    private Integer bkRMaxLimit;
    private Integer bkSMinLimit;
    private Integer bkSMaxLimit;
    private Integer bkHPMinLimit;
    private Integer bkHPMaxLimit;
    private Integer bkPMinLimit;
    private Integer bkPMaxLimit;
    private Integer bkTMinLimit;
    private Integer bkTMaxLimit;
    private Integer bkHTMinLimit;
    private Integer bkHTMaxLimit;
    private Integer bkCMinLimit;
    private Integer bkCMaxLimit;
    private Integer bkMSMinLimit;
    private Integer bkMSMaxLimit;
    private Integer bkMHPMinLimit;
    private Integer bkMHPMaxLimit;
    private Integer bkMPMinLimit;
    private Integer bkMPMaxLimit;
    private Integer bkMTMinLimit;
    private Integer bkMTMaxLimit;
    private Integer bkMHTMinLimit;
    private Integer bkMHTMaxLimit;
    private Integer bkMCMinLimit;
    private Integer bkMCMaxLimit;
    private Integer bkMFMinLimit;
    private Integer bkMFMaxLimit;
    private Integer ftWHOneTwo;
    private Integer ftWHMaxMin;
    private Integer ftWHRQ;
    private Integer ftWHBD;
    private Integer ftWOneTwo;
    private Integer ftWMaxMin;
    private Integer ftWMRQ;
    private Integer ftWMDS;
    private Integer ftWMBD;
    private Integer ftWMZRQ;
    private Integer ftWMBQC;
    private Integer ftWMZH;
    private Integer ftWMGJ;
    private Integer bkWHOneTwo;
    private Integer bkWHMaxMin;
    private Integer bkWHRQ;
    private Integer bkWHBD;
    private Integer bkWOneTwo;
    private Integer bkWMaxMin;
    private Integer bkWMRQ;
    private Integer bkWMDS;
    private Integer bkWMBD;
    private Integer bkWMZRQ;
    private Integer bkWMBQC;
    private Integer bkWMZH;
    private Integer bkWMGJ;
    private Integer chuanMinNumber;
    private Integer chuanMaxNumber;
    private String FKey;
    private String message;
    private List<String> ordersList;
    private String key;
    private String sportsType;
}
