package es.uji.ei1027.skillsharing.model;

public class SkillType {
    private String name;
    private int level;
    private String description;

    public SkillType(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
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
