import com.yue.Crawel.Service.DoubanService;
import com.yue.Crawel.model.DoubanComment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by Andrew on 16/4/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-servlet.xml"})
public class TestSpider {
    private BigInteger cookieNumber = new BigInteger("1462779369");
    private static Integer page = 1;
    //用来判断ip队列是否为空,当为空的话重新进行抓取
    private static Boolean emptyFlag = true;
    private static Log LOG = LogFactory.getLog(TestSpider.class);
    private static final long serialVersionUID = -3986244606585552569L;
    private static final String URL_TMPLT = "http://movie.douban.com/subject/%s/comments?start=%s&limit=%s&sort=time";
    //    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:33.0) Gecko/20100101 Firefox/33.0";
//    private static final String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E)";
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.86 Safari/537.36";

    private String cacheCookie = "bid=\"7Hku0QjxH11\"";
    private static final int SOCK_TIMEOUT = 30000;
    private static int index = 0;
    private static final int CONNECT_TIMEOUT = 30000;
    private String cookie;

    @Resource
    private DoubanService doubanService;

    @Test
    public void testSpider(){
        List<DoubanComment> doubanComments = doubanService.getDoubanComments("澳门风云");
        doubanService.saveComments2DB(doubanComments);
    }

    /*
    每次在原来的cookie的数值上面加2就可以访问到
     */
    public String getCookie() {
        String cookie = "CNZZDATA1257387763=1215088684-1462768482-http%253A%252F%252Fwww.kuaidaili.com%252F%7C1462768482; Hm_lvt_ff5f844e34c1a10ebe66a69e384c675f=1462768545; Hm_lpvt_ff5f844e34c1a10ebe66a69e384c675f=1462768745; sessionid=6567afae5ca1795f40f5707c3904ae40; _gat=1; Hm_lvt_7ed65b1cc4b810e9fd37959c9bb51b31=1462757591,1462764700,1462778071,1462778074;_ga=GA1.2.700516828.1462006281";
        int randomInteger = cookieNumber.intValue()+3;
//        cookie += "Hm_lpvt_7ed65b1cc4b810e9fd37959c9bb51b31="+randomInteger;
        cookie += "Hm_lpvt_7ed65b1cc4b810e9fd37959c9bb51b31=1462779370";
        return cookie;
    }

//    @Test
//    public void testProxy(){
//        String url = "http://dev.kuaidaili.com/api/getproxy?orderid=966327077087745&num=100&kps=1";
//        HttpClient httpClient = new HttpClient();
//        httpClient.setConnectionTimeout(CONNECT_TIMEOUT);
//        httpClient.setTimeout(SOCK_TIMEOUT);
//        httpClient.getHttpConnectionManager().getParams().setSoTimeout(SOCK_TIMEOUT);
//        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECT_TIMEOUT);
//        GetMethod getMethod = new GetMethod(url);
//        try{
//            httpClient.executeMethod(getMethod);
//            String html = getMethod.getResponseBodyAsString();
//            System.out.println(html);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}

