package com.first.first_app.Service;

import com.first.first_app.Model.Subject;
import com.first.first_app.Model.Teacher;
import com.first.first_app.Repo.LevelRepo;
import com.first.first_app.Repo.SubjectRepo;
import com.first.first_app.Repo.TeacherRepo;

import com.first.first_app.Model.Level;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    private final SubjectRepo subjectRepo;

    @Autowired
    private TeacherRepo teacherRepo;

    @Autowired
    private LevelRepo levelRepo;

    public SubjectService(SubjectRepo subjectRepo,TeacherRepo teacherRepo,LevelRepo levelRepo) {
        this.subjectRepo = subjectRepo;
        this.levelRepo=levelRepo;
        this.teacherRepo=teacherRepo;
    }

    public Subject createSubject(Subject subject) {
        return subjectRepo.save(subject);
    }

    public List<Subject> getAllSubjects() {
        return subjectRepo.findAll();
    }

    public Subject getSubjectById(int subjectId) {
        return subjectRepo.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found with ID: " + subjectId));
    }

    public Subject updateSubject(int id, Subject updatedSubject) {
        Subject existingSubject = getSubjectById(id);

        existingSubject.setCode(updatedSubject.getCode());
        existingSubject.setName(updatedSubject.getName());
     
        existingSubject.setLevel(updatedSubject.getLevel());

        
        existingSubject.setTeacher(updatedSubject.getTeacher());

        return subjectRepo.save(existingSubject);
    }

    @Transactional
    public void deleteSubject(int id) {
        Subject subject = subjectRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        if (subject.getTeacher() != null) {
            Teacher teacher = subject.getTeacher();
            teacher.setSubject(null);
            teacherRepo.save(teacher); 
        }

      
        if (subject.getLevel() != null) {
         
            Level level = subject.getLevel();

            level.getSubjects().remove(subject);

            levelRepo.save(level);
        }

 
        subject.setLevel(null);
        subject.setTeacher(null);

        subjectRepo.delete(subject);
    }
}