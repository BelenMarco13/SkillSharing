package es.uji.ei1027.skillsharing.controller;

import es.uji.ei1027.skillsharing.Gender;
import es.uji.ei1027.skillsharing.Level;
import es.uji.ei1027.skillsharing.dao.OfferDao;
import es.uji.ei1027.skillsharing.model.Offer;
import es.uji.ei1027.skillsharing.model.Student;
import es.uji.ei1027.skillsharing.services.GetSkillTypesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.print.DocFlavor;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/offer")
public class OfferController {

    private OfferDao offerDao;
    private GetSkillTypesService getSkillTypesService;

    @Autowired
    public void setOfferDao(OfferDao offerDao){
        this.offerDao = offerDao;
    }

    @RequestMapping("/list")
    public String listOffers(Model model){
        model.addAttribute("offers", offerDao.getOffers());
        return "offer/list";
    }

    @RequestMapping("/add")
    public String addOffer(Model model, HttpSession session){

        if (session.getAttribute("student") == null){
            session.setAttribute("nextUrl", "/offer/add");
            return "redirect:../login";
        }

        model.addAttribute("offer", new Offer());
        model.addAttribute("skillTypes", getSkillTypesService.getSkillTypes());
        model.addAttribute("values", Level.values());
        return "offer/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAndSubmit(Model model, @ModelAttribute("offer") Offer offer,
                                   BindingResult bindingResult, HttpSession session){

        if (bindingResult.hasErrors()){
            model.addAttribute("values", Level.values());
            for (ObjectError e: bindingResult.getAllErrors()){
                System.out.println(e.toString());
            }
            return "offer/add";
        }

        offerDao.addOffer(offer, (Student)session.getAttribute("student"));
        return "redirect:list";
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String editOffer(Model model, @PathVariable int id){
        model.addAttribute("offer", offerDao.getOffer(id));
        model.addAttribute("values", Level.values());
        return "offer/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateAndSubmit(Model model, @ModelAttribute("offer") Offer offer, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            model.addAttribute("values", Level.values());
            return "offer/update";
        }
        offerDao.updateOffer(offer);
        return "redirect:list";
    }

    @RequestMapping("/delete/{id}")
    public String processDelete(@PathVariable int id){
        offerDao.deleteOffer(id);
        return "redirect:../list";
    }
}
