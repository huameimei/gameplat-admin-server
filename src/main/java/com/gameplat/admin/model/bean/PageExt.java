package com.gameplat.admin.model.bean;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PageExt<T,E> extends Page<T> {

  private E otherData;

  public PageExt(IPage<T> page, E otherData) {
    this.size = page.getSize();
    this.total= page.getTotal();
    this.records =page.getRecords();
    this.current = page.getCurrent();
    this.otherData = otherData;
  }
}
