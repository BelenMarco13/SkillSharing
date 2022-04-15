package es.uji.ei1027.skillsharing.controller;

import es.uji.ei1027.skillsharing.Level;
import es.uji.ei1027.skillsharing.SkillsharingApplication;
import es.uji.ei1027.skillsharing.dao.OfferDao;
import es.uji.ei1027.skillsharing.dao.RequestDao;
import es.uji.ei1027.skillsharing.model.Request;
import es.uji.ei1027.skillsharing.model.SkillType;
import es.uji.ei1027.skillsharing.model.Student;
import es.uji.ei1027.skillsharing.services.GetSkillTypesService;
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
import java.util.logging.Logger;


@Controller
@RequestMapping("/request")
public class RequestController {

    @Autowired
    private GetSkillTypesService getSkillTypesService;

    private RequestDao requestDao;
    private static final Logger log =
            Logger.getLogger(SkillsharingApplication.class.getName());
    @Autowired
    public void setRequestDao(RequestDao requestDao){
        this.requestDao = requestDao;
    }

    @RequestMapping("/list")
    public String listOffers(Model model){
        model.addAttribute("requests", requestDao.getRequests());
        return "request/list";
    }

    @RequestMapping("/add")
    public String addRequest(Model model, HttpSession session) {

        if (session.getAttribute("student") == null){
            session.setAttribute("nextUrl", "/request/add");
            return "redirect:../login";
        }

        model.addAttribute("request", new Request());
        model.addAttribute("skillTypes", getSkillTypesService.getSkillTypes());
        model.addAttribute("values", Level.values());
        return "request/add";
    }

    //Collect the form
    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(Model model, @ModelAttribute("request") Request request,
                                   BindingResult bindingResult, HttpSession session) {

        if (bindingResult.hasErrors()){
            model.addAttribute("values", Level.values());
            return "request/add";}

        requestDao.addRequest(request, (Student)session.getAttribute("student"));
        return "redirect:list";
    }

    //Send the form
    @RequestMapping(value="/update/{id}", method = RequestMethod.GET)
    public String editRequest(Model model, @PathVariable int id) {
        model.addAttribute("request", requestDao.getRequest(id));
        model.addAttribute("values", Level.values());
        log.info("cojo la info");
        return "request/update";
    }

    //Collect the form
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(Model model, @ModelAttribute("request") Request request,
            BindingResult bindingResult) {
        log.info(request.toString());
        if (bindingResult.hasErrors()) {
            model.addAttribute("values", Level.values());
            return "request/update";
        }
        requestDao.updateRequest(request);
        return "redirect:list";
    }

    //Delete request
    @RequestMapping(value="/delete/{id}")
    public String processDelete(@PathVariable int id) {
        requestDao.deleteRequest(id);
        return "redirect:../list";
    }



}