package mvccrud.controller;

import mvccrud.model.Course;
import mvccrud.service.CourseService;
import mvccrud.service.CourseService.CourseNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService service;

    public CourseController(CourseService service) {
        this.service = service;
    }

    // GET /api/courses → list all
    @GetMapping
    public ResponseEntity<List<Course>> listCourses() {
        return ResponseEntity.ok(service.getAllCourses());
    }

    // GET /api/courses/{id} → single course
    @GetMapping("/{id}")
    public ResponseEntity<?> getCourse(@PathVariable long id) {
        try {
            return ResponseEntity.ok(service.getCourseById(id));
        } catch (CourseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    // POST /api/courses → create
    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody Course course) {
        try {
            Course created = service.createCourse(course);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    // PUT /api/courses/{id} → update
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable long id,
            @RequestBody Course course) {
        try {
            Course updated = service.updateCourse(id, course);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (CourseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    // DELETE /api/courses/{id} → delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable long id) {
        try {
            service.deleteCourse(id);
            return ResponseEntity.ok(Map.of("message", "Course deleted successfully."));
        } catch (CourseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}
