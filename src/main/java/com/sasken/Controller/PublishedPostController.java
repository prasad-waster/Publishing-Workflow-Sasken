package com.sasken.Controller;

import com.sasken.Model.BlogPost;
import com.sasken.Service.BlogPostService;
import com.sasken.Model.PostStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
public class PublishedPostController {

    @Autowired
    private BlogPostService blogPostService;

    // ✅ Return only PUBLISHED posts
    @GetMapping("/published")
public List<BlogPost> getPublishedPosts() {
    // Call the service method that fetches published posts directly from DB
    return blogPostService.getPublishedPosts();
}

}
