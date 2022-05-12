package es.uji.ei1027.skillsharing.controller;

import es.uji.ei1027.skillsharing.dao.CollaborationDao;
import es.uji.ei1027.skillsharing.model.Collaboration;
import es.uji.ei1027.skillsharing.model.SkillType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import es.uji.ei1027.skillsharing.services.AddCollabService;
import javax.print.DocFlavor;

@Controller
@RequestMapping("/collaboration")
class CollaborationController {

    private CollaborationDao collaborationDao;
    @Autowired
    private AddCollabService addCollabService;
    @Autowired
    public void setCollaborationDao(CollaborationDao collaborationDao){
        this.collaborationDao = collaborationDao;
    }

    @RequestMapping("/list")
    public String listCollaborations(Model model){
        model.addAttribute("collaborations", collaborationDao.getCollaborations());
        return "collaboration/list";
    }

    @RequestMapping("/add/{idreq}/{idof}")
    public String añadirColab(Model model, @PathVariable int idreq, @PathVariable int idof){
        Collaboration collaboration = addCollabService.addColab(idreq,idof);
        collaborationDao.addCollaboration(collaboration);
        return "collaboration/list";
    }

    @RequestMapping("/add")
    public String addCollaboration(Model model){
        model.addAttribute("collaboration", new Collaboration());
        return "collaboration/add";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("collaboration") Collaboration collaboration,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "collaboration/add";
        collaborationDao.addCollaboration(collaboration);
        return "redirect:list";
    }


    @RequestMapping(value="/update/{idRequest}/{idOffer}", method = RequestMethod.GET)
    public String editSkillType(Model model, @PathVariable int idRequest, @PathVariable int idOffer) {
        model.addAttribute("collaboration", collaborationDao.getCollaboration(idRequest, idOffer));
        return "collaboration/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateAndSubmit(@ModelAttribute("collaboration") Collaboration collaboration,
                                         BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "collaboration/update";
        collaborationDao.updateCollaboration(collaboration);
        return "redirect:list";
    }

    @RequestMapping("/delete/{idRequest}/{idOffer}")
    public String processDelete(@PathVariable int id){
        collaborationDao.deleteCollaborationIdRequest(id);
        return "redirect:list";
    }


}