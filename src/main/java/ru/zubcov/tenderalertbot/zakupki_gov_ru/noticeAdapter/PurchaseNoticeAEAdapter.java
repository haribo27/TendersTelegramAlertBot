package ru.zubcov.tenderalertbot.zakupki_gov_ru.noticeAdapter;

import com.ss.generated.purchase.ElectronicPlaceInfoType;
import com.ss.generated.purchase.PurchaseNoticeAE;
import com.ss.generated.purchase.PurchaseNoticeAEItemType;
import com.ss.generated.purchase.PurchaseNoticeItemType;
import ru.zubcov.tenderalertbot.utils.UtilsTenderParser;

import java.time.LocalDateTime;
import java.util.List;

public class PurchaseNoticeAEAdapter implements UnifiedNotice {

    private final PurchaseNoticeAE purchaseNotice;
    private final PurchaseNoticeAEItemType purchaseNoticeItemType;

    public PurchaseNoticeAEAdapter(PurchaseNoticeAE notice) {
        this.purchaseNotice = notice;
        this.purchaseNoticeItemType = purchaseNotice.getBody().getItem();
    }

    @Override
    public String getEisRegNumber() {
        return purchaseNoticeItemType.getPurchaseNoticeAEData().getRegistrationNumber();
    }

    @Override
    public String getTenderTitle() {
        return purchaseNoticeItemType.getPurchaseNoticeAEData().getName();
    }

    @Override
    public List<String> getLotsDeliveryPlaces() {
        return UtilsTenderParser.getLotsDeliveryPlaces(purchaseNoticeItemType.getPurchaseNoticeAEData().getLots().getLot());
    }

    @Override
    public String getEtpTenderUrl() {
        return purchaseNoticeItemType.getPurchaseNoticeAEData().getUrlVSRZ();
    }

    @Override
    public LocalDateTime getPublishedDate() {
        return UtilsTenderParser.xmlCalendar2LocalDate(purchaseNoticeItemType.getPurchaseNoticeAEData().getPublicationDateTime());
    }

    @Override
    public ElectronicPlaceInfoType getPlaceUrl() {
        return purchaseNotice.getBody().getItem().getPurchaseNoticeAEData().getElectronicPlaceInfo();
    }

    @Override
    public LocalDateTime getPublicationDate() {
        return UtilsTenderParser.xmlCalendar2LocalDate(
                purchaseNoticeItemType.getPurchaseNoticeAEData().getPublicationDateTime()
        );
    }

    @Override
    public String getCustomerName() {
        return purchaseNoticeItemType.getPurchaseNoticeAEData().getCustomer().getMainInfo().getFullName();
    }

    @Override
    public String getContactPhone() {
        return purchaseNoticeItemType.getPurchaseNoticeAEData().getContact().getPhone();
    }

    @Override
    public String getContactEmail() {
        return purchaseNoticeItemType.getPurchaseNoticeAEData().getContact().getEmail();
    }

    @Override
    public String getTenderPrice() {
        return UtilsTenderParser.calculateTenderPriceLotType(purchaseNoticeItemType.getPurchaseNoticeAEData().getLots().getLot());
    }
}