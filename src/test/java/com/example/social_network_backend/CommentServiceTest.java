package com.example.social_network_backend;

import com.example.social_network_backend.Entities.Comment;
import com.example.social_network_backend.Entities.Post;
import com.example.social_network_backend.Entities.User;
import com.example.social_network_backend.Repositories.CommentRepository;
import com.example.social_network_backend.Repositories.PostRepository;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private CommentService commentService;

    private User user;
    private Post post;
    private Comment comment;

    @BeforeEach
    void setUp() { //before tests
        comment = new Comment();
        comment.setId(1L);
        comment.setText("Initial comment");

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        post = new Post();
        post.setId(1L);
        post.setText("Initial post");
        post.setCreator(user);

    }

    @Test
    void createComment_Success() {
        when(commentRepository.save(any(Comment.class))).thenReturn(comment); //WHEN we use this method  in this case THEN RETURN what we prepared
        Comment createdComment = commentService.createComment(comment);  //test method

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
    void updateComment_Success() {
        Comment newCommentData = new Comment(); //Comment with updating data
        newCommentData.setText("Updated comment");

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment)); //before updating-comment
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment updatedComment = commentService.updateComment(1L, newCommentData);

        assertNotNull(updatedComment);
        assertEquals("Updated comment", comment.getText()); // check initial comment
        verify(commentRepository).findById(1L);
        verify(commentRepository).save(any(Comment.class));
    }


    @Test
    void updateComment_ThrowsException_WhenNotFound() {
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> commentService.updateComment(1L, comment));
        verify(commentRepository).findById(1L);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void deleteComment_Success() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        doNothing().when(commentRepository).deleteById(1L);

        commentService.deleteComment(1L);

        verify(commentRepository).deleteById(1L);
    }
}