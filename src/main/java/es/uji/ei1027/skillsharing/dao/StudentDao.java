package es.uji.ei1027.skillsharing.dao;

import es.uji.ei1027.skillsharing.Gender;
import es.uji.ei1027.skillsharing.model.Student;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class StudentDao {

    private JdbcTemplate jdbcTemplate;
    final Map<String, Student> knownStudents = new HashMap<String, Student>();

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //Add the student to BBDD
    public void addStudent(Student student) {
        jdbcTemplate.update("INSERT INTO student VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, cast(? as gender),?)",
                student.getDni(), student.getName(), student.getEmail(), student.getUserName(),
                student.getPwd(), student.getDegree(), student.getCourse(), student.getBalanceHours(),
                student.getSkp(), student.getAddress(), student.getAge(), student.getGender().toString(), false);
        knownStudents.put(student.getDni(),student);
    }

    //Deletes the student from BBDD
    public void deleteStudent(Student student) {
        jdbcTemplate.update("DELETE FROM student WHERE dni=?", student.getDni());
    }

    //Deletes the student from BBDD with dni
    public void deleteStudentDni(String dni) {
        jdbcTemplate.update("DELETE FROM student WHERE dni=?", dni);
    }

    //Updates the student
    public void updateStudent(Student student) {
        jdbcTemplate.update("UPDATE student SET name=?, email=?, user_name=?, pwd=?," +
                        "degree=?, course=?, balance_hours=?, skp=?, address=?, age=?, gender=cast(? as gender) WHERE dni=?",
                student.getName(), student.getEmail(), student.getUserName(), student.getPwd(),
                student.getDegree(), student.getCourse(), student.getBalanceHours(),
                student.getSkp(), student.getAddress(), student.getAge(), student.getGender().toString(), student.getDni());
    }

    //Block student
    public void block(Student student){
        jdbcTemplate.update("UPDATE student SET blocked=? WHERE dni=?", true, student.getDni());
    }

    //Unblock student
    public void unblock(Student student){
        jdbcTemplate.update("UPDATE student SET blocked=? WHERE dni=?", false, student.getDni());
    }

    //Returns the student with the given dni
    public Student getStudent(String dni) {
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM student WHERE dni=?",
                    new StudentRowMapper(), dni);
        }catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    //Returns all the students from the BBDD
    public List<Student> getStudents() {
        try {
            return jdbcTemplate.query("SELECT * FROM student", new StudentRowMapper());
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<Student>();
        }
    }

    public Student loadStudentByDni(String dni, String pwd){
        Student student = getStudent(dni);
        if(student == null){
            return null;
        }
        //BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        //if(passwordEncryptor.checkPassword(pwd, student.getPwd())){
        if(pwd.equals(student.getPwd())){
            return student;
        }else{
            return null;
        }
    }
}
