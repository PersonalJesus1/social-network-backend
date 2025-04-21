package com.example.social_network_backend.Facades;

import com.example.social_network_backend.DTO.Like.CreateLikeDTO;
import com.example.social_network_backend.DTO.Like.ResponseLikeDTO;
import com.example.social_network_backend.Entities.Like;
import com.example.social_network_backend.Services.LikeService;
import com.example.social_network_backend.Exceptions.UserAlreadyLikedPostException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@Service
public class LikeFacade {

    private final LikeService likeService;
    private final Validator validator;

    public ResponseLikeDTO createLike(CreateLikeDTO likeDTO) {
        validate(likeDTO);
        if (likeService.existsByCreatorIdAndPostId(likeDTO.userId(), likeDTO.postId())) {
            throw new UserAlreadyLikedPostException("User already liked this post");
        }
        Like like = likeService.createLike(likeDTO);
        return mapToResponseDto(like);
    }

    public ResponseLikeDTO getLikeById(Long id) {
        Like like = likeService.getLikeById(id);
        return mapToResponseDto(like);
    }

    public List<ResponseLikeDTO> getAllLikes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return likeService.getAllLikes(pageable)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public ResponseLikeDTO updateLike(Long id) {
        Like like = likeService.getLikeById(id);
        likeService.updateLike(id);
        return mapToResponseDto(like);
    }

    public void deleteLike(Long id) {
        likeService.deleteLike(id);
    }

    private void validate(Object dto) {
        Set<ConstraintViolation<Object>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private ResponseLikeDTO mapToResponseDto(Like like) {
        return new ResponseLikeDTO(like.getDate(), like.getId());
    }
}