package com.gameplat.admin.service.live.api.ae.bean;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AeLoginAndLaunchRep implements Serializable {

    private String url;

    private String status;

    private String extension;
}
