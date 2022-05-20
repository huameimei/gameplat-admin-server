package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.common.util.Date2LongSerializerUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 字典数据Vo
 *
 * @author three
 */
@Data
public class DictDataVo implements Serializable {

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

  private String updateBy;

  private String createBy;

  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date updateTime;
}
