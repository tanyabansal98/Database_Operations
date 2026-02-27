package mvccrud.model;

import java.io.Serializable;
import java.sql.Timestamp;

// Serializable required for Redis caching
public class Course implements Serializable {

    private static final long serialVersionUID = 2L;

    private long courseId;
    private String courseCode;
    private String courseName;
    private int credits;
    private String instructor;
    private String department;
    private Timestamp createdAt;

    // ── Constructors ──
    public Course() {
    }

    public Course(long courseId, String courseCode, String courseName,
            int credits, String instructor, String department, Timestamp createdAt) {
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.instructor = instructor;
        this.department = department;
        this.createdAt = createdAt;
    }

    // ── Getters & Setters ──
    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long id) {
        this.courseId = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String code) {
        this.courseCode = code;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String name) {
        this.courseName = name;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String inst) {
        this.instructor = inst;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String dept) {
        this.department = dept;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp ts) {
        this.createdAt = ts;
    }
}
