package es.uji.ei1027.skillsharing.dao;

import es.uji.ei1027.skillsharing.model.SkillType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

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
}
