package com.acloudchina.app.bean;

public class TvBean {
    private boolean iscontract;//是否在合约期
    private boolean islegal;// IP是否合法

    public boolean isIscontract() {
        return iscontract;
    }

    public void setIscontract(boolean iscontract) {
        this.iscontract = iscontract;
    }

    public boolean isIslegal() {
        return islegal;
    }

    public void setIslegal(boolean islegal) {
        this.islegal = islegal;
    }

    @Override
    public String toString() {
        return "TvBean{" +
                "iscontract='" + iscontract + '\'' +
                ", islegal='" + islegal + '\'' +
                '}';
    }
}
