package es.uji.ei1027.skillsharing.dao;

import es.uji.ei1027.skillsharing.model.Offer;
import es.uji.ei1027.skillsharing.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OfferDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addOffer(Offer offer, Student student){
        jdbcTemplate.update("INSERT INTO Offer VALUES(?, ?, ?, ?, ?, ?, ?, cast(? as level))",
                offer.getId(), offer.getName(), offer.getDescription(), offer.getStartDate(),
                offer.getEndDate(), student.getDni(), offer.getSkillName(), offer.getSkillLevel().toString());
    }

    public void deleteOffer(Offer offer){
        jdbcTemplate.update("DELETE FROM Offer WHERE id = ?", offer.getId());
    }

    public void deleteOffer(int offerId){
        jdbcTemplate.update("DELETE FROM Offer WHERE id = ?", offerId);
    }

    public void updateOffer(Offer offer){
        jdbcTemplate.update("UPDATE Offer SET name = ?, description = ?, start_date = ?, " +
                "end_date = ?, skill_name = ?, skill_level = cast(? as level) WHERE id = ?",
                offer.getName(), offer.getDescription(), offer.getStartDate(), offer.getEndDate(),
                offer.getSkillName(), offer.getSkillLevel().toString(), offer.getId());
    }

    public Offer getOffer(int offer){
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM Offer WHERE id = ?", new OfferRowMapper(), offer);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public List<Offer> getOffers(){
        try{
            return jdbcTemplate.query("SELECT * FROM Offer", new OfferRowMapper());
        }catch (EmptyResultDataAccessException e){
            return new ArrayList<Offer>();
        }
    }
}
