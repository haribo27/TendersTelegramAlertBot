package ru.zubcov.tenderalertbot.zakupki_gov_ru;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.zubcov.tenderalertbot.tender.Tender;
import ru.zubcov.tenderalertbot.utils.UtilsTenderParser;
import ru.zubcov.tenderalertbot.zakupki_gov_ru.dto.ParsedFilteredRssTendersDto;
import ru.zubcov.tenderalertbot.zakupki_gov_ru.dto.Tender44FzEisDto;
import ru.zubcov.tenderalertbot.zakupki_gov_ru.eis_rss_history.EisRssHistoryService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
@Slf4j
public class ZakupkiRssParser {

    private static final String RSS_URL = "https://zakupki.gov.ru/epz/order/extendedsearch/rss.html";
    private static final String DESCRIPTION_TAG = "description";
    private static final String TITLE_TAG = "title";
    private static final String LAW_TYPE_REGEX = "Размещение выполняется по:\\s*</strong>([^<]+)";
    private static final String TENDER_SUBJECT_REGEX = "Наименование объекта закупки:\\s*</strong>([^<]+)";
    private static final String TENDER_REG_NUM_REGEX = "№\\s*(\\d+)";
    private static final String TENDER_STAGE_REGEX = "Этап размещения:\\s*</strong>([^<]+)";
    private static final String CUSTOMER_NAME_REGEX = "Наименование Заказчика:\\s*</strong>([^<]+)";
    private static final String TENDER_PRICE_REGEX = "Начальная цена контракта:\\s*</strong>([\\d\\s]+[.,]\\d{2})";

    private final ZakupkiTenderParser zakupkiTenderParser;
    private final EisRssHistoryService eisRssHistoryService;

    private static final List<String> KEYWORDS = List.of(
            "клининг", "клининговые услуги", "услуги уборки",
            "комплексная уборка", "профессиональная уборка", "санитарное содержание",
            "эксплуатационная уборка", "уборка офисных помещений", "уборка бизнес-центров",
            "уборка торговых центров", "уборка складских помещений", "уборка производственных помещений",
            "уборка административных зданий", "убока мкд", "уборка нежилых помещений",
            "уборка помещений", "аутсорсинг уборки", "санитарное обслуживание",
            "оказание услуг по уборке помещений", "оказание клининговых услуг",
            "услуги по санитарному содержанию", "комплексное обслуживание объекта",
            "эксплуатация и уборка", "техническое обслуживание и уборка",
            "содержание помещений", "оказание услуг по уборке",
            "санитарное содержание", "комплексное обслуживание", "услуги по уборке", "комплексной уборке", "Услуги по чистке и уборке"
    );

