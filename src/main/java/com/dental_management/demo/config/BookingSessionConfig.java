package com.dental_management.demo.config;

import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;

@Configuration
public class BookingSessionConfig {

    public static final LocalTime MORNING_START = LocalTime.of(9, 0);
    public static final LocalTime MORNING_END = LocalTime.of(12, 0);

    public static final LocalTime EVENING_START = LocalTime.of(13, 0);
    public static final LocalTime EVENING_END = LocalTime.of(18, 0);

    public static final int SLOT_INTERVAL_MINUTES = 60;
}