package com.jtun.router.http;

import android.net.MacAddress;
import android.text.TextUtils;

import com.jtun.router.net.wifi.SoftApConfigurationCompat;
import com.jtun.router.net.wifi.WifiSsidCompat;

import java.util.ArrayList;
import java.util.List;

public class ApConfig {
    private String ssid;//名称
    private String bssid;//mac
    private String passphrase;//密码
    private boolean isHiddenSsid;//是否隐藏网络
    //安全模式 SECURITY_TYPE_OPEN = 0;SECURITY_TYPE_WPA2_PSK = 1;SECURITY_TYPE_WPA3_SAE_TRANSITION = 2;SECURITY_TYPE_WPA3_SAE = 3;
    // SECURITY_TYPE_WPA3_OWE_TRANSITION = 4; SECURITY_TYPE_WPA3_OWE = 5;
    private int securityType;
    private int maxClients;//最大客户端数量
    private boolean isAutoShutdown;//未连接客户端是否自动关闭
    private long shutdownTimeout;//关闭延时(毫秒)
    private boolean isClientControl;//是否过滤设备
    private List<String> blockedList;//设备黑名单
    private List<String> allowedList;//设备白名单
    private boolean isWifi6 = true;//是否启用Wifi6
    private boolean isWifi7 = true;//是否启用Wifi7
    /**
     * 数组记录两个参数，如果 相同只保留一个如两个都是3 则为3=0  一个1一个3则为 1=0,3=0
     */
    private int band;//1 2.4GHZ 2 5GHZ 3 2.4/5GHz
    private int channel;//* 2G  1-11  5G  36,40,44,48,149,153,157,161,165
    private int bandWidth;// -1 Auto 2 20MHZ 3 40MHZ 4 80MHZ

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    public boolean isHiddenSsid() {
        return isHiddenSsid;
    }

    public void setHiddenSsid(boolean hiddenSsid) {
        isHiddenSsid = hiddenSsid;
    }

    public int getSecurityType() {
        return securityType;
    }

    public void setSecurityType(int securityType) {
        this.securityType = securityType;
    }

    public int getMaxClients() {
        return maxClients;
    }

    public void setMaxClients(int maxClients) {
        this.maxClients = maxClients;
    }

    public boolean isAutoShutdown() {
        return isAutoShutdown;
    }

    public void setAutoShutdown(boolean autoShutdown) {
        isAutoShutdown = autoShutdown;
    }

    public long getShutdownTimeout() {
        return shutdownTimeout;
    }

    public void setShutdownTimeout(long shutdownTimeout) {
        this.shutdownTimeout = shutdownTimeout;
    }

    public boolean isClientControl() {
        return isClientControl;
    }

    public void setClientControl(boolean clientControl) {
        isClientControl = clientControl;
    }

    public List<String> getBlockedList() {
        return blockedList;
    }

    public void setBlockedList(List<String> blockedList) {
        this.blockedList = blockedList;
    }

    public List<String> getAllowedList() {
        return allowedList;
    }

    public void setAllowedList(List<String> allowedList) {
        this.allowedList = allowedList;
    }

    public boolean isWifi6() {
        return isWifi6;
    }

    public void setWifi6(boolean wifi6) {
        isWifi6 = wifi6;
    }

    public boolean isWifi7() {
        return isWifi7;
    }

    public void setWifi7(boolean wifi7) {
        isWifi7 = wifi7;
    }

    public int getBand() {
        return band;
    }

    public void setBand(int band) {
        this.band = band;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getBandWidth() {
        return bandWidth;
    }

    public void setBandWidth(int bandWidth) {
        this.bandWidth = bandWidth;
    }

    public void softInitData(SoftApConfigurationCompat softApConfigurationCompat){
        if(softApConfigurationCompat != null){
            if(softApConfigurationCompat.getSsid() != null)
                this.ssid = softApConfigurationCompat.getSsid().toString();
            if(softApConfigurationCompat.getBssid() != null)
                this.bssid = softApConfigurationCompat.getBssid().toString();
            this.passphrase = softApConfigurationCompat.getPassphrase();
            this.isHiddenSsid = softApConfigurationCompat.isHiddenSsid();
            this.securityType = softApConfigurationCompat.getSecurityType();
            this.maxClients = softApConfigurationCompat.getMaxNumberOfClients();
            this.isAutoShutdown = softApConfigurationCompat.isAutoShutdownEnabled();
            this.shutdownTimeout = softApConfigurationCompat.getShutdownTimeoutMillis();
            this.isClientControl = softApConfigurationCompat.isClientControlByUserEnabled();
            this.blockedList = new ArrayList<>();
            for (MacAddress macAddress : softApConfigurationCompat.getBlockedClientList()){
                blockedList.add(macAddress.toString());
            }
            this.allowedList = new ArrayList<>();
            for (MacAddress macAddress : softApConfigurationCompat.getAllowedClientList()){
                allowedList.add(macAddress.toString());
            }
            this.isWifi6 = softApConfigurationCompat.isIeee80211axEnabled();
            this.isWifi7 = softApConfigurationCompat.isIeee80211beEnabled();
            this.band = softApConfigurationCompat.getChannels().keyAt(0);
//            if(softApConfigurationCompat.getChannels().size() > 1){
            this.channel = softApConfigurationCompat.getChannels().valueAt(0);
//            }
            this.bandWidth = softApConfigurationCompat.getMaxChannelBandwidth();
        }
    }
    public void data2SoftApConfig(SoftApConfigurationCompat softApConfigurationCompat){
        if(softApConfigurationCompat != null){
            softApConfigurationCompat.setSsid(WifiSsidCompat.Companion.fromUtf8Text(ssid,false));
            if(!TextUtils.isEmpty(bssid))
                softApConfigurationCompat.setBssid(MacAddress.fromString(bssid));
            softApConfigurationCompat.setPassphrase(passphrase);
            softApConfigurationCompat.setHiddenSsid(isHiddenSsid);
            softApConfigurationCompat.setSecurityType(securityType);
            softApConfigurationCompat.setMaxNumberOfClients(maxClients);
            softApConfigurationCompat.setAutoShutdownEnabled(isAutoShutdown);
            softApConfigurationCompat.setShutdownTimeoutMillis(shutdownTimeout);
            softApConfigurationCompat.setClientControlByUserEnabled(isClientControl);
            softApConfigurationCompat.getBlockedClientList().clear();
            if(blockedList != null){
                for (String mac : blockedList){
                    softApConfigurationCompat.getBlockedClientList().add(MacAddress.fromString(mac));
                }
            }
            softApConfigurationCompat.getAllowedClientList().clear();
            if(allowedList != null){
                for (String mac : allowedList){
                    softApConfigurationCompat.getAllowedClientList().add(MacAddress.fromString(mac));
                }
            }
            softApConfigurationCompat.setIeee80211axEnabled(isWifi6);
            softApConfigurationCompat.setIeee80211beEnabled(isWifi7);
            softApConfigurationCompat.getChannels().clear();
//            if(band > 0){
//                softApConfigurationCompat.getChannels().append(band,0);
//            }
//            if(band != channel && channel > 0){
//            }
            softApConfigurationCompat.getChannels().put(band,channel);
            softApConfigurationCompat.setMaxChannelBandwidth(bandWidth);
        }
    }
}
