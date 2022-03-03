package es.uji.ei1027.skillsharing.dao;

import es.uji.ei1027.skillsharing.SkillsharingApplication;
import es.uji.ei1027.skillsharing.model.Offer;
import es.uji.ei1027.skillsharing.model.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


@Repository
public class RequestDao {
    private JdbcTemplate jdbcTemplate;
    private static final Logger log =
            Logger.getLogger(SkillsharingApplication.class.getName());
    @Autowired
    public void setDataSource(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // Afegeix la prova a la base de dades
    public void addRequest(Request request){
        jdbcTemplate.update("INSERT INTO Request VALUES(?, ?, ?, ?, ?, ?, ?, cast(? as Level))",
                request.getId(), request.getName(), request.getDescription(), request.getStartDate(),
                request.getEndDate(), request.getStudent(), request.getSkillName(), request.getSkillLevel().toString());
    }

    public void deleteRequest(int id){
        jdbcTemplate.update("DELETE FROM Request WHERE id = ?", id);
    }

    public void updateRequest(Request request){
        log.info("updateeeee:");
        log.info(request.toString());
        jdbcTemplate.update("UPDATE Request SET name = ?, description = ?, start_date = ?, " +
                        "end_date = ?, student = ?, type_name = ?, type_level = cast(? as Level) WHERE id = ?",
                request.getName(), request.getDescription(), request.getStartDate(), request.getEndDate(),
                request.getStudent(), request.getSkillName(), request.getSkillLevel().toString(), request.getId());
    }

    public Request getRequest(int id){
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM Request WHERE id = ?", new RequestRowMapper(), id);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public List<Request> getRequests(){
        try{
            return jdbcTemplate.query("SELECT * FROM Request", new RequestRowMapper());
        }catch (EmptyResultDataAccessException e){
            return new ArrayList<Request>();
        }
    }
}
