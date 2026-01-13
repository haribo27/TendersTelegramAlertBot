package ru.zubcov.tenderalertbot.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "telegram_id", nullable = false, unique = true)
    private long telegramId;

    private String username;

    private String firstName;

    private String lastName;

    private LocalDateTime createdAt = LocalDateTime.now();

    public User(long telegramId, String username, String firstName, String lastName) {
        this.telegramId = telegramId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
