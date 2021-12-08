package com.gameplat.admin.model.bean.router;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;

/**
 * Vue路由 Meta
 * @author three
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RouterMeta implements Serializable {

    private static final long serialVersionUID = 5499925008927195914L;

    public RouterMeta(){
        closeable = false;
        isShow = false;
    }

    public RouterMeta(Boolean _closeable, Boolean _isShow) {
        closeable = _closeable;
        isShow = _isShow;
    }

    /**
     * 允许关闭
     */
    private Boolean closeable;

    /**
     * 是否已显示
     */
    private Boolean isShow;

    public Boolean getCloseable() {
        return closeable;
    }

    public void setCloseable(Boolean closeable) {
        this.closeable = closeable;
    }

    public Boolean getShow() {
        return isShow;
    }

    public void setShow(Boolean show) {
        isShow = show;
    }
}
