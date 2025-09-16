package com.example.social_network_backend;

import com.example.social_network_backend.Entities.Comment;
import com.example.social_network_backend.Entities.Post;
import com.example.social_network_backend.Entities.User;
import com.example.social_network_backend.Exceptions.AccessDeniedException;
import com.example.social_network_backend.Repositories.CommentRepository;
import com.example.social_network_backend.Services.CommentService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    private User user;
    private Post post;
    private Comment comment;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() { //before tests
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        comment = new Comment();
        comment.setId(1L);
        comment.setText("Initial comment");
        comment.setCreator(user);

        post = new Post();
        post.setId(1L);
        post.setText("Initial post");
        post.setCreator(user);
    }

    @Test
    void createComment_Success() {
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        Comment createdComment = commentService.createComment(comment);

        assertNotNull(createdComment);// should not be null
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void getCommentById_Success() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        Comment foundComment = commentService.getCommentById(1L);

        assertNotNull(foundComment);
        assertEquals(1L, foundComment.getId());
        verify(commentRepository).findById(1L);
    }

    @Test
    void getCommentById_ThrowsException_WhenNotFound() {
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> commentService.getCommentById(1L));
        verify(commentRepository).findById(1L);
    }

    @Test
    void getAllComments_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> page = new PageImpl<>(List.of(comment));
        when(commentRepository.findAll(pageable)).thenReturn(page);

        Page<Comment> comments = commentService.getAllComments(pageable);

        assertNotNull(comments);
        assertEquals(1, comments.getTotalElements());
        verify(commentRepository).findAll(pageable);
    }

    @Test
    void updateComment_Success_WhenUserIsCreator() {
        Comment newCommentData = new Comment();
        newCommentData.setText("Updated comment");

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(authentication.getName()).thenReturn("test@example.com");

        Comment updatedComment = commentService.updateComment(1L, newCommentData, authentication);

        assertNotNull(updatedComment);
        assertEquals("Updated comment", comment.getText());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void updateComment_ThrowsAccessDenied_WhenUserIsNotCreator() {
        Comment newCommentData = new Comment();
        newCommentData.setText("Updated comment");

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(authentication.getName()).thenReturn("other@example.com");

        assertThrows(AccessDeniedException.class,
                () -> commentService.updateComment(1L, newCommentData, authentication));

        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void updateComment_ThrowsException_WhenNotFound() {
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> commentService.updateComment(1L, comment, authentication));

        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void deleteComment_Success() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        commentService.deleteComment(1L);

        verify(commentRepository).deleteById(1L);
    }

    @Test
    void deleteComment_ThrowsException_WhenNotFound() {
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> commentService.deleteComment(1L));

        verify(commentRepository, never()).deleteById(anyLong());
    }
}