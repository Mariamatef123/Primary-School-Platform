package com.first.first_app.Service;

import com.first.first_app.FirstAppApplication;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.first.first_app.DTO.LevelDTO;
import com.first.first_app.DTO.ParentDTO;
import com.first.first_app.DTO.PhoneDTO;
import com.first.first_app.DTO.StudentDTO;
import com.first.first_app.DTO.StudentListDTO;
import com.first.first_app.DTO.SubjectDTO;
import com.first.first_app.DTO.SubjectListDTO;
import com.first.first_app.DTO.TeacherDTO;
import com.first.first_app.Enum.Term;
import com.first.first_app.Model.Admin;
import com.first.first_app.Model.Family;
import com.first.first_app.Model.Level;
import com.first.first_app.Model.Parent;

import com.first.first_app.Model.Student;
import com.first.first_app.Model.Subject;
import com.first.first_app.Model.Teacher;

import com.first.first_app.Model.UserPhone;
import com.first.first_app.Repo.AdminRepo;
import com.first.first_app.Repo.FamilyRepo;
import com.first.first_app.Repo.LevelRepo;
import com.first.first_app.Repo.ParentRepo;
import com.first.first_app.Repo.StudentRepo;
import com.first.first_app.Repo.SubjectRepo;
import com.first.first_app.Repo.TeacherRepo;
import com.first.first_app.Repo.UserPhoneRepo;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

@Service
public class AdminService {

    private final FirstAppApplication firstAppApplication;

    private final LevelService levelService;
 
    private final FamilyRepo familyRepo;
    private final StudentRepo studentRepo;
    private final TeacherRepo teacherRepo;
    private final ParentRepo parentRepo;
    private final SubjectRepo subjectRepo;
    private final AdminRepo adminRepo;
    private final LevelRepo levelRepo;
    private final SubjectService subjectService;
    private final UserPhoneRepo userPhoneRepo;
    private final PasswordEncoder passwordEncoder;

    AdminService(LevelService levelService,
                 FirstAppApplication firstAppApplication,
                 FamilyRepo familyRepo,
                 StudentRepo studentRepo,
                 TeacherRepo teacherRepo,
                 ParentRepo parentRepo,
                 SubjectRepo subjectRepo,
                 AdminRepo adminRepo,
                 LevelRepo levelRepo,
                 SubjectService subjectService,
                 UserPhoneRepo userPhoneRepo,
                 PasswordEncoder passwordEncoder) {
        this.levelService = levelService;
        this.firstAppApplication = firstAppApplication;
        this.familyRepo = familyRepo;
        this.studentRepo = studentRepo;
        this.teacherRepo = teacherRepo;
        this.parentRepo = parentRepo;
        this.subjectRepo = subjectRepo;
        this.adminRepo = adminRepo;
        this.levelRepo = levelRepo;
        this.subjectService = subjectService;
        this.userPhoneRepo = userPhoneRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public Teacher assignSubjectToTeacher(int teacherId, int subjectId) {
        Teacher teacher = teacherRepo.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with ID: " + teacherId));
        Subject subject = subjectRepo.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found with ID: " + subjectId));

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


    public void editStudent(int studentId, StudentDTO dto) {
        Student existing = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        existing.setName(dto.getName());
        existing.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            existing.setPassword(dto.getPassword());
        }
        existing.setSsn(dto.getSsn());
        existing.setDateOfBirth(dto.getDateOfBirth());

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
public List<StudentListDTO> getAllStudents() {
    List<Student> students = studentRepo.findAllWithSubjectsAndTeachers();
    return students.stream()
            .map(StudentListDTO::new)
            .collect(Collectors.toList());
}


public StudentDTO getStudentById(Integer studentId) {
    Student student = studentRepo.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));
    return new StudentDTO(student);
}

public List<StudentDTO> getRecentStudents() {
    return studentRepo.findTop5ByOrderByIdDesc()
              .stream() .map(StudentDTO::new)
            .collect(Collectors.toList());
}

    @Transactional 
    public Teacher addTeacher(Teacher teacher, List<Student> students) {
        for (Student student : students) {
            teacher.getStudents().add(student);
            student.getTeachers().add(teacher);
        }
        return teacherRepo.save(teacher);
    }


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

