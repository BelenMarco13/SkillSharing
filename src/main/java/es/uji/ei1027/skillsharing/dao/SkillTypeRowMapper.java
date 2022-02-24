package es.uji.ei1027.skillsharing.dao;

import es.uji.ei1027.skillsharing.Level;
import es.uji.ei1027.skillsharing.model.SkillType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SkillTypeRowMapper implements RowMapper<SkillType> {
    public SkillType mapRow(ResultSet rs, int rowNum) throws SQLException {
        SkillType skillType = new SkillType();
        skillType.setName(rs.getString("name"));
        skillType.setLevel(Level.valueOf(rs.getString("level")));
        skillType.setDescription(rs.getString("description"));
        return skillType;
    }
}
