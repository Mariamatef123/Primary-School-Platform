package com.first.first_app;

import java.time.LocalDate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.first.first_app.Enum.StudentStatus;
import com.first.first_app.Model.Student;
import com.first.first_app.Repo.StudentRepo;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UnlockScheduler {

    private final StudentRepo studentRepo;

    @Scheduled(cron = "0 15 9 12 6 *")
    @Transactional
    public void unlockStudents() {

       
        if (LocalDate.now().getYear() != 2026) {
            return;
        }

        var students = studentRepo.findByStatus(StudentStatus.PASSED);

        for (Student student : students) {
            student.setLocked(false);
            student.setStatus(StudentStatus.PASSED);
        }

        studentRepo.saveAll(students);

        System.out.println("students unlocked successfully.");
    }
}