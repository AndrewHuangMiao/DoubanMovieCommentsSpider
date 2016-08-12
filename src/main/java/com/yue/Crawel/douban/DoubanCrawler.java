package com.yue.Crawel.douban;

import com.yue.Crawel.model.CrawlItem;
import com.yue.Crawel.model.DoubanComment;
import com.yue.Crawel.model.IP2Port;
import com.yue.Crawel.model.MovieDetail;
import com.yue.Crawel.util.HttpUtil;
import com.yue.Crawel.util.IPUtil;
import com.yue.Crawel.util.StringUtil;
import com.yue.Crawel.util.TextUtil;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpHost;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.params.ConnRoutePNames;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * Author: andrew
 * Create: 12/25/14
 * 进行豆瓣电影评论的抓取
 */
public class DoubanCrawler implements Serializable {
    private static Integer page = 1;
    //用来判断ip队列是否为空,当为空的话重新进行抓取
    private static Boolean emptyFlag = true;
    private static List<IP2Port> ip2PortList = new LinkedList<IP2Port>();
    private static Log LOG = LogFactory.getLog(DoubanCrawler.class);
    private static final long serialVersionUID = -3986244606585552569L;
    private static final String URL_TMPLT = "https://movie.douban.com/subject/%s/comments?start=%s&limit=%s&sort=time";
    //    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:33.0) Gecko/20100101 Firefox/33.0";
    private static final String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E)";
    private String cacheCookie = "gr_user_id=3e378661-2458-42ba-824a-fb90e0658039;bid=\"7Hku0QjxH11\"";
    private static final int SOCK_TIMEOUT = 30000;
    private static int index = 0;
    private static final int CONNECT_TIMEOUT = 30000;

    public List<DoubanComment> getComments(CrawlItem crawlItem) throws IOException {
        return getComments(crawlItem.getSubjectId(), crawlItem.getStart(), crawlItem.getLimit());
    }

    public List<DoubanComment> getComments(String subjectId, int start, int limit) throws IOException {
        return parse(getPage(subjectId, start, limit));
    }

    public int getTotalNum(String subjectId) throws IOException {
        MovieDetail res = parseDetail(getPage(subjectId, 0, 10));
        if (res == null) return -1;
        return res.getTotalComment();
    }

    public MovieDetail getMovieDetail(String subjectId) throws IOException {
        return parseDetail(getPage(subjectId, 0, 0));
    }

    /*
    这里会开始去某个网站抓取代理ip
     */
    public String getPage(String subjectId, int start, int limit) {
        try {
            //用来判断使用到了代理ip的第几位
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(getIpCrawel(ip2PortList));

            cacheCookie = genRandomBrowserId();
            String url = genUrl(subjectId, start, limit);
            HttpClient httpClient = new HttpClient();

            //使用代理ip进行抓取数据
            httpClient.setConnectionTimeout(CONNECT_TIMEOUT);
            httpClient.setTimeout(SOCK_TIMEOUT);
            httpClient.getHttpConnectionManager().getParams().setSoTimeout(SOCK_TIMEOUT);
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECT_TIMEOUT);
            httpClient.getHostConfiguration().setProxy("115.159.108.142", 16816);
            httpClient.getParams().setAuthenticationPreemptive(true);
            httpClient.getState().setProxyCredentials(AuthScope.ANY, new UsernamePasswordCredentials("452978456", "6kn29dwa"));

            final GetMethod getMethod = new GetMethod(url);

            getMethod.setRequestHeader("Upgrade-Insecure-Requests", "1");
            getMethod.setRequestHeader("Host", "movie.douban.com");
            getMethod.setRequestHeader("User-Agent", USER_AGENT);
            getMethod.setRequestHeader("Referer", "http://movie.douban.com/subject/" + subjectId + "/?from=showing");
            getMethod.setRequestHeader("Cookie", cacheCookie);
            getMethod.setRequestHeader("Connection", "keep-alive");
            getMethod.setRequestHeader("Pragma", "no-cache");

            while(true) {
                //使用间隔减少被发现的可能性
                Thread.sleep(1000);
                httpClient.executeMethod(getMethod);
                String html = getMethod.getResponseBodyAsString();

                if (isForbid(html) || html.equals("")) {
                    //抓取失败后,更换关键cookie
                    System.out.println("forbidden!!!!");
                    String bid = "7Hku0QjxH" + Integer.toString(new Random().nextInt(9)) + Integer.toString(new Random().nextInt(9));
                    cacheCookie = getCookie(bid);
                    HttpClient httpClientSpare = new HttpClient();
                    httpClientSpare.setConnectionTimeout(CONNECT_TIMEOUT);
                    httpClientSpare.setTimeout(SOCK_TIMEOUT);
                    httpClientSpare.getHttpConnectionManager().getParams().setSoTimeout(SOCK_TIMEOUT);
                    httpClientSpare.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECT_TIMEOUT);
                    final GetMethod getMethodSecond = new GetMethod(url);
                    getMethodSecond.setRequestHeader("User-Agent", USER_AGENT);
                    getMethodSecond.setRequestHeader("Cookie", cacheCookie);
                    getMethodSecond.setRequestHeader("Referer", "http://movie.douban.com/subject/" + subjectId + "/?from=showing");

                    if(!emptyFlag && index <= ip2PortList.size()) {
                        synchronized (ip2PortList) {
                            if(index==ip2PortList.size()){
//                                ip2PortList.clear();
//                                emptyFlag = true;
                                index = 0;
                                return html;
//                                return html;
                            }

                            IP2Port ip2Port = ip2PortList.get(index);
                            HttpHost proxy = new HttpHost(ip2Port.getIp(), ip2Port.getPort());
                            httpClientSpare.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
                            index++;
                        }
                    }

                    try {
                        httpClientSpare.executeMethod(getMethodSecond);
                        html = getMethodSecond.getResponseBodyAsString();
                        String browserId = TextUtil.getFirstBetween(html, "browserId = '", "'");
                        cacheCookie = getCookie(browserId);

                    } catch (Exception e1) {

                        e1.printStackTrace();
                        LOG.info("在更换了cookie之后还是出现了错误!");
                        LOG.info("开始使用代理IP,因为网上抓取所以有限");

                        if (page > 5) {
                            return html;
                        }
                    }
                }

                if(!isForbid(html) && (!html.equals(""))){
                    LOG.info("success!");
                    getMethod.abort();
                    return html;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getCookie(String bid) {
        String bidCookie = "bid = \"" + bid + "\"";
        String cookie = "_ga=GA1.2.1390427853.1439113839; lzstat_uv=1389762687912142207|3600220; gr_user_id=3e378661-2458-42ba-824a-fb90e0658039; ll=\"118281\"; viewed=\"5287474_6781808_21979064_2041304_26422275_1456987_26364303_10558241_3879301_1183730\"; ct=y; _pk_ref.100001.4cf6=%5B%22%22%2C%22%22%2C1462782138%2C%22https%3A%2F%2Fwww.baidu.com%2Flink%3Furl%3DX-uu2Q7MAufDcrlHbkHg4BsEKlXQsg0uHAldXs3RjdWljgw3z1DwyDCAgV_efUt2%26wd%3D%26eqid%3Db2d1865a0018b09700000006572bf4bc%22%5D; __utmt=1; as=\"https://movie.douban.com/subject/11625097/comments?start=30&limit=20&sort=new_score\"; ps=y; dbcl2=\"84670909:THfUsMyEGgQ\"; ck=Ifoc; push_noty_num=1; push_doumail_num=0; _pk_id.100001.4cf6=6825e43da4c35351.1452617847.21.1462782431.1462526020.; _pk_ses.100001.4cf6=*; __utma=30149280.1390427853.1439113839.1462526019.1462782138.69; __utmb=30149280.2.10.1462782138; __utmc=30149280; __utmz=30149280.1462526019.68.59.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; __utma=223695111.1390427853.1439113839.1462526019.1462782138.17; __utmb=223695111.0.10.1462782138; __utmc=223695111; __utmz=223695111.1462526019.16.13.utmcsr=baidu|utmccn=(organic)|utmcmd=organic";
        return (bidCookie + cookie);
    }


    /*
    启动一个监控线程当empty的时候抓取下一页的ip和port,直到page为5
     */
//


    private Runnable getIpCrawel(final List<IP2Port> ip2PortList) {
        Runnable ipCrawel = new Runnable() {
            public void run() {
                String url = "http://dev.kuaidaili.com/api/getproxy?orderid=986407925598983&num=100&kps=1";
                HttpClient httpClient = new HttpClient();
                httpClient.setConnectionTimeout(CONNECT_TIMEOUT);
                httpClient.setTimeout(SOCK_TIMEOUT);
                httpClient.getHttpConnectionManager().getParams().setSoTimeout(SOCK_TIMEOUT);
                httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECT_TIMEOUT);
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
                            IP2Port ip2Port = new IP2Port(ip2PortArr[0], Integer.parseInt(ip2PortArr[1]));
                            synchronized (ip2PortList) {
                                ip2PortList.add(ip2Port);
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
        };
        return ipCrawel;
    }




    private boolean isForbid(String html) {
        if (html.contains("403 Forbidden") || !html.contains("browserId")) {
            return true;
        } else {
            return false;
        }
    }

    private MovieDetail parseDetail(String html) {
        Document document = Jsoup.parse(html);
        //得到这个电影的详细信息
        MovieDetail movieDetail = getSubjectDetail(document);
        int total = -1;
        String movieImg = null;
        try {
            String totalStr = document.select("span.total").text();
            total = Integer.parseInt(totalStr.split(" ")[1]);
            movieImg = document.select("div.movie-pic a img").attr("src");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("fail get total number");
        }
        movieDetail.setTotalComment(total);
        if(movieImg!=null) {
            movieDetail.setMovieImg(movieImg);
        }

        return movieDetail;
    }

    private List<DoubanComment> parse(String html) {
        if (html == null || html.isEmpty()) {
            return null;
        }


        List<DoubanComment> doubanComments = new LinkedList<DoubanComment>();
        Document document = Jsoup.parse(html);
        Elements commentList = document.select("div.comment-item");

        if (commentList.isEmpty())
            return null;
        for (Element comment : commentList) {
            try {
                Element u = comment.select("div.comment").select("span.comment-info").first();
                String cid = comment.attr("data-cid");
                    String userName = u.select("a").text();
                String userUrl = u.select("a").attr("href");
                String userId = getUserId(userUrl);
                int star = -1;
                String time;
                if (u.select("span").size() > 2) {
                    String starStr = u.select("span").get(1).attr("class");
                    star = StringUtil.isNullOrEmpty(starStr) ? -1 : getStar(starStr);
                    time = u.select("span").get(2).text();
                } else {
                    time = u.select("span").get(1).text();
                }

                String commentText = comment.select("div.comment").select("p").text();
                String headUrl = comment.select("div.avatar").select("img").attr("src");
                MovieDetail movieDetail = parseDetail(html);
                String movieName = movieDetail.getMovieName();
                DoubanComment doubanComment = new DoubanComment(cid, userName, userId, headUrl, commentText, time, star, movieName);
                doubanComments.add(doubanComment);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return doubanComments;
    }

    private MovieDetail getSubjectDetail(Document document) {
        Elements elements = document.select("div.movie-summary").select("span.attrs").select("p");
        String movieName = getSubjectName(document);

        MovieDetail movieDetail = new MovieDetail();
        movieDetail.setMovieName(movieName);

        Map<String, String> moreInfo = new HashMap<String, String>();
        for (Element element : elements) {
            String title = element.select("span.pl").text();
            String val = "";
            if (title.contains("导演")) {
                val = element.select("a").text();
                movieDetail.setDirector(val);
            } else if (title.contains("主演")) {
                String acts = element.select("a").text();
                movieDetail.setActors(acts);
            } else if (title.contains("类型")) {
                val = element.text();
                if (!val.isEmpty()) {
                    val = val.split(": ")[1];
                }
                movieDetail.setType(val);
            } else if (title.contains("地区")) {
                val = element.text();
                if (!val.isEmpty()) {
                    val = val.split(": ")[1];
                }
                movieDetail.setArea(val);
            } else if (title.contains("片长")) {
                val = element.text();
                if (!val.isEmpty()) {
                    val = val.split(": ")[1];
                }
                movieDetail.setDuration(val);
            } else if (title.contains("上映")) {
                val = element.text();
                if (!val.isEmpty()) {
                    val = val.split(": ")[1];
                }
                movieDetail.setTime(val);
            } else {
                val = element.text();
                if (!val.isEmpty()) {
                    val = val.split(": ")[1];
                }
                moreInfo.put(title, val);
            }
        }
        movieDetail.setMoreInfo(moreInfo);
        return movieDetail;
    }

    private String getSubjectName(Document document) {
        String subjectName = null;
        Elements subjectNameNode = document.select("div#content").select("h1");
        try {
            for (Element element : subjectNameNode) {
                subjectName = element.text().split(" ")[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return subjectName;
    }

    private String genUrl(String subjectId, int start, int limit) {
        return String.format(URL_TMPLT, subjectId, start, limit);
    }

    private int getStar(String starStr) {
        try {
            return Integer.parseInt(TextUtil.getFirstBetween(starStr, "star", " "));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("fail get star: " + starStr);
            return -1;
        }
    }

    private String getUserId(String userUrl) {
        try {
            String sps[] = userUrl.split("/");
            if (sps[sps.length - 1].isEmpty()) {
                return sps[sps.length - 2];
            } else {
                return sps[sps.length - 1];
            }
        } catch (Exception e) {
            e.getMessage();
            System.out.println("fail getUserId: " + userUrl);
            return "-1";
        }
    }

    private static Random randGen = null;
    private static char[] numbersAndLetters = null;

    private String genRandomBrowserId() {
        return String.format("bid=\"%s\"", randomString(11));
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
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
        }
        return new String(randBuffer);
    }

}
