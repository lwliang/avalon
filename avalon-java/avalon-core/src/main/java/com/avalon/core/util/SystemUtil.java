/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.util;

import com.avalon.core.exception.AvalonException;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Formatter;
import java.util.Locale;
import java.util.Map;

/**
 * 获取操作系统信息
 */
public class SystemUtil {
    /**
     * 获取MAC地址的方法
     *
     * @return
     */
    public static String getMACAddress() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            NetworkInterface ni = NetworkInterface.getByInetAddress(address);
            //ni.getInetAddresses().nextElement().getAddress();
            byte[] mac = ni.getHardwareAddress();
            if (mac == null) {
                mac = ni.getInetAddresses().nextElement().getAddress();
            }

//            String sIP = address.getHostAddress();
            String sMAC = "";
            Formatter formatter = new Formatter();
            for (int i = 0; i < mac.length; i++) {
                sMAC = formatter.format(Locale.getDefault(), "%02X%s", mac[i],
                        (i < mac.length - 1) ? "-" : "").toString();
            }
            return sMAC;
        } catch (Exception e) {
            throw new AvalonException("获取MAC地址失败:" + e.getMessage());
        }

    }

    private static Map<String, String> getSystemEnv() {
        return System.getenv();
    }

    /**
     * 获取用户名
     *
     * @return
     */
    public static String getSystemUserName() {
        if (isWindows()) {
            return getSystemEnv().get("USERNAME");
        } else {
            return getSystemEnv().get("USER");
        }
    }

    /**
     * 获取计算机名
     *
     * @return
     */
    public static String getComputerName() {
        return getSystemEnv().get("COMPUTERNAME");
    }

    public static Boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    public static Boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

    public static Boolean isMac() {
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }
}
