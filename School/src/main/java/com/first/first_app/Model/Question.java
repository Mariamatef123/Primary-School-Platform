package com.first.first_app.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.first.first_app.Enum.CorrectChoice;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    private String headline;

    @ElementCollection
    @CollectionTable(name = "question_choices", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "choice")
    private List<String> choices = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private CorrectChoice correct;

    @ManyToOne
    @JoinColumn(name = "assessment_id")
    @JsonBackReference("assessment-questions")
    private Assessment assessment;

    public Question() {
    }

    public Question(String headline, List<String> choices, CorrectChoice correct) {
        this.headline = headline;
        this.choices = new ArrayList<>(choices);
        this.correct = correct;
    }

    public int getId() {
        return id;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public List<String> getChoices() {
        return choices;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices != null ? new ArrayList<>(choices) : new ArrayList<>();
    }

    public CorrectChoice getCorrect() {
        return correct;
    }

    public void setCorrect(CorrectChoice correct) {
        this.correct = correct;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

 
}
