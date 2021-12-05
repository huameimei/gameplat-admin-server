package com.gameplat.admin.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberVipSignStatisConvert;
import com.gameplat.admin.mapper.MemberVipSignStatisMapper;
import com.gameplat.admin.model.domain.MemberVipSignStatis;
import com.gameplat.admin.model.dto.MemberVipSignStatisDTO;
import com.gameplat.admin.model.vo.MemberVipSignStatisVO;
import com.gameplat.admin.service.MemberVipSignStatisService;
import com.gameplat.base.common.exception.ServiceException;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


/**
 * @author lily
 * @description VIP会员签到汇总
 * @date 2021/11/24
 */

@Service
@RequiredArgsConstructor
public class MemberVipSignStatisServiceImpl extends ServiceImpl<MemberVipSignStatisMapper, MemberVipSignStatis> implements MemberVipSignStatisService {

    @Autowired private MemberVipSignStatisConvert signStatisConvert;

    /**
     * 查询VIP会员签到记录列表
     */
    @Override
    public IPage<MemberVipSignStatisVO> findSignList(IPage<MemberVipSignStatis> page, MemberVipSignStatisDTO queryDTO) {
        return
                this.lambdaQuery()
                        .like(ObjectUtils.isNotEmpty(queryDTO.getUserName()), MemberVipSignStatis::getUserName, queryDTO.getUserName())
                        .ge(ObjectUtils.isNotEmpty(queryDTO.getBeginTime()), MemberVipSignStatis::getCreateTime, queryDTO.getBeginTime())
                        .le(ObjectUtils.isNotEmpty(queryDTO.getEndTime()), MemberVipSignStatis::getCreateTime, queryDTO.getEndTime())
                        .orderByDesc(MemberVipSignStatis::getCreateTime)
                        .page(page)
                        .convert(signStatisConvert::toVo);
    }

    /**
     * 导出签名
     */
    @Override
    public void exportSignStatis(MemberVipSignStatisDTO queryDTO, HttpServletResponse response) {

        try{
            List<MemberVipSignStatis> list = this.lambdaQuery()
                    .like(ObjectUtils.isNotEmpty(queryDTO.getUserName()), MemberVipSignStatis::getUserName, queryDTO.getUserName())
                    .ge(ObjectUtils.isNotEmpty(queryDTO.getBeginTime()), MemberVipSignStatis::getCreateTime, queryDTO.getBeginTime())
                    .le(ObjectUtils.isNotEmpty(queryDTO.getEndTime()), MemberVipSignStatis::getCreateTime, queryDTO.getEndTime())
                    .list();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=myExcel.xls");
            @Cleanup OutputStream ouputStream = null;
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("VIP会员签到记录导出","VIP会员签到记录"),
                    MemberVipSignStatis.class, list );
            ouputStream = response.getOutputStream();
            workbook.write(ouputStream);
        }catch (IOException e) {
            throw new ServiceException("导出失败:"+e);
        }

    }
}
