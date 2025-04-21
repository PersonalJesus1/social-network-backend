package com.example.social_network_backend.Controllers;

import com.example.social_network_backend.DTO.Like.CreateLikeDTO;
import com.example.social_network_backend.DTO.Like.ResponseLikeDTO;
import com.example.social_network_backend.Facades.LikeFacade;
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
@RequestMapping("/api/v1/likes")
public class LikeController {

    private final LikeFacade likeFacade;

    @PostMapping
    public ResponseEntity<ResponseLikeDTO> createLike(@Valid @RequestBody CreateLikeDTO likeDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(likeFacade.createLike(likeDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseLikeDTO> getLikeById(@PathVariable Long id) {
        return ResponseEntity.ok(likeFacade.getLikeById(id));
    }

    @GetMapping
    public ResponseEntity<List<ResponseLikeDTO>> getAllLikes(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                             @RequestParam(defaultValue = "10")@Min(1) int size) {
        return ResponseEntity.ok(likeFacade.getAllLikes(page, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseLikeDTO> updateLike( @PathVariable Long id) {
        return ResponseEntity.ok(likeFacade.updateLike(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLike(@PathVariable Long id) {
        likeFacade.deleteLike(id);
        return ResponseEntity.noContent().build();
    }
}