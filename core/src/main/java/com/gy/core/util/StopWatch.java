package com.gy.core.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static java.time.temporal.ChronoUnit.NANOS;

/**
 * 计时器
 */
public class StopWatch {

    private LocalDateTime start;

    private LocalDateTime end;

    public void start() {
        this.start = LocalDateTime.now();
    }

    public void stop() {
        this.end = LocalDateTime.now();
    }

    public long getTotalTime() {
        return Duration.between(start, end).toMillis();
    }

    public long getTotalTime(TimeUnit timeUnit) {
        Duration duration = Duration.between(start, end);

        switch (timeUnit) {
            case MICROSECONDS:
                return duration.get(NANOS) / 1000;
            case MILLISECONDS:
                return duration.getSeconds() * 1000;
            case SECONDS:
                return duration.getSeconds();
            case MINUTES:
                return duration.getSeconds() / 1000;
            default:
                throw new IllegalArgumentException("不支持的类型");
        }
    }


}
