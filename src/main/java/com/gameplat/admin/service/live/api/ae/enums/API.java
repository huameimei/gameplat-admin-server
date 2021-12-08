package com.gameplat.admin.service.live.api.ae.enums;

public enum API {
    LOGIN_AND_LAUNCH("AE登陆并启动游戏", "/wallet/doLoginAndLaunchGame"),

    CREATE_MEMBER("AE创建会员", "/wallet/createMember"),

    GET_BALANCE("AE查询余额", "/wallet/getBalance"),

    TRANSER_IN("AE转入", "/wallet/deposit"),

    TRANSER_OUT("AE转出", "/wallet/withdraw"),

    CHECK_TRANSFER_OPERATION("AE检查交易状态", "/wallet/checkTransferOperation"),

    GET_BET_RECORD_BY_TX_TIME("AE获取交易记录", "/fetch/getTransactionByTxTime");

    /** 名称 */
    private String name;

    /** 地址 */
    private String url;

    API(String name, String url) {
      this.name = name;
      this.url = url;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }
  }