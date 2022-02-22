package com.gameplat.admin.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.constant.SystemConstant;
import com.gameplat.admin.convert.SpreadLinkInfoConvert;
import com.gameplat.admin.mapper.MemberInfoMapper;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.domain.MemberInfo;
import com.gameplat.admin.service.MemberInfoService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.base.common.enums.EnableEnum;
import com.gameplat.common.enums.BooleanEnum;
import com.gameplat.common.enums.DefaultEnums;
import com.gameplat.admin.mapper.SpreadLinkInfoMapper;
import com.gameplat.admin.model.domain.SpreadLinkInfo;
import com.gameplat.admin.model.dto.SpreadLinkInfoAddDTO;
import com.gameplat.admin.model.dto.SpreadLinkInfoDTO;
import com.gameplat.admin.model.dto.SpreadLinkInfoEditDTO;
import com.gameplat.admin.model.vo.SpreadConfigVO;
import com.gameplat.admin.service.SpreadLinkInfoService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.base.common.validator.ValidatorUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.gameplat.common.enums.UserTypes;
import com.gameplat.common.lang.Assert;
import lombok.Cleanup;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

/**
 * 域名推广配置 服务实现层
 *
 * @author three
 */
@Service
@SuppressWarnings("all")
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class SpreadLinkInfoServiceImpl extends ServiceImpl<SpreadLinkInfoMapper, SpreadLinkInfo>
        implements SpreadLinkInfoService {

    @Autowired
    private SpreadLinkInfoConvert spreadLinkInfoConvert;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberInfoMapper memberInfoMapper;

    @Override
    public IPage<SpreadConfigVO> page(PageDTO<SpreadLinkInfo> page, SpreadLinkInfoDTO dto) {
        return this.lambdaQuery()
                .eq(ObjectUtils.isNotNull(dto.getId()), SpreadLinkInfo::getId, dto.getId())
                .eq(
                        ObjectUtils.isNotEmpty(dto.getAgentAccount()),
                        SpreadLinkInfo::getAgentAccount,
                        dto.getAgentAccount())
                .eq(
                        ObjectUtils.isNotEmpty(dto.getSpreadType()),
                        SpreadLinkInfo::getSpreadType,
                        dto.getSpreadType())
                .eq(
                        ObjectUtils.isNotNull(dto.getUserType()),
                        SpreadLinkInfo::getUserType,
                        dto.getUserType())
                .eq(ObjectUtils.isNotNull(dto.getStatus()), SpreadLinkInfo::getStatus, dto.getStatus())
                .eq(ObjectUtils.isNotEmpty(dto.getCode()), SpreadLinkInfo::getCode, dto.getCode())
                .orderBy(
                        StringUtils.equals(dto.getOrderByColumn(), "createTime"),
                        ValidatorUtil.isAsc(dto.getSortBy()),
                        SpreadLinkInfo::getCreateTime)
                .orderBy(
                        StringUtils.equals(dto.getOrderByColumn(), "visitCount"),
                        ValidatorUtil.isAsc(dto.getSortBy()),
                        SpreadLinkInfo::getVisitCount)
                .orderBy(
                        StringUtils.equals(dto.getOrderByColumn(), "registCount"),
                        ValidatorUtil.isAsc(dto.getSortBy()),
                        SpreadLinkInfo::getRegistCount)
                .page(page)
                .convert(spreadLinkInfoConvert::toVo);
    }

    @Override
    public void exportList(SpreadLinkInfoDTO dto, HttpServletResponse response) {
        try {
            List<SpreadLinkInfo> list = this.lambdaQuery()
                    .eq(ObjectUtils.isNotNull(dto.getId()), SpreadLinkInfo::getId, dto.getId())
                    .eq(
                            ObjectUtils.isNotEmpty(dto.getAgentAccount()),
                            SpreadLinkInfo::getAgentAccount,
                            dto.getAgentAccount())
                    .eq(
                            ObjectUtils.isNotEmpty(dto.getSpreadType()),
                            SpreadLinkInfo::getSpreadType,
                            dto.getSpreadType())
                    .eq(
                            ObjectUtils.isNotNull(dto.getUserType()),
                            SpreadLinkInfo::getUserType,
                            dto.getUserType())
                    .eq(ObjectUtils.isNotNull(dto.getStatus()), SpreadLinkInfo::getStatus, dto.getStatus())
                    .eq(ObjectUtils.isNotEmpty(dto.getCode()), SpreadLinkInfo::getCode, dto.getCode())
                    .orderBy(
                            StringUtils.equals(dto.getOrderByColumn(), "createTime"),
                            ValidatorUtil.isAsc(dto.getSortBy()),
                            SpreadLinkInfo::getCreateTime)
                    .orderBy(
                            StringUtils.equals(dto.getOrderByColumn(), "visitCount"),
                            ValidatorUtil.isAsc(dto.getSortBy()),
                            SpreadLinkInfo::getVisitCount)
                    .orderBy(
                            StringUtils.equals(dto.getOrderByColumn(), "registCount"),
                            ValidatorUtil.isAsc(dto.getSortBy()),
                            SpreadLinkInfo::getRegistCount)
                    .list();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename = myExcel.xls");
            @Cleanup OutputStream outputStream = null;
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("域名推广列表导出", "域名推广列表"), SpreadLinkInfo.class, list);
            outputStream = response.getOutputStream();
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new ServiceException("导出失败:" + e);
        }

    }

    /**
     * 新增推广信息
     * 1.推广域名是否为空： 不为空 需要校验（是否被其它代理作为了专属域名）
     *
     * @param dto 需求: 1、代理专属推广地址唯一 2、推广码唯一 3、公共推广地址下允许多个推广码 4、代理专属推广地址允许多个推广码
     */
    @Override
    public void add(SpreadLinkInfoAddDTO dto) {
        // 实体转换
        SpreadLinkInfo linkInfo = spreadLinkInfoConvert.toEntity(dto);
        if (StrUtil.isBlank(linkInfo.getAgentAccount())) {
            throw new ServiceException("代理账号不能为空！");
        }
        // 校验账号的用户类型
        Member member = memberService.getByAccount(linkInfo.getAgentAccount()).orElseThrow(() -> new ServiceException("代理账号不存在!"));
        Assert.isTrue(UserTypes.AGENT.value().equalsIgnoreCase(member.getUserType()), "账号类型不支持！");
        // 如果推广码为空  随机生成 4-20位
        if (StrUtil.isBlank(linkInfo.getCode())) {
            linkInfo.setCode(RandomStringUtils.random(6, true, true).toLowerCase());
        }
        // 校验推广码格式 并且是否已经存在
        this.checkCode(linkInfo.getCode());
        // 当推广链接不为空时 需要校验 此推广链接地址是否被其它代理作为了专属域名
        if (StrUtil.isNotBlank(linkInfo.getExternalUrl())) {
            boolean exists = this.lambdaQuery()
                    .ne(SpreadLinkInfo::getAgentAccount, linkInfo.getAgentAccount())
                    .eq(SpreadLinkInfo::getExternalUrl, linkInfo.getExternalUrl())
                    .eq(SpreadLinkInfo::getExclusiveFlag, BooleanEnum.YES.value())
                    .exists();
            if (exists) {
                throw new ServiceException("推广链接地址已被使用！");
            }
        }
        Assert.isTrue(this.save(linkInfo), "创建失败！");
    }

    @Override
    public void update(SpreadLinkInfoEditDTO dto) {
        SpreadLinkInfo linkInfo = spreadLinkInfoConvert.toEntity(dto);
        if (StrUtil.isBlank(linkInfo.getAgentAccount())) {
            throw new ServiceException("代理账号不能为空！");
        }
        // 校验账号的用户类型
        Member member = memberService.getByAccount(linkInfo.getAgentAccount()).orElseThrow(() -> new ServiceException("代理账号不存在!"));
        Assert.isTrue(UserTypes.AGENT.value().equalsIgnoreCase(member.getUserType()), "账号类型不支持！");
        // 当推广链接不为空时 需要校验 此推广链接地址是否被其它代理作为了专属域名
        if (StrUtil.isNotBlank(linkInfo.getExternalUrl())) {
            boolean exists = this.lambdaQuery()
                    .ne(SpreadLinkInfo::getAgentAccount, linkInfo.getAgentAccount())
                    .eq(SpreadLinkInfo::getExternalUrl, linkInfo.getExternalUrl())
                    .eq(SpreadLinkInfo::getExclusiveFlag, BooleanEnum.YES.value())
                    .exists();
            if (exists) {
                throw new ServiceException("推广链接地址已被使用！");
            }
        }
        Assert.isTrue(this.updateById(linkInfo), "编辑失败！");
    }

    @Override
    public void deleteById(Long id) {
        this.removeById(id);
    }

    @Override
    public void changeStatus(SpreadLinkInfoEditDTO dto) {
        if (!this.updateById(spreadLinkInfoConvert.toEntity(dto))) {
            throw new ServiceException("修改失败");
        }
    }

    @Override
    public void changeReleaseTime(Long id) {
        if (!this.updateById(SpreadLinkInfo.builder().id(id).createTime(new Date()).build())) {
            throw new ServiceException("修改失败");
        }
    }

    @Override
    public void batchEnableStatus(List<Long> ids) {
        if (!this.lambdaUpdate()
                .in(SpreadLinkInfo::getId, ids)
                .set(SpreadLinkInfo::getStatus, EnableEnum.ENABLED.code())
                .update(new SpreadLinkInfo())) {
            throw new ServiceException("批量修改状态失败!");
        }
    }

    @Override
    public void batchDisableStatus(List<Long> ids) {
        if (!this.lambdaUpdate()
                .in(SpreadLinkInfo::getId, ids)
                .set(SpreadLinkInfo::getStatus, EnableEnum.DISABLED.code())
                .update(new SpreadLinkInfo())) {
            throw new ServiceException("批量修改状态失败!");
        }
    }

    @Override
    public void batchDeleteByIds(List<Long> ids) {
        if (!this.removeByIds(ids)) {
            throw new ServiceException("批量删除失败");
        }
    }

    @Override
    public List<SpreadLinkInfo> getSpreadList(String agentAccount) {
        return this.lambdaQuery().eq(SpreadLinkInfo::getAgentAccount, agentAccount).list();
    }

    @Override
    public void checkCode(String code) {
        String reg = "^[a-zA-Z0-9]{4,20}$";
        if (!code.matches(reg)) {
            throw new ServiceException("推广码必须由4-20位数字或字母组成！");
        }
        boolean exists = this.lambdaQuery()
                .eq(ObjectUtils.isNotEmpty(code),
                        SpreadLinkInfo::getCode, code)
                .exists();
        if (exists == true) {
            throw new ServiceException("推广码已被使用！");
        }
    }

    @Override
    public JSONArray getSpreadLinkRebate(String account, Boolean statisMax, Boolean statisMin){
        BigDecimal min = BigDecimal.ZERO;
        BigDecimal max = BigDecimal.ZERO;
        if (StrUtil.isBlank(account)) {
            min = new BigDecimal("0").setScale(2,BigDecimal.ROUND_HALF_UP);
            max = new BigDecimal("9").setScale(2,BigDecimal.ROUND_HALF_UP);
        } else {
            if (statisMax) {
                BigDecimal userRebate = memberInfoMapper.findUserRebate(account);
                max = ObjectUtils.isNull(userRebate) ? new BigDecimal("9").setScale(2,BigDecimal.ROUND_HALF_UP)
                        : userRebate.setScale(2,BigDecimal.ROUND_HALF_UP);
            } else {
                max = new BigDecimal("9").setScale(2,BigDecimal.ROUND_HALF_UP);
            }
            if (statisMin) {
                // 获取直属下级的最大返点等级
                BigDecimal userLowerMaxRebate = memberInfoMapper.findUserLowerMaxRebate(account);
                min = ObjectUtils.isNull(userLowerMaxRebate) ? new BigDecimal("0").setScale(2,BigDecimal.ROUND_HALF_UP)
                        : userLowerMaxRebate.setScale(2,BigDecimal.ROUND_HALF_UP);
                // 再获取此账号推广码的最大返点等级
                LambdaQueryWrapper<SpreadLinkInfo> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(SpreadLinkInfo::getAgentAccount, account)
                        .orderByDesc(SpreadLinkInfo::getRebate)
                        .last("limit 1").select(SpreadLinkInfo::getRebate);
                SpreadLinkInfo linkMinRebateObj = this.getOne(queryWrapper);
                BigDecimal linkMinRebate = BigDecimal.ZERO;
                if (BeanUtil.isEmpty(linkMinRebateObj)) {
                    linkMinRebate = new BigDecimal("0").setScale(2,BigDecimal.ROUND_HALF_UP);
                } else {
                    linkMinRebate = BigDecimal.valueOf(linkMinRebateObj.getRebate()).setScale(2,BigDecimal.ROUND_HALF_UP);
                }

                min = min.compareTo(linkMinRebate) >= 0 ? min : linkMinRebate;
            } else {
                min = new BigDecimal("0").setScale(2,BigDecimal.ROUND_HALF_UP);
            }
        }

        JSONArray jsonArray = new JSONArray();
        BigDecimal rebate = max;
        Integer base = 1800;
        BigDecimal value = BigDecimal.ZERO;
        String text = "";
        while (rebate.compareTo(min) >= 0) {
            JSONObject jsonObject = new JSONObject();
            value = rebate;
            Integer baseData = base + value.multiply(BigDecimal.valueOf(20L)).intValue();
            text = rebate.toString().concat("% ---- ").concat(baseData.toString());
            jsonObject.set("value", value);
            jsonObject.set("text", text);
            jsonArray.add(jsonObject);
            rebate = rebate.subtract(BigDecimal.valueOf(0.1)).setScale(2,BigDecimal.ROUND_HALF_UP);
        }
        return jsonArray;
    }

    public static void main(String[] args) {
        BigDecimal min = new BigDecimal("0").setScale(1,BigDecimal.ROUND_HALF_UP);
        BigDecimal max = new BigDecimal("9").setScale(1,BigDecimal.ROUND_HALF_UP);
        JSONArray jsonArray = new JSONArray();
        BigDecimal rebate = max;
        Integer base = 1800;
        BigDecimal value = BigDecimal.ZERO;
        String text = "";
        while (rebate.compareTo(min) >= 0) {
            JSONObject jsonObject = new JSONObject();
            value = rebate;
            Integer baseData = base + value.multiply(BigDecimal.valueOf(20L)).intValue();
            text = rebate.toString().concat("% ---- ").concat(baseData.toString());
            jsonObject.set("value", value);
            jsonObject.set("text", text);
            jsonArray.add(jsonObject);
            rebate = rebate.subtract(BigDecimal.valueOf(0.1)).setScale(2,BigDecimal.ROUND_HALF_UP);
        }
        System.out.println(jsonArray);
    }
}
