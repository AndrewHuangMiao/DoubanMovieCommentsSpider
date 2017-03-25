package com.yue.Crawel.model;

import com.google.gson.Gson;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: andrew.huang
 */
@Data
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

    public String toGsonString() {
        Gson gson = new Gson();
        return gson.toJson(this).toString();
    }

}
