package mvinuesa.reactor;

import static reactor.util.retry.Retry.withThrowable;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.retry.Retry;
import reactor.retry.RetryContext;
import reactor.test.StepVerifier;

public class TestRetry {

  private final Queue<RetryContext<?>> retries = new ConcurrentLinkedQueue<>();

  @Test
  public void fluxRetryFixedBackoff() {
    Flux<Integer> flux =
        Flux.concat(Flux.range(0, 2), Flux.error(new IOException()))
            .doOnNext(integer -> System.out.println("before: " + integer))
            .retryWhen(
                withThrowable(
                    Retry.any()
                        .fixedBackoff(Duration.ofMillis(500))
                        .retryOnce()
                        .doOnRetry(onRetry())))
            .doOnNext(integer -> System.out.println("after: " + integer));

    StepVerifier.create(flux)
        .expectNext(0, 1)
        //.expectNoEvent(Duration.ofMillis(300))
        //.thenAwait(Duration.ofMillis(300))
        .expectNext(0, 1)
        .expectErrorSatisfies(
            System.out::println)
        .verify();
  }

    @Test
    public void fluxRetryWithError() {
        Flux<?> flux =
           Flux.error(new IOException())
                .doOnNext(integer -> System.out.println("before: " + integer))
                .retryWhen(
                    withThrowable(
                        Retry.any()
                            .fixedBackoff(Duration.ofMillis(200))
                            .retryOnce()
                            .doOnRetry(onRetry())))
                .doOnNext(integer -> System.out.println("after: " + integer));

        StepVerifier.create(flux)
            //.thenAwait(Duration.ofMillis(300))
            .expectErrorSatisfies(
                System.out::println)
            .verify();
    }


    @Test
    public void fluxRetryConcatEmpty() {
        Mono<?> mono =
            Flux.concat(Flux.empty(), Flux.empty())
                .collectList()
                .map(this::checkCollection)
                .doOnError(throwable -> System.out.println("doOnError: " + throwable))
                .doOnNext(integer -> System.out.println("before: " + integer))
                .retryWhen(
                    withThrowable(
                        Retry.any()
                            .fixedBackoff(Duration.ofMillis(200))
                            .retryOnce()
                            .doOnRetry(onRetry())))
                .doOnNext(integer -> System.out.println("after: " + integer));

        StepVerifier.create(mono)
            //.thenAwait(Duration.ofMillis(300))
            .expectErrorSatisfies(
                System.out::println)
            .verify();
    }

    @Test
    public void fluxRetryEmpty() {
        Mono<?> mono =
            Flux.empty()
                .collectList()
                .map(this::checkCollection)
                .doOnError(throwable -> System.out.println("doOnError: " + throwable))
                .doOnNext(integer -> System.out.println("before: " + integer))
                .retryWhen(
                    withThrowable(
                        Retry.any()
                            .fixedBackoff(Duration.ofMillis(200))
                            .retryOnce()
                            .doOnRetry(onRetry())))
                .doOnNext(integer -> System.out.println("after: " + integer));

        StepVerifier.withVirtualTime(() -> mono)
            .thenAwait(Duration.ofMillis(300))
            .expectErrorSatisfies(
                System.out::println)
            .verify();
    }

    private Object checkCollection(List<Object> collection) {

        if (collection.isEmpty()) {
            throw new RuntimeException("elements not found ");
        } else {
            return collection.get(0);
        }
    }

    Consumer<? super RetryContext<?>> onRetry() {
    return context -> retries.add(context);
  }
}
