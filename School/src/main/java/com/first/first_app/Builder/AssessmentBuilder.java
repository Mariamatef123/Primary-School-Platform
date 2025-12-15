package com.first.first_app.Builder;

import com.first.first_app.Model.*;

import java.util.List;

public interface AssessmentBuilder {

    AssessmentBuilder withTitle(String title);

    AssessmentBuilder ofType(AssessmentType type);

    AssessmentBuilder withCustomDuration(Integer duration);

    AssessmentBuilder withCustomNumOfQuestions(Integer num);

    AssessmentBuilder belongsToSubject(Subject subject);

    AssessmentBuilder assignToTeacher(Teacher teacher);

    AssessmentBuilder withQuestions(List<Question> questions);

    Assessment build();
}
