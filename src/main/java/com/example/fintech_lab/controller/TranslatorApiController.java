package com.example.fintech_lab.controller;

import com.example.fintech_lab.dto.yandex.GetAllLanguagesRequest;
import com.example.fintech_lab.dto.yandex.GetAllLanguagesResponse;
import com.example.fintech_lab.dto.yandex.YandexRequest;
import com.example.fintech_lab.dto.yandex.YandexResponse;
import com.example.fintech_lab.exceptions.AccessResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Semaphore;

@Component
@RequiredArgsConstructor
public class TranslatorApiController {
    private final RestTemplate restTemplate;
    private final Semaphore semaphore = new Semaphore(10, true);

    public String translateWord(YandexRequest translateApiRequest) throws InterruptedException {
        try {
            semaphore.acquire();
            String urlEndpoint = "/translate";
            ResponseEntity<YandexResponse> response = restTemplate.postForEntity(
                    urlEndpoint,
                    translateApiRequest,
                    YandexResponse.class
            );
            if (response.getBody() != null && !response.getBody().translations().isEmpty()) {
                return response.getBody().translations().get(0).text();
            } else {
                throw new AccessResourceException("Ошибка доступа к ресурсу перевода");
            }
        } catch (Exception e) {
            throw new AccessResourceException("Ошибка доступа к ресурсу перевода");
        } finally {
            Thread.sleep(500);
            semaphore.release();
        }
    }


    public GetAllLanguagesResponse getLanguages(GetAllLanguagesRequest getAllLanguagesRequest) {
        String urlEndpoint = "/languages";
        try {
            ResponseEntity<GetAllLanguagesResponse> response = restTemplate.postForEntity(
                    urlEndpoint,
                    getAllLanguagesRequest,
                    GetAllLanguagesResponse.class
            );
            return response.getBody();
        } catch (Exception e) {
            throw new AccessResourceException("Ошибка доступа к ресурсу перевода");
        }


    }
}
