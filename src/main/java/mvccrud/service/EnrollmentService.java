package mvccrud.service;

import mvccrud.dao.CourseDao;
import mvccrud.dao.EnrollmentDao;
import mvccrud.dao.StudentDao;
import mvccrud.model.Enrollment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentDao enrollmentDao;
    private final StudentDao studentDao;
    private final CourseDao courseDao;

    public EnrollmentService(EnrollmentDao enrollmentDao,
            StudentDao studentDao, CourseDao courseDao) {
        this.enrollmentDao = enrollmentDao;
        this.studentDao = studentDao;
        this.courseDao = courseDao;
    }

    // ── Get all enrollments for a student ──
    public List<Enrollment> getEnrollmentsForStudent(long studentId) {
        if (studentDao.findById(studentId) == null)
            throw new IllegalArgumentException("Student " + studentId + " not found.");
        return enrollmentDao.findByStudentId(studentId);
    }

    // ── Enroll ──
    public Enrollment enroll(long studentId, long courseId) {
        if (studentDao.findById(studentId) == null)
            throw new IllegalArgumentException("Student " + studentId + " not found.");
        if (courseDao.findById(courseId) == null)
            throw new IllegalArgumentException("Course " + courseId + " not found.");
        if (enrollmentDao.isEnrolled(studentId, courseId))
            throw new IllegalArgumentException("Student is already enrolled in this course.");

        enrollmentDao.enroll(studentId, courseId);

        // Return the enrollment with join data
        List<Enrollment> enrollments = enrollmentDao.findByStudentId(studentId);
        return enrollments.stream()
                .filter(e -> e.getCourseId() == courseId)
                .findFirst()
                .orElse(null);
    }

    // ── Drop ──
    public void drop(long studentId, long courseId) {
        int rows = enrollmentDao.drop(studentId, courseId);
        if (rows == 0)
            throw new IllegalArgumentException("Enrollment not found.");
    }
}
