package ru.zubcov.tenderalertbot.zakupki_gov_ru.noticeAdapter;

import com.ss.generated.purchase.*;
import ru.zubcov.tenderalertbot.utils.UtilsTenderParser;

import java.time.LocalDateTime;
import java.util.List;

public class PurchaseNoticeOKAdapter implements UnifiedNotice {

    private final PurchaseNoticeOK purchaseNotice;
    private final PurchaseNoticeOKItemType purchaseNoticeItemType;

    public PurchaseNoticeOKAdapter(PurchaseNoticeOK notice) {
        this.purchaseNotice = notice;
        this.purchaseNoticeItemType = purchaseNotice.getBody().getItem();
    }

    @Override
    public String getEisRegNumber() {
        return purchaseNoticeItemType.getPurchaseNoticeOKData().getRegistrationNumber();
    }

    @Override
    public String getTenderTitle() {
        return purchaseNoticeItemType.getPurchaseNoticeOKData().getName();
    }

    @Override
    public List<String> getLotsDeliveryPlaces() {
        return UtilsTenderParser.getLotsDeliveryPlaces(purchaseNoticeItemType.getPurchaseNoticeOKData().getLots().getLot());
    }

    @Override
    public String getEtpTenderUrl() {
        return purchaseNoticeItemType.getPurchaseNoticeOKData().getUrlVSRZ();
    }

    @Override
    public LocalDateTime getPublishedDate() {
        return UtilsTenderParser.xmlCalendar2LocalDate(purchaseNoticeItemType.getPurchaseNoticeOKData().getPublicationDateTime());
    }

    @Override
    public ElectronicPlaceInfoType getPlaceUrl() {
        return null;
    }

    @Override
    public LocalDateTime getPublicationDate() {
        return UtilsTenderParser.xmlCalendar2LocalDate(
                purchaseNoticeItemType.getPurchaseNoticeOKData().getPublicationDateTime()
        );
    }

    @Override
    public String getCustomerName() {
        return purchaseNoticeItemType.getPurchaseNoticeOKData().getCustomer().getMainInfo().getFullName();
    }

    @Override
    public String getContactPhone() {
        return purchaseNoticeItemType.getPurchaseNoticeOKData().getContact().getPhone();
    }

    @Override
    public String getContactEmail() {
        return purchaseNoticeItemType.getPurchaseNoticeOKData().getContact().getEmail();
    }

    @Override
    public String getTenderPrice() {
        return UtilsTenderParser.calculateTenderPriceLotType(purchaseNoticeItemType.getPurchaseNoticeOKData().getLots().getLot());
    }
}