package uz.swe.telegramexceptionlogger.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import uz.swe.telegramexceptionlogger.exception.GlobalExceptionHandler;
import uz.swe.telegramexceptionlogger.filter.RequestCachingFilter;
import uz.swe.telegramexceptionlogger.service.TelegramService;
import uz.swe.telegramexceptionlogger.util.ErrorMessageBuilder;

@AutoConfiguration
@EnableConfigurationProperties(TelegramProperties.class)
@ConditionalOnProperty(prefix = "telegram.bot", name = "enabled", havingValue = "true")
@Import({GlobalExceptionHandler.class, RequestCachingFilter.class, ErrorMessageBuilder.class})
public class TelegramExceptionLoggerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TelegramService telegramService(TelegramProperties properties) {
        return new TelegramService(properties);
    }
}
