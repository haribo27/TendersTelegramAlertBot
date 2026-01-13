package ru.zubcov.tenderalertbot.zakupki_gov_ru.noticeAdapter;

import com.ss.generated.purchase.ElectronicPlaceInfoType;
import com.ss.generated.purchase.PurchaseNoticeZPESMBO;
import com.ss.generated.purchase.PurchaseNoticeZPESMBOItemType;
import ru.zubcov.tenderalertbot.utils.UtilsTenderParser;

import java.time.LocalDateTime;
import java.util.List;

public class PurchaseNoticeZPESMBOAdapter implements UnifiedNotice {

    private final PurchaseNoticeZPESMBO purchaseNotice;
    private final PurchaseNoticeZPESMBOItemType purchaseNoticeItemType;

    public PurchaseNoticeZPESMBOAdapter(PurchaseNoticeZPESMBO notice) {
        this.purchaseNotice = notice;
        this.purchaseNoticeItemType = purchaseNotice.getBody().getItem();
    }

    @Override
    public String getEisRegNumber() {
        return purchaseNoticeItemType.getPurchaseNoticeZPESMBOData().getRegistrationNumber();
    }

    @Override
    public String getTenderTitle() {
        return purchaseNoticeItemType.getPurchaseNoticeZPESMBOData().getName();
    }

    @Override
    public List<String> getLotsDeliveryPlaces() {
        return UtilsTenderParser.getLotsDeliveryPlaces(purchaseNoticeItemType.getPurchaseNoticeZPESMBOData().getLots().getLot());
    }

    @Override
    public String getEtpTenderUrl() {
        return purchaseNoticeItemType.getPurchaseNoticeZPESMBOData().getUrlVSRZ();
    }

    @Override
    public LocalDateTime getPublishedDate() {
        return UtilsTenderParser.xmlCalendar2LocalDate(purchaseNoticeItemType.getPurchaseNoticeZPESMBOData().getPublicationDateTime());
    }

    @Override
    public ElectronicPlaceInfoType getPlaceUrl() {
        return purchaseNotice.getBody().getItem().getPurchaseNoticeZPESMBOData().getElectronicPlaceInfo();
    }

    @Override
    public LocalDateTime getPublicationDate() {
        return UtilsTenderParser.xmlCalendar2LocalDate(
                purchaseNoticeItemType.getPurchaseNoticeZPESMBOData().getPublicationDateTime()
        );
    }

    @Override
    public String getCustomerName() {
        return purchaseNoticeItemType.getPurchaseNoticeZPESMBOData().getCustomer().getMainInfo().getFullName();
    }

    @Override
    public String getContactPhone() {
        return purchaseNoticeItemType.getPurchaseNoticeZPESMBOData().getContact().getPhone();
    }

    @Override
    public String getContactEmail() {
        return purchaseNoticeItemType.getPurchaseNoticeZPESMBOData().getContact().getEmail();
    }

    @Override
    public String getTenderPrice() {
        return UtilsTenderParser.calculateTenderPriceLotType(purchaseNoticeItemType.getPurchaseNoticeZPESMBOData().getLots().getLot());
    }
}
