package org.example.urlshortener.service;

import lombok.RequiredArgsConstructor;
import org.example.urlshortener.entity.Url;
import org.example.urlshortener.repository.UrlRepository;
import org.example.urlshortener.utils.Base62;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;
    private final Base62 base62;
    private final RedisCacheManager cacheManager;

    @Cacheable(value = "longToShort", key = "#longUrl")
    public String shorten(String longUrl) {
        Optional<Url> existingUrl = urlRepository.findByLongUrl(longUrl);
        if (existingUrl.isPresent()) {
            return existingUrl.get().getShortUrl();
        }

        Url newUrl = new Url();
        newUrl.setLongUrl(longUrl);
        urlRepository.save(newUrl);

        String shortUrl = base62.encode(newUrl.getId());
        newUrl.setShortUrl(shortUrl);
        urlRepository.save(newUrl);

        Objects.requireNonNull(cacheManager.getCache("longToShort")).put(longUrl, newUrl);

        return shortUrl;
    }

    @Cacheable(value = "shortToLong", key = "#shortUrl")
    public String getOriginalUrl(String shortUrl) {
        return urlRepository.findByShortUrl(shortUrl).map(Url::getLongUrl).orElseThrow(RuntimeException::new);
    }
}
