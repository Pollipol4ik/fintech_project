package com.example.fintech_lab.repository;

import com.example.fintech_lab.IntegrationEnvironment;
import com.example.fintech_lab.entity.TranslationTextEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext
public class TranslationRepositoryTest  extends IntegrationEnvironment {

    @Autowired
    private TranslationRepositoryImpl repository;

    @Autowired
    private JdbcClient jdbcClient;

    @Test
    @Transactional
    @Rollback
    public void saveShouldInsertNewTranslationRequest_whenIpNotExists() {
        // Arrange
        String ipAddress = "192.168.0.1";
        String inputText = "Hello";
        String translatedText = "Hola";

        TranslationTextEntity entity = new TranslationTextEntity(ipAddress, inputText, translatedText);

        // Act
        repository.save(entity);

        // Assert
        Long count = jdbcClient.sql("SELECT COUNT(*) FROM translation_request WHERE input_text = :inputText AND translated_text = :translatedText")
                .param("inputText", inputText)
                .param("translatedText", translatedText)
                .query(Long.class)
                .single();
        assertThat(count).isEqualTo(1L);

        Long ipCount = jdbcClient.sql("SELECT COUNT(*) FROM ip_address WHERE ip_address = :ipAddress")
                .param("ipAddress", ipAddress)
                .query(Long.class)
                .single();
        assertThat(ipCount).isEqualTo(1L);
    }

    @Test
    @Transactional
    @Rollback
    public void save_shouldInsertNewTranslationRequest_whenIpExists() {
        // Arrange
        String ipAddress = "192.168.0.2";
        String inputText = "Hi";
        String translatedText = "Hola";

        jdbcClient.sql("INSERT INTO ip_address (ip_address) VALUES (:ipAddress)")
                .param("ipAddress", ipAddress)
                .update();

        TranslationTextEntity entity = new TranslationTextEntity(ipAddress, inputText, translatedText);

        // Act
        repository.save(entity);

        // Assert
        Long count = jdbcClient.sql("SELECT COUNT(*) FROM translation_request WHERE input_text = :inputText AND translated_text = :translatedText")
                .param("inputText", inputText)
                .param("translatedText", translatedText)
                .query(Long.class)
                .single();
        assertThat(count).isEqualTo(1L);
    }
}
