package mvinuesa.reactor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * This class test Reactor error handling
 */
public class TestReactorErrorHandling {


    @Test
    @DisplayName("Test error handling with switch Mono error")
    void testErrorHandlingWithSwitch() {
        Flux<String> flux = Flux.just("Raúl", "Fernando", "Mario");
        flux.filter("Carlos"::equals)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Name not found")))
            .doOnNext(name -> System.out.println("doOnNext: " + name))
            .subscribe(name -> System.out.println("Ok name" + name),
                throwable -> System.out.println("Error: " + throwable));
    }

    @Test
    @DisplayName("Test error handling with switch Mono error onErrorContinue")
    void testErrorHandlingWithSwitchErrorContinue() {
        Flux<String> flux = Flux.just("Raúl", "Fernando", "Mario");
        flux.filter("Carlos"::equals)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Name not found")))
            .onErrorContinue((throwable, o) -> System.out.println("Error continue: " + o))
            .doOnNext(name -> System.out.println("doOnNext: " + name))
            .subscribe(name -> System.out.println("Ok name" + name),
                throwable -> System.out.println("Error: " + throwable));
    }

    @Test
    @DisplayName("Test error handling with switch Mono error onErrorContinue")
    void testErrorHandlingWithThrowException() {
        Flux<String> flux = Flux.just("Raúl", "Fernando", "Mario");
        flux.filter("Fernando"::equals)
            .doOnNext(s -> {throw new IllegalArgumentException("Name not found");})
            .onErrorContinue((throwable, o) -> System.out.println("Error continue: " + o))
            .doOnNext(name -> System.out.println("doOnNext: " + name))
            .subscribe(name -> System.out.println("Ok name" + name),
                throwable -> System.out.println("Error: " + throwable));
    }

    @Test
    @DisplayName("Test error handling with switch Mono error onErrorContinue")
    void testErrorHandlingWithThrowException2() {
        Flux<String> flux = Flux.just("Raúl", "Fernando", "Mario");
        flux
            .map(s -> {
                if ("Fernando".equals(s)) {
                    throw new IllegalArgumentException("Name not found");
                }
                return s;
            })
            .onErrorContinue((throwable, o) -> System.out.println("Error continue: " + o))
            .doOnNext(name -> System.out.println("doOnNext: " + name))
            .subscribe(name -> System.out.println("Ok name" + name),
                throwable -> System.out.println("Error: " + throwable));
    }

    @Test
    @DisplayName("Test error handling with switch Mono error onErrorContinue")
    void testErrorHandlingWithThrowExceptionWithoutErrorContinue() {
        Flux<String> flux = Flux.just("Raúl", "Fernando", "Mario");
        flux
            .map(s -> {
                if ("Fernando".equals(s)) {
                    throw new IllegalArgumentException("Name not found");
                }
                return s;
            })
            .doOnNext(name -> System.out.println("doOnNext: " + name))
            .subscribe(name -> System.out.println("Ok name" + name),
                throwable -> System.out.println("Error: " + throwable));
    }

    @Test
    @DisplayName("Test error handling with switch Mono error onErrorContinue")
    void testErrorHandlingWithThrowExceptionWithoutErrorContinueMonoError() {
        Flux<String> flux = Flux.just("Raúl", "Fernando", "Mario");
        flux
            .flatMap(s -> {
                if ("Fernando".equals(s)) {
                    return Mono.error(new IllegalArgumentException("Name not found"));
                }
                return Mono.just(s);
            })
            .doOnNext(name -> System.out.println("doOnNext: " + name))
            .subscribe(name -> System.out.println("Ok name" + name),
                throwable -> System.out.println("Error: " + throwable));
    }


    @Test
    @DisplayName("Test error handling with switch Mono error onErrorContinue")
    void testErrorHandlingWithThrowExceptionWithoutErrorContinueDoubleFlux() {
        Flux<String> flux = Flux.just("Raúl", "Fernando", "Mario");
        flux
            .flatMap(s -> getNewName())
            .doOnNext(name -> System.out.println("doOnNext: " + name))
            .subscribe(name -> System.out.println("Ok name" + name),
                throwable -> System.out.println("Error: " + throwable));
    }


    @Test
    @DisplayName("Test error handling with mono return void")
    void testErrorHandlingWithThrowExceptionWithoutCallToMonoReturnVoid() {
        Flux<String> flux = Flux.just("Raúl", "Fernando", "Mario");
        flux
            .doOnNext(name -> {
                System.out.println("processMono with name: " + name);
                processMono(name);
            })
            .subscribe(name -> System.out.println("Ok name" + name),
                throwable -> System.out.println("Error: " + throwable));
    }

