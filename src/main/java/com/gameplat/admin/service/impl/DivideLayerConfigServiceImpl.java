package com.gameplat.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.constant.SystemConstant;
import com.gameplat.admin.mapper.DivideLayerConfigMapper;
import com.gameplat.admin.mapper.GameKindMapper;
import com.gameplat.admin.mapper.SpreadLinkInfoMapper;
import com.gameplat.admin.mapper.SysDictDataMapper;
import com.gameplat.admin.model.domain.GameKind;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.domain.SpreadLinkInfo;
import com.gameplat.admin.model.domain.SysDictData;
import com.gameplat.admin.model.domain.proxy.DivideLayerConfig;
import com.gameplat.admin.model.dto.DivideConfigDTO;
import com.gameplat.admin.model.dto.SpreadLinkInfoDTO;
import com.gameplat.admin.model.vo.DivideLayerConfigVo;
import com.gameplat.admin.model.vo.GameDivideVo;
import com.gameplat.admin.model.vo.SpreadLinkInfoVo;
import com.gameplat.admin.service.DivideLayerConfigService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.admin.service.RecommendConfigService;
import com.gameplat.base.common.enums.EnableEnum;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.UserTypes;
import com.gameplat.common.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@SuppressWarnings("all")
public class DivideLayerConfigServiceImpl extends ServiceImpl<DivideLayerConfigMapper, DivideLayerConfig> implements DivideLayerConfigService {
    @Autowired
    private DivideLayerConfigMapper layerConfigMapper;
    @Autowired
    private MemberService memberService;
    @Autowired
    private RecommendConfigService recommendConfigService;
    @Autowired
    private SpreadLinkInfoMapper spreadLinkInfoMapper;
    @Autowired
    private SysDictDataMapper sysDictDataMapper;
    @Autowired
    private GameKindMapper gameKindMapper;

    /**
     * 分页查询
     * @param page
     * @param dto
     * @return
     */
    @Override
    public IPage<DivideLayerConfigVo> page(PageDTO<DivideLayerConfig> page,DivideConfigDTO dto) {
        IPage<DivideLayerConfigVo> pageList = layerConfigMapper.pageList(page, dto);
        return pageList;
    }

