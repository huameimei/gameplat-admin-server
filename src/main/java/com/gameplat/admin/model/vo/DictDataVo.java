package com.gameplat.admin.model.vo;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import lombok.Data;

/**
 * 字典数据Vo
 *
 * @author three
 */
@Data
public class DictDataVo {

    private Long id;
    private String dictName;
    private String dictType;
    private String dictLabel;
    private String dictValue;
    private Integer status;
    @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date createTime;
    private String remark;
    private Integer dictSort;
}
