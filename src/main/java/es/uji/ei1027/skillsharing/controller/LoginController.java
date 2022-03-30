package es.uji.ei1027.skillsharing.controller;

import es.uji.ei1027.skillsharing.dao.StudentDao;
import es.uji.ei1027.skillsharing.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private StudentDao studentDao;

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("student", new Student());
        return "login";
    }

    @RequestMapping(value="/login", method= RequestMethod.POST)
    public String checkLogin(@ModelAttribute("student") Student student,
                             BindingResult bindingResult, HttpSession session) {
        StudentValidator studentValidator = new StudentValidator();
        studentValidator.validate(student, bindingResult);

        if (bindingResult.hasErrors()) {
            return "login";
        }

        student = studentDao.loadStudentByDni(student.getDni(), student.getPwd());
        if (student == null) {
            bindingResult.rejectValue("pwd", "badpwd", "Bad password");
            return "login";
        }

        session.setAttribute("student", student);

        String nextUrl = (String) session.getAttribute("nextUrl");
        if(nextUrl != null){
            session.removeAttribute(nextUrl);
            return "redirect:" + nextUrl;
        }
        return "redirect:/student/perfil";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @RequestMapping("/signUp")
    public String signUp(Model model) {
        model.addAttribute("student", new Student());
        return "perfil";
    }

    @RequestMapping(value="/signUp", method= RequestMethod.POST)
    public String checkSignUp(@ModelAttribute("student") Student student,
                             BindingResult bindingResult, HttpSession session) {
        StudentValidator studentValidator = new StudentValidator();
        studentValidator.validate(student, bindingResult);

        if (bindingResult.hasErrors()) {
            return "perfil";
        }

        student = studentDao.loadStudentByDni(student.getDni(), student.getPwd());
        if (student == null) {
            bindingResult.rejectValue("pwd", "badpwd", "Bad password");
            return "login";
        }

        session.setAttribute("student", student);

        String nextUrl = (String) session.getAttribute("nextUrl");
        if(nextUrl != null){
            session.removeAttribute(nextUrl);
            return "redirect:" + nextUrl;
        }
        return "redirect:/";
    }
}
