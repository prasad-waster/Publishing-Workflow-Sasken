package com.sasken.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sasken.dto.DashboardStatsDTO;
import com.sasken.Model.BlogPost;
import com.sasken.Model.PostStatus;
import com.sasken.Repository.BlogPostRepository;

@Service
public class DashboardService {

    @Autowired
    private BlogPostRepository blogPostRepo;

    public DashboardStatsDTO getDashboardStats() {
        List<BlogPost> publishedPosts = blogPostRepo.findByStatus(PostStatus.PUBLISHED);

        int totalPublished = publishedPosts.size();
        int totalViews = publishedPosts.stream().mapToInt(BlogPost::getViews).sum();
        int totalLikes = publishedPosts.stream().mapToInt(BlogPost::getLikes).sum();
        int totalComments = publishedPosts.stream()
                                          .mapToInt(p -> p.getComments() != null ? p.getComments().size() : 0)
                                          .sum();

        BlogPost mostViewed = publishedPosts.stream()
                                            .max(Comparator.comparingInt(BlogPost::getViews))
                                            .orElse(null);

        BlogPost mostLiked = publishedPosts.stream()
                                           .max(Comparator.comparingInt(BlogPost::getLikes))
                                           .orElse(null);

        long newPostsThisWeek = publishedPosts.stream()
            .filter(p -> p.getCreatedAt() != null && ChronoUnit.DAYS.between(p.getCreatedAt(), LocalDateTime.now()) <= 7)
            .count();

        LocalDateTime lastPostDate = publishedPosts.stream()
            .map(BlogPost::getCreatedAt)
            .filter(d -> d != null)
            .max(LocalDateTime::compareTo)
            .orElse(null);

        DashboardStatsDTO stats = new DashboardStatsDTO();
        stats.setTotalPublishedBlogs(totalPublished);
        stats.setTotalViews(totalViews);
        stats.setTotalLikes(totalLikes);
        stats.setTotalComments(totalComments);
        stats.setMostViewedBlog(mostViewed != null ? mostViewed.getTitle() : "N/A");
        stats.setMostLikedBlog(mostLiked != null ? mostLiked.getTitle() : "N/A");
        stats.setNewPostsThisWeek((int) newPostsThisWeek);
        stats.setLastPostDate(lastPostDate != null ? lastPostDate.toLocalDate() : null);
// lastPostDate is LocalDateTime — DTO must accept this or format in controller

        return stats;
    }
}
