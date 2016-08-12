package com.yue.Crawel.util;


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

    /*
    启动一个线程去监控,每隔半个小时就更换一次代理ip和port
    要注意多线程情况下ip2PortList这个对象存在竟态条件
     */
//    public static void getIp2PortList(final List<Map.Entry<String, Integer>> ip2PortList) {
//
//        Runnable ip2PortCrawel = new Runnable() {
//            public void run() {
//                while (true) {
//                    Map<String, Integer> ip2portPool = IPUtil.getIps();
//                    synchronized (ip2PortList){
//                        ip2PortList.clear();
//                        for (Map.Entry<String, Integer> ip2Port : ip2portPool.entrySet()) {
//                            ip2PortList.add(ip2Port);
//                        }
//                    }
//
//                    try {
//                        Thread.sleep(1000 * 60 * 10);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                        LOG.info("定时器发生错误!");
//                    }
//                }
//            }
//        };
//
//        Thread ip2PortThread = new Thread(ip2PortCrawel);
//        ip2PortThread.start();
//
//    }
}
