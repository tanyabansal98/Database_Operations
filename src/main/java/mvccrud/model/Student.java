package mvccrud;

import java.sql.Timestamp;

public class Student {
    private long studentId;
    private String fullName;
    private String email;
    private Timestamp createdAt;

    //Empty Constructor
    public Student() {};

    //Parameterized Constructor
    public Student(long studentId, String fullName, String email, Timestamp createdAt) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.email = email;
        this.createdAt = createdAt;
    }

    //Getters and Setters

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public String getFullName() {
        return fullName;
    }


    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
