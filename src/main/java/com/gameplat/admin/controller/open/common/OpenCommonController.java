package com.gameplat.admin.controller.open.common;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONObject;
import com.gameplat.admin.model.bean.router.VueRouter;
import com.gameplat.admin.model.domain.SysMenu;
import com.gameplat.admin.model.vo.ConfigVO;
import com.gameplat.admin.service.CommonService;
import com.gameplat.admin.service.ConfigService;
import com.gameplat.admin.service.PermissionService;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.base.common.web.Result;
import com.gameplat.common.compent.captcha.CaptchaConfig;
import com.gameplat.common.compent.captcha.CaptchaEnums;
import com.gameplat.common.compent.captcha.CaptchaProvider;
import com.gameplat.common.compent.captcha.CaptchaStrategyContext;
import com.gameplat.common.compent.captcha.image.Kaptcha;
import com.gameplat.common.compent.oss.AbstractFileStorageProvider;
import com.gameplat.common.compent.oss.FileStorageProvider;
import com.gameplat.common.compent.oss.FileStorageStrategyContext;
import com.gameplat.common.compent.oss.config.FileConfig;
import com.gameplat.common.enums.DictTypeEnum;
import com.gameplat.security.SecurityUserHolder;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.base.Captcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 公共控制类
 *
 * @author three
 */
@Api(tags = "公共控制类")
@Slf4j
@RestController
@RequestMapping("/api/admin/common")
public class OpenCommonController {

  @Autowired private CommonService commonService;

  @Autowired private PermissionService permissionService;

  @Autowired private CaptchaStrategyContext captchaStrategyContext;

  @Autowired private FileStorageStrategyContext fileStorageStrategyContext;

  @Autowired private ConfigService configService;

  @GetMapping("/vCode")
  public Kaptcha createCode(HttpServletRequest request, HttpServletResponse response) {
    response.setHeader("param", "no-cache");
    response.setHeader("cache-control", "no-cache");
    response.setIntHeader("expires", 0);
    return captchaStrategyContext.getImage().create(request, 1);
  }

  @GetMapping("/menuList")
  public ArrayList<VueRouter<SysMenu>> menuList() {
    return permissionService.getMenuList(SecurityUserHolder.getUsername());
  }

  @ApiOperation(value = "获取字典数据")
  @GetMapping("/getDictByTypes/{types}")
  public Map<Object, List<JSONObject>> getDictByTypes(@PathVariable String types) {
    return StringUtils.isEmpty(types) ? null : commonService.getDictByTypes(types);
  }

  @GetMapping("/config")
  public ConfigVO config() {
    return commonService.getConfig();
  }

  @GetMapping("/file/upload")
  public Map<String, String> fileUpload(@RequestPart MultipartFile file) throws IOException {
    FileConfig fileConfig =
        configService.getDefaultConfig(DictTypeEnum.FILE_CONFIG, FileConfig.class);
    FileStorageProvider fileStorageProvider = fileStorageStrategyContext.getProvider(fileConfig);
    String url = fileStorageProvider.upload(file.getResource().getFile());

    Map<String, String> map = new HashMap<>(1);
    map.put("url", url);
    return map;
  }
}
