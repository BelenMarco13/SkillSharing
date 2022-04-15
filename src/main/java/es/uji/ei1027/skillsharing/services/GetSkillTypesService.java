package es.uji.ei1027.skillsharing.services;

import es.uji.ei1027.skillsharing.dao.SkillTypeDao;
import es.uji.ei1027.skillsharing.model.SkillType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetSkillTypesService {

    @Autowired
    private SkillTypeDao skillTypeDao;

    public List<SkillType> getSkillTypes(){
        return skillTypeDao.getSkills();
    }

}
