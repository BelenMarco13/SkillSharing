package es.uji.ei1027.skillsharing.dao;

import es.uji.ei1027.skillsharing.model.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OfferDao {
    private JdbcTemplate jdbcTemplate;

    // Obte el jdbcTemplate a partir del Data Source
    @Autowired
    public void setDataSource(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // Afegeix la prova a la base de dades
    public void addOffer(Offer offer){
        jdbcTemplate.update("INSERT INTO Offer VALUES(?, ?, ?, ?, ?, ?, ?, ?, cast(? as type))",
                offer.getId(), offer.getName(), offer.getDescription(), offer.getStartDate(),
                offer.getEndDate(), offer.getStudent(), offer.getTypeName(), offer.getTypeLevel().toString());
    }

    public void deleteOffer(Offer offer){
        jdbcTemplate.update("DELETE FROM Offer WHERE id = ?", offer.getId());
    }

    public void updateOffer(Offer offer){
        jdbcTemplate.update("UPDATE Offer SET name = ?, description = ?, start_date = ?, " +
                "end_date = ?, student = ?, type_name = ?, type_level = cast(? as type) WHERE id = ?",
                offer.getName(), offer.getDescription(), offer.getStartDate(), offer.getEndDate(),
                offer.getStudent(), offer.getTypeName(), offer.getTypeLevel().toString(), offer.getId());
    }

    public Offer getOffer(String offer){
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
