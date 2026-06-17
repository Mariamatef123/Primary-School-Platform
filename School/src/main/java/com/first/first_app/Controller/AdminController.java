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
import com.first.first_app.Repo.FamilyRepo;
import com.first.first_app.Repo.LevelRepo;
import com.first.first_app.Repo.ParentRepo;
import com.first.first_app.Repo.StudentRepo;
import com.first.first_app.Repo.SubjectRepo;
import com.first.first_app.Repo.UserPhoneRepo;
import com.first.first_app.DTO.FamilyResponseDTO;
import com.first.first_app.DTO.LevelDTO;
import com.first.first_app.DTO.ParentDTO;
import com.first.first_app.DTO.PhoneDTO;
import com.first.first_app.DTO.StudentDTO;
import com.first.first_app.DTO.StudentListDTO;
import com.first.first_app.DTO.SubjectDTO;
import com.first.first_app.DTO.SubjectListDTO;
import com.first.first_app.DTO.TeacherDTO;
import com.first.first_app.Model.Family;
import com.first.first_app.Model.Level;
import com.first.first_app.Model.Subject;
import com.first.first_app.Service.AdminService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:8082", allowCredentials = "true")
public class AdminController {

    private final LevelService levelService;
    @Autowired
    private  LevelRepo levelRepo;

    @Autowired
    private SubjectRepo subjectRepo;

    @Autowired
    private  StudentRepo studentRepo;
    @Autowired
    private  FamilyRepo familyRepo;
    @Autowired
    private AdminService adminService;
    @Autowired
    private ParentRepo parentRepo;
    @Autowired
    private UserPhoneRepo userPhoneRepo;

    AdminController(LevelService levelService) {
        this.levelService = levelService;
        this.levelRepo = null;
        this.subjectRepo = null;
        this.studentRepo = null;
        this.familyRepo = null;
        this.adminService=null;
        this.parentRepo=null;
        this.userPhoneRepo=null;
    }

