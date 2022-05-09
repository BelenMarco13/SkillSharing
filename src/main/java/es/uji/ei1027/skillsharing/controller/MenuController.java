package es.uji.ei1027.skillsharing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class MenuController {


    @RequestMapping("/menu")
    public String index(HttpSession session){

        return "menu";
    }
}