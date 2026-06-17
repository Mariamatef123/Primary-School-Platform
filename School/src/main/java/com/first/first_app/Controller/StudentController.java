package com.first.first_app.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonView;
import com.first.first_app.DTO.ScoreDTO;
import com.first.first_app.DTO.StudentDTO;
import com.first.first_app.DTO.SubjectDTO;
import com.first.first_app.Enum.AssessmentType;
import com.first.first_app.Enum.Term;
import com.first.first_app.Model.Assessment;
import com.first.first_app.Model.Level;
import com.first.first_app.Model.Parent;
import com.first.first_app.Model.Score;
import com.first.first_app.Model.Student;
import com.first.first_app.Model.Subject;
import com.first.first_app.Model.Views;
import com.first.first_app.Repo.AssessmentRepo;
import com.first.first_app.Repo.LevelRepo;
import com.first.first_app.Repo.ScoreRepo;
import com.first.first_app.Repo.StudentRepo;
import com.first.first_app.Service.AssessmentService;
import com.first.first_app.Service.StudentService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;



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
        @Autowired
    private LevelRepo levelRepo;
    @Autowired
      private  JavaMailSender mailSender;

    @PostMapping("/{studentId}/subjects/{subjectId}")
    public Student enrollInSubject(
            @PathVariable int studentId,
            @RequestBody Subject subject) {

        return studentService.addSubject(studentId, subject);
    }

    @GetMapping("/{studentId}/subjects")
   
    public List<SubjectDTO> getSubjects(
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

@PostMapping("/{studentId}/submit/{subjectId}/{assessmentId}")
public ResponseEntity<?> submitAssessment(@PathVariable int studentId,
                                          @PathVariable int subjectId,
                                          @PathVariable int assessmentId,
                                          @RequestBody List<String> answers) {
    try {
        Score score = studentService.submitAssessment(studentId, subjectId, assessmentId, answers);
        

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Assessment submitted successfully");
        response.put("score", score.getScore());
        response.put("maxScore", score.getAssessment() != null ? score.getAssessment().getNumOfQues() : 100);
        
        return ResponseEntity.ok(response);
        
    } catch (Exception e) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}
    @GetMapping("/{studentId}")
    public StudentDTO getStudent(@PathVariable int studentId) {
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
  sendEmail(student, assessment, newScore);
        return ResponseEntity.ok(Map.of(
                "message", "Score added successfully",
                "score", newScore.getScore()));
    }
private void sendEmail(Student student, Assessment assessment, Score score) {
    try {
        if (student.getParents() == null || student.getParents().isEmpty()) {
            System.out.println("No parents found for student: " + student.getName());
            return;
        }
        
        String studentName = student.getName();
        String subjectName = assessment.getSubject().getName();
        String assessmentTitle = assessment.getTitle();
        int scoreValue = score.getScore();
        int maxScore = assessment.getNumOfQues();
        double percentage = (scoreValue * 100.0) / maxScore;
        String date = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        
        for (Parent parent : student.getParents()) {
            if (parent.getEmail() != null && !parent.getEmail().isEmpty()) {
                try {
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setTo(parent.getEmail());
                    message.setFrom("mariamatef353@gmail.com");
                    message.setSubject("Academic Assessment Result - " + studentName + " - " + subjectName);
                    
                    String emailBody = String.format(
                        "TINYSCHOLARS ACADEMY\n" +
                        "====================\n\n" +
                        "Date: %s\n" +
                       
                        "Dear %s,\n\n" +
                        "ACADEMIC ASSESSMENT REPORT\n\n" +
                        "Student Name: %s\n" +
                        "Subject: %s\n" +
                        "Assessment: %s\n" +
                        "Type: %s\n" +
                        "Score: %d / %d\n" +
                        "Percentage: %.1f%%\n\n" +
                        "Performance Level: %s\n\n" +
                        "Should you have any questions, please contact the academic office.\n\n" +
                        "Sincerely,\n" +
                        "Academic Affairs Office\n" +
                        "TinyScholars Academy\n" +
                        "================================\n" +
                        "This is an automated notification.",
                        date,
                       
                        parent.getName(),
                        studentName,
                        subjectName,
                        assessmentTitle,
                        assessment.getType().toString(),
                        scoreValue,
                        maxScore,
                        percentage,
                        percentage >= 70 ? "Satisfactory" : (percentage >= 50 ? "Needs Improvement" : "Unsatisfactory")
                    );
                    
                    message.setText(emailBody);
                    mailSender.send(message);
                    System.out.println("Email sent to: " + parent.getEmail());
                    
                } catch (Exception e) {
                    System.err.println("Failed to send email to " + parent.getEmail() + ": " + e.getMessage());
                }
            }
        }
    } catch (Exception e) {
        System.err.println("Email error: " + e.getMessage());
    }
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


    return ResponseEntity.ok(new ScoreDTO(score));
}
    @GetMapping("/getTermResult/{studentId}")
public ResponseEntity<?> getTermResult(@PathVariable int studentId) {
    return studentService.getTermResult(studentId);
}
@GetMapping("RequiredNoOfAssessments")
public int getNoOFRequiredAssessments() {
    return studentService.getNoOfRequiredAssessments();
}
@GetMapping("/{studentId}/getSummerExams")
public ResponseEntity<?> getSummerExams(@PathVariable int studentId) {
    return ResponseEntity.ok(studentService.getSummerExams(studentId));
}
@GetMapping("/{studentId}/all-terms-all-levels")
public ResponseEntity<?> getAllTermsAllLevels(@PathVariable int studentId) {
    try {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<Map<String, Object>> allTermsResults = new ArrayList<>();

    
        Set<Subject> allSubjectsFromScores = student.getScores().stream()
                .filter(score -> score.getAssessment() != null
                        && score.getAssessment().getSubject() != null
                        && score.getAssessment().getSubject().getLevel() != null)
                .map(score -> score.getAssessment().getSubject())
                .collect(Collectors.toSet());


        if (student.getLevel() != null && student.getLevel().getSubjects() != null) {
            allSubjectsFromScores.addAll(student.getLevel().getSubjects());
        }


        Map<Integer, List<Subject>> subjectsByLevelId = allSubjectsFromScores.stream()
                .filter(s -> s.getLevel() != null)
                .collect(Collectors.groupingBy(s -> s.getLevel().getId()));

        List<Integer> sortedLevelIds = new ArrayList<>(subjectsByLevelId.keySet());
        Collections.sort(sortedLevelIds);

        List<Score> allTerm1Scores = student.getScores().stream()
                .filter(score -> score.getAssessment() != null
                        && score.getAssessment().getSubject() != null
                        && score.getAssessment().getSubject().getTerm() == Term.TERM_1
                        && Boolean.FALSE.equals(score.getAssessment().getSummerExam()))
                .collect(Collectors.toList());

        List<Score> allTerm2Scores = student.getScores().stream()
                .filter(score -> score.getAssessment() != null
                        && score.getAssessment().getSubject() != null
                        && score.getAssessment().getSubject().getTerm() == Term.TERM_2
                        && Boolean.FALSE.equals(score.getAssessment().getSummerExam()))
                .collect(Collectors.toList());

        for (int levelId : sortedLevelIds) {
            Level level = levelRepo.findById(levelId).orElse(null);
            if (level == null) continue;

            List<Subject> levelSubjects = subjectsByLevelId.get(levelId);

            List<Subject> term1Subjects = levelSubjects.stream()
                    .filter(s -> s.getTerm() == Term.TERM_1)
                    .collect(Collectors.toList());

            List<Subject> term2Subjects = levelSubjects.stream()
                    .filter(s -> s.getTerm() == Term.TERM_2)
                    .collect(Collectors.toList());


            if (!term1Subjects.isEmpty()) {
                Map<String, Object> term1Result = calculateTermResultForLevel(
                        student, level, Term.TERM_1,
                        term1Subjects,       
                        allTerm1Scores, allTerm2Scores);
                if (term1Result != null) allTermsResults.add(term1Result);
            }


            if (!term2Subjects.isEmpty()) {
                List<Subject> allLevelSubjectsForTerm2 = new ArrayList<>();
                allLevelSubjectsForTerm2.addAll(term1Subjects);
                allLevelSubjectsForTerm2.addAll(term2Subjects);

                Map<String, Object> term2Result = calculateTermResultForLevel(
                        student, level, Term.TERM_2,
                        allLevelSubjectsForTerm2,  
                        allTerm1Scores, allTerm2Scores);
                if (term2Result != null) allTermsResults.add(term2Result);
            }
        }

   
        int totalTerms      = allTermsResults.size();
        int passedTerms     = (int) allTermsResults.stream()
                .filter(r -> "Passed".equals(r.get("overallStatus"))).count();
        int failedTerms     = (int) allTermsResults.stream()
                .filter(r -> "Failed".equals(r.get("overallStatus"))).count();
        int summerExamTerms = (int) allTermsResults.stream()
                .filter(r -> "Failed but Has Summer Exam".equals(r.get("overallStatus"))).count();

        double avgPercentage = allTermsResults.stream()
                .mapToDouble(r -> {
                    Object val = r.get("overallPercentage");
                    return val instanceof Number ? ((Number) val).doubleValue() : 0.0;
                })
                .average().orElse(0.0);

        Map<String, Object> response = new HashMap<>();
        response.put("studentId",        student.getId());
        response.put("studentName",      student.getName() != null ? student.getName() : "");
        response.put("currentLevelId",   student.getLevelId());
        response.put("currentLevelName", student.getLevel() != null ? student.getLevel().getName() : null);
        response.put("currentTerm",      student.getCurrentTerm());
        response.put("allTermsResults",  allTermsResults);

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalTerms",        totalTerms);
        statistics.put("passedTerms",       passedTerms);
        statistics.put("failedTerms",       failedTerms);
        statistics.put("summerExamTerms",   summerExamTerms);
        statistics.put("averagePercentage", avgPercentage);
        statistics.put("levelsCount",       sortedLevelIds.size());
        response.put("statistics", statistics);

        return ResponseEntity.ok(response);

    } catch (Exception e) {
        e.printStackTrace();
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }
}


private Map<String, Object> calculateTermResultForLevel(Student student, Level level, Term term,
                                                         List<Subject> subjects,
                                                         List<Score> allTerm1Scores,
                                                         List<Score> allTerm2Scores) {
    try {
        List<Map<String, Object>> subjectResults = new ArrayList<>();
        double grandTotal = 0;
        int failedCount   = 0;

        if (term == Term.TERM_2) {
    
            Map<String, List<Subject>> subjectsByName = subjects.stream()
                    .collect(Collectors.groupingBy(subject -> {
                        String name = subject.getName();
                        return name.replaceAll("\\s+T[12]$", "")
                                   .replaceAll("\\s+TERM[\\s_]*[12]", "")
                                   .trim();
                    }));

            for (Map.Entry<String, List<Subject>> entry : subjectsByName.entrySet()) {
                String baseSubjectName    = entry.getKey();
                List<Subject> versions    = entry.getValue();

                Subject term1Subject = versions.stream()
                        .filter(s -> s.getTerm() == Term.TERM_1)
                        .findFirst().orElse(null);

                Subject term2Subject = versions.stream()
                        .filter(s -> s.getTerm() == Term.TERM_2)
                        .findFirst().orElse(null);

     
                double term1AssignmentTotal = 0, term1ExamTotal = 0, term1Total = 0;
                if (term1Subject != null) {
                    final Integer t1Id = term1Subject.getId();

                    term1AssignmentTotal = allTerm1Scores.stream()
                            .filter(s -> s.getSubject() != null
                                    && t1Id.equals(s.getSubject().getId())
                                    && s.getAssessment().getType() == AssessmentType.ASSIGNMENT)
                            .mapToDouble(Score::getScore).sum();

                    term1ExamTotal = allTerm1Scores.stream()
                            .filter(s -> s.getSubject() != null
                                    && t1Id.equals(s.getSubject().getId())
                                    && s.getAssessment().getType() == AssessmentType.EXAM)
                            .mapToDouble(Score::getScore).sum();

                    term1Total = term1AssignmentTotal + term1ExamTotal;
                }

                double term2AssignmentTotal = 0, term2ExamTotal = 0, term2Total = 0;
                if (term2Subject != null) {
                    final Integer t2Id = term2Subject.getId();

                    term2AssignmentTotal = allTerm2Scores.stream()
                            .filter(s -> s.getSubject() != null
                                    && t2Id.equals(s.getSubject().getId())
                                    && s.getAssessment().getType() == AssessmentType.ASSIGNMENT)
                            .mapToDouble(Score::getScore).sum();

                    term2ExamTotal = allTerm2Scores.stream()
                            .filter(s -> s.getSubject() != null
                                    && t2Id.equals(s.getSubject().getId())
                                    && s.getAssessment().getType() == AssessmentType.EXAM)
                            .mapToDouble(Score::getScore).sum();

                    term2Total = term2AssignmentTotal + term2ExamTotal;
                }

                double combinedTotal = term1Total + term2Total;
                double maxPossible   = 200.0; 
                if (combinedTotal < 100) failedCount++;
                grandTotal += combinedTotal;

                Map<String, Object> subjectResult = new HashMap<>();
                subjectResult.put("subjectId",   term2Subject != null ? term2Subject.getId()
                                               : (term1Subject != null ? term1Subject.getId() : null));
                subjectResult.put("subjectName",          baseSubjectName);
                subjectResult.put("term1Total",           term1Total);
                subjectResult.put("term2Total",           term2Total);
                subjectResult.put("term1AssignmentTotal", term1AssignmentTotal);
                subjectResult.put("term1ExamTotal",       term1ExamTotal);
                subjectResult.put("term2AssignmentTotal", term2AssignmentTotal);
                subjectResult.put("term2ExamTotal",       term2ExamTotal);
                subjectResult.put("assignmentTotal",      term1AssignmentTotal + term2AssignmentTotal);
                subjectResult.put("examTotal",            term1ExamTotal + term2ExamTotal);
                subjectResult.put("totalScore",           combinedTotal);
                subjectResult.put("maxScore",             maxPossible);
                subjectResult.put("percentage",           (combinedTotal / maxPossible) * 100);
                subjectResult.put("status",               combinedTotal >= 100 ? "Passed" : "Failed");

                subjectResults.add(subjectResult);
            }

            long pairCount = subjectsByName.size();
            double maxGrandTotal   = pairCount * 200.0;
            double overallPct      = maxGrandTotal > 0 ? (grandTotal / maxGrandTotal) * 100 : 0;
            String overallStatus   = failedCount == 0 ? "Passed"
                                   : failedCount <= 3  ? "Failed but Has Summer Exam"
                                   :                     "Failed";

            String completionKey   = "LEVEL_" + level.getId() + "_" + term.name();
            LocalDate completionDate = student.getCompletedTerms() != null
                    ? student.getCompletedTerms().get(completionKey) : null;

            Map<String, Object> result = new HashMap<>();
            result.put("levelId",             level.getId());
            result.put("levelName",           level.getName());
            result.put("levelNumber",         extractLevelNumber(level.getName()));
            result.put("term",                term.name());
            result.put("termNumber",          2);
            result.put("termDisplay",         "Second Term");
            result.put("hasCompleted",        completionDate != null);
            result.put("completedDate",       completionDate);
            result.put("subjects",            subjectResults);
            result.put("grandTotal",          grandTotal);
            result.put("maxGrandTotal",       maxGrandTotal);
            result.put("overallPercentage",   overallPct);
            result.put("overallStatus",       overallStatus);
            result.put("failedSubjectsCount", failedCount);
            result.put("isCurrentLevel",      student.getLevelId() == level.getId());
            result.put("isCurrentTerm",       student.getCurrentTerm() == term
                                              && student.getLevelId() == level.getId());
            return result;

        } else {

            for (Subject subject : subjects) {
                final Integer subId = subject.getId();

                double assignmentTotal = allTerm1Scores.stream()
                        .filter(s -> s.getSubject() != null
                                && subId.equals(s.getSubject().getId())
                                && s.getAssessment().getType() == AssessmentType.ASSIGNMENT)
                        .mapToDouble(Score::getScore).sum();

                double examTotal = allTerm1Scores.stream()
                        .filter(s -> s.getSubject() != null
                                && subId.equals(s.getSubject().getId())
                                && s.getAssessment().getType() == AssessmentType.EXAM)
                        .mapToDouble(Score::getScore).sum();

                double totalScore  = assignmentTotal + examTotal;
                double maxPossible = 100.0;

                if (totalScore < 50) failedCount++;
                grandTotal += totalScore;

                Map<String, Object> subjectResult = new HashMap<>();
                subjectResult.put("subjectId",        subId);
                subjectResult.put("subjectName",      subject.getName());
                subjectResult.put("assignmentTotal",  assignmentTotal);
                subjectResult.put("examTotal",        examTotal);
                subjectResult.put("totalScore",       totalScore);
                subjectResult.put("maxScore",         maxPossible);
                subjectResult.put("percentage",       (totalScore / maxPossible) * 100);
                subjectResult.put("status",           totalScore >= 50 ? "Passed" : "Failed");
                subjectResult.put("term1Total",       totalScore);
                subjectResult.put("term2Total",       0);
                subjectResult.put("term1AssignmentTotal", assignmentTotal);
                subjectResult.put("term1ExamTotal",       examTotal);
                subjectResult.put("term2AssignmentTotal", 0);
                subjectResult.put("term2ExamTotal",       0);

                subjectResults.add(subjectResult);
            }

            double maxGrandTotal   = subjects.size() * 100.0;
            double overallPct      = maxGrandTotal > 0 ? (grandTotal / maxGrandTotal) * 100 : 0;
            String overallStatus   = failedCount == 0 ? "Passed"
                                   :  "Failed but moved to second term";
                                 

            String completionKey   = "LEVEL_" + level.getId() + "_" + term.name();
            LocalDate completionDate = student.getCompletedTerms() != null
                    ? student.getCompletedTerms().get(completionKey) : null;

            Map<String, Object> result = new HashMap<>();
            result.put("levelId",             level.getId());
            result.put("levelName",           level.getName());
            result.put("levelNumber",         extractLevelNumber(level.getName()));
            result.put("term",                term.name());
            result.put("termNumber",          1);
            result.put("termDisplay",         "First Term");
            result.put("hasCompleted",        completionDate != null);
            result.put("completedDate",       completionDate);
            result.put("subjects",            subjectResults);
            result.put("grandTotal",          grandTotal);
            result.put("maxGrandTotal",       maxGrandTotal);
            result.put("overallPercentage",   overallPct);
            result.put("overallStatus",       overallStatus);
            result.put("failedSubjectsCount", failedCount);
            result.put("isCurrentLevel",      student.getLevelId() == level.getId());
            result.put("isCurrentTerm",       student.getCurrentTerm() == term
                                              && student.getLevelId() == level.getId());
            return result;
        }

    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}

private int extractLevelNumber(String levelName) {
    if (levelName == null) return 1;
    String numbers = levelName.replaceAll("\\D+", "");
    return numbers.isEmpty() ? 1 : Integer.parseInt(numbers);
}

}