    /**
     * 编辑前获取层层代分红配置可编辑数据
     * @param userName
     * @param s
     * @return
     */
    @Override
    public Map<String, Object> getLayerConfigForEdit(String userName, String s) {
        Assert.isTrue(StrUtil.isNotBlank(userName), "用户名参数缺失！");
        Member member = memberService.getAgentByAccount(userName).orElseThrow(() -> new ServiceException("代理账号不存在!"));
        // 获取到自己的分红配置
        DivideLayerConfig ownerLayerConfig = layerConfigMapper.getByUserName(userName);
        if (BeanUtil.isEmpty(ownerLayerConfig) || StrUtil.isBlank(ownerLayerConfig.getDivideConfig())) {
            throw new ServiceException("分红配置为空！");
        }
        Map<String,Object> returnMap = new HashMap<>();
        Map<String,JSONObject> ownerLayerConfigMap = JSONUtil.toBean(ownerLayerConfig.getDivideConfig(),Map.class);

        Map<String, JSONObject> parentLayerConfigMap = new HashMap<>();
        if (member.getAgentLevel() > 1) {
            DivideLayerConfig parentLayerConfig = layerConfigMapper.getByUserName(member.getParentName());
            if (BeanUtil.isEmpty(parentLayerConfig) || StrUtil.isBlank(parentLayerConfig.getDivideConfig())) {
                throw new ServiceException("上级代理：" + member.getParentName() + "未设置分红配置,无法添加");
            }
            parentLayerConfigMap = JSONUtil.toBean(parentLayerConfig.getDivideConfig(),Map.class);
            returnMap.put("isDisabled",true);
        } else {
            parentLayerConfigMap = new HashMap<>(ownerLayerConfigMap);
            returnMap.put("isDisabled",false);
        }
        List<SysDictData> liveGameTypeList = sysDictDataMapper.findDataByType("LIVE_GAME_TYPE", "1");
        if (CollectionUtil.isEmpty(liveGameTypeList)) {
            throw new ServiceException("游戏大类数据为空");
        }
        List<GameKind> levelOneList = gameKindMapper.selectList(
                new QueryWrapper<GameKind>().eq("enable", EnableEnum.ENABLED.code())
        );
        Map<String, List<GameKind>> levelOneMap = levelOneList.stream().collect(Collectors.groupingBy(GameKind::getCode));

        // 是否有下级存在配置
        Integer childCount = layerConfigMapper.countTeam(userName);
        // 是否设置过推广链接中的分红配置
        Integer linkCount = spreadLinkInfoMapper.countTeamLinkInfo(userName);
        Map<String, List<GameDivideVo>> tmpMap = new TreeMap<>();

        for (SysDictData dictData : liveGameTypeList) {
            List<GameDivideVo> divideLevelOneList = new ArrayList<>();
            for (Map.Entry<String,JSONObject> voMap: parentLayerConfigMap.entrySet()) {
                if (BeanUtil.isEmpty(ownerLayerConfigMap.get(voMap.getKey()))) {
                    continue;
                }
                if (dictData.getDictValue().equalsIgnoreCase(voMap.getValue().getStr("liveGameCode"))) {
                    // 填充最大可编辑的分红点数值
                    if (member.getAgentLevel() == 1) {
                        voMap.getValue().put("maxRatio", new BigDecimal("100"));
                    } else {
                        // 如果不是顶级代理 则可调整的最大值就是他直属上级被直属上级的直属上级所分配的分红比例 即 他直属上级的divideRatio值
                        BigDecimal maxRatio = parentLayerConfigMap.get(voMap.getKey()).getBigDecimal("divideRatio");
                        voMap.getValue().put("maxRatio", maxRatio);
                    }

                    // 填充最小可编辑的分红点数值
                    BigDecimal min = BigDecimal.ZERO;
                    if (childCount > 0) {
                        // 最小值就是 它分配给所有直接下级的分红比例的最大值  无直属下级分红时  就是0
                        BigDecimal childMaxDivideRatio = layerConfigMapper.getChildMaxDivideRatio(userName,voMap.getKey());
                        min = childMaxDivideRatio.compareTo(min) > 0 ? childMaxDivideRatio : min;
                    }
                    if (linkCount > 0) {
                        // 最小值就是 绑定此代理的推广链接的默认分红配置的分红比例最大值
                        BigDecimal promotionMaxDivideRatio = spreadLinkInfoMapper.getlinkMaxDivideRatio(userName,voMap.getKey());
                        min = promotionMaxDivideRatio.compareTo(min) > 0 ? promotionMaxDivideRatio : min;
                    }
                    voMap.getValue().put("minRatio",min);
                    voMap.getValue().put("divideRatio",ownerLayerConfigMap.get(voMap.getKey()).getBigDecimal("divideRatio"));
                    voMap.getValue().put("parentDivideRatio",ownerLayerConfigMap.get(voMap.getKey()).getBigDecimal("parentDivideRatio"));

                    List<GameKind> tmpLevelOneList = levelOneMap.get(voMap.getValue().getStr("code"));
                    if (CollectionUtil.isNotEmpty(tmpLevelOneList)) {
                        voMap.getValue().put("name", tmpLevelOneList.get(0).getName());
                    }

                    divideLevelOneList.add(JSONUtil.toBean(voMap.getValue(),GameDivideVo.class));
                }
            }
            tmpMap.put(dictData.getDictLabel(),divideLevelOneList);
        }
        returnMap.put("ownerConfigMap",tmpMap);
        return returnMap;
    }

    @Override
    public void remove(String ids) {
        String[] idArr = ids.split(",");
        for (String idStr: idArr) {
            DivideLayerConfig layerConfig = layerConfigMapper.selectById(idStr);
            if (BeanUtil.isNotEmpty(layerConfig)) {
                List<DivideLayerConfigVo> teamList = layerConfigMapper.getTeamList(layerConfig.getUserName());
                if (CollectionUtil.isNotEmpty(teamList)) {
                    List<Long> idList = teamList.stream().map(DivideLayerConfigVo::getId).collect(Collectors.toList());
                    layerConfigMapper.deleteBatchIds(idList);
                }
                layerConfigMapper.deleteById(layerConfig.getId());
            }
        }
    }

