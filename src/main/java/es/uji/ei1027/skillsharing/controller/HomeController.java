package es.uji.ei1027.skillsharing.controller;

import es.uji.ei1027.skillsharing.model.Student;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {


   @RequestMapping("/")
    public String index(HttpSession session){

        return "index";
    }






}
