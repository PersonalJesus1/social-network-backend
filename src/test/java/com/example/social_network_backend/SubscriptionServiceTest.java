package com.example.social_network_backend;

import com.example.social_network_backend.Entities.Subscription;
import com.example.social_network_backend.Entities.User;
import com.example.social_network_backend.Exceptions.SubscriptionExistsException;
import com.example.social_network_backend.Repositories.SubscriptionRepository;
import com.example.social_network_backend.Repositories.UserRepository;
import com.example.social_network_backend.Services.SubscriptionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @InjectMocks
    private SubscriptionService subscriptionService;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void subscribe_Success() {
        Subscription subscription = new Subscription();
        User follower = new User();
        follower.setId(1L);
        User following = new User();
        following.setId(2L);
        subscription.setFollower(follower);
        subscription.setFollowing(following);

        when(userRepository.findById(1L)).thenReturn(Optional.of(follower));
        when(userRepository.findById(2L)).thenReturn(Optional.of(following));
        when(subscriptionRepository.existsByFollowerIdAndFollowingId(1L, 2L)).thenReturn(false);

        subscriptionService.subscribe(subscription);

        verify(subscriptionRepository).save(any(Subscription.class));
    }

    @Test
    void unsubscribe_Success() {
        User follower = new User();
        follower.setId(1L);
        User following = new User();
        following.setId(2L);
        Subscription subscription = new Subscription();
        subscription.setFollower(follower);
        subscription.setFollowing(following);

        when(subscriptionRepository.existsByFollowerIdAndFollowingId(1L, 2L)).thenReturn(true);

        subscriptionService.unsubscribe(subscription);

        verify(subscriptionRepository).deleteByFollowerIdAndFollowingId(1L, 2L);
    }

    @Test
    void subscribe_ThrowsException_WhenAlreadySubscribed() {
        Subscription subscription = new Subscription();
        User follower = new User();
        follower.setId(1L);
        User following = new User();
        following.setId(2L);
        subscription.setFollower(follower);
        subscription.setFollowing(following);

        when(userRepository.findById(1L)).thenReturn(Optional.of(follower));
        when(userRepository.findById(2L)).thenReturn(Optional.of(following));
        when(subscriptionRepository.existsByFollowerIdAndFollowingId(1L, 2L)).thenReturn(true);

        assertThrows(SubscriptionExistsException.class, () -> subscriptionService.subscribe(subscription));
        verify(subscriptionRepository, never()).save(any(Subscription.class));
    }

    @Test
    void getUserSubscriptions_ReturnsList() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Subscription> subscriptions = List.of(new Subscription(), new Subscription());
        User follower = new User();
        follower.setId(1L);

        when(userRepository.existsById(1L)).thenReturn(true);

        when(subscriptionRepository.findAllByFollowerId(1L, pageable)).thenReturn(subscriptions);

        List<Subscription> result = subscriptionService.getUserSubscriptions(1L, pageable);

        assertEquals(2, result.size());
        verify(subscriptionRepository).findAllByFollowerId(1L, pageable);
    }

    @Test
    void getUserFollowers_ReturnsList() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Subscription> followers = List.of(new Subscription(), new Subscription(), new Subscription());
        when(userRepository.existsById(1L)).thenReturn(true);

        when(subscriptionRepository.findAllByFollowingId(1L, pageable)).thenReturn(followers);

        List<Subscription> result = subscriptionService.getUserFollowers(1L, pageable);

        assertEquals(3, result.size());
        verify(subscriptionRepository).findAllByFollowingId(1L, pageable);
    }

    @Test
    void getSubscriptionsCount_ReturnsCorrectCount() {
        List<Subscription> subscriptions = List.of(new Subscription(), new Subscription());

        when(subscriptionRepository.findAllByFollowingId(1L)).thenReturn(subscriptions);
        when(userRepository.existsById(1L)).thenReturn(true);

        int count = subscriptionService.getSubscriptionsCount(1L);

        assertEquals(2, count);
        verify(subscriptionRepository).findAllByFollowingId(1L);
    }

    @Test
    void getFollowersCount_ReturnsCorrectCount() {
        List<Subscription> followers = List.of(new Subscription(), new Subscription(), new Subscription());
        when(userRepository.existsById(1L)).thenReturn(true);

        when(subscriptionRepository.findAllByFollowerId(1L)).thenReturn(followers);

        int count = subscriptionService.getFollowersCount(1L);

        assertEquals(3, count);
        verify(subscriptionRepository).findAllByFollowerId(1L);
    }
}