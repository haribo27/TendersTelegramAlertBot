package ru.zubcov.tenderalertbot.subsciption;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {

    private final SubscriptionRepo repository;

    public SubscriptionService(SubscriptionRepo repository) {
        this.repository = repository;
    }

    public void subscribe(Long chatId) {
        Subscription sub = repository
                .findByChatId(chatId)
                .orElseGet(() -> new Subscription(chatId));

        sub.setActive(true);
        repository.save(sub);
    }

    public void unsubscribe(Long chatId) {
        repository.findByChatId(chatId)
                .ifPresent(sub -> {
                    sub.setActive(false);
                    repository.save(sub);
                });
    }

    public boolean isSubscribed(Long chatId) {
        return repository.findByChatId(chatId)
                .map(Subscription::isActive)
                .orElse(false);
    }

    public List<Subscription> getActiveSubscriptions() {
        return repository.findAllByActiveTrue();
    }
}
