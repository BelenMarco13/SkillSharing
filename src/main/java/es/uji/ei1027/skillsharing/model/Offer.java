package es.uji.ei1027.skillsharing.model;

import es.uji.ei1027.skillsharing.Level;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class Offer {
    private int id;
    private String name;
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private String student;
    private String skillName;
    private Level skillLevel;
    private String skillTypeLevel;

    public Offer(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public Level getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(Level skillLevel) {
        this.skillLevel = skillLevel;
    }

    public String getSkillTypeLevel() { return skillTypeLevel; }

    public void setSkillTypeLevel(String skillTypeLevel) { this.skillTypeLevel = skillTypeLevel; }

    @Override
    public String toString() {
        return "Offer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", student='" + student + '\'' +
                ", skillName='" + skillName + '\'' +
                ", skillLevel=" + skillLevel +
                '}';
    }

}
