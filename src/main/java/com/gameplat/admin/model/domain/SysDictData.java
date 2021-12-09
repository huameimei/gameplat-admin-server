package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 字典数据表
 *
 * @author Lenovo
 */
@Data
@TableName("sys_dict_data")
public class SysDictData implements Serializable {

  @ApiModelProperty(value = "字典编码")
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  @ApiModelProperty(value = "字典名称")
  private String dictName;

  @ApiModelProperty(value = "字典标签")
  private String dictLabel;

  @ApiModelProperty(value = "字典键值")
  private String dictValue;

  @ApiModelProperty(value = "字典类型")
  private String dictType;

  @ApiModelProperty(value = "字典排序")
  private Integer dictSort;

  @ApiModelProperty(value = "样式属性（其他样式扩展）")
  private String cssClass;

  @ApiModelProperty(value = "表格回显样式")
  private String listClass;

  @ApiModelProperty(value = "1显示2不显示")
  private Integer isVisible;

  @ApiModelProperty(value = "是否默认（Y是 N否）")
  private String isDefault;

  @ApiModelProperty(value = "状态（1正常 0停用）")
  private Integer status;

  @TableField(fill = FieldFill.INSERT)
  @ApiModelProperty(value = "创建者")
  private String createBy;

  @TableField(fill = FieldFill.INSERT)
  @ApiModelProperty(value = "创建时间")
  private Date createTime;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  @ApiModelProperty(value = "更新者")
  private String updateBy;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  @ApiModelProperty(value = "更新时间")
  private Date updateTime;

  @ApiModelProperty(value = "备注")
  private String remark;
}
