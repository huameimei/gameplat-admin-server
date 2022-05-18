package com.gameplat.admin.controller.open.common;

import com.alibaba.fastjson.JSONObject;
import com.gameplat.admin.model.bean.router.VueRouter;
import com.gameplat.admin.model.vo.ConfigVO;
import com.gameplat.admin.service.CaptchaService;
import com.gameplat.admin.service.CommonService;
import com.gameplat.admin.service.OssService;
import com.gameplat.admin.service.PermissionService;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.compent.captcha.image.Kaptcha;
import com.gameplat.model.entity.sys.SysMenu;
import com.gameplat.security.SecurityUserHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公共控制类
 *
 * @author three
 */
@Tag(name = "公共控制类")
@RestController
@RequestMapping("/api/admin/common")
public class OpenCommonController {

  @Autowired private CommonService commonService;

  @Autowired private PermissionService permissionService;

  @Autowired private OssService ossService;

  @Autowired private CaptchaService captchaService;

  @GetMapping("/vCode")
  public Kaptcha createCode(HttpServletRequest request, HttpServletResponse response) {
    response.setHeader(HttpHeaders.PRAGMA, "no-cache");
    response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
    response.setIntHeader(HttpHeaders.EXPIRES, 0);
    return captchaService.getImage().create(request, 1);
  }

  @GetMapping("/menuList")
  public ArrayList<VueRouter<SysMenu>> menuList() {
    return permissionService.getMenuList(SecurityUserHolder.getUsername());
  }

  @Operation(summary = "获取字典数据")
  @GetMapping("/getDictByTypes/{types}")
  public Map<Object, List<JSONObject>> getDictByTypes(@PathVariable String types) {
    return StringUtils.isEmpty(types) ? null : commonService.getDictByTypes(types);
  }

  @GetMapping("/config")
  public ConfigVO config() {
    return commonService.getConfig();
  }

  @PostMapping("/file/upload")
  public Map<String, String> fileUpload(@RequestPart MultipartFile file) {
    Map<String, String> map = new HashMap<>(1);
    map.put("url", ossService.upload(file));
    return map;
  }
}
