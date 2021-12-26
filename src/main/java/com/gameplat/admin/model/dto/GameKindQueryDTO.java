package com.gameplat.admin.model.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class GameKindQueryDTO implements Serializable {
    private String gameType;

    private String platformCode;

    /**
     * 是否热门(0：否；1:是)
     */
    private Integer hot;
}
