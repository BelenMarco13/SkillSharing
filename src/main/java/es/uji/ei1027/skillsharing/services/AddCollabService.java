package es.uji.ei1027.skillsharing.services;

import es.uji.ei1027.skillsharing.dao.CollaborationDao;
import es.uji.ei1027.skillsharing.dao.OfferDao;
import es.uji.ei1027.skillsharing.dao.RequestDao;
import es.uji.ei1027.skillsharing.dao.StudentDao;
import es.uji.ei1027.skillsharing.model.Collaboration;
import es.uji.ei1027.skillsharing.model.Offer;
import es.uji.ei1027.skillsharing.model.Request;
import es.uji.ei1027.skillsharing.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class AddCollabService {

    @Autowired
    OfferDao offerDao;

    @Autowired
    CollaborationDao collaborationDao;

    @Autowired
    StudentDao studentDao;

    @Autowired
    RequestDao requestDao;

    Logger log;

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
            log.info("Se envia un mensaje");
    }
}
