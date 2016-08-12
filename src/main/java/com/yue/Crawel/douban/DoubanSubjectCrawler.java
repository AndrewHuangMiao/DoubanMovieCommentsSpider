package com.yue.Crawel.douban;

import com.yue.Crawel.model.DoubanSubject;
import com.yue.Crawel.util.Pair;
import com.yue.Crawel.util.TextUtil;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * Author: andrew
 * Create: 12/26/14
 * 进行豆瓣电影信息的抓取
 */
public class DoubanSubjectCrawler implements Serializable {
    private static final int ITEM_PER_PAGE = 20;
    private static final long serialVersionUID = -3986244606585552569L;
    private static final String URL_TMPLT = "http://movie.douban.com/tag/%s?type=T&start=%s";
    private static final String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E)";
    private String cacheCookie = "bid=\"7Hku0QjxH11\"";

    public List<DoubanSubject> getSubjects(int year, int start) throws IOException {
        Pair<Integer,List<DoubanSubject>> res = parse(getPage(year, start));
        if(res==null) return null;
        return res.getSecond();
    }

    public int getTotalPage(int year) throws IOException {
        Pair<Integer,List<DoubanSubject>> res = parse(getPage(year, 0));
        if(res==null) return -1;
        return res.getFirst();
    }

    private String getPage(int year, int start) throws IOException {
        cacheCookie = genRandomBrowserId();
        String url = genUrl(year, start);
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod(url);
        getMethod.setRequestHeader("User-Agent", USER_AGENT);
        getMethod.setRequestHeader("Cookie", cacheCookie);
        httpClient.executeMethod(getMethod);
        String html = getMethod.getResponseBodyAsString();
//        System.out.println("cacheCookie: " + cacheCookie);
        getMethod.abort();

        if (isForbid(html)) {
            System.out.println("forbidden!!!!");
            String bid = "7Hku0QjxH" + Integer.toString(new Random().nextInt(9)) + Integer.toString(new Random().nextInt(9));
            cacheCookie = "bid=\""+bid+"\"";
            httpClient = new HttpClient();
            getMethod = new GetMethod(url);
            getMethod.setRequestHeader("User-Agent", USER_AGENT);
            getMethod.setRequestHeader("Cookie", cacheCookie);
            httpClient.executeMethod(getMethod);
            html = getMethod.getResponseBodyAsString();
            String browserId = TextUtil.getFirstBetween(html, "browserId = '", "'");
            cacheCookie = String.format("bid=\"%s\"", browserId);
            getMethod.abort();
        }
        return html;
    }

    private boolean isForbid(String html){
        if(html.contains("403 Forbidden") || !html.contains("browserId")){
            return true;
        } else{
            return false;
        }
    }

    private Pair<Integer,List<DoubanSubject>> parse(String html){
        List<DoubanSubject> doubanSubjects = new LinkedList<DoubanSubject>();
        Document document = Jsoup.parse(html);
        Elements subjectList = document.select("table").select("tbody").select("tr.item");
        int totalPage = -1;
        try{
            Elements emls = document.select("div.paginator").select("a");
            if(emls.size()==0){
                totalPage = 1;
            } else{
                String totalStr = emls.get(emls.size()-2).text();
                if(totalStr.contains("前页")){
                    totalPage = 2;
                } else{
                    totalPage = Integer.parseInt(totalStr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("fail get total page");
        }

        if(subjectList.isEmpty())
            return null;
        for (Element subject : subjectList) {
            String title = subject.select("a.nbg").attr("title");
            String subUrl = subject.select("a.nbg").attr("href");
            String subjectId = getSubjectId(subUrl);
            String imgUrl = subject.select("a.nbg").select("img").attr("src");
            String desc = subject.select("div.pl2").select("p.pl").text();
            String commentNumStr = subject.select("div.pl2").select("span.pl").text();
            int commentNum = -1;
            float rate = -1;
            if(commentNumStr.contains("尚未上映")){
                commentNum = -2;
                rate = -2;
            } else if(commentNumStr.contains("评价人数不足")|| commentNumStr.contains("目前无人评价") ||commentNumStr.contains("少于10人评价") ){
                commentNum = -3;
                rate = -3;
            } else{
                commentNum = getCommentNum(commentNumStr);
                String rateStr = subject.select("div.pl2").select("span.rating_nums").text();
                if(!rateStr.isEmpty()){
                    rate = getRate(rateStr);
                }
            }
            DoubanSubject doubanSubject = new DoubanSubject(subjectId, title, imgUrl, desc, rate, commentNum);
            doubanSubjects.add(doubanSubject);
//            System.out.println(doubanSubject);
        }
        return new Pair<Integer,List<DoubanSubject>>(totalPage, doubanSubjects);
    }

    private String genUrl(int year, int start){
        return String.format(URL_TMPLT, year, start);
    }

    private float getRate(String rateStr){
        try{
            return Float.parseFloat(rateStr);
        } catch (Exception e){
            System.out.println("fail getRate: " + rateStr);
            return -1;
        }
    }
    private int getCommentNum(String commentNumStr){
        try{
            return Integer.parseInt(TextUtil.getFirstBetween(commentNumStr, "(", "人"));
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("fail getCommentNum: " + commentNumStr);
            return -1;
        }
    }
    private String getSubjectId(String subjectUrl){
        try{
            String sps[] = subjectUrl.split("/");
            if(sps[sps.length-1].isEmpty()){
                return sps[sps.length-2];
            } else{
                return sps[sps.length-1];
            }
        }catch (Exception e) {
            e.getMessage();
            System.out.println("fail getSubjectId: " + subjectUrl);
            return "-1";
        }
    }

    private static Random randGen = null;
    private static char[] numbersAndLetters = null;
    private String genRandomBrowserId(){
        return String.format("bid=\"%s\"",randomString(11));
    }
    private static final String randomString(int length) {
        if (length < 1) {
            return null;
        }
        if (randGen == null) {
            randGen = new Random();
            numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz" +
                    "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
        }
        char [] randBuffer = new char[length];
        for (int i=0; i<randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
        }
        return new String(randBuffer);
    }

    public static void main(String[] args) throws IOException {
//        DoubanSubjectCrawler doubanSubjectCrawler = new DoubanSubjectCrawler();
//        doubanSubjectCrawler.getSubjects(2015, 260);



        DoubanSubjectCrawler doubanSubjectCrawler = new DoubanSubjectCrawler();
        String dir = "douban/";
        for(int year = 1914; year>=1900; year--) {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(dir + year+".txt")));
            System.out.println("====================");
            System.out.println("start year: " + year);
            int maxPage = doubanSubjectCrawler.getTotalPage(year);
            System.out.println("maxPage: " + maxPage);
            Set<String> subjectIds = new HashSet<String>();

            for (int i = 0; i < maxPage; i++) {
                try {
                    System.out.println("begin crawl year: " + year + ", page: " + i);
                    List<DoubanSubject> subjects = doubanSubjectCrawler.getSubjects(year, i * ITEM_PER_PAGE);
                    if (subjects != null) {
                        for (DoubanSubject subject : subjects) {
                            String subId = subject.getSubjectId();
                            if (subjectIds.contains(subId)) continue;
                            subjectIds.add(subId);
//                            System.out.println(subject.getTitle() +"\t"+subject.getCountryTime());
                            bufferedWriter.write(subject.toGsonString() + "\n");
                        }
                        bufferedWriter.flush();
                    }
                    System.out.println("finish crawl year: " + year + ", page: " + i + ", curSize: " + subjects.size() + ", totalSize: " + subjectIds.size());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("fail crawl year: " + year + ", page: " + i + ", " + e.getMessage());
                }
            }
            bufferedWriter.close();
        }
    }
}
