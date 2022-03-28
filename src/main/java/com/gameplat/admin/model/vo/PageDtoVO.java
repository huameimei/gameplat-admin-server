package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Lenovo
 */
@Data
public class PageDtoVO<T> implements Serializable {

  private Page<T> page;

  private Map<String, Object> otherData;
}
