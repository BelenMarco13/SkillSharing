package es.uji.ei1027.skillsharing.controller;

import es.uji.ei1027.skillsharing.Gender;
import es.uji.ei1027.skillsharing.dao.StudentDao;
import es.uji.ei1027.skillsharing.model.Request;
import es.uji.ei1027.skillsharing.model.Student;
import es.uji.ei1027.skillsharing.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {
    private StudentDao studentDao;
    private EmailService emailService = new EmailService();

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

    @RequestMapping("/perfil")
    public String perfilStudent(HttpSession session, Model model) {
        if(session.getAttribute("student") == null) {
            session.setAttribute("nextUrl", "/student/perfil");
            return "login";
        }
        model.addAttribute("student", session.getAttribute("student"));
        return "student/perfil";
    }

    //Send the form
    @RequestMapping(value="/add")
    public String addStudent(Model model, HttpSession session) {
        model.addAttribute("student", new Student());
        model.addAttribute("values", Gender.values());
        return "student/add";
    }

    //Collect the form
    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(Model model, @ModelAttribute("student") Student student,
                                   BindingResult bindingResult) {

        StudentValidator studentValidator = new StudentValidator();
        studentValidator.validateRegister(student, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("values", Gender.values());
            return "student/add";
        }

        Student user = studentDao.getStudent(student.getDni());

        if (user != null) {
            bindingResult.rejectValue("dni", "registeredDni", "This dni is already registered");
            return "student/add";
        }

        studentDao.addStudent(student);
        return "redirect:/";
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
        return "redirect:../";
    }

    //Cancel account
    @RequestMapping(value="/cancelAccount/{dni}")
    public String processCancelAccount(@PathVariable String dni) {
        studentDao.cancelAccount(dni);

        //Email Service
        String destinatario = studentDao.getStudent(dni).getEmail();
        String nombre = studentDao.getStudent(dni).getName();
        String skpEmail = studentDao.getSkp().getEmail();
        String cuerpo = "Hello " + nombre + ",\n\nYour account in the Skill Exchange Application has been blocked, " +
                "if you believe this is an error please contact the administrator at the following e-mail address " +
                skpEmail + "\n\n\tThe Skill Sharing Team.";
        String asunto = "Cancel Account";
        emailService.enviarConGMail(destinatario,asunto,cuerpo);

        return "redirect:../list";
    }

    //Return account
    @RequestMapping(value="/returnAccount/{dni}")
    public String processReturnAccount(@PathVariable String dni) {
        studentDao.returnAccount(dni);

        //Email Service
        String destinatario = studentDao.getStudent(dni).getEmail();
        String nombre = studentDao.getStudent(dni).getName();
        String cuerpo = "Hello " + nombre + ",\n\nYour account in Skill Sharing Application has been reinstated." +
                "\n\n\tThe Skill Sharing Team.";
        String asunto = "Reinstated Account.";
        emailService.enviarConGMail(destinatario,asunto,cuerpo);

        return "redirect:../list";
    }

    @RequestMapping(value = "/search/{searchStudent}", method = RequestMethod.GET)
    public String searchRequest(Model model, @PathVariable String name){
        model.addAttribute("searchStudent", name);
        return "student/list";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String processSearch(Model model, @ModelAttribute("searchStudent") String name, BindingResult bindingResult){
        model.addAttribute("students", studentDao.getStudentName(name));
        return "student/list";
    }
}
