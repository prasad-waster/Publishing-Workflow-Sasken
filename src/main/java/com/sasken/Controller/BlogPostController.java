package com.sasken.Controller;

import java.util.List;

import com.sasken.dto.CommentRequest;
import com.sasken.dto.DashboardStatsDTO;
import com.sasken.Model.BlogPost;
import com.sasken.Model.PostStatus;
import com.sasken.Service.BlogPostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin("*")
@Tag(name = "Blog Posts", description = "Blog post management endpoints")
public class BlogPostController {

    private final BlogPostService blogPostService;

    public BlogPostController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    @Autowired
    private BlogPostService service;

    @PostMapping
    @Operation(summary = "Create a new blog post",
        description = "Creates a new blog post and saves it as a draft. The post will be assigned to the specified author.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Post created successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = BlogPost.class),
                examples = @ExampleObject(value = """
                    {
                      "id": 1,
                      "title": "Getting Started with Spring Boot",
                      "content": "Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications...",
                      "status": "DRAFT",
                      "createdAt": "2024-01-15T10:30:00",
                      "updatedAt": "2024-01-15T10:30:00",
                      "authorId": 1
                    }
                """))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<BlogPost> createPost(@Valid @RequestBody BlogPost post) {
        return ResponseEntity.ok(service.createDraft(post));
    }

    @PutMapping("/{postId}/status")
    @Operation(summary = "Change post status",
        description = "Changes the status of a blog post. Valid transitions: DRAFT → REVIEW → APPROVED → PUBLISHED")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status changed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid status transition"),
        @ApiResponse(responseCode = "404", description = "Post not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<BlogPost> changeStatus(
        @Parameter(description = "ID of the post to update", required = true) @PathVariable Long postId,
        @Parameter(description = "New status for the post", required = true,
            schema = @Schema(allowableValues = {"DRAFT", "REVIEW", "APPROVED", "PUBLISHED"})) @RequestParam PostStatus status,
        @Parameter(description = "ID of the user making the change", required = true) @RequestParam Long userId) {
        return ResponseEntity.ok(service.changeStatus(postId, status, userId));
    }

    @GetMapping
    @Operation(summary = "Get all blog posts", description = "Retrieves a list of all blog posts in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Posts retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<BlogPost>> getAll() {
        return ResponseEntity.ok(service.getAllPosts());
    }

    @GetMapping("/{postId}")
    @Operation(summary = "Get a specific blog post", description = "Retrieves a specific blog post by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Post retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Post not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<BlogPost> getOne(@Parameter(description = "ID of the post to retrieve", required = true)
                                           @PathVariable Long postId) {
        return ResponseEntity.ok(service.getPost(postId));
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "Delete a blog post", description = "Permanently deletes a blog post from the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Post deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Post not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> deletePost(@Parameter(description = "ID of the post to delete", required = true)
                                             @PathVariable Long postId) {
        service.deletePost(postId);
        return ResponseEntity.ok("Deleted");
    }

    @PutMapping("/{postId}")
    @Operation(summary = "Update a blog post", description = "Updates an existing blog post with new content")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Post updated successfully"),
        @ApiResponse(responseCode = "404", description = "Post not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<BlogPost> updatePost(@Parameter(description = "ID of the post to update", required = true)
                                               @PathVariable Long postId,
                                               @Parameter(description = "Updated blog post data", required = true)
                                               @RequestBody BlogPost updatedPost) {
        return ResponseEntity.ok(service.updatePost(postId, updatedPost));
    }

    // Exception Handlers
    @ExceptionHandler(jakarta.persistence.EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNotFound() {}

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleIllegalState() {}

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleValidationExceptions() {}

    @PostMapping("/{postId}/like")
    @Operation(summary = "Like a blog post", description = "Increments the like count for a blog post")
    public ResponseEntity<BlogPost> likePost(@PathVariable Long postId) {
        BlogPost updatedPost = service.incrementLikes(postId);
        return ResponseEntity.ok(updatedPost);
    }

    @PostMapping("/{postId}/comment")
    @Operation(summary = "Add a comment to a blog post", description = "Adds a new comment to the specified blog post")
    public ResponseEntity<BlogPost> addComment(@PathVariable Long postId,
                                               @RequestBody CommentRequest commentRequest) {
        BlogPost updatedPost = service.addComment(postId, commentRequest.getComment());
        return ResponseEntity.ok(updatedPost);
    }

    @PutMapping("/{id}/view")
    public ResponseEntity<BlogPost> incrementViews(@PathVariable Long id) {
        BlogPost updated = service.incrementViews(id);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/search")
    public List<BlogPost> searchPosts(@RequestParam String query) {
        return blogPostService.searchPublishedPosts(query);
    }

    // ✅ New endpoint for dashboard stats (summary cards)
    @GetMapping("/dashboard/stats")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        return ResponseEntity.ok(service.getDashboardStats());
    }
}
