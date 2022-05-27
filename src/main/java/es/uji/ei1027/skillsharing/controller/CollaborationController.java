package es.uji.ei1027.skillsharing.controller;

import es.uji.ei1027.skillsharing.dao.CollaborationDao;
import es.uji.ei1027.skillsharing.model.Collaboration;
import es.uji.ei1027.skillsharing.model.Offer;
import es.uji.ei1027.skillsharing.model.Request;
import es.uji.ei1027.skillsharing.model.Student;
import es.uji.ei1027.skillsharing.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import es.uji.ei1027.skillsharing.services.CollabService;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

@Controller
@RequestMapping("/collaboration")
class CollaborationController {

    private CollaborationDao collaborationDao;
    @Autowired
    private CollabService collabService;

    @Autowired
    private EmailService emailService;

    @Autowired
    public void setCollaborationDao(CollaborationDao collaborationDao){
        this.collaborationDao = collaborationDao;
    }

    @RequestMapping("/list")
    public String listCollaborations(Model model, HttpSession session){
        if(session.getAttribute("student") == null) {
            session.setAttribute("nextUrl", "/collaboration/list");
            return "redirect:/login";
        }
        //buscar skillname y skill level para info !!


        List<Collaboration> colabs = collaborationDao.getCollaborations();
        model.addAttribute("colabInfo", collabService.getCollabsInfo(colabs));
        model.addAttribute("collaborations", colabs);
        return "collaboration/list";
    }

    @RequestMapping("/addprevio/{idreq}/{idof}")
    public String añadirColab(Model model, @PathVariable int idreq, @PathVariable int idof){
            Collaboration collaboration = collabService.addColab(idreq, idof);
            collaborationDao.addCollaboration(collaboration);
            return "redirect:/collaboration/list";

    }

    @RequestMapping("/add/{id_request}/{id_offer}")
    public String addCollaboration(Model model, HttpSession session,
                                   @PathVariable String id_request, @PathVariable String id_offer){

        if(session.getAttribute("student") == null) {
            session.setAttribute("nextUrl", "/collaboration/add/"+id_request+"/"+id_offer);
            return "redirect:/login";
        }

        Request request;
        Offer offer;
        Collaboration collaboration = new Collaboration();

        // 1. Obtener el último indice usado para representar una request/offer en la base de datos
        // y crear la colaboración asignando ese índice al elemento que falte para crearla (offer/request)
        if (id_request.equals("null")){
            collaboration.setIdOffer(Integer.parseInt(id_offer));
            collaboration.setIdRequest(collabService.getLastRequestId() + 1);
        } else if (id_offer.equals("null")){
            collaboration.setIdRequest(Integer.parseInt(id_request));
            collaboration.setIdOffer(collabService.getLastOfferId() + 1);
        }

        // 2. Redirigir al menú de all collaboration para que el usuario introduzca las fechas de incio y fin deseadas
        session.setAttribute("collaboration", collaboration);
        return "redirect:/collaboration/add";
    }

    @RequestMapping("/add")
    public String addCollaboration(Model model, HttpSession session){
        if(session.getAttribute("student") == null) {
            session.setAttribute("nextUrl", "/collaboration/add");
            return "redirect:/login";
        }

        Collaboration collaboration = (Collaboration) session.getAttribute("collaboration");
        Request request = collabService.getRequest(collaboration.getIdRequest());
        Offer offer = collabService.getOffer(collaboration.getIdOffer());

        if (request != null){
            model.addAttribute("request_name", request.getName());
            model.addAttribute("start_date", request.getStartDate());
            model.addAttribute("end_date", request.getEndDate());
            session.setAttribute("emailTo", collabService.getStudent(request.getStudent()).getEmail());
            session.setAttribute("activityName", request.getName());
            session.setAttribute("activityType", "request");
        } else if (offer != null) {
            model.addAttribute("offer_name", offer.getName());
            model.addAttribute("start_date", offer.getStartDate());
            model.addAttribute("end_date", offer.getEndDate());
            session.setAttribute("emailTo", collabService.getStudent(offer.getStudent()).getEmail());
            session.setAttribute("activityName", offer.getName());
            session.setAttribute("activityType", "offer");
        }

        model.addAttribute("collaboration", collaboration);

        return "collaboration/add";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("collaboration") Collaboration collaboration,
                                   HttpSession session,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "collaboration/add";

        System.out.println(collaboration);

        String receiver = (String)session.getAttribute("emailTo");
        String sender = ((Student) session.getAttribute("student")).getName();
        String activityName = (String) session.getAttribute("activityName");


        emailService.enviarConGMail(receiver,
                ""+sender+" has applied to your activity '"+activityName+"'",
                "If you want to accept the collaboration click in the following link: "+
                "localhost:8090/collaboration/accept/"+collaboration.getIdRequest()+"/"+
                collaboration.getIdOffer());
        //collaborationDao.addCollaboration(collaboration);
        return "redirect:list";
    }

    @RequestMapping("/info/{idRequest}/{idOffer}")
    public String informationCollab(Model model, @PathVariable int idRequest, @PathVariable int idOffer,HttpSession session) throws NullPointerException{
        if(session.getAttribute("student") == null) {
            session.setAttribute("nextUrl", "/collaboration/list");
            return "redirect:/login";
        }
        Collaboration colab = collaborationDao.getCollaboration(idRequest,idOffer);
        Request request = collabService.getRequest(idRequest);
        Offer offer = collabService.getOffer(idOffer);
        Student studentReq = collabService.getStudent(request.getStudent());
        Student studentOf = collabService.getStudent(offer.getStudent());
        model.addAttribute("request", request);
        model.addAttribute("offer",offer);
        model.addAttribute("studentReq", studentReq);
        model.addAttribute("studentOf", studentOf);
        model.addAttribute("collaboration", colab);
        model.addAttribute("hoy", LocalDate.now());
        return "/collaboration/info";
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
    public String processDelete(Model model, @PathVariable int idRequest, @PathVariable int idOffer){
        collaborationDao.finishCollaboration(idRequest,idOffer);
        return "redirect:collaboration/list";
    }

    @RequestMapping(value= "/valorar/{idRequest}/{idOffer}", method= RequestMethod.GET)
    public String processvalorar(Model model, @PathVariable int idRequest, @PathVariable int idOffer,HttpSession session){
        if(session.getAttribute("student") == null) {
            session.setAttribute("nextUrl", "/collaboration/list");
            return "redirect:/login";
        }
        Collaboration colab= collaborationDao.getCollaboration(idRequest,idOffer);

        model.addAttribute("collaboration",colab);
        System.out.println(colab);
        return "collaboration/valorar";
    }

    @RequestMapping(value = "/valorar",method=RequestMethod.POST)
    public String processValorar(Model model, @ModelAttribute("collaboration") Collaboration collaboration){
        collaborationDao.updateCollaboration(collaboration);
        System.out.println(collaboration);

        return "redirect:list";
    }



}