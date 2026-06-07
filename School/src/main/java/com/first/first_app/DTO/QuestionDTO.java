package com.first.first_app.DTO;

import java.util.List;

import com.first.first_app.Model.CorrectChoice;

public class QuestionDTO {
        private int id; 
    private String headline;
    private List<String> choices;
    private CorrectChoice correct;
    public void setChoices(List<String> choices) {
        this.choices = choices;
    }
    public void setCorrect(CorrectChoice correct) {
        this.correct = correct;
    }
    public void setHeadline(String headline) {
        this.headline = headline;
    }
    public void setId(int id) {
        this.id = id;
    }
    public List<String> getChoices() {
        return choices;
    }
    public CorrectChoice getCorrect() {
        return correct;
    }
    public String getHeadline() {
        return headline;
    }
    public int getId() {
        return id;
    }
}
