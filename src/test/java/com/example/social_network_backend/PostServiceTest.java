package com.example.social_network_backend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.social_network_backend.DTO.Post.CreatePostDTO;
import com.example.social_network_backend.DTO.Post.UpdatePostDTO;
import com.example.social_network_backend.Entities.Post;
import com.example.social_network_backend.Entities.User;
import com.example.social_network_backend.Repositories.PostRepository;
import com.example.social_network_backend.Repositories.UserRepository;
import com.example.social_network_backend.Services.PostService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostService postService;
    private User user;
    private Post post;
    private CreatePostDTO createPostDTO;
    private UpdatePostDTO updatePostDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        post = new Post();
        post.setId(1L);
        post.setText("Initial post");
        post.setCreator(user);

        createPostDTO = new CreatePostDTO("New post", "base64image");
        updatePostDTO = new UpdatePostDTO("Updated post", "updatedBase64image");
    }

    @Test
    void createPost_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post createdPost = postService.createPost(createPostDTO, 1L);

        assertNotNull(createdPost);
        assertEquals(post.getText(), createdPost.getText());
        verify(postRepository).save(any(Post.class));
    }

    @Test
    void createPost_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> postService.createPost(createPostDTO, 1L));
    }

    @Test
    void getPostById_Success() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        Post foundPost = postService.getPostById(1L);
        assertNotNull(foundPost);
        assertEquals(post.getId(), foundPost.getId());
    }

    @Test
    void getPostById_NotFound() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> postService.getPostById(1L));
    }

    @Test
    void getAllPosts_Success() {
        when(postRepository.findAll()).thenReturn(List.of(post));
        List<Post> posts = postService.getAllPosts();
        assertEquals(1, posts.size());
    }

    @Test
    void getUserPosts_Success() {
        Pageable pageable = mock(Pageable.class);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(postRepository.findByCreator(user, pageable)).thenReturn(List.of(post));
        List<Post> posts = postService.getUserPosts(1L, pageable);

        assertEquals(1, posts.size());
        assertEquals(post, posts.get(0));
    }

    @Test
    void updatePost_Success() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post updatedPost = postService.updatePost(updatePostDTO, 1L);
        assertNotNull(updatedPost);
        assertEquals(updatePostDTO.text(), updatedPost.getText());
    }

    @Test
    void updatePost_PostNotFound() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> postService.updatePost(updatePostDTO, 1L));
    }

    @Test
    void deletePost_Success() {
        when(postRepository.existsById(1L)).thenReturn(true);
        doNothing().when(postRepository).deleteById(1L);

        postService.deletePost(1L);

        verify(postRepository).deleteById(1L);
    }
}