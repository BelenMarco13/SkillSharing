package es.uji.ei1027.skillsharing.controller;

import es.uji.ei1027.skillsharing.Gender;
import es.uji.ei1027.skillsharing.dao.StudentDao;
import es.uji.ei1027.skillsharing.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/student")
public class StudentController {
    private StudentDao studentDao;

    @Autowired
    public void setStudentDao(StudentDao studentDao) {
        this.studentDao=studentDao;
    }

    @RequestMapping("/list")
    public String listStudents(HttpSession session, Model model) {
        if(session.getAttribute("student") == null) {
            model.addAttribute("student", new Student());
            session.setAttribute("nextUrl", "student/list");
            return "login";
        }
        model.addAttribute("students", studentDao.getStudents());
        return "student/list";
    }

    //Send the form
    @RequestMapping(value="/add")
    public String addStudent(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("values", Gender.values());
        return "student/add";
    }

    //Collect the form
    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(Model model, @ModelAttribute("student") Student student,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("values", Gender.values());
            return "student/add";
        }
        studentDao.addStudent(student);
        return "redirect:list";
    }

    //Send the form
    @RequestMapping(value="/update/{dni}", method = RequestMethod.GET)
    public String editStudent(Model model, @PathVariable String dni) {
        model.addAttribute("student", studentDao.getStudent(dni));
        model.addAttribute("values", Gender.values());
        return "student/update";
    }

    //Collect the form
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit( Model model, @ModelAttribute("student") Student student,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("values", Gender.values());
            return "student/update";
        }
        studentDao.updateStudent(student);
        return "redirect:list";
    }

    //Delete student
    @RequestMapping(value="/delete/{dni}")
    public String processDelete(@PathVariable String dni) {
        studentDao.deleteStudentDni(dni);
        return "redirect:../list";
    }
}