    public List<TeacherDTO> getAllTeachers() {
        return teacherRepo.findAll().stream() .map(TeacherDTO::new)
            .collect(Collectors.toList());
    }
@PostMapping("/parents")
public ResponseEntity<?> addParent(@RequestBody ParentDTO parentDTO) {
    try {
        Parent parent = new Parent();
        parent.setName(parentDTO.getName());
        parent.setEmail(parentDTO.getEmail());
        parent.setPassword(parentDTO.getPassword());
        
        
        if (parentDTO.getPhones() != null && !parentDTO.getPhones().isEmpty()) {
            List<UserPhone> phones = parentDTO.getPhones().stream()
                    .map(phoneDTO -> {
                        UserPhone phone = new UserPhone();
                        phone.setPhoneNumber(phoneDTO.getPhoneNumber());
                        phone.setPhoneType(phoneDTO.getPhoneType());
                        phone.setUser(parent);
                        return phone;
                    })
                    .collect(Collectors.toList());
            parent.setPhones(phones);
        }
        
        List<Integer> studentIds = parentDTO.getChildren() != null
                ? parentDTO.getChildren().stream()
                    .map(c -> c.getId())
                    .toList()
                : new ArrayList<>();

        Parent saved = addParentWithChildren(parent, studentIds);

        return ResponseEntity.ok(saved);

    } catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
  
private boolean isStrongPassword(String password) { return password != null && password.length() >= 8 && password.matches(".*[A-Z].*") && password.matches(".*[a-z].*") && password.matches(".*\\d.*") && password.matches(".*[@#$%^&+=!].*"); }

public ParentDTO getParentById(Integer parentId) {

    Parent parent = parentRepo.findById(parentId)
            .orElseThrow(() -> new RuntimeException(
                    "Parent not found with ID: " + parentId));

    ParentDTO dto = new ParentDTO(parent);

    List<PhoneDTO> phones = userPhoneRepo.findByUser_Id(parent.getId())
            .stream()
            .map(phone -> new PhoneDTO(
                    phone.getPhoneNumber(),
                    phone.getPhoneType()))
            .collect(Collectors.toList());

    dto.setPhones(phones);

    return dto;
}

public List<ParentDTO> getAllParents() {

    List<Parent> parents = parentRepo.findAll();

    return parents.stream().map(parent -> {

        ParentDTO dto = new ParentDTO(parent);

        List<PhoneDTO> phones = userPhoneRepo.findByUser_Id(parent.getId())
                .stream()
                .map(phone -> new PhoneDTO(
                        phone.getPhoneNumber(),
                        phone.getPhoneType()))
                .collect(Collectors.toList());

        dto.setPhones(phones);

        return dto;

    }).collect(Collectors.toList());
}

@Transactional 
public Subject addSubject(Subject subject) {

    
    if (subject.getLevel() == null || subject.getLevel().getId() == 0) {
        throw new RuntimeException("Level ID is required for a new subject.");
    }
    

    if (subject.getTerm() == null) {
        throw new RuntimeException("Term is required. Please select TERM_1 or TERM_2.");
    }
    

    if (subjectRepo.findAll().stream()
            .anyMatch(s -> subject.getCode() == s.getCode())) {
        throw new RuntimeException("Subject code must be unique. Code " + subject.getCode() + " is already in use.");
    }
    

    Level level = levelRepo.findById(subject.getLevel().getId())
            .orElseThrow(() -> new RuntimeException("Level not found"));
    subject.setLevel(level);
    

    long existingCountInTerm = subjectRepo.countByLevelIdAndTerm(level.getId(), subject.getTerm());
    
    if (existingCountInTerm >= Level.SUBJECTS_PER_TERM) {
        throw new RuntimeException(
            String.format("Cannot add subject to %s in level '%s'. This term already has the maximum limit of %d subjects. " +
                          "Current subjects in %s: %d/%d",
                subject.getTerm().getDisplayName(),
                level.getName(),
                Level.SUBJECTS_PER_TERM,
                subject.getTerm().getDisplayName(),
                existingCountInTerm,
                Level.SUBJECTS_PER_TERM)
        );
    }
  
    if (subject.getTeacher() != null && subject.getTeacher().getId() != null) {
        Teacher teacher = teacherRepo.findById(subject.getTeacher().getId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        subject.setTeacher(teacher);
    }
    
    return subjectRepo.save(subject);
}
@Transactional 
public Subject editSubject(int id, Subject updated) {
    Subject subject = subjectRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Subject not found"));
    
  
    Integer newCode = updated.getCode();
    
  
    if (newCode != null && newCode != 0 && !newCode.equals(subject.getCode())) {
        boolean codeExists = subjectRepo.findAll().stream()
                .anyMatch(s -> s.getId() != id && s.getCode() == newCode);
        if (codeExists) {
            throw new RuntimeException("Subject code must be unique. Code " + newCode + " is already in use.");
        }
        subject.setCode(newCode);
    }

  
    if (updated.getName() != null && !updated.getName().equals(subject.getName())) {
        subject.setName(updated.getName());
    }

    if (updated.getTerm() != null && updated.getTerm() != subject.getTerm()) {
        Level currentLevel = subject.getLevel();
        if (currentLevel != null) {
      
            long termCountInCurrentLevel = subjectRepo.countByLevelIdAndTerm(currentLevel.getId(), updated.getTerm());
            if (termCountInCurrentLevel >= Level.SUBJECTS_PER_TERM) {
                throw new RuntimeException(
                    String.format("Cannot change term to %s. Level '%s' already has %d subjects in this term (maximum is %d).",
                        updated.getTerm().getDisplayName(), 
                        currentLevel.getName(), 
                        termCountInCurrentLevel, 
                        Level.SUBJECTS_PER_TERM)
                );
            }
        }
        subject.setTerm(updated.getTerm());
    }


    if (updated.getLevel() != null && updated.getLevel().getId() != 0) {
        Level newLevel = levelRepo.findById(updated.getLevel().getId())
                .orElseThrow(() -> new RuntimeException("Level not found"));

        if (subject.getLevel() == null || subject.getLevel().getId() != newLevel.getId()) {
          
            Term termToCheck = updated.getTerm() != null ? updated.getTerm() : subject.getTerm();
            
            if (termToCheck == null) {
                throw new RuntimeException("Cannot move subject: Term is required. Please set a term first.");
            }
      
            long existingCountInNewLevel = subjectRepo.countByLevelIdAndTerm(newLevel.getId(), termToCheck);
            
            if (existingCountInNewLevel >= Level.SUBJECTS_PER_TERM) {
                throw new RuntimeException(
                    String.format("Cannot move subject to level '%s' for %s. This term already has the maximum limit of %d subjects.",
                        newLevel.getName(), 
                        termToCheck.getDisplayName(), 
                        Level.SUBJECTS_PER_TERM)
                );
            }
            subject.setLevel(newLevel);
        }
    }
    
  
    if (updated.getTeacher() != null && updated.getTeacher().getId() != null) {
        Teacher teacher = teacherRepo.findById(updated.getTeacher().getId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        subject.setTeacher(teacher);
    } else if (updated.getTeacher() != null && updated.getTeacher().getId() == null) {
        subject.setTeacher(null);
    }

    return subjectRepo.save(subject);
}


public SubjectDTO getSubjectById(int id) {
    Subject subject = subjectRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Subject not found"));
    return new SubjectDTO(subject);
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

    public List<LevelDTO> getAllLevels() {
        return levelService.getAllLevels();
    }
public List<SubjectListDTO> getAllSubjects() {
    List<Subject> subjects = subjectService.getAllSubjects();
    return subjects.stream()
            .map(SubjectListDTO::new)
            .collect(Collectors.toList());
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

public List<SubjectListDTO> getSubjectsInLevel(int levelId) {
    Level level = levelService.getLevelById(levelId);
    
    if (level == null || level.getSubjects() == null) {
        return new ArrayList<>();
    }
    
    return level.getSubjects().stream()
            .map(SubjectListDTO::new)
            .collect(Collectors.toList());
}

  @Transactional
    public void deleteStudent(Integer studentId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

    
        for (Parent parent : new ArrayList<>(student.getParents())) {
            parent.getChildren().remove(student);
            parentRepo.save(parent);
        }

    
        student.getParents().clear();
        
  
        if (student.getTeachers() != null) {
            for (Teacher teacher : new ArrayList<>(student.getTeachers())) {
                teacher.getStudents().remove(student);
                teacherRepo.save(teacher);
            }
            student.getTeachers().clear();
        }

   
        studentRepo.delete(student);
    }

    @Transactional
    public void deleteTeacher(Integer teacherId) {
        Teacher teacher = teacherRepo.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with ID: " + teacherId));


        if (teacher.getStudents() != null) {
            for (Student student : new ArrayList<>(teacher.getStudents())) {
                student.getTeachers().remove(teacher);
                studentRepo.save(student);
            }
            teacher.getStudents().clear();
        }

     
        Subject assignedSubject = teacher.getSubject();
        if (assignedSubject != null && assignedSubject.getTeacher() != null 
                && assignedSubject.getTeacher().equals(teacher)) {
            assignedSubject.setTeacher(null);
            subjectRepo.save(assignedSubject);
        }
        
        teacher.setSubject(null);
        
     
        teacherRepo.delete(teacher);
    }

    @Transactional
    public void deleteParent(Integer parentId) {
        Parent parent = parentRepo.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found with ID: " + parentId));

        for (Student student : new ArrayList<>(parent.getChildren())) {
            student.getParents().remove(parent);
            studentRepo.save(student);
        }
        
        parent.getChildren().clear();
        
        if (parent.getFamily() != null) {
            Family family = parent.getFamily();
            family.getParents().remove(parent);
            parent.setFamily(null);
            familyRepo.save(family);
        }
        
       
        parentRepo.delete(parent);
    }
@Transactional
public void addParentToFamily(int familyId, int parentId) {

    Family family = getFamilyById(familyId);
    Parent parent = getParentByIdWithoutDTO(parentId);

    if (parent.getFamily() == null || parent.getFamily().getId() != familyId) {

        parent.setFamily(family);

        if (family.getParents() == null) {
            family.setParents(new ArrayList<>());
        }

        if (family.getParents().stream().noneMatch(p -> p.getId() == parentId)) {
            family.getParents().add(parent);
        }

        parentRepo.save(parent);
        familyRepo.save(family);
    }
}
    @Transactional
    public Parent addParentWithChildren(Parent parent, List<Integer> studentIds) {
       
        if (parent.getPassword() != null && !parent.getPassword().isEmpty()) {
            if (!isStrongPassword(parent.getPassword())) {
                throw new RuntimeException(
                    "Password must be at least 12 characters long and include uppercase, lowercase, number, and special character."
                );
            }
            parent.setPassword(passwordEncoder.encode(parent.getPassword()));
        }
        
  
     
        
    
        if (studentIds != null && !studentIds.isEmpty()) {
            List<Student> students = studentRepo.findAllById(studentIds);
            if (students.size() != studentIds.size()) {
                throw new RuntimeException("Some students not found");
            }
            
            parent.setChildren(students);
          
            for (Student student : students) {
                if (student.getParents() == null) {
                    student.setParents(new ArrayList<>());
                }
                if (!student.getParents().contains(parent)) {
                    student.getParents().add(parent);
                }
                studentRepo.save(student);
            }
        } else {
            parent.setChildren(new ArrayList<>());
        }
        
        return parentRepo.save(parent);
    }

    @Transactional
    public Parent addFamilyMember(int parentId, List<Integer> studentIds) {
        Parent parent = parentRepo.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found"));

        if (studentIds != null && !studentIds.isEmpty()) {
            List<Student> students = studentRepo.findAllById(studentIds);
            if (students.size() != studentIds.size()) {
                throw new RuntimeException("Some students not found");
            }
            
       
            for (Student student : students) {
                if (!parent.getChildren().contains(student)) {
                    parent.getChildren().add(student);
                }
                if (!student.getParents().contains(parent)) {
                    student.getParents().add(parent);
                    studentRepo.save(student);
                }
            }
        }

        return parentRepo.save(parent);
    }
@Transactional
public void editParent(int parentId, Parent parent) {
    Parent existingParent = parentRepo.findById(parentId)
            .orElseThrow(() -> new RuntimeException("Parent not found with ID: " + parentId));

    if (parent.getName() != null && !parent.getName().isBlank()) {
        existingParent.setName(parent.getName().trim());
    }

 
    if (parent.getEmail() != null && !parent.getEmail().isBlank()) {
        existingParent.setEmail(parent.getEmail().trim());
    }


    if (parent.getPassword() != null && !parent.getPassword().trim().isEmpty()) {
        String rawPassword = parent.getPassword().trim();
        if (!rawPassword.isEmpty()) {
            if (!isStrongPassword(rawPassword)) {
                throw new RuntimeException(
                    "Password must be at least 8 characters long and include uppercase, lowercase, number, and special character."
                );
            }
            existingParent.setPassword(passwordEncoder.encode(rawPassword));
        }
    }
if (parent.getPhones() != null) {


    userPhoneRepo.deleteByUserId(existingParent.getId());

  
    userPhoneRepo.flush();

    
    existingParent.getPhones().clear();


    for (UserPhone phone : parent.getPhones()) {

        UserPhone newPhone = new UserPhone();
        newPhone.setPhoneNumber(phone.getPhoneNumber());
        newPhone.setPhoneType(phone.getPhoneType());
        newPhone.setUser(existingParent);

        userPhoneRepo.save(newPhone);

        existingParent.getPhones().add(newPhone);
    }
}
  
    if (parent.getChildren() != null && !parent.getChildren().isEmpty()) {
        for (Student student : parent.getChildren()) {
            Student existingStudent = studentRepo.findById(student.getId())
                    .orElseThrow(() -> new RuntimeException("Student not found: " + student.getId()));
            
            if (!existingParent.getChildren().contains(existingStudent)) {
                existingParent.getChildren().add(existingStudent);
            }
            if (!existingStudent.getParents().contains(existingParent)) {
                existingStudent.getParents().add(existingParent);
                studentRepo.save(existingStudent);
            }
        }
    }

    parentRepo.save(existingParent);
}

@Transactional
    public Family createFamily(Family family) {
        if (family.getFamilyName() == null || family.getFamilyName().trim().isEmpty()) {
            throw new RuntimeException("Family name is required");
        }
        
        if (family.getParents() == null) {
            family.setParents(new ArrayList<>());
        }
        
        return familyRepo.save(family);
    }

    @Transactional
    public Family getFamilyById(int familyId) {
        return familyRepo.findById(familyId)
                .orElseThrow(() -> new RuntimeException("Family not found with ID: " + familyId));
    }

@Transactional
public List<Map<String, Object>> getAllFamiliesWithDetails() {
    List<Family> families = familyRepo.findAll();
    List<Map<String, Object>> result = new ArrayList<>();
    
    for (Family family : families) {
        Map<String, Object> familyMap = new HashMap<>();
        familyMap.put("familyId", family.getId());
        familyMap.put("familyName", family.getFamilyName());
        
       
        List<Parent> parents = family.getParents();
        List<Map<String, Object>> parentList = new ArrayList<>();
        List<Map<String, Object>> allChildren = new ArrayList<>();
        
        if (parents != null && !parents.isEmpty()) {
            for (Parent parent : parents) {
             
                Map<String, Object> parentMap = new HashMap<>();
                parentMap.put("id", parent.getId());
                parentMap.put("name", parent.getName());
                parentMap.put("email", parent.getEmail());
                
            
                if (parent.getPhones() != null && !parent.getPhones().isEmpty()) {
                    List<Map<String, String>> phones = new ArrayList<>();
                    for (UserPhone phone : parent.getPhones()) {
                        Map<String, String> phoneMap = new HashMap<>();
                        phoneMap.put("phoneNumber", phone.getPhoneNumber());
                        phoneMap.put("phoneType", phone.getPhoneType());
                        phones.add(phoneMap);
                    }
                    parentMap.put("phones", phones);
                }
                
                parentList.add(parentMap);
                
        
                if (parent.getChildren() != null) {
                    for (Student child : parent.getChildren()) {
                        Map<String, Object> childMap = new HashMap<>();
                        childMap.put("id", child.getId());
                        childMap.put("name", child.getName());
                        childMap.put("email", child.getEmail());
                        allChildren.add(childMap);
                    }
                }
            }
        }
        
        familyMap.put("parents", parentList);
        familyMap.put("children", allChildren);
        
      
        if (!parentList.isEmpty()) {
            familyMap.put("parent", parentList.get(0));
        } else {
            familyMap.put("parent", null);
        }
        
        result.add(familyMap);
    }
    
    return result;
}@Transactional

public Parent addParentWithDetails(ParentDTO parentDTO) {
    try {
        if (parentDTO.getName() == null || parentDTO.getName().isEmpty())
            throw new RuntimeException("Parent name is required");

        if (parentDTO.getEmail() == null || parentDTO.getEmail().isEmpty())
            throw new RuntimeException("Parent email is required");

        if (parentDTO.getPassword() == null || parentDTO.getPassword().isEmpty())
            throw new RuntimeException("Password is required");

        Parent parent = new Parent();
        parent.setName(parentDTO.getName());
        parent.setEmail(parentDTO.getEmail());
        parent.setPassword(passwordEncoder.encode(parentDTO.getPassword()));
      

        if (parentDTO.getPhones() != null) {
            List<UserPhone> phones = new ArrayList<>();
            for (com.first.first_app.DTO.PhoneDTO phoneDTO : parentDTO.getPhones()) {
                UserPhone phone = new UserPhone();
                phone.setPhoneNumber(phoneDTO.getPhoneNumber());
                phone.setPhoneType(phoneDTO.getPhoneType());
                phone.setUser(parent);
                phones.add(phone);
            }
            parent.setPhones(phones);
        }

        return parentRepo.save(parent);
    } catch (Exception e) {
        throw new RuntimeException("Error adding parent: " + e.getMessage());
    }
}

public Parent getParentByIdWithoutDTO(int parentId) {  return parentRepo.findById(parentId)
            .orElseThrow(() -> new RuntimeException("Parent not found with ID: " + parentId));
}


}