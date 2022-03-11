package es.uji.ei1027.skillsharing.services;

import es.uji.ei1027.skillsharing.dao.CollaborationDao;
import es.uji.ei1027.skillsharing.dao.OfferDao;
import es.uji.ei1027.skillsharing.dao.RequestDao;
import es.uji.ei1027.skillsharing.model.Collaboration;
import es.uji.ei1027.skillsharing.model.Offer;
import es.uji.ei1027.skillsharing.model.Request;
import es.uji.ei1027.skillsharing.model.Student;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BlockStudentService {

    @Autowired
    RequestDao requestDao;

    @Autowired
    OfferDao offerDao;

    @Autowired
    CollaborationDao collaborationDao;

    public void blockStudentActions(Student student){

        List<Request> lRequest = new ArrayList<>();
        List<Offer> lOffer = new ArrayList<>();

        // 1. First modify the date of all student requests
        for (Request request:
                requestDao.getRequests()) {
            if (request.getStudent().equals(student.getDni())){

                // Modify the request
                request.setEndDate(LocalDate.MIN);

                // Update the request
                requestDao.updateRequest(request);

                // Store the request
                lRequest.add(request);
            }
        }

        // 2. Then modify the date of all student offers
        for (Offer offer:
                offerDao.getOffers()) {
            if (offer.getStudent().equals(student.getDni())){

                // Modify the offer
                offer.setEndDate(LocalDate.MIN);

                // Update the offer
                offerDao.updateOffer(offer);

                // Store the offer
                lOffer.add(offer);
            }
        }

        // 3. Then modify the date of all student collaborations
        for (Collaboration collaboration :
                collaborationDao.getCollaborations()) {

            // Modify the date of the collaboration if the bloqued student apears on the request or in the offer
            if (lOffer.contains(offerDao.getOffer(collaboration.getIdOffer())) || lRequest.contains(requestDao.getRequest(collaboration.getIdRequest())))
                collaboration.setEndDate(LocalDate.MIN);
        }
    }
}
