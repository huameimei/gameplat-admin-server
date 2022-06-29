package com.gameplat.admin.model.bean;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.gameplat.admin.model.vo.MemberRechBalanceVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author kb @Date 2022/6/29 19:29 @Version 1.0
 */
@Data
public class MemberRechListener extends AnalysisEventListener<MemberRechBalanceVO> {

  private List<MemberRechBalanceVO> voList = new ArrayList<MemberRechBalanceVO>();

  public MemberRechListener() {
    super();
    voList.clear();
  }

  /**
   * 每一条数据解析都会调用
   */
  @Override
  public void invoke(MemberRechBalanceVO vo, AnalysisContext context) {
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
