package com.first.first_app.Model;

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String headline;

    @ElementCollection
    private List<String> choices;

    @Enumerated(EnumType.STRING)
    @JsonView(Views.Internal.class) 
    private CorrectChoice correct;

    @ManyToOne
    @JoinColumn(name = "assessment_id")
    @JsonBackReference("assessment-questions")
    private Assessment assessment;

    public Question() {
    }

    public Question(String headline, List<String> choices, CorrectChoice correct) {
        this.headline = headline;
        this.choices = choices;
        this.correct = correct;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public List<String> getchoices() {
        return choices;
    }

    public void setchoices(List<String> choices) {
        this.choices = choices;
    }

    public CorrectChoice getCorrect() {
        return correct;
    }

    public void setCorrect(CorrectChoice correct) {
        this.correct = correct;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    public Assessment getAssessment() {
        return assessment;
    }
}
