package com.gameplat.admin.service.impl;

import com.gameplat.admin.model.vo.ExternalDataVo;
import com.gameplat.admin.service.ExternalDataService;
import com.gameplat.base.common.util.EasyExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class ExternalDataServiceImpl implements ExternalDataService {
    /**
     * 开始处理外部数据导入
     * @param username
     * @param file
     * @param request
     */
    @Override
    public void dealData(String username, MultipartFile file, HttpServletRequest request) {
        //1. 解析Excel
        try {
            List<ExternalDataVo> paramlist = EasyExcelUtil.readExcel(file.getInputStream(), ExternalDataVo.class);
            log.info("共需导入{}条数据", paramlist.size());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
