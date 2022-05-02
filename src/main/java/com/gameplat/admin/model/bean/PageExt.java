package com.gameplat.admin.model.bean;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页扩展
 *
 * @author robben
 * @param <T>
 * @param <E>
 */
@Deprecated
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PageExt<T, E> extends Page<T> {

  private E otherData;

  public PageExt(IPage<T> page, E otherData) {
    this.size = page.getSize();
    this.total = page.getTotal();
    this.records = page.getRecords();
    this.current = page.getCurrent();
    this.otherData = otherData;
  }

  public PageExt(long current, long size, long total, List<T> records, E otherData) {
    this.current = current;
    this.size = size;
    this.total = total;
    this.records = records;
    this.otherData = otherData;
  }

  public static <T, E> PageExt<T, E> of(
      long current, long size, long total, List<T> records, E otherData) {
    return new PageExt<>(current, size, total, records, otherData);
  }
}
