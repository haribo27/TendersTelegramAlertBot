package ru.zubcov.tenderalertbot.notification;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tender_notification_history")
@Getter
@Setter
public class TenderNotificationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "tender_id", nullable = false)
    private long tenderId;
}
