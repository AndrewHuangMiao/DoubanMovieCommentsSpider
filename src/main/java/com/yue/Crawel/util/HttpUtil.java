package com.yue.Crawel.util;






import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.client.params.ClientPNames;


import java.io.*;
import java.net.ConnectException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author andrew
 *
 */
public class HttpUtil {
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final int SOCK_TIMEOUT = 30000;
    private static final int CONNECT_TIMEOUT = 30000;


    public static String getDocumentString(String uri) throws IOException {
        HttpClient client = new HttpClient();
        client.setConnectionTimeout(CONNECT_TIMEOUT);
        client.setTimeout(SOCK_TIMEOUT);
        client.getHttpConnectionManager().getParams().setSoTimeout(SOCK_TIMEOUT);
        client.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECT_TIMEOUT);
        client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(1, false));
        client.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, false);
        client.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        GetMethod get = new CustomGetMethod(uri);
        get.setRequestHeader("User-Agent", SparkConsts.UA_PC_CHROME);

        String ret = null;
        try {

            int status = client.executeMethod(get);
            ret = get.getResponseBodyAsString();
            get.abort();
            get.releaseConnection();
            if (status != 200) {
                System.err.println("[WARNING] status code\t" + status
                        + "\t url:\t" + uri);
            }
        } catch (ConnectException e) {
            e.printStackTrace();

        }
        return ret;
    }



    public static URI parsrUrl(String uriStr, String charset)
            throws URIException {
            return new URI(uriStr, true, charset);
    }

    public static GetMethod getCommonGetMethod(String url) {
        String cacheCookie = "bid=\"tnXuPiO2Hp0\"";
        List<Map.Entry<String, Integer>> ip2PortList = new LinkedList<Map.Entry<String, Integer>>();
//        IPUtil.getIp2PortList(ip2PortList);
        HttpClient httpClient = getCommonClient();
        GetMethod getMethod = new GetMethod(url);

        getMethod.setRequestHeader("Upgrade-Insecure-Requests", "1");
        getMethod.setRequestHeader("Host", "movie.douban.com");
        getMethod.setRequestHeader("User-Agent", SparkConsts.UA_PC_CHROME);
        getMethod.setRequestHeader("Connection", "keep-alive");
        getMethod.setRequestHeader("Pragma", "no-cache");
        getMethod.setRequestHeader("Referer", "www.baidu.com");
        getMethod.setRequestHeader("Cookie", cacheCookie);

        try {
            httpClient.executeMethod(getMethod);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("获取movieId出错!考虑使用代理ip!");
        }

        return getMethod;
    }

    public static String executeNewIp2PortGet(HttpClient httpClient, GetMethod getMethod, String ip, Integer port)throws Exception{
        httpClient.getHostConfiguration().setProxy(ip, port);
        httpClient.executeMethod(getMethod);
        return "success";
    }

    public static HttpClient getCommonClient() {
        HttpClient httpClient = new HttpClient();

        httpClient.setConnectionTimeout(CONNECT_TIMEOUT);
        httpClient.setTimeout(SOCK_TIMEOUT);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(SOCK_TIMEOUT);
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECT_TIMEOUT);

        return httpClient;
    }
}
