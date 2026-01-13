package ru.zubcov.tenderalertbot.zakupki_gov_ru.eis_client;

import com.ss.generated.webRequest.NoticeVersionsDataResponse;
import com.ss.generated.webRequest.ResultType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.PlaceholderResolutionException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class EisClient {

    public static final String EIS_LOGIN = "login";
    public static final String EIS_PASSWORD = "password";
    public static final String EIS_ENTITY_TYPE = "entityType";
    public static final String EIS_REG_NUMBER = "regNumber";
    public static final String EIS_ENTITY_TYPE_PURCHASE_NOTICE = "purchaseNotice";
    private static final String ESTP_EIS_LOGIN = "Estp111223";
    private static final String ESTP_EIS_PASSWORD = "EstpSro02015";
    private static final String GET_PURCHASE_NOTICE_URL = "https://int.zakupki.gov.ru/223/integration/rest/publishedInfo/regNumber";

    private final RestTemplate restTemplate;

    public NoticeVersionsDataResponse fetchNotice(String eisRegNumber) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        body.add(EIS_LOGIN, ESTP_EIS_LOGIN);
        body.add(EIS_PASSWORD, ESTP_EIS_PASSWORD);
        body.add(EIS_ENTITY_TYPE, EIS_ENTITY_TYPE_PURCHASE_NOTICE);
        body.add(EIS_REG_NUMBER, eisRegNumber);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<NoticeVersionsDataResponse> eisResponse;
        try {
            eisResponse = restTemplate.exchange(GET_PURCHASE_NOTICE_URL, HttpMethod.POST, requestEntity, NoticeVersionsDataResponse.class);
        } catch (ResourceAccessException e) {
            log.error("ErrEisClient001 Failed to get access to resource by url: {}, Error message: {}", GET_PURCHASE_NOTICE_URL, e.getMessage());
            throw new RuntimeException();
        } catch (RestClientResponseException ex) {
            log.error("ErrEisClient002 Failed to get NoticeVersionsDataResponse status, response status: {}, body: {}, url: {}",
                    ex.getStatusCode(), ex.getResponseBodyAsString(), GET_PURCHASE_NOTICE_URL);
            throw new RuntimeException();
        } catch (RestClientException ex) {
            log.error("ErrEisClient003 Failed to get NoticeVersionsDataResponse status status by url: {}", GET_PURCHASE_NOTICE_URL);
            throw new RuntimeException();
        }
        NoticeVersionsDataResponse response = eisResponse.getBody();
        if (response == null) {
            log.error("ErrEisClient004 Failed to get NoticeVersionsDataResponse status status by url: {}", GET_PURCHASE_NOTICE_URL);
            throw new RuntimeException();
        }
        if (response.getBody() == null) {
            log.error("ErrEisClient004 Failed to get NoticeVersionsDataResponse status status by url: {}", GET_PURCHASE_NOTICE_URL);
            throw new RuntimeException();
        }
        if (response.getBody().getResult().equals(ResultType.FAILURE)) {
            log.error("ErrEisClient004 NoticeVersionsDataResponse status Failure. From url: {}, message: {}", GET_PURCHASE_NOTICE_URL, response.getBody().getViolations());
            throw new RuntimeException();
        }
        return response;
    }
}
