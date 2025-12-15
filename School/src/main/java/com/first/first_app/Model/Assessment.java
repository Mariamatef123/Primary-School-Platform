package com.first.first_app.Model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "assessments")
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int assessmentId;

    @Column(nullable = false)
    private String title;

    private Integer duration;

    private int numOfQues;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssessmentType type;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    @JsonBackReference("subject-assessment")
    private Subject subject;

    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("assessment-questions")
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Score> scores = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    @JsonBackReference("teacher-assessment")
    private Teacher teacher;

    @PrePersist
    @PreUpdate
    private void setDefaults() {
        if (this.type != null) {
            if (this.duration == null) {
                this.duration = type.getDefaultDuration(); 
            }
            if (this.numOfQues == 0) {
                this.numOfQues = type.getNoQuestions();
            }
        }

    }


    public Assessment() {
    }

    public Assessment(String title, AssessmentType type) {
        this.title = title;
        setAssessmentType(type);
    }


    public void setAssessmentType(AssessmentType type) {
        this.type = type;
        this.numOfQues = type.getNoQuestions();
        this.duration = type.getDefaultDuration();
    }

    public void editQuestion(Question updatedQuestion) {
        boolean found = false;
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).getId() == updatedQuestion.getId()) {
                questions.set(i, updatedQuestion);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new IllegalArgumentException(
                    "Question not found with ID: " + updatedQuestion.getId());
        }
    }

    public void deleteQuestion(Question question) {
        if (!questions.remove(question)) {
            throw new IllegalArgumentException(
                    "Question not found in this assessment: " + question.getHeadline());
        }
    }

    public int remainingQuestions() {
        return this.numOfQues - questions.size();
    }

    public int getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AssessmentType getType() {
        return type;
    }

    public Integer getDuration() {
        return duration;
    }

    public int getNumOfQues() {
        return numOfQues;
    }

    public void setNumOfQuestions(int num) {
        if (type == null)
            throw new IllegalStateException("Type must be set before numOfQuestions.");

        if (num <= 0 || num > type.getNoQuestions()) {
            throw new IllegalArgumentException(
                    "numOfQuestions must be between 1 and " + type.getNoQuestions());
        }

        this.numOfQues = num;
    }

    public void addQuestion(Question q) {
        if (questions.size() >= numOfQues)
            throw new IllegalArgumentException("Cannot exceed max questions.");
        questions.add(q);
        q.setAssessment(this);
    }

    public List<Question> getQuestions() {
        return new ArrayList<>(questions);
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public List<Score> getScores() {
        return scores;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
    }

    public void addScore(Score score) {
        scores.add(score);
        score.setAssessment(this);
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setNumOfQues(int numOfQues) {
        this.numOfQues = numOfQues;
    }

    public void setType(AssessmentType type) {
        this.type = type;
    }

}
