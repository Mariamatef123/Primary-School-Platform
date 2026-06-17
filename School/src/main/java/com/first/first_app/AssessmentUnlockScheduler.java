package com.first.first_app;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.first.first_app.Enum.Term;
import com.first.first_app.Model.Student;
import com.first.first_app.Repo.StudentRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssessmentUnlockScheduler {

    private final StudentRepo studentRepo;

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void unlockAssessments() {
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();
        int currentMonth = today.getMonthValue();
        int currentDay = today.getDayOfMonth();
        
        List<Student> lockedStudents = studentRepo.findByIsAssessmentsLockedTrue();
        
        for (Student student : lockedStudents) {
            int currentUnlockedCount = student.getUnlockedAssessmentCount();
            int nextAssessmentToUnlock = currentUnlockedCount + 1;
            
           
            boolean shouldUnlock = shouldUnlockAssessment(
                student.getCurrentTerm(), 
                nextAssessmentToUnlock,
                currentYear, 
                currentMonth, 
                currentDay
            );
            
            if (shouldUnlock) {
                student.setUnlockedAssessmentCount(nextAssessmentToUnlock);
                
               
                if (nextAssessmentToUnlock >= 6) {
                    student.setAssessmentsLocked(false);
                    System.out.println("All assessments unlocked for student: " + student.getName());
                }
                
                studentRepo.save(student);
                System.out.println("Unlocked assessment #" + nextAssessmentToUnlock + " for student: " + student.getName());
            }
        }
    }
    
    private boolean shouldUnlockAssessment(Term term, int assessmentNumber, int year, int month, int day) {
        if (term == Term.TERM_1) {
            switch (assessmentNumber) {
                case 1: return (year == 2026 && month == 6 && day >= 17);
                case 2: return (year == 2026 && month == 10 && day >= 20);
                case 3: return (year == 2026 && month == 11 && day >= 20);
                case 4: return (year == 2026 && month == 12 && day >= 20);
                case 5: return (year == 2027 && month == 1 && day >= 10);
                case 6: return (year == 2027 && month == 2 && day >= 10);
                default: return false;
            }
        } else if (term == Term.TERM_2) {
            switch (assessmentNumber) {
                case 1: return (year == 2027 && month == 2 && day >= 25);
                case 2: return (year == 2027 && month == 3 && day >= 25);
                case 3: return (year == 2027 && month == 4 && day >= 25);
                case 4: return (year == 2027 && month == 5 && day >= 20);
                case 5: return (year == 2027 && month == 5 && day >= 30);
                case 6: return (year == 2027 && month == 6 && day >= 10);
                default: return false;
            }
        }
        return false;
    }
}