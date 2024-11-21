/*
 * Copyright (c) 2012-2024 Arne Schwabe
 * Distributed under the GNU GPL v2 with additional terms. For full terms see the file doc/LICENSE.txt
 */

package com.jtun.router.http.response.openvpn

/**
 * open vpn 配置信息
 * uuid 唯一标识
 * name 名称
 * message 连接显示状态
 * active 是否连接
 */
data class OpenvpnItem (val uuid:String,val name:String,val message:String,val active:Boolean,val servers: List<ItemServer>)