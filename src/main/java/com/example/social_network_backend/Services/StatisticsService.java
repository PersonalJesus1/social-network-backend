package com.example.social_network_backend.Services;

import com.example.social_network_backend.DTO.Statistics.ResponseStatisticsDTO;
import com.example.social_network_backend.Entities.Post;
import com.example.social_network_backend.Entities.User;
import com.example.social_network_backend.Entities.UserRole;
import com.example.social_network_backend.Exceptions.AccessDeniedException;
import com.example.social_network_backend.Repositories.PostRepository;
import com.example.social_network_backend.Repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Service
public class StatisticsService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public ResponseStatisticsDTO getStatistics(LocalDate startDate, LocalDate endDate, Authentication authentication) {
        String currentEmail = authentication.getName();
        UserRole role = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + currentEmail))
                .getRole();

        if (role != UserRole.ADMIN) {
            throw new AccessDeniedException("Access denied");
        }
        Long totalPosts = postRepository.getTotalPosts(startDate, endDate);
        Long averagePostsPerDay = postRepository.getAveragePostsPerDay(startDate, endDate);
        List<User> topPostAuthors = postRepository.getTopPostAuthors(startDate, endDate);
        List<Post> topPostsByLikes = postRepository.getTopPostsByLikes(startDate, endDate);
        List<Post> topPostsByComments = postRepository.getTopPostsByComments(startDate, endDate);
        return new ResponseStatisticsDTO(totalPosts, averagePostsPerDay, topPostAuthors, topPostsByLikes, topPostsByComments);
    }
}