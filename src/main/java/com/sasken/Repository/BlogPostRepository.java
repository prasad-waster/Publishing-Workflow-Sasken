package com.sasken.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sasken.Model.BlogPost;
import com.sasken.Model.PostStatus;
import java.util.List;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    List<BlogPost> findByStatus(PostStatus status);
    List<BlogPost> findByStatusAndTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
    PostStatus status, String title, String content);


}