    @Test
    @DisplayName("Test error handling with switch add element")
    void testErrorHandlingWithSwitchAddElement() {
        Flux<String> flux = Flux.just("Raúl", "Fernando", "Mario");
        flux.filter("Carlos"::equals)
            .switchIfEmpty(Mono.just("Juan"))
            .onErrorContinue((throwable, o) -> System.out.println("Error" + o))
            .doOnNext(name -> System.out.println("doOnNext: " + name))
            .subscribe(name -> System.out.println("Ok name: " + name),
                throwable -> System.out.println("Error: " + throwable));
    }

    @Test
    @DisplayName("Test error handling without switch")
    void testErrorHandlingWithoutSwitch() {
        Flux<String> flux = Flux.just("Raúl", "Fernando", "Mario");
        flux.filter("Carlos"::equals)
            .subscribe(name -> System.out.println("Ok name" + name),
                throwable -> System.out.println("Error: " + throwable),
                () -> System.out.println("Finally"));
    }


    @Test
    @DisplayName("Test error handling with mono return void")
    void testErrorHandlingWithThrowExceptionWithoutCallToMonoReturnError() {
        Flux<String> flux = Flux.just("Raúl", "Fernando", "Mario");
        flux
            .doOnNext(name -> {
                System.out.println("processMono with name: " + name);
                processMonoWithError(name);
            })
            .subscribe(name -> System.out.println("Ok name" + name),
                throwable -> System.out.println("Error: " + throwable));
    }

    @Test
    @DisplayName("Test error handling with mono return void")
    void testErrorHandlingWithThrowExceptionWithoutCallToMonoReturnErrorWithoutErrorHandling() {
        Flux<String> flux = Flux.just("Raúl", "Fernando", "Mario");
        flux
            .doOnNext(name -> {
                System.out.println("processMono with name: " + name);
                processMonoWithError(name);
            })
            .subscribe(name -> System.out.println("Ok name" + name));
    }

    @Test
    @DisplayName("Test error handling with mono return error on error continue")
    void testErrorHandlingWithThrowExceptionWithoutCallToMonoReturnErrorContinue() {
        Flux<String> flux = Flux.just("Raúl", "Fernando", "Mario");
        flux
            .doOnNext(name -> {
                System.out.println("processMono with name: " + name);
                processMonoWithError(name);
            })
            .onErrorContinue((throwable, o) -> System.out.println("Error" + o))
            .subscribe(name -> System.out.println("Ok name" + name),
                throwable -> System.out.println("Error: " + throwable));
    }

    @Test
    @DisplayName("Test error handling with mono return error on error processMonoWithErrorErrorContinue")
    void testErrorHandlingWithThrowExceptionWithoutCallToMonoProcessMonoWithErrorErrorContinue() {
        Flux<String> flux = Flux.just("Raúl", "Fernando", "Mario");
        flux
            .doOnNext(name -> {
                System.out.println("processMono with name: " + name);
                processMonoWithErrorErrorContinue(name);
            })
            .onErrorContinue((throwable, o) -> System.out.println("Error" + o))
            .subscribe(name -> System.out.println("Ok name" + name),
                throwable -> System.out.println("Error: " + throwable));
    }

    @Test
    @DisplayName("Test error handling with mono return error on error processMonoWithErrorHandling")
    void testErrorHandlingWithThrowExceptionWithoutCallToMonoProcessMonoWithErrorHandling() {
        Flux<String> flux = Flux.just("Raúl", "Fernando", "Mario");
        flux
            .doOnNext(name -> {
                System.out.println("processMono with name: " + name);
                processMonoWithErrorHandling(name);
            })
            .onErrorContinue((throwable, o) -> System.out.println("Error: " + o))
            .subscribe(name -> System.out.println("Ok name (1): " + name),
                throwable -> System.out.println("Error: " + throwable));
    }


    @Test
    @DisplayName("Test error handling with mono return error on error processMonoWithErrorHandling")
    void testErrorHandlingWithThrowExceptionWithoutCallToMono() {
        Flux<String> flux = Flux.just("Raúl", "Fernando", "Mario");
        flux
            .flatMap(name -> {
                System.out.println("processMono with name: " + name);
                return monoWithError(name);
            })
            .onErrorContinue((throwable, o) -> System.out.println("onErrorContinue: " + o))
            .subscribe(name -> System.out.println("Ok name (1): " + name),
                throwable -> System.out.println("Error: " + throwable));
    }

    @Test
    @DisplayName("Test error handling with mono return error on error processMonoWithErrorHandling without error continue")
    void testErrorHandlingWithThrowExceptionWithoutCallWithoutErrorContinue() {
        Flux<String> flux = Flux.just("Raúl", "Fernando", "Mario");
        flux
            .flatMap(name -> {
                System.out.println("processMono with name: " + name);
                return monoWithError(name);
            })
            .subscribe(name -> System.out.println("Ok name (1): " + name),
                throwable -> System.out.println("Error: " + throwable));
    }


