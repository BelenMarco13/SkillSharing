package es.uji.ei1027.skillsharing.controller;

import es.uji.ei1027.skillsharing.Level;
import es.uji.ei1027.skillsharing.dao.OfferDao;
import es.uji.ei1027.skillsharing.model.Collaboration;
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
import es.uji.ei1027.skillsharing.services.CollabService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/offer")
public class OfferController {

    @Autowired
    private GetSkillTypesService getSkillTypesService;

    @Autowired
    private CollabService collabService;
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

    //lista requests del usuario
    @RequestMapping("/listusu")
    public String listOffersUsu(Model model, HttpSession session) throws NullPointerException {
        Student student= (Student) session.getAttribute("student");
        model.addAttribute("offersUsuario", offerDao.getOffers(student));

        return "offer/listusu";
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
        return "redirect:listusu";
    }

    @RequestMapping("/listcolab/{skillName}/{skillLevel}/{idreq}")
    public String listReqsColab(Model model,@PathVariable String skillName, @PathVariable Level skillLevel, @PathVariable int idreq ) throws NullPointerException {
        List<Collaboration> colabs = collabService.getCollabsReq(idreq);
        List<Offer> offers = offerDao.getOffers(skillName,skillLevel);
        for( Collaboration colab : colabs){
            for( Offer offer : offers){
                if( colab.getIdOffer() == offer.getId()){
                    offers.remove(offer);
                }
            }
        }
        model.addAttribute("offersColab", offers);
        model.addAttribute("skillName",skillName);
        model.addAttribute("skillLevel", skillLevel);
        model.addAttribute("idreq", idreq);


        return "offer/listcolab";
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
