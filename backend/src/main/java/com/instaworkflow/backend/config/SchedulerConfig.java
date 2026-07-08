package com.instaworkflow.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Configuration
@EnableScheduling
@ConditionalOnProperty(value = "app.scheduling.enabled", havingValue = "true", matchIfMissing = false)
public class SchedulerConfig {
}
