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
import java.time.LocalDate;

@Controller
@RequestMapping("/offer")
public class OfferController {

    @Autowired
    private GetSkillTypesService getSkillTypesService;
    private OfferDao offerDao;

    @Autowired
    public void setOfferDao(OfferDao offerDao){
        this.offerDao = offerDao;
    }

    @RequestMapping("/list")
    public String listOffers(Model model){
        model.addAttribute("offers", offerDao.getOffers());
        return "offer/list";
    }

    //lista de offers del usuario
    @RequestMapping("/listusu")
    public String listOffersUsu(Model model, HttpSession session) throws NullPointerException {
        Student student= (Student) session.getAttribute("student");
        model.addAttribute("offersUsuario", offerDao.getOffers(student));

        return "offer/listusu";
    }

    @RequestMapping("/listcolab/{skillName}/{skillLevel}")
    public String listOffersColab(Model model,@PathVariable String skillName, @PathVariable Level skillLevel ) throws NullPointerException {
        model.addAttribute("offersColab", offerDao.getOffers(skillName,skillLevel));
        model.addAttribute("skillName",skillName);
        model.addAttribute("skillLevel", skillLevel);
        return "offer/listcolab";
    }


    @RequestMapping("/add")
    public String addOffer(Model model, HttpSession session){

        if (session.getAttribute("student") == null){
            session.setAttribute("nextUrl", "/offer/add");
            return "redirect:../login";
        }

        model.addAttribute("offer", new Offer());
        model.addAttribute("skillTypes", getSkillTypesService.getSkillTypeLevel());
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
        model.addAttribute("skillTypes", getSkillTypesService.getSkillTypeLevel());
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

    @RequestMapping("/end/{id}")
    public String processEnd(@PathVariable int id){
        offerDao.endOffer(id);
        return "redirect:../list";
    }
}
