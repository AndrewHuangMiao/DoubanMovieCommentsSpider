package com.yue.Crawel.util;

import com.yue.Crawel.douban.DoubanCommentCrawlerRunner;
import com.yue.Crawel.douban.DoubanCrawler;
import com.yue.Crawel.model.DoubanComment;
import com.yue.Crawel.model.MovieDetail;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by andrew on 16/2/13.
 */
public class DoubanCommentUtil {

    private static final String URL = "http://cn.bing.com/search?q=";
    private static final String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E)";
    private static final int SOCK_TIMEOUT = 30000;
    private static final int CONNECT_TIMEOUT = 30000;
    private static String movieId = null;

    public static void main(String[] args) {
        System.out.println("请输入电影的名字");
        Scanner scanner = new Scanner(System.in);
        String movie = scanner.next();

//        start(movie);
    }

    public static List<DoubanComment> startCraweling(String movie) {
        List<DoubanComment> doubanComments = null;
        String queryURL = null;
        try {
            queryURL = URL + new String(URLEncoder.encode(movie + "豆瓣电影", "UTF-8").getBytes());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            Element firstResult = null;
            String html = HttpUtil.getDocumentString(queryURL);
            Document document = Jsoup.parse(html);
            Elements content = document.select("div#b_content");
            firstResult = content.select("li.b_algo").first();
            String href = firstResult.select("h2").select("a").attr("href");
            System.out.println("重定向的url是:" + href);
            movieId = getMovieID(href);
            if(movieId==null){
                return null;
            }
            doubanComments = getDoubanComments(movieId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return doubanComments;
    }

    public static MovieDetail getMovieDetail(String movieName) {
        DoubanCrawler crawler = new DoubanCrawler();
        MovieDetail movieDetail = null;
        String queryURL = null;
        try {
            queryURL = URL + new String(URLEncoder.encode(movieName + "豆瓣电影", "UTF-8").getBytes());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            Element firstResult = null;
            String html = HttpUtil.getDocumentString(queryURL);
            Document document = Jsoup.parse(html);
            Elements content = document.select("div#b_content");
            firstResult = content.select("li.b_algo").first();
            String href = firstResult.select("h2").select("a").attr("href");
            System.out.println("重定向的url是:" + href);
            movieId = getMovieID(href);
            if(movieId==null){
                return null;
            }
            movieDetail = crawler.getMovieDetail(movieId);
        }catch (Exception e){
            e.printStackTrace();
        }

        return movieDetail;
    }

    public static List<DoubanComment> getDoubanComments(String movieId) {
        DoubanCommentCrawlerRunner crawler = new DoubanCommentCrawlerRunner(movieId);
        return crawler.crawelMovieComment(movieId);
    }

    public static String getMovieID(String url) {
        String movieID = null;
        String newURL = null;

        GetMethod getMethod = HttpUtil.getCommonGetMethod(url);
        try {
            newURL = getMethod.getURI().toString();

        } catch (URIException e) {
            e.printStackTrace();
        }

        if (newURL != null && !newURL.equals("")) {
            System.out.println("新的url是:" + newURL);
            try {
                movieID = parseURL2ID(newURL);
            }catch (Exception e){
                e.printStackTrace();
                movieID = "26322792";
            }

            System.out.println("电影的ID是:" + movieID);
        }

        return movieID;
    }

    private static String parseURL2ID(String url) {
        String subjectID = null;
        String regex = null;

        if(url.contains("https")){
            regex = "https://.*?subject/(\\d+)/";
        }else{
            regex = "http://.*?subject/(\\d+)/";
        }

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(url);

        if(m.find()){
            subjectID = m.group(1);
        }else{
            System.out.println("匹配错误!");
        }

        return subjectID;
    }

}
