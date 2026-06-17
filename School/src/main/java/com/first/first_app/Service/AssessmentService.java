package com.first.first_app.Service;

import org.springframework.stereotype.Service;

import com.first.first_app.Model.Assessment;
import com.first.first_app.Model.Score;
import com.first.first_app.Model.Question;
import com.first.first_app.Model.Student;
import com.first.first_app.Repo.AssessmentRepo;
import com.first.first_app.Repo.QuestionRepo;
import com.first.first_app.Repo.ScoreRepo;
import com.first.first_app.Repo.StudentRepo;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AssessmentService {

    private final AssessmentRepo assessmentRepo;
    private final QuestionRepo questionRepo;
    private final ScoreRepo scoreRepo;
    public final StudentRepo studentRepo;

    public AssessmentService(AssessmentRepo assessmentRepo,
            QuestionRepo questionRepo,
            ScoreRepo scoreRepo) {
        this.assessmentRepo = assessmentRepo;
        this.questionRepo = questionRepo;
        this.scoreRepo = scoreRepo;
        this.studentRepo = null;
    }

    public Assessment getAssessmentById(int assessmentId) {
        return assessmentRepo.findById(assessmentId)
                .orElseThrow(() -> new RuntimeException("Assessment not found with ID: " + assessmentId));
    }

   
    public Assessment addQuestion(int assessmentId, Question q) {
        Assessment a = getAssessmentById(assessmentId);
        a.addQuestion(q);
        return assessmentRepo.save(a);
    }


    public Assessment removeQuestion(int assessmentId, int questionId) {
        Assessment a = getAssessmentById(assessmentId);
        Question q = questionRepo.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        a.deleteQuestion(q);
        return assessmentRepo.save(a);
    }


    public Assessment editQuestion(int assessmentId, Question updatedQ) {
        Assessment a = getAssessmentById(assessmentId);
        a.editQuestion(updatedQ);
        return assessmentRepo.save(a);
    }

    public int remainingQuestions(int assessmentId) {
        Assessment a = getAssessmentById(assessmentId);
        return a.remainingQuestions();
    }


    @Transactional
    public Score evaluateAnswers(Assessment assessment, Student student, List<String> answers) {
        List<Question> questions = assessment.getQuestions();

        if (answers.size() != questions.size()) {
            throw new IllegalArgumentException(
                    "Expected " + questions.size() + " answers, but got " + answers.size());
        }

        int scoreCount = 0;
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            String correct = q.getCorrect() == null ? "" : q.getCorrect().name().toLowerCase();
            String given = answers.get(i) == null ? "" : answers.get(i).trim().toLowerCase();

            if (given.equals(correct) || given.equals(correct.replace("choice", ""))) {
                scoreCount++;
            }
        }
        Score scoreObj = new Score();
        scoreObj.setStudent(student);
        scoreObj.setAssessment(assessment);
        scoreObj.setScore(scoreCount);
        scoreObj.setIsTaken(true);

    
        if (assessment.getScores() == null) {
            assessment.setScores(new ArrayList<>());
        }
        assessment.getScores().add(scoreObj);
        scoreRepo.save(scoreObj);
        assessmentRepo.save(assessment);
        return scoreObj;
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

}