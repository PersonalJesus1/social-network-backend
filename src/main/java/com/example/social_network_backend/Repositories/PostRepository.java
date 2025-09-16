package com.example.social_network_backend.Repositories;

import com.example.social_network_backend.Entities.Post;
import com.example.social_network_backend.Entities.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCreator(User user, Pageable pageable);

    Post getPostById(@NotNull Long aLong);

    @Query("SELECT COUNT(p) FROM Post p WHERE p.createdDate BETWEEN :startDate AND :endDate")
    Long getTotalPosts(@Param("startDate") LocalDate startDate,
                       @Param("endDate") LocalDate endDate);

    @Query(value = """
            SELECT COUNT(*)::double precision
                   / (DATE_PART('day', CAST(:endDate AS timestamp) - CAST(:startDate AS timestamp)) + 1)
            FROM post p
            WHERE p.created_date >= :startDate AND p.created_date < (:endDate + INTERVAL '1 day')
            """,
            nativeQuery = true)
    Long getAveragePostsPerDay(@Param("start") LocalDate start,
                               @Param("end") LocalDate end);

    @Query("SELECT p.creator FROM Post p WHERE p.createdDate BETWEEN :startDate AND :endDate GROUP BY p.creator ORDER BY COUNT(p) DESC")
    List<User> getTopPostAuthors(@Param("startDate") LocalDate startDate,
                                 @Param("endDate") LocalDate endDate);

    @Query("SELECT p FROM Post p LEFT JOIN p.likes l WHERE p.createdDate BETWEEN :startDate AND :endDate GROUP BY p ORDER BY COUNT(l) DESC")
    List<Post> getTopPostsByLikes(@Param("startDate") LocalDate startDate,
                                  @Param("endDate") LocalDate endDate);

    @Query("SELECT p FROM Post p LEFT JOIN p.comments c WHERE p.createdDate BETWEEN :startDate AND :endDate GROUP BY p ORDER BY COUNT(c) DESC")
    List<Post> getTopPostsByComments(@Param("startDate") LocalDate startDate,
                                     @Param("endDate") LocalDate endDate);
}