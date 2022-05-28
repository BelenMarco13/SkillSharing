package es.uji.ei1027.skillsharing.dao;

import es.uji.ei1027.skillsharing.model.Collaboration;

import es.uji.ei1027.skillsharing.model.SkillType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CollaborationDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

        public void addCollaboration(Collaboration collaboration){
            System.out.println(collaboration);
            System.out.println(collaboration.getComment());
        jdbcTemplate.update("INSERT INTO Collaboration VALUES(?, ?, ?, ?, ?, ?)",
                collaboration.getIdRequest(), collaboration.getIdOffer(), collaboration.getStartDate(),
                collaboration.getEndDate(), collaboration.getScore(), collaboration.getComment());
    }

    public void deleteCollaboration(Collaboration collaboration){
        jdbcTemplate.update("DELETE FROM Collaboration WHERE id_request = ?", collaboration.getIdRequest());
    }

    public void deleteCollaborationIdRequest(int idRequest){
        jdbcTemplate.update("DELETE FROM Collaboration WHERE id_request = ?", idRequest);
    }

    public void updateCollaboration(Collaboration collaboration){
        jdbcTemplate.update("UPDATE Collaboration SET start_date=?, end_date=?, score=?, comment=?" +
                        "WHERE id_request=? and id_offer=?",
                collaboration.getStartDate(), collaboration.getEndDate(), collaboration.getScore(),
                collaboration.getComment(), collaboration.getIdRequest(), collaboration.getIdOffer());
    }
    public void finishCollaboration(int id_req, int id_off){
        jdbcTemplate.update("UPDATE  Collaboration SET end_date= ? " +
                        "WHERE id_request=? and id_offer=?", LocalDate.now(), id_req, id_off);


    }
    public void finishCollaboration(Collaboration collaboration){
        LocalDate start= collaboration.getStartDate();
        int id_req = collaboration.getIdRequest();
        int id_off = collaboration.getIdOffer();
        if(start.compareTo(LocalDate.now()) >=0){
            start=LocalDate.now().minusDays(1);
        }
        jdbcTemplate.update("UPDATE  Collaboration SET end_date= ?, start_date=?" +
                "WHERE id_request=? and id_offer=?", LocalDate.now(), start, id_req, id_off);


    }
    public Collaboration getCollaboration(int idRequest, int idOffer){
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM Collaboration WHERE id_request=? AND id_offer=?",
                    new CollaborationRowMapper(), idRequest, idOffer);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }
    public List<Collaboration> getCollaborations(int idRequest){
        try{
            return jdbcTemplate.query("SELECT * FROM Collaboration WHERE id_request=?",
                    new CollaborationRowMapper(), idRequest);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public List<Collaboration> getCollaborations(){
        try{
            return jdbcTemplate.query("SELECT * FROM Collaboration", new CollaborationRowMapper());
        }catch (EmptyResultDataAccessException e){
            return new ArrayList<Collaboration>();
        }
    }



}

