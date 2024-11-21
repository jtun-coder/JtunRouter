package com.jtun.router.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.CellIdentityNr;
import android.telephony.CellInfo;
import android.telephony.CellInfoNr;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrengthNr;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.format.Formatter;

import com.jtun.router.App;
import com.jtun.router.http.response.InternetInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public class NetworkUtils {
    public static String URL = "https://www.baidu.com";
    public static int NET_CNNT_BAIDU_OK = 1; // NetworkAvailable
    public static int NET_CNNT_BAIDU_TIMEOUT = 2; // no NetworkAvailable
    public static int NET_NOT_PREPARE = 3; // Net no ready
    public static int NET_ERROR = 4; //net error
    private static final String TAG = "ConnectManager";

    /**
     * Indicates this network uses a Cellular transport.
     */
    public static final int TRANSPORT_CELLULAR = 0;

    /**
     * Indicates this network uses a Wi-Fi transport.
     */
    public static final int TRANSPORT_WIFI = 1;

    /**
     * Indicates this network uses a Bluetooth transport.
     */
    public static final int TRANSPORT_BLUETOOTH = 2;

    /**
     * Indicates this network uses an Ethernet transport.
     */
    public static final int TRANSPORT_ETHERNET = 3;

    /**
     * Indicates this network uses a VPN transport.
     */
    public static final int TRANSPORT_VPN = 4;

    /**
     * Indicates this network uses a Wi-Fi Aware transport.
     */
    public static final int TRANSPORT_WIFI_AWARE = 5;

    /**
     * Indicates this network uses a LoWPAN transport.
     */
    public static final int TRANSPORT_LOWPAN = 6;

    /**
     * Indicates this network uses a Test-only virtual interface as a transport.
     * @hide
     */
    public static final int TRANSPORT_TEST = 7;

    /**
     * Indicates this network uses a USB transport.
     */
    public static final int TRANSPORT_USB = 8;



    /**
     * >= Android 10（Q版本）推荐
     * 当前使用MOBILE流量上网
     */
    public static boolean isMobileNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        Network network = cm.getActiveNetwork();
        if (null == network) {
            return false;
        }

        NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
        if (null == capabilities) {
            return false;
        }
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }


    /**
     * >= Android 10（Q版本）推荐
     *
     * 当前使用WIFI上网
     */

    public static boolean isWifiNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = cm.getActiveNetwork();
        if (null == network) {
            return false;
        }
        NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
        if (null == capabilities) {
            return false;
        }
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }


    /**
     *  >= Android 10（Q版本）推荐
     *
     * 当前使用以太网上网
     */
    public static boolean isEthernetNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = cm.getActiveNetwork();
        if (null == network) {
            return false;
        }
        NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
        if (null == capabilities) {
            return false;
        }
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }



    /**
     * >= Android 10（Q版本）推荐
     *
     * NetworkCapabilities.NET_CAPABILITY_INTERNET，表示此网络应该(maybe)能够访问internet
     *
     *  判断当前网络可以正常上网
     *  表示此连接此网络并且能成功上网。  例如，对于具有NET_CAPABILITY_INTERNET的网络，这意味着已成功检测到INTERNET连接。
     */
    public static boolean isConnectedAvailableNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = cm.getActiveNetwork();
        if (null == network) {
            return false;
        }
        NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
        if (null == capabilities) {
            return false;
        }
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }


    /**
     * >= Android 10（Q版本）推荐
     *
     * 获取成功上网的网络类型
     * value = {
     *    TRANSPORT_CELLULAR,   0 表示此网络使用蜂窝传输。
     *    TRANSPORT_WIFI,       1 表示此网络使用Wi-Fi传输。
     *    TRANSPORT_BLUETOOTH,  2 表示此网络使用蓝牙传输。
     *    TRANSPORT_ETHERNET,   3 表示此网络使用以太网传输。
     *    TRANSPORT_VPN,        4 表示此网络使用VPN传输。
     *    TRANSPORT_WIFI_AWARE, 5 表示此网络使用Wi-Fi感知传输。
     *    TRANSPORT_LOWPAN,     6 表示此网络使用LoWPAN传输。
     *    TRANSPORT_TEST,       7 指示此网络使用仅限测试的虚拟接口作为传输。
     *    TRANSPORT_USB,        8 表示此网络使用USB传输
     * }
     */
    public static int getConnectedNetworkType(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = cm.getActiveNetwork();
        if (null == network) {
            return -1;
        }
        NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
        if (null == capabilities) {
            return -1;
        }
        if (capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return NetworkCapabilities.TRANSPORT_CELLULAR;
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return NetworkCapabilities.TRANSPORT_WIFI;
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)) {
                return NetworkCapabilities.TRANSPORT_BLUETOOTH;
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return NetworkCapabilities.TRANSPORT_ETHERNET;
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                return NetworkCapabilities.TRANSPORT_VPN;
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE)) {
                return NetworkCapabilities.TRANSPORT_WIFI_AWARE;
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN)) {
                return NetworkCapabilities.TRANSPORT_LOWPAN;
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_USB)) {
                return NetworkCapabilities.TRANSPORT_USB;
            }
        }
        return -1;
    }
    /**
     *ping "http://www.baidu.com"
     * @return
     */
    static private boolean connectionNetwork() {
        boolean result = false;
        HttpURLConnection httpUrl = null;
        try {
            httpUrl = (HttpURLConnection) new URL(URL)
                    .openConnection();
            httpUrl.setConnectTimeout(3000);
            httpUrl.connect();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != httpUrl) {
                httpUrl.disconnect();
            }
            httpUrl = null;
        }
        return result;
    }
    /**
     * 返回当前网络状态
     *
     * @param context
     * @return
     */
    public static int getNetState(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo networkinfo = connectivity.getActiveNetworkInfo();
                if (networkinfo != null) {
                    if (networkinfo.isAvailable() && networkinfo.isConnected()) {
                        if (!connectionNetwork())
                            return NET_CNNT_BAIDU_TIMEOUT;
                        else
                            return NET_CNNT_BAIDU_OK;
                    } else {
                        return NET_NOT_PREPARE;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return NET_ERROR;
    }

    public static InetAddress getActiveAddress() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ConnectivityManager connectivityManager = (ConnectivityManager) App.app.getSystemService(Context.CONNECTIVITY_SERVICE);
        Method activeLinkPropertiesMethod = connectivityManager.getClass().getDeclaredMethod("getActiveLinkProperties");
        activeLinkPropertiesMethod.setAccessible(true);
        Object linkProperties = activeLinkPropertiesMethod.invoke(connectivityManager);
        if (linkProperties == null) return null;
        Method addressesMethod = linkProperties.getClass().getDeclaredMethod("getAddresses");
        Collection<InetAddress> inetAddresses = (Collection<InetAddress>)addressesMethod.invoke(linkProperties);
        Iterator<InetAddress> iterator = inetAddresses.iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }

    // 获取本地IP
    public static String getLocalIpv4Address() {
        String ipv4 = "0.0.0.0";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface networkInterface = en.nextElement();
                KLog.i("getName ： " + networkInterface.getDisplayName());
                if("wlan0".equals(networkInterface.getDisplayName())){
                    for (Enumeration<InetAddress> enumIpAddress = networkInterface.getInetAddresses(); enumIpAddress.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddress.nextElement();
                        if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress instanceof Inet4Address) {
                            ipv4 = inetAddress.getHostAddress();
                            KLog.i("ipv4 :" + ipv4);
                            return ipv4;
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return ipv4;
    }

    // 获取本地IP
    public static String getLocalIpv6Address() {
        String ipv6 = "0.0.0.0";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface networkInterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddress = networkInterface.getInetAddresses(); enumIpAddress.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddress.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress instanceof Inet6Address) {
                        KLog.i("inetAddress : " + inetAddress.getHostAddress());
                        ipv6 = inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return ipv6;
    }
    public static String getGatewayIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
        String gateway = Formatter.formatIpAddress(dhcpInfo.gateway);
        return gateway;
    }
    public static String getOperatorName(Context context) {
        // 获取TelephonyManager实例
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        // 检查是否不为空
        if (telephonyManager != null) {
            // 获取运营商名称
            return telephonyManager.getNetworkOperatorName();
        }

        return "Unknown";
    }
    public static String getNetworkType(TelephonyManager telephonyManager){
        // 获取 TelephonyManager 实例
        // 获取手机网络类型
        int networkType = telephonyManager.getNetworkType();
        String networkMode;
        // 根据网络类型判断网络模式
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                networkMode = "2G";
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                networkMode = "3G";
                break;
            case TelephonyManager.NETWORK_TYPE_LTE:
                networkMode = "4G";
                break;
            case TelephonyManager.NETWORK_TYPE_NR:
                networkMode = "5G";
                break;
            default:
                networkMode = "Unknown";
                break;
        }
        return networkMode;
    }

    public static InternetInfo getCellInfo(Context context) {
        InternetInfo internetInfo = new InternetInfo();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            int cellid = 0;
            internetInfo.setImei(telephonyManager.getImei());
            internetInfo.setIccid(telephonyManager.getSimSerialNumber());
            internetInfo.setOperatorName(telephonyManager.getNetworkOperatorName());
            internetInfo.setNetworkType(getNetworkType(telephonyManager));
            CellLocation cel = telephonyManager.getCellLocation();
            //移动联通 GsmCellLocation
            if (cel instanceof GsmCellLocation) {
                GsmCellLocation gsmCellLocation = (GsmCellLocation) cel;
                cellid = gsmCellLocation.getCid();
            }else if(cel instanceof CdmaCellLocation){
                //电信   CdmaCellLocation
                CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) cel;
                cellid = cdmaCellLocation.getBaseStationId();
            }
            KLog.i("cellid : " + cellid);
            internetInfo.setCellID(cellid);
            //telephonyManager 自己创建一个哈，就不写了
            List<CellInfo> allCellInfo = telephonyManager.getAllCellInfo();
            if (allCellInfo != null && allCellInfo.size() > 0) {
                CellInfo info = allCellInfo.get(0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // 5G
                    if (info instanceof CellInfoNr) {
                        CellIdentityNr nr = (CellIdentityNr) info
                                .getCellIdentity();
                        CellSignalStrengthNr nrStrength = (CellSignalStrengthNr) info
                                .getCellSignalStrength();
                        if(nr.getBands().length > 0){
                            internetInfo.setBAND(nr.getBands()[0]);
                        }
                        internetInfo.setRSRP(nrStrength.getSsRsrp());
                        internetInfo.setRERQ(nrStrength.getSsRsrq());
                        internetInfo.setPCI(nr.getPci());
                    }
                }
            }
        }
        return internetInfo;
    }

    public static String ping(String host) {
        Runtime runtime = Runtime.getRuntime();
        java.lang.Process ipProcess = null;
        InputStream inputStrem = null;
        BufferedReader in = null;
        StringBuilder stringBuffer = new StringBuilder();
        try {
            ipProcess = runtime.exec("ping -c 4 -w 5 " + host);
            inputStrem = ipProcess.getInputStream();
            in = new BufferedReader(new InputStreamReader(inputStrem));
            String content = "";

            while ((content = in.readLine()) != null) {
                if(stringBuffer.length() > 0){
                    stringBuffer.append("\n");
                }
                stringBuffer.append(content);
            }
            int exitValue = ipProcess.waitFor();
            KLog.i(TAG, "return result after executing the ping command: " + exitValue);
        } catch(IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (ipProcess != null) {
                ipProcess.destroy();
            }

            try {
                if (in != null) {
                    in.close();
                }
                if (inputStrem != null) {
                    inputStrem.close();
                }
            } catch (IOException e) {
                KLog.e(TAG, " IOException " + e);
            }

            runtime.gc();
        }
        return stringBuffer.toString();
    }
}
