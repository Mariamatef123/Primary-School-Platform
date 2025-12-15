package com.first.first_app.Service;

import org.springframework.stereotype.Service;

import com.first.first_app.Model.Assessment;
import com.first.first_app.Model.Score;
import com.first.first_app.Model.Student;
import com.first.first_app.Repo.StudentRepo;
import com.first.first_app.Model.Subject;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepo studentRepo;
    private final AssessmentService assessmentService;


    
    public StudentService(StudentRepo studentRepo, AssessmentService assessmentService) {
        this.studentRepo = studentRepo;
        this.assessmentService = assessmentService;
    }

   // set the student's level based on the subject's level.
    //enroll the student in the subject
     //given the Student to Level to Subject model structure.
    public Student addSubject(int studentId, Subject subject) {
        Student student = getStudentById(studentId);

        if (subject == null || subject.getLevel() == null)
            throw new IllegalArgumentException("Subject or subject's Level cannot be null.");

      
        student.setLevel(subject.getLevel());
        
        return studentRepo.save(student);
    }

    public Student getStudentById(int studentId) {
        return studentRepo.findById(studentId)
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
    // 1️⃣ Retrieve the student, throw if not found
    Student student = studentRepo.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));

    // 2️⃣ Verify that the subject belongs to the student
    Subject subject = student.getSubjects().stream()
            .filter(s -> s.getId() == subjectId)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Subject does not belong to this student"));

    // 3️⃣ Get all assessments for the subject
    List<Assessment> assessments = subject.getAssessments();



    return assessments;
}



    public Score submitAssessment(int studentId, int subjectId, int assessmentId, List<String> answers) {
        Assessment assessment = getAssessment(studentId, subjectId, assessmentId);
        Student student = getStudentById(studentId);
    
        // This evaluatingAnswer must be fixed to also set the subject on the Score object if required
        // by the Score model (which it is, based on the provided snippet).
        Score score = assessmentService.evaluateAnswers(assessment, student, answers);
        
        // Ensure the score object has the Subject reference if the model requires it
        score.setSubject(assessment.getSubject()); 
        
        return score; // score is already saved and returned by AssessmentService
    }

    public List<Subject> getSubjects(int studentId) {
        Student student = getStudentById(studentId);
        // Student.getSubjects() correctly fetches subjects via their assigned level.
        return student.getSubjects(); 
    }
        public List<Score> getAllScoresForStudent(int studentId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return student.getScores();
    }

    // Get score for a specific assessment
    public Score getScoreForStudentAssessment(int studentId, int assessmentId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return student.getScores().stream()
                .filter(score -> score.getAssessment().getAssessmentId() == assessmentId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Score not found for this assessment"));
    }

}