package com.example.social_network_backend.Facades;

import com.example.social_network_backend.DTO.SubscriptionDTO;
import com.example.social_network_backend.Entities.Subscription;
import com.example.social_network_backend.Entities.User;
import com.example.social_network_backend.Repositories.UserRepository;
import com.example.social_network_backend.Services.SubscriptionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@Service
public class SubscriptionFacade {
    private final SubscriptionService subscriptionService;
    private final Validator validator;
    private final UserRepository userRepository;

    public void subscribe(SubscriptionDTO dto) {
        validate(dto);
        subscriptionService.subscribe(mapToEntity(dto));
    }

    public void unsubscribe(SubscriptionDTO dto) {
        validate(dto);
        subscriptionService.unsubscribe(mapToEntity(dto));
    }

    public List<SubscriptionDTO> getUserSubscriptions(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return subscriptionService.getUserSubscriptions(userId, pageable).stream().map(post -> mapToDto(post)).toList();
    }

    public List<SubscriptionDTO> getUserFollowers(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return subscriptionService.getUserFollowers(userId, pageable).stream().map(post -> mapToDto(post)).toList();
    }

    public Integer getSubscriptionsCount(Long userId) {
        return subscriptionService.getSubscriptionsCount(userId);
    }

    public Integer getFollowersCount(Long userId) {
        return subscriptionService.getFollowersCount(userId);
    }

    private void validate(Object dto) {
        Set<ConstraintViolation<Object>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private SubscriptionDTO mapToDto(Subscription subscription) {
        return new SubscriptionDTO(subscription.getFollower().getId(), subscription.getFollowing().getId());
    }

    private Subscription mapToEntity(SubscriptionDTO dto) {
        Subscription subscription = new Subscription();
        User following = userRepository.findById(dto.followingId())
                .orElseThrow(() -> new EntityNotFoundException());
        User follower = userRepository.findById(dto.followerId())
                .orElseThrow(() -> new EntityNotFoundException());
        subscription.setFollower(follower);
        subscription.setFollowing(following);
        return subscription;
    }
}