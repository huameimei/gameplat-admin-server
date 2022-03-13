
import com.alibaba.fastjson.JSON;
import com.gameplat.admin.service.TenantFloatTypeService;
import com.gameplat.model.entity.setting.TenantFloatSetting;
import com.gameplat.model.entity.setting.TenantFloatType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统消息
 *
 * @author key
 */
@Slf4j
@RestController
@RequestMapping("/api/open/float")
public class TenantFloatTypeController {
    @Autowired
    private TenantFloatTypeService tenantFloatTypeService;

/*    *//**
     * 查询游戏浮窗类型列表
     *//*
    @GetMapping("/query")
    public List<TenantFloatTypeVo> list(TenantFloatTypeVo tenantFloatTypeVo) {
        return tenantFloatTypeService.selectSysFloatTypeList(tenantFloatTypeVo);
    }*/

    /**
     * 新增游戏浮窗
     */
    @PutMapping("/insert")
    @CacheEvict(cacheNames = "TENANT_FLOAT_LIST",allEntries = true)
    public void add(@RequestBody TenantFloatSetting tenantFloatSetting) {
        tenantFloatTypeService.insertSysFloat(tenantFloatSetting);
    }

    /**
     * 删除游戏浮窗类型
     */
    // @PreAuthorize("@ss.hasPermi('kg:type:remove')")
    @DeleteMapping("/delById")
    @CacheEvict(cacheNames = "TENANT_FLOAT_LIST",allEntries = true)
    public void remove(Integer id) {
        tenantFloatTypeService.deleteSysFloatById(id);
    }

    /**
     * 编辑游戏浮窗
     */
    // @PreAuthorize("@ss.hasPermi('kg:type:remove')")
    @PostMapping("/update")
    @CacheEvict(cacheNames = "TENANT_FLOAT_LIST",allEntries = true)
    public void update(@RequestBody TenantFloatSetting tenantFloatSetting) {
        tenantFloatTypeService.updateFloat(tenantFloatSetting);
    }

    /**
     * 批量编辑排序
     */
    // @PreAuthorize("@ss.hasPermi('kg:type:remove')")
    @PostMapping("/updateBatch")
    @CacheEvict(cacheNames = "TENANT_FLOAT_LIST",allEntries = true)
    public void updateBatch(@RequestBody List<TenantFloatSetting> tenantFloatSettings) {
        tenantFloatTypeService.updateBatch(tenantFloatSettings);
    }

    /**
     * 编辑游戏浮窗类型
     */
    @PostMapping("/updateFloatType")
    @CacheEvict(cacheNames = "TENANT_FLOAT_LIST",allEntries = true)
    public void updateFloatType(@RequestBody TenantFloatType tenantFloatType) {
        tenantFloatTypeService.updateFloatType(tenantFloatType);
    }

    /**
     * 编辑游戏浮窗类型
     */
    @PostMapping("/showPosition")
    @CacheEvict(cacheNames = "TENANT_FLOAT_LIST",allEntries = true)
    public void showPosition(@RequestBody List<String> showPositionList) {
        tenantFloatTypeService.updateShowPosition(JSON.toJSONString(showPositionList));
    }
}
