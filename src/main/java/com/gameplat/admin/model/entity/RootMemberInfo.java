package com.gameplat.admin.model.entity;

/**
 * 根分类，覆盖了分类的一些方法，防止这些操作被错误地用到顶级分类上。
 */
public class RootMemberInfo extends MemberInfo {
    @Override
    public void moveTo(Long target) {
        throw new UnsupportedOperationException("根用户不支持此操作");
    }

    @Override
    public void moveTreeTo(Long target) {
        throw new UnsupportedOperationException("根用户不支持此操作");
    }
}
