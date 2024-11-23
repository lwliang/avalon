/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.util;


import com.avalon.core.context.SystemConstant;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求工具类
 */
public class HttpRequestUtils {
    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * <p>
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     * <p>
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,
     * 192.168.1.100
     * <p>
     * 用户真实IP为： 192.168.1.110
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-real-ip");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("x-forwarded-for");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            String localIp = "127.0.0.1";
            String localIpv6 = "0:0:0:0:0:0:0:1";
            if (ipAddress.equals(localIp) || ipAddress.equals(localIpv6)) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                    ipAddress = inet.getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        String ipSeparate = ",";
        int ipLength = 15;
        if (ipAddress != null && ipAddress.length() > ipLength) {
            if (ipAddress.indexOf(ipSeparate) > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(ipSeparate));
            }
        }
        return ipAddress;
    }

    /**
     * 获取token
     */
    public static String getRequestToken(HttpServletRequest httpRequest) {
        //从header中获取时间轴
        String token = httpRequest.getHeader(SystemConstant.TOKEN);
        //如果header中不存在时间轴，则从参数中获取时间轴
        if (ObjectUtils.isNull(token)) {
            token = httpRequest.getParameter(SystemConstant.TOKEN);
            if (token == null) {
                token = "0";
            }
        }
        return token;
    }

    /**
     * 获取token
     */
    public static String getRequestParam(String param,HttpServletRequest httpRequest) {
        //从header中获取时间轴
        String value = httpRequest.getHeader(param);
        //如果header中不存在时间轴，则从参数中获取时间轴
        if (ObjectUtils.isNull(value)) {
            value = httpRequest.getParameter(param);
        }
        return value;
    }
    /**
     * 将URL的参数和body参数合并
     *
     * @param request
     */
//    public static SortedMap<String, String> getAllParams(HttpServletRequest request) throws IOException {
//
//        SortedMap<String, String> result = new TreeMap<>();
//        //获取URL上的参数
//        Map<String, String> urlParams = getUrlParams(request);
//        for (Map.Entry entry : urlParams.entrySet()) {
//            result.put((String) entry.getKey(), (String) entry.getValue());
//        }
//        Map<String, String> allRequestParam = new HashMap<>(16);
//        // get请求不需要拿body参数
//        if (!HttpMethod.GET.name().equals(request.getMethod())) {
//            allRequestParam = getAllRequestParam(request);
//        }
//        //将URL的参数和body参数进行合并
//        if (allRequestParam != null) {
//            for (Map.Entry entry : allRequestParam.entrySet()) {
//                result.put((String) entry.getKey(), (String) entry.getValue());
//            }
//        }
//        return result;
//    }


    /**
     * 将URL请求参数转换成Map
     *
     * @param request
     * @author show
     */
    public static Map<String, String> getUrlParams(HttpServletRequest request) {

        String param = "";
        try {
            param = URLDecoder.decode(request.getQueryString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Map<String, String> result = new HashMap<>(16);
        String[] params = param.split("&");
        for (String s : params) {
            int index = s.indexOf("=");
            result.put(s.substring(0, index), s.substring(index + 1));
        }
        return result;
    }


//    public static String getHost(String url) {
//        if (!(StringUtils.startsWithIgnoreCase(url, "http://") || StringUtils
//                .startsWithIgnoreCase(url, "https://"))) {
//            url = "http://" + url;
//        }
//        String returnVal = StringUtils.EMPTY;
//        try {
//            URI uri = new URI(url);
//            returnVal = uri.getHost();
//        } catch (Exception e) {
//        }
//        if ((StringUtils.endsWithIgnoreCase(returnVal, ".html") || StringUtils
//                .endsWithIgnoreCase(returnVal, ".htm"))) {
//            returnVal = StringUtils.EMPTY;
//        }
//        return returnVal;
//    }

    /**
     * 获取请求的时间轴
     */
//    public static String getRequestTimestamp(HttpServletRequest httpRequest) {
//        //从header中获取时间轴
//        String timestamp = httpRequest.getHeader(SystemConfig.TIMESTAMP);
//        //如果header中不存在时间轴，则从参数中获取时间轴
//        if (ObjectUtil.isNull(timestamp)) {
//            timestamp = httpRequest.getParameter(SystemConfig.TIMESTAMP);
//            if (timestamp == null) {
//                timestamp = "0";
//            }
//        }
//        return timestamp;
//    }

    /**
     * 获取请求的语言
     */
//    public static String getRequestLang(HttpServletRequest httpRequest) {
//        //从header中获取语言
//        String lang = httpRequest.getHeader(SystemConfig.LANG);
//        //如果header中不存在语言，则从参数中获取语言
//        if (ObjectUtil.isNull(lang)) {
//            lang = httpRequest.getParameter(SystemConfig.LANG);
//            if (lang == null) {
//                lang = "zh_cn";
//            }
//        }
//        return lang;
//    }
//
//    /**
//     * 获取请求的时区
//     */
//    public static String getRequestTimeZone(HttpServletRequest httpRequest) {
//        //从header中获取时区
//        String timeZone = httpRequest.getHeader(SystemConfig.TIMEZONE);
//        //如果header中不存在时区，则从参数中获取时区
//        if (ObjectUtil.isNull(timeZone)) {
//            timeZone = httpRequest.getParameter(SystemConfig.TIMEZONE);
//            if (timeZone == null) {
//                timeZone = "0";
//            }
//        }
//        return timeZone;
//    }
//
//    /**
//     * 获取请求的版本号
//     */
//    public static String getRequestVersion(HttpServletRequest httpRequest) {
//        //从header中获取版本号
//        String version = httpRequest.getHeader(SystemConfig.VERSION);
//        //如果header中不存在版本号，则从参数中获取版本号
//        if (StringUtil.isNullOrEmpty(version)) {
//            version = httpRequest.getParameter(SystemConfig.VERSION);
//            if (version == null) {
//                version = "200";
//            }
//        }
//        return version;
//    }
//
//    /**
//     * 获取请求的系统
//     */
//    public static String getRequestClient(HttpServletRequest httpRequest) {
//        //从header中获取版本号
//        String client = httpRequest.getHeader(SystemConfig.CLIENT);
//        //如果header中不存在版本号，则从参数中获取版本号
//        if (StringUtil.isNullOrEmpty(client)) {
//            client = httpRequest.getParameter(SystemConfig.CLIENT);
//            if (client == null) {
//                client = "PC";
//            }
//        }
//        return client;
//    }
}
