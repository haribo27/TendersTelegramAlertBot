package ru.zubcov.tenderalertbot.zakupki_gov_ru.eis_rss_history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface EisRssHistoryRepo extends JpaRepository<EisRssHistory, Long> {

    @Query("SELECT MAX(e.lastDateParse) FROM EisRssHistory e")
    LocalDateTime findMaxLastDateParse();
}
