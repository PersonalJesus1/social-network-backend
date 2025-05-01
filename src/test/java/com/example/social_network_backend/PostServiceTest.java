package com.example.social_network_backend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.social_network_backend.DTO.Post.CreatePostDTO;
import com.example.social_network_backend.DTO.Post.UpdatePostDTO;
import com.example.social_network_backend.Entities.Image;
import com.example.social_network_backend.Entities.Post;
import com.example.social_network_backend.Entities.User;
import com.example.social_network_backend.Repositories.PostRepository;
import com.example.social_network_backend.Repositories.UserRepository;
import com.example.social_network_backend.Services.FileService;
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

    @Mock
    private FileService fileService;
    @InjectMocks
    private PostService postService;

    private User user;
    private Post post;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        post = new Post();
        post.setId(1L);
        post.setText("Initial post");
        post.setCreator(user);
        Image image = new Image();
        image.setImagePath("some/path/to/image.jpg");

        post.setImage(image);
    }

    @Test
    void createPost_Success() {
        // Arrange
        Post inputPost = new Post();
        inputPost.setText("Hello world");

        Image dummyImage = new Image();
        dummyImage.setImagePath("base64EncodedImageString");
        inputPost.setImage(dummyImage);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(fileService.saveImage(anyString(), eq(user.getEmail()))).thenReturn(dummyImage);
        when(postRepository.save(any(Post.class))).thenReturn(inputPost);

        // Act
        Post created = postService.createPost(1L, inputPost);

        // Assert
        assertNotNull(created);
        assertEquals("Hello world", created.getText());
        verify(fileService).deleteFile(anyString());
        verify(fileService).saveImage(anyString(), eq(user.getEmail()));
        verify(postRepository).save(any(Post.class));
    }
    @Test
    void createPost_UserNotFound_ShouldThrowException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Post inputPost = new Post();
        inputPost.setText("Test");
        inputPost.setImage(new Image());

        assertThrows(EntityNotFoundException.class, () -> postService.createPost(99L, inputPost));
    }


    @Test
    void createPost_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> postService.createPost(1L, post));
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
        // Arrange
        Post updatedPost = new Post();
        updatedPost.setText("Updated text");

        Image updatedImage = new Image();
        updatedImage.setImagePath("newBase64ImageString");
        updatedPost.setImage(updatedImage);
        updatedPost.setCreator(user);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(fileService.saveImage(anyString(), eq(user.getEmail()))).thenReturn(updatedImage);
        when(postRepository.save(any(Post.class))).thenReturn(updatedPost);

        // Act
        Post result = postService.updatePost(1L, updatedPost);

        // Assert
        assertNotNull(result);
        assertEquals("Updated text", result.getText());
        verify(fileService).deleteFile(anyString());
        verify(fileService).saveImage(anyString(), eq(user.getEmail()));
        verify(postRepository).save(any(Post.class));
    }
    @Test
    void updatePost_PostNotFound_ShouldThrowException() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        Post updatedPost = new Post();
        updatedPost.setText("Updated text");
        updatedPost.setImage(new Image());
        updatedPost.setCreator(user);

        assertThrows(EntityNotFoundException.class, () -> postService.updatePost(1L, updatedPost));
    }

    @Test
    void updatePost_PostNotFound() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> postService.updatePost(1L, post));
    }

    @Test
    void deletePost_Success() {
        when(postRepository.existsById(1L)).thenReturn(true);
        doNothing().when(postRepository).deleteById(1L);

        postService.deletePost(1L);

        verify(postRepository).deleteById(1L);
    }
}