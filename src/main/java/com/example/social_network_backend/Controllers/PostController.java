package com.example.social_network_backend.Controllers;

import com.example.social_network_backend.DTO.Post.CreatePostDTO;
import com.example.social_network_backend.DTO.Post.ResponsePostDTO;
import com.example.social_network_backend.DTO.Post.UpdatePostDTO;
import com.example.social_network_backend.Facades.PostFacade;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostFacade postFacade;

    @PostMapping
    public ResponseEntity<ResponsePostDTO> createPost(@Valid @RequestBody CreatePostDTO postDTO,
                                                      @RequestParam Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postFacade.createPost(postDTO, userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponsePostDTO> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postFacade.getPostById(id));
    }

    @GetMapping
    public ResponseEntity<List<ResponsePostDTO>> getAllPosts() {
        return ResponseEntity.ok(postFacade.getAllPosts());
    }


    @GetMapping("/user/{id}")
    public ResponseEntity<List<ResponsePostDTO>> getUserPosts(@PathVariable Long id,
                                                              @RequestParam(defaultValue = "0") @Min(0) int page,
                                                              @RequestParam(defaultValue = "10") @Min(1) int size) {
        return ResponseEntity.ok(postFacade.getUserPosts(id, page, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponsePostDTO> updatePost(@PathVariable Long id, @Valid @RequestBody UpdatePostDTO dto) {
        return ResponseEntity.ok(postFacade.updatePost(dto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@Valid @PathVariable Long id) {
        postFacade.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}