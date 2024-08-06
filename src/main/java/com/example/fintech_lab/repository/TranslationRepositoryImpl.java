package com.example.fintech_lab.repository;

import com.example.fintech_lab.entity.TranslationTextEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TranslationRepositoryImpl implements TranslationRepository {
    private final JdbcClient jdbcClient;

    @Override
    public void save(TranslationTextEntity entity) {
        Optional<Long> ipIdOptional = findIpId(entity.ipAddress());

        Long ipId;
        if (ipIdOptional.isEmpty()) {
            jdbcClient.sql("INSERT INTO ip_address (ip_address) VALUES (:ip)")
                    .param("ip", entity.ipAddress())
                    .update();

            ipId = findIpId(entity.ipAddress())
                    .orElseThrow(() -> new IllegalStateException("Failed to retrieve IP ID after insert"));
        } else {
            ipId = ipIdOptional.get();
        }

        jdbcClient.sql("INSERT INTO translation_request (ip_id, input_text, translated_text) VALUES (?, ?, ?)")
                .params(ipId, entity.inputText(), entity.translatedText())
                .update();
    }

    private Optional<Long> findIpId(String ipAddress) {
        return jdbcClient.sql("SELECT id FROM ip_address WHERE ip_address = :ip")
                .param("ip", ipAddress)
                .query((rs, rowNum) -> rs.getLong("id"))
                .optional(); // Используем optional() для возвращения Optional<Long>
    }
}
