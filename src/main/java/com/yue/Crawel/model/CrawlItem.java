package com.yue.Crawel.model;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * Author: abekwok
 * Create: 12/25/14
 */
public class CrawlItem implements Serializable {
    private static final long serialVersionUID = -3986244606585552569L;
    private String subjectId;
    private int start;
    private int limit;

    public CrawlItem(){}

    public CrawlItem(String subjectId, int start, int limit) {
        this.subjectId = subjectId;
        this.start = start;
        this.limit = limit;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "CrawlItem{" +
                "subjectId='" + subjectId + '\'' +
                ", start=" + start +
                ", limit=" + limit +
                '}';
    }
}
