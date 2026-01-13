package ru.zubcov.tenderalertbot.zakupki_gov_ru.noticeAdapter;

import com.ss.generated.purchase.ElectronicPlaceInfoType;
import com.ss.generated.purchase.PurchaseNotice;
import com.ss.generated.purchase.PurchaseNoticeItemType;
import ru.zubcov.tenderalertbot.utils.UtilsTenderParser;

import java.time.LocalDateTime;
import java.util.List;

public class PurchaseNoticeAdapter implements UnifiedNotice {

    private final PurchaseNotice purchaseNotice;
    private final PurchaseNoticeItemType purchaseNoticeItemType;

    public PurchaseNoticeAdapter(PurchaseNotice notice) {
        this.purchaseNotice = notice;
        this.purchaseNoticeItemType = purchaseNotice.getBody().getItem();
    }

    @Override
    public String getEisRegNumber() {
        return purchaseNoticeItemType.getPurchaseNoticeData().getRegistrationNumber();
    }

    @Override
    public String getTenderTitle() {
        return purchaseNoticeItemType.getPurchaseNoticeData().getName();
    }

    @Override
    public List<String> getLotsDeliveryPlaces() {
        return UtilsTenderParser.getLotsDeliveryPlaces(purchaseNoticeItemType.getPurchaseNoticeData().getLots().getLot());
    }

    @Override
    public String getEtpTenderUrl() {
        return purchaseNoticeItemType.getPurchaseNoticeData().getUrlVSRZ();
    }

    @Override
    public LocalDateTime getPublishedDate() {
        return UtilsTenderParser.xmlCalendar2LocalDate(purchaseNoticeItemType.getPurchaseNoticeData().getPublicationDateTime());
    }


    @Override
    public ElectronicPlaceInfoType getPlaceUrl() {
        return purchaseNotice.getBody().getItem().getPurchaseNoticeData().getElectronicPlaceInfo();
    }

    @Override
    public LocalDateTime getPublicationDate() {
        return UtilsTenderParser.xmlCalendar2LocalDate(
                purchaseNoticeItemType.getPurchaseNoticeData().getPublicationDateTime()
        );
    }

    @Override
    public String getCustomerName() {
        return purchaseNoticeItemType.getPurchaseNoticeData().getCustomer().getMainInfo().getFullName();
    }

    @Override
    public String getContactPhone() {
        return purchaseNoticeItemType.getPurchaseNoticeData().getContact().getPhone();
    }

    @Override
    public String getContactEmail() {
        return purchaseNoticeItemType.getPurchaseNoticeData().getContact().getEmail();
    }

    @Override
    public String getTenderPrice() {
        return UtilsTenderParser.calculateTenderPriceLotType(purchaseNoticeItemType.getPurchaseNoticeData().getLots().getLot());
    }
}