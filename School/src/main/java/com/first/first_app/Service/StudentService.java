package com.first.first_app.Service;

import com.first.first_app.Repo.SubjectRepo;

import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.first.first_app.DTO.AssessmentDTO;
import com.first.first_app.DTO.StudentDTO;
import com.first.first_app.DTO.SubjectDTO;
import com.first.first_app.Enum.AssessmentType;
import com.first.first_app.Enum.StudentStatus;
import com.first.first_app.Enum.Term;
import com.first.first_app.Model.Assessment;
import com.first.first_app.Model.Level;
import com.first.first_app.Model.Parent;
import com.first.first_app.Model.Score;
import com.first.first_app.Model.Student;
import com.first.first_app.Repo.AssessmentRepo;
import com.first.first_app.Repo.LevelRepo;
import com.first.first_app.Repo.ScoreRepo;
import com.first.first_app.Repo.StudentRepo;
import com.first.first_app.Model.Subject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final SubjectRepo subjectRepo;
    private final StudentRepo studentRepo;
    private final AssessmentService assessmentService;
    private final LevelRepo levelRepo;
    private final AssessmentRepo assessmentRepo;
    private final JavaMailSender mailSender;
    private final ScoreRepo scoreRepo;


    
    public StudentService(StudentRepo studentRepo, AssessmentService assessmentService, LevelRepo levelRepo, AssessmentRepo assessmentRepo, SubjectRepo subjectRepo, JavaMailSender mailSender,ScoreRepo scoreRepo) {
        this.studentRepo = studentRepo;
        this.assessmentService = assessmentService;
        this.levelRepo = levelRepo;
        this.assessmentRepo = assessmentRepo;
        this.subjectRepo = subjectRepo;
        this.mailSender = mailSender;
        this.scoreRepo=scoreRepo;
    }

    private JavaMailSender getMailSender() {
        return this.mailSender;
    }


    public Student addSubject(int studentId, Subject subject) {
        Student student = getStudent(studentId);

        if (subject == null || subject.getLevel() == null)
            throw new IllegalArgumentException("Subject or subject's Level cannot be null.");

      
        student.setLevel(subject.getLevel());
        
        return studentRepo.save(student);
    }

    public Student getStudent(int studentId) {
    return studentRepo.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));
}

public StudentDTO getStudentById(int studentId) {
    return studentRepo.findById(studentId)
            .map(StudentDTO::new)
            .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));
}

public Assessment getAssessment(int studentId, int subjectId, int assessmentId) {
    Student student = studentRepo.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));

    Subject subject = student.getSubjects().stream()
            .filter(s -> s.getId() == subjectId)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Subject does not belong to this student"));

    return subject.getAssessments().stream()
            .filter(a -> a.getAssessmentId() == assessmentId)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Assessment does not belong to this subject"));
}

public List<Assessment> getAllAssessments(int studentId, int subjectId) {

    Student student = studentRepo.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));


    Subject subject = student.getSubjects().stream()
            .filter(s -> s.getId() == subjectId)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Subject does not belong to this student"));

    
    List<Assessment> assessments = subject.getAssessments().stream().filter(a->a.getSummerExam()==false).toList();



    return assessments;
}



public Score submitAssessment(int studentId, int subjectId, int assessmentId, List<String> answers) {

        Assessment assessment = assessmentRepo.findById(assessmentId)
                .orElseThrow(() -> new RuntimeException("Assessment not found"));
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Score score = assessmentService.evaluateAnswers(assessment, student, answers);
        score.setSubject(assessment.getSubject());
        scoreRepo.save(score);

      
        sendQuickEmail(student, assessment, score);

        if (assessment.getSummerExam()) {
            if (student.getStatus() != StudentStatus.SUMMER_EXAM) {
                throw new RuntimeException("Student is not eligible for summer exam");
            }

            if (score.getScore() >= 15) {
                Level nextLevel = levelRepo.findById(student.getLevelId() + 1)
                        .orElseThrow(() -> new RuntimeException("Next level not found"));
                student.setLevel(nextLevel);
                student.setStatus(StudentStatus.PASSED);
                studentRepo.save(student);
            } else {
                student.setStatus(StudentStatus.FAILED);
                studentRepo.save(student);
            }
        }

        return score;
    }
    
private void sendQuickEmail(Student student, Assessment assessment, Score score) {
    try {
        if (student.getParents() == null) return;
        
        String parentEmail = student.getParents().get(0).getEmail();
        if (parentEmail == null) return;
        
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(parentEmail);
        msg.setFrom("misrshoryanelataa@gmail.com");
        msg.setSubject("Score: " + assessment.getSubject().getName());
        msg.setText(student.getName() + " scored " + score.getScore() + "/" + assessment.getNumOfQues());
        mailSender.send(msg);
        System.out.println("Email sent");
        
    } catch (Exception e) {
        System.out.println("Email failed: " + e.getMessage());
    }
}


