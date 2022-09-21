package mvinuesa.reactor;

import static java.lang.System.out;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuples;

public class TestFlows {



    @Test
    @DisplayName("Test Flow")
    void testErrorHandlingWithSwitch() {
        Flux<String> flux = Flux.just("Raul", "Fernando", "Mario");
        flux.filter("Fernando"::equals)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Name not found")))
            .doOnNext(name -> out.println("doOnNext: " + name))
            .subscribe(name -> out.println("Ok name" + name),
                throwable -> out.println("Error: " + throwable),
                () -> out.println("End correctly"));
    }

    @Test
    @DisplayName("Test Flow")
    void testFlux() {
        Flux.range(0, 8)
            .doOnNext(integer -> System.out.println(integer))
            .map(integer -> integer + 1)
            .filter(integer -> integer % 2 == 0)
            .buffer(2)
            .subscribe(
                integers -> System.out.println(integers),
                throwable -> System.out.println(throwable),
                () -> System.out.println("End")
            );
    }

    @DisplayName("Error in flux with onErrorResume handling flux empty")
    @Test
    void errorInFluxWithOnErrorResumeHandlingFluxEmpty() {

        Mono<Integer> test =
            Flux.range(1, 5)
                .reduce(Integer::sum);

        StepVerifier
            .create(test)
            .expectNext(2)
            .verifyComplete();
    }

    @DisplayName("Error in flux with onErrorResume handling flux empty")
    @Test
    void errorInFluxWithOlnErrorResumeHandlingFluxEmpty() {

        Mono<Integer> test =
            Flux.range(1, 5)
                .map(integer -> integer == 2 ? integer / 0 : integer)
                .reduce(Integer::sum);

        StepVerifier
            .create(test)
            .expectErrorSatisfies(
                throwable -> {
                    Assertions.assertEquals(ArithmeticException.class, throwable.getClass());
                    Assertions.assertEquals("/ by zero" , throwable.getMessage());
                })
            .verify();

        Tuples.of("String", 8);
    }

    @Test
    @DisplayName("Test Flow")
    void testFluxFilter12() {
        Flux.range(0, 8)
            .map(integer -> integer + 1)
            .flatMap(integer -> Flux.error(new NullPointerException()))
            .doOnNext(integer -> System.out.println(integer))
            .buffer(2)
            .subscribe(
                integers -> System.out.println(integers),
                throwable -> System.out.println("Error" + throwable),
                () -> System.out.println("End")
            );
    }

    @Test
    @DisplayName("Test Flow")
    void testFluxFilterDivide0() {
        Flux.range(0, 8)
            .map(integer -> integer + 1)
            .flatMap(
                integer -> getIntegerFlux(integer))
            .onErrorResume(
                err -> {
                    System.out.println("onErrorResume" + err);
                    return Mono.empty();
                })
            .buffer(2)
            .subscribe(
                integers -> System.out.println(integers),
                throwable -> System.out.println("Error" + throwable),
                () -> System.out.println("End"));
    }

    @Test
    @DisplayName("Test Flow")
    void testFluxFilterDivide0ErrorManage() {
        Flux.range(0, 8)
            .map(integer -> integer + 1)
            .flatMap(
                integer -> getIntegerFlux(integer).onErrorResume(err -> {
                    System.out.println("Inner onErrorResume" + err);
                    return Mono.empty();
                }))
            .onErrorResume(
                err -> {
                    System.out.println("onErrorResume" + err);
                    return Mono.empty();
                })
            .buffer(2)
            .subscribe(
                integers -> System.out.println(integers),
                throwable -> System.out.println("Error" + throwable),
                () -> System.out.println("End"));
    }

    private Flux<Integer> getIntegerFlux(Integer integer) {
        if (integer == 3) {
            return Flux.error(new NullPointerException("Error"));
        } else {
            return Flux.just(integer);
        }
    }
}
