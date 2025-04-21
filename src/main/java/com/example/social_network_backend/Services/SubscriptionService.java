package com.example.social_network_backend.Services;

import com.example.social_network_backend.DTO.SubscriptionDTO;
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
    public void subscribe(SubscriptionDTO dto) {
        Subscription subscription = mapToEntity(dto);

        if (subscriptionRepository.existsByFollowerIdAndFollowingId(
                subscription.getFollower().getId(), subscription.getFollowing().getId())) {
            throw new SubscriptionExistsException("Already subscribed");
        }
        subscriptionRepository.save(subscription);
    }

    @Transactional
    public void unsubscribe(SubscriptionDTO dto) {
        Subscription subscription = mapToEntity(dto);
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

    private Subscription mapToEntity(SubscriptionDTO dto) {
        Subscription subscription = new Subscription();
        System.out.println("Проверяем, какие пользователи есть в userRepository:");
        userRepository.findAll().forEach(user ->
                System.out.println("User: ID = " + user.getId() + ", Email = " + user.getEmail())
        );
        User following = userRepository.findById(dto.followingId())
                .orElseThrow(() -> {
                    System.out.println("Не найден user с ID = " + dto.followingId());
                    return new EntityNotFoundException("Following user not found");
                });
        User follower = userRepository.findById(dto.followerId())
                .orElseThrow(() -> {
                    System.out.println("Не найден user с ID = " + dto.followerId());
                    return new EntityNotFoundException("Follower user not found");
                });
        subscription.setFollower(follower);
        subscription.setFollowing(following);
        return subscription;
    }
}