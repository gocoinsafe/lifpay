package org.hcm.lifpay.util;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class RequestAddDataUtil {

    public static Mono<Void> addData(Map<String, String> addData, ServerWebExchange exchange, GatewayFilterChain chain, ServerHttpResponseDecorator decoratedResponse) {
        exchange.getAttributes().put("startTime", System.currentTimeMillis());
        if (exchange.getRequest().getMethod().equals(HttpMethod.POST)) {
            //重新构造request，参考ModifyRequestBodyGatewayFilterFactory
            ServerRequest serverRequest = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());
            MediaType mediaType = exchange.getRequest().getHeaders().getContentType();
            //重点
            Mono<String> modifiedBody = serverRequest.bodyToMono(String.class).flatMap(body -> {
                //因为约定了终端传参的格式，所以只考虑json的情况，如果是表单传参，请自行发挥
                if (MediaType.APPLICATION_JSON.isCompatibleWith(mediaType) || MediaType.APPLICATION_JSON_UTF8.isCompatibleWith(mediaType)) {
                    JSONObject jsonObject = JSONObject.parseObject(body);
                    Set<String> keys = addData.keySet();
                    for (String key : keys) {
                        jsonObject.put(key, addData.get(key));
                    }
                    XssUtil.cleanObj(jsonObject);
                    return Mono.just(jsonObject.toJSONString());
                }
                return Mono.empty();
            });
            BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
            HttpHeaders headers = new HttpHeaders();
            headers.putAll(exchange.getRequest().getHeaders());
            //之前修改了body但是没有重新写content length,http通信基于socket ,接收端通过报文长度来接收数据，所以要重新对报文长度修改
            headers.remove("Content-Length");
            //MyCachedBodyOutputMessage 这个类完全就是CachedBodyOutputMessage，只不过CachedBodyOutputMessage不是公共的
            MyCachedBodyOutputMessage outputMessage = new MyCachedBodyOutputMessage(exchange, headers);
            log.info("RequestAddDataUtil    add data");
            return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> {
                ServerHttpRequest decorator = decorate(exchange, headers, outputMessage);
                return returnMono(chain, exchange.mutate().request(decorator).response(decoratedResponse).build());
            }));
        } else {
            //代码扫描： 说删除不再使用的代码段，这里后期可能会用到，所以先保留
            //GET 验签
//            MultiValueMap<String, String> map = exchange.getRequest().getQueryParams();
//            if (!CollectionUtils.isEmpty(map)) {
//                String paramStr = map.getFirst("param");
//                try{
//                    verifySignature(paramStr);
//                }catch (Exception e){
//                    return processError(e.getMessage());
//                }
//            }
            return returnMono(chain, exchange);
        }
    }


    private static Mono<Void> returnMono(GatewayFilterChain chain, ServerWebExchange exchange) {
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            Long startTime = exchange.getAttribute("startTime");
            if (startTime != null) {
                long executeTime = (System.currentTimeMillis() - startTime);
                log.info("耗时：{}ms", executeTime);
                log.info("状态码：{}", Objects.requireNonNull(exchange.getResponse().getStatusCode()).value());
            }
        }));
    }


    private Mono processError(String message) {
        log.error(message);
        return Mono.error(new Exception(message));
    }

    private static ServerHttpRequestDecorator decorate(ServerWebExchange exchange, HttpHeaders headers, MyCachedBodyOutputMessage outputMessage) {
        return new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public HttpHeaders getHeaders() {
                long contentLength = headers.getContentLength();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.putAll(super.getHeaders());
                if (contentLength > 0L) {
                    httpHeaders.setContentLength(contentLength);
                } else {
                    httpHeaders.set("Transfer-Encoding", "chunked");
                }
                return httpHeaders;
            }

            @Override
            public Flux<DataBuffer> getBody() {
                return outputMessage.getBody();
            }
        };
    }
}
