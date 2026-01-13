package ru.zubcov.tenderalertbot.zakupki_gov_ru.eis_rss_history;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "eis_rss_history")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EisRssHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "last_date_parse", nullable = false)
    private LocalDateTime lastDateParse;

    public EisRssHistory(LocalDateTime lastDateParse) {
        this.lastDateParse = lastDateParse;
    }
}
