package com.instaworkflow.resumeapi.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    @Value("${bucket4j.filters[0].rate:20}")
    private int rate;

    @Value("${bucket4j.filters[0].rate-interval:1}")
    private int rateInterval;

    private Bucket resolveBucket(String key) {
        return cache.computeIfAbsent(key, k -> {
            Refill refill = Refill.intervally(rate, Duration.ofMinutes(rateInterval));
            Bandwidth limit = Bandwidth.classic(rate, refill);
            return Bucket.builder().addLimit(limit).build();
        });
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String ip = request.getRemoteAddr();
        Bucket bucket = resolveBucket(ip);
        
        if (bucket.tryConsume(1)) {
            return true;
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            return false;
        }
    }
}
