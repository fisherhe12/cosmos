package org.cosmos.common.web;


import org.cosmos.common.text.EscapeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * HTTP工具类
 * </p>
 *
 * @author fisher
 */
public class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * 允许 JS 跨域设置
     */
    public static void allowJsCrossDomain(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS, POST, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        response.setHeader("Access-Control-Max-Age", "3600");
    }

    /**
     * 判断请求是否为 AJAX
     */
    public static boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    /**
     * 获取当前URL的查询条件
     * 默认编码UTF-8
     */
    public static String getQueryString(HttpServletRequest request) throws IOException {
        StringBuffer sb = new StringBuffer(request.getRequestURL());
        String query = request.getQueryString();
        if (query != null && query.length() > 0) {
            sb.append("?").append(query);
        }
        return EscapeUtil.urlEncode(sb.toString());
    }

    /**
     * 获取当前完整请求地址
     */
    public static String getRequestUrl(HttpServletRequest request) {
        StringBuilder url = new StringBuilder(request.getScheme());
        // 请求协议 http,https
        url.append("://");
        url.append(request.getHeader("host"));
        url.append(request.getRequestURI());
        if (request.getQueryString() != null) {
            // 请求参数
            url.append("?").append(request.getQueryString());
        }
        return url.toString();
    }
}
