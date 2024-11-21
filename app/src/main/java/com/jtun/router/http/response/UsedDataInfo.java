package com.jtun.router.http.response;

import com.jtun.router.control.WifiApControl;

public class UsedDataInfo {
    private long today;
    private long month;
    private long all;

    public long getToday() {
        return today;
    }

    public void setToday(long today) {
        this.today = today;
    }

    public long getMonth() {
        return month;
    }

    public void setMonth(long month) {
        this.month = month;
    }

    public long getAll() {
        return all;
    }

    public void setAll(long all) {
        this.all = all;
    }

    public void initData(){
        today = WifiApControl.Companion.getInstance().getTodayUsed();
        month = WifiApControl.Companion.getInstance().getMonthUsed();
        all = WifiApControl.Companion.getInstance().getAllUsed();
    }


}
