package ru.zubcov.tenderalertbot.utils;

import com.ss.generated.purchase.DeliveryPlaceType;
import com.ss.generated.purchase.LotType;
import org.jsoup.nodes.Element;
import ru.zubcov.tenderalertbot.tender.Tender;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class UtilsTenderParser {

    public static LocalDateTime xmlCalendar2LocalDate(XMLGregorianCalendar xmlGregorianCalendar) {
        if (xmlGregorianCalendar == null) {
            return null;
        }
        GregorianCalendar gregorianCalendar = xmlGregorianCalendar.toGregorianCalendar();
        ZonedDateTime zonedDateTime = gregorianCalendar.toZonedDateTime();
        return zonedDateTime.toLocalDateTime();
    }

    public static String calculateTenderPriceLotType(List<? extends LotType> lots) {
        BigDecimal tenderPrice = new BigDecimal(0);
        if (lots != null && !lots.isEmpty()) {
            for (LotType lot : lots) {
                if (lot != null && lot.getLotData() != null) {
                    BigDecimal initialSum = lot.getLotData().getInitialSum();
                    tenderPrice = tenderPrice.add(initialSum);
                }
            }
        }
        return tenderPrice.toString();
    }

    public static List<String> getLotsDeliveryPlaces(List<? extends LotType> lots) {
        List<String> result = new ArrayList<>();
        if (lots != null && !lots.isEmpty()) {
            for (LotType lot : lots) {
                if (lot != null && lot.getLotData() != null) {
                    DeliveryPlaceType deliveryPlace = lot.getLotData().getDeliveryPlace();
                    result.add(deliveryPlace.getRegion() + " " + deliveryPlace.getAddress());
                }
            }
        }
        return result;
    }

    public static String getEisTenderLink(String tenderRegNumber, String regulationCode) {
        if (Tender.REGULATION_CODE_44.equals(regulationCode)) {
            return String.format("https://zakupki.gov.ru/epz/order/notice/zk20/view/common-info.html?regNumber=%s", tenderRegNumber);
        } else {
            return String.format("https://zakupki.gov.ru/epz/order/notice/notice223/common-info.html?noticeInfoId=%s", tenderRegNumber);
        }

    }

    public static LocalDateTime convertDateToLocalDateTime(Element item) {
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        Element dateElement = item.selectFirst("pubDate");
        if (dateElement != null) {
            return LocalDateTime.parse(dateElement.text(), formatter);
        }
        return LocalDateTime.of(2000, 1, 1, 0, 0);
    }

}
