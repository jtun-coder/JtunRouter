package com.jj.routingservice.http.response;

public class InternetInfo {
    private String iccid = "iccid";
    private String imei = "imei";
    private String operatorName;
    private String networkType;
    private int BAND;
    private int earfcn;
    private int RSRP;
    private int RSSI;
    private int RERQ;
    private int CellID;
    private int PCI;

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public int getBAND() {
        return BAND;
    }

    public void setBAND(int BAND) {
        this.BAND = BAND;
    }

    public int getEarfcn() {
        return earfcn;
    }

    public void setEarfcn(int earfcn) {
        this.earfcn = earfcn;
    }

    public int getRSRP() {
        return RSRP;
    }

    public void setRSRP(int RSRP) {
        this.RSRP = RSRP;
    }

    public int getRSSI() {
        return RSSI;
    }

    public void setRSSI(int RSSI) {
        this.RSSI = RSSI;
    }

    public int getRERQ() {
        return RERQ;
    }

    public void setRERQ(int RERQ) {
        this.RERQ = RERQ;
    }

    public int getCellID() {
        return CellID;
    }

    public void setCellID(int cellID) {
        CellID = cellID;
    }

    public int getPCI() {
        return PCI;
    }

    public void setPCI(int PCI) {
        this.PCI = PCI;
    }
}
