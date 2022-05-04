package es.uji.ei1027.skillsharing.model;

import es.uji.ei1027.skillsharing.Level;

public class SkillType {
    private String name;
    private Level level;
    private String description;

    public SkillType(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "SkillType{" +
                "name='" + name + '\'' +
                ", level=" + level +
                ", description='" + description + '\'' +
                '}';
    }
}
