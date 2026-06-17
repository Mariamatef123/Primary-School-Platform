package com.first.first_app.Builder;
import com.first.first_app.Enum.AssessmentType;
import com.first.first_app.Model.*;

import java.util.ArrayList;
import java.util.List;

public class ConcreteAssessmentBuilder implements AssessmentBuilder {

    private final Assessment assessment;

    public ConcreteAssessmentBuilder() {
        this.assessment = new Assessment();
        this.assessment.setQuestions(new ArrayList<>());
    }

    @Override
    public AssessmentBuilder withTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required.");
        }
        assessment.setTitle(title);
        return this;
    }

    @Override
    public AssessmentBuilder ofType(AssessmentType type) {
        if (type == null) throw new IllegalArgumentException("Type is required.");

        assessment.setType(type);  
        assessment.setDuration(type.getDefaultDuration());
        assessment.setNumOfQuestions(type.getNoQuestions());
        return this;
    }

    @Override
    public AssessmentBuilder withCustomDuration(Integer duration) {
        if (duration != null) {
            assessment.setDuration(duration);
        }
        return this;
    }

    @Override
    public AssessmentBuilder withCustomNumOfQuestions(Integer num) {
        if (num != null) {
            assessment.setNumOfQuestions(num);
        }
        return this;
    }

    @Override
    public AssessmentBuilder belongsToSubject(Subject subject) {
        if (subject == null) throw new IllegalArgumentException("Subject is required.");
        assessment.setSubject(subject);
        return this;
    }

    @Override
    public AssessmentBuilder assignToTeacher(Teacher teacher) {
        if (teacher == null) throw new IllegalArgumentException("Teacher is required.");
        assessment.setTeacher(teacher);
        return this;
    }
    
    @Override
    public AssessmentBuilder isSummerExam(boolean isSummerExam) {
        assessment.setSummerExam(isSummerExam);
        return this;
    }

    @Override
    public AssessmentBuilder withQuestions(List<Question> questions) {
        if (questions != null) {
            for (Question q : questions) {
             if (q.getChoices() == null) {
    q.setChoices(new ArrayList<>());
}

if (q.getChoices().size() < 2) {
    throw new IllegalArgumentException("Each question must have at least 2 choices.");
}
            
                q.setAssessment(assessment);
                assessment.addQuestion(q);
            }
        }
        return this;
    }

    @Override
    public Assessment build() {
        if (assessment.getTitle() == null)
            throw new IllegalStateException("Title is required.");
        if (assessment.getType() == null)
            throw new IllegalStateException("Type is required.");
        if (assessment.getSubject() == null)
            throw new IllegalStateException("Subject is required.");

        return assessment;
    }
}
