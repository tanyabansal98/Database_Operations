package mvccrud.dao;

import mvccrud.model.Course;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseDao {

    private final JdbcTemplate jdbc;

    public CourseDao(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // ── Shared RowMapper ──
    private Course mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        Course c = new Course();
        c.setCourseId(rs.getLong("Course_Id"));
        c.setCourseCode(rs.getString("Course_Code"));
        c.setCourseName(rs.getString("Course_Name"));
        c.setCredits(rs.getInt("Credits"));
        c.setInstructor(rs.getString("Instructor"));
        c.setDepartment(rs.getString("Department"));
        c.setCreatedAt(rs.getTimestamp("Created_At"));
        return c;
    }

    // ── List all courses ──
    public List<Course> findAll() {
        String sql = "SELECT Course_Id, Course_Code, Course_Name, Credits, " +
                "Instructor, Department, Created_At FROM Courses ORDER BY Course_Code ASC";
        return jdbc.query(sql, this::mapRow);
    }

    // ── Find one course by ID ──
    public Course findById(long courseId) {
        String sql = "SELECT Course_Id, Course_Code, Course_Name, Credits, " +
                "Instructor, Department, Created_At FROM Courses WHERE Course_Id = ?";
        List<Course> r = jdbc.query(sql, this::mapRow, courseId);
        return r.isEmpty() ? null : r.get(0);
    }

    // ── Find by course code (for duplicate checks) ──
    public Course findByCode(String courseCode) {
        String sql = "SELECT Course_Id, Course_Code, Course_Name, Credits, " +
                "Instructor, Department, Created_At FROM Courses WHERE Course_Code = ?";
        List<Course> r = jdbc.query(sql, this::mapRow, courseCode);
        return r.isEmpty() ? null : r.get(0);
    }

    // ── Insert a new course (accepts Course object) ──
    public void create(Course course) {
        String sql = "INSERT INTO Courses (Course_Code, Course_Name, Credits, Instructor, Department) " +
                "VALUES (?, ?, ?, ?, ?)";
        jdbc.update(sql,
                course.getCourseCode().toUpperCase(),
                course.getCourseName(),
                course.getCredits(),
                course.getInstructor(),
                course.getDepartment());
    }

    // ── Update a course (accepts Course object, returns rows affected) ──
    public int update(Course course) {
        String sql = "UPDATE Courses SET Course_Code = ?, Course_Name = ?, Credits = ?, " +
                "Instructor = ?, Department = ? WHERE Course_Id = ?";
        return jdbc.update(sql,
                course.getCourseCode().toUpperCase(),
                course.getCourseName(),
                course.getCredits(),
                course.getInstructor(),
                course.getDepartment(),
                course.getCourseId());
    }

    // ── Delete a course (returns rows affected) ──
    public int delete(long courseId) {
        String sql = "DELETE FROM Courses WHERE Course_Id = ?";
        return jdbc.update(sql, courseId);
    }
}
