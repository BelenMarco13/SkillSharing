package es.uji.ei1027.skillsharing.controller;

import es.uji.ei1027.skillsharing.model.Request;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class RequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> cls) {
        return Request.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        Request request = (Request) obj;
        if(request.getName().equals(""))
            errors.rejectValue("name", "required","Name required");
        if(request.getDescription().equals(""))
            errors.rejectValue("description", "required","Description required");
        if(request.getStartDate() == null)
            errors.rejectValue("startDate", "required","Start date required");
        if(request.getEndDate() == null)
            errors.rejectValue("endDate", "required","End date required");
    }
}
