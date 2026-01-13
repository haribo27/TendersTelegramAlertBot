package ru.zubcov.tenderalertbot.tender;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TenderRepo extends JpaRepository<Tender, Long> {

    @Query("select t from Tender t where t.isNotificationSent = false ")
    List<Tender> getNotNotificatedTenders();
}
