package mvccrud.controller;

import jakarta.servlet.http.HttpServletResponse;
import mvccrud.model.Student;
import mvccrud.service.StudentService;
import mvccrud.service.StudentService.StudentNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class StudentController {

    private final StudentService service; // ← Service instead of DAO

    public StudentController(StudentService service) {
        this.service = service;
    }

    // ── Home page ──
    @GetMapping("/")
    public String home() {
        return "forward:/index.jsp";
    }

    // ── List all students ── (200 OK / 500 Internal Server Error)
    @GetMapping("/students")
    public String listStudents(Model model, HttpServletResponse response) {
        try {
            model.addAttribute("students", service.getAllStudents());
            response.setStatus(HttpServletResponse.SC_OK);
            model.addAttribute("statusCode", HttpServletResponse.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            model.addAttribute("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            model.addAttribute("error", "Failed to load students: " + e.getMessage());
        }
        return "students";
    }

    // ── Create a new student ── (201 / 400 / 500)
    @PostMapping("/students/create")
    public String createStudent(@RequestParam("fullName") String fullName,
            @RequestParam(value = "email", required = false) String email,
            RedirectAttributes redirectAttrs) {
        try {
            service.createStudent(fullName, email);
            redirectAttrs.addFlashAttribute("success", "Student created successfully!");
            redirectAttrs.addFlashAttribute("statusCode", HttpServletResponse.SC_CREATED);
        } catch (IllegalArgumentException e) {
            // Validation failure → 400
            redirectAttrs.addFlashAttribute("error", e.getMessage());
            redirectAttrs.addFlashAttribute("statusCode", HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Failed to create student: " + e.getMessage());
            redirectAttrs.addFlashAttribute("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return "redirect:/students";
    }

    // ── Load student for editing ── (200 / 404 / 500)
    @GetMapping("/students/edit")
    public String editStudent(@RequestParam("id") long id, Model model,
            HttpServletResponse response) {
        try {
            Student s = service.getStudentById(id);
            response.setStatus(HttpServletResponse.SC_OK);
            model.addAttribute("statusCode", HttpServletResponse.SC_OK);
            model.addAttribute("student", s);
        } catch (StudentNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("statusCode", HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("error", e.getMessage());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            model.addAttribute("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            model.addAttribute("error", "Failed to load student: " + e.getMessage());
        }
        return "student-form";
    }

    // ── Update an existing student ── (200 / 400 / 404 / 500)
    @PostMapping("/students/update")
    public String updateStudent(@RequestParam("id") long id,
            @RequestParam("fullName") String fullName,
            @RequestParam(value = "email", required = false) String email,
            RedirectAttributes redirectAttrs) {
        try {
            service.updateStudent(id, fullName, email);
            redirectAttrs.addFlashAttribute("success", "Student updated successfully!");
            redirectAttrs.addFlashAttribute("statusCode", HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
            redirectAttrs.addFlashAttribute("statusCode", HttpServletResponse.SC_BAD_REQUEST);
        } catch (StudentNotFoundException e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
            redirectAttrs.addFlashAttribute("statusCode", HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Failed to update student: " + e.getMessage());
            redirectAttrs.addFlashAttribute("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return "redirect:/students";
    }

    // ── Delete a student ── (200 / 404 / 500)
    @PostMapping("/students/delete")
    public String deleteStudent(@RequestParam("id") long id,
            RedirectAttributes redirectAttrs) {
        try {
            service.deleteStudent(id);
            redirectAttrs.addFlashAttribute("success", "Student deleted successfully!");
            redirectAttrs.addFlashAttribute("statusCode", HttpServletResponse.SC_OK);
        } catch (StudentNotFoundException e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
            redirectAttrs.addFlashAttribute("statusCode", HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Failed to delete student: " + e.getMessage());
            redirectAttrs.addFlashAttribute("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return "redirect:/students";
    }
}
