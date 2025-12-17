package org.hcm.lifpay.subscriber;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscription;
import org.slf4j.MDC;
import reactor.core.CoreSubscriber;
import reactor.util.context.Context;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Reactor订阅
 * @param <T>
 */
@Slf4j
public class TraceReactorSubscriber<T> implements CoreSubscriber<T> {
    private CoreSubscriber coreSubscriber;

    public TraceReactorSubscriber(CoreSubscriber coreSubscriber) {
        this.coreSubscriber = coreSubscriber;
    }

    @Override
    public void onNext(T t) {
        // Reactor响应式中的链路传递
        copyToMdc(coreSubscriber.currentContext());
        coreSubscriber.onNext(t);
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        coreSubscriber.onSubscribe(subscription);
    }

    @Override
    public void onComplete() {
        coreSubscriber.onComplete();
    }

    @Override
    public void onError(Throwable throwable) {
        coreSubscriber.onError(throwable);
    }

    @Override
    public Context currentContext() {
        return coreSubscriber.currentContext();
    }

    private void copyToMdc(Context context) {
        if (!context.isEmpty()) {
            Map<String, String> map = context.stream()
                    .collect(Collectors.toMap(e -> e.getKey().toString(), e -> e.getValue().toString()));
            MDC.setContextMap(map);
//            log.info("MdcSubscriber.copyToMdc: {}", map);
        }
    }
}
