package com.first.first_app.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import com.first.first_app.DTO.ScoreDTO;
import com.first.first_app.DTO.StudentDTO;
import com.first.first_app.DTO.SubjectDTO;
import com.first.first_app.Model.Assessment;
import com.first.first_app.Model.Parent;

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

  
public List<StudentDTO> getChildren(int parentId) {
    Parent parent = parentRepo.findById(parentId)
            .orElseThrow(() -> new RuntimeException("Parent not found with ID: " + parentId));
    
    List<Student> children = parent.getChildren();
    
    if (children == null || children.isEmpty()) {
        return new ArrayList<>(); 
    }
    
    return children.stream()
            .map(StudentDTO::new)
            .collect(Collectors.toList());
}


    public List<SubjectDTO> getChildSubjects(int parentId, int childId) {

        Parent parent = parentRepo.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found"));

        Student child = parent.getChildren().stream()
                .filter(c -> c.getId() == childId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("This child does not belong to this parent"));

        return child.getSubjects().stream()
            .map(SubjectDTO::new)
            .collect(Collectors.toList());
    }

   
    public List<Assessment> getChildAssessments(int parentId, int childId, int subjectId) {

        Student child = validateChild(parentId, childId);

        com.first.first_app.Model.Subject subject = child.getSubjects().stream()
                .filter(s -> s.getId() == subjectId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Subject not found for this child"));

        return subject.getAssessments();
    }


public ScoreDTO getChildScore(int parentId, int childId, int assessmentId) {

    Student child = validateChild(parentId, childId);

    return child.getScores().stream()
            .filter(s -> s.getAssessment() != null && s.getAssessment().getAssessmentId() == assessmentId)
            .findFirst()
            .map(ScoreDTO::new)
            .orElseThrow(() -> new RuntimeException("Score not found for assessment ID: " + assessmentId));
}
  
    private Student validateChild(int parentId, int childId) {
        Parent parent = parentRepo.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found"));

        return parent.getChildren().stream()
                .filter(c -> c.getId() == childId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Child does not belong to this parent"));
    }
}
