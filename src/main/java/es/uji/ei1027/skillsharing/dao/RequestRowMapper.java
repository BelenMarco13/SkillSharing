package es.uji.ei1027.skillsharing.dao;

import es.uji.ei1027.skillsharing.Level;
import es.uji.ei1027.skillsharing.SkillsharingApplication;
import es.uji.ei1027.skillsharing.model.Offer;
import es.uji.ei1027.skillsharing.model.Offer;
import es.uji.ei1027.skillsharing.model.Request;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class RequestRowMapper implements RowMapper<Request> {
    private static final Logger log =
            Logger.getLogger(SkillsharingApplication.class.getName());
    @Override
    public Request mapRow(ResultSet rs, int rowNum) throws SQLException {
        Request request = new Request();
        request.setId(rs.getInt("id"));
        request.setName(rs.getString("name"));
        request.setDescription(rs.getString("description"));


       /* DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String stringend = rs.getString("end_date");
        String stringstart = rs.getString("start_date");

        //convert String to LocalDate
        LocalDate start = LocalDate.parse(stringstart, formatter);
        LocalDate end = LocalDate.parse(stringend,formatter);
        request.setStartDate(start);
        request.setEndDate(end);*/
        request.setStartDate(rs.getObject("start_date",LocalDate.class));
        request.setEndDate(rs.getObject("end_date", LocalDate.class));

        request.setStudent(rs.getString("student"));
        request.setSkillName(rs.getString("type_name"));
        request.setSkillLevel(Level.valueOf(rs.getString("type_level")));
        return request;
    }
}
