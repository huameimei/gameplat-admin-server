package com.gameplat.admin.model.bean;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class NameValuePair implements Serializable {
  private static final long serialVersionUID = 3821538825409856504L;

  @NotBlank
  @Length(max = 100)
  private String name;

  @Length(max = 255)
  private String value;

  public NameValuePair() {}

  public NameValuePair(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return this.value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
