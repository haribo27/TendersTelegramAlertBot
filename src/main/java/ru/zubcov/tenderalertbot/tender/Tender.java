package ru.zubcov.tenderalertbot.tender;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "tender")
@Getter
@Setter
@NoArgsConstructor
public class Tender {

    public static String REGULATION_CODE_223 = "223-ФЗ";
    public static String REGULATION_CODE_44 = "44-ФЗ";

    public static final String STAGE_COMPLETED = "Закупка завершена";
    public static final String STAGE_REQ_GET = "Подача заявок";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "external_id", nullable = false, length = 64)
    private String externalId;

    @Column(name = "title", length = 4096, nullable = false)
    private String title;

    @Column(name = "price", length = 64)
    private String price;

    @Column(name = "region", length = 1024)
    private String region;

    @Column(name = "regulation_code", length = 32)
    private String regulationCode;

    @Column(name = "customer_name", length = 2048)
    private String customerName;

    @Column(name = "customer_email", length = 64)
    private String customerEmail;

    @Column(name = "customer_phone", length = 32)
    private String customerPhone;

    @Column(name = "aggregator_url", length = 2048, nullable = false)
    private String aggregatorUrl;

    @Column(name = "etp_url", length = 2048)
    private String etpUrl;

    private LocalDateTime publishedAt;

    @Column(name = "tender_stage", length = 128)
    private String tenderStage;

    @ColumnDefault("false")
    @Column(name = "is_notification_sent", nullable = false)
    private boolean isNotificationSent;

    public Tender(String externalId, String title, String price, String region,
                  String customerName, String customerEmail, String customerPhone,
                  String aggregatorUrl, String etpUrl, LocalDateTime publishedAt, String regulationCode, String tenderStage) {
        this.externalId = externalId;
        this.title = title;
        this.price = price;
        this.region = region;
        this.regulationCode = regulationCode;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.aggregatorUrl = aggregatorUrl;
        this.etpUrl = etpUrl;
        this.publishedAt = publishedAt;
        this.tenderStage = tenderStage;
    }

    public Tender(String externalId, String title, String tenderPrice, String customerName,
                  LocalDateTime publishedDate, String aggregatorUrl,String regulationCode, String tenderStage) {
        this.externalId = externalId;
        this.title = title;
        this.price = tenderPrice;
        this.customerName = customerName;
        this.publishedAt = publishedDate;
        this.aggregatorUrl = aggregatorUrl;
        this.regulationCode = regulationCode;
        this.tenderStage = tenderStage;
    }
}
