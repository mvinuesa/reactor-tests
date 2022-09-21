package mvinuesa.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestStream {

    @Test
    @DisplayName("test stream reduce")
    void testStreamReduce() {
        List<String> valueList = new ArrayList<>();
        valueList.add("Joe");
        valueList.add("John");
        valueList.add("Sean");
        valueList.add("Joe");
        valueList.add("John");
        valueList.add("Sean 2");
        valueList.add("Sean 3");
        valueList.add("John");

        Stream<String> stream = valueList.stream();
        Optional<String> value = stream
            .filter(s -> s.startsWith("Sean"))
            .reduce((first, second) -> first + second);

        System.out.println("RESULT " + value.orElse(null));
    }

    @Test
    @DisplayName("test stream skip")
    void testStreamSkip() {
        List<String> valueList = new ArrayList<>();
        valueList.add("Joe");
        valueList.add("John");
        valueList.add("Sean");
        valueList.add("Joe");
        valueList.add("John");
        valueList.add("Sean 2");
        valueList.add("Sean 3");
        valueList.add("John");

        long count = valueList.size();
        Stream<String> stream = valueList.stream();

        String value = stream.skip(count - 1).findFirst().get();

        System.out.println("RESULT " + value);
    }

}
