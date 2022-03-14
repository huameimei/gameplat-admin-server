package com.gameplat.admin.util;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.ClassPathResource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * @description: JXLS工具类
 * @author: CC
 * @create: 2019-08-26 20:54
 **/
public class JxlsExcelUtil {

    public static final String separator = File.separator;

    /**
     * 下载excel
     * @param beanParams excel内容
     * @return
     * @throws ParsePropertyException
     * @throws InvalidFormatException
     * @throws IOException
     */
    public static void downLoadExcel(Map<String, Object> beanParams,String templateFileName, OutputStream os)
            throws ParsePropertyException, InvalidFormatException, IOException {
        ClassPathResource classPathResource = new ClassPathResource("static/excel/" + templateFileName);
        InputStream is = classPathResource.getInputStream();
        XLSTransformer transformer = new XLSTransformer();
        //向模板中写入内容
        Workbook workbook = transformer.transformXLS(is, beanParams);
        //写入成功后转化为输出流
        workbook.write(os);
        workbook.close();
    }
}
