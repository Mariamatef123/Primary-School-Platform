// package com.first.first_app.Controller;

// import java.util.List;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.first.first_app.Model.Subject;
// import com.first.first_app.Service.AdminService;
// import com.first.first_app.Service.SubjectService;


// @RestController
// @RequestMapping("/api/admin")
// public class SubjectController {

//     private final AdminService adminService;

//     public SubjectController(AdminService adminService) {
//         this.adminService = adminService;
//     }

//     // Create Subject
//     @PostMapping("/subjects")
//     public ResponseEntity<?> addSubject(@RequestBody Subject subject) {
//         try {
//             Subject newSubject = adminService.addSubject(subject);
//             return ResponseEntity.ok(newSubject);
//         } catch (IllegalArgumentException e) {
//             return ResponseEntity.badRequest().body(e.getMessage());
//         } catch (Exception e) {
//             return ResponseEntity.internalServerError().body("Failed to create subject: " + e.getMessage());
//         }
//     }

//     // Edit Subject
//     @PutMapping("/subjects/{id}")
//     public ResponseEntity<?> editSubject(@PathVariable int id, @RequestBody Subject subject) {
//         try {
//             Subject updated = adminService.editSubject(id, subject);
//             return ResponseEntity.ok(updated);
//         } catch (RuntimeException e) {
//             return ResponseEntity.badRequest().body(e.getMessage());
//         }
//     }

//     // Get Subject by ID
//     @GetMapping("/subjects/{id}")
//     public ResponseEntity<?> getSubjectById(@PathVariable int id) {
//         try {
//             Subject subject = adminService.getSubjectById(id);
//             return ResponseEntity.ok(subject);
//         } catch (RuntimeException e) {
//             return ResponseEntity.notFound().build();
//         }
//     }

//     // Delete Subject
//     @DeleteMapping("/subjects/{id}")
//     public ResponseEntity<String> deleteSubject(@PathVariable int id) {
//         try {
//             adminService.deleteSubject(id);
//             return ResponseEntity.ok("Subject deleted successfully");
//         } catch (RuntimeException e) {
//             return ResponseEntity.badRequest().body(e.getMessage());
//         }
//     }

//     // Get all subjects
//     @GetMapping("/subjects")
//     public List<Subject> getAllSubjects() {
//         return adminService.getAllSubjects();
//     }
// }
