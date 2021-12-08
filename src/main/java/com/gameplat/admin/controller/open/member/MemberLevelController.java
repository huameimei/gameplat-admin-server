package com.gameplat.admin.controller.open.member;

import com.gameplat.admin.model.dto.MemberLevelAddDTO;
import com.gameplat.admin.model.dto.MemberLevelAllocateDTO;
import com.gameplat.admin.model.dto.MemberLevelEditDTO;
import com.gameplat.admin.model.vo.MemberLevelVO;
import com.gameplat.admin.service.MemberLevelService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/admin/member/level")
public class MemberLevelController {

  @Autowired private MemberLevelService memberLevelService;

  @GetMapping("/list")
  public List<MemberLevelVO> getList() {
    return memberLevelService.getList();
  }

  @PostMapping("/add")
  public void add(@RequestBody @Validated MemberLevelAddDTO dto) {
    memberLevelService.add(dto);
  }

  @PutMapping("/update")
  public void update(@RequestBody @Validated MemberLevelEditDTO dto) {
    memberLevelService.update(dto);
  }

  @DeleteMapping("/delete/{id}")
  public void delete(@PathVariable Long id) {
    memberLevelService.delete(id);
  }

  @PostMapping("/lock/{id}")
  public void lock(@PathVariable Long id) {
    memberLevelService.lock(id);
  }

  @PostMapping("/unlock/{id}")
  public void unlock(@PathVariable Long id) {
    memberLevelService.unlock(id);
  }

  @PostMapping("/enable/{id}")
  public void enable(@PathVariable Long id) {
    memberLevelService.enable(id);
  }

  @PostMapping("/disable/{id}")
  public void disable(@PathVariable Long id) {
    memberLevelService.disable(id);
  }

  @PostMapping("/enable/withdraw/{id}")
  public void enableWithdraw(@PathVariable Long id) {
    memberLevelService.enableWithdraw(id);
  }

  @PostMapping("/disable/withdraw/{id}")
  public void disableWithdraw(@PathVariable Long id) {
    memberLevelService.disableWithdraw(id);
  }

  @PostMapping("/batchAllocate")
  public void batchAllocate(@Valid @RequestBody List<MemberLevelAllocateDTO> dtos) {
    memberLevelService.batchAllocate(dtos);
  }
}
