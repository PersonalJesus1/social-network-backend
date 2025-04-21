package com.example.social_network_backend;

import com.example.social_network_backend.DTO.Like.CreateLikeDTO;
import com.example.social_network_backend.Entities.Like;
import com.example.social_network_backend.Entities.Post;
import com.example.social_network_backend.Entities.User;
import com.example.social_network_backend.Repositories.LikeRepository;
import com.example.social_network_backend.Repositories.PostRepository;
import com.example.social_network_backend.Repositories.UserRepository;
import com.example.social_network_backend.Services.LikeService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LikeService likeService;

    private Like like;
    private CreateLikeDTO createLikeDTO;
    private User user;
    private Post post;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        post = new Post();
        post.setId(1L);

        like = new Like();
        like.setId(1L);
        like.setPost(post);
        like.setCreator(user);
        like.setDate(LocalDateTime.now());

        createLikeDTO = new CreateLikeDTO(1L, 1L);
    }

    @Test
    void createLike_Success() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(likeRepository.save(any(Like.class))).thenReturn(like);

        Like createdLike = likeService.createLike(createLikeDTO);

        assertNotNull(createdLike);
        assertEquals(like.getPost(), createdLike.getPost());
        assertEquals(like.getCreator(), createdLike.getCreator());
        verify(likeRepository, times(1)).save(any(Like.class));
    }

    @Test
    void getLikeById_Success() {
        when(likeRepository.findById(1L)).thenReturn(Optional.of(like));

        Like foundLike = likeService.getLikeById(1L);

        assertNotNull(foundLike);
        assertEquals(1L, foundLike.getId());
        verify(likeRepository).findById(1L);
    }

    @Test
    void getAllLikes_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Like> page = new PageImpl<>(List.of(like));
        when(likeRepository.findAll(pageable)).thenReturn(page);
        Page<Like> likes = likeService.getAllLikes(pageable);

        assertNotNull(likes);
        assertEquals(1, likes.getTotalElements());
        verify(likeRepository).findAll(pageable);
    }

    @Test
    void getLikeById_ThrowsException_WhenNotFound() {
        when(likeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> likeService.getLikeById(1L));
    }

    @Test
    void updateLike_Success() {
        when(likeRepository.findById(1L)).thenReturn(Optional.of(like));
        when(likeRepository.save(any(Like.class))).thenReturn(like);
        Like like = likeRepository.findById(1L).get();
        Like updatedLike = likeService.updateLike(1L);

        assertNotNull(updatedLike);
        assertEquals(like.getDate(), updatedLike.getDate());
    }

    @Test
    void updateLike_ThrowsException_WhenNotFound() {
        when(likeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> likeService.updateLike(1L));
    }

    @Test
    void deleteLike_Success() {
        when(likeRepository.findById(1L)).thenReturn(Optional.of(like));
        likeService.deleteLike(1L);

        verify(likeRepository).deleteById(1L);
    }
}