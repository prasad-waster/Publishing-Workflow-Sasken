package com.sasken.Service;

import com.sasken.Model.BlogPost;
import com.sasken.Model.PostStatus;
import com.sasken.Repository.BlogPostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BlogPostServiceTest {

    @Mock
    private BlogPostRepository blogPostRepo;

    @InjectMocks
    private BlogPostService blogPostService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateDraftSetsStatusAndTimestamps() {
        BlogPost post = BlogPost.builder().title("Test").content("Content").authorId(1L).build();
        when(blogPostRepo.save(any(BlogPost.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BlogPost result = blogPostService.createDraft(post);

        assertNotNull(result);
        assertEquals(PostStatus.DRAFT, result.getStatus());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        verify(blogPostRepo).save(any(BlogPost.class));
    }

    @Test
    void testChangeStatusValidTransition() {
        BlogPost post = BlogPost.builder().id(1L).status(PostStatus.DRAFT).build();
        when(blogPostRepo.findById(1L)).thenReturn(Optional.of(post));
        when(blogPostRepo.save(any(BlogPost.class))).thenReturn(post);

        BlogPost result = blogPostService.changeStatus(1L, PostStatus.REVIEW, 1L);
        assertEquals(PostStatus.REVIEW, result.getStatus());
        verify(blogPostRepo).save(any(BlogPost.class));
    }

    @Test
    void testChangeStatusInvalidTransitionThrows() {
        BlogPost post = BlogPost.builder().id(1L).status(PostStatus.DRAFT).build();
        when(blogPostRepo.findById(1L)).thenReturn(Optional.of(post));
        assertThrows(IllegalStateException.class, () ->
            blogPostService.changeStatus(1L, PostStatus.APPROVED, 1L)
        );
    }

    @Test
    void testGetPostFound() {
        BlogPost post = BlogPost.builder().id(1L).title("Test").build();
        when(blogPostRepo.findById(1L)).thenReturn(Optional.of(post));
        BlogPost result = blogPostService.getPost(1L);
        assertEquals("Test", result.getTitle());
    }

    @Test
    void testGetPostNotFoundThrows() {
        when(blogPostRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () ->
            blogPostService.getPost(1L)
        );
    }

    @Test
    void testGetAllPosts() {
        BlogPost post = BlogPost.builder().id(1L).build();
        when(blogPostRepo.findAll()).thenReturn(Collections.singletonList(post));
        List<BlogPost> result = blogPostService.getAllPosts();
        assertEquals(1, result.size());
    }

    @Test
    void testUpdatePost() {
        BlogPost existing = BlogPost.builder().id(1L).title("Old").content("Old").build();
        BlogPost updated = BlogPost.builder().title("New").content("New").build();
        when(blogPostRepo.findById(1L)).thenReturn(Optional.of(existing));
        when(blogPostRepo.save(any(BlogPost.class))).thenReturn(existing);
        BlogPost result = blogPostService.updatePost(1L, updated);
        assertEquals("New", result.getTitle());
        assertEquals("New", result.getContent());
    }

    @Test
    void testDeletePost() {
        doNothing().when(blogPostRepo).deleteById(1L);
        blogPostService.deletePost(1L);
        verify(blogPostRepo).deleteById(1L);
    }
} 