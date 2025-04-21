package com.example.social_network_backend.Services;

import com.example.social_network_backend.DTO.Post.CreatePostDTO;
import com.example.social_network_backend.DTO.Post.UpdatePostDTO;
import com.example.social_network_backend.Entities.Post;
import com.example.social_network_backend.Entities.User;
import com.example.social_network_backend.Repositories.PostRepository;
import com.example.social_network_backend.Repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Post createPost(CreatePostDTO dto, Long userId) {
        System.out.println("Searching for user with ID: " + userId);
        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Post post = new Post();
        post.setText(dto.text());
        post.setCreator(creator);

        // Save image and record the path
        if (dto.pictureBase64() != null && !dto.pictureBase64().isEmpty()) {
            // Delete old image if it exists
            if (post.getImagePath() != null) {
                File oldFile = new File(post.getImagePath());
                if (oldFile.exists()) {
                    oldFile.delete();
                }
            }
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
                .orElseThrow(EntityNotFoundException::new); // Получаем объект User, а не просто ID
        return postRepository.findByCreator(user, pageable);        // Передаем объект User в метод репозитория
    }

    @Transactional
    public Post updatePost(UpdatePostDTO dto, Long id) {
        Post post = postRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        post.setText(dto.text());
        // Save image and record the path
        if (dto.pictureBase64() != null && !dto.pictureBase64().isEmpty()) {
            String imagePath = saveImage(dto.pictureBase64(), post.getCreator().getEmail());
            post.setImagePath(imagePath);
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

    private String saveImage(String base64Image, String userEmail) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64Image);

            String uploadDir = "C:/projects/social-network/uploads/images/" + userEmail + "/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs(); // Create folder for user if it doesnt exist
            }

            String fileName = UUID.randomUUID() + ".png";
            File file = new File(uploadDir, fileName);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(decodedBytes);
            }

            return uploadDir + fileName;
        } catch (IllegalArgumentException | IOException e) {
            throw new RuntimeException("Error saving image", e);
        }
    }
}