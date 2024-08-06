package com.example.fintech_lab.dto.yandex;

import java.util.List;

public record YandexRequest(String folderId,
                            List<String> texts,
                            String targetLanguageCode,
                            String sourceLanguageCode) {
}
