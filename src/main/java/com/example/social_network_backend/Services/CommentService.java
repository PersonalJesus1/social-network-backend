package com.example.social_network_backend.Services;

import com.example.social_network_backend.DTO.Comment.CreateCommentDTO;
import com.example.social_network_backend.DTO.Comment.UpdateCommentDTO;
import com.example.social_network_backend.Entities.Comment;
import com.example.social_network_backend.Repositories.CommentRepository;
import com.example.social_network_backend.Repositories.PostRepository;
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
    private final PostRepository postRepository;

    @Transactional
    public Comment createComment(CreateCommentDTO dto) {
        Comment comment = mapToEntity(dto);
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
    public Comment updateComment(Long id, UpdateCommentDTO dto) {
        Comment comment = commentRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        comment.setText(dto.text());
        commentRepository.save(comment);
        return comment;
    }

    @Transactional
    public void deleteComment(Long id) {
        commentRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        commentRepository.deleteById(id);
    }

    private Comment mapToEntity(CreateCommentDTO dto) {
        Comment comment = new Comment();
        comment.setText(dto.text());
        comment.setPost(postRepository.getPostById(dto.postId()));
        return comment;
    }
}