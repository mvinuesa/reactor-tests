package mvinuesa.functional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestFunctional {


    @Test
    @DisplayName("testFunction")
    void testFunction() {

        var optional = Optional.of(1).map(integer -> 1 + integer);
        assertEquals(2, optional.orElse(0));
    }

    @Test
    @DisplayName("testFunction2")
    void testFunction2() {

        var optional = Optional.of(1).map(add3());
        assertEquals(1, optional.orElse(0));
    }

    @Test
    @DisplayName("testFunction3")
    void testFunction3() {

        Function<Integer, Integer> add2 = integer -> 2 + integer;
        var optional = Optional.of(1).map(add2);

        assertEquals(3, optional.orElse(0));
    }

    @Test
    @DisplayName("testFunction4")
    void testFunction4() {

        UnaryOperator<Integer> add2 = integer -> 2 + integer;
        var optional = Optional.of(1).map(add2);

        assertEquals(3, optional.orElse(0));
    }

    private Function<Integer, Integer> add3() {
        return integer -> 3 + integer;
    }
}
