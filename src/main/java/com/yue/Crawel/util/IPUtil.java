package com.yue.Crawel.util;


import com.yue.Crawel.consts.Constant;
import com.yue.Crawel.model.IpPort;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.*;

/**
 * Created by andrew on 16/2/19.
 */
public class IPUtil {
    private static Log LOG = LogFactory.getLog(IPUtil.class);

    public static Map<String, Integer> getIps() {
        Map<String,Integer> ipPool = new HashMap<String, Integer>();
        String url = "http://www.kuaidaili.com/proxylist/1/";
        HttpClient client = HttpUtil.getCommonClient();
        GetMethod getMethod = new GetMethod(url);
        String html = null;

        getMethod.setRequestHeader("Upgrade-Insecure-Requests", "1");
        getMethod.setRequestHeader("Host", "www.kuaidaili.com\n");
        getMethod.setRequestHeader("User-Agent", SparkConsts.UA_PC_CHROME);
        getMethod.setRequestHeader("Connection", "keep-alive");
        getMethod.setRequestHeader("Pragma", "no-cache");
        getMethod.setRequestHeader("Referer", "http://www.kuaidaili.com/proxylist/2/");

        try {
            client.executeMethod(getMethod);
            html = getMethod.getResponseBodyAsString();
            Document doc = Jsoup.parse(html);
            Elements ipMessageList = doc.select("div#list").select("table").select("tbody").select("tr");
            for(Element ipMessage : ipMessageList){
                String ip = ipMessage.select("td").first().html();
                Integer port = Integer.parseInt(ipMessage.select("td").get(1).html());
                ipPool.put(ip, port);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return ipPool;
    }

    /**
     * 初始化代理ip和端口
     * @param page
     * @param emptyFlag
     */
    public static void initProxyIpPortList(int page, boolean emptyFlag, List<IpPort> ipPortList){
        String url = "http://dev.kuaidaili.com/api/getproxy?orderid=986407925598983&num=100&kps=1";
        HttpClient httpClient = new HttpClient();
        httpClient.setConnectionTimeout(Constant.CONNECT_TIMEOUT);
        httpClient.setTimeout(Constant.SOCK_TIMEOUT);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(Constant.SOCK_TIMEOUT);
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(Constant.CONNECT_TIMEOUT);
        final GetMethod getMethod = new GetMethod(url);

        while(true){
            if(page > 5){
                return;
            }

            try {
                if (emptyFlag && page < 5) {
                    httpClient.executeMethod(getMethod);
                    String html = getMethod.getResponseBodyAsString();
                    String[] ip2PortArr = html.split(":");
                    IpPort ipPort = new IpPort(ip2PortArr[0], Integer.parseInt(ip2PortArr[1]));
                    synchronized (ipPortList) {
                        ipPortList.add(ipPort);
                        emptyFlag = false;
                    }
                    page++;
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

}
