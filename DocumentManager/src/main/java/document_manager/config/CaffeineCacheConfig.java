package document_manager.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.TimeUnit;

/*
    Created by KhaiTT
    Time: 10:57 7/27/2022
*/
@Configuration
public class CaffeineCacheConfig {
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("userList", "userId", "rotationList", "rotationDtoList", "rotationId", "incomingDocumentList", "incomingDocumentDtoList", "incomingDocumentId");
        cacheManager.setCaffeine(caffeineCacheBuilder());
        return cacheManager;
    }

    private Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
                .initialCapacity(300)
                .maximumSize(50000)
                .expireAfterAccess(1, TimeUnit.MINUTES)
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .recordStats();
    }
}