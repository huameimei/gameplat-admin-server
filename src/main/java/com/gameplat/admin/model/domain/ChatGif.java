package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lily
 * @description git
 * @date 2022/2/10
 */
@Data
@TableName("chat_gif")
public class ChatGif implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "关键词")
    private String name;

    @ApiModelProperty(value = "存储文件名")
    private String storeFileName;

    @ApiModelProperty(value = "图片地址")
    private String fileUrl;

    private Integer height;

    private Integer width;

    @ApiModelProperty(value = "使用次数")
    private Integer userCount;

    @ApiModelProperty(value = "md5值")
    private String md5;

    @ApiModelProperty(value = "服务器名称")
    private String serviceFileName;

    @ApiModelProperty(value = "服务提供商（1.本地 2.minio 3.七牛云 4.阿里云 5.腾讯云）")
    private Integer serviceProvider;

    @ApiModelProperty(value = "文件类型")
    private String fileType;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
