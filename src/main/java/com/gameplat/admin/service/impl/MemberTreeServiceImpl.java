package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.dao.MemberTreeMapper;
import com.gameplat.admin.model.entity.MemberTree;
import com.gameplat.admin.service.MemberTreeService;
import com.gameplat.admin.utils.TreeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Lenovo
 */
@Service
public class MemberTreeServiceImpl extends ServiceImpl<MemberTreeMapper, MemberTree> implements
        MemberTreeService {

    @Autowired
    private MemberTreeMapper memberTreeMapper;


    /**
     * 获取某一级分类的数量，参数从1开始，表示第一级分类（根分类的子类）。
     *
     * @param layer 层级（从1开始）
     * @return 数量
     * @throws IllegalArgumentException 如果layer不是正数
     */
    @Override
    public int countOfLayer(int layer) {
        TreeUtils.checkPositive(layer, "layer");
        return this.lambdaQuery().eq(MemberTree::getAncestor, 0)
                .eq(MemberTree::getDistance, layer).count();
    }


}
