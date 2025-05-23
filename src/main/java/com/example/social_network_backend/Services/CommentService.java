package com.example.social_network_backend.Services;

import com.example.social_network_backend.Entities.Comment;
import com.example.social_network_backend.Repositories.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public Comment createComment(Comment comment) {
        commentRepository.save(comment);
        return comment;
    }

    public Comment getCommentById(Long id) {
        return commentRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Page<Comment> getAllComments(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }

    @Transactional
    public Comment updateComment(Long id, Comment comment) {
       Comment existingComment = commentRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        existingComment.setText(comment.getText());
        return commentRepository.save(existingComment);
    }

    @Transactional
    public void deleteComment(Long id) {
        commentRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        commentRepository.deleteById(id);
    }
}