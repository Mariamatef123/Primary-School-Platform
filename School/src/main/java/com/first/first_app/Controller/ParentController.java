package com.first.first_app.Controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.first.first_app.Service.ParentService;

@RestController
@RequestMapping("/api/parents")
public class ParentController {

    private final ParentService parentService;

    public ParentController(ParentService parentService) {
        this.parentService = parentService;
    }

    @GetMapping("/{parentId}/children")
    
    public ResponseEntity<?> getChildren(@PathVariable int parentId) {
        try {
            return ResponseEntity.ok(parentService.getChildren(parentId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{parentId}/children/{childId}/subjects")
    public ResponseEntity<?> getChildSubjects(
            @PathVariable int parentId,
            @PathVariable int childId) {
        try {
            return ResponseEntity.ok(parentService.getChildSubjects(parentId, childId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", e.getMessage()));
        }
    }


    @GetMapping("/{parentId}/children/{childId}/subjects/{subjectId}/assessments")
    public ResponseEntity<?> getChildAssessments(
            @PathVariable int parentId,
            @PathVariable int childId,
            @PathVariable int subjectId) {
        try {
            return ResponseEntity.ok(parentService.getChildAssessments(parentId, childId, subjectId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{parentId}/children/{childId}/assessments/{assessmentId}/score")
    public ResponseEntity<?> getChildScore(
            @PathVariable int parentId,
            @PathVariable int childId,
            @PathVariable int assessmentId) {
        try {
            return ResponseEntity.ok(parentService.getChildScore(parentId, childId, assessmentId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllParents() {
        try {
            return ResponseEntity.ok(parentService.getAllParents());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

}
