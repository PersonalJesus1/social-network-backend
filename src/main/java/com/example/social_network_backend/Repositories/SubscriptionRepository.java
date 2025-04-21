package com.example.social_network_backend.Repositories;

import com.example.social_network_backend.Entities.Subscription;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);

    List<Subscription> findAllByFollowerId(Long followerId, Pageable pageable); //All Users subscriptions

    List<Subscription> findAllByFollowerId(Long followerId);

    List<Subscription> findAllByFollowingId(Long followingId, Pageable pageable); //All Users followers

    List<Subscription> findAllByFollowingId(Long followingId);
}