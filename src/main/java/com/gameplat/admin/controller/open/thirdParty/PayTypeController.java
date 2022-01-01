package com.gameplat.admin.controller.open.thirdParty;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.domain.PayType;
import com.gameplat.admin.model.dto.PayTypeAddDTO;
import com.gameplat.admin.model.dto.PayTypeEditDTO;
import com.gameplat.admin.model.vo.PayTypeVO;
import com.gameplat.admin.service.PayTypeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/thirdParty/payTypes")
public class PayTypeController {

  @Autowired private PayTypeService payTypeService;

  @DeleteMapping("/remove/{id}")
  @PreAuthorize("hasAuthority('thirdParty:payTypes:remove')")
  public void remove(@PathVariable Long id) {
    payTypeService.delete(id);
  }

  @PostMapping("/list")
  @PreAuthorize("hasAuthority('thirdParty:payTypes:list')")
  public List<PayTypeVO> findPayTypes(String name,Integer status) {
    return payTypeService.queryList(name,status);
  }

  @PostMapping("/add")
  @PreAuthorize("hasAuthority('thirdParty:payTypes:add')")
  public void add(@RequestBody PayTypeAddDTO dto) {
    payTypeService.save(dto);
  }

  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('thirdParty:payTypes:edit')")
  public void edit(@RequestBody PayTypeEditDTO dto) {
    payTypeService.update(dto);
  }

  @PostMapping("/editStatus")
  @PreAuthorize("hasAuthority('thirdParty:payTypes:editStatus')")
  public void updateStatus(Long id, Integer status) {
    payTypeService.updateStatus(id, status);
  }


  @PostMapping("/page")
  @PreAuthorize("hasAuthority('thirdParty:payTypes:page')")
  public IPage<PayType> queryPage(Page<PayType> page) {
    return payTypeService.queryPage(page);
  }
}
