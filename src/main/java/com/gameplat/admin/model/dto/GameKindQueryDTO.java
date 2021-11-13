package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GameKindQueryDTO implements Serializable {
    private String gameType;

    private String platformCode;
}
