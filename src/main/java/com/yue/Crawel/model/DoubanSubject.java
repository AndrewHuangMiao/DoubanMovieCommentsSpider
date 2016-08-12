package com.yue.Crawel.model;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Author: abekwok
 * Create: 12/26/14
 */
public class DoubanSubject {
    private String subjectId;
    private String title;
    private String imgUrl;
    private String desc;
    private float rate;
    private int commentNum;

    public DoubanSubject() {
    }

    public DoubanSubject(String subjectId, String title, String imgUrl, String desc, float rate, int commentNum) {
        this.subjectId = subjectId;
        this.title = title;
        this.imgUrl = imgUrl;
        this.desc = desc;
        this.rate = rate;
        this.commentNum = commentNum;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public Map<String,String> getCountryTime() {
        Map<String, String> countryTime = new HashMap<String, String>();
        String sps[] = desc.split(" / ");
        for (String seg : sps) {
            if (!seg.contains("-"))
                break;
            if(seg.contains("(")){
                String time = seg.split("\\(")[0];
                String country = seg.split("\\(")[1].split("\\)")[0];
                countryTime.put(country,time);
            } else{
                countryTime.put("unknown",seg);
            }
        }
        return countryTime;
    }

    @Override
    public String toString() {
        return "DoubanSubject{" +
                "subjectId='" + subjectId + '\'' +
                ", title='" + title + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", desc='" + desc + '\'' +
                ", rate=" + rate +
                ", commentNum=" + commentNum +
                '}';
    }

    public String toGsonString() {
        Gson gson = new Gson();
        return gson.toJson(this).toString();
    }

    public static DoubanSubject fromGsonString(String gsonString) {
        Gson gson = new Gson();
        return gson.fromJson(gsonString, DoubanSubject.class);
    }

}
