package es.uji.ei1027.skillsharing.controller;

import es.uji.ei1027.skillsharing.dao.CollaborationDao;
import es.uji.ei1027.skillsharing.model.Collaboration;
import es.uji.ei1027.skillsharing.model.Offer;
import es.uji.ei1027.skillsharing.model.Request;
import es.uji.ei1027.skillsharing.model.Student;
import es.uji.ei1027.skillsharing.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import es.uji.ei1027.skillsharing.services.CollabService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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


        // Agruparemos requests por request/offer nuestra que las haya originado
        Map<String, List<List<Offer>>> offersCollaboratingWithMyRequest;
        offersCollaboratingWithMyRequest = collabService.getOffersCollaboratingWithMyRequest(
                ((Student)session.getAttribute("student")).getDni());

        model.addAttribute("userRequestedCollaborations", offersCollaboratingWithMyRequest);

        // Map with user's names from the related offers
        Map<String, String> studentsNamesFromOffersMap = offersCollaboratingWithMyRequest.values().stream()
                .flatMap(Collection::stream).flatMap(Collection::stream)
                .collect(Collectors.toMap(Offer::getStudent,
                        offer -> collabService.getStudent(offer.getStudent()).getName(),
                        (student1, student2) -> student1));

        model.addAttribute("studentNamesFromOffersMap", studentsNamesFromOffersMap);



        Map<String, List<List<Request>>> requestsCollaboratingWithMyOffer;
        requestsCollaboratingWithMyOffer = collabService.getRequestsCollaboratingWithMyOffers(
                ((Student) session.getAttribute("student")).getDni());

        model.addAttribute("userOfferedCollaborations", requestsCollaboratingWithMyOffer);

        // Map with user's names from the related requests
        Map<String, String> studentsNamesFromRequestsMap = requestsCollaboratingWithMyOffer.values().stream()
                .flatMap(Collection::stream).flatMap(Collection::stream)
                .collect(Collectors.toMap(Request::getStudent,
                        request -> collabService.getStudent(request.getStudent()).getName(),
                        (student1, student2) -> student1));

        model.addAttribute("studentNamesFromRequestsMap", studentsNamesFromRequestsMap);


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
        String senderName = ((Student) session.getAttribute("student")).getName();
        String senderDNI = ((Student) session.getAttribute("student")).getDni();
        String activityName = (String) session.getAttribute("activityName");


        emailService.enviarConGMail(receiver,
                ""+senderName+" has applied to your activity '"+activityName+"'",
                "The user '"+senderName+"' has applied to your activity '"+activityName+
                        "' sugesting to start on "+collaboration.getStartDate()+" and ending on "+
                        collaboration.getEndDate()+
                        ".\n\nIf you want to accept the collaboration click on the following link: "+
                        "http://localhost:8080/collaboration/accept/"+collaboration.getIdRequest()+
                        "/"+collaboration.getIdOffer()+"/"+senderDNI+"/"+collaboration.getStartDate()+
                        "/"+collaboration.getEndDate()+"\n\n\tThe Skill Sharing Team.");
        //collaborationDao.addCollaboration(collaboration);
        return "redirect:list";
    }

    @RequestMapping("/accept/{id_request}/{id_offer}/{dni_solicitante}/{start_date}/{end_date}")
    public String acceptCollaboration(Model model, HttpSession session, HttpServletRequest httpRequest,
                                      @PathVariable int id_request, @PathVariable int id_offer,
                                      @PathVariable String dni_solicitante,
                                      @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start_date,
                                      @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end_date){

        if(session.getAttribute("student") == null) {
            session.setAttribute("nextUrl", httpRequest.getRequestURI());
            return "redirect:/login";
        }

        // Creamos la request/offer del solicitante ya que hasta ahora no se ha hecho
        if (collabService.getRequest(id_request) == null){
                if (!((Student)session.getAttribute("student")).getDni().equals(collabService.getOffer(id_offer).getStudent())){
                    session.setAttribute("nextUrl", httpRequest.getRequestURI());
                    return "redirect:/login";
                }

                collabService.addRequest(id_request, dni_solicitante, start_date, end_date);
        } else if (collabService.getOffer(id_offer) == null){
            if (!((Student)session.getAttribute("student")).getDni().equals(collabService.getRequest(id_request).getStudent())){
                session.setAttribute("nextUrl", httpRequest.getRequestURI());
                return "redirect:/login";
            }

            collabService.addOffer(id_offer, dni_solicitante, start_date, end_date);
        }

        // Creamos y guardamos la colaboración
        Collaboration collaboration = new Collaboration();
        collaboration.setIdOffer(id_offer);
        collaboration.setIdRequest(id_request);
        collaboration.setStartDate(start_date);
        collaboration.setEndDate(end_date);

        collaborationDao.addCollaboration(collaboration);

        return "redirect:/collaboration/list";
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
        Collaboration colab = collaborationDao.getCollaboration(idRequest,idOffer);
        collaborationDao.finishCollaboration(colab);
        return "redirect:/collaboration/list";
    }

    @RequestMapping(value= "/valorar/{idRequest}/{idOffer}", method= RequestMethod.GET)
    public String processvalorar(Model model, @PathVariable int idRequest, @PathVariable int idOffer,HttpSession session){
        if(session.getAttribute("student") == null) {
            session.setAttribute("nextUrl", "/collaboration/list");
            return "redirect:/login";
        }
        Collaboration colab= collaborationDao.getCollaboration(idRequest,idOffer);
        model.addAttribute("collaboration",colab);
        System.out.println("primero" + colab);
        return "collaboration/valorar";
    }

    @RequestMapping(value = "/valorar",method=RequestMethod.POST)
    public String processValorar(Model model, @ModelAttribute("collaboration") Collaboration collaboration){
        collabService.setHorasColab(collaboration, collaboration.getHoras());
        collaborationDao.updateCollaboration(collaboration);

        return "redirect:list";
    }

    @RequestMapping("/manage")
    public String manageCollaborations(Model model, HttpSession session){
        if(session.getAttribute("student") == null) {
            session.setAttribute("nextUrl", "/collaboration/manage");
            return "redirect:/login";
        }

        model.addAttribute("hoy", LocalDate.now());
        List<Collaboration> colabs = collaborationDao.getCollaborations();
        model.addAttribute("colabInfo", collabService.getCollabsInfo(colabs));
        model.addAttribute("collaborations", colabs);
        return "collaboration/manage";
    }



}