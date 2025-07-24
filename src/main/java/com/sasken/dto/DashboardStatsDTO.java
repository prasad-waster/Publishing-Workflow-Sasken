package com.sasken.dto;

import java.time.LocalDate;

public class DashboardStatsDTO {

    private int totalPublishedBlogs;
    private long totalViews;
    private long totalLikes;
    private long totalComments;
    private String mostViewedBlog;
    private String mostLikedBlog;
    private int newPostsThisWeek;
    private LocalDate lastPostDate;

    // Default constructor
    public DashboardStatsDTO() {
    }

    // Parameterized constructor
    public DashboardStatsDTO(int totalPublishedBlogs, long totalViews, long totalLikes,
                             long totalComments, String mostViewedBlog, String mostLikedBlog,
                             int newPostsThisWeek, LocalDate lastPostDate) {
        this.totalPublishedBlogs = totalPublishedBlogs;
        this.totalViews = totalViews;
        this.totalLikes = totalLikes;
        this.totalComments = totalComments;
        this.mostViewedBlog = mostViewedBlog;
        this.mostLikedBlog = mostLikedBlog;
        this.newPostsThisWeek = newPostsThisWeek;
        this.lastPostDate = lastPostDate;
    }

    public int getTotalPublishedBlogs() {
        return totalPublishedBlogs;
    }

    public void setTotalPublishedBlogs(int totalPublishedBlogs) {
        this.totalPublishedBlogs = totalPublishedBlogs;
    }

    public long getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(long totalViews) {
        this.totalViews = totalViews;
    }

    public long getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(long totalLikes) {
        this.totalLikes = totalLikes;
    }

    public long getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(long totalComments) {
        this.totalComments = totalComments;
    }

    public String getMostViewedBlog() {
        return mostViewedBlog;
    }

    public void setMostViewedBlog(String mostViewedBlog) {
        this.mostViewedBlog = mostViewedBlog;
    }

    public String getMostLikedBlog() {
        return mostLikedBlog;
    }

    public void setMostLikedBlog(String mostLikedBlog) {
        this.mostLikedBlog = mostLikedBlog;
    }

    public int getNewPostsThisWeek() {
        return newPostsThisWeek;
    }

    public void setNewPostsThisWeek(int newPostsThisWeek) {
        this.newPostsThisWeek = newPostsThisWeek;
    }

    public LocalDate getLastPostDate() {
        return lastPostDate;
    }

    public void setLastPostDate(LocalDate lastPostDate) {
        this.lastPostDate = lastPostDate;
    }
}
