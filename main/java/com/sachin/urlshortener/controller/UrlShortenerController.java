package com.sachin.urlshortener.controller;

import com.sachin.urlshortener.common.constants.APIConstants;
import com.sachin.urlshortener.dto.LongUrlResponse;
import com.sachin.urlshortener.service.UrlShortenerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(APIConstants.API)
@RequiredArgsConstructor
public class UrlShortenerController {

    @Autowired
    private final UrlShortenerService urlShortenerService;

    /**
     * POST /api/shorten?longUrl=...&customAlias=...
     */
    @PostMapping(APIConstants.SHORTEN)
    public String shortenUrl(
            @RequestParam String longUrl,
            @RequestParam(required = false) String customAlias
    ) {
        return urlShortenerService.shortenUrl(longUrl, customAlias);
    }

    /**
     * GET /api/getLongUrl?shortUrl=...
     */
    @GetMapping(APIConstants.GET_LONG_URL)
    public ResponseEntity<LongUrlResponse> getLongUrl(@RequestParam String shortUrl) {
        return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT).body(urlShortenerService.getLongUrl(shortUrl));
    }

    /**
     * GET /api/usageCount?shortUrl=...
     */
    @GetMapping(APIConstants.USAGE_COUNT)
    public long getUsageCount(@RequestParam String shortUrl) {
        return urlShortenerService.getUsageCount(shortUrl);
    }
}
