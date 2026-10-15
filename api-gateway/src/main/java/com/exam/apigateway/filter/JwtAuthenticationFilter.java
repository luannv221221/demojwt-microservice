package com.exam.apigateway.filter;

import com.exam.apigateway.config.JwtUtils;
import com.exam.apigateway.config.RouterValidator;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;

import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component("JwtAuthenticationGatewayFilterFactory")
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter, Ordered{
    private final RouterValidator routerValidator;
    private final JwtUtils jwtUtils;


    // HTTP 401 khi loiS
    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus){
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("HIHI");
        ServerHttpRequest request = exchange.getRequest();
        if(routerValidator.predicate.test(request)){
            System.out.println("Check route");
            // check header Authorization co ton tai hay ko
            if(!request.getHeaders().containsHeader(HttpHeaders.AUTHORIZATION)){
                System.out.println("Thiey header");
                return this.onError(exchange,HttpStatus.UNAUTHORIZED);
            }

            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")){
                System.out.println("KO có hoạc ko theoa type");
                return this.onError(exchange,HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);
            if(jwtUtils.isInvalid(token)){
                System.out.println("Token saii");
                return this.onError(exchange,HttpStatus.UNAUTHORIZED);
            }

            Claims claims = jwtUtils.getAllClaim(token);
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("X-Auth-Userid",claims.get("userId",String.class))
                    .header("X-Auth-User",claims.getSubject())
                    .header("X-Auth-Roles",claims.get("roles",String.class))
                    .build();
            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        }

        // neu API la public /login, /register cho qua luon ko chekc
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }


}
