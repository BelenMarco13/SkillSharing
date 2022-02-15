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

    //Envía el formulario
    @RequestMapping(value="/add")
    public String addStudent(Model model) {
        model.addAttribute("student", new Student());
        return "student/add";
    }

    //Recoge el formulario
    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("student") Student student,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "student/add";
        studentDao.addStudent(student);
        return "redirect:list";
    }
}
