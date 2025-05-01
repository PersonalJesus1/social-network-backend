package com.example.social_network_backend.Services;

import com.example.social_network_backend.Entities.Image;
import com.example.social_network_backend.Entities.Post;
import com.example.social_network_backend.Entities.User;
import com.example.social_network_backend.Repositories.PostRepository;
import com.example.social_network_backend.Repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    @Transactional
    public Post createPost(Long userId, Post post) {
        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        post.setCreator(creator);

        if (post.getImage() != null && post.getImage().getImagePath() != null
                && !post.getImage().getImagePath().isEmpty()) {
            // imagePath contains base64
            Image savedImage = fileService.saveImage(post.getImage().getImagePath(), creator.getEmail());
            savedImage.setPost(post); // connection between post and image
            post.setImage(savedImage);
        }
        return postRepository.save(post);
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<Post> getUserPosts(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);
        return postRepository.findByCreator(user, pageable);
    }

    @Transactional
    public Post updatePost(Long id, Post post) {
        Post existedPost = postRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        existedPost.setText(post.getText());
        // Save image and record the path
        if (post.getImage() != null && post.getImage().getImagePath() != null && !post.getImage().getImagePath().isEmpty()) {
            fileService.deleteFile(post.getImage().getImagePath());
            Image image = fileService.saveImage(post.getImage().getImagePath(), post.getCreator().getEmail());
            post.setImage(image);
        } else {
            Image image = fileService.saveImage(post.getImage().getImagePath(), post.getCreator().getEmail());
            post.setImage(image);
        }
        postRepository.save(post);
        return post;
    }

    @Transactional
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new EntityNotFoundException("Post with ID " + id + " not found");
        }
        postRepository.deleteById(id);
    }
}