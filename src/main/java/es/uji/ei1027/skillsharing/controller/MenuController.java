package es.uji.ei1027.skillsharing.controller;

import es.uji.ei1027.skillsharing.model.Student;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class MenuController {

    @RequestMapping("/menu")
    public String index(HttpSession session){
        if(session.getAttribute("student") == null) {
            session.setAttribute("nextUrl", "/menu");
            return "redirect:/login";
        }
        Student student = (Student) session.getAttribute("student");
        if( ! student.getSkp()) {
            return "redirect:/login";
        }
        return "menu";
    }
}