package com.first.first_app.Controller;

import com.first.first_app.Service.LevelService; 
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.first.first_app.Model.Parent;
import com.first.first_app.Model.Role;
import com.first.first_app.Model.Student;
import com.first.first_app.Model.Teacher;
import com.first.first_app.Model.UserPhone;
import com.first.first_app.Repo.AdminRepo;
import com.first.first_app.Repo.LevelRepo;
import com.first.first_app.Repo.ParentRepo;
import com.first.first_app.Repo.StudentRepo;
import com.first.first_app.Repo.SubjectRepo;
import com.first.first_app.DTO.ParentDTO;
import com.first.first_app.DTO.StudentDTO;
import com.first.first_app.DTO.TeacherDTO;

import com.first.first_app.Model.Level;
import com.first.first_app.Model.Subject;
import com.first.first_app.Service.AdminService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final LevelService levelService;
    @Autowired
    private final LevelRepo levelRepo;

    @Autowired
    private SubjectRepo subjectRepo;

    @Autowired
    private final StudentRepo studentRepo;
    @Autowired
    private final AdminRepo adminRepo;
    @Autowired
    private AdminService adminService;
    @Autowired
    private ParentRepo parentRepo;

    AdminController(LevelService levelService) {
        this.levelService = levelService;
        this.levelRepo = null;
        this.subjectRepo = null;
        this.studentRepo = null;
        this.adminRepo = null;
    }

    @GetMapping("/dashboard-summary")//size of all needed
    public Map<String, Object> getDashboardSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalStudents", adminService.getAllStudents().size());
        summary.put("totalTeachers", adminService.getAllTeachers().size());
        summary.put("totalParents", adminService.getAllParents().size());
        summary.put("totalSubjects", adminService.getAllSubjects().size());
        return summary;
    }

    @GetMapping("/students/recent")//find top 5 desc
    public List<Student> getRecentStudents() {
        return adminService.getRecentStudents(); // implement method in service
    }


    @PostMapping(value = "/students", consumes = { "application/json", "application/json;charset=UTF-8" })
    public ResponseEntity<?> addStudent(@RequestBody @Valid StudentDTO studentDTO) {
        try {

            Level level = levelRepo.findById(studentDTO.getLevelId())
                    .orElseThrow(() -> new RuntimeException("Level not found"));

            List<Parent> parents = studentDTO.getParentIds() != null
                    ? parentRepo.findAllById(studentDTO.getParentIds())
                    : new ArrayList<>();

            Student student = new Student();
            student.setName(studentDTO.getName());
            student.setDateOfBirth(studentDTO.getdateOfBirth());
            student.setSsn(studentDTO.getSsn());
            student.setEmail(studentDTO.getEmail());
            student.setRole(Role.valueOf(studentDTO.getRole()));

            if (studentDTO.getPassword() != null && !studentDTO.getPassword().isEmpty()) {
                student.setPassword(studentDTO.getPassword());
            }
            student.setLevel(level);
            student.setParents(parents);
            Student saved = adminService.addStudent(student);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //set all details of student and check the level that assigned to it found or not and call the service to save in repo
    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return adminService.getAllStudents();
    }
// call service to get all students by find all method of student repo

    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Integer id) {
        try {
            Student student = adminService.getStudentById(id);
            return ResponseEntity.ok(student);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
//call the service that this student found(by id) or not

    @PutMapping("/students/{id}")
    public ResponseEntity<String> editStudent(@PathVariable Integer id, @RequestBody StudentDTO studentDTO) {
        try {
            adminService.editStudent(id, studentDTO);
            return ResponseEntity.ok("Student updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
//take the id and object to check if exists or not by service

    @DeleteMapping("/students/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Integer id) {
        try {
            adminService.deleteStudent(id);
            return ResponseEntity.ok("Student deleted successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
// take id and call the service that checks if exists to delete or not

    @PostMapping("/teachers")
    public ResponseEntity<?> addTeacher(@RequestBody TeacherDTO teacherDTO) {
        try {
            
            if (teacherDTO.getSubjectId() == null) {
                return ResponseEntity.badRequest().body("Subject ID is required");
            }

            Subject subject = subjectRepo.findById(teacherDTO.getSubjectId())
                    .orElseThrow(() -> new RuntimeException("Subject not found"));


            Teacher teacher = new Teacher();
            teacher.setName(teacherDTO.getName());
            teacher.setEmail(teacherDTO.getEmail());
            teacher.setPassword(teacherDTO.getPassword());
            teacher.setRole(Role.valueOf(teacherDTO.getRole()));
            teacher.setSubject(subject);


            if (teacherDTO.getPhones() != null) {
                List<UserPhone> phones = teacherDTO.getPhones().stream().map(pdto -> {
                    UserPhone phone = new UserPhone();
                    phone.setPhoneNumber(pdto.getPhoneNumber());
                    phone.setPhoneType(pdto.getPhoneType());
   
                    //Set the back-reference to the parent Teacher entity 
                    phone.setUser(teacher);

                    return phone;
                }).toList();
                teacher.setPhones(phones);
            }

            //Get students by level
            Level level = subject.getLevel();
            List<Student> students = studentRepo.findByLevel(level);

            //Save teacher
            Teacher newTeacher = adminService.addTeacher(teacher, students);

            return ResponseEntity.ok(newTeacher);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }

    @PutMapping("/teachers/{id}")
    public ResponseEntity<String> editTeacher(@PathVariable Integer id, @RequestBody TeacherDTO teacherDTO) {
        try {
            adminService.editTeacher(id, teacherDTO);
            return ResponseEntity.ok("Teacher updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/teachers")
    public List<Teacher> getAllTeachers() {
        return adminService.getAllTeachers();
    }

    @GetMapping("/teachers/{id}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable Integer id) {
        try {
            Teacher teacher = adminService.getTeacherById(id);
            return ResponseEntity.ok(teacher);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/teachers/{id}")
    public ResponseEntity<String> deleteTeacher(@PathVariable Integer id) {
        try {
            adminService.deleteTeacher(id);
            return ResponseEntity.ok("Teacher deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/teachers/{teacherId}/subjects/{subjectId}")
    public ResponseEntity<Teacher> assignSubjectToTeacher(
            @PathVariable int teacherId,
            @PathVariable int subjectId) {
        try {
            Teacher teacher = adminService.assignSubjectToTeacher(teacherId, subjectId);
            return ResponseEntity.ok(teacher);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/teachers/{teacherId}/subjects/{subjectId}")
    public ResponseEntity<Teacher> removeSubjectFromTeacher(
            @PathVariable int teacherId,
            @PathVariable int subjectId) {
        try {
            Teacher teacher = adminService.removeSubjectFromTeacher(teacherId, subjectId);
            return ResponseEntity.ok(teacher);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/parents")//valid the data and set the details and find the student that belongs to it and add to him and add him to their
    public ResponseEntity<?> addParent(@RequestBody ParentDTO parentDTO) {
        try {
            if (parentDTO.getName() == null || parentDTO.getName().isEmpty()) {
                return ResponseEntity.badRequest().body("Parent name is required");
            }
            if (parentDTO.getEmail() == null || parentDTO.getEmail().isEmpty()) {
                return ResponseEntity.badRequest().body("Parent email is required");
            }
            if (parentDTO.getPassword() == null || parentDTO.getPassword().isEmpty()) {
                return ResponseEntity.badRequest().body("Password is required");
            }

            // Map DTO to parent entity
            Parent parent = new Parent();
            parent.setName(parentDTO.getName());
            parent.setEmail(parentDTO.getEmail());
            parent.setPassword(parentDTO.getPassword());

            if (parentDTO.getPhones() != null) {
                List<UserPhone> phones = parentDTO.getPhones().stream().map(pdto -> {
                    UserPhone phone = new UserPhone();
                    phone.setPhoneNumber(pdto.getPhoneNumber());
                    phone.setPhoneType(pdto.getPhoneType());
                    phone.setUser(parent); // back-reference
                    return phone;
                }).toList();
                parent.setPhones(phones);
            }
            if (parentDTO.getChildren() != null) {
                List<Integer> studentIds = parentDTO.getChildren().stream()
                        .map(cdto -> cdto.getId())
                        .filter(java.util.Objects::nonNull)
                        .toList();

                List<Student> existingChildren = studentRepo.findAllById(studentIds);

                if (existingChildren.size() != studentIds.size()) {
                    return ResponseEntity.badRequest().body("One or more children IDs were invalid or not found.");
                }

                parent.setChildren(existingChildren);
                for (Student student : existingChildren) {
                    student.getParents().add(parent);
                }
            }
            Parent newParent = adminService.addParent(parent);
            return ResponseEntity.ok(newParent);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }

    @GetMapping("/parents")
    public List<Parent> getAllParents() {
        return adminService.getAllParents();
    }

    @GetMapping("/parents/{id}")
    public ResponseEntity<Parent> getParentById(@PathVariable Integer id) {
        try {
            Parent parent = adminService.getParentById(id);
            return ResponseEntity.ok(parent);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/parents/{id}")
    public ResponseEntity<String> editParent(@PathVariable Integer id, @RequestBody Parent parent) {
        try {
            adminService.editParent(id, parent);
            return ResponseEntity.ok("Parent updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/parents/{id}")
    public ResponseEntity<String> deleteParent(@PathVariable Integer id) {
        try {
            adminService.deleteParent(id);
            return ResponseEntity.ok("Parent deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/levels")
    public ResponseEntity<?> createLevel(@RequestBody Level level) {
        try {
            Level newLevel = adminService.createLevel(level);
            return ResponseEntity.ok(newLevel);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/levels")
    public ResponseEntity<List<Level>> getAllLevels() {
        return ResponseEntity.ok(adminService.getAllLevels());
    }

    @GetMapping("/subjects")
    public ResponseEntity<List<Subject>> getAllSubjects() {
        return ResponseEntity.ok(adminService.getAllSubjects());
    }

    @GetMapping("/levels/{id}")
    public ResponseEntity<Level> getLevelById(@PathVariable int id) {
        try {
            Level level = adminService.getLevelById(id);
            return ResponseEntity.ok(level);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/levels/{id}")
    public ResponseEntity<String> editLevel(@PathVariable int id, @RequestBody Level level) {
        try {
            adminService.editLevel(id, level);
            return ResponseEntity.ok("Level updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/levels/{id}")
    public ResponseEntity<String> deleteLevel(@PathVariable int id) {
        try {
            adminService.deleteLevel(id);
            return ResponseEntity.ok("Level deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/levels/{levelId}/subjects/{subjectId}")
    public ResponseEntity<?> assignSubjectToLevel(
            @PathVariable int levelId,
            @PathVariable int subjectId) {
        try {
            Level level = adminService.assignSubjectToLevel(levelId, subjectId);
            return ResponseEntity.ok(level);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/levels/{levelId}/subjects/{subjectId}")
    public ResponseEntity<Level> removeSubjectFromLevel(
            @PathVariable int levelId,
            @PathVariable int subjectId) {
        try {
            Level level = adminService.removeSubjectFromLevel(levelId, subjectId);
            return ResponseEntity.ok(level);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/levels/{levelId}/subjects")
    public ResponseEntity<List<Subject>> getSubjectsInLevel(
            @PathVariable int levelId) {
        try {
            List<Subject> subjects = adminService.getSubjectsInLevel(levelId);
            return ResponseEntity.ok(subjects);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/subjects")
    public ResponseEntity<?> addSubject(@RequestBody Subject subject) {
        try {
            Subject newSubject = adminService.addSubject(subject);
            return ResponseEntity.ok(newSubject);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to create subject: " + e.getMessage());
        }
    }

    @PutMapping("/subjects/{id}")
    public ResponseEntity<String> editSubject(@PathVariable int id, @RequestBody Subject subject) {
        try {
            adminService.editSubject(id, subject);
            return ResponseEntity.ok("Subject updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/subjects/{id}")
    public ResponseEntity<Subject> getSubjectById(@PathVariable int id) {
        try {
            Subject subject = adminService.getSubjectById(id);
            return ResponseEntity.ok(subject);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/subjects/{id}")
    public ResponseEntity<String> deleteSubject(@PathVariable int id) {
        try {
            adminService.deleteSubject(id);
            return ResponseEntity.ok("Subject deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}