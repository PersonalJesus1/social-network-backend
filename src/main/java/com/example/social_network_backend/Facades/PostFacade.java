package com.example.social_network_backend.Facades;

import com.example.social_network_backend.DTO.Post.CreatePostDTO;
import com.example.social_network_backend.DTO.Post.ResponsePostDTO;
import com.example.social_network_backend.DTO.Post.ResponseUpdatedPostDTO;
import com.example.social_network_backend.DTO.Post.UpdatePostDTO;
import com.example.social_network_backend.Entities.Image;
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

    public ResponsePostDTO createPost(Long userId, CreatePostDTO dto) {
        validate(dto);
        Post post = postService.createPost(userId, mapToEntity(dto));
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

    public ResponseUpdatedPostDTO updatePost(Long id, UpdatePostDTO dto) {
        validate(dto);
        Post post = new Post();
        post.setText(dto.text());
        post.setImage(dto.image());
        return mapToUpdatedResponseDto(postService.updatePost(id, post));
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

    private Post mapToEntity(CreatePostDTO dto) {
        Post post = new Post();
        post.setText(dto.text());
        if (dto.base64Image() != null && !dto.base64Image().isEmpty()) {
            Image image = new Image();
            image.setImagePath(dto.base64Image());
            post.setImage(image);
        }
        return post;
    }

    private ResponsePostDTO mapToResponseDto(Post post) {
        int likeCount = post.getLikes() != null && !post.getLikes().isEmpty() ? post.getLikes().size() : 0;
        return new ResponsePostDTO(
                post.getId(),
                post.getText(),
                post.getImage(),
                post.getCreatedDate(),
                likeCount);
    }

    private ResponseUpdatedPostDTO mapToUpdatedResponseDto(Post post) {
        int likeCount = post.getLikes() != null && !post.getLikes().isEmpty() ? post.getLikes().size() : 0;
        return new ResponseUpdatedPostDTO(
                post.getId(),
                post.getText(),
                post.getImage(),
                post.getUpdatedDate(),
                likeCount);
    }
}