package com.exam.apigateway.config;


import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouterValidator {
    public static final List<String> openApiEndpoints = List.of(
            "/api/auth/login",
            "/api/auth/register",
            "/api/auth/refreshToken"
    );

    // kiem tra xem request di qua co nam trong danh sachs can bao ve hayko

    public Predicate<ServerHttpRequest> predicate = serverHttpRequest ->
            openApiEndpoints.stream().noneMatch(uri->serverHttpRequest.getURI().getPath().contains(uri));

}
