package com.first.first_app.DTO;

import java.util.List;

import com.first.first_app.Model.Assessment;


public class AssessmentDTO {
    private int assessmentId;
    private String title;
    private Integer duration;
    private int numOfQues;
    private String type;
    private int subjectId; // add this
    private List<QuestionDTO> questions;
    // private List<Integer> deletedQuestionIds;
    public AssessmentDTO(Assessment a) {
        this.assessmentId = a.getAssessmentId();
        this.title = a.getTitle();
        this.duration = a.getDuration();
        this.numOfQues = a.getNumOfQues();
        this.type = a.getType().name();
        this.subjectId = a.getSubject() != null ? a.getSubject().getId() : 0;
    }

    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }
    public int getAssessmentId() {
        return assessmentId;
    }
    public void setDuration(Integer duration) {
        this.duration = duration;
    }
    public Integer getDuration() {
        return duration;
    }
    public void setNumOfQues(int numOfQues) {
        this.numOfQues = numOfQues;
    }
    public int getNumOfQues() {
        return numOfQues;
    }
    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }
    public int getSubjectId() {
        return subjectId;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
public void setQuestions(List<QuestionDTO> questions) {
    this.questions = questions;
}
public List<QuestionDTO> getQuestions() {
    return questions;
}
// public void setDeletedQuestionIds(List<Integer> deletedQuestionIds) {
//     this.deletedQuestionIds = deletedQuestionIds;
// }
// public List<Integer> getDeletedQuestionIds() {
//     return deletedQuestionIds;
// }
}
