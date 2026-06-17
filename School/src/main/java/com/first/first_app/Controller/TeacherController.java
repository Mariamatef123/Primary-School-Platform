package com.first.first_app.Controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.first.first_app.DTO.AssessmentDTO;
import com.first.first_app.DTO.SubjectDTO;
import com.first.first_app.DTO.TeacherDTO;
import com.first.first_app.Enum.AssessmentType;
import com.first.first_app.Model.Assessment;
import com.first.first_app.Model.Score;
import com.first.first_app.Model.Subject;
import com.first.first_app.Model.Teacher;
import com.first.first_app.Model.UserPhone;
import com.first.first_app.Repo.AssessmentRepo;
import com.first.first_app.Repo.ScoreRepo;
import com.first.first_app.Repo.StudentRepo;
import com.first.first_app.Model.Student;
import com.first.first_app.Service.TeacherService;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {
    private static final String TEACHER_AUTH = "hasRole('ADMIN') or #teacherId == authentication.principal.id";

    private final TeacherService teacherService;
    private final ScoreRepo scoreRepo;
    private final StudentRepo studentRepo;
    private final AssessmentRepo assessmentRepo;

    public TeacherController(TeacherService teacherService,
                             ScoreRepo scoreRepo,
                             StudentRepo studentRepo,
                             AssessmentRepo assessmentRepo) {
        this.teacherService = teacherService;
        this.scoreRepo = scoreRepo;
        this.studentRepo = studentRepo;
        this.assessmentRepo = assessmentRepo;
    }

    @GetMapping("/{teacherId}/dashboard")
    
    public Map<String, Object> getTeacherDashboard(@PathVariable int teacherId) {
        Map<String, Object> dashboard = new HashMap<>();
List<Assessment> teacherAssessments = assessmentRepo.findAll().stream()
        .filter(assessment ->
                assessment.getSubject() != null &&
                assessment.getSubject().getTeacher() != null &&
                assessment.getSubject().getTeacher().getId() == teacherId
        )
        .toList();
        long totalExams = teacherAssessments.stream()
                .filter(a -> a.getType() == AssessmentType.EXAM)
                .count();
        long totalAssignments = teacherAssessments.stream()
                .filter(a -> a.getType() == AssessmentType.ASSIGNMENT)
                .count();

        List<Score> scoresForTeacher = teacherAssessments.stream()
                .flatMap(a -> a.getScores() != null ? a.getScores().stream() : Stream.empty())
                .toList();

        long totalStudents = scoresForTeacher.stream()
                .map(s -> s.getStudent().getId())
                .distinct()
                .count();

        long pendingSubmissions = scoresForTeacher.stream()
                .filter(s -> !s.isTaken())
                .count();

        dashboard.put("totalAssignments", totalAssignments);
        dashboard.put("totalExams", totalExams);
        dashboard.put("totalStudents", totalStudents);
        dashboard.put("pendingSubmissions", pendingSubmissions);

        return dashboard;
    }

  @GetMapping("/{teacherId}/recent-submissions")
public List<Map<String, Object>> getRecentSubmissions(@PathVariable int teacherId) {

    return scoreRepo.findAll().stream()
            .filter(s ->
                    s.getAssessment() != null &&
                    s.getAssessment().getSubject() != null &&
                    s.getAssessment().getSubject().getTeacher() != null &&
                    s.getAssessment().getSubject().getTeacher().getId() == teacherId
            )
            .sorted(Comparator.comparingInt(Score::getId).reversed())
            .limit(10)
            .map(s -> {
                Map<String, Object> map = new HashMap<>();
                map.put("studentName", s.getStudent().getName());
                map.put("assessmentTitle", s.getAssessment().getTitle());
                map.put("status", s.isTaken() ? "Taken" : "Pending");
                map.put("score", s.getScore());
                map.put("scoreId", s.getId());
                return map;
            })
            .toList();
}

    @PostMapping(value = "/assessments", consumes = "application/json")
    
    public ResponseEntity<?> createAssessment(
            @RequestParam int teacherId,
            @RequestBody Assessment requestBody,
            HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "false") boolean isSummerExam
        ) {
        try {
            Teacher teacher = teacherService.getTeacherById(teacherId);

            if (teacher.getSubject() == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 400,
                        "error", "Bad Request",
                        "message", "Teacher must be assigned to a subject to create assessments.",
                        "path", request.getRequestURI()));
            }

            Assessment created = teacherService.createAssessment(
                    requestBody,
                    teacher.getSubject(),
                    teacher,isSummerExam);

            return ResponseEntity.ok(created);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "timestamp", LocalDateTime.now(),
                    "status", 500,
                    "error", "Internal Server Error",
                    "message", e.getMessage(),
                    "path", request.getRequestURI()));
        }
    }

    @PutMapping(value = "/assessments/{id}", consumes = "application/json")
    
    public ResponseEntity<?> updateAssessment(
            @PathVariable int id,
            @RequestParam int teacherId,
            @RequestBody Map<String, Object> payload,
            HttpServletRequest request) {
        try {
            Teacher teacher = teacherService.getTeacherById(teacherId);

            Assessment assessmentPayload = new ObjectMapper()
                    .convertValue(payload, Assessment.class);

            List<Integer> deletedQuestionIds = (List<Integer>) payload.get("deletedQuestionIds");

            Assessment updated = teacherService.updateAssessment(id, assessmentPayload, deletedQuestionIds);

            return ResponseEntity.ok(updated);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "timestamp", LocalDateTime.now(),
                    "status", 500,
                    "error", "Internal Server Error",
                    "message", e.getMessage(),
                    "path", request.getRequestURI()));
        }
    }

    @DeleteMapping("/assessments/{assessmentId}")
 
    public void removeAssessment(@PathVariable int assessmentId) {
        teacherService.removeAssessment(assessmentId);
    }

    @GetMapping("/assessments")
 
    public List<Assessment> getAssessments(@RequestParam int teacherId) {
        return teacherService.getAssessmentsByTeacherId(teacherId);
    }

    @PatchMapping("/assessments/{id}")
 
    public Assessment partialUpdateAssessment(
            @PathVariable int id,
            @RequestBody Assessment assessment) {
        return teacherService.partialUpdateAssessment(id, assessment);
    }

