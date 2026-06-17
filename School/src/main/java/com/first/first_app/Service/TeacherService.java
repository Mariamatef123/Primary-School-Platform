package com.first.first_app.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.first.first_app.Builder.ConcreteAssessmentBuilder;
import com.first.first_app.Model.Assessment;
import com.first.first_app.Model.Question;
import com.first.first_app.Model.Score;
import com.first.first_app.Model.Student;
import com.first.first_app.Model.Subject;
import com.first.first_app.Model.Teacher;
import com.first.first_app.Repo.AssessmentRepo;
import com.first.first_app.Repo.QuestionRepo;
import com.first.first_app.Repo.StudentRepo;
import com.first.first_app.Repo.TeacherRepo;

import java.util.ArrayList;
import java.util.List;


@Service
public class TeacherService {

    private final AssessmentRepo assessmentRepo;
    private final StudentRepo studentRepo;
    private final TeacherRepo teacherRepo;
  @Autowired QuestionRepo questionRepo;
    public TeacherService(AssessmentRepo assessmentRepo,
                          StudentRepo studentRepo,
                          TeacherRepo teacherRepo) {
        this.assessmentRepo = assessmentRepo;
        this.studentRepo = studentRepo;
        this.teacherRepo = teacherRepo;
    }

    public Teacher getTeacherById(int teacherId) {
        return teacherRepo.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with ID: " + teacherId));
    }

    public Teacher saveTeacher(Teacher teacher) {
        return teacherRepo.save(teacher);
    }

    public void removeAssessment(int assessmentId) {
        if (!assessmentRepo.existsById(assessmentId)) {
            throw new RuntimeException("Assessment not found with ID: " + assessmentId);
        }
        assessmentRepo.deleteById(assessmentId);
    }

 
    public List<Assessment> getAssessmentsByTeacherId(int teacherId) {
        Teacher teacher = getTeacherById(teacherId);
        Subject subject = teacher.getSubject();
        
        if (subject == null) {
            throw new RuntimeException("Teacher is not currently assigned to a subject.");
        }
    
        return subject.getAssessments().stream().filter(a->a.getSummerExam()==false).toList(); 
    }


    public Assessment partialUpdateAssessment(int id, Assessment updatedAssessment) {
        Assessment existing = assessmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Assessment not found"));
                
        if (updatedAssessment.getTitle() != null) existing.setTitle(updatedAssessment.getTitle());
        if (updatedAssessment.getType() != null) existing.setType(updatedAssessment.getType());
        
 
        if (updatedAssessment.getQuestions() != null && !updatedAssessment.getQuestions().isEmpty()) {
            existing.getQuestions().clear(); 
            updatedAssessment.getQuestions().forEach(existing::addQuestion);
        }
        
        return assessmentRepo.save(existing);
    }

    public List<Student> getStudentsByTeacherId(int teacherId) {
        Teacher teacher = getTeacherById(teacherId);
        return teacher.getStudents();
    }

    public Student getStudent(int studentId) {
        return studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
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
       
    public Assessment getAssessmentById(int id) {
        return assessmentRepo.findById(id).orElse(null);
    }

public Assessment createAssessment(Assessment request, Subject subject, Teacher teacher,boolean isSummerExam   ) {

    if (request.getQuestions() == null || request.getQuestions().isEmpty()) {
        throw new IllegalArgumentException("Questions cannot be empty");
    }

    for (Question q : request.getQuestions()) {
        if (q.getChoices() == null || q.getChoices().size() < 2) {
            throw new IllegalArgumentException("Each question must have at least 2 choices");
        }
        q.setAssessment(null);
    }

    Assessment assessment = new ConcreteAssessmentBuilder()
            .withTitle(request.getTitle())
            .ofType(request.getType())
            .withCustomDuration(request.getDuration())
            .withCustomNumOfQuestions(request.getNumOfQues())
            .belongsToSubject(subject)
            .assignToTeacher(teacher)
            .withQuestions(request.getQuestions())
            .isSummerExam(request.getSummerExam())
            .build();

    return assessmentRepo.save(assessment);
}

    public Assessment updateAssessment(int id, Assessment request, List<Integer> deletedQuestionIds) {

        Assessment existing = assessmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Assessment not found with id: " + id));

     
        existing.setTitle(request.getTitle());

        if (request.getDuration() != null) existing.setDuration(request.getDuration());
        if (request.getNumOfQues() > 0) existing.setNumOfQuestions(request.getNumOfQues());

        if (deletedQuestionIds != null) {
            for (Integer qid : deletedQuestionIds) {
                Question q = questionRepo.findById(qid)
                        .orElseThrow(() -> new RuntimeException("Question not found with id: " + qid));
                existing.getQuestions().remove(q);
                questionRepo.delete(q);
            }
        }

  
        List<Question> incoming = request.getQuestions() != null ? request.getQuestions() : new ArrayList<>();
        List<Question> current = existing.getQuestions();

        for (Question q : incoming) {
            if (q.getId() == 0) { 
                q.setAssessment(existing);
                current.add(q);
            } else {
                current.stream()
                        .filter(eq -> eq.getId() == q.getId())
                        .findFirst()
                        .ifPresent(eq -> {
                            eq.setHeadline(q.getHeadline());
                            eq.setChoices(q.getChoices());
                            eq.setCorrect(q.getCorrect());
                        });
            }
        }

        return assessmentRepo.save(existing);
    }

}