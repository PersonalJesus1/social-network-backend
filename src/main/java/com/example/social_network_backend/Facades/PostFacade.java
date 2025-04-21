package com.example.social_network_backend.Facades;

import com.example.social_network_backend.DTO.Post.CreatePostDTO;
import com.example.social_network_backend.DTO.Post.ResponsePostDTO;
import com.example.social_network_backend.DTO.Post.UpdatePostDTO;
import com.example.social_network_backend.Entities.Post;
import com.example.social_network_backend.Repositories.UserRepository;
import com.example.social_network_backend.Services.PostService;
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
public class PostFacade {

    private final PostService postService;
    private final Validator validator;
    private final UserRepository userRepository;

    public ResponsePostDTO createPost(CreatePostDTO postDTO, Long userId) {
        validate(postDTO); // сначала валидация текста
        Post post = postService.createPost(postDTO, userId); // проверка user уже здесь
        return mapToResponseDto(post);
    }


    public ResponsePostDTO getPostById(Long id) {
        Post post = postService.getPostById(id);
        return mapToResponseDto(post);
    }

    public List<ResponsePostDTO> getAllPosts() {
        return postService.getAllPosts()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public List<ResponsePostDTO> getUserPosts(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postService.getUserPosts(userId, pageable).stream().map(post -> mapToResponseDto(post)).toList();
    }

    public ResponsePostDTO updatePost(UpdatePostDTO dto, Long id) {
        Post post = postService.getPostById(id);
        validate(dto);
        postService.updatePost(dto, id);
        return mapToResponseDto(post);
    }

    public void deletePost(Long id) {

        postService.deletePost(id);
    }

    private void validate(Object dto) {
        Set<ConstraintViolation<Object>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private ResponsePostDTO mapToResponseDto(Post post) {
        return new ResponsePostDTO(post.getId(), post.getText(), post.getImagePath(),
                post.getDate(), post.getPostLikeList() != null ? post.getPostLikeList().size() : 0);
    }
}