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
        jdbcTemplate.update("INSERT INTO skill_type VALUES(?, cast(? as Level), ?)",
                skillType.getName(), skillType.getLevel().toString(), skillType.getDescription());
    }

    //Deletes the skill from BBDD
    public void deleteSkill(SkillType skillType) {
        jdbcTemplate.update("DELETE FROM skill_type WHERE name=? and level=cast(? as level)",
                skillType.getName(), skillType.getLevel().toString());
    }

    //Deletes the skill from BBDD with name and level
    public void deleteSkillKey(String name, String level) {
        jdbcTemplate.update("DELETE FROM skill_type WHERE name=? and level=cast(? as level)", name, level);
    }

    //Updates the skills
    public void updateSkillType(SkillType skillType) {
        jdbcTemplate.update("UPDATE skill_type SET description=? WHERE name=? and level=cast(? as level)",
                skillType.getDescription(), skillType.getName(), skillType.getLevel().toString());
    }

    //Returns the skill with the given key
    public SkillType getSkillType(String name, String level) {
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM skill_type WHERE name=? and level=cast(? as level)",
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
