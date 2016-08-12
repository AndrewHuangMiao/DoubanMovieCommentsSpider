package com.yue.Crawel.model;

import com.google.gson.Gson;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Author: abekwok
 * Create: 12/26/14
 */
public class MovieDetail {
    private Integer id;
    private String movieName;
    private String subjectId;
    private String director;
    private String actors;
    private String type;
    private String area;
    private String duration;
    private String time;
    private int totalComment;
    private String movieImg;
    private Map<String, String> moreInfo;

    public MovieDetail() {
    }

    public MovieDetail(String movieName, String subjectId, String director, String actors, String type, String area, String duration, String time, int totalComment, Map<String, String> moreInfo) {
        this.movieName = movieName;
        this.subjectId = subjectId;
        this.director = director;
        this.actors = actors;
        this.type = type;
        this.area = area;
        this.duration = duration;
        this.time = time;
        this.totalComment = totalComment;
        this.moreInfo = moreInfo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getTotalComment() {
        return totalComment;
    }

    public void setTotalComment(int totalComment) {
        this.totalComment = totalComment;
    }

    public Map<String, String> getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(Map<String, String> moreInfo) {
        this.moreInfo = moreInfo;
    }

//    @Override
//    public String toString() {
//        return "MovieDetail{" +
//                "subjectId='" + subjectId + '\'' +
//                ", director='" + director + '\'' +
//                ", actors='" + actors + '\'' +
//                ", type='" + type + '\'' +
//                ", dict='" + dict + '\'' +
//                ", duration='" + duration + '\'' +
//                ", time='" + time + '\'' +
//                ", totalComment=" + totalComment +
//                ", moreInfo=" + moreInfo +
//                '}';
//    }

    public String toGsonString() {
        Gson gson = new Gson();
        return gson.toJson(this).toString();
    }

    public static MovieDetail fromGsonString(String gsonString) {
        Gson gson = new Gson();
        return gson.fromJson(gsonString, MovieDetail.class);
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieImg() {
        return movieImg;
    }

    public void setMovieImg(String movieImg) {
        this.movieImg = movieImg;
    }
}