    //@Scheduled(fixedDelay = 15 * 60 * 1000) // Каждые 15 минут
    @Scheduled(fixedDelay = 60000)
    public void parseRssFeed() {
        try {
            log.info("Parsing RSS Feed... Date: {}", LocalDateTime.now());

            String xml = fetch(RSS_URL);
            log.info("Start of xml {}", xml.substring(0, 15));

            Document doc = Jsoup
                    .parse(xml, "", Parser.xmlParser());

            List<Element> itemElements = doc.select("item");

            List<ParsedFilteredRssTendersDto> filteredTenders = filterEntries(itemElements);

            if (!filteredTenders.isEmpty()) {
                zakupkiTenderParser.parseTendersInfoFromEis(filteredTenders);
            }

        } catch (Exception e) {
            System.err.println("Ошибка при парсинге RSS: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String fetch(String url) {
        try {
            Process process = new ProcessBuilder(
                    "curl",
                    "-s",
                    "-L",
                    "--compressed",
                    "--connect-timeout", "15",
                    "--max-time", "30",
                    url
            ).start();

            byte[] bytes = process.getInputStream().readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch RSS via curl", e);
        }
    }

    private List<ParsedFilteredRssTendersDto> filterEntries(List<Element> items) {
        List<Element> filtered = new ArrayList<>();
        List<ParsedFilteredRssTendersDto> filteredTenders = new ArrayList<>();
        LocalDateTime lastDateParse = eisRssHistoryService.getLastDateParse();
        if (lastDateParse != null) {
            // сортируем от меньшего к большему времени
            // и убираем записи меньше той, которая была в предыдущей итерации
            items = items.stream()
                    .sorted(Comparator.comparing(UtilsTenderParser::convertDateToLocalDateTime
                    ))
                    .filter(entry -> UtilsTenderParser.convertDateToLocalDateTime(entry).isAfter(lastDateParse))
                    .toList();
        } else {
            // сортируем от меньшего к большему времени
            items = items.stream()
                    .sorted(Comparator.comparing(UtilsTenderParser::convertDateToLocalDateTime
                    ))
                    .toList();
        }
        if (items.isEmpty()) {
            return List.of();
        }

        LocalDateTime lastEntryDate = null;
        for (Element item : items) {
            if (item == null) {
                continue;
            }
            lastEntryDate = UtilsTenderParser.convertDateToLocalDateTime(item);
            if (matchesKeywords(item)) {
                filtered.add(item);
            }
        }
        eisRssHistoryService.saveLastDateParse(lastEntryDate);
        if (!filtered.isEmpty()) {
            filtered.forEach(item -> {
                String regulationCode = extractLawType(item);
                String regNum = extractTargetTextFromXml(item, TITLE_TAG, TENDER_REG_NUM_REGEX);
                String tenderStage = extractTargetTextFromXml(item, DESCRIPTION_TAG, TENDER_STAGE_REGEX);
                if (!Tender.STAGE_REQ_GET.equals(tenderStage)) {
                    return;
                }
                if (regNum != null) {
                    if (Tender.REGULATION_CODE_44.equals(regulationCode)) {
                        filteredTenders.add(new ParsedFilteredRssTendersDto(regulationCode, regNum, tenderStage,  createTender44Dto(item)));
                    } else {
                        filteredTenders.add(new ParsedFilteredRssTendersDto(regulationCode, regNum, tenderStage));
                    }
                }
            });
        }

        return filteredTenders;
    }

    private Tender44FzEisDto createTender44Dto(Element item) {
        String title = extractTargetTextFromXml(item, DESCRIPTION_TAG, TENDER_SUBJECT_REGEX);
        String customerName = extractTargetTextFromXml(item, DESCRIPTION_TAG, CUSTOMER_NAME_REGEX);
        String tenderPriceRegex = extractTargetTextFromXml(item, DESCRIPTION_TAG, TENDER_PRICE_REGEX);
        LocalDateTime publishedDate = UtilsTenderParser.convertDateToLocalDateTime(item);
        return new Tender44FzEisDto(title, customerName, tenderPriceRegex, publishedDate);
    }


    public String extractTargetTextFromXml(Element item, String targetTag, String patternRegex) {
        Element elementText = item.selectFirst(targetTag);
        String text = elementText.text();

        Pattern pattern = Pattern.compile(patternRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public String extractLawType(Element item) {
        String lawType = extractTargetTextFromXml(item, DESCRIPTION_TAG, LAW_TYPE_REGEX);
        if (lawType != null) {
            if (lawType.contains(Tender.REGULATION_CODE_44)) {
                return Tender.REGULATION_CODE_44;
            } else if (lawType.contains(Tender.REGULATION_CODE_223)) {
                return Tender.REGULATION_CODE_223;
            }
            return lawType;
        } else {
            return null;
        }
    }

    private boolean matchesKeywords(Element item) {
        String tenderSubject = extractTargetTextFromXml(item, DESCRIPTION_TAG,
                TENDER_SUBJECT_REGEX);
        if (tenderSubject == null || tenderSubject.isEmpty()) {
            return false;
        }
        // Ищем совпадения по ключевым словам
        for (String keyword : KEYWORDS) {
            if (tenderSubject.toLowerCase().contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}