package ru.zubcov.tenderalertbot.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.zubcov.tenderalertbot.bot.TelegramService;
import ru.zubcov.tenderalertbot.subsciption.Subscription;
import ru.zubcov.tenderalertbot.subsciption.SubscriptionService;
import ru.zubcov.tenderalertbot.tender.Tender;
import ru.zubcov.tenderalertbot.tender.TenderService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TenderNotificationService {

    private final SubscriptionService subscriptionService;
    private final TelegramService telegramService;
    private final TenderService tenderService;

    @Scheduled(fixedDelay = 60000)
    public void sendNotifications() {
        List<Subscription> subscribers = subscriptionService.getActiveSubscriptions();
        List<Tender> newTenders = tenderService.getNotNotificatedTenders();
        for (Subscription sub : subscribers) {
            for (Tender tender : newTenders) {
                telegramService.send(sub.getChatId(), generateNewTenderMessage(tender));
                tender.setNotificationSent(true);
                tenderService.saveTender(tender);
            }
        }
    }

    private String generateNewTenderMessage(Tender tender) {

        StringBuilder sb = new StringBuilder();

        sb.append("🔔 <b>Новый тендер по клинингу</b>\n\n");

        appendIfPresent(sb, "🧹 <b>Название:</b>", tender.getTitle());
        appendIfPresent(sb, "💰 <b>Цена:</b>", formatPrice(tender.getPrice()));
        appendIfPresent(sb, "📍 <b>Регион:</b>", tender.getRegion());
        appendIfPresent(sb, "🏢 <b>Заказчик:</b>", tender.getCustomerName());
        appendIfPresent(sb, "📞 <b>Телефон:</b>", tender.getCustomerPhone());
        appendIfPresent(sb, "📧 <b>Email:</b>", tender.getCustomerEmail());

        if (tender.getPublishedAt() != null) {
            sb.append("🕒 <b>Опубликован:</b>\n")
                    .append(formatDate(tender.getPublishedAt()))
                    .append("\n\n");
        }

        appendIfPresent(sb, "📌 <b>Этап:</b>", tender.getTenderStage());
        appendIfPresent(sb, "📄 <b>Регламент:</b>", tender.getRegulationCode());

        if (hasLinks(tender)) {
            sb.append("🔗 <b>Ссылки:</b>\n");

            if (isNotBlank(tender.getAggregatorUrl())) {
                sb.append("• 📊 <a href=\"")
                        .append(tender.getAggregatorUrl())
                        .append("\">Агрегатор</a>\n");
            }

            if (isNotBlank(tender.getEtpUrl())) {
                sb.append("• 🌐 <a href=\"")
                        .append(tender.getEtpUrl())
                        .append("\">ЭТП</a>\n");
            }
        }

        return sb.toString().trim();
    }

    private void appendIfPresent(StringBuilder sb, String label, String value) {
        if (value != null && !value.isBlank()) {
            sb.append(label).append("\n")
                    .append(value).append("\n\n");
        }
    }

    private boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }

    private boolean hasLinks(Tender tender) {
        return isNotBlank(tender.getAggregatorUrl()) || isNotBlank(tender.getEtpUrl());
    }

    private String formatPrice(String price) {
        return price == null ? null : price + " ₽";
    }

    private String formatDate(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }
}
