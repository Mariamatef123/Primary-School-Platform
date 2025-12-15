package com.first.first_app.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonView;
import com.first.first_app.Model.Assessment;
import com.first.first_app.Model.Score;
import com.first.first_app.Model.Student;
import com.first.first_app.Model.Subject;
import com.first.first_app.Model.Views;
import com.first.first_app.Repo.AssessmentRepo;
import com.first.first_app.Repo.ScoreRepo;
import com.first.first_app.Repo.StudentRepo;
import com.first.first_app.Service.AssessmentService;
import com.first.first_app.Service.StudentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private AssessmentRepo assessmentRepo;
    @Autowired
    private StudentRepo studentRepo;
    @Autowired
    private ScoreRepo scoreRepo;
    @Autowired
    private AssessmentService assessmentService;

    @PostMapping("/{studentId}/subjects/{subjectId}")
    public Student enrollInSubject(
            @PathVariable int studentId,
            @RequestBody Subject subject) {

        return studentService.addSubject(studentId, subject);
    }

    @GetMapping("/{studentId}/subjects")
   
    public List<Subject> getSubjects(
            @PathVariable int studentId) {
        return studentService.getSubjects(studentId);
    }

    @GetMapping("/{studentId}/subjects/{subjectId}/assessments/{assessmentId}")
    public Assessment getAssessment(
            @PathVariable int studentId,
            @PathVariable int subjectId,
            @PathVariable int assessmentId) {
        return studentService.getAssessment(studentId, subjectId, assessmentId);
    }

    @GetMapping("/{studentId}/subjects/{subjectId}/assessments")
    @JsonView(Views.Public.class)
    public List<Assessment> getAllAssessments(
            @PathVariable int studentId,
            @PathVariable int subjectId) {
        return studentService.getAllAssessments(studentId, subjectId);
    }

    @PostMapping("/{studentId}/subjects/{subjectId}/assessments/{assessmentId}/submit")
    public ResponseEntity<?> submitAssessment(
            @PathVariable int studentId,
            @PathVariable int subjectId,
            @PathVariable int assessmentId,
            @RequestBody List<String> answers) {
        try {

            Score score = studentService.submitAssessment(studentId, subjectId, assessmentId, answers);
            return ResponseEntity.ok(score);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "status", 400,
                            "error", "Bad Request",
                            "message", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "status", 404,
                            "error", "Not Found",
                            "message", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", 500,
                            "error", "Internal Server Error",
                            "message", e.getMessage()));
        }
    }

    @GetMapping("/{studentId}")
    public Student getStudent(@PathVariable int studentId) {
        return studentService.getStudentById(studentId);
    }

    @GetMapping("/{studentId}/scores")
    public ResponseEntity<?> getAllStudentScores(@PathVariable int studentId) {
        try {
            List<Score> scores = studentService.getAllScoresForStudent(studentId);
            return ResponseEntity.ok(scores);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/{studentId}/assessments/{assessmentId}/score")
    public ResponseEntity<?> addScore(
            @PathVariable int studentId,
            @PathVariable int assessmentId,
            @RequestBody Score newScore) {

        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Assessment assessment = assessmentRepo.findById(assessmentId)
                .orElseThrow(() -> new RuntimeException("Assessment not found"));
        newScore.setAssessment(assessment);
        newScore.setStudent(student);
        newScore.setSubject(assessment.getSubject());
        newScore.setIsTaken(true);
        if (assessment.getScores() == null) {
            assessment.setScores(new ArrayList<>());
        }
        assessment.getScores().add(newScore);
        scoreRepo.save(newScore);
        assessmentRepo.save(assessment);

        return ResponseEntity.ok(Map.of(
                "message", "Score added successfully",
                "score", newScore));
    }

    @GetMapping("/{studentId}/assessments/{assessmentId}/score")
    public ResponseEntity<?> getScore(@PathVariable int studentId, @PathVariable int assessmentId) {

        Optional<Assessment> optAssessment = assessmentRepo.findById(assessmentId);
        if (optAssessment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Assessment not found"));
        }

        Assessment assessment = optAssessment.get();

        if (assessment.getScores() == null || assessment.getScores().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No scores exist for this assessment"));
        }

        Score score = assessment.getScores().stream()
                .filter(s -> s.getStudent() != null && s.getStudent().getId() == studentId)
                .findFirst()
                .orElse(null);

        if (score == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Score not found for this student"));
        }

        return ResponseEntity.ok(score);
    }

}
