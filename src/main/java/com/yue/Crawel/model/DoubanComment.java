package com.yue.Crawel.model;


import com.yue.Crawel.util.TextUtil;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * Author: Andrew
 * 对应于数据库表中的douban_movie_comment
 */
public class DoubanComment implements Serializable{
    private static final long serialVersionUID = -3986244606585552569L;
    private String cid;
    private String userName;
    private String userId;
    private String headUrl;
    private String comment;
    private String time;
    private int star;
    private String movieName;

    public DoubanComment(){}

    public DoubanComment(String cid, String userName, String userId, String headUrl, String comment, String time, int star, String movieName) {
        this.cid = cid;
        this.userName = userName;
        this.userId = userId;
        this.headUrl = headUrl;
        this.comment = comment;
        this.time = time;
        this.star = star;
        this.movieName = movieName;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    @Override
    public String toString() {
        return "DoubanComment{" +
                "cid='" + cid + '\'' +
                ", userName='" + userName + '\'' +
                ", userId='" + userId + '\'' +
                ", headUrl='" + headUrl + '\'' +
                ", comment='" + comment + '\'' +
                ", time='" + time + '\'' +
                ", star=" + star + '\'' +
                ", movieName = " + movieName +
                '}';
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

//    public static DoubanComment fromToString(String toString) {
//        String cid = TextUtil.getFirstBetween(toString, "cid='", "'");
//        String userName = TextUtil.getFirstBetween(toString, "userName='", "'");
//        String userId = TextUtil.getFirstBetween(toString, "userId='", "'");
//        String headUrl = TextUtil.getFirstBetween(toString, "headUrl='", "'");
//        String comment = TextUtil.getFirstBetween(toString, "comment='", "'");
//        String time = TextUtil.getFirstBetween(toString, "time='", "'");
//        String star = TextUtil.getFirstBetween(toString, "star=", "}");
//        return new DoubanComment(cid, userName, userId, headUrl, comment, time, Integer.parseInt(star), );
//    }
}
