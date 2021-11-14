package com.gameplat.admin.model.vo;

import lombok.Data;

import java.util.Date;

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
    private Date createTime;
    private String remark;
    private Integer dictSort;
}