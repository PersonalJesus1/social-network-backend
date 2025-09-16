package com.example.social_network_backend;

import com.example.social_network_backend.DTO.Statistics.ResponseStatisticsDTO;
import com.example.social_network_backend.Entities.Post;
import com.example.social_network_backend.Entities.User;
import com.example.social_network_backend.Entities.UserRole;
import com.example.social_network_backend.Repositories.PostRepository;
import com.example.social_network_backend.Repositories.UserRepository;
import com.example.social_network_backend.Services.StatisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatisticsServiceTest {

    @InjectMocks
    private StatisticsService statisticsService;
    @Mock
    private Authentication authentication;

    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;
    private LocalDate startDate;
    private LocalDate endDate;

    @BeforeEach
    void setUp() {
        startDate = LocalDate.of(2025, 1, 1);
        endDate = LocalDate.of(2025, 1, 31);
    }

    @Test
    void getStatistics_ReturnsExpectedDTO() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@example.com");
        user1.setRole(UserRole.ADMIN);

        Post post1 = new Post();
        post1.setId(101L);
        post1.setText("Post with likes");

        Post post2 = new Post();
        post2.setId(102L);
        post2.setText("Post with comments");

        System.out.println("Current email: " + user1.getEmail());

        when(authentication.getName()).thenReturn("user1@example.com");
        when(userRepository.findByEmail("user1@example.com")).thenReturn(Optional.of(user1));

        when(postRepository.getTotalPosts(startDate, endDate)).thenReturn(50L);
        when(postRepository.getAveragePostsPerDay(startDate, endDate)).thenReturn(5L);
        when(postRepository.getTopPostAuthors(startDate, endDate)).thenReturn(List.of(user1));
        when(postRepository.getTopPostsByLikes(startDate, endDate)).thenReturn(List.of(post1));
        when(postRepository.getTopPostsByComments(startDate, endDate)).thenReturn(List.of(post2));

        ResponseStatisticsDTO result = statisticsService.getStatistics(startDate, endDate, authentication);

        assertNotNull(result);
        assertEquals(50L, result.totalPosts());
        assertEquals(5L, result.averagePostsPerDay());
        assertEquals(1, result.topPostAuthors().size());
        assertEquals(user1, result.topPostAuthors().get(0));
        assertEquals(1, result.topPostsByLikes().size());
        assertEquals(post1, result.topPostsByLikes().get(0));
        assertEquals(1, result.topPostsByComments().size());
        assertEquals(post2, result.topPostsByComments().get(0));

        verify(postRepository).getTotalPosts(startDate, endDate);
        verify(postRepository).getAveragePostsPerDay(startDate, endDate);
        verify(postRepository).getTopPostAuthors(startDate, endDate);
        verify(postRepository).getTopPostsByLikes(startDate, endDate);
        verify(postRepository).getTopPostsByComments(startDate, endDate);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void getStatistics_WhenRepositoryReturnsEmptyLists() {

        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@example.com");
        user1.setRole(UserRole.ADMIN);

        when(authentication.getName()).thenReturn("user1@example.com");
        when(userRepository.findByEmail("user1@example.com")).thenReturn(Optional.of(user1));
        when(postRepository.getTotalPosts(startDate, endDate)).thenReturn(0L);
        when(postRepository.getAveragePostsPerDay(startDate, endDate)).thenReturn(0L);
        when(postRepository.getTopPostAuthors(startDate, endDate)).thenReturn(List.of());
        when(postRepository.getTopPostsByLikes(startDate, endDate)).thenReturn(List.of());
        when(postRepository.getTopPostsByComments(startDate, endDate)).thenReturn(List.of());

        ResponseStatisticsDTO result = statisticsService.getStatistics(startDate, endDate, authentication);

        assertNotNull(result);
        assertEquals(0L, result.totalPosts());
        assertEquals(0L, result.averagePostsPerDay());
        assertTrue(result.topPostAuthors().isEmpty());
        assertTrue(result.topPostsByLikes().isEmpty());
        assertTrue(result.topPostsByComments().isEmpty());
    }
}