package com.gameplat.admin.model.vo;

import java.util.List;
import lombok.Data;

/**
 * @author lily
 * @description
 * @date 2021/11/20
 */
@Data
public class MemberConfigLevelVO {

    private MemberGrowthConfigVO configVO;

    private List<MemberGrowthLevelVO> levelVO;
}
