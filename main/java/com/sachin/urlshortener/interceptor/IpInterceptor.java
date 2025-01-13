package com.sachin.urlshortener.interceptor;

import com.sachin.urlshortener.model.IpTracking;
import com.sachin.urlshortener.repository.IpTrackingRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Instant;

import static com.sachin.urlshortener.common.constants.Constants.IP_BLACKLIST_MAX_INTERVAL_IN_SECONDS;
import static com.sachin.urlshortener.common.constants.Constants.X_FORWARDED_FOR;
import static com.sachin.urlshortener.common.messagecontants.MessageConstants.IP_BLOCKED_MESSAGE;

public class IpInterceptor implements HandlerInterceptor {

    @Autowired
    private IpTrackingRepository ipTrackingRepository;

    private static final int MAX_REQUESTS = 10;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Get the IP address from X-Forwarded-For header (if present)
        String ipAddress = request.getHeader(X_FORWARDED_FOR);
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }

        // Check if the IP is blocked
        IpTracking ipTracking = ipTrackingRepository.findByIpAddress(ipAddress).orElse(new IpTracking());
        if (ipTracking.isBlocked()) {
            //add logic to unblock and reset count if 5 minutes passed
            if (ipTracking.getLastRequestTime().plusSeconds(IP_BLACKLIST_MAX_INTERVAL_IN_SECONDS).isBefore(Instant.now())) {
                ipTracking.setBlocked(false);
                ipTracking.setRequestCount(0);
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write(IP_BLOCKED_MESSAGE);
                return false;
            }
        }

        // Increment the request count or create a new record
        if (ipTracking.getIpAddress() == null) {
            ipTracking.setIpAddress(ipAddress);
            ipTracking.setRequestCount(1);
            ipTracking.setLastRequestTime(Instant.now());
        } else {
            ipTracking.setRequestCount(ipTracking.getRequestCount() + 1);
            ipTracking.setLastRequestTime(Instant.now());
        }

        // If request count exceeds threshold, block the IP
        if (ipTracking.getRequestCount() > MAX_REQUESTS) {
            ipTracking.setBlocked(true);
            ipTrackingRepository.save(ipTracking); // Save blocked state
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(IP_BLOCKED_MESSAGE);
            return false;
        }

        ipTrackingRepository.save(ipTracking); // Save request count and timestamp
        return true;
    }
}
