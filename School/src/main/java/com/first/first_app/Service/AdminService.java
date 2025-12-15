package com.first.first_app.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.first.first_app.DTO.StudentDTO;
import com.first.first_app.DTO.TeacherDTO;
import com.first.first_app.Model.Admin;
import com.first.first_app.Model.Level;
import com.first.first_app.Model.Parent;
import com.first.first_app.Model.Student;
import com.first.first_app.Model.Subject;
import com.first.first_app.Model.Teacher;
// Assuming UserPhone is a model class
import com.first.first_app.Model.UserPhone;
import com.first.first_app.Repo.AdminRepo;
import com.first.first_app.Repo.LevelRepo;
import com.first.first_app.Repo.ParentRepo;
import com.first.first_app.Repo.StudentRepo;
import com.first.first_app.Repo.SubjectRepo;
import com.first.first_app.Repo.TeacherRepo;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private final LevelService levelService;

    @Autowired
    private StudentRepo studentRepo;
    @Autowired
    private TeacherRepo teacherRepo;
    @Autowired
    private ParentRepo parentRepo;
    @Autowired
    private SubjectRepo subjectRepo;
    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    LevelRepo levelRepo;
    @Autowired
    private SubjectService subjectService;

    AdminService(LevelService levelService) {
        this.levelService = levelService;
    }

    public Teacher assignSubjectToTeacher(int teacherId, int subjectId) {
        Teacher teacher = teacherRepo.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with ID: " + teacherId));
        Subject subject = subjectRepo.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found with ID: " + subjectId));

        // Use the Teacher model's setter, which handles the bidirectional link logic
        teacher.setSubject(subject);

        return teacherRepo.save(teacher);
    }

    public Teacher removeSubjectFromTeacher(int teacherId, int subjectId) {
        Teacher teacher = teacherRepo.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with ID: " + teacherId));

        if (teacher.getSubject() != null && teacher.getSubject().getId() == subjectId) {

            teacher.setSubject(null);
            return teacherRepo.save(teacher);
        } else {
            throw new RuntimeException("Teacher is not assigned subject with ID: " + subjectId);
        }
    }

    public Student addStudent(Student student) {
        return studentRepo.save(student);
    }

    @Transactional // check the id and remove the student from its parent first and after that delete from repo
    public void deleteStudent(Integer studentId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        for (Parent parent : new ArrayList<>(student.getParents())) {
            parent.getChildren().remove(student);
        }

        student.getParents().clear();

        studentRepo.delete(student);
    }

    public void editStudent(int studentId, StudentDTO dto) {
        Student existing = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        existing.setName(dto.getName());
        existing.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            existing.setPassword(dto.getPassword());
        }
        existing.setSsn(dto.getSsn());
        existing.setDateOfBirth(dto.getdateOfBirth());

        if (dto.getLevelId() != null) {
            Level level = levelRepo.findById(dto.getLevelId())
                    .orElseThrow(() -> new RuntimeException("Level not found"));
            existing.setLevel(level);
        }

        if (dto.getAdminId() != null) {
            Admin admin = adminRepo.findById(dto.getAdminId())
                    .orElseThrow(() -> new RuntimeException("Admin not found"));
            existing.setAdmin(admin);
        }

        if (dto.getTeacherIds() != null) {
            List<Teacher> teachers = teacherRepo.findAllById(dto.getTeacherIds());
            existing.setTeachers(teachers);
        }

        if (dto.getPhones() != null) {
            List<UserPhone> phones = dto.getPhones().stream().map(p -> {
                UserPhone phone = new UserPhone();
                phone.setPhoneNumber(p.getPhoneNumber());
                phone.setPhoneType(p.getPhoneType());
                phone.setUser(existing);
                return phone;
            }).toList();
            existing.setPhones(phones);
        }

        studentRepo.save(existing);
    }

    public Student getStudentById(Integer studentId) {
        return studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));
    }

    public List<Student> getAllStudents() {
        return studentRepo.findAllWithSubjectsAndTeachers();
    }

    public List<Student> getRecentStudents() {
        return studentRepo.findTop5ByOrderByIdDesc(); // get last 5 students
    }

    @Transactional // add the teacher to students that in and add the students to teacher
    public Teacher addTeacher(Teacher teacher, List<Student> students) {
        for (Student student : students) {
            teacher.getStudents().add(student);
            student.getTeachers().add(teacher);
        }
        return teacherRepo.save(teacher);
    }

    @Transactional // set the subject to null of teacher and and set the subjects of teacher null and delete it
    public void deleteTeacher(Integer teacherId) {

        Teacher teacher = teacherRepo.findById(teacherId)

                .orElseThrow(() -> new RuntimeException("Teacher not found with ID: " + teacherId));

        Subject assignedSubject = teacher.getSubject();

        if (assignedSubject != null) {

            if (assignedSubject.getTeacher() != null && assignedSubject.getTeacher().equals(teacher)) {

                assignedSubject.setTeacher(null);

                subjectRepo.save(assignedSubject);
            }
        }

        teacher.setSubject(null);

        teacherRepo.delete(teacher);
    }

    // checks from data and save it
    public void editTeacher(Integer teacherId, TeacherDTO teacherDTO) {

        Teacher existingTeacher = teacherRepo.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        existingTeacher.setName(teacherDTO.getName());
        existingTeacher.setEmail(teacherDTO.getEmail());

        if (teacherDTO.getPassword() != null && !teacherDTO.getPassword().isEmpty()) {

            existingTeacher.setPassword(teacherDTO.getPassword());
        }

        if (teacherDTO.getSubjectId() != null) {
            Subject subject = subjectRepo.findById(teacherDTO.getSubjectId())
                    .orElseThrow(() -> new RuntimeException("Subject not found"));
            existingTeacher.setSubject(subject);
        }

        if (existingTeacher.getPhones() != null) {
            existingTeacher.getPhones().clear();
        }

        if (teacherDTO.getPhones() != null && !teacherDTO.getPhones().isEmpty()) {
            List<UserPhone> newPhones = teacherDTO.getPhones().stream().map(pdto -> {
                UserPhone phone = new UserPhone();
                phone.setPhoneNumber(pdto.getPhoneNumber());
                phone.setPhoneType(pdto.getPhoneType());
                phone.setUser(existingTeacher);
                return phone;
            }).toList();

            existingTeacher.setPhones(newPhones);
        }

        teacherRepo.save(existingTeacher);
    }

    public Teacher getTeacherById(Integer teacherId) {
        return teacherRepo.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with ID: " + teacherId));
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepo.findAll();
    }

    public Parent addParent(Parent parent) {
        // The controller already set the children and the bidirectional links.
        return parentRepo.save(parent);
    }

    public void deleteParent(Integer parentId) {
        if (parentRepo.existsById(parentId)) {
            parentRepo.deleteById(parentId);
        } else {
            throw new RuntimeException("Parent not found with ID: " + parentId);
        }
    }

    public void editParent(int parentId, Parent parent) {
        Optional<Parent> optionalParent = parentRepo.findById(parentId);
        if (optionalParent.isPresent()) {
            Parent existingParent = optionalParent.get();

            existingParent.setName(parent.getName());
            existingParent.setEmail(parent.getEmail());
            if (parent.getPassword() != null && !parent.getPassword().isEmpty()) {
                existingParent.setPassword(parent.getPassword());
            }
            existingParent.setPhones(parent.getPhones());

            existingParent.setChildren(parent.getChildren());

            parentRepo.save(existingParent);
        } else {
            throw new RuntimeException("Parent not found with ID: " + parentId);
        }
    }

    public Parent getParentById(Integer parentId) {
        return parentRepo.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found with ID: " + parentId));
    }

    public List<Parent> getAllParents() {
        return parentRepo.findAll();
    }

    @Transactional 
    // assign the level to subject and chack if exceed the limit of subjects in that level or not  and set teacher to subject and save in repo
    public Subject addSubject(Subject subject) {

        if (subject.getLevel() == null || (Integer) subject.getLevel().getId() == null) {
            throw new RuntimeException("Level ID is required for a new subject.");
        }
        Level level = levelRepo.findById(subject.getLevel().getId())
                .orElseThrow(() -> new RuntimeException("Level not found"));
        subject.setLevel(level);

        Long existingCount = subjectRepo.countByLevelId(level.getId());
        if (existingCount >= 7) {
            throw new RuntimeException("The selected level already has the maximum limit of 7 subjects.");
        }

        if (subject.getTeacher() != null && subject.getTeacher().getId() != null) {
            Teacher teacher = teacherRepo.findById(subject.getTeacher().getId())
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));
            subject.setTeacher(teacher);
        }

        return subjectRepo.save(subject);
    }

    @Transactional //validate the data and save it
    public Subject editSubject(int id, Subject updated) {
        Subject subject = subjectRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        if (updated.getName() != null)
            subject.setName(updated.getName());

        if ((Integer) updated.getCode() != null && updated.getCode() != 0)
            subject.setCode(updated.getCode());

        if (updated.getLevel() != null && (Integer) updated.getLevel().getId() != null) {
            Level newLevel = levelRepo.findById(updated.getLevel().getId())
                    .orElseThrow(() -> new RuntimeException("Level not found"));

            if (subject.getLevel().getId() != (newLevel.getId())) {
                Long existingCount = subjectRepo.countByLevelId(newLevel.getId());
                if (existingCount >= 7) {
                    throw new RuntimeException("The target level already has the maximum limit of 7 subjects.");
                }
            }
            subject.setLevel(newLevel);
        }
        if (updated.getTeacher() != null && updated.getTeacher().getId() != null) {
            Teacher teacher = teacherRepo.findById(updated.getTeacher().getId())
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));
            subject.setTeacher(teacher);
        }

        return subjectRepo.save(subject);
    }

    public Subject getSubjectById(int id) {
        return subjectRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
    }

    public void deleteSubject(int id) {
        if (subjectRepo.existsById(id)) {
            subjectService.deleteSubject(id);
        } else {
            throw new RuntimeException("Subject not found");
        }
    }

    public Level createLevel(Level level) {
        return levelService.createLevel(level);
    }

    public List<Level> getAllLevels() {
        return levelService.getAllLevels();
    }

    public List<Subject> getAllSubjects() {
        return subjectService.getAllSubjects();
    }

    public Level getLevelById(int levelId) {
        return levelService.getLevelById(levelId);
    }

    public Level editLevel(int levelId, Level level) {
        return levelService.editLevel(levelId, level);
    }

    public void deleteLevel(int levelId) {
        levelService.deleteLevel(levelId);
    }

    public Level assignSubjectToLevel(int levelId, int subjectId) {
        return levelService.assignSubjectToLevel(levelId, subjectId);
    }

    public Level removeSubjectFromLevel(int levelId, int subjectId) {
        return levelService.removeSubjectFromLevel(levelId, subjectId);
    }

    public List<Subject> getSubjectsInLevel(int levelId) {
        Level level = levelService.getLevelById(levelId);
        return level.getSubjects();
    }
}