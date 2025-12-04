package org.hcm.lifpay.filter;

import cn.hutool.core.lang.Pair;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;

import org.hcm.lifpay.common.GwResultEnum;
import org.hcm.lifpay.common.ResultEnum;
import org.hcm.lifpay.redis.RedisDBKey;
import org.hcm.lifpay.util.*;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.*;

@RefreshScope
@Component
@Slf4j
public class CheckGlobalFilter implements GlobalFilter, Ordered {


    static final String TOKEN_NAME = "LifpayToken";
    //redis 锁
//    public static final String SCRIPT_LOCK = "if redis.call('setnx', KEYS[1], ARGV[1]) == 1 then redis.call('pexpire', KEYS[1], ARGV[2]) return 1 else return 0 end";

    public static final String SCRIPT_LOCK = "if redis.call('setNx',KEYS[1],ARGV[1]) then if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('expire',KEYS[1],ARGV[2]) else return 0 end end";


    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${sign.privateKey}")
    private String privateKey;
    @Value("${sign.publicKey}")
    private String publicKey;
    @Value("${sign.keyId}")
    private String keyId;
    @Value("${rsa.privateKey}")
    private String rsaPrivateKey;

    @Value("#{'${exportUrlList}'.split(',')}")
    private List<String> exportUrlList;


    private final String getServerKeyIdUrl = "/api/serverKeyId/get";

    private final List<String> needRsaUrl = Arrays.asList("/wapi/admin/user/login");

    @Value("#{'${whiteList}'.split(',')}")
    List<String> whiteList;

    private static final String VALUE = "0x";
    private static final Integer MIN = 1;
    private static final Integer SECOND = 60;
    private static final Integer MILLISECOND = 1000;

    @Value("${tokenExpireTime.token_Expire_Time:18000}")
    private int tokenValidTime;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String uri = request.getPath().value();
        log.info("uri =======" + uri);
        Flux<DataBuffer> cachedBody = exchange.getAttribute(CacheBodyUtil.CACHE_REQUEST_BODY_OBJECT_KEY);
        log.info("cachedBody====" + JSON.toJSONString(cachedBody));
        if (cachedBody == null) {
            return getErrorRequestMono(exchange);
        }
        String raw = CacheBodyUtil.toRaw(cachedBody);
        log.info("raw==" + raw);
        JSONObject bodyJson = JSONObject.parseObject(raw);
        //防重放
        String timestamp = bodyJson.getString("timestamp");
        String requestId = bodyJson.getString("requestId");
        Pair<Boolean, Mono<Void>> replayAttackResult = preventReplayAttack(exchange, timestamp, requestId);
        log.info("replayAttackResult ===" + JSON.toJSONString(replayAttackResult));
        if (!replayAttackResult.getKey()) {
            return replayAttackResult.getValue();
        }
        if (getServerKeyIdUrl.equals(uri)) {
            return getServerKeyMono(exchange);
        }
        String aesKey = publicKey;
        Map<String, String> data = new HashMap<>(4);
        putKeys(uri, aesKey, timestamp, data);
        //请求此uri是否需要登录
        if (isInWileList(uri)) {
            log.info("白名单url uri = " + uri);
            return RequestAddDataUtil.addData(data, exchange, chain, signResponse(exchange));
        }