    @GetMapping("/dashboard-summary")
    public Map<String, Object> getDashboardSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalStudents", adminService.getAllStudents().size());
        summary.put("totalTeachers", adminService.getAllTeachers().size());
        summary.put("totalParents", adminService.getAllParents().size());
        summary.put("totalSubjects", adminService.getAllSubjects().size());
        return summary;
    }

    @GetMapping("/students/recent")
    public List<StudentDTO> getRecentStudents() {
        return adminService.getRecentStudents(); 
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
            student.setDateOfBirth(studentDTO.getDateOfBirth());
            student.setSsn(studentDTO.getSsn());
            student.setEmail(studentDTO.getEmail());
            student.setRole(Role.STUDENT);

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

    @GetMapping("/students")
    public List<StudentListDTO> getAllStudents() {
        return adminService.getAllStudents();
    }


    @GetMapping("/students/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Integer id) {
        try {
            StudentDTO student = adminService.getStudentById(id);
            return ResponseEntity.ok(student);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/students/{id}")
    public ResponseEntity<String> editStudent(@PathVariable Integer id, @RequestBody StudentDTO studentDTO) {
        try {
            adminService.editStudent(id, studentDTO);
            return ResponseEntity.ok("Student updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


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
   
              
                    phone.setUser(teacher);

                    return phone;
                }).toList();
                teacher.setPhones(phones);
            }

            Level level = subject.getLevel();
            List<Student> students = studentRepo.findByLevel(level);

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
    public List<TeacherDTO> getAllTeachers() {
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

    @GetMapping("/parents")
    public List<ParentDTO> getAllParents() {
        return adminService.getAllParents();
    }

    @GetMapping("/parents/{id}")
    public ResponseEntity<ParentDTO> getParentById(@PathVariable Integer id) {
        try {
            ParentDTO parent = adminService.getParentById(id);
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
    public ResponseEntity<List<LevelDTO>> getAllLevels() {
        return ResponseEntity.ok(adminService.getAllLevels());
    }

    @GetMapping("/subjects")
    public ResponseEntity<List<SubjectListDTO>> getAllSubjects() {
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
    public ResponseEntity<List<SubjectListDTO>> getSubjectsInLevel(
            @PathVariable int levelId) {
        try {
            List<SubjectListDTO> subjects = adminService.getSubjectsInLevel(levelId);
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
    public ResponseEntity<SubjectDTO> getSubjectById(@PathVariable int id) {
        try {
            SubjectDTO subject = adminService.getSubjectById(id);
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



@PostMapping("/parents/{parentId}/students/{studentId}")
public ResponseEntity<?> link(
        @PathVariable int parentId,
        @PathVariable int studentId) {

    try {
        Parent parent = parentRepo.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found"));

        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

  
        Parent parent2 = student.getParents()
                .stream()
                .filter(p -> p.getId() != parentId)
                .findFirst()
                .orElse(null);
Family family1 = parent.getFamily();
        Family family2 = parent2 != null ? parent2.getFamily() : null;

        if (family2 != null) {

 
            adminService.addParentToFamily(family2.getId(), parentId);
            parent.setFamily(family2);

            Map<Integer, Student> uniqueStudents = new LinkedHashMap<>();
            if (parent.getChildren() != null)
                for (Student s : parent.getChildren())
                    uniqueStudents.put(s.getId(), s);
            if (parent2.getChildren() != null)
                for (Student s : parent2.getChildren())
                    uniqueStudents.put(s.getId(), s);
            uniqueStudents.put(student.getId(), student);

            List<Student> mergedStudents = new ArrayList<>(uniqueStudents.values());


            parent.setChildren(new ArrayList<>(mergedStudents));
            parent2.setChildren(new ArrayList<>(mergedStudents));

    
            for (Student s : mergedStudents) {
                Map<Integer, Parent> uniqueParents = new LinkedHashMap<>();
                for (Parent p : s.getParents())
                    uniqueParents.put(p.getId(), p);
                uniqueParents.put(parent.getId(), parent);
                uniqueParents.put(parent2.getId(), parent2);
                s.setParents(new ArrayList<>(uniqueParents.values()));
            }


            studentRepo.saveAll(mergedStudents);
            parentRepo.save(parent);
            parentRepo.save(parent2);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Families merged into: " + family2.getFamilyName());
            response.put("familyId", family2.getId());
            deleteFamily(family1.getId());
            return ResponseEntity.ok(response);
        }


        boolean alreadyLinked = parent.getChildren().stream()
                .anyMatch(s -> s.getId() == student.getId());
        if (!alreadyLinked)
            parent.getChildren().add(student);

        boolean parentExists = student.getParents().stream()
                .anyMatch(p -> p.getId() == parent.getId());
        if (!parentExists)
            student.getParents().add(parent);


        Map<Integer, Student> uniqueChildren = new LinkedHashMap<>();
        for (Student s : parent.getChildren()) uniqueChildren.put(s.getId(), s);
        parent.setChildren(new ArrayList<>(uniqueChildren.values()));

        Map<Integer, Parent> uniqueParents = new LinkedHashMap<>();
        for (Parent p : student.getParents()) uniqueParents.put(p.getId(), p);
        student.setParents(new ArrayList<>(uniqueParents.values()));

        parentRepo.save(parent);
        studentRepo.save(student);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Linked successfully");
        response.put("parentId", parentId);
        response.put("studentId", studentId);
        return ResponseEntity.ok(response);

    } catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
@PostMapping("/families/{familyId}/parents/{parentId}")
public ResponseEntity<?> addParentToFamily(@PathVariable int familyId, @PathVariable int parentId) {

    try {
        Family family = adminService.getFamilyById(familyId);
        Parent newParent = adminService.getParentByIdWithoutDTO(parentId);

        if (family.getParents() == null)
            family.setParents(new ArrayList<>());

      
        boolean alreadyIn = family.getParents()
                .stream()
                .anyMatch(p -> p.getId() == parentId);

        if (!alreadyIn) {
            family.getParents().add(newParent);
        }

  
        List<Parent> allParents = family.getParents();


        Map<Integer, Student> mergedChildren = new LinkedHashMap<>();

        for (Parent p : allParents) {
            if (p.getChildren() != null) {
                for (Student s : p.getChildren()) {
                    mergedChildren.put(s.getId(), s);
                }
            }
        }

        List<Student> finalChildren = new ArrayList<>(mergedChildren.values());

        for (Parent p : allParents) {
            p.setChildren(new ArrayList<>(finalChildren));
            p.setFamily(family);
            parentRepo.save(p);
        }

        for (Student s : finalChildren) {
            if (s.getParents() == null)
                s.setParents(new ArrayList<>());

            Map<Integer, Parent> uniqueParents = new LinkedHashMap<>();

            for (Parent p : allParents) {
                uniqueParents.put(p.getId(), p);
            }

            s.setParents(new ArrayList<>(uniqueParents.values()));
        }

        studentRepo.saveAll(finalChildren);
        familyRepo.save(family);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Parent added and family synchronized successfully");
        response.put("familyId", familyId);

        return ResponseEntity.ok(response);

    } catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
@PostMapping("/parents")
public ResponseEntity<?> addParent(@RequestBody ParentDTO parentDTO) {
    try {
        Parent parent = new Parent();
        parent.setName(parentDTO.getName());
        parent.setEmail(parentDTO.getEmail());
        parent.setPassword(parentDTO.getPassword());
        
    
        Parent savedParent = parentRepo.save(parent);
        System.out.println("Saved parent ID: " + savedParent.getId());
        
      
        if (parentDTO.getPhones() != null) {
            for (PhoneDTO phoneDTO : parentDTO.getPhones()) {
                UserPhone phone = new UserPhone();
                phone.setPhoneNumber(phoneDTO.getPhoneNumber());
                phone.setPhoneType(phoneDTO.getPhoneType());
                phone.setUser(savedParent);
    
                userPhoneRepo.save(phone);
                System.out.println("Saved phone: " + phone.getPhoneNumber());
            }
        }
        

        Parent result = parentRepo.findById(savedParent.getId()).orElse(savedParent);
        
        return ResponseEntity.ok(new ParentDTO(result));

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
}
private Parent addParentWithChildren(Parent parent, List<Integer> studentIds) {

    Parent savedParent = parentRepo.save(parent);
    

    if (studentIds != null && !studentIds.isEmpty()) {
        List<Student> children = studentRepo.findAllById(studentIds);
        savedParent.setChildren(children);
 
        for (Student child : children) {
            if (child.getParents() == null) {
                child.setParents(new ArrayList<>());
            }
            if (!child.getParents().contains(savedParent)) {
                child.getParents().add(savedParent);
                studentRepo.save(child);
            }
        }
    }
    
    return parentRepo.save(savedParent);
}
@DeleteMapping("/parents/{parentId}/students/{studentId}")
public ResponseEntity<?> removeChild(@PathVariable int parentId, @PathVariable int studentId) {
    try {
        Parent parent = parentRepo.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found"));
        
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        

        parent.getChildren().remove(student);
        student.getParents().remove(parent);
        
        parentRepo.save(parent);
        studentRepo.save(student);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Removed successfully");
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}


@DeleteMapping("/families/{familyId}/parents/{parentId}")
public ResponseEntity<?> removeParentFromFamily(@PathVariable int familyId, @PathVariable int parentId) {
    try {
        Family family = adminService.getFamilyById(familyId);
        Parent parent = adminService.getParentByIdWithoutDTO(parentId);
        
        if (parent.getFamily() != null && parent.getFamily().getId() == familyId) {
            parent.setFamily(null);
            parentRepo.save(parent);
        }
        
        if (family.getParents() != null) {
            family.getParents().remove(parent);
            familyRepo.save(family);
        }
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Parent removed from family successfully");
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
   @PostMapping("/families")
    public ResponseEntity<?> createFamily(@RequestBody Map<String, String> body) {
        try {
            String name = body.get("familyName");
            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Family name is required");
            }
            
            Family family = new Family();
            family.setFamilyName(name);
            family.setParents(new ArrayList<>());
            
            Family savedFamily = adminService.createFamily(family);
            
            Map<String, Object> response = new HashMap<>();
            response.put("familyId", savedFamily.getId());
            response.put("familyName", savedFamily.getFamilyName());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating family: " + e.getMessage());
        }
    }

@GetMapping("/families")
public ResponseEntity<List<FamilyResponseDTO>> getFamilies() {
    List<Family> families = familyRepo.findAll();
    List<FamilyResponseDTO> result = families.stream()
            .map(FamilyResponseDTO::new)
            .collect(Collectors.toList());
    
    return ResponseEntity.ok(result);
}
@DeleteMapping("/families/{familyId}")
    public ResponseEntity<?> deleteFamily(@PathVariable int familyId) {
        try {
            Family family = adminService.getFamilyById(familyId);
            
            if (family.getParents() != null) {
                for (Parent parent : family.getParents()) {
                    parent.setFamily(null);
                    parentRepo.save(parent);
                }
            }
            
       
            familyRepo.delete(family);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Family deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

}