@GetMapping("/{id}/subject")
public ResponseEntity<SubjectDTO> showSubject(@PathVariable int id) {
    Teacher teacher = teacherService.getTeacherById(id);
    Subject subject = teacher.getSubject();
    
    if (subject == null) {
        return ResponseEntity.notFound().build();
    }
    
    return ResponseEntity.ok(new SubjectDTO(subject));
}
    @GetMapping("/students")
 
    public List<Student> showStudents(@RequestParam int teacherId) {
        return teacherService.getTeacherById(teacherId).getStudents();
    }

    @GetMapping("/students/{id}")
    
    public Student showStudent(@PathVariable int id) {
        return teacherService.getStudent(id);
    }

@GetMapping("/{teacherId}/students-grades")
public List<Map<String, Object>> getStudentGrades(@PathVariable int teacherId) {

    return scoreRepo.findAll().stream()
            .filter(score ->
                    score.getAssessment() != null &&
                    score.getAssessment().getSubject() != null &&
                    score.getAssessment().getSubject().getTeacher() != null &&
                    score.getAssessment().getSubject().getTeacher().getId() == teacherId
            )
            .map(s -> {
                Map<String, Object> map = new HashMap<>();
                map.put("studentName", s.getStudent().getName());
                map.put("assessmentTitle", s.getAssessment().getTitle());
                map.put("score", s.getScore());
                map.put("total", s.getAssessment().getNumOfQues());
                return map;
            })
            .toList();
}

@GetMapping("/{teacherId}")
public ResponseEntity<TeacherDTO> getTeacherProfile(@PathVariable int teacherId) {
    Teacher teacher = teacherService.getTeacherById(teacherId);
    
    if (teacher == null) {
        return ResponseEntity.notFound().build();
    }
    

    return ResponseEntity.ok(new TeacherDTO(teacher));
}

    @PutMapping("/{teacherId}")
    
    
    public ResponseEntity<Teacher> updateTeacher(
            @PathVariable int teacherId,
            @RequestBody Map<String, Object> updates) {
        Teacher existingTeacher = teacherService.getTeacherById(teacherId);
        if (existingTeacher == null) {
            return ResponseEntity.notFound().build();
        }
        String currentPassword = existingTeacher.getPassword();
        if (updates.containsKey("name")) {
            existingTeacher.setName((String) updates.get("name"));
        }
        if (updates.containsKey("email")) {
            existingTeacher.setEmail((String) updates.get("email"));
        }
        if (updates.containsKey("phones")) {
            List<Map<String, String>> phoneList = (List<Map<String, String>>) updates.get("phones");
            existingTeacher.getPhones().clear(); 
            for (Map<String, String> p : phoneList) {
                UserPhone phone = new UserPhone();
                phone.setPhoneNumber(p.get("phoneNumber"));
                phone.setPhoneType(p.getOrDefault("phoneType", "MOBILE"));
                phone.setUser(existingTeacher);
                existingTeacher.getPhones().add(phone);
            }
        }

        existingTeacher.setPassword(currentPassword);

        Teacher savedTeacher = teacherService.saveTeacher(existingTeacher);
        return ResponseEntity.ok(savedTeacher);
    }

@GetMapping("/assessments/{id}")
public ResponseEntity<AssessmentDTO> getAssessment(@PathVariable int id) {
    Assessment assessment = assessmentRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Assessment not found with id: " + id));
    
    AssessmentDTO assessmentDTO = new AssessmentDTO(assessment);
    return ResponseEntity.ok(assessmentDTO);
}
@GetMapping("/{teacherId}/SummerExam")
public ResponseEntity<List<AssessmentDTO>> getSummerExam(@PathVariable int teacherId) {
    Teacher teacher = teacherService.getTeacherById(teacherId);
    Subject subject = teacher.getSubject();
    
    if (subject == null) {
        return ResponseEntity.ok(Collections.emptyList());
    }
    
    List<AssessmentDTO> summerExams = subject.getAssessments().stream()
            .filter(assessment -> assessment != null && Boolean.TRUE.equals(assessment.getSummerExam()))
            .map(AssessmentDTO::new)
            .collect(Collectors.toList());
    
    return ResponseEntity.ok(summerExams);
}
}