    /**
     * 添加层层代分红配置
     * @param divideConfigDTO
     */
    @Override
    public void add(String userName, String lang) {
        Assert.isTrue(StrUtil.isNotBlank(userName), "用户名参数缺失！");
        Member member = memberService.getAgentByAccount(userName).orElseThrow(() -> new ServiceException("代理账号不存在!"));
        Long count = this.lambdaQuery().eq(DivideLayerConfig::getUserName, userName).count();
        Assert.isTrue(count <= 0,"不能重复添加！");
        DivideLayerConfig saveObj = DivideLayerConfig.builder()
                .userId(member.getId())
                .userName(member.getAccount())
                .build();

        Map<String,GameDivideVo> saveMap = new HashMap<>();
        if (member.getAgentLevel() != 1) {
            // 判断直属上级是否添加了分红  系统默认代理不用判断
            if (!member.getParentName().equalsIgnoreCase(SystemConstant.DEFAULT_WEB_ROOT) &&
                    !member.getParentName().equalsIgnoreCase(SystemConstant.DEFAULT_TEST_ROOT) &&
                        !member.getParentName().equalsIgnoreCase(SystemConstant.DEFAULT_WAP_ROOT)) {
                Assert.isTrue(StrUtil.isNotBlank(member.getSuperPath()), "代理路径为空！");
                // 顶级上级名称
                String realSuperName = this.getRealSuperName(member.getSuperPath());
                DivideLayerConfig superParentDivide = layerConfigMapper.getByUserName(realSuperName);
                if (BeanUtil.isEmpty(superParentDivide) || StrUtil.isBlank(superParentDivide.getDivideConfig())) {
                    throw new ServiceException("上级代理未添加分红！");
                }

                DivideLayerConfig parentDivide = new DivideLayerConfig();
                // 判断顶级上级和直属上级是不是同一个  是得话 可以避免一次查询
                if (member.getParentName().equalsIgnoreCase(realSuperName)) {
                    parentDivide = superParentDivide;
                } else {
                    parentDivide = layerConfigMapper.getByUserName(member.getParentName());
                }
                if (BeanUtil.isEmpty(parentDivide) || StrUtil.isBlank(parentDivide.getDivideConfig())) {
                    throw new ServiceException("上级代理未添加分红！");
                }

                // 将顶级的分红配置格式化取出来
                Map<String, JSONObject> superConfigMap = JSONUtil.toBean(superParentDivide.getDivideConfig(), Map.class);
                Map<String, JSONObject> parentConfigMap = JSONUtil.toBean(parentDivide.getDivideConfig(), Map.class);
                for (Map.Entry<String, JSONObject> divMap: superConfigMap.entrySet()) {
                    // 一级游戏编码
                    String code = divMap.getKey();
                    JSONObject json = divMap.getValue();
                    GameDivideVo saveVo = GameDivideVo.builder()
                            .liveGameName(json.getStr("liveGameName"))//游戏大类名称
                            .liveGameCode(json.getStr("liveGameCode"))//游戏大类编码
                            .code(json.getStr("code"))//一级游戏编码
                            .name(json.getStr("name"))//一级游戏名称
                            .amountRatio(json.getBigDecimal("amountRatio"))//金额比例
                            .settleType(json.getInt("settleType"))//结算方式
                            .divideRatio(BigDecimal.ZERO)//分红比例
                            .build();
                    if (ObjectUtils.isNull(parentConfigMap.get(code))) {
                        saveVo.setParentDivideRatio(BigDecimal.ZERO);
                    } else {
                        saveVo.setParentDivideRatio(parentConfigMap.get(code).getBigDecimal("divideRatio"));
                    }
                    saveMap.put(code,saveVo);
                }
                saveObj.setDivideConfig(JSONUtil.toJsonStr(saveMap));
            } else {// 走初始化流程
                saveObj.setDivideConfig(recommendConfigService.initDivideConfig(lang));
            }
        } else {// 也走初始化流程
            saveObj.setDivideConfig(recommendConfigService.initDivideConfig(lang));
        }
        Assert.isTrue(this.save(saveObj), "");
    }

    /**
     * 编辑层层代分红配置
     * @param divideConfigDTO
     * @param s
     */
    @Override
    public void edit(DivideConfigDTO divideConfigDTO, String s) {
        Assert.isTrue(divideConfigDTO.getId() != null, "主键参数缺失！");
        Assert.isTrue(divideConfigDTO.getUserId() != null, "用户ID参数缺失！");
        Assert.isTrue(StrUtil.isNotBlank(divideConfigDTO.getUserName()), "用户名参数缺失！");
        Assert.isTrue(CollectionUtil.isNotEmpty(divideConfigDTO.getOwnerConfigMap()), "分红配置参数缺失！");
        Map<String, List<GameDivideVo>> ownerConfigMap = divideConfigDTO.getOwnerConfigMap();
        Map<String,JSONObject> saveMap = new HashMap<>();
        for (Map.Entry<String,List<GameDivideVo>> map : ownerConfigMap.entrySet()) {
            List<GameDivideVo> value = map.getValue();
            for (GameDivideVo vo : value) {
                saveMap.put(vo.getCode(),JSONUtil.parseObj(vo));
            }
        }
        Assert.isTrue(BeanUtil.isNotEmpty(saveMap), "分红配置为空！");
        Member member = memberService.getAgentByAccount(divideConfigDTO.getUserName()).orElseThrow(() -> new ServiceException("代理账号不存在!"));

        Map<String, JSONObject> parentDivideMap = new HashMap<>();
        if (member.getAgentLevel() > 1) {
            DivideLayerConfig parentLayerConfig = layerConfigMapper.getByUserName(member.getParentName());
            if (BeanUtil.isEmpty(parentLayerConfig) || StrUtil.isBlank(parentLayerConfig.getDivideConfig())) {
                throw new ServiceException("上级代理未添加分红！");
            }
            parentDivideMap = JSONUtil.toBean(parentLayerConfig.getDivideConfig(),Map.class);
        }
        List<DivideLayerConfigVo> allChildConfig = this.getTeamLayerDivideConfig(divideConfigDTO.getUserName());
        List<SpreadLinkInfoVo> allSpreadLinkInfo = this.getTeamLinkInfo(divideConfigDTO.getUserName());
        for (Map.Entry<String,JSONObject> ownerMap : saveMap.entrySet()) {
            if (member.getAgentLevel() > 1 && BeanUtil.isNotEmpty(parentDivideMap)) {
                ownerMap.getValue().put("parentDivideRatio",
                        parentDivideMap.get(ownerMap.getKey()).getBigDecimal("divideRatio").subtract(ownerMap.getValue().getBigDecimal("divideRatio"))
                );
            }
            if (CollectionUtil.isNotEmpty(allChildConfig)) {
                for (DivideLayerConfigVo childVo : allChildConfig) {
                    Map<String, JSONObject> childMap = childVo.getDivideMap();
                    JSONObject jsonObject = childMap.get(ownerMap.getKey());
                    try {
                        jsonObject.put("liveGameName",ownerMap.getValue().getStr("liveGameName"));
                        jsonObject.put("liveGameCode",ownerMap.getValue().getStr("liveGameCode"));
                        jsonObject.put("code",ownerMap.getValue().getStr("code"));
                        jsonObject.put("name",ownerMap.getValue().getStr("name"));
                        jsonObject.put("settleType",ownerMap.getValue().getInt("settleType"));
                        jsonObject.put("amountRatio",ownerMap.getValue().getBigDecimal("amountRatio"));
                        // 如果是直属下级
                        if (childVo.getParentName().equals(member.getAccount())) {
                            jsonObject.put("parentDivideRatio",
                                    ownerMap.getValue().getBigDecimal("divideRatio").subtract(jsonObject.getBigDecimal("divideRatio"))
                            );
                        }
                    } catch (Exception e) {
                        childMap.remove(ownerMap.getKey());
                        continue;
                    }
                    childMap.put(ownerMap.getKey(),jsonObject);
                }
            }

            if (CollectionUtil.isNotEmpty(allSpreadLinkInfo)) {
                for (SpreadLinkInfoVo childVo : allSpreadLinkInfo) {
                    Map<String, JSONObject> childMap = childVo.getDivideMap();
                    if (BeanUtil.isEmpty(childMap)) {
                        continue;
                    }
                    JSONObject jsonObject = childMap.get(ownerMap.getKey());
                    try {
                        jsonObject.put("liveGameName",ownerMap.getValue().getStr("liveGameName"));
                        jsonObject.put("liveGameCode",ownerMap.getValue().getStr("liveGameCode"));
                        jsonObject.put("code",ownerMap.getValue().getStr("code"));
                        jsonObject.put("name",ownerMap.getValue().getStr("name"));
                        jsonObject.put("settleType",ownerMap.getValue().getInt("settleType"));
                        jsonObject.put("amountRatio",ownerMap.getValue().getBigDecimal("amountRatio"));
                        jsonObject.put("parentDivideRatio",
                                ownerMap.getValue().getBigDecimal("divideRatio").subtract(jsonObject.getBigDecimal("divideRatio"))
                        );
                    } catch (Exception e) {
                        childMap.remove(ownerMap.getKey());
                        continue;
                    }
                    childMap.put(ownerMap.getKey(),jsonObject);
                }
            }
        }

        if (CollectionUtil.isNotEmpty(allChildConfig)) {
            for (DivideLayerConfigVo layerConfig : allChildConfig) {
                try {
                    DivideLayerConfig editLayerObj = DivideLayerConfig.builder()
                            .id(layerConfig.getId())
                            .divideConfig(JSONUtil.toJsonStr(layerConfig.getDivideMap()))
                            .build();
                    layerConfigMapper.updateById(editLayerObj);
                } catch (Exception e) {
                    continue;
                }
            }
        }
        if (CollectionUtil.isNotEmpty(allSpreadLinkInfo)) {
            for (SpreadLinkInfoVo linkInfoVo : allSpreadLinkInfo) {
                SpreadLinkInfo editLinkObj = SpreadLinkInfo.builder()
                        .id(linkInfoVo.getId())
                        .divideConfig(JSONUtil.toJsonStr(linkInfoVo.getDivideMap()))
                        .build();
                spreadLinkInfoMapper.updateById(editLinkObj);
            }
        }

        DivideLayerConfig editObj = DivideLayerConfig.builder()
                .id(divideConfigDTO.getId())
                .divideConfig(JSONUtil.toJsonStr(saveMap))
                .build();
        int i = layerConfigMapper.updateById(editObj);

        Assert.isTrue(i > 0, "编辑失败！");
    }

