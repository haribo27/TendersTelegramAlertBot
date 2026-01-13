package ru.zubcov.tenderalertbot.tender;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zubcov.tenderalertbot.utils.UtilsTenderParser;
import ru.zubcov.tenderalertbot.zakupki_gov_ru.dto.ParsedFilteredRssTendersDto;
import ru.zubcov.tenderalertbot.zakupki_gov_ru.dto.Tender44FzEisDto;
import ru.zubcov.tenderalertbot.zakupki_gov_ru.noticeAdapter.UnifiedNotice;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenderService {

    private final TenderRepo tenderRepo;

    @Transactional
    public void save223Tender(UnifiedNotice tenderEis, ParsedFilteredRssTendersDto filteredTender) {
        StringBuilder tenderDeliveryPlaces = new StringBuilder();
        tenderEis.getLotsDeliveryPlaces().forEach(tenderDeliveryPlaces::append
        );
        String region = tenderDeliveryPlaces.toString();

        Tender tender = new Tender(tenderEis.getEisRegNumber(), tenderEis.getTenderTitle(), tenderEis.getTenderPrice(), region,
                tenderEis.getCustomerName(), tenderEis.getContactEmail(), tenderEis.getContactPhone(),
                UtilsTenderParser.getEisTenderLink(tenderEis.getEisRegNumber(), filteredTender.getRegulationCode()), tenderEis.getEtpTenderUrl(),
                tenderEis.getPublicationDate(), Tender.REGULATION_CODE_223, filteredTender.getTenderStage());
        tenderRepo.save(tender);
    }

    @Transactional
    public void save44Tender(ParsedFilteredRssTendersDto filteredTender) {
        Tender44FzEisDto tender44Fz = filteredTender.getTender44FzEisDto();
        Tender tender = new Tender(filteredTender.getRegNumber(), tender44Fz.getTitle(), tender44Fz.getTenderPrice(),
                tender44Fz.getCustomerName(), tender44Fz.getPublishedDate(),
                UtilsTenderParser.getEisTenderLink(filteredTender.getRegNumber(), filteredTender.getRegulationCode()),
                filteredTender.getRegulationCode(), filteredTender.getTenderStage());
        tenderRepo.save(tender);
    }

    public void saveTender(Tender tender) {
        tenderRepo.save(tender);
    }

    public List<Tender> getNotNotificatedTenders() {
        return tenderRepo.getNotNotificatedTenders();
    }
}
