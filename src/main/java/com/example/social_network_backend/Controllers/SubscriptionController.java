package com.example.social_network_backend.Controllers;

import com.example.social_network_backend.DTO.SubscriptionDTO;
import com.example.social_network_backend.Facades.SubscriptionFacade;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {
    private final SubscriptionFacade subscriptionFacade;

    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribe(@RequestBody @Valid SubscriptionDTO dto) {
        subscriptionFacade.subscribe(dto);
        return ResponseEntity.ok("Subscription successful");
    }

    @DeleteMapping("/unsubscribe")
    public ResponseEntity<String> unsubscribe(@RequestBody @Valid SubscriptionDTO dto) {
        subscriptionFacade.unsubscribe(dto);
        return ResponseEntity.ok("Unsubscription successful");
    }

    @GetMapping("/{userId}/subscriptions")
    public ResponseEntity<List<SubscriptionDTO>> getUserSubscriptions(@PathVariable Long userId,
                                                                      @RequestParam(defaultValue = "0") @Min(0)int page,
                                                                      @RequestParam(defaultValue = "10") @Min(1)int size) {
        return ResponseEntity.ok(subscriptionFacade.getUserSubscriptions(userId, page, size));
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<SubscriptionDTO>> getUserFollowers(@PathVariable Long userId,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(subscriptionFacade.getUserFollowers(userId, page, size));
    }

    @GetMapping("/{userId}/subscriptions/count")
    public ResponseEntity<Integer> getSubscriptionsCount(@PathVariable Long userId) {
        return ResponseEntity.ok(subscriptionFacade.getSubscriptionsCount(userId));
    }

    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<Integer> getFollowersCount(@PathVariable Long userId) {
        return ResponseEntity.ok(subscriptionFacade.getFollowersCount(userId));
    }
}