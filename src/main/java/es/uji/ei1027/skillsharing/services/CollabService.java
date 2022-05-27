package es.uji.ei1027.skillsharing.services;

import es.uji.ei1027.skillsharing.Level;
import es.uji.ei1027.skillsharing.dao.*;
import es.uji.ei1027.skillsharing.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class CollabService {

    @Autowired
    OfferDao offerDao;

    @Autowired
    CollaborationDao collaborationDao;

    @Autowired
    StudentDao studentDao;

    @Autowired
    SkillTypeDao skillTypeDao;

    @Autowired
    RequestDao requestDao;

    String SkillNames;
    SkillType SkillTypes;

    Logger log;

    public Map<Collaboration, List<String>> getCollabsInfo(List<Collaboration> colabs) {
        Map<Collaboration, List<String>> mapa = new HashMap<>();
        for (Collaboration colab : colabs) {
            List<String> lista = new ArrayList<>();
            String nameReq = studentDao.getStudent(requestDao.getRequest(colab.getIdRequest()).getStudent()).getName();
            String nameOf = studentDao.getStudent(offerDao.getOffer(colab.getIdOffer()).getStudent()).getName();
            String SkillName = requestDao.getRequest(colab.getIdRequest()).getSkillName();
            String SkillLevel = String.valueOf(requestDao.getRequest(colab.getIdRequest()).getSkillLevel());
            lista.add(nameReq);
            lista.add(nameOf);
            lista.add(SkillName);
            lista.add(SkillLevel);
            mapa.put(colab, lista);


        }

        return mapa;
    }



    public Collaboration addColab(int idreq, int idof){
        Request request= requestDao.getRequest(idreq);
        Offer offer = offerDao.getOffer(idof);
        LocalDate startDate = offer.getStartDate();
        LocalDate endDate = request.getEndDate();

            sendEmail(request,offer);
            Collaboration colab = new Collaboration(idreq,idof,startDate,endDate);
            return colab;
        }

    public void sendEmail(Request req, Offer offer){

    }
    public boolean exists(int idreq, int idof){
        if( collaborationDao.getCollaboration(idreq,idof)!= null){
            return true;
        }else{
            return false;
        }
    }
    public List<Offer> getOptionsColabs(int req, Level skillLevel, String skillName ) {
        List<Offer> offers;
        List<Offer> optionsColab = new ArrayList<>();
        try {
            List<Collaboration> collabs = collaborationDao.getCollaborations(req);
            offers = offerDao.getOffers(skillName, skillLevel);

            System.out.println(offers);
            Request request = requestDao.getRequest(req);
            for (Offer offer : offers) {
                for (Collaboration colab : collabs) {
                    System.out.println("hay colabs");

                    String dniReq = request.getStudent();
                    String dniOf = offer.getStudent();
                    System.out.println(offer);
                    if (offer.getId() != colab.getIdOffer() && offer.getStudent() != request.getStudent()) {
                        optionsColab.add(offer);
                        System.out.println(optionsColab);

                    }
                }
                if(collabs.isEmpty()){
                    System.out.println("no hay colabs");
                    if(offer.getStudent() != request.getStudent()){
                        optionsColab.add(offer);
                    }
                }

            }

        } catch (Exception e) {
            offers = new ArrayList<>();
        }

        return optionsColab;

    }
    public List<Collaboration> getCollabsReq(int idreq){
        List<Collaboration> collabs = collaborationDao.getCollaborations(idreq);
        return collabs;
    }
    public Request getRequest(int idreq){
         return requestDao.getRequest(idreq);
    }
    public Offer getOffer(int idoffer){
        return offerDao.getOffer(idoffer);
    }
    public Student getStudent(String dni){
        return studentDao.getStudent(dni);
    }
    public String getSkillNames(int idreq){
        return requestDao.getRequest(idreq).getSkillName();
    }

    public Level getSkillLevel(int idreq){
        return requestDao.getRequest(idreq).getSkillLevel();
    }

    /**
     * Para poder crear una colaboración sin una request ya creada vamos a ponerle un id que no exista.
     * Para ello vamos a mirar en las request y colabs de la base de datos
     * @return último indice de request existente mirando tabla de request y de collabs
     */
    public int getLastRequestId() {

        // Miramos el último existente en la tabla de requests
        int ultimoIndice = requestDao.getRequests().get(requestDao.getRequests().size()-1).getId();

        // Miramos el último y aexistente en la tabla de colaboraciones
        List<Collaboration> collaborations= collaborationDao.getCollaborations();

        for (Collaboration collaboration :
                collaborations) {
            if (collaboration.getIdRequest() > ultimoIndice)
                ultimoIndice = collaboration.getIdRequest();
        }
        return ultimoIndice;
    }

    /**
     * Para poder crear una colaboración sin una offer ya creada vamos a ponerle un id que no exista.
     * Para ello vamos a mirar en las offers y colabs de la base de datos
     * @return último indice de offer existente mirando tabla de offers y de collabs
     */
    public int getLastOfferId() {

        // Miramos el último existente en la tabla de offers
        int ultimoIndice = offerDao.getOffers().get(offerDao.getOffers().size()-1).getId();

        // Miramos el último y aexistente en la tabla de colaboraciones
        List<Collaboration> collaborations= collaborationDao.getCollaborations();

        for (Collaboration collaboration :
                collaborations) {
            if (collaboration.getIdOffer() > ultimoIndice)
                ultimoIndice = collaboration.getIdOffer();
        }
        return ultimoIndice;
    }

    public void addRequest(int id_request, String dni_solicitante, LocalDate start_date, LocalDate end_date) {
        Request request = new Request();
        request.setName("Collab request");
        request.setDescription("Collab request");
        request.setId(id_request);
        request.setStartDate(start_date);
        request.setEndDate(end_date);
        request.setSkillTypeLevel(skillTypeDao.getSkills().get(0).getName()+" "+
                skillTypeDao.getSkills().get(0).getLevel());

        requestDao.addRequest(request, studentDao.getStudent(dni_solicitante));
    }

    public void addOffer(int id_offer, String dni_solicitante, LocalDate start_date, LocalDate end_date) {
        Offer offer = new Offer();
        offer.setName("Collab offer");
        offer.setDescription("Collab offer");
        offer.setId(id_offer);
        offer.setStartDate(start_date);
        offer.setEndDate(end_date);
        offer.setSkillTypeLevel(skillTypeDao.getSkills().get(0).getName()+" "+
                skillTypeDao.getSkills().get(0).getLevel());

        offerDao.addOffer(offer, studentDao.getStudent(dni_solicitante));
    }


}

