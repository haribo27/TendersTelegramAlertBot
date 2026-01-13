package ru.zubcov.tenderalertbot.zakupki_gov_ru.eis_rss_history;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EisRssHistoryService {

    private final EisRssHistoryRepo repo;

    public LocalDateTime getLastDateParse() {
        return repo.findMaxLastDateParse();
    }

    public void saveLastDateParse(LocalDateTime lastDateParse) {
        repo.save(new EisRssHistory(lastDateParse));
    }
}
