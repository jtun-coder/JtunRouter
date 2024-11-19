package com.jj.routingservice.http.response;

public class DeviceInfo {
    private String iccid = "iccid";
    private String imei = "imei";
    private long linkTime;//连接时间
    private String wan_ipv4;//wan ip
    private String wan_ipv6;
    private String ip;//本机Ip
    private String mac;
    private float cpuTemp;//cpu温度
    private float cpuUsage;//cpu使用率 百分比
    private float memoryUsage;//内存使用率 百分比
    private String model;//设备型号
    private String firmwareVersion;//硬件信息
    private String kernelVersion;//软件版本信息
    private long uptime;//运行时间
    private long systemTime;//系统时间
    private long dataPlan;//计划流量
    private long monthUsed;//当月已使用
    private String simType;//sim卡类型

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

    public long getLinkTime() {
        return linkTime;
    }

    public void setLinkTime(long linkTime) {
        this.linkTime = linkTime;
    }

    public String getWan_ipv4() {
        return wan_ipv4;
    }

    public void setWan_ipv4(String wan_ipv4) {
        this.wan_ipv4 = wan_ipv4;
    }

    public String getWan_ipv6() {
        return wan_ipv6;
    }

    public void setWan_ipv6(String wan_ipv6) {
        this.wan_ipv6 = wan_ipv6;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public float getCpuTemp() {
        return cpuTemp;
    }

    public void setCpuTemp(float cpuTemp) {
        this.cpuTemp = cpuTemp;
    }

    public float getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(float cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public float getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(float memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public String getKernelVersion() {
        return kernelVersion;
    }

    public void setKernelVersion(String kernelVersion) {
        this.kernelVersion = kernelVersion;
    }

    public long getUptime() {
        return uptime;
    }

    public void setUptime(long uptime) {
        this.uptime = uptime;
    }

    public long getSystemTime() {
        return systemTime;
    }

    public void setSystemTime(long systemTime) {
        this.systemTime = systemTime;
    }

    public long getDataPlan() {
        return dataPlan;
    }

    public void setDataPlan(long dataPlan) {
        this.dataPlan = dataPlan;
    }

    public long getMonthUsed() {
        return monthUsed;
    }

    public void setMonthUsed(long monthUsed) {
        this.monthUsed = monthUsed;
    }

    public String getSimType() {
        return simType;
    }

    public void setSimType(String simType) {
        this.simType = simType;
    }
}
