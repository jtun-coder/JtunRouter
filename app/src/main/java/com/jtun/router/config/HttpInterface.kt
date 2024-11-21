package com.jtun.router.config

object HttpInterface {
    const val LOGIN = "/login"//接口地址
    const val SET_ADMIN_PASS = "/setAdminPass"//修改管理密码
    const val NET_STATE = "/netState"//连接状态
    const val DEVICE_INFO = "/deviceInfo"//设备信息
    const val AP_CONFIG = "/apConfig"//获取热点配置信息
    const val SET_AP_CONFIG = "/setApConfig"//修改热点信息
    const val GET_CLIENTS = "/getClients"//获取连接客户端列表
    const val SET_CLIENT = "/setClient"//修改客户端
    const val DELETE_CLIENT = "/deleteClient"//删除客户端
    const val GET_INTERNET_INFO = "/internetInfo"//获取网络信息
    const val SET_DATA_PLAN = "/setDataPlan"//设置数据流量
    const val GET_USED_INFO = "/getUsedInfo"//获取使用流量信息
    const val DELETE_CLIENT_LIST = "/deleteClientList"//删除多个客户端
    const val GET_APP_LIST = "/getApps"//获取已安装第三方应用列表
    const val START_APP = "/startApp"//启动指定应用
    const val STOP_APP = "/stopApp"//停止指定应用
    const val V2RAY_CONFIG = "/v2rayGetConfig"//获取v2ray配置信息
    const val V2RAY_IMPORT = "/v2rayImport" //导入v2ray配置信息
    const val V2RAY_START = "/v2rayStart" //启动v2ray服务
    const val V2RAY_STOP = "/v2rayStop" //停止v2ray服务
    const val V2RAY_CHECK_CONFIG = "/v2rayCheckConfig"//选中指定配置
    const val V2RAY_DELETE_CONFIG =  "/v2rayDeleteConfig"//删除指定配置
    const val V2RAY_PING_TEST =  "/v2rayPingTest"//测试网络

    const val ALIST_STATE = "/alistState"//alist运行状态
    const val ALIST_START = "/alistStart"//alist启动
    const val ALIST_STOP = "/alistStop"//alist停止运行
    const val ALIST_ADMIN_PASS = "/alistAdminPass"//alist管理密码

    const val OPENVPN_IMPORT = "/openvpnImport"//导入openvpn
    const val OPENVPN_START = "/openvpnStart"//启动
    const val OPENVPN_STOP = "/openvpnStop"//停止
    const val OPENVPN_LIST = "/openvpnList"//获取列表
    const val OPENVPN_REMOVE = "/openvpnRemove"//删除配置信息

    const val APP_INSTALL = "/appInstall"//安装
    const val APP_UNINSTALL = "/appUninstall"//卸载
    const val INSTALL_INFO = "/appInstallInfo"//获取安装信息

    const val SMS_BY_CONTACT = "/smsByContact"//获取短信会话，获取最新一条短信
    const val SMS_LIST = "/smsList"//获取短信列表
    const val SEND_SMS = "/sendSms"//发送短信
    const val READ_SMS = "/readSms"//已读短信e
    const val DELETE_SMS = "/deleteSms"//删除短信
    const val DELETE_SMS_CONTACT = "/deleteSmsByContact"//删除短信会话
    const val DELETE_SMS_LIST = "/deleteSmsList"//根据id列表删除短信
    const val BLACK_CLIENT="/blackClient"//添加设备黑名单
    const val BLACK_CLIENT_REMOVE="/removeBlackClient"//删除设备黑名单
    const val LOG_LIST="/logList"//获取设备日志
    const val REFRESH_LIST_BY_ID="/refreshListById"//根据id获取设备最新日志
    const val MORE_LIST_BY_ID="/moreListById"//根据id获取设备最新日志
}
