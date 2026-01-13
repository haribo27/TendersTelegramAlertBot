package ru.zubcov.tenderalertbot.subsciption;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepo extends JpaRepository<Subscription, Long> {


    Optional<Subscription> findByChatId(Long chatId);

    @Query("select s from Subscription s where s.isActive = true")
    List<Subscription> findAllByActiveTrue();
}
