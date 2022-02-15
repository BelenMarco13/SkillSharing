package es.uji.ei1027.skillsharing.model;

public class Student {
    private String dni;
    private String name;
    private String email;
    private String userName;
    private String pwd;
    private String degree;
    private String course;
    private Float balanceHours;
    private String skp;
    private String address;
    private int age;

    public Student() {
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Float getBalanceHours() {
        return balanceHours;
    }

    public void setBalanceHours(Float balanceHours) {
        this.balanceHours = balanceHours;
    }

    public String getSkp() {
        return skp;
    }

    public void setSkp(String skp) {
        this.skp = skp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" +
                "dni='" + dni + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", user_name='" + userName + '\'' +
                ", pwd='" + pwd + '\'' +
                ", degree='" + degree + '\'' +
                ", course='" + course + '\'' +
                ", balance_hours=" + balanceHours +
                ", skp='" + skp + '\'' +
                ", address='" + address + '\'' +
                ", age=" + age +
                '}';
    }
}
