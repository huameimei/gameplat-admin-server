package com.gameplat.admin;

import com.gameplat.admin.service.ActivityQualificationService;
import com.gameplat.admin.service.ConfigService;
import com.gameplat.common.enums.DictDataEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class AdminApplicationTests {

    @Autowired
    private ActivityQualificationService activityQualificationService;

    @Autowired private ConfigService configService;

    @Test
    public void testCount(){
        log.info("资料表有={}条记录",activityQualificationService.count());
    }

    @Test
    public void getConfig(){
        //查询红包配置
        String redPacketConfig = configService.getValue(DictDataEnum.REDPACKET);

        log.info(redPacketConfig);
    }
}
