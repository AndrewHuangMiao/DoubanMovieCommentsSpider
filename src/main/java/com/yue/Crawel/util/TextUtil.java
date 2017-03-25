package com.yue.Crawel.util;

import java.util.List;

/**
 * Author: andrew
 */
public class TextUtil {
    /**
     * 去掉 //@ 后面的转发内容
     * @param text
     * @return
     */
    public static String removeRepost(String text){
        return text.split("//@")[0]; // 去掉转发内容
    }

    /**
     * 去掉@XXX
     * @param org
     * @return
     */
    public static String removeAts(String org){
        String filtered = org;
        filtered = removeBetween(filtered,'@',' ');
        filtered = removeBetween(filtered,'@','@');
        return filtered.trim();
    }
    /**
     * 去掉两个字符之间的内容
     * @param org
     * @param str
     * @param end
     * @return
     */
    public static String removeBetween(String org, char str, char end){
        String filtered = org;
        int atFlagPos = filtered.indexOf(str);
        String head = null;
        String tail = null;
        while(-1 != atFlagPos){
            head = filtered.substring(0,filtered.indexOf(str));
            tail = filtered.substring(filtered.indexOf(str));
            if(-1!=tail.indexOf(end)){
                filtered = head + tail.substring(tail.indexOf(end)+1);
            }
            else{
                filtered = head;
            }
            atFlagPos = filtered.indexOf(str);
        }
        return filtered;
    }

    public static <T> String packList(List<T> list){
        StringBuilder s = new StringBuilder();
        s.append("(");
        for(T t: list){
            if(s.length()>1)
                s.append(",");
            s.append(t);
        }
        s.append(")");
        return s.toString();
    }

    /**
     * 加单引号
     * @param list
     * @param <T>
     * @return
     */
    public static <T> String packListWithSingleQuo(List<T> list){
        StringBuilder s = new StringBuilder();
        s.append("(");
        for(T t: list){
            if(s.length()>1)
                s.append(",");
            s.append("'"+t+"'");
        }
        s.append(")");
        return s.toString();
    }


    public static String getFirstBetween(String content, String before, String after){
        try{
            int i = content.indexOf(before);
            if(i<0) return null;
            content = content.substring(i+before.length());
            i = content.indexOf(after);
            if(i<0) return null;
            return content.substring(0,i);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
