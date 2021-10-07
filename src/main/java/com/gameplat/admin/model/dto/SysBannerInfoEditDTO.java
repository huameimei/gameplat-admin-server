package com.gameplat.admin.model.dto;

import com.gameplat.common.model.entity.BaseEntity;
import lombok.Data;

@Data
public class SysBannerInfoEditDTO extends BaseEntity {

    private Integer bannerType;

    private String childName;

    private String childType;

    private String pcPicUrl;

    private String appPicUrl;

    private Integer status;
}
