package es.uji.ei1027.skillsharing.dao;


import es.uji.ei1027.skillsharing.Score;
import es.uji.ei1027.skillsharing.model.Collaboration;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CollaborationRowMapper implements RowMapper<Collaboration>{
    public Collaboration mapRow(ResultSet rs, int rowNum) throws java.sql.SQLException {
        Collaboration collaboration = new Collaboration();
        collaboration.setIdRequest(rs.getInt("id_request"));
        collaboration.setIdOffer(rs.getInt("id_offer"));
        collaboration.setStartDate(rs.getObject("start_date", java.time.LocalDate.class));
        collaboration.setEndDate(rs.getObject("end_date", java.time.LocalDate.class));
        collaboration.setScore(Score.valueOf(rs.getString("score")));
        collaboration.setComment(rs.getString("comment"));

        return collaboration;
    }
}





