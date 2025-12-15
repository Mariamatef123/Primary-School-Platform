package com.first.first_app.Service;

import java.util.List;


import org.springframework.stereotype.Service;

import com.first.first_app.Model.Assessment;
import com.first.first_app.Model.Parent;
import com.first.first_app.Model.Score;
import com.first.first_app.Model.Student;
import com.first.first_app.Repo.ParentRepo;
import com.first.first_app.Repo.StudentRepo;

@Service
public class ParentService {

    private final ParentRepo parentRepo;
    private final StudentRepo studentRepo;

    public ParentService(ParentRepo parentRepo, StudentRepo studentRepo) {
        this.parentRepo = parentRepo;
        this.studentRepo = studentRepo;
    }

    public List<Parent> getAllParents() {
    return parentRepo.findAll();
}

     // find the parent by id if exists return if not return not found
    public List<Student> getChildren(int parentId) {
        Parent parent = parentRepo.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found"));

        return parent.getChildren();
    }

    // find the parent and get the children and filter to get this student and get subjects
    public List<com.first.first_app.Model.Subject> getChildSubjects(int parentId, int childId) {

        Parent parent = parentRepo.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found"));

        Student child = parent.getChildren().stream()
                .filter(c -> c.getId() == childId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("This child does not belong to this parent"));

        return child.getSubjects();
    }

    // get the subjects of this child and filter to this subject and get assessment
    public List<Assessment> getChildAssessments(int parentId, int childId, int subjectId) {

        Student child = validateChild(parentId, childId);

        com.first.first_app.Model.Subject subject = child.getSubjects().stream()
                .filter(s -> s.getId() == subjectId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Subject not found for this child"));

        return subject.getAssessments();
    }


    //filter the child and filter the score when the assessment id matches
    public Score getChildScore(int parentId, int childId, int assessmentId) {

        Student child = validateChild(parentId, childId);

        return child.getScores().stream()
                .filter(score -> score.getAssessment().getAssessmentId() == assessmentId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Score not found"));
    }


    // this is check if the id of parent found get the child from his children by id
    private Student validateChild(int parentId, int childId) {
        Parent parent = parentRepo.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found"));

        return parent.getChildren().stream()
                .filter(c -> c.getId() == childId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Child does not belong to this parent"));
    }
}
