package com.gameplat.admin.model.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class PayTypeQueryDTO implements Serializable {

    private Integer isSystemCode;

    private Integer status;

    private Integer onlinePayEnabled;
}
