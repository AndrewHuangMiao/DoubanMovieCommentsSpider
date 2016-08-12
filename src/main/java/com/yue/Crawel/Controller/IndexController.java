package com.yue.Crawel.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Andrew on 16/4/21.
 */

@RequestMapping("/index")
@Controller
public class IndexController {

    @RequestMapping("/test")
    public String index(){
        return "index";
    }

}
