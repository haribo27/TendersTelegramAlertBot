package ru.zubcov.tenderalertbot.zakupki_gov_ru.noticeAdapter;

import com.ss.generated.purchase.ElectronicPlaceInfoType;
import com.ss.generated.purchase.PurchaseNoticeAE94FZ;
import com.ss.generated.purchase.PurchaseNoticeAE94FZItemType;
import ru.zubcov.tenderalertbot.utils.UtilsTenderParser;

import java.time.LocalDateTime;
import java.util.List;

public class PurchaseNoticeAE94FZAdapter implements UnifiedNotice {

    private final PurchaseNoticeAE94FZ purchaseNotice;
    private final PurchaseNoticeAE94FZItemType purchaseNoticeItemType;

    public PurchaseNoticeAE94FZAdapter(PurchaseNoticeAE94FZ notice) {
        this.purchaseNotice = notice;
        this.purchaseNoticeItemType = purchaseNotice.getBody().getItem();
    }

    @Override
    public String getEisRegNumber() {
        return purchaseNoticeItemType.getPurchaseNoticeAE94FZData().getRegistrationNumber();
    }

    @Override
    public String getTenderTitle() {
        return purchaseNoticeItemType.getPurchaseNoticeAE94FZData().getName();
    }

    @Override
    public List<String> getLotsDeliveryPlaces() {
        return UtilsTenderParser.getLotsDeliveryPlaces(purchaseNoticeItemType.getPurchaseNoticeAE94FZData().getLots().getLot());
    }

    @Override
    public String getEtpTenderUrl() {
        return purchaseNoticeItemType.getPurchaseNoticeAE94FZData().getUrlVSRZ();
    }

    @Override
    public LocalDateTime getPublishedDate() {
        return UtilsTenderParser.xmlCalendar2LocalDate(purchaseNoticeItemType.getPurchaseNoticeAE94FZData().getPublicationDateTime());
    }

    @Override
    public ElectronicPlaceInfoType getPlaceUrl() {
        return purchaseNotice.getBody().getItem().getPurchaseNoticeAE94FZData().getElectronicPlaceInfo();
    }

    @Override
    public LocalDateTime getPublicationDate() {
        return UtilsTenderParser.xmlCalendar2LocalDate(
                purchaseNoticeItemType.getPurchaseNoticeAE94FZData().getPublicationDateTime()
        );
    }

    @Override
    public String getCustomerName() {
        return purchaseNoticeItemType.getPurchaseNoticeAE94FZData().getCustomer().getMainInfo().getFullName();
    }

    @Override
    public String getContactPhone() {
        return purchaseNoticeItemType.getPurchaseNoticeAE94FZData().getContact().getPhone();
    }

    @Override
    public String getContactEmail() {
        return purchaseNoticeItemType.getPurchaseNoticeAE94FZData().getContact().getEmail();
    }

    @Override
    public String getTenderPrice() {
        return UtilsTenderParser.calculateTenderPriceLotType(purchaseNoticeItemType.getPurchaseNoticeAE94FZData().getLots().getLot());
    }
}