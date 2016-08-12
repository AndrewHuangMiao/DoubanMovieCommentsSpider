package com.yue.Crawel.Controller;

import com.yue.Crawel.Service.DoubanService;
import com.yue.Crawel.model.DoubanComment;
import com.yue.Crawel.util.DownloadUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrew on 16/5/12.
 */
@Controller
@RequestMapping("/download")
public class DownloadController {

    @Autowired
    DoubanService doubanService;

    @RequestMapping("/downloadComment")
    @ResponseBody
    public ResponseEntity<byte[]> download(HttpServletRequest request) throws IOException {
        Map<String,String[]> requestMap = request.getParameterMap();
        String movieName = null;
        try {
            movieName = requestMap.get("movieName")[0];
        }catch (Exception e){
            e.printStackTrace();
        }
        movieName = new String(movieName.getBytes("ISO8859-1"),"UTF-8");
        List<String> commentStrs = new LinkedList<String>();
        File file = new File("/Users/wenyue/test/" + movieName + ".txt");
        if(!file.exists()){
            file.createNewFile();
        }
        List<DoubanComment> doubanComments = doubanService.getAllDoubanComment(movieName);
        for (DoubanComment doubanComment : doubanComments) {
            commentStrs.add(doubanComment.getUserName() + "\t"
                    + doubanComment.getComment() + "\t"
                    + doubanComment.getTime() + "\t"
                    + doubanComment.getStar());

        }

        FileUtils.writeLines(file, commentStrs);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", new String((movieName+".txt").getBytes("UTF-8"), "ISO8859-1"));  //解决文件名中文乱码问题
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
    }
}