    @Test
    @DisplayName("Test error handling with mono return error on error processMonoWithErrorHandling")
    void testErrorHandlingWithThrowExceptionWithoutCallToMonoWithMono() {
        Mono<String> mono = Mono.just("Raúl");
        mono
            .flatMap(name -> {
                System.out.println("processMono with name: " + name);
                return monoWithError(name);
            })
            .onErrorContinue((throwable, o) -> System.out.println("onErrorContinue: " + o))
            .subscribe(name -> System.out.println("Ok name (1): " + name),
                throwable -> System.out.println("Error: " + throwable));
    }

    @Test
    @DisplayName("Test error handling with mono return error on error processMonoWithErrorHandling")
    void testErrorHandlingWithThrowExceptionWithoutCallToMonoWithMonoWithoutErrorContinue() {
        Mono<String> mono = Mono.just("Raúl");
        mono
            .flatMap(name -> {
                System.out.println("processMono with name: " + name);
                return monoWithError(name);
            })
            .subscribe(name -> System.out.println("Ok name (1): " + name),
                throwable -> System.out.println("Error: " + throwable));
    }


    @Test
    @DisplayName("Test error handling range with 1 to 10")
    void testErrorHandlingRanget1To10() {
        Flux<Integer> s = Flux.range(1, 10)
            .map(this::doubleElement)
            .map(this::doSecondTransformWithError);
        s.subscribe(value -> System.out.println("RECEIVED " + value),
            error -> System.err.println("CAUGHT " + error)
        );
    }

    @Test
    @DisplayName("Test error handling range with 1 to 10")
    void testErrorHandlingRanget1To10ErrorMono() {
        Flux<Integer> s = Flux.range(1, 10)
            .map(this::doubleElement)
            .flatMap(this::doSecondTransformWithErrorMono);
        s.onErrorContinue((throwable, o) -> System.out.println("onErrorContinue: " + o))
            .subscribe(value -> System.out.println("RECEIVED " + value),
            error -> System.err.println("CAUGHT " + error)
        );
    }

    @Test
    @DisplayName("testErrorHandlingRanget1To10OnErrorContinue")
    void testErrorHandlingRanget1To10OnErrorContinue() {
        Flux<Integer> s = Flux.range(1, 10)
            .map(this::doubleElement)
            .map(this::doSecondTransformWithError);
        s.onErrorContinue((throwable, o) -> System.out.println("onErrorContinue: " + o))
            .subscribe(value -> System.out.println("RECEIVED " + value)
        );
    }

    @Test
    @DisplayName("testErrorHandlingRanget1To10OnErrorContinueAndError")
    void testErrorHandlingRanget1To10OnErrorContinueAndError() {
        Flux<Integer> s = Flux.range(1, 10)
            .map(this::doubleElement)
            .map(this::doSecondTransformWithError);
        s.onErrorContinue((throwable, o) -> System.out.println("onErrorContinue: " + o))
            .subscribe(value -> System.out.println("RECEIVED " + value),
                error -> System.err.println("CAUGHT " + error)
            );
    }

    @Test
    @DisplayName("testErrorHandlingRanget1To10OnErrorContinueAndError")
    void testErrorHandlingRanget1To10OnErrorContinueAndErrorFlux1() {
        Flux<Integer> s = Flux.range(5, 1)
            .map(this::doubleElement)
            .flatMap(this::doSecondTransformWithMonoError);
        s.onErrorContinue((throwable, o) -> System.out.println("onErrorContinue: " + o))
            .subscribe(value -> System.out.println("RECEIVED " + value),
                error -> System.err.println("CAUGHT " + error)
            );
    }

    @Test
    @DisplayName("testErrorHandlingRanget1To10OnErrorContinueAndError")
    void testErrorHandlingRanget1To10OnErrorContinueAndErrorFlux1Close() {
        Flux<Integer> s = Flux.range(5, 1)
            .map(this::doubleElement)
            .flatMap(this::doSecondTransformWithMonoError);
        s.subscribe(value -> System.out.println("RECEIVED " + value),
                error -> System.err.println("CAUGHT " + error)
            );
    }

    @Test
    @DisplayName("testErrorHandlingRanget1To10OnErrorContinueAndError")
    void testErrorHandlingRanget1To10OnMonoErrorContinueAndError() {
        Mono<Integer> s = Mono.just(5)
            .map(this::doubleElement)
            .map(this::doSecondTransformWithError);
        s.onErrorContinue((throwable, o) -> System.out.println("onErrorContinue: " + o))
            .subscribe(value -> System.out.println("RECEIVED " + value),
                error -> System.err.println("CAUGHT " + error)
            );
    }


