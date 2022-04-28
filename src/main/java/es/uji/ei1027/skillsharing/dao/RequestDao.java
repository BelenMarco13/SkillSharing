package es.uji.ei1027.skillsharing.dao;

import es.uji.ei1027.skillsharing.Level;
import es.uji.ei1027.skillsharing.SkillsharingApplication;
import es.uji.ei1027.skillsharing.model.Offer;
import es.uji.ei1027.skillsharing.model.Request;
import es.uji.ei1027.skillsharing.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
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

    public void addRequest(Request request, Student student){
        jdbcTemplate.update("INSERT INTO Request VALUES(?, ?, ?, ?, ?, ?, ?, cast(? as Level))",
                request.getId(), request.getName(), request.getDescription(), request.getStartDate(),
                request.getEndDate(), student.getDni(), request.getSkillTypeLevel().split(" ")[0],
                request.getSkillTypeLevel().split(" ")[1]);
    }

    public void deleteRequest(int id){
        jdbcTemplate.update("DELETE FROM Request WHERE id = ?", id);
    }

    public void endRequest(int requestId){
        jdbcTemplate.update("UPDATE Request SET end_date = ? WHERE id = ?", LocalDate.now(), requestId);
    }

    public void updateRequest(Request request){
        log.info(request.toString());
        jdbcTemplate.update("UPDATE Request SET name = ?, description = ?, start_date = ?, " +
                        "end_date = ?, skill_name = ?, skill_level = cast(? as Level) WHERE id = ?",
                request.getName(), request.getDescription(), request.getStartDate(), request.getEndDate(),
                request.getSkillTypeLevel().split(" ")[0],
                request.getSkillTypeLevel().split(" ")[1], request.getId());
    }

    public Request getRequest(int id){
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM Request WHERE id = ?", new RequestRowMapper(), id);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }
    public List<Request> getRequests(Student student){
        try{
            return jdbcTemplate.query("SELECT * FROM Request WHERE student = ?", new RequestRowMapper(), student.getDni());
        }catch (EmptyResultDataAccessException e) {
            return new ArrayList<Request>();
        }catch(NullPointerException ex){
            return new ArrayList<Request>();

        }
    }

    public List<Request> getRequests(){
        try{
            return jdbcTemplate.query("SELECT * FROM Request", new RequestRowMapper());
        }catch (EmptyResultDataAccessException e){
            return new ArrayList<Request>();
        }
    }
    public List<Request> getRequests(String skillName, Level skillLevel){
        try{
            return jdbcTemplate.query("SELECT * FROM Request" +
                                             "WHERE skillName = ? AND skillLevel = ? ", new RequestRowMapper(),skillName,skillLevel);
        }catch (EmptyResultDataAccessException e){
            return new ArrayList<Request>();
        }
    }
}
