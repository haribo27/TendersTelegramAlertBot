package ru.zubcov.tenderalertbot.zakupki_gov_ru.noticeAdapter;

import com.ss.generated.purchase.ElectronicPlaceInfoType;

import java.time.LocalDateTime;
import java.util.List;

public interface UnifiedNotice {
    ElectronicPlaceInfoType getPlaceUrl();
    String getEisRegNumber();
    LocalDateTime getPublicationDate();
    String getCustomerName();
    String getContactPhone();
    String getContactEmail();
    String getTenderPrice();
    String getTenderTitle();
    List<String> getLotsDeliveryPlaces();
    String getEtpTenderUrl();
    LocalDateTime getPublishedDate();
}