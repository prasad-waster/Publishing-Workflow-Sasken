package com.sasken.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sasken.Model.BlogPost;
import com.sasken.Model.PostStatus;
import com.sasken.Repository.BlogPostRepository;
import com.sasken.dto.DashboardStatsDTO;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BlogPostService {

    @Autowired
    private BlogPostRepository blogPostRepo;

    @Autowired
    private DashboardService dashboardService;

    public BlogPost createDraft(BlogPost post) {
        post.setStatus(PostStatus.DRAFT);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        return blogPostRepo.save(post);
    }

    public BlogPost changeStatus(Long postId, PostStatus newStatus, Long userId) {
        BlogPost post = blogPostRepo.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        PostStatus currentStatus = post.getStatus();

        if (isValidTransition(currentStatus, newStatus)) {
            post.setStatus(newStatus);
            post.setUpdatedAt(LocalDateTime.now());
            return blogPostRepo.save(post);
        } else {
            throw new IllegalStateException("Invalid status transition from " + currentStatus + " to " + newStatus);
        }
    }

    private boolean isValidTransition(PostStatus current, PostStatus next) {
        return (current == PostStatus.DRAFT && next == PostStatus.REVIEW) ||
               (current == PostStatus.REVIEW && (next == PostStatus.APPROVED || next == PostStatus.DRAFT)) ||
               (current == PostStatus.APPROVED && (next == PostStatus.PUBLISHED || next == PostStatus.REVIEW));
    }

    public List<BlogPost> getAllPosts() {
        return blogPostRepo.findAll();
    }

    public BlogPost getPost(Long postId) {
        return blogPostRepo.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
    }

    public void deletePost(Long postId) {
        blogPostRepo.deleteById(postId);
    }

    public BlogPost updatePost(Long postId, BlogPost updatedPost) {
        BlogPost existing = blogPostRepo.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        existing.setTitle(updatedPost.getTitle());
        existing.setContent(updatedPost.getContent());
        existing.setUpdatedAt(LocalDateTime.now());

        return blogPostRepo.save(existing);
    }

    // ✅ Support content-dashboard for published posts
    public List<BlogPost> getPublishedPosts() {
        return blogPostRepo.findByStatus(PostStatus.PUBLISHED);
    }

    // ✅ Increment likes
    public BlogPost incrementLikes(Long postId) {
        BlogPost post = getPost(postId);
        post.setLikes(post.getLikes() + 1);
        return blogPostRepo.save(post);
    }

    // ✅ Add comment
    public BlogPost addComment(Long postId, String comment) {
        BlogPost post = getPost(postId);
        if (post.getComments() == null) {
            post.setComments(new ArrayList<>());
        }
        post.getComments().add(comment);
        return blogPostRepo.save(post);
    }

    public List<BlogPost> searchPublishedPosts(String query) {
        return blogPostRepo.findByStatusAndTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
            PostStatus.PUBLISHED, query, query
        );
    }

    public BlogPost incrementViews(Long postId) {
        BlogPost post = blogPostRepo.findById(postId)
            .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setViews(post.getViews() + 1);
        post.setUpdatedAt(LocalDateTime.now());

        return blogPostRepo.save(post);
    }

    // ✅ Get dashboard stats
    public DashboardStatsDTO getDashboardStats() {
    return dashboardService.getDashboardStats();
}

}
