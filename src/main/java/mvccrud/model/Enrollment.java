package mvccrud.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Enrollment implements Serializable {

    private static final long serialVersionUID = 3L;

    private long enrollmentId;
    private long studentId;
    private long courseId;
    private String grade;
    private Timestamp enrolledAt;

    // ── Joined fields (for display — populated via JOIN queries) ──
    private String studentName;
    private String courseName;
    private String courseCode;

    // ── Constructors ──
    public Enrollment() {
    }

    // ── Getters & Setters ──
    public long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(long id) {
        this.enrollmentId = id;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long id) {
        this.studentId = id;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long id) {
        this.courseId = id;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Timestamp getEnrolledAt() {
        return enrolledAt;
    }

    public void setEnrolledAt(Timestamp ts) {
        this.enrolledAt = ts;
    }

    // ── Joined display fields ──
    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String name) {
        this.studentName = name;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String name) {
        this.courseName = name;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String code) {
        this.courseCode = code;
    }
}
