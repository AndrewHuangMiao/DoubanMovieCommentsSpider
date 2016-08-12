package com.yue.Crawel.Controller;

import com.yue.Crawel.Service.DoubanService;
import com.yue.Crawel.model.DoubanComment;
import com.yue.Crawel.model.JSend;
import com.yue.Crawel.model.MovieDetail;
import org.apache.commons.net.ntp.TimeStamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Andrew on 16/4/17.
 */
@Controller
@RequestMapping("/crawel")
public class DoubanController {

    @Autowired
    DoubanService doubanService;

    /*
    通过这个exist来判断是否已经抓取过这个电影的评论
     */
    @RequestMapping("/getDoubanComments")
    @ResponseBody
    public List<DoubanComment> getDoubanComments(HttpServletRequest request){
        Map<String,String[]> requestMap = request.getParameterMap();
        List<DoubanComment> doubanComments = null;
        MovieDetail movieDetail = null;
        String movieName = null;
        Integer currentPage = null;
        Integer pageSize = null;
        try {
            movieName = requestMap.get("movieName")[0];
            currentPage = Integer.parseInt(requestMap.get("currentPage")[0]);
            pageSize = Integer.parseInt(requestMap.get("pageSize")[0]);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(movieName==null || movieName.equals("")){
            System.out.println("无法得到正确的movieName!");
            return new LinkedList<DoubanComment>();
        }
        doubanComments = doubanService.getDoubanComments(movieName);
        if(doubanComments==null){
            return null;
        }
        doubanService.saveComments2DB(doubanComments);
        if(movieDetail==null){
            movieDetail = doubanService.getMovieDetail(movieName);
        }
        if(movieDetail.getMovieName()==null){
            doubanComments = doubanService.getDoubanCommentsFromDB(movieName, currentPage, pageSize);
        }else{
            doubanComments = doubanService.getDoubanCommentsFromDB(movieDetail.getMovieName(), currentPage, pageSize);
        }
        //这里保留一个后门作为不时之需
        if(doubanComments == null || doubanComments.size()==0){
            doubanComments = doubanService.getBackUpDoubanComment(movieName);
        }
        return doubanComments;
    }


    @RequestMapping("/getMovieDetail")
    @ResponseBody
    public MovieDetail getMovieDetail(HttpServletRequest request){
        Map<String,String[]> requestMap = request.getParameterMap();
        String movieName = null;
        MovieDetail movieDetail = null;
        try {
            movieName = requestMap.get("movieName")[0];
        }catch (Exception e){
            e.printStackTrace();
        }
        if(movieName==null || movieName.equals("")){
            System.out.println("无法得到正确的movieName!");
            return new MovieDetail();
        }
        if(movieDetail==null){
            movieDetail = doubanService.getMovieDetail(movieName);
        }
        if(movieDetail == null){
            return null;
        }
        if(movieDetail.getMovieName()!=null){
            doubanService.saveMovieDetail(movieDetail);
        }
        //这里保留一个后门作为不时之需
        if(movieDetail == null || movieDetail.getMovieName()==null){
            movieDetail = doubanService.getBackUpMovieDetail(movieName);
            doubanService.saveMovieDetail(movieDetail);
        }
        return movieDetail;
    }

    /*
    判断电影是否已经被抓取过,如果是的话拿取
     */
    @RequestMapping("/checkMovie")
    @ResponseBody
    public Boolean checkMovie(HttpServletRequest request){
        Map<String,String[]> requestMap = request.getParameterMap();
        String movieName = null;
        try {
            movieName = requestMap.get("movieName")[0];
        }catch (Exception e){
            e.printStackTrace();
        }
        if(movieName==null || movieName.equals("")){
            System.out.println("无法得到正确的movieName!");
            return false;
        }
        Boolean isExist = doubanService.checkMovieExist(movieName);
        if(isExist){
            return true;
        }else{
            return false;
        }
    }

    @RequestMapping("/getTotalCount")
    @ResponseBody
    public Integer getTotalCount(HttpServletRequest request){
        Map<String,String[]> requestMap = request.getParameterMap();
        String movieName = null;
        try {
            movieName = requestMap.get("movieName")[0];
        }catch (Exception e){
            e.printStackTrace();
        }
        return doubanService.getTotalCount(movieName);
    }

    @RequestMapping("/getMovieDetailFromDB")
    @ResponseBody
    public MovieDetail getMovieDetailFromDB(HttpServletRequest request){
        Map<String,String[]> requestMap = request.getParameterMap();
        String movieName = null;
        try {
            movieName = requestMap.get("movieName")[0];
        }catch (Exception e){
            e.printStackTrace();
        }
        MovieDetail newMovieDetail = doubanService.getMovieDetailFromDB(movieName);
        return newMovieDetail;
    }

    @RequestMapping("/getCommentsFromDB")
    @ResponseBody
    public List<DoubanComment> getCommentFromDB(HttpServletRequest request){
        Map<String,String[]> requestMap = request.getParameterMap();
        String movieName = null;
        Integer currentPage = null;
        Integer pageSize = null;
        try {
            movieName = requestMap.get("movieName")[0];
            currentPage = Integer.parseInt(requestMap.get("currentPage")[0]);
            pageSize = Integer.parseInt(requestMap.get("pageSize")[0]);
        }catch (Exception e){
            e.printStackTrace();
        }
        List<DoubanComment> doubanComments = doubanService.getDoubanCommentsFromDB(movieName, currentPage, pageSize);
        return  doubanComments;
    }

    @RequestMapping("/getAllMovie")
    @ResponseBody
    public List<MovieDetail> getAllMovieDetail(HttpServletRequest request){
        List<MovieDetail> movieDetailList = doubanService.getAllMovieDetail();
        return movieDetailList;
    }

    @RequestMapping("/getMovieByPage")
    @ResponseBody
    public List<MovieDetail> getMovieByPage(HttpServletRequest request){
        Map<String,String[]> requestMap = request.getParameterMap();
        Integer currentPage = null;
        Integer pageSize = null;
        Integer currentSize = null;
        List<MovieDetail> movieDetailList = null;
        try {
            currentPage = Integer.parseInt(requestMap.get("currentPage")[0]);
            pageSize = Integer.parseInt(requestMap.get("pageSize")[0]);
            currentSize = currentPage * pageSize;
            movieDetailList = doubanService.getMovieByPage(currentSize, pageSize);
        }catch (Exception e){
            e.printStackTrace();
        }
        return movieDetailList;
    }


    @RequestMapping("/updateMovieDetail")
    @ResponseBody
    public Boolean updateMovieDetail(HttpServletRequest request){
        Map<String,String[]> requestMap = request.getParameterMap();
        String movieName = null;
        String director = null;
        String actors = null;
        String type = null;
        String time = null;
        String duration = null;
        String area = null;
        String headUrl = null;
        try {
            movieName = requestMap.get("movieName")[0];
            director = requestMap.get("director")[0];
            actors = requestMap.get("actors")[0];
            type = requestMap.get("type")[0];
            time = requestMap.get("time")[0];
            duration = requestMap.get("duration")[0];
            area = requestMap.get("area")[0];
            headUrl = requestMap.get("headUrl")[0];
            MovieDetail newMovieDetail = new MovieDetail();
            newMovieDetail.setType(type);
            newMovieDetail.setMovieImg(headUrl);
            newMovieDetail.setDuration(duration);
            newMovieDetail.setDirector(director);
            newMovieDetail.setActors(actors);
            newMovieDetail.setTime(time);
            newMovieDetail.setMovieName(movieName);
            newMovieDetail.setArea(area);
            doubanService.updateMovieDetail(newMovieDetail);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    @RequestMapping("/deleteMovieDetail")
    @ResponseBody
    public Boolean deleteMovieDetail(HttpServletRequest request){
        Map<String,String[]> requestMap = request.getParameterMap();
        String movieName = null;
        try {
            movieName = requestMap.get("movieName")[0];
            doubanService.deleteMovieDetail(movieName);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
       return false;
    }

    @RequestMapping("/getMovieCount")
    @ResponseBody
    public Integer getMovieCount(HttpServletRequest request){
        try {
            List<MovieDetail> movieDetailList = doubanService.getAllMovieDetail();
            return movieDetailList.size();
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    @RequestMapping("/crawelFromWeb")
    public String crawelFromWeb(){
        return "crawelFromWeb";
    }

    @RequestMapping("/analyseComment")
    @ResponseBody
    public Integer analyseComment(HttpServletRequest request){
        Map<String,String[]> requestMap = request.getParameterMap();
        String movieName = null;
        Integer total = 0;
        try {
            movieName = requestMap.get("movieName")[0];
        }catch (Exception e){
            e.printStackTrace();
        }
        List<DoubanComment> doubanComments = doubanService.getAllDoubanComment(movieName);
        for(DoubanComment doubanComment : doubanComments){
            total += doubanComment.getStar();
        }

        int avg = total / doubanComments.size();
        return avg;
    }

}
