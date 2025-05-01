package com.example.social_network_backend.Facades;

import com.example.social_network_backend.DTO.Like.CreateLikeDTO;
import com.example.social_network_backend.DTO.Like.ResponseLikeDTO;
import com.example.social_network_backend.Entities.Like;
import com.example.social_network_backend.Entities.Post;
import com.example.social_network_backend.Entities.User;
import com.example.social_network_backend.Repositories.PostRepository;
import com.example.social_network_backend.Repositories.UserRepository;
import com.example.social_network_backend.Services.LikeService;
import com.example.social_network_backend.Exceptions.UserAlreadyLikedPostException;
import jakarta.persistence.EntityNotFoundException;
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
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public ResponseLikeDTO createLike(CreateLikeDTO dto) {
        validate(dto);
        if (likeService.existsByCreatorIdAndPostId(dto.userId(), dto.postId())) {
            throw new UserAlreadyLikedPostException("User already liked this post");
        }
        Like like = likeService.createLike(mapToEntity(dto));
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
        return new ResponseLikeDTO(like.getCreatedDate(), like.getId());
    }

    public Like mapToEntity(CreateLikeDTO dto) {
        Post post = postRepository.findById(dto.postId()).orElseThrow(EntityNotFoundException::new);
        User creator = userRepository.findById(dto.userId()).orElseThrow(EntityNotFoundException::new);
        Like like = new Like();
        like.setPost(post);
        like.setCreator(creator);
        return like;
    }
}