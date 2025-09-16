package com.example.social_network_backend.Facades;

import com.example.social_network_backend.DTO.Comment.CreateCommentDTO;
import com.example.social_network_backend.DTO.Comment.ResponseCommentDTO;
import com.example.social_network_backend.DTO.Comment.ResponseUpdatedCommentDTO;
import com.example.social_network_backend.DTO.Comment.UpdateCommentDTO;
import com.example.social_network_backend.Entities.Comment;
import com.example.social_network_backend.Repositories.PostRepository;
import com.example.social_network_backend.Repositories.UserRepository;
import com.example.social_network_backend.Services.CommentService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@Service
public class CommentFacade {

    private final CommentService commentService;
    private final PostRepository postRepository;
    private final Validator validator;
    private final UserRepository userRepository;

    public ResponseCommentDTO createComment(CreateCommentDTO dto) {
        validate(dto);
        Comment comment = commentService.createComment(mapToEntity(dto));
        return mapToResponseDto(comment);
    }

    public ResponseCommentDTO getCommentById(Long id) {
        Comment comment = commentService.getCommentById(id);
        return mapToResponseDto(comment);
    }

    public List<ResponseCommentDTO> getAllComments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return commentService.getAllComments(pageable)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public ResponseUpdatedCommentDTO updateComment(Long id, UpdateCommentDTO dto, Authentication authentication) {
        validate(dto);
        Comment comment = new Comment();
        comment.setText(dto.text());
        Comment updatedComment = commentService.updateComment(id, comment, authentication);
        return mapToUpdatedResponseDto(updatedComment);
    }

    public void deleteComment(Long id) {
        commentService.deleteComment(id);
    }

    private void validate(Object dto) {
        Set<ConstraintViolation<Object>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private ResponseCommentDTO mapToResponseDto(Comment comment) {
        return new ResponseCommentDTO(comment.getId(), comment.getText(), comment.getCreatedDate());
    }

    private ResponseUpdatedCommentDTO mapToUpdatedResponseDto(Comment comment) {
        return new ResponseUpdatedCommentDTO(comment.getId(), comment.getText(), comment.getUpdatedDate());
    }

    private Comment mapToEntity(CreateCommentDTO dto) {
        Comment comment = new Comment();
        comment.setText(dto.text());
        comment.setPost(postRepository.getPostById(dto.postId()));
        comment.setCreator(userRepository.getById(dto.userId()));
        return comment;
    }
}