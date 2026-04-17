package uz.swe.telegramexceptionlogger.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.swe.telegramexceptionlogger.service.TelegramService;
import uz.swe.telegramexceptionlogger.util.ErrorMessageBuilder;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final TelegramService telegramService;
    private final ErrorMessageBuilder errorMessageBuilder;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Map<String, Object> userInfo = getUserInfo();
        
        String message = errorMessageBuilder.buildMessage(ex, request, status, userInfo);
        telegramService.sendMessage(message);

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", java.time.LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", ex.getMessage());
        body.put("path", request.getRequestURI());

        return new ResponseEntity<>(body, status);
    }

    private Map<String, Object> getUserInfo() {
        Map<String, Object> info = new HashMap<>();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
                info.put("username", auth.getName());
                info.put("role", auth.getAuthorities().toString());
                info.put("id", "N/A");
            }
        } catch (NoClassDefFoundError | Exception e) {
        }
        return info;
    }
}
