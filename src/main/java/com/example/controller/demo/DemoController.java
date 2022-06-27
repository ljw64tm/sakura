package com.example.controller.demo;

import com.example.service.file.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;


@Controller("/")
public class DemoController {

    @Autowired
    private DemoService demoService;

    @RequestMapping("/")
    public void index(HttpServletResponse response) throws IOException {
        response.sendRedirect("/static/index.html");
    }

    @GetMapping("/testdb")
    @ResponseBody
    public String testdb(HttpServletResponse response) throws IOException {
        return demoService.testdb() + "";
    }

    @GetMapping("hello")
    @ResponseBody
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        System.out.println(new Date());
        System.out.println("-----------------------------------------------");
        return String.format("Hello %s!", name);
    }
}