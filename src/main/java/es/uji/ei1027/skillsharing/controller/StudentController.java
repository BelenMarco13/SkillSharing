package es.uji.ei1027.skillsharing.controller;

import es.uji.ei1027.skillsharing.dao.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student")
public class StudentController {
    private StudentDao studentDao;

    @Autowired
    public void setStudentDao(StudentDao studentDao) {
        this.studentDao=studentDao;
    }

    @RequestMapping("/list")
    public String listStudents(Model model) {
        model.addAttribute("students", studentDao.getStudents());
        return "student/list";
    }

}
