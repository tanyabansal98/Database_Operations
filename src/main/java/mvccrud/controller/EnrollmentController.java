package mvccrud.controller;

import mvccrud.model.Enrollment;
import mvccrud.service.CourseService;
import mvccrud.service.EnrollmentService;
import mvccrud.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final StudentService studentService;
    private final CourseService courseService;

    public EnrollmentController(EnrollmentService enrollmentService,
            StudentService studentService, CourseService courseService) {
        this.enrollmentService = enrollmentService;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    // GET /api/enrollments?studentId=42 → list a student's enrollments
    @GetMapping
    public ResponseEntity<?> listEnrollments(@RequestParam long studentId) {
        try {
            List<Enrollment> enrollments = enrollmentService.getEnrollmentsForStudent(studentId);
            return ResponseEntity.ok(enrollments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    // POST /api/enrollments → enroll { studentId, courseId }
    // Spring deserializes JSON into an Enrollment object automatically
    @PostMapping
    public ResponseEntity<?> enroll(@RequestBody Enrollment enrollment) {
        try {
            if (enrollment.getStudentId() == 0 || enrollment.getCourseId() == 0)
                return ResponseEntity.badRequest().body(Map.of("error", "studentId and courseId are required."));

            Enrollment created = enrollmentService.enroll(
                    enrollment.getStudentId(),
                    enrollment.getCourseId());
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    // DELETE /api/enrollments?studentId=42&courseId=7 → drop
    @DeleteMapping
    public ResponseEntity<?> drop(@RequestParam long studentId,
            @RequestParam long courseId) {
        try {
            enrollmentService.drop(studentId, courseId);
            return ResponseEntity.ok(Map.of("message", "Dropped successfully."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}
