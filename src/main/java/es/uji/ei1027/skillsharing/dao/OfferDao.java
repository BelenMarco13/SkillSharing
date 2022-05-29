package es.uji.ei1027.skillsharing.dao;

import es.uji.ei1027.skillsharing.Level;
import es.uji.ei1027.skillsharing.model.Offer;
import es.uji.ei1027.skillsharing.model.Request;
import es.uji.ei1027.skillsharing.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.websocket.Session;
import java.time.LocalDate;
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
        int id = getId();
        jdbcTemplate.update("INSERT INTO Offer VALUES(?, ?, ?, ?, ?, ?, ?, cast(? as level))",
                id, offer.getName(), offer.getDescription(), offer.getStartDate(),
                offer.getEndDate(), student.getDni(), offer.getSkillTypeLevel().split(" ")[0],
                offer.getSkillTypeLevel().split(" ")[1]);

    }

    public void deleteOffer(int offerId){
        jdbcTemplate.update("DELETE FROM Offer WHERE id = ?", offerId);
    }

    public void endOffer(int offerId){
        jdbcTemplate.update("UPDATE Offer SET end_date = ? WHERE id = ?", LocalDate.now(), offerId);
    }

    public void updateOffer(Offer offer){
        jdbcTemplate.update("UPDATE Offer SET name = ?, description = ?, start_date = ?, " +
                "end_date = ?, skill_name = ?, skill_level = cast(? as level) WHERE id = ?",
                offer.getName(), offer.getDescription(), offer.getStartDate(), offer.getEndDate(),
                offer.getSkillTypeLevel().split(" ")[0],
                offer.getSkillTypeLevel().split(" ")[1], offer.getId());
    }

    public Offer getOffer(int offer){
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM Offer WHERE id = ?", new OfferRowMapper(), offer);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public List<Offer> getOffers(Student student){
        try{
            return jdbcTemplate.query("SELECT * FROM Offer WHERE student = ?", new OfferRowMapper(), student.getDni());
        }catch (EmptyResultDataAccessException e) {
            return new ArrayList<Offer>();
        }catch(NullPointerException ex){
            return new ArrayList<Offer>();

        }
    }
    public List<Offer> getOffers(String skillName, Level skillLevel){
        try{
            return jdbcTemplate.query("SELECT * FROM Offer" +
                    " WHERE skill_name = ? AND skill_level = cast(? as Level) ", new OfferRowMapper(),skillName,skillLevel.toString());
        }catch (EmptyResultDataAccessException e){
            return new ArrayList<Offer>();
        }
    }

    public List<Offer> getOffers(){
        try{
            return jdbcTemplate.query("SELECT * FROM Offer", new OfferRowMapper());
        }catch (EmptyResultDataAccessException e){
            return new ArrayList<Offer>();
        }
    }

    public List<Offer> getOffersSkill(String skill){
        try{
            return jdbcTemplate.query("SELECT * FROM Offer WHERE skill_name = ?",
                    new OfferRowMapper(), skill);
        }catch (EmptyResultDataAccessException e){
            return new ArrayList<Offer>();
        }
    }


    public int getId(){
        int id = getOffers().size() +1;
        return id;

    }
}
