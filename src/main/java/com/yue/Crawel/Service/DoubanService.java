package com.yue.Crawel.Service;


import com.yue.Crawel.dao.DoubanCommetMapper;
import com.yue.Crawel.model.DoubanComment;
import com.yue.Crawel.model.MovieDetail;
import com.yue.Crawel.util.DoubanCommentUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Andrew on 16/4/17.
 */
@Service
public class DoubanService {

    private static Log LOG = LogFactory.getLog(DoubanService.class);

    @Resource
    private DoubanCommetMapper doubanCommetMapper;
    private List<MovieDetail> allMovieDetail;

    public List<DoubanComment> getDoubanComments(String movieName) {
        DoubanCommentUtil doubanMovieUtil = new DoubanCommentUtil();
        return doubanMovieUtil.startCraweling(movieName);
    }

    public MovieDetail getMovieDetail(String movieName) {
        DoubanCommentUtil doubanMovieUtil = new DoubanCommentUtil();
        return doubanMovieUtil.getMovieDetail(movieName);
    }

    /*
    将抓取到的评论保存到数据库中,暂时效率较低
     */
    public void saveComments2DB(List<DoubanComment> doubanComments) {
        LOG.info("开始讲数据保存到数据库");
        try {
            for (DoubanComment doubanComment : doubanComments) {
                doubanCommetMapper.saveComments2DB(doubanComment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<DoubanComment> getDoubanCommentsFromDB(String movieName, Integer currentPage ,Integer pageSize) {
        Integer currentSize = null;
        List<DoubanComment> commentList = null;
        try {
            currentSize = currentPage * pageSize;
            commentList = doubanCommetMapper.getDoubanCommentsFromDB(movieName, currentSize, pageSize);
        }catch (Exception e){
            e.printStackTrace();
        }
        return commentList;
    }

    public Boolean checkMovieExist(String movieName) {
        Integer count = doubanCommetMapper.checkMovieExist(movieName);
        if(count==0) return false;
        else return true;
    }

    public MovieDetail getMovieDetailFromDB(String movieName) {
       return doubanCommetMapper.getMovieDetailFromDB(movieName);
    }


    public void saveMovieDetail(MovieDetail movieDetail) {
        try {
            doubanCommetMapper.saveMovieDetail(movieDetail);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Integer getTotalCount(String movieName){
        return doubanCommetMapper.getTotalCount(movieName);
    }

    /*
    保存文件
     */
    public void saveComment2File(List<DoubanComment> doubanComments) {
        List<String> commentStrs = new ArrayList<String>();
        String movieName = null;
        if(doubanComments.size() >0){
            movieName = doubanComments.get(0).getMovieName();
        }else{
            LOG.info("爬取出错!");
            return;
        }
        for (DoubanComment doubanComment : doubanComments) {
            commentStrs.add(doubanComment.getUserName() + "\t"
                    + doubanComment.getComment() + "\t"
                    + doubanComment.getTime() + "\t"
                    + doubanComment.getStar());
        }

        try {
            FileUtils.writeLines(new File("/Users/wenyue/test/" + movieName + ".txt"), commentStrs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<MovieDetail> getAllMovieDetail() {
        List<MovieDetail> movieDetailList = null;
        try{
            movieDetailList = doubanCommetMapper.getAllMovieDetail();
        }catch (Exception e){
            e.printStackTrace();
        }
       return movieDetailList;
    }

    public void deleteMovieDetail(String movieName) {
        try {
            doubanCommetMapper.deleteMovieDetail(movieName);
            doubanCommetMapper.deleteMovieComment(movieName);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public List<DoubanComment> getAllDoubanComment(String movieName){
        return doubanCommetMapper.getAllDoubanComment(movieName);
    }

    public void updateMovieDetail(MovieDetail movieDetail) {
        doubanCommetMapper.updateMovieDetail(movieDetail);
    }

    public List<MovieDetail> getMovieByPage(Integer currentSize, Integer pageSize) {
        return doubanCommetMapper.getMovieByPage(currentSize, pageSize);
    }

    public List<DoubanComment> getBackUpDoubanComment(String movieName){
        return doubanCommetMapper.getBackUpDoubanComment(movieName);
    }

    public MovieDetail getBackUpMovieDetail(String movieName){
        return doubanCommetMapper.getBackUpMovieDetail(movieName);
    }
}