package es.uji.ei1027.skillsharing.controller;

import es.uji.ei1027.skillsharing.model.Student;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class StudentValidator implements Validator {
    @Override
    public boolean supports(Class<?> cls) {
        return Student.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        Student student = (Student) obj;
        if(student.getDni().trim().equals(""))
            errors.rejectValue("dni", "required","ID required");
        if(student.getPwd().trim().equals(""))
            errors.rejectValue("pwd", "required","Password required");
    }
}
