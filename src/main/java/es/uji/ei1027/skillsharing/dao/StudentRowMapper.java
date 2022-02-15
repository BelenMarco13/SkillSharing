package es.uji.ei1027.skillsharing.dao;

import es.uji.ei1027.skillsharing.model.Student;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentRowMapper implements RowMapper<Student> {
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        Student student = new Student();
        student.setDni(rs.getString("dni"));
        student.setName(rs.getString("name"));
        student.setEmail(rs.getString("email"));
        student.setUserName(rs.getString("user_name"));
        student.setPwd(rs.getString("pwd"));
        student.setDegree(rs.getString("degree"));
        student.setCourse(rs.getInt("course"));
        student.setBalanceHours(rs.getFloat("balance_hours"));
        student.setSkp(rs.getString("skp"));
        student.setAddress(rs.getString("address"));
        student.setAge(rs.getInt("age"));
        return student;
    }
}
