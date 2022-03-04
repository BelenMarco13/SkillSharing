package es.uji.ei1027.skillsharing.dao;

import es.uji.ei1027.skillsharing.model.Collaboration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import es.uji.ei1027.skillsharing.dao.CollaborationRowMapper;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CollaborationDao {
    private JdbcTemplate jdbcTemplate;

    // Obte el jdbcTemplate a partir del Data Source
    @Autowired
    public void setDataSource(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // Afegeix la prova a la base de dades
        public void addCollaboration(Collaboration collaboration){
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
        jdbcTemplate.update("UPDATE INTO Collaboration SET start_date=?, end_date=?, ?, comment=?" +
                        "WHERE id_request=? and id_offer=?",
                collaboration.getStartDate(), collaboration.getEndDate(), collaboration.getScore(),
                collaboration.getComment(), collaboration.getIdRequest(), collaboration.getIdOffer());
    }

    public Collaboration getCollaboration(int idRequest, int idOffer){
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM Collaboration WHERE id_request=? AND id_offer=?",
                    new CollaborationRowMapper(), idRequest, idOffer);
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

