package com.gameplat.admin.model.bean;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author kb @Date 2022/6/29 19:29 @Version 1.0
 */
@Data
public class MemberRechBatchListener extends AnalysisEventListener<RechargeMemberFileBean> {

  private List<RechargeMemberFileBean> voList = new ArrayList<RechargeMemberFileBean>();

  public MemberRechBatchListener() {
    super();
    voList.clear();
  }

  /**
   * 每一条数据解析都会调用
   */
  @Override
  public void invoke(RechargeMemberFileBean vo, AnalysisContext context) {
    voList.add(vo);
  }

  /**
   * 所有数据解析完成都会调用
   */
  @Override
  public void doAfterAllAnalysed(AnalysisContext context) {
    voList.forEach(System.out::println);
  }
}
