package com.sasken.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sasken.Model.BlogPost;
import com.sasken.Model.PostStatus;
import com.sasken.Service.BlogPostService;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin("*")
public class BlogPostController {

    @Autowired
    private BlogPostService service;

    @PostMapping
    public ResponseEntity<BlogPost> createPost(@RequestBody BlogPost post) {
        return ResponseEntity.ok(service.createDraft(post));
    }

    @PutMapping("/{postId}/status")
    public ResponseEntity<BlogPost> changeStatus(
            @PathVariable Long postId,
            @RequestParam PostStatus status,
            @RequestParam Long userId
    ) {
        return ResponseEntity.ok(service.changeStatus(postId, status, userId));
    }

    @GetMapping
    public ResponseEntity<List<BlogPost>> getAll() {
        return ResponseEntity.ok(service.getAllPosts());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<BlogPost> getOne(@PathVariable Long postId) {
        return ResponseEntity.ok(service.getPost(postId));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId) {
    service.deletePost(postId);
        return ResponseEntity.ok("Deleted");
    }

    @PutMapping("/{postId}")
    public ResponseEntity<BlogPost> updatePost(
        @PathVariable Long postId,
        @RequestBody BlogPost updatedPost) {
    return ResponseEntity.ok(service.updatePost(postId, updatedPost));
    }


}