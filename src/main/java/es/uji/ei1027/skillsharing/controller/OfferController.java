package es.uji.ei1027.skillsharing.controller;

import es.uji.ei1027.skillsharing.dao.OfferDao;
import es.uji.ei1027.skillsharing.model.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
