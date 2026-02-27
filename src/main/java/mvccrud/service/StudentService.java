package mvccrud.service;

import mvccrud.dao.StudentDao;
import mvccrud.model.Student;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService extends BaseService { // ← INHERITANCE: extends BaseService

    private final StudentDao dao;

    public StudentService(StudentDao dao) {
        this.dao = dao;
    }

    // ═══════════════════════════════════════════════
    // OVERRIDING — @Override means we're replacing
    // the parent BaseService.validate() with our
    // own student-specific rules.
    // ═══════════════════════════════════════════════
    @Override
    public String validate(String input) {
        // First apply the parent's generic null/blank check
        String baseError = super.validate(input); // ← calls BaseService.validate()
        if (baseError != null)
            return baseError;

        // Then add Student-specific rules on top
        if (input.trim().length() < 2) {
            return "Name must be at least 2 characters.";
        }
        if (input.trim().length() > 100) {
            return "Name must not exceed 100 characters.";
        }
        if (!input.trim().matches("[a-zA-Z\\s'-]+")) {
            return "Name must contain only letters, spaces, hyphens, or apostrophes.";
        }
        return null; // null = valid
    }

    // ── List all students ──
    @Cacheable("students")
    public List<Student> getAllStudents() {
        return dao.findAll();
    }

    // ── Get student by ID ──
    @Cacheable(value = "student", key = "#id")
    public Student getStudentById(long id) {
        Student s = dao.findById(id);
        if (s == null) {
            throw new StudentNotFoundException("Student with ID " + id + " not found.");
        }
        return s;
    }

    // ── Create a student (accepts Student object) ──
    @CacheEvict(value = { "students", "student" }, allEntries = true)
    public Student createStudent(Student student) {
        // 1. Validate name
        String nameError = validate(student.getFull_Name());
        if (nameError != null) {
            throw new IllegalArgumentException(nameError);
        }

        // 2. Sanitize name
        student.setFull_Name(ServiceHelper.capitalizeName(student.getFull_Name().trim()));

        // 3. Validate & normalize email
        if (!ServiceHelper.isBlank(student.getEmail())) {
            student.setEmail(student.getEmail().trim().toLowerCase());
            if (!ServiceHelper.isValidEmail(student.getEmail())) {
                throw new IllegalArgumentException("Invalid email format: " + student.getEmail());
            }
            Student existing = dao.findByEmail(student.getEmail());
            if (existing != null) {
                throw new IllegalArgumentException(
                        "A student with email '" + student.getEmail() + "' already exists (ID: "
                                + existing.getStudent_Id() + ").");
            }
        }

        // 4. Default status
        if (ServiceHelper.isBlank(student.getStatus())) {
            student.setStatus("Active");
        }

        // 5. Persist
        dao.create(student);

        // 6. Return the created student
        if (student.getEmail() != null) {
            Student created = dao.findByEmail(student.getEmail());
            if (created != null)
                return created;
        }
        List<Student> all = dao.findAll();
        return all.isEmpty() ? student : all.get(0);
    }

    // ── Update a student (accepts Student object) ──
    @CacheEvict(value = { "students", "student" }, allEntries = true)
    public Student updateStudent(long id, Student student) {
        // 1. Validate name
        String nameError = validate(student.getFull_Name());
        if (nameError != null) {
            throw new IllegalArgumentException(nameError);
        }
        student.setFull_Name(ServiceHelper.capitalizeName(student.getFull_Name().trim()));

        // 2. Validate & normalize email
        if (!ServiceHelper.isBlank(student.getEmail())) {
            student.setEmail(student.getEmail().trim().toLowerCase());
            if (!ServiceHelper.isValidEmail(student.getEmail())) {
                throw new IllegalArgumentException("Invalid email format: " + student.getEmail());
            }
            Student existing = dao.findByEmail(student.getEmail());
            if (existing != null && existing.getStudent_Id() != id) {
                throw new IllegalArgumentException(
                        "Email '" + student.getEmail() + "' is already taken by student ID "
                                + existing.getStudent_Id() + ".");
            }
        }

        // 3. Set the ID on the student object and persist
        student.setStudent_Id(id);
        int rows = dao.update(student);
        if (rows == 0) {
            throw new StudentNotFoundException("Student with ID " + id + " not found.");
        }

        return dao.findById(id);
    }

    // ── Delete a student ──
    @CacheEvict(value = { "students", "student" }, allEntries = true)
    public void deleteStudent(long id) {
        int rows = dao.delete(id);
        if (rows == 0) {
            throw new StudentNotFoundException("Student with ID " + id + " not found.");
        }
    }

    // ── Custom exception ──
    public static class StudentNotFoundException extends RuntimeException {
        public StudentNotFoundException(String message) {
            super(message);
        }
    }
}
