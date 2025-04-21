package com.example.social_network_backend.Controllers;

import com.example.social_network_backend.DTO.Comment.CreateCommentDTO;
import com.example.social_network_backend.DTO.Comment.ResponseCommentDTO;
import com.example.social_network_backend.DTO.Comment.UpdateCommentDTO;
import com.example.social_network_backend.Facades.CommentFacade;
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
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentFacade commentFacade;

    @PostMapping
    public ResponseEntity<ResponseCommentDTO> createComment(@Valid @RequestBody CreateCommentDTO commentDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentFacade.createComment(commentDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseCommentDTO> getCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(commentFacade.getCommentById(id));
    }

    @GetMapping
    public ResponseEntity<List<ResponseCommentDTO>> getAllComments(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                                   @RequestParam(defaultValue = "10") @Min(1) int size) {

        return ResponseEntity.ok(commentFacade.getAllComments(page, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseCommentDTO> updateComment(@PathVariable Long id, @Valid @RequestBody UpdateCommentDTO dto) {
        return ResponseEntity.ok(commentFacade.updateComment(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentFacade.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}