package ru.zubcov.tenderalertbot.zakupki_gov_ru;

import com.ss.generated.purchase.*;
import com.ss.generated.webRequest.NoticeVersionsDataResponse;
import com.ss.generated.webRequest.NoticeVersionsDataType;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.zubcov.tenderalertbot.tender.Tender;
import ru.zubcov.tenderalertbot.tender.TenderService;
import ru.zubcov.tenderalertbot.zakupki_gov_ru.dto.ParsedFilteredRssTendersDto;
import ru.zubcov.tenderalertbot.zakupki_gov_ru.eis_client.EisClient;
import ru.zubcov.tenderalertbot.zakupki_gov_ru.noticeAdapter.*;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ZakupkiTenderParser {

    private final EisClient eisClient;
    private static final JAXBContext jaxbContext;
    private final TenderService tenderService;

    static {
        try {
            jaxbContext = JAXBContext.newInstance(
                    PurchaseNotice.class,
                    PurchaseNoticeZKESMBO.class,
                    PurchaseNoticeZPESMBO.class,
                    PurchaseNoticeZK.class,
                    PurchaseNoticeOK.class,
                    PurchaseNoticeOA.class,
                    PurchaseNoticeEP.class,
                    PurchaseNoticeAESMBO.class,
                    PurchaseNoticeAE94FZ.class,
                    PurchaseNoticeAE.class
            );
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to initialize JAXBContext", e);
        }
    }

    public void parseTendersInfoFromEis(List<ParsedFilteredRssTendersDto> filteredTenders) {
        for (ParsedFilteredRssTendersDto filteredTender : filteredTenders) {
            if (Tender.REGULATION_CODE_223.equals(filteredTender.getRegulationCode())) {
                UnifiedNotice tender = getUnifiedNoticeFromEis(filteredTender.getRegNumber());
                tenderService.save223Tender(tender, filteredTender);
            } else {
                tenderService.save44Tender(filteredTender);
            }

        }
    }

    public UnifiedNotice getUnifiedNoticeFromEis(String tenderRegNum) {
        NoticeVersionsDataResponse noticeDataResponse = eisClient.fetchNotice(tenderRegNum);
        NoticeVersionsDataResponse.Body bodyResponse = noticeDataResponse.getBody();
        if (bodyResponse.getResult().equals(com.ss.generated.webRequest.ResultType.FAILURE)) {
            log.info("Eis response notice request status: {}, violations: {}",
                    ResultType.FAILURE, bodyResponse.getViolations().getViolation().toString());
            throw new RuntimeException();
        }
        NoticeVersionsDataType bodyDataType = bodyResponse.getBody();
        byte[] decodedBytes = bodyDataType.getZipEncodedData();

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decodedBytes);
             ZipInputStream zipInputStream = new ZipInputStream(byteArrayInputStream)) {

            ZipEntry entry = zipInputStream.getNextEntry();
            if (entry == null) {
                log.error("ErrZakupkiTenderParser001 ZIP-файл пустой для извещения RegNum: {}", tenderRegNum);
                throw new RuntimeException();
            }

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            Object obj = unmarshaller.unmarshal(zipInputStream);
            return checkAndReturnObj(obj);
        } catch (Exception e) {
            log.error("ErrZakupkiTenderParser002 Ошибка при обработке извещения RegNum: {}. Error message: {}", tenderRegNum, e.getMessage());
            throw new RuntimeException();
        }
    }

    private UnifiedNotice checkAndReturnObj(Object obj) {
        if (obj instanceof PurchaseNotice) {
            return new PurchaseNoticeAdapter((PurchaseNotice) obj);
        }
        if (obj instanceof PurchaseNoticeZKESMBO) {
            return new PurchaseNoticeZKESMBOAdapter((PurchaseNoticeZKESMBO) obj);
        }
        if (obj instanceof PurchaseNoticeZPESMBO) {
            return new PurchaseNoticeZPESMBOAdapter((PurchaseNoticeZPESMBO) obj);
        }
        if (obj instanceof PurchaseNoticeZK) {
            return new PurchaseNoticeZKAdapter((PurchaseNoticeZK) obj);
        }
        if (obj instanceof PurchaseNoticeOK) {
            return new PurchaseNoticeOKAdapter((PurchaseNoticeOK) obj);
        }
        if (obj instanceof PurchaseNoticeOA) {
            return new PurchaseNoticeOAAdapter((PurchaseNoticeOA) obj);
        }
        if (obj instanceof PurchaseNoticeEP) {
            return new PurchaseNoticeEPAdapter((PurchaseNoticeEP) obj);
        }
        if (obj instanceof PurchaseNoticeAESMBO) {
            return new PurchaseNoticeAESMBOAdapter((PurchaseNoticeAESMBO) obj);
        }
        if (obj instanceof PurchaseNoticeAE94FZ) {
            return new PurchaseNoticeAE94FZAdapter((PurchaseNoticeAE94FZ) obj);
        }
        if (obj instanceof PurchaseNoticeAE) {
            return new PurchaseNoticeAEAdapter((PurchaseNoticeAE) obj);
        }
        if (obj instanceof PurchaseNoticeKESMBO) {
            return new PurchaseNoticeKESMBOAdapter((PurchaseNoticeKESMBO) obj);
        }
        return null;
    }
}
