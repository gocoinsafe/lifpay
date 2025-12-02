package org.hcm.lifpay.filter;

import org.hcm.lifpay.util.CacheBodyUtil;
import org.springframework.stereotype.Component;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CacheBodyGatewayFilter implements Ordered, GlobalFilter{

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (exchange.getRequest().getHeaders().getContentType() == null) {
            return chain.filter(exchange);
        } else {

            return DataBufferUtils.join(exchange.getRequest().getBody())
                    .flatMap(dataBuffer -> {
                        return putCache(dataBuffer, exchange, chain);
                    });
        }
    }

    private Mono<Void> putCache(DataBuffer dataBuffer, ServerWebExchange exchange, GatewayFilterChain chain) {
        byte[] bytes = new byte[dataBuffer.readableByteCount()];

        dataBuffer.read(bytes);
        DataBufferUtils.release(dataBuffer);

        Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return Mono.just(buffer);
        });
        ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public Flux<DataBuffer> getBody() {
                return cachedFlux;
            }
        };
        ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
        mutatedExchange.getAttributes().put(CacheBodyUtil.CACHE_REQUEST_BODY_OBJECT_KEY, cachedFlux);
        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }


}
