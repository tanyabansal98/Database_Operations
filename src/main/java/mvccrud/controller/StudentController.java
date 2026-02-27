package mvccrud.controller;

import mvccrud.model.Student;
import mvccrud.service.StudentService;
import mvccrud.service.StudentService.StudentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    // GET /api/students → list all students
    @GetMapping
    public ResponseEntity<List<Student>> listStudents() {
        return ResponseEntity.ok(service.getAllStudents());
    }

    // GET /api/students/{id} → single student
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudent(@PathVariable long id) {
        try {
            return ResponseEntity.ok(service.getStudentById(id));
        } catch (StudentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // POST /api/students → create
    // Spring deserializes the JSON body into a Student object using @JsonProperty
    // mappings
    @PostMapping
    public ResponseEntity<?> createStudent(@RequestBody Student student) {
        try {
            service.createStudent(
                    student.getFull_Name(),
                    student.getEmail(),
                    student.getPhone(),
                    student.getDate_Of_Birth() != null ? student.getDate_Of_Birth().toString() : null,
                    student.getMajor(),
                    student.getStatus() != null ? student.getStatus() : "Active");
            // Return the created student (find by email since Oracle doesn't return
            // generated ID)
            Student created = service.getAllStudents().stream()
                    .filter(s -> student.getEmail() != null &&
                            student.getEmail().equalsIgnoreCase(s.getEmail()))
                    .findFirst()
                    .orElse(null);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    // PUT /api/students/{id} → update
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable long id,
            @RequestBody Student student) {
        try {
            service.updateStudent(id,
                    student.getFull_Name(),
                    student.getEmail(),
                    student.getPhone(),
                    student.getDate_Of_Birth() != null ? student.getDate_Of_Birth().toString() : null,
                    student.getMajor(),
                    student.getStatus());
            return ResponseEntity.ok(service.getStudentById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (StudentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    // DELETE /api/students/{id} → delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable long id) {
        try {
            service.deleteStudent(id);
            return ResponseEntity.ok(Map.of("message", "Student deleted successfully."));
        } catch (StudentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}
