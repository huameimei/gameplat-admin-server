package com.gameplat.admin.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberBillConvert;
import com.gameplat.admin.mapper.MemberBillMapper;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.domain.MemberBill;
import com.gameplat.admin.model.dto.MemberBillDTO;
import com.gameplat.admin.model.vo.MemberBillVO;
import com.gameplat.admin.service.MemberBillService;
import com.gameplat.base.common.exception.ServiceException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import lombok.Cleanup;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberBillServiceImpl extends ServiceImpl<MemberBillMapper, MemberBill> implements  MemberBillService {

  @Autowired
  private MemberBillMapper memberBillMapper;
  @Autowired
  private MemberBillConvert memberBillConvert;

  @Override
  public void save(Member member, MemberBill memberBill) throws Exception {
    memberBill.setAccount(member.getAccount());
    memberBill.setMemberPath(member.getSuperPath());
    memberBill.setTableIndex(member.getTableIndex());
    memberBill.setMemberId(member.getId());
    this.save(memberBill);
  }

  @Override
  public IPage<MemberBillVO> findMemberBilllist(PageDTO<MemberBill> page, MemberBillDTO dto) {

    if (ObjectUtils.isEmpty(dto.getBeginTime()) || ObjectUtils.isEmpty(dto.getEndTime())){
      throw new ServiceException("查询账变记录需选择时间段！");
    }
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date nowDate = null;
    Date beginTime = null;
    Date endTime = null;
    try {
      nowDate = sdf.parse(sdf.format(new Date()));
      beginTime = sdf.parse(dto.getBeginTime());
      endTime = sdf.parse(dto.getEndTime());
    } catch (ParseException e) {
      e.printStackTrace();
    }

    //今天
    if (beginTime.compareTo(nowDate) == 0 && endTime.compareTo(nowDate) == 0){
      dto.setBeginTime(dto.getBeginTime()+" 00:00:00");
      dto.setEndTime(dto.getEndTime()+" 23:59:59");
      return this.lambdaQuery()
              .eq(ObjectUtils.isNotEmpty(dto.getAccount()), MemberBill::getAccount, dto.getAccount())
              .eq(ObjectUtils.isNotEmpty(dto.getOrderNo()), MemberBill::getOrderNo, dto.getOrderNo())
              .ge(ObjectUtils.isNotEmpty(dto.getBeginTime()), MemberBill::getCreateTime, dto.getBeginTime())
              .le(ObjectUtils.isNotEmpty(dto.getEndTime()), MemberBill::getCreateTime, dto.getEndTime())
              .in(ObjectUtils.isNotEmpty(dto.getTranTypes()), MemberBill::getTranType, dto.getTranTypes())
              .orderByDesc(MemberBill::getCreateTime)
              .orderByDesc(MemberBill::getId)
              .page(page)
              .convert(memberBillConvert::toVo);
      //历史
    }else {
      if(ObjectUtils.isEmpty(dto.getAccount())){
        throw new ServiceException("查询【非当日】账变记录需提供【会员帐号】！");
      }
      Integer tableIndex = this.lambdaQuery()
              .eq(ObjectUtils.isNotEmpty(dto.getAccount()), MemberBill::getAccount, dto.getAccount())
              .list().get(0).getTableIndex();

      IPage<MemberBillVO> pageList = memberBillMapper.findyByTableIndex(page, dto.getAccount(),
              dto.getOrderNo(),
              dto.getTranTypes(),
              dto.getBeginTime(),
              dto.getEndTime(),
              tableIndex);

      return pageList;
    }

  }

  @Override
  public void exportList(MemberBillDTO dto, HttpServletResponse response){
    try{
      List<MemberBillVO> list = getList(dto);
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Content-disposition", "attachment;filename=myExcel.xls");
      @Cleanup OutputStream ouputStream = null;
      Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("VIP会员签到记录导出","VIP会员签到记录"),
              MemberBillVO.class, list );
      ouputStream = response.getOutputStream();
      workbook.write(ouputStream);
    }catch (IOException e) {
      throw new ServiceException("导出失败:"+e);
    }
  }

  @Override
  public MemberBill queryLiveBill(Long id, String orderNo, int transType) {
    //TODO 获取额度转换流水记录
    // 1. 现在在主表查询，接口为空就根据会员ID取模 到对应的历史表中获取数据


    return null;
  }

  public List<MemberBillVO> getList(MemberBillDTO dto) {
    if (ObjectUtils.isEmpty(dto.getBeginTime()) || ObjectUtils.isEmpty(dto.getEndTime())) {
      throw new ServiceException("查询账变记录需选择时间段！");
    }
    List<MemberBillVO> voList = new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date nowDate = null;
    Date beginTime = null;
    Date endTime = null;
    try {
      nowDate = sdf.parse(sdf.format(new Date()));
      beginTime = sdf.parse(dto.getBeginTime());
      endTime = sdf.parse(dto.getEndTime());
    } catch (ParseException e) {
      e.printStackTrace();
    }

    //今天
    if (beginTime.compareTo(nowDate) == 0 && endTime.compareTo(nowDate) == 0) {
      dto.setBeginTime(dto.getBeginTime() + " 00:00:00");
      dto.setEndTime(dto.getEndTime() + " 23:59:59");
      List<MemberBill> list = this.lambdaQuery()
              .eq(ObjectUtils.isNotEmpty(dto.getAccount()), MemberBill::getAccount, dto.getAccount())
              .eq(ObjectUtils.isNotEmpty(dto.getOrderNo()), MemberBill::getOrderNo, dto.getOrderNo())
              .ge(ObjectUtils.isNotEmpty(dto.getBeginTime()), MemberBill::getCreateTime, dto.getBeginTime())
              .le(ObjectUtils.isNotEmpty(dto.getEndTime()), MemberBill::getCreateTime, dto.getEndTime())
              .in(ObjectUtils.isNotEmpty(dto.getTranTypes()), MemberBill::getTranType, dto.getTranTypes())
              .orderByDesc(MemberBill::getCreateTime)
              .orderByDesc(MemberBill::getId)
              .list();

      for (int i = 0; i < list.size(); i++) {
        voList.add(memberBillConvert.toVo(list.get(i)));
      }
      return voList;
      //历史
    } else {
      if (ObjectUtils.isEmpty(dto.getAccount())) {
        throw new ServiceException("查询【非当日】账变记录需提供【会员帐号】！");
      }
      Integer tableIndex = this.lambdaQuery()
              .eq(ObjectUtils.isNotEmpty(dto.getAccount()), MemberBill::getAccount, dto.getAccount())
              .list().get(0).getTableIndex();

      voList = memberBillMapper.findyByTableIndex(dto.getAccount(),
              dto.getOrderNo(),
              dto.getTranTypes(),
              dto.getBeginTime(),
              dto.getEndTime(),
              tableIndex);

      return voList;
    }
  }

}
