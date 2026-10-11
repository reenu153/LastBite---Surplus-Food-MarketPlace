package com.example.tgtg.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import java.time.Clock;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@Configuration
// Gives Page<T> a stable JSON shape: {"content": [...], "page": {...}}
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class AppConfig {

    // LEARN: A @Bean is an object Spring creates and injects wherever it's needed.
    // Injecting Clock (instead of calling LocalDateTime.now()) makes time testable.
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
