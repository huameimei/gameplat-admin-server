package com.gameplat.admin.model.vo.activity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dozer.Mapping;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 红包雨
 * </p>
 *
 * @author 沙漠
 * @since 2020-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MemberImRedPacketVO implements Serializable {

    private static final long serialVersionUID=1L;

    @JsonSerialize(using= ToStringSerializer.class)
    @ApiModelProperty(value = "主键")
    @Mapping(value = "packetId")
    private Long id;

    @ApiModelProperty(value = "红包雨标题")
    private String realTitle;

    @ApiModelProperty(value = "红包雨位置")
    private String realLocation;

    @ApiModelProperty(value = "频率")
    private String frequency;

    @ApiModelProperty(value = "持续时长")
    private String duration;

    @ApiModelProperty(value = "启动时间(时分秒)")
    private String startTime;

    @ApiModelProperty(value = "终止时间(时分秒)")
    private String stopTime;

    @ApiModelProperty(value = "下红包雨的时间点")
    private List<String> timeList;

}
