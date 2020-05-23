package com.tech.base.utils;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class IpUtil {

    /**
     * 获取最后一个内网ip地址
     */
    public static String getLocalHostAddress() {
        try {
            String ip = null;
            Enumeration<NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces();
            while (nifs.hasMoreElements()) {
                NetworkInterface nif = nifs.nextElement();
                String ethernetName = nif.getName();
                if (!ethernetName.contains("docker") && !ethernetName.contains("lo")) {
                    Enumeration<InetAddress> addresses = nif.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress addr = addresses.nextElement();
                        if (addr instanceof Inet6Address) {
                            continue;
                        }
                        if (addr.isSiteLocalAddress() && !addr.isLoopbackAddress()) {
                            ip = addr.getHostAddress();
                            break;
                        }
                    }
                }
            }
            return ip;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    // IP转换为Long
    public static long ipToLong(String ip) {
        String[] ipArray = ip.split("\\.");
        List<Long> ipNums = new ArrayList<>();
        for (int i = 0; i < 4; ++i) {
            ipNums.add(Long.valueOf(Long.parseLong(ipArray[i].trim())));
        }
        long zhongIPNumTotal = ((Long) ipNums.get(0)).longValue() * 256L * 256L * 256L + ((Long) ipNums.get(1)).longValue() * 256L * 256L
                + ((Long) ipNums.get(2)).longValue() * 256L + ((Long) ipNums.get(3)).longValue();

        return zhongIPNumTotal;
    }
}
