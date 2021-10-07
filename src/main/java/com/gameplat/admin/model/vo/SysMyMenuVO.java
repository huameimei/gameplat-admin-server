package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.dozer.Mapping;

import java.io.Serializable;
import java.util.List;

/**
 * @Author bhf @Description 返回当前用户菜单实体类 @Date 2020/5/19 9:29
 */
@Data
public class SysMyMenuVO implements Serializable {

    private static final long serialVersionUID = -4976033152027517481L;

    @Mapping(value = "path")
    @ApiModelProperty(value = "菜单路径", required = true)
    private String path;

    @Mapping(value = "path")
    @ApiModelProperty(value = "名称", required = true)
    private String name;

    @Mapping(value = "hidden")
    @ApiModelProperty(value = "是否隐藏", required = true)
    private Integer hidden;

    @Mapping(value = "meta")
    @ApiModelProperty(value = "样式", required = true)
    private MetaVO meta;

  /* @Mapping(value = "redirect")
  @ApiModelProperty(value = "重定向", required = true)
  private String redirect;*/

    @Mapping(value = "subMenus")
    @ApiModelProperty(value = "子菜单", required = true)
    private List<SysMyChildMenuVO> children;
}
