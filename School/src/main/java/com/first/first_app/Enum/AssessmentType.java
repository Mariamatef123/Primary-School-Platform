package com.first.first_app.Enum;

public enum AssessmentType {

    EXAM(60, 30, 2),       
    ASSIGNMENT(null, 10, 4);  
    private final Integer defaultDuration;  
    private final int noQuestions;
    private final int noOfAss;

    AssessmentType(Integer defaultDuration, int noQuestions, int noOfAss) {
        this.defaultDuration = defaultDuration;
        this.noQuestions = noQuestions;
        this.noOfAss = noOfAss;
    }

    public Integer getDefaultDuration() {
        return defaultDuration;
    }

    public int getNoQuestions() {
        return noQuestions;
    }

    public int getNoOfAssessment() {
        return noOfAss;
    }
}
