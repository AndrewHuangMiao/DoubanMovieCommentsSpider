package com.yue.Crawel.dao;

import com.yue.Crawel.model.DoubanComment;
import com.yue.Crawel.model.MovieDetail;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by Andrew on 16/4/30.
 */
public interface DoubanCommetMapper {

     void saveComments2DB(@Param("doubanComment")DoubanComment doubanComment);

     List<DoubanComment> getDoubanCommentsFromDB(@Param("movieName")String movieName, @Param("currentSize")Integer currentSize, @Param("pageSize")Integer pageSize);

     Integer checkMovieExist(@Param("movieName")String movieName);

     MovieDetail getMovieDetailFromDB(@Param("movieName")String movieName);

     void saveMovieDetail(@Param("movieDetail")MovieDetail movieDetail);

     Integer getTotalCount(@Param("movieName")String movieName);

     List<MovieDetail> getAllMovieDetail();

     void deleteMovieDetail(@Param("movieName")String movieName);

     void deleteMovieComment(@Param("movieName")String movieName);

     List<DoubanComment> getAllDoubanComment(@Param("movieName")String movieName);

     void updateMovieDetail(@Param("movieDetail")MovieDetail movieDetail);

     List<MovieDetail> getMovieByPage(@Param("currentSize")Integer currentSize, @Param("pageSize")Integer pageSize);

     List<DoubanComment> getBackUpDoubanComment(@Param("movieName")String movieName);

     MovieDetail getBackUpMovieDetail(@Param("movieName")String movieName);

}