        //从cookie中获取token
        String jwtToken = getToken(request.getCookies());
        //校验jwtToken的合法性
        if (jwtToken != null) {
            Pair<Boolean, Mono<Void>> result = verifyTokenAndSign(exchange, jwtToken, raw, data);
            if (!result.getKey()) {
                return result.getValue();
            }
            //导出的数据不做响应的处理
            if (exportUrlList.contains(uri)) {
                log.info("exportUrlList.{}.return", uri);
                return chain.filter(exchange);
            }

            return RequestAddDataUtil.addData(null, exchange, chain, signResponse(exchange));
        }
        log.info("用户未登录……");
        return getErrorVoidMono(exchange);
    }

    /**
     * 检验token的合法性
     *
     * @param exchange exchange
     * @param jwtToken token
     * @param raw      Body raw
     * @param data     data 转发至其它微服务携带参数
     */
    private Pair<Boolean, Mono<Void>> verifyTokenAndSign(ServerWebExchange exchange, String jwtToken, String raw, Map<String, String> data) {
        log.info("jwtToken=" + jwtToken);
        String[] tokens = jwtToken.split(" ");
        String token = jwtToken;
        if (tokens.length > 1) {
            token = tokens[1];
        }

        JSONObject tokenInfo = getTokenInfo(token);
        if (tokenInfo == null) {
            return new Pair<>(false, getErrorVoidMono(exchange));
        }

        // 获取token 中的客户端公钥
        String publicKey = tokenInfo.getString("publicKey");

        Pair<Boolean, Mono<Void>> verifySignResult = verifySign(exchange, publicKey, raw);
        if (!verifySignResult.getKey()) {
            return verifySignResult;
        }

        String userId = tokenInfo.getString("userId");
        if (userId == null) {
            return new Pair<>(false, getErrorVoidMono(exchange));
        }

        boolean tokenValid = advanceInvalidToken(tokenInfo.getString("timestamp"), userId, token);
        if (!tokenValid) {
            return new Pair<>(false, getErrorVoidMono(exchange));
        }
        // 将用户id作为参数传递下去
        data.put("userId", userId);
        // 根据public key生成deviceId
        data.put("deviceId", getUserDeviceIdFrmPubKey(publicKey));
        return new Pair<>(true, null);
    }

    private boolean advanceInvalidToken(String timestamp, String userId, String token) {
        long tokenTimestamp = Long.parseLong(timestamp);
        long count = tokenTimestamp + tokenValidTime * 1000L - 60000;
        if (System.currentTimeMillis() > count) {
            log.error("error: token timeout soon; userId:{}; token:{}", userId, token);
            return false;
        }
        return true;
    }

    private Pair<Boolean, Mono<Void>> verifySign(ServerWebExchange exchange, String publicKey, String raw) {
        String sign = exchange.getRequest().getHeaders().getFirst("signature");
        if (sign == null) {
            log.error("sign is null");
            return new Pair<>(false, getSignErrorMono(exchange));
        }
        // 验签
        boolean checkSign = ECUtil.verify(raw, sign, publicKey, ECUtil.SECP256R1);
        if (!checkSign) {
            log.error("publicKey:{} ,checkSign:{}", publicKey, checkSign);
            return new Pair<>(false, getSignErrorMono(exchange));
        }
        return new Pair<>(true, null);
    }

    /**
     * 获取token信息
     *
     * @param token token
     */
    private JSONObject getTokenInfo(String token) {
        log.info("token = " + token);
//        String jsonToken = getUserIdFromToken(token);
        String jsonToken = null;
        if (jsonToken == null) {
            log.error("error: TOKEN IS NULL;  token:{}", token);
            return null;
        }
        JSONObject jsonObject;
        try {
            jsonObject = JSONObject.parseObject(jsonToken);
        } catch (Exception e) {
            log.error("error: TOKEN IS ERROR; token:{}", token, e);
            return null;
        }
        return jsonObject;
    }

    public String getUserIdFromToken(String token) {
        String tokenKey = String.format(RedisDBKey.GET_USER_ID_BY_TOKEN, token);

        Object value = redisTemplate.opsForValue().get(tokenKey);
        if (null == value) {
            return "";
        }
        return JSON.toJSONString(value);
    }

    private void putKeys(String uri, String aesKey, String timestamp, Map<String, String> data) {
        data.put("aesKey", aesKey);
        if (needRsaUrl.contains(uri)) {
            String encrypt = rsaPrivateKey + "&&" + timestamp;
            data.put("privateContent", AESCBCUtils.encrypt(encrypt, aesKey.substring(0, 16)));
        }
    }

    /**
     * 校验是否重复提交
     *
     * @param exchange     exchange
     * @param timestampStr 时间戳
     * @param requestId    请求id
     */
    private Pair<Boolean, Mono<Void>> preventReplayAttack(ServerWebExchange exchange, String timestampStr, String requestId) {
        long time = (long) MIN * SECOND * MILLISECOND;
        if (StringUtils.isNotEmpty(requestId)) {
//            String requestKey = String.format(RedisKey.REQUEST_CHECK, requestId);
            //如果redis那边存在相同的requestId ，返回错误的请求
            String requestKey = "";
            try {

                RedisScript<Long> redisScript = new DefaultRedisScript<>(SCRIPT_LOCK, Long.class);

                Object result = redisTemplate.execute(redisScript, new StringRedisSerializer(), new StringRedisSerializer(), Collections.singletonList(requestKey), requestId, time + "");

                if (null == result || "0".equals(result.toString())) {
                    return new Pair<>(false, getErrorRequestMono(exchange));
                }
            } catch (Exception exception) {
                return new Pair<>(false, getErrorRequestMono(exchange));
            }
        }

        long timestamp;
        try {
            timestamp = Long.parseLong(timestampStr);
        } catch (Exception e) {
            log.error("param timestamp error", e);
            return new Pair<>(false, getParamErrorMono(exchange));
        }
        //防重放
        if (System.currentTimeMillis() - timestamp > time) {
            return new Pair<>(false, getErrorRequestMono(exchange));
        }
        return new Pair<>(true, null);
    }

    private String getToken(MultiValueMap<String, HttpCookie> cookieMultiValueMap) {
        String jwtToken = null;
        List<HttpCookie> cookies = cookieMultiValueMap.get(TOKEN_NAME);
        if (cookies != null && cookies.size() > 0) {
            jwtToken = cookies.get(0).getValue();
        }
        return jwtToken;
    }


    /**
     * 判断是否在白名单里面
     *
     * @param path 请求地址
     */
    public boolean isInWileList(String path) {
        if (StringUtils.isBlank(path)) {
            return false;
        }
        if (CollectionUtils.isEmpty(whiteList)) {
            return false;
        }
        return whiteList.contains(path);

    }

    private String getUserDeviceIdFrmPubKey(String publicKey) {
        if (publicKey.startsWith(VALUE)) {
            return publicKey.substring(0, 22);
        }
        return publicKey.substring(0, 20);
    }

    private Mono<Void> getServerKeyMono(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", ResultEnum.SUCCESS.getCode());
        jsonObject.put("message", ResultEnum.SUCCESS.getMsg());
        jsonObject.put("timestamp", System.currentTimeMillis());
        JSONObject data = new JSONObject();
        data.put("serverKeyId", keyId);
        jsonObject.put("data", data);
        String jsonStr = jsonObject.toJSONString();
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(jsonStr.getBytes());
        return response.writeWith(Mono.just(bodyDataBuffer));
    }

    private Mono<Void> getSignErrorMono(ServerWebExchange exchange) {
        return getErrorMono(exchange, GwResultEnum.SIGN_ERROR);
    }

    private Mono<Void> getErrorVoidMono(ServerWebExchange exchange) {
        return getErrorMono(exchange, GwResultEnum.NO_LOGIN);
    }

    private Mono<Void> getErrorRequestMono(ServerWebExchange exchange) {
        return getErrorMono(exchange, GwResultEnum.REQUEST_ERROR);
    }

    private Mono<Void> getParamErrorMono(ServerWebExchange exchange) {
        return getErrorMono(exchange, GwResultEnum.PARAM_ERROR);
    }


    private Mono<Void> getErrorMono(ServerWebExchange exchange, GwResultEnum resultEnum) {
        //不合法(响应未登录的异常)
        ServerHttpResponse response = exchange.getResponse();
        //设置headers
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        //设置body
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", resultEnum.getCode());
        jsonObject.put("message", resultEnum.getMsg());
        jsonObject.put("timestamp", System.currentTimeMillis());
        String jsonStr = jsonObject.toJSONString();
        byte[] signature = ECUtil.sign(jsonStr, privateKey, ECUtil.SECP256R1);
        httpHeaders.add("signature", Hex.toHexString(signature));
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(jsonStr.getBytes());
        return response.writeWith(Mono.just(bodyDataBuffer));
    }


    private ServerHttpResponseDecorator signResponse(ServerWebExchange exchange) {
        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();
        return new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                    return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                        // probably should reuse buffers
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        dataBuffers.forEach(dataBuffer -> {
                            byte[] content = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(content);
                            DataBufferUtils.release(dataBuffer);
                            try {
                                os.write(content);
                            } catch (Exception e) {
                                log.error("--list.add--error", e);
                            }
                        });
                        String s = os.toString();
                        JSONObject jsonObject = JSON.parseObject(s);
                        transferLongToString(jsonObject);
                        byte[] uppedContent = jsonObject.toJSONString().getBytes();
                        byte[] signature = ECUtil.sign(HashUtil.sha256(uppedContent), Hex.decode(privateKey), ECUtil.SECP256R1);
                        String returnSign = Hex.toHexString(signature);
                        HttpHeaders httpHeaders = this.getDelegate().getHeaders();
                        httpHeaders.set("signature", returnSign);
                        return bufferFactory.wrap(uppedContent);
                    }));
                }
                // if body is not a flux. never got there.
                return super.writeWith(body);
            }
        };
    }

    @Override
    public int getOrder() {
        return -2;
    }

    private void transferLongToString(JSONObject jsonObject) {
        jsonObject.forEach((key, value) -> {
            if (value instanceof Long ) {
                jsonObject.put(key, value.toString());
            }
            if(value instanceof BigDecimal){
                jsonObject.put(key,((BigDecimal) value).toPlainString());
            }
            if (value instanceof Boolean) {
                jsonObject.put(key, (Boolean) value ? 1 : 0);
            }
            if (value instanceof JSONObject) {
                transferLongToString((JSONObject) value);
            }
            if (value instanceof JSONArray) {
                jsonObject.put(key, transferLongToStringOfJsonArray((JSONArray) value));
            }
        });
    }

    private JSONArray transferLongToStringOfJsonArray(JSONArray jsonArray) {
        JSONArray tmp = new JSONArray();
        jsonArray.forEach(e -> {
            if (e instanceof Long) {
                tmp.add(e.toString());
            }
            if (e instanceof Boolean) {
                tmp.add((Boolean) e ? 1 : 0);
            }
            if (e instanceof JSONObject) {
                transferLongToString((JSONObject) e);
                tmp.add(e);
            }
            if (e instanceof JSONArray) {
                tmp.add(transferLongToStringOfJsonArray((JSONArray) e));
            }
        });
        return tmp;
    }

}
