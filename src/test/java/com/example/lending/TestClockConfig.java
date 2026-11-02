package com.example.lending;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/** テストでは業務日付を 2026-10-15 に固定する。 */
@TestConfiguration
public class TestClockConfig {

    public static final ZoneId ZONE = ZoneId.of("Asia/Tokyo");

    @Bean
    @Primary
    public Clock fixedClock() {
        return Clock.fixed(Instant.parse("2026-10-15T01:00:00Z"), ZONE);
    }
}
