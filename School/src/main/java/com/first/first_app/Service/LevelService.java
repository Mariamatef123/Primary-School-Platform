package com.first.first_app.Service;

import com.first.first_app.Model.Level;
import com.first.first_app.Model.Subject;
import com.first.first_app.Repo.LevelRepo;
import com.first.first_app.Repo.SubjectRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LevelService {

    private final LevelRepo levelRepo;
    private final SubjectRepo subjectRepo;

    public LevelService(LevelRepo levelRepo, SubjectRepo subjectRepo) {
        this.levelRepo = levelRepo;
        this.subjectRepo = subjectRepo;
    }

    // check if exceed the no of level that req or not
    public Level createLevel(Level level) {

        long currentLevelCount = levelRepo.count();

        if (currentLevelCount >= Level.LEVEL_COUNT) {
            throw new IllegalArgumentException(
                    "Maximum level capacity (" + Level.LEVEL_COUNT + ") reached. Cannot create more levels.");
        }

        return levelRepo.save(level);
    }

    public List<Level> getAllLevels() {
        return levelRepo.findAll();
    }

    public Level getLevelById(int levelId) {
        return levelRepo.findById(levelId)
                .orElseThrow(() -> new IllegalArgumentException("Level not found with ID: " + levelId));
    }

    // check if exists and not exceed the no of subjects in that level and save it
    public Level assignSubjectToLevel(int levelId, int subjectId) {
        Level level = getLevelById(levelId);
        Subject subject = subjectRepo.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found with ID: " + subjectId));

        if (level.getSubjects().contains(subject)) {

            return level;
        }

        if (level.getSubjects().size() >= Level.SUBJECT_COUNT) {
            throw new IllegalArgumentException(
                    "Maximum number of subjects (" + Level.SUBJECT_COUNT + ") reached for Level ID: " + levelId);
        }

        level.addSubject(subject);
        return levelRepo.save(level);
    }

    // check if exists remove the subject from level and save
    public Level removeSubjectFromLevel(int levelId, int subjectId) {
        Level level = getLevelById(levelId);
        Subject subject = subjectRepo.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found with ID: " + subjectId));

        level.removeSubject(subject);
        return levelRepo.save(level);
    }

    public Level editLevel(int levelId, Level updatedLevel) {
        Level existingLevel = getLevelById(levelId);

        if (updatedLevel.getName() != null && !updatedLevel.getName().isEmpty()) {
            existingLevel.setName(updatedLevel.getName());
        }

        return levelRepo.save(existingLevel);
    }

    public void deleteLevel(int levelId) {
        if (!levelRepo.existsById(levelId)) {
            throw new IllegalArgumentException("Level not found with ID: " + levelId);
        }
        levelRepo.deleteById(levelId);
    }
}