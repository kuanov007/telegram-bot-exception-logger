package uz.swe.telegramexceptionlogger.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import uz.swe.telegramexceptionlogger.config.TelegramProperties;

import java.util.Map;

@Slf4j
@Service
public class TelegramService {

    private final TelegramProperties properties;
    private final WebClient webClient = WebClient.builder().build();

    public TelegramService(TelegramProperties properties) {
        this.properties = properties;
    }

    public void sendMessage(String text) {
        if (!properties.isEnabled() || properties.getToken() == null || properties.getChatId() == null) {
            return;
        }

        String url = String.format("https://api.telegram.org/bot%s/sendMessage", properties.getToken());

        webClient.post()
                .uri(url)
                .bodyValue(Map.of(
                        "chat_id", properties.getChatId(),
                        "text", text,
                        "parse_mode", "HTML"
                ))
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(e -> log.error("Failed to send error message to Telegram: {}", e.getMessage()))
                .subscribe();
    }
}
