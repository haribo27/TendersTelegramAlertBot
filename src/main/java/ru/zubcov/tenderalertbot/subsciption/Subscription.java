package ru.zubcov.tenderalertbot.subsciption;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "subscription")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "chat_id", nullable = false)
    private long chatId;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    public Subscription(long chatId, boolean isActive) {
        this.chatId = chatId;
        this.isActive = isActive;
    }

    public Subscription(long chatId) {
        this.chatId = chatId;
    }
}
