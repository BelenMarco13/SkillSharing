package es.uji.ei1027.skillsharing.services;

import es.uji.ei1027.skillsharing.dao.SkillTypeDao;
import es.uji.ei1027.skillsharing.model.SkillType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetSkillTypesService {

    @Autowired
    private SkillTypeDao skillTypeDao;

    public List<SkillType> getSkillTypes(){
        return skillTypeDao.getSkills();
    }

    public List<String> getSkillTypeLevel(){
        List<SkillType> skillList = skillTypeDao.getSkills();
        List<String> skillTypeLevelList = new ArrayList<String>();
        String cadena;
        int i = 0;

        for (SkillType skillType : skillList){
            cadena = skillType.getName() + " " + skillType.getLevel();
            skillTypeLevelList.add(i++,cadena);
        }
        return skillTypeLevelList;
    }
}
