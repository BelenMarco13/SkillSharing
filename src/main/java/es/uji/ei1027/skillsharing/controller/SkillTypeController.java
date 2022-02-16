package es.uji.ei1027.skillsharing.controller;

import es.uji.ei1027.skillsharing.dao.SkillTypeDao;
import es.uji.ei1027.skillsharing.model.SkillType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/skillType")
public class SkillTypeController {
    private SkillTypeDao skillTypeDao;

    @Autowired
    public void setSkillTypeDao(SkillTypeDao skillTypeDao) {
        this.skillTypeDao=skillTypeDao;
    }

    @RequestMapping("/list")
    public String listSkills(Model model) {
        model.addAttribute("skills", skillTypeDao.getSkills());
        return "skillType/list";
    }

    //Send the form
    @RequestMapping(value="/add")
    public String addSkillType(Model model) {
        model.addAttribute("skillType", new SkillType());
        return "skillType/add";
    }

    //Collect the form
    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("skillType") SkillType skillType,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "skillType/add";
        skillTypeDao.addSkillType(skillType);
        return "redirect:list";
    }

    //Send the form
    @RequestMapping(value="/update/{name}/{level}", method = RequestMethod.GET)
    public String editSkillType(Model model, @PathVariable String name, @PathVariable int level) {
        model.addAttribute("skillType", skillTypeDao.getSkillType(name, level));
        return "skillType/update";
    }

    //Collect the form
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(
            @ModelAttribute("skillType") SkillType skillType,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "skillType/update";
        skillTypeDao.updateSkillType(skillType);
        return "redirect:list";
    }

    //Delete skillType
    @RequestMapping(value="/delete/{name}/{level}")
    public String processDelete(@PathVariable String name, @PathVariable int level) {
        skillTypeDao.deleteSkillKey(name, level);
        return "redirect:../list";
    }
}
