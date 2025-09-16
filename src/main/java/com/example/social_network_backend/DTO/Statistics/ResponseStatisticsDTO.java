package com.example.social_network_backend.DTO.Statistics;

import com.example.social_network_backend.Entities.Post;
import com.example.social_network_backend.Entities.User;

import java.util.List;

public record ResponseStatisticsDTO(Long totalPosts, Long averagePostsPerDay, List<User> topPostAuthors,
                                    List<Post> topPostsByLikes, List<Post> topPostsByComments) {
}