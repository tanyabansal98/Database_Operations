package mvccrud.service;

import mvccrud.dao.CourseDao;
import mvccrud.model.Course;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseDao dao;

    public CourseService(CourseDao dao) {
        this.dao = dao;
    }

    // ── List all courses (cached 10 min) ──
    @Cacheable("courses")
    public List<Course> getAllCourses() {
        return dao.findAll();
    }

    // ── Get course by ID (cached 15 min) ──
    @Cacheable(value = "course", key = "#id")
    public Course getCourseById(long id) {
        Course c = dao.findById(id);
        if (c == null)
            throw new CourseNotFoundException("Course " + id + " not found.");
        return c;
    }

    // ── Create (accepts Course object) ──
    @CacheEvict(value = "courses", allEntries = true)
    public Course createCourse(Course course) {
        if (ServiceHelper.isBlank(course.getCourseCode()))
            throw new IllegalArgumentException("Course Code is required.");
        if (ServiceHelper.isBlank(course.getCourseName()))
            throw new IllegalArgumentException("Course Name is required.");
        if (course.getCredits() < 1 || course.getCredits() > 6)
            throw new IllegalArgumentException("Credits must be 1–6.");

        // Sanitize
        course.setCourseCode(course.getCourseCode().trim().toUpperCase());
        course.setCourseName(course.getCourseName().trim());
        if (course.getInstructor() != null)
            course.setInstructor(course.getInstructor().trim());
        if (course.getDepartment() != null)
            course.setDepartment(course.getDepartment().trim());

        // Duplicate check
        Course existing = dao.findByCode(course.getCourseCode());
        if (existing != null)
            throw new IllegalArgumentException("Course code '" + course.getCourseCode() + "' already exists.");

        dao.create(course);

        // Return the newly created course
        return dao.findByCode(course.getCourseCode());
    }

    // ── Update (accepts Course object) ──
    @CacheEvict(value = { "courses", "course" }, allEntries = true)
    public Course updateCourse(long id, Course course) {
        if (ServiceHelper.isBlank(course.getCourseCode()))
            throw new IllegalArgumentException("Course Code is required.");
        if (ServiceHelper.isBlank(course.getCourseName()))
            throw new IllegalArgumentException("Course Name is required.");

        // Sanitize
        course.setCourseCode(course.getCourseCode().trim().toUpperCase());
        course.setCourseName(course.getCourseName().trim());

        // Duplicate check (allow same ID to keep its code)
        Course existing = dao.findByCode(course.getCourseCode());
        if (existing != null && existing.getCourseId() != id)
            throw new IllegalArgumentException("Course code '" + course.getCourseCode() + "' is already taken.");

        // Set the ID on the course object and persist
        course.setCourseId(id);
        int rows = dao.update(course);
        if (rows == 0)
            throw new CourseNotFoundException("Course " + id + " not found.");

        return dao.findById(id);
    }

    // ── Delete ──
    @CacheEvict(value = { "courses", "course" }, allEntries = true)
    public void deleteCourse(long id) {
        int rows = dao.delete(id);
        if (rows == 0)
            throw new CourseNotFoundException("Course " + id + " not found.");
    }

    public static class CourseNotFoundException extends RuntimeException {
        public CourseNotFoundException(String message) {
            super(message);
        }
    }
}
