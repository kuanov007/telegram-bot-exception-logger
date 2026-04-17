package uz.swe.telegramexceptionlogger.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ErrorMessageBuilder {

    @Value("${spring.application.name:N/A}")
    private String applicationName;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public String buildMessage(Exception ex, HttpServletRequest request, HttpStatus status, Map<String, Object> userInfo) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String ip = getClientIp(request);
        
        String userId = String.valueOf(userInfo.getOrDefault("id", "N/A"));
        String username = String.valueOf(userInfo.getOrDefault("username", "N/A"));
        String role = String.valueOf(userInfo.getOrDefault("role", "N/A"));

        String queryParams = getQueryParams(request);
        String pathVariables = getPathVariables(request);
        String body = getRequestBody(request);

        return String.format(
            "🚨 [%s] XATOLIK ANIQLANDI\n\n" +
            "🕒 Vaqt: %s\n" +
            "🔢 Status: %d\n" +
            "⚠️ Exception: %s\n" +
            "📩 Xabar: %s\n\n" +
            "🌐 Endpoint: %s %s\n" +
            "📍 User IP: %s\n" +
            "🆔 ID: %s | 👤 User: %s | 🎭 Role: %s\n\n" +
            "🔍 Parametrlar: %s\n" +
            "🛣️ Path Variables: %s\n\n" +
            "📦 Body:\n%s\n\n" +
            "📂 Location:\n%s",
            applicationName,
            timestamp,
            status.value(),
            ex.getClass().getName(),
            escapeHtml(ex.getMessage() != null ? ex.getMessage() : "N/A"),
            method,
            uri,
            ip,
            userId,
            username,
            role,
            queryParams,
            pathVariables,
            body,
            escapeHtml(ex.toString())
        );
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    private String getQueryParams(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap.isEmpty()) {
            return "N/A";
        }
        return parameterMap.entrySet().stream()
                .map(e -> e.getKey() + "=" + String.join(",", e.getValue()))
                .collect(Collectors.joining(", "));
    }

    private String getPathVariables(HttpServletRequest request) {
        Object pathVars = request.getAttribute("org.springframework.web.servlet.HandlerMapping.uriTemplateVariables");
        if (pathVars instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) pathVars;
            if (map.isEmpty()) return "N/A";
            return map.toString();
        }
        return "N/A";
    }

    private String getRequestBody(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper wrapper) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                String body = new String(buf, 0, buf.length, StandardCharsets.UTF_8);
                return body.length() > 2000 ? body.substring(0, 2000) + "... [truncated]" : body;
            }
        }
        return "N/A";
    }

    private String escapeHtml(String input) {
        if (input == null) return "N/A";
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
