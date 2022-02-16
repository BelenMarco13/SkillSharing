package es.uji.ei1027.skillsharing.dao;

import es.uji.ei1027.skillsharing.model.SkillType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SkillTypeDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //Add the skill to BBDD
    public void addSkillType(SkillType skillType) {
        jdbcTemplate.update("INSERT INTO Skill_type VALUES(?, ?, ?)",
                skillType.getName(), skillType.getLevel(), skillType.getDescription());
    }

    //Deletes the skill from BBDD
    public void deleteSkill(SkillType skillType) {
        jdbcTemplate.update("DELETE FROM skill_type WHERE name=? and level=?",
                skillType.getName(), skillType.getLevel());
    }

    //Deletes the skill from BBDD with name and level
    public void deleteSkillKey(String name, int level) {
        jdbcTemplate.update("DELETE FROM skill_type WHERE name=? and level=?", name, level);
    }

    //Updates the skills
    public void updateSkillType(SkillType skillType) {
        jdbcTemplate.update("UPDATE skill_type SET description=? WHERE name=?, level=?",
                skillType.getDescription(), skillType.getName(), skillType.getLevel());
    }

    //Returns the skill with the given key
    public SkillType getSkillType(String name, int level) {
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM skill_type WHERE name=?, level=?",
                    new SkillTypeRowMapper(), name, level);
        }catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    //Returns all the skills from the BBDD
    public List<SkillType> getSkills() {
        try {
            return jdbcTemplate.query("SELECT * FROM skill_type", new SkillTypeRowMapper());
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<SkillType>();
        }
    }
}
