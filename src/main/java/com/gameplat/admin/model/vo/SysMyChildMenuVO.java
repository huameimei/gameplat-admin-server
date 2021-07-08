package com.gameplat.admin.model.vo;

import java.io.Serializable;
import java.util.List;
import lombok.Data;
import org.dozer.Mapping;

/** 返回子菜单实体类 */
@Data
public class SysMyChildMenuVO implements Serializable {

  @Mapping(value = "path")
  private String path;

  @Mapping(value = "component")
  private String component;

  @Mapping(value = "name")
  private String name;

  @Mapping(value = "hidden")
  private boolean hidden;

  @Mapping(value = "meta")
  private MetaVO meta;

  @Mapping(value = "subMenus")
  private List<SysMyChildMenuVO> children;
}
