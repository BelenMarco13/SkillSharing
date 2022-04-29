package es.uji.ei1027.skillsharing.services;

import es.uji.ei1027.skillsharing.dao.CollaborationDao;
import es.uji.ei1027.skillsharing.dao.OfferDao;
import es.uji.ei1027.skillsharing.dao.RequestDao;
import es.uji.ei1027.skillsharing.dao.StudentDao;
import es.uji.ei1027.skillsharing.model.Collaboration;
import es.uji.ei1027.skillsharing.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetCollaborationsByStudentService {

    @Autowired
    RequestDao requestDao;

    @Autowired
    OfferDao offerDao;

    @Autowired
    CollaborationDao collaborationDao;

    public List<Collaboration> getCollaboraionsByStudent(Student student){

        List<Collaboration> result = new ArrayList();

        // 1. Get all collaborations with this student offers
        for (Collaboration collaboration : collaborationDao.getCollaborations()) {

            if (requestDao.getRequest(collaboration.getIdRequest()).getStudent().equals(student.getDni())
            || offerDao.getOffer(collaboration.getIdOffer()).getStudent().equals(student.getDni())){
                result.add(collaboration);
            }
        }

        return result;
    }
}
