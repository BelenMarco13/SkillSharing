package es.uji.ei1027.skillsharing.controller;

import es.uji.ei1027.skillsharing.dao.OfferDao;
import es.uji.ei1027.skillsharing.model.Offer;
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

@Controller
@RequestMapping("/offer")
public class OfferController {

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

    @RequestMapping("/add")
    public String addOffer(Model model){
        model.addAttribute("offer", new Offer());
        return "offer/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAndSubmit(@ModelAttribute("offer") Offer offer, BindingResult bindingResult){
        if (bindingResult.hasErrors()){

            for (ObjectError e: bindingResult.getAllErrors()){
                System.out.println(e.toString());
            }
            return "offer/add";
        }

        offerDao.addOffer(offer);
        return "redirect:list";
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String editOffer(Model model, @PathVariable int id){
        model.addAttribute("offer", offerDao.getOffer(id));
        return "offer/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateAndSubmit(@ModelAttribute("offer") Offer offer, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "offer/update";
        offerDao.updateOffer(offer);
        return "redirect:list";
    }

    @RequestMapping("/delete/{id}")
    public String processDelete(@PathVariable int id){
        offerDao.deleteOffer(id);
        return "redirect:list";
    }
}
