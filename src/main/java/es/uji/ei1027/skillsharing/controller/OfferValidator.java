package es.uji.ei1027.skillsharing.controller;

import es.uji.ei1027.skillsharing.model.Offer;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class OfferValidator implements Validator {
    @Override
    public boolean supports(Class<?> cls) {
        return Offer.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        Offer offer = (Offer) obj;
        if(offer.getName().equals(""))
            errors.rejectValue("name", "required","Name required");
        if(offer.getDescription().equals(""))
            errors.rejectValue("description", "required","Description required");
        if(offer.getStartDate() == null)
            errors.rejectValue("startDate", "required","Start date required");
        if(offer.getEndDate() == null)
            errors.rejectValue("endDate", "required","End date required");
    }
}
