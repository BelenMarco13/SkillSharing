package es.uji.ei1027.skillsharing.controller;

import es.uji.ei1027.skillsharing.Level;
import es.uji.ei1027.skillsharing.dao.OfferDao;
import es.uji.ei1027.skillsharing.model.Collaboration;
import es.uji.ei1027.skillsharing.model.Offer;
import es.uji.ei1027.skillsharing.model.Request;
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
import java.util.ArrayList;
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

    private List<List<Offer>> getValidOfferInContainers(List<Offer> rawOfferList){
        List<List<Offer>> listContainers = new ArrayList<>();
        List<Offer> offers = new ArrayList<>();

        // Add just non-blank offers (which are used just to create a collaboration without offer)
        for (Offer offer :
                rawOfferList) {
            if (!offer.getName().equals("null") && !offer.getDescription().equals("null"))
                offers.add(offer);
        }

        for (int i = 0; i < offers.size() / 3; i++) {
            listContainers.add(List.of(offers.get(i), offers.get(i+1), offers.get(i+2)));
        }
        if (offers.size()%3 == 1)
            listContainers.add(List.of(offers.get(offers.size()-1)));
        else if (offers.size()%3 == 2)
            listContainers.add(List.of(offers.get(offers.size()-2),offers.get(offers.size()-1)));

        return listContainers;
    }

    @RequestMapping("/list")
    public String listOffers(Model model){

        List<List<Offer>> listContainers = getValidOfferInContainers(offerDao.getOffers());

        model.addAttribute("listContainers", listContainers);
        return "offer/list";
    }

    //lista requests del usuario
    @RequestMapping("/listusu")
    public String listOffersUsu(Model model, HttpSession session) throws NullPointerException {
        Student student= (Student) session.getAttribute("student");
        model.addAttribute("offersUsuario", getValidOfferInContainers(offerDao.getOffers(student)));
        model.addAttribute("title", "List of my offers");

        return "offer/listusu";
    }

    @RequestMapping("/manage")
    public String listOffersManage(Model model, HttpSession session) throws NullPointerException {
        model.addAttribute("offersUsuario", getValidOfferInContainers(offerDao.getOffers()));
        model.addAttribute("title", "List of offers");

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
        List<Offer> offers = collabService.getOptionsColabs(idreq, skillLevel,skillName);
        model.addAttribute("skillName",skillName);
        model.addAttribute("skillLevel", skillLevel);
        model.addAttribute("idreq", idreq);
        if (offers.isEmpty()){
            return "offer/listcolabvacio";
        }
        else{
            model.addAttribute("offersColab", getValidOfferInContainers(offers));
            return "offer/listcolab";
        }

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

    @RequestMapping(value = "/search/{searchOffer}", method = RequestMethod.GET)
    public String searchOffer(Model model, @PathVariable String skill){
        model.addAttribute("searchOffer", skill);
        return "offer/list";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String processSearch(Model model, @ModelAttribute("searchOffer") String skill, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "offer/list";
        }

        List<List<Offer>> listContainers = new ArrayList<>();
        List<Offer> offers = offerDao.getOffersSkill(skill);
        for (int i = 0; i < offers.size() / 3; i++) {
            listContainers.add(List.of(offers.get(i), offers.get(i+1), offers.get(i+2)));
        }
        if (offers.size()%3 == 1)
            listContainers.add(List.of(offers.get(offers.size()-1)));
        else if (offers.size()%3 == 2)
            listContainers.add(List.of(offers.get(offers.size()-2),offers.get(offers.size()-1)));

        model.addAttribute("listContainers", listContainers);
        return "offer/list";
    }

}
