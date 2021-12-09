package com.gameplat.admin.model.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class GameKindQueryDTO implements Serializable {
    private String gameType;

    private String platformCode;
}
