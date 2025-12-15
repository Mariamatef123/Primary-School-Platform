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

    public SubjectService(SubjectRepo subjectRepo) {
        this.subjectRepo = subjectRepo;
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
        // Level is usually set via LevelService.assignSubjectToLevel, but can be
        // updated here
        existingSubject.setLevel(updatedSubject.getLevel());

        // One-to-One Teacher is handled by the model's setter logic
        existingSubject.setTeacher(updatedSubject.getTeacher());

        return subjectRepo.save(existingSubject);
    }

    @Transactional
    public void deleteSubject(int id) {
        Subject subject = subjectRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        // --- FIX 1: Break the Teacher-Subject One-to-One Link ---
        if (subject.getTeacher() != null) {
            Teacher teacher = subject.getTeacher();
            teacher.setSubject(null);
            teacherRepo.save(teacher); // Explicitly save the Teacher
        }

        // --- FIX 2: Break the Level-Subject One-to-Many Link ---
        if (subject.getLevel() != null) {
            // Retrieve the Level and remove the Subject from the List
            Level level = subject.getLevel();

            // This line is CRITICAL for @OneToMany link management
            level.getSubjects().remove(subject);

            // Ensure the Level is saved to flush the collection change
            levelRepo.save(level);
        }

        // --- Cleanup the inverse (ManyToOne) side as well, just in case ---
        subject.setLevel(null);
        subject.setTeacher(null);

        // Now delete the Subject
        subjectRepo.delete(subject);
    }
}