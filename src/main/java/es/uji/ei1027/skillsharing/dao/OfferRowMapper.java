package es.uji.ei1027.skillsharing.dao;

import es.uji.ei1027.skillsharing.Level;
import es.uji.ei1027.skillsharing.model.Offer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class OfferRowMapper implements RowMapper<Offer> {

    @Override
    public Offer mapRow(ResultSet rs, int rowNum) throws SQLException {
        Offer offer = new Offer();
        offer.setId(rs.getInt("id"));
        offer.setName(rs.getString("name"));
        offer.setDescription(rs.getString("description"));
        offer.setStartDate(rs.getObject("start_date", LocalDate.class));
        offer.setEndDate(rs.getObject("end_date", LocalDate.class));
        offer.setStudent(rs.getString("student"));
        offer.setSkillName(rs.getString("skill_name"));
        offer.setSkillLevel(Level.valueOf(rs.getString("skill_level")));
        return offer;
    }
}
