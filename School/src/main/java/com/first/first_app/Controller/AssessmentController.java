package com.first.first_app.Controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.first.first_app.Model.Assessment;
import com.first.first_app.Model.AssessmentType;
import com.first.first_app.Model.Question;
import com.first.first_app.Model.Score;
import com.first.first_app.Model.Student;
import com.first.first_app.Service.AssessmentService;

@RestController
@RequestMapping("/assessments")
public class AssessmentController {
 
    @Autowired
    private AssessmentService assessmentService;

    @GetMapping("/{assessmentId}")
    public Assessment getAssessment(@PathVariable int assessmentId) {
        return assessmentService.getAssessmentById(assessmentId);
    }

    @PostMapping("/{assessmentId}/questions")
    public Assessment addQuestion( @PathVariable int assessmentId,@RequestBody Question question
    ) {
        return assessmentService.addQuestion(assessmentId, question);
    }

    @DeleteMapping("/{assessmentId}/questions/{questionId}")
    public Assessment removeQuestion( @PathVariable int assessmentId,@PathVariable int questionId
    ) {
        return assessmentService.removeQuestion(assessmentId, questionId);
    }


    @PostMapping("/{assessmentId}/submit/{studentId}")// when submit call the function of evaluate answers
    public Score submitAssessment(
            @PathVariable int assessmentId,
            @PathVariable int studentId,
            @RequestBody List<String> answers
    ) {
      
        Student student = new Student(); 
        student.setId(studentId); 

        Assessment assessment = assessmentService.getAssessmentById(assessmentId);
        return assessmentService.evaluateAnswers(assessment, student, answers);
    }


    @GetMapping("/{assessmentId}/remaining")
    public int remainingQuestions(@PathVariable int assessmentId) {
        return assessmentService.remainingQuestions(assessmentId);
    }


 @GetMapping("/assessmentNoOfQuesAssignment")// get the no of question of assignment
    public ResponseEntity<Integer> getExactNoOfQuestionsAssignment() {
        return ResponseEntity.ok(AssessmentType.ASSIGNMENT.getNoQuestions());
    }

    @GetMapping("/assessmentNoOfQuesExam")// get the no of question of exam
    public ResponseEntity<Integer> getExactNoOfQuestionsExam() {
        return ResponseEntity.ok(AssessmentType.EXAM.getNoQuestions());
    }

    @GetMapping("/maxAssignments")
    public ResponseEntity<Integer> getMaxAssignments() {
        return ResponseEntity.ok(AssessmentType.ASSIGNMENT.getNoOfAssessment());
    }

    @GetMapping("/maxExams")
    public ResponseEntity<Integer> getMaxExams() {
        return ResponseEntity.ok(AssessmentType.EXAM.getNoOfAssessment());
    }


    @GetMapping("/assessmentDurationAssignment")
    public ResponseEntity<Integer> getDurationAssignment() {
        return ResponseEntity.ok(AssessmentType.ASSIGNMENT.getDefaultDuration());
    }

    @GetMapping("/assessmentDurationExam")
    public ResponseEntity<Integer> getDurationExam() {
        return ResponseEntity.ok(AssessmentType.EXAM.getDefaultDuration());
    }


}
