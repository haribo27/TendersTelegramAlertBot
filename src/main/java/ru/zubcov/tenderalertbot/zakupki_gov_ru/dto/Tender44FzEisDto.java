package ru.zubcov.tenderalertbot.zakupki_gov_ru.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Tender44FzEisDto {

    private String title;
    private String customerName;
    private String tenderPrice;
    private LocalDateTime publishedDate;
}
