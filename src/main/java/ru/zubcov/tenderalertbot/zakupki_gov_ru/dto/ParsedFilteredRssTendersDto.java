package ru.zubcov.tenderalertbot.zakupki_gov_ru.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ParsedFilteredRssTendersDto {

    private String regulationCode;
    private String regNumber;
    private String tenderStage;
    private Tender44FzEisDto tender44FzEisDto;

    public ParsedFilteredRssTendersDto(String regulationCode, String regNumber, String tenderStage, Tender44FzEisDto tender44FzEisDto) {
        this.regulationCode = regulationCode;
        this.regNumber = regNumber;
        this.tender44FzEisDto = tender44FzEisDto;
        this.tenderStage = tenderStage;
    }

    public ParsedFilteredRssTendersDto(String regulationCode, String regNumber, String tenderStage) {
        this.regulationCode = regulationCode;
        this.regNumber = regNumber;
        this.tenderStage = tenderStage;
    }
}