    /**
     * 获取真实代理路径----替换掉系统拼接的三个代理
     * @param oldSuperPath
     * @return
     */
    public String getRealSuperPath(String oldSuperPath) {
        oldSuperPath = oldSuperPath.replace("/".concat(SystemConstant.DEFAULT_WEB_ROOT).concat("/"),"");
        oldSuperPath = oldSuperPath.replace("/".concat(SystemConstant.DEFAULT_TEST_ROOT).concat("/"),"");
        oldSuperPath = oldSuperPath.replace("/".concat(SystemConstant.DEFAULT_WAP_ROOT).concat("/"),"");
        return oldSuperPath;
    }

    /**
     * 根据代理路径获取到真实 顶级 代理 ---- 不算系统的三个代理
     * @param superPath
     * @return
     */
    public String getRealSuperName(String superPath){
        superPath = getRealSuperPath(superPath);
        if (superPath.startsWith("/")) {
            superPath = superPath.substring(1);
        }
        if (superPath.endsWith("/")) {
            superPath = superPath.substring(0, superPath.length()-1);
        }
        return superPath.split("/")[0];
    }

    /**
     * 获取所有直接下已经配置了的分红
     * @param userName
     * @return
     */
    public List<DivideLayerConfigVo> getTeamLayerDivideConfig(String userName){
        List<DivideLayerConfigVo> childDivideConfig = layerConfigMapper.getTeamList(userName);
        childDivideConfig = childDivideConfig.stream().filter(item -> StrUtil.isNotBlank(item.getDivideConfig())).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(childDivideConfig)) {
            for (DivideLayerConfigVo vo : childDivideConfig) {
                String divideConfig = vo.getDivideConfig();
                if (StrUtil.isBlank(divideConfig)) {
                    continue;
                }
                // 将分红配置变成一个map
                vo.setDivideMap(JSONUtil.toBean(divideConfig,Map.class));
            }
        }
        return childDivideConfig;
    }

    /**
     * 获取所有直接下已经配置了的分红
     * @param userName
     * @return
     */
    public List<SpreadLinkInfoVo> getTeamLinkInfo(String userName){
        List<SpreadLinkInfoVo> recommendLink = spreadLinkInfoMapper.getTeamLinkInfo(new SpreadLinkInfoDTO() {{
            setAgentAccount(userName);
        }});
        recommendLink = recommendLink.stream().filter(item -> StrUtil.isNotBlank(item.getDivideConfig())).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(recommendLink)) {
            for (SpreadLinkInfoVo vo : recommendLink) {
                String divideConfig = vo.getDivideConfig();
                if (StrUtil.isBlank(divideConfig)) {
                    continue;
                }
                // 将分红配置变成一个map
                vo.setDivideMap(JSONUtil.toBean(divideConfig,Map.class));
            }
        }
        return recommendLink;
    }
}