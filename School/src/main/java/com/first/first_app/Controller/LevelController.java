package com.first.first_app.Controller;

import com.first.first_app.Model.Level;
import com.first.first_app.Service.LevelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/levels")
public class LevelController {

    private final LevelService levelService;

    public LevelController(LevelService levelService) {
        this.levelService = levelService;
    }

    @PostMapping
    public Level createLevel(@RequestBody Level level) {
        return levelService.createLevel(level);
    }

    @GetMapping
    public List<Level> getAllLevels() {
        return levelService.getAllLevels();
    }

    @GetMapping("/{levelId}")
    public Level getLevelById(@PathVariable int levelId) {
        return levelService.getLevelById(levelId);
    }

    @PostMapping("/{levelId}/assign-subject/{subjectId}")
    public Level assignSubject(@PathVariable int levelId, @PathVariable int subjectId) {
        return levelService.assignSubjectToLevel(levelId, subjectId);
    }

    @DeleteMapping("/{levelId}/remove-subject/{subjectId}")
    public Level removeSubject(@PathVariable int levelId, @PathVariable int subjectId) {
        return levelService.removeSubjectFromLevel(levelId, subjectId);
    }
}
