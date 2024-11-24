package com.yiqiyuns.WebUtils;

import jakarta.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

/**
 * 获取客户端的 IP 地址
 *
 * @author 17Yuns
 * @version 1.0
 */
@SuppressWarnings("all")
public class IpUtils {

    // 常见的获取IP地址的请求头
    private static final List<String> IP_HEADERS = Arrays.asList(
            "x-forwarded-for",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
    );

    /**
     * 获取客户端的真实 IP 地址
     *
     * @param request HTTP 请求对象
     * @return 客户端的 IP 地址，若未找到则返回 "unknown"
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = extractClientIp(request);
        return (ip != null && !ip.isEmpty()) ? ip : "unknown";
    }

    /**
     * 提取客户端IP地址，按顺序检查请求头，并返回第一个有效的 IP 地址
     *
     * @param request HTTP 请求对象
     * @return 客户端 IP 地址
     */
    private static String extractClientIp(HttpServletRequest request) {
        String ip = null;

        // 遍历请求头，尝试提取有效的 IP 地址
        for (String header : IP_HEADERS) {
            ip = getValidIpFromHeader(request, header);
            if (ip != null) {
                break;
            }
        }

        // 如果未从请求头中找到有效的 IP 地址，获取请求的远程地址
        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        // 如果是 localhost 地址，转换为本机地址
        return "0:0:0:0:0:0:0:1".equals(ip) ? getLocalHostIp() : ip;
    }

    /**
     * 从 HTTP 请求头中获取有效的 IP 地址
     *
     * @param request HTTP 请求对象
     * @param header  请求头名称
     * @return 有效的 IP 地址，或 null 如果无效
     */
    private static String getValidIpFromHeader(HttpServletRequest request, String header) {
        String ip = request.getHeader(header);
        return isValidIp(ip) ? getFirstIp(ip) : null;
    }

    /**
     * 判断 IP 是否有效
     *
     * @param ip 要判断的 IP 地址
     * @return true 如果 IP 有效，false 如果无效
     */
    private static boolean isValidIp(String ip) {
        return ip != null && !ip.trim().isEmpty() && !"unknown".equalsIgnoreCase(ip);
    }

    /**
     * 获取 IP 地址字符串中的第一个有效 IP（如果有多个）
     *
     * @param ip 可能包含多个 IP 地址的字符串
     * @return 第一个有效 IP 地址
     */
    private static String getFirstIp(String ip) {
        if (ip.contains(",")) {
            return ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * 获取本机的 IP 地址
     *
     * @return 本机 IP 地址
     */
    private static String getLocalHostIp() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            return localHost.getHostAddress();
        } catch (UnknownHostException e) {
            // 如果无法获取本机 IP，则返回 "unknown"
            return "unknown";
        }
    }
}