    @Test
    @DisplayName("testErrorHandlingRanget1To10OnErrorContinueAndError")
    void testErrorHandlingRanget1To10OnMonoErrorAndError() {
        Mono<Integer> s = Mono.just(5)
            .map(this::doubleElement)
            .map(this::doSecondTransformWithError);
        s.subscribe(value -> System.out.println("RECEIVED " + value),
                error -> System.err.println("CAUGHT " + error)
            );
    }


    @Test
    @DisplayName("testErrorHandlingRanget1To10OnErrorContinueAndError")
    void testErrorHandlingRanget1To10OnMonoErrorAndErrorWithMonoMethod() {
        Mono<Integer> s = Mono.just(5)
            .map(this::doubleElement)
            .flatMap(this::doSecondTransformWithErrorMono);
        s.subscribe(value -> System.out.println("RECEIVED " + value),
            error -> System.err.println("CAUGHT " + error)
        );
    }

    @Test
    @DisplayName("testErrorHandlingRanget1To10OnErrorContinueAndError")
    void testErrorHandlingRanget1To10OnMonoErrorAndErrorWithErrorMonoMethodErrorContinue() {
        Mono<Integer> s = Mono.just(5)
            .map(this::doubleElement)
            .flatMap(this::doSecondTransformWithErrorMono);
        s.onErrorContinue((throwable, o) -> System.out.println("onErrorContinue: " + o))
            .subscribe(value -> System.out.println("RECEIVED " + value),
                error -> System.err.println("CAUGHT " + error)
            );
    }

    @Test
    @DisplayName("testErrorHandlingRanget1To10OnErrorContinueAndError")
    void testErrorHandlingRanget1To10OnMonoErrorAndErrorWithMonoErrorMethodErrorContinue() {
        Mono<Integer> s = Mono.just(5)
            .map(this::doubleElement)
            .flatMap(this::doSecondTransformWithMonoError);
        s.onErrorContinue((throwable, o) -> System.out.println("onErrorContinue: " + o))
            .subscribe(value -> System.out.println("RECEIVED " + value),
            error -> System.err.println("CAUGHT " + error)
        );
    }

    @Test
    @DisplayName("testErrorHandlingRanget1To10OnErrorContinueAndError")
    void testErrorHandlingRanget1To10OnMonoErrorAndErrorWithMonoErrorMethod() {
        Mono<Integer> s = Mono.just(5)
            .map(this::doubleElement)
            .flatMap(this::doSecondTransformWithMonoError);
        s.subscribe(value -> System.out.println("RECEIVED " + value),
            error -> System.err.println("CAUGHT " + error)
        );
    }

    private Integer doSecondTransformWithError(Integer v) {
        if (v == 25) {
            throw new NumberFormatException("Error number");
        }
        return v -1;
    }

    private Mono<Integer> doSecondTransformWithMonoError(Integer v) {
        if (v == 25) {
            return Mono.error(new NumberFormatException("Error number"));
        }
        return Mono.just(v -1);
    }

    private Mono<Integer> doSecondTransformWithErrorMono(Integer v) {
        if (v == 25) {
            throw new NumberFormatException("Error number");
        }
        return Mono.just(v -1);
    }

    private Integer doubleElement(Integer v) {
        return v * v;
    }



    private void processMonoWithError(String name) {
        Mono.just(name).map(String::toUpperCase).doOnNext(s -> {throw new IllegalArgumentException();}).subscribe();
    }

    private void processMonoWithErrorHandling(String name) {
        Mono.just(name).map(String::toUpperCase).doOnNext(s -> {throw new IllegalArgumentException();}).subscribe(
            s -> System.out.println("[processMonoWithErrorHandling] name: " + name),
            throwable -> System.out.println("Error handling: " + throwable));
    }

    private void processMonoWithErrorErrorContinue(String name) {
        Mono.just(name).map(String::toUpperCase).doOnNext(s -> {throw new IllegalArgumentException();})
            .onErrorContinue((throwable, o) -> System.out.println("processMonoWithError error: " + o))
            .subscribe();
    }


    private Mono<String> monoWithError(String name) {
        return Mono.just(name).map(String::toUpperCase).doOnNext(s -> {throw new IllegalArgumentException();});
    }

    private void processMono(String name) {
        Mono.just(name).map(String::toUpperCase).doOnNext(s -> System.out.println("Name in upper case: " + s)).subscribe();
    }

    private Mono<String> getNewName() {
        return Mono.just("Juan").map(String::toUpperCase);
    }

}
