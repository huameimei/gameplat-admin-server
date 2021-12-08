package com.gameplat.admin.model.vo;

import java.util.Date;
import lombok.Data;

/**
 * 短信VO
 * @author three
 */
@Data
public class SMSVO {

    private Long id;
    private Integer operator;
    private String operatorName;
    private Integer smsType;
    private String phone;
    private String validCode;
    private String content;
    private String requestIp;
    private Date expireDate;
    private Integer status;
    private String message;
    private Date createTime;

}
