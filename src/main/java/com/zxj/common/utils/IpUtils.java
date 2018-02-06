package com.zxj.common.utils;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zxj.common.config.StringPool;

import java.net.Inet6Address;
import java.net.InetAddress;

/**
 * IpUtils
 *
 * @author 
 */
public class IpUtils {

    /**
     * logger
     */
    private static Logger logger = LoggerFactory.getLogger(IpUtils.class);

    /**
     * ip环境变量的keys
     */
    static String[] args = new String[]{"x-forwarded-for", "Proxy-Client-IP", "X-Forwarded-For", "WL-Proxy-Client-IP", "X-Real-IP", "HTTP_X_FORWARDED_FOR", "HTTP_CLIENT_IP"};

    /**
     * 获取当前ip
     *
     * @param request HttpServletRequest
     * @return String
     */
    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String ip = null;
        for (String key : args) {
            ip = request.getHeader(key);
            if (StringUtils.isNotBlank(ip)) {
                String[] ips = ip.split(StringPool.Symbol.COMMA);//有可能是多个ip,取第一个.
                ip = ips[0];
                break;
            }
        }
        return ip;
    }

    /**
     * string类型的ip转换为number类型
     *
     * @param ip xxx.xxx.xxx.xxx
     * @return long 3663452325
     */
    public static long encodeIp(String ip) {
        long ret = 0;
        if (ip == null) {
            return ret;
        }
        String[] segs = ip.split("\\.");

        for (int i = 0; i < segs.length; i++) {
            long seg = Long.parseLong(segs[i]);
            ret += (seg << ((3 - i) * 8));
        }

        return ret;
    }

    /**
     * number类型的ip转换为string类型
     *
     * @param ipLong 3663452325
     * @return String xxx.xxx.xxx.xxx
     */
    public static String decodeIp(long ipLong) {
        StringBuilder ip = new StringBuilder(String.valueOf(ipLong >> 24));
        ip.append(StringPool.Symbol.DOT);
        ip.append(String.valueOf((ipLong & 16711680) >> 16));
        ip.append(StringPool.Symbol.DOT);
        ip.append(String.valueOf((ipLong & 65280) >> 8));
        ip.append(StringPool.Symbol.DOT);
        ip.append(String.valueOf(ipLong & 255));

        return ip.toString();
    }


    /**
     * 主机 ip
     */
    private static String HOST_IP;

    /**
     * 主机名
     */
    private static String HOST_NAME;

    /**
     * 是否初始化过
     */
    private static boolean inited = false;

    private static void initHost() {
        if (!inited) {
            synchronized (IpUtils.class) {
                if (!inited) {
                    try {
                        InetAddress addr = InetAddress.getLocalHost();
                        HOST_IP = addr.getHostAddress();
                        HOST_NAME = addr.getHostName();
                        if (StringUtils.isNotEmpty(HOST_NAME)) {
                            InetAddress[] addrs = InetAddress.getAllByName(HOST_NAME);
                            if (addrs != null && addrs.length > 0) {
                                for (InetAddress address : addrs) {
                                    //排除回环 ip
                                    if (address.isLoopbackAddress())
                                        continue;
                                    //排除 ipv6
                                    if (address instanceof Inet6Address)
                                        continue;
                                    if (address.isLinkLocalAddress())
                                        continue;
                                    HOST_IP = address.getHostAddress();
                                }

                            }
                        }
                    } catch (Exception ex) {
                        logger.error("ip init error ", ex);
                    }
                    inited = true;
                }
            }
        }
    }

    /**
     * 获取本机的IP
     *
     * @return Ip地址
     */
    public static String getHostIP() {
        initHost();
        return HOST_IP;
    }

    /**
     * 或者主机名：
     *
     * @return 主机名
     */
    public static String getHostName() {
        initHost();
        return HOST_NAME;
    }
}