public List<SubjectDTO> getSubjects(int studentId) {
    Student student = getStudent(studentId);
   
    return student.getSubjects().stream().filter(s->s.getTerm()==student.getCurrentTerm())
            .map(SubjectDTO::new)
            .collect(Collectors.toList());
}
        public List<Score> getAllScoresForStudent(int studentId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return student.getScores();
    }


    public Score getScoreForStudentAssessment(int studentId, int assessmentId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return student.getScores().stream()
                .filter(score -> score.getAssessment().getAssessmentId() == assessmentId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Score not found for this assessment"));
    }
public ResponseEntity<?> getTermResult(int studentId) {

    Student student = studentRepo.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));

  
    List<Subject> currentTermSubjects = student.getSubjects().stream()
            .filter(subject -> subject.getTerm() == student.getCurrentTerm())
            .collect(Collectors.toList());

    int regularAssessmentsPerSubject = AssessmentType.EXAM.getNoOfAssessment() 
                                     + AssessmentType.ASSIGNMENT.getNoOfAssessment();
    
    int requiredAssessments = regularAssessmentsPerSubject * currentTermSubjects.size();

    long completedAssessments = student.getScores().stream()
            .filter(score -> score.getAssessment() != null)
            .filter(score -> score.getAssessment().getSummerExam() == false)
            .count();

    boolean completed = completedAssessments >= requiredAssessments;

    if (!completed) {
        return ResponseEntity.badRequest().body(
                Map.of(
                        "message",
                        "Term result is not available until all assessments are completed"
                )
        );
    }

    student.getSubjectsFailed().clear();

    AtomicInteger failed = new AtomicInteger(0);
    AtomicInteger summerFailed = new AtomicInteger(0);
    AtomicInteger summerSolved = new AtomicInteger(0);

    List<Map<String, Object>> results = new ArrayList<>();

   
    boolean isTerm2 = student.getCurrentTerm() == Term.TERM_2;

    if (isTerm2) {
  
        List<Score> term1Scores = student.getScores().stream()
                .filter(score -> score.getAssessment() != null && 
                       score.getAssessment().getSubject().getTerm() == Term.TERM_1)
                .collect(Collectors.toList());
        
        List<Score> term2Scores = student.getScores().stream()
                .filter(score -> score.getAssessment() != null && 
                       score.getAssessment().getSubject().getTerm() == Term.TERM_2)
                .collect(Collectors.toList());

     
        Map<String, List<Subject>> subjectsByName = student.getSubjects().stream()
                .collect(Collectors.groupingBy(subject -> {
                    String name = subject.getName();
                    return name.replaceAll("\\s+T[12]$", "").replaceAll("\\s+TERM[\\s_]*[12]", "").trim();
                }));

        for (Map.Entry<String, List<Subject>> entry : subjectsByName.entrySet()) {
            String baseSubjectName = entry.getKey();
            List<Subject> subjectVersions = entry.getValue();
            
            Subject term1Subject = subjectVersions.stream()
                    .filter(s -> s.getTerm() == Term.TERM_1 || s.getName().contains("T1"))
                    .findFirst()
                    .orElse(null);
            
            Subject term2Subject = subjectVersions.stream()
                    .filter(s -> s.getTerm() == Term.TERM_2 || s.getName().contains("T2"))
                    .findFirst()
                    .orElse(null);
            
           
            double term1AssignmentTotal = 0;
            double term1ExamTotal = 0;
            double term1Total = 0;
            
            if (term1Subject != null) {
                term1AssignmentTotal = term1Scores.stream()
                        .filter(score -> score.getSubject().getId().equals(term1Subject.getId()))
                        .filter(score -> score.getAssessment().getType() == AssessmentType.ASSIGNMENT)
                        .mapToDouble(Score::getScore)
                        .sum();
                
                term1ExamTotal = term1Scores.stream()
                        .filter(score -> score.getSubject().getId().equals(term1Subject.getId()))
                        .filter(score -> score.getAssessment().getType() == AssessmentType.EXAM)
                        .filter(score -> !score.getAssessment().getSummerExam())
                        .mapToDouble(Score::getScore)
                        .sum();
                
                term1Total = term1AssignmentTotal + term1ExamTotal;
            }
            
        
            double term2AssignmentTotal = 0;
            double term2ExamTotal = 0;
            double term2Total = 0;
            
            if (term2Subject != null) {
                term2AssignmentTotal = term2Scores.stream()
                        .filter(score -> score.getSubject().getId().equals(term2Subject.getId()))
                        .filter(score -> score.getAssessment().getType() == AssessmentType.ASSIGNMENT)
                        .mapToDouble(Score::getScore)
                        .sum();
                
                term2ExamTotal = term2Scores.stream()
                        .filter(score -> score.getSubject().getId().equals(term2Subject.getId()))
                        .filter(score -> score.getAssessment().getType() == AssessmentType.EXAM)
                        .filter(score -> !score.getAssessment().getSummerExam())
                        .mapToDouble(Score::getScore)
                        .sum();
                
                term2Total = term2AssignmentTotal + term2ExamTotal;
            }
            
            double combinedTotal = term1Total + term2Total;
            double maxPossible = 200.0;
            
            boolean isFailed = combinedTotal < 100;
            if (isFailed) {
                failed.incrementAndGet();
                if (term1Subject != null) student.addSubjectFailed(term1Subject);
                if (term2Subject != null) student.addSubjectFailed(term2Subject);
            }
            
            Map<String, Object> subjectResult = new HashMap<>();
            subjectResult.put("subjectId", term2Subject != null ? term2Subject.getId() : 
                             (term1Subject != null ? term1Subject.getId() : null));
            subjectResult.put("subjectName", baseSubjectName);
            subjectResult.put("term1Total", term1Total);
            subjectResult.put("term2Total", term2Total);
            subjectResult.put("term1AssignmentTotal", term1AssignmentTotal);
            subjectResult.put("term1ExamTotal", term1ExamTotal);
            subjectResult.put("term2AssignmentTotal", term2AssignmentTotal);
            subjectResult.put("term2ExamTotal", term2ExamTotal);
            subjectResult.put("totalScore", combinedTotal);
            subjectResult.put("maxScore", maxPossible);
            subjectResult.put("percentage", (combinedTotal / maxPossible) * 100);
            subjectResult.put("status", combinedTotal >= 100 ? "Passed" : "Failed");
            subjectResult.put("term1SubjectName", term1Subject != null ? term1Subject.getName() : null);
            subjectResult.put("term2SubjectName", term2Subject != null ? term2Subject.getName() : null);

            results.add(subjectResult);
        }
        
    } else {
       
        
        List<Score> term1Scores = student.getScores().stream()
                .filter(score -> score.getAssessment() != null && 
                       score.getAssessment().getSubject().getTerm() == Term.TERM_1)
                .collect(Collectors.toList());

        for (Subject subject : currentTermSubjects) {
            double term1AssignmentTotal = term1Scores.stream()
                    .filter(score -> score.getSubject().getId().equals(subject.getId()))
                    .filter(score -> score.getAssessment().getType() == AssessmentType.ASSIGNMENT)
                    .mapToDouble(Score::getScore)
                    .sum();
            
            double term1ExamTotal = term1Scores.stream()
                    .filter(score -> score.getSubject().getId().equals(subject.getId()))
                    .filter(score -> score.getAssessment().getType() == AssessmentType.EXAM)
                    .filter(score -> !score.getAssessment().getSummerExam())
                    .mapToDouble(Score::getScore)
                    .sum();
            
            double term1Total = term1AssignmentTotal + term1ExamTotal;
            double maxPossible = 100.0;
            
            boolean isFailed = term1Total < 50;
            if (isFailed) {
                failed.incrementAndGet();
                student.addSubjectFailed(subject);
            }
            
            Map<String, Object> subjectResult = new HashMap<>();
            subjectResult.put("subjectId", subject.getId());
            subjectResult.put("subjectName", subject.getName());
            subjectResult.put("term1Total", term1Total);
            subjectResult.put("term2Total", 0);
            subjectResult.put("term1AssignmentTotal", term1AssignmentTotal);
            subjectResult.put("term1ExamTotal", term1ExamTotal);
            subjectResult.put("term2AssignmentTotal", 0);
            subjectResult.put("term2ExamTotal", 0);
            subjectResult.put("totalScore", term1Total);
            subjectResult.put("maxScore", maxPossible);
            subjectResult.put("percentage", (term1Total / maxPossible) * 100);
            subjectResult.put("status", term1Total >= 50 ? "Passed" : "Failed");
            subjectResult.put("term1SubjectName", subject.getName());
            subjectResult.put("term2SubjectName", null);
            
            results.add(subjectResult);
        }
        
        results.sort(Comparator.comparing(r -> ((String) r.get("subjectName"))));
    }

    String overallStatus = "Pending";

    if (student.getCurrentTerm() == Term.TERM_2) {
        if (failed.get() == 0) {
            overallStatus = "Passed";
            student.setStatus(StudentStatus.PASSED);
            student.getSubjectsFailed().clear();
            student.setAssessmentsLocked(true);
            student.setUnlockedAssessmentCount(0);
            
            
            String term1Key = "LEVEL_" + student.getLevelId() + "_TERM_1";
            String term2Key = "LEVEL_" + student.getLevelId() + "_TERM_2";
            LocalDate now = LocalDate.now();
            
            if (student.getCompletedTerms() == null) {
                student.setCompletedTerms(new HashMap<>());
            }
            student.getCompletedTerms().put(term1Key, now);
            student.getCompletedTerms().put(term2Key, now);
            
            if (student.getCompletedLevelIds() == null) {
                student.setCompletedLevelIds(new HashSet<>());
            }
            student.getCompletedLevelIds().add(student.getLevelId());
            
           
            int nextLevelId = student.getLevelId() + 1;
            levelRepo.findById(nextLevelId).ifPresent(level -> {
                student.setLevel(level);
                student.setCurrentTerm(Term.TERM_1);
            });
            
        } else if (failed.get() <= 3) {
            overallStatus = "Failed but Has Summer Exam";
            student.setStatus(StudentStatus.SUMMER_EXAM);
        } else {
            overallStatus = "Failed";
            student.setStatus(StudentStatus.FAILED);
        }
        
       
        for (Subject subject : student.getSubjectsFailed()) {
            if (subject != null && subject.getAssessments() != null) {
                for (Assessment a : subject.getAssessments()) {
                    if (Boolean.TRUE.equals(a.getSummerExam())) {
                        student.getScores().stream()
                                .filter(s -> s.getAssessment() != null
                                        && s.getAssessment().getAssessmentId() == a.getAssessmentId())
                                .findFirst()
                                .ifPresent(s -> {
                                    summerSolved.incrementAndGet();
                                    if (s.getScore() < 15) {
                                        summerFailed.incrementAndGet();
                                    }
                                });
                    }
                }
            }
        }

        if (summerSolved.get() == student.getSubjectsFailed().size() && student.getSubjectsFailed().size() > 0) {
            if (summerFailed.get() == 0) {
                int levelId = student.getLevelId() + 1;
                Level level = levelRepo.findById(levelId).orElse(null);
                if (level != null) {
                    student.setLevel(level);
                }
                student.setLocked(true);
                student.setStatus(StudentStatus.PASSED);
                student.getSubjectsFailed().clear();
                overallStatus = "Passed after Summer Exams";
                
               
                String summerKey = "LEVEL_" + student.getLevelId() + "_SUMMER";
                student.getCompletedTerms().put(summerKey, LocalDate.now());
                student.getCompletedLevelIds().add(student.getLevelId());
            } else {
                student.setStatus(StudentStatus.FAILED);
                overallStatus = "Failed after Summer Exams";
            }
        }
    } else {
    
        student.setStatus(StudentStatus.PENDING);
        overallStatus = "Pending - Moving to Term 2";
        student.setAssessmentsLocked(true);
        student.setUnlockedAssessmentCount(0);
        student.setCurrentTerm(Term.TERM_2);
        
 
        String term1Key = "LEVEL_" + student.getLevelId() + "_TERM_1";
        if (student.getCompletedTerms() == null) {
            student.setCompletedTerms(new HashMap<>());
        }
        student.getCompletedTerms().put(term1Key, LocalDate.now());
    }

    studentRepo.save(student);

    double grandTotal = results.stream()
            .mapToDouble(r -> ((Number) r.get("totalScore")).doubleValue())
            .sum();
    
    double maxGrandTotal = results.size() * (isTerm2 ? 200.0 : 100.0);

        return ResponseEntity.ok(
            Map.ofEntries(
                Map.entry("studentId", student.getId()),
                Map.entry("studentName", student.getName()),
                Map.entry("currentTerm", student.getCurrentTerm()),
                Map.entry("grandTotal", grandTotal),
                Map.entry("maxGrandTotal", maxGrandTotal),
                Map.entry("overallPercentage", (grandTotal / maxGrandTotal) * 100),
                Map.entry("subjects", results),
                Map.entry("overallStatus", overallStatus),
                Map.entry("failedSubjectsCount", failed.get()),
                Map.entry("completedLevelIds", student.getCompletedLevelIds()),
                Map.entry("completedTerms", student.getCompletedTerms())
            )
        );
}

public int getNoOfRequiredAssessments() {
   int requiredAssessments =
           (AssessmentType.EXAM.getNoOfAssessment()
            + AssessmentType.ASSIGNMENT.getNoOfAssessment())*7 ; 
            return requiredAssessments;
}
public List<AssessmentDTO> getSummerExams(int studentId) {

    Student student = studentRepo.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));


    if (student.getStatus() != StudentStatus.SUMMER_EXAM) {
        return List.of();
    }

    return student.getSubjectsFailed().stream()
            .filter(Objects::nonNull)
            .filter(subject -> subject.getAssessments() != null)
            .flatMap(subject -> subject.getAssessments().stream())
            .filter(assessment -> Boolean.TRUE.equals(assessment.getSummerExam()))
            .map(AssessmentDTO::new)
            .collect(Collectors.toList());
}


}


