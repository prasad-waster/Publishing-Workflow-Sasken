package com.blogcraft.controller;

import com.blogcraft.model.Comment;
import com.blogcraft.model.Draft;
import com.blogcraft.repository.CommentRepository;
import com.blogcraft.repository.DraftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collab")
@CrossOrigin(origins = "*") // allows frontend calls from any domain
public class CollaborationController {

    @Autowired
    private DraftRepository draftRepository;

    @Autowired
    private CommentRepository commentRepository;

    // ---------- DRAFT APIs ----------

    @PostMapping("/drafts")
    public Draft createDraft(@RequestBody Draft draft) {
        return draftRepository.save(draft);
    }

    @GetMapping("/drafts")
    public List<Draft> getAllDrafts() {
        return draftRepository.findAll();
    }

    @GetMapping("/drafts/{id}")
    public Draft getDraftById(@PathVariable Long id) {
        return draftRepository.findById(id).orElse(null);
    }

    // ---------- COMMENT APIs ----------

    @PostMapping("/comments")
    public Comment createComment(@RequestBody Comment comment) {
        return commentRepository.save(comment);
    }

    @GetMapping("/comments")
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }
}

