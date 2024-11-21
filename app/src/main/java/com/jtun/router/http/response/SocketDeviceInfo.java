package com.jtun.router.http.response;

import com.jtun.router.http.ApConfig;
import com.jtun.router.room.ClientConnected;

import java.util.List;

public class SocketDeviceInfo {
    private ApConfig apconfig;
    private DeviceInfo device;
    private List<ClientConnected> client;

    public ApConfig getApconfig() {
        return apconfig;
    }

    public void setApconfig(ApConfig apconfig) {
        this.apconfig = apconfig;
    }

    public DeviceInfo getDevice() {
        return device;
    }

    public void setDevice(DeviceInfo device) {
        this.device = device;
    }

    public List<ClientConnected> getClient() {
        return client;
    }

    public void setClient(List<ClientConnected> client) {
        this.client = client;
    }
}
