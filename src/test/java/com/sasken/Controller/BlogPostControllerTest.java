package com.sasken.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sasken.Model.BlogPost;
import com.sasken.Model.PostStatus;
import com.sasken.Service.BlogPostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

@WebMvcTest(BlogPostController.class)
@AutoConfigureMockMvc(addFilters = false)
class BlogPostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlogPostService blogPostService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenCreatePost_thenReturns200() throws Exception {
        BlogPost post = new BlogPost();
        post.setTitle("New Post");
        post.setContent("Some content");
        post.setAuthorId(1L);

        BlogPost savedPost = new BlogPost();
        savedPost.setId(1L);
        savedPost.setTitle("New Post");
        savedPost.setContent("Some content");
        savedPost.setAuthorId(1L);
        savedPost.setStatus(PostStatus.DRAFT);

        when(blogPostService.createDraft(any(BlogPost.class))).thenReturn(savedPost);

        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("New Post"))
                .andExpect(jsonPath("$.status").value("DRAFT"));
    }

    @Test
    void whenCreatePostWithMissingTitle_thenReturns400() throws Exception {
        BlogPost post = new BlogPost();
        post.setContent("Content only"); // No title
        post.setAuthorId(1L);
        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenCreatePostWithMissingContent_thenReturns400() throws Exception {
        BlogPost post = new BlogPost();
        post.setTitle("Title only"); // No content
        post.setAuthorId(1L);
        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenChangeStatus_thenReturns200() throws Exception {
        BlogPost post = new BlogPost();
        post.setId(1L);
        post.setStatus(PostStatus.REVIEW);

        when(blogPostService.changeStatus(eq(1L), eq(PostStatus.REVIEW), eq(1L))).thenReturn(post);

        mockMvc.perform(put("/api/posts/1/status")
                .param("status", "REVIEW")
                .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REVIEW"));
    }

    @Test
    void whenChangeStatusWithInvalidValue_thenReturns400() throws Exception {
        mockMvc.perform(put("/api/posts/1/status")
                .param("status", "INVALID_STATUS")
                .param("userId", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGetPost_thenReturns200() throws Exception {
        BlogPost post = new BlogPost();
        post.setId(1L);
        post.setTitle("Test Post");

        when(blogPostService.getPost(1L)).thenReturn(post);

        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Post"));
    }

    @Test
    void whenGetAllPosts_thenReturns200() throws Exception {
        BlogPost post1 = new BlogPost();
        post1.setId(1L);
        post1.setTitle("Post 1");
        BlogPost post2 = new BlogPost();
        post2.setId(2L);
        post2.setTitle("Post 2");
        when(blogPostService.getAllPosts()).thenReturn(List.of(post1, post2));

        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Post 1"))
                .andExpect(jsonPath("$[1].title").value("Post 2"));
    }

    @Test
    void whenGetAllPostsEmpty_thenReturnsEmptyList() throws Exception {
        when(blogPostService.getAllPosts()).thenReturn(List.of());
        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void whenUpdatePost_thenReturns200() throws Exception {
        BlogPost updated = new BlogPost();
        updated.setId(1L);
        updated.setTitle("Updated");
        updated.setContent("Updated content");
        when(blogPostService.updatePost(eq(1L), any(BlogPost.class))).thenReturn(updated);

        mockMvc.perform(put("/api/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"));
    }

    @Test
    void whenGetPostNotFound_thenReturns404() throws Exception {
        when(blogPostService.getPost(99L)).thenThrow(new jakarta.persistence.EntityNotFoundException("Post not found"));
        mockMvc.perform(get("/api/posts/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenChangeStatusInvalidTransition_thenReturns500() throws Exception {
        when(blogPostService.changeStatus(eq(1L), eq(PostStatus.APPROVED), eq(1L)))
                .thenThrow(new IllegalStateException("Invalid status transition"));
        mockMvc.perform(put("/api/posts/1/status")
                .param("status", "APPROVED")
                .param("userId", "1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenDeletePost_thenReturns200() throws Exception {
        mockMvc.perform(delete("/api/posts/1"))
                .andExpect(status().isOk());
    }

    @Test
    void whenDeletePostNotFound_thenReturns404() throws Exception {
        doThrow(new jakarta.persistence.EntityNotFoundException("Post not found")).when(blogPostService).deletePost(99L);
        mockMvc.perform(delete("/api/posts/99"))
                .andExpect(status().isNotFound());
    }
} 