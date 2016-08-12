package com.yue.Crawel.douban;

import com.yue.Crawel.model.CrawlItem;
import com.yue.Crawel.model.DoubanComment;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Author: andrew
 */
public final class DoubanCommentCrawlerRunner implements Serializable {
    private static Log LOG = LogFactory.getLog(DoubanCommentCrawlerRunner.class);
    private static final long serialVersionUID = -3986244606585552569L;
    private String sparkUrl;
    private String[] jars;
    private JavaSparkContext jsc;
    private String subjectId;
    private String subjectName;
    List<String> subjectIds = new ArrayList<String>();
    private int commentSize = 100;
    public void setJars(String[] jars) {
        this.jars = jars;
    }

    public DoubanCommentCrawlerRunner(String subjectId) {
        this.subjectId = subjectId;
    }

    public DoubanCommentCrawlerRunner(String subjectId, List<String> subjectIds, int commentSize) {
        this.subjectId = subjectId;
        this.subjectIds = subjectIds;
        this.commentSize = commentSize;
    }

    public List<DoubanComment> crawelMovieComment(String movieId) {
        LOG.info("crawel Movie:" + movieId);
        List<DoubanComment> doubanComments = null;
        try {

            initCrawel(movieId);
            List<CrawlItem> items;

            items = produceCrawelItems(movieId);
            doubanComments = crawlComment(items);
            jsc.stop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jsc != null) jsc.stop();
        }

        return doubanComments;
    }

    private static final int PAGE_LIMIT = 20;

    /*
    每个crawItem对应一个电影页面,现在暂时只设定以500条记录来处理问题
     */
    private List<CrawlItem> produceCrawelItems(String subjectId) throws IOException {
        LOG.info("start crawel items");
        List<CrawlItem> items = new LinkedList<CrawlItem>();
        DoubanCrawler doubanCrawler = new DoubanCrawler();
        int totalNum = doubanCrawler.getTotalNum(subjectId);

//        int maxPage = 500;
        int maxPage = 500;
        for (int i = 0; i < maxPage; i++) {
            items.add(new CrawlItem(subjectId, i * PAGE_LIMIT, PAGE_LIMIT));
        }
        System.out.println("totalNum: " + totalNum);
        System.out.println("maxPage: " + maxPage);
        return items;
    }

    private void initCrawel(String subjectId) {
        String sparkHome = System.getenv("SPARK_HOME");
        SparkConf conf = new SparkConf()
                .setMaster("local[4]")
                .setAppName("DoubanCommentCrawl-" + subjectId)
                .setJars(jars != null ? jars : JavaSparkContext.jarOfClass(DoubanCommentCrawlerRunner.class));
        conf = conf.set("spark.cores.max", "2");
        conf = conf.set("spark.default.parallelism", "8");
        if (sparkHome != null) {
            System.out.println("SparkHome: " + sparkHome);
            conf = conf.setSparkHome(sparkHome);
        }

        jsc = new JavaSparkContext(conf);
    }

    /*
    根据crawelItem进行每个页面的电影评论的抓取
    抓取之后保存一个txt在本地,可以提供下载功能
     */
    private List<DoubanComment> crawlComment(List<CrawlItem> crawlItems) {
        System.out.println("start crawl douban comment in spark");
        JavaRDD<CrawlItem> itemJavaRDD = jsc.parallelize(crawlItems);
        List<DoubanComment> doubanComments = itemJavaRDD.repartition(40).flatMap(new DoubanCommentCrawlerMap()).collect();
        LOG.info("完成抓取");
        return doubanComments;
    }

}

