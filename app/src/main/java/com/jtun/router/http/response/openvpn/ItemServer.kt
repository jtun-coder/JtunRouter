/*
 * Copyright (c) 2012-2024 Arne Schwabe
 * Distributed under the GNU GPL v2 with additional terms. For full terms see the file doc/LICENSE.txt
 */

package com.jtun.router.http.response.openvpn

data class ItemServer(val serverName:String,val port:String,val useUdp:Boolean,val enabled:Boolean,val timeout: Int)
