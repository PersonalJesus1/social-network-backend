package com.example.social_network_backend.Services;

import com.example.social_network_backend.Entities.Subscription;
import com.example.social_network_backend.Entities.User;
import com.example.social_network_backend.Exceptions.SubscriptionWasDeletedException;
import com.example.social_network_backend.Repositories.SubscriptionRepository;
import com.example.social_network_backend.Repositories.UserRepository;
import com.example.social_network_backend.Exceptions.SubscriptionExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @Transactional
    public void subscribe(Subscription subscription) {
        User follower = userRepository.findById(subscription.getFollower().getId())
                .orElseThrow(() -> new EntityNotFoundException("Follower not found"));
        User following = userRepository.findById(subscription.getFollowing().getId())
                .orElseThrow(() -> new EntityNotFoundException("Following not found"));

        if (subscriptionRepository.existsByFollowerIdAndFollowingId(follower.getId(), following.getId())) {
            throw new SubscriptionExistsException("Already subscribed");
        }
        subscriptionRepository.save(subscription);
    }

    @Transactional
    public void unsubscribe(Subscription subscription) {
        if (!subscriptionRepository.existsByFollowerIdAndFollowingId(subscription.getFollower().getId(), subscription.getFollowing().getId())) {
            throw new SubscriptionWasDeletedException("Already unsubscribed");
        }
        subscriptionRepository.deleteByFollowerIdAndFollowingId(subscription.getFollower().getId(), subscription.getFollowing().getId());
    }

    public List<Subscription> getUserSubscriptions(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found");
        }
        return subscriptionRepository.findAllByFollowerId(userId, pageable);
    }

    public List<Subscription> getUserFollowers(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found");
        }
        return subscriptionRepository.findAllByFollowingId(userId, pageable);
    }

    public Integer getSubscriptionsCount(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found");
        }
        return subscriptionRepository.findAllByFollowingId(userId).size();
    }

    public Integer getFollowersCount(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found");
        }
        return subscriptionRepository.findAllByFollowerId(userId).size();
    }